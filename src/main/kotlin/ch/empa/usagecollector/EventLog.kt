package ch.empa.usagecollector

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlinx.datetime.*

@Serializable
class EventLog(val events: List<InstanceEvent>) {
    companion object{
        fun buildLogBetween(api: IApplicationServerApi, url: String, token: String, start: LocalDate, end: LocalDate): EventLog{
            val dsEvents = NewDatasetEventGatherer(api, url, start, end)
            val objEvents = NewObjectEventGatherer(api, url, start, end)
            val perEvents = NewUserEventGatherer(api, url, start, end)
            val projEvent = NewProjectEvenGatherer(api, url, start, end)
            return  EventLog(listOf(dsEvents, objEvents, perEvents, projEvent).flatMap { it.gather(token) }.sortedBy {  it.date } )
        }
    }

    fun write(path: File): Unit{
        val format = Json { prettyPrint = false }
        path.writer().use {
            events.map { ev -> it.write(format.encodeToString(ev) + "\n") }
        }
    }
}