package ch.empa.usagecollector

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.poi.ss.formula.functions.Even
import java.io.File
import kotlinx.datetime.*

@Serializable
class EventLog(val events: List<InstanceEvent>) {
    companion object{
        fun buildBetween(api: IApplicationServerApi, token: String, start: LocalDate,  end: LocalDate): EventLog{
            val dsEvents = NewDatasetEventGatherer(api, start, end)
            val objEvents = NewObjectEventGatherer(api, start, end)
            val perEvents = NewUserEventGatherer(api, start, end)
            val projEvent = NewProjectEvenGatherer(api, start, end)
            return  EventLog(listOf<IEventGatherer>(dsEvents, objEvents, perEvents, projEvent).flatMap { it.gather(token) }.sortedBy {  it.date } )
        }
    }

    fun write(path: File): Unit{
        val format = Json { prettyPrint = false }
        path.writer().use {
            events.map { ev -> it.write(format.encodeToString(ev) + "\n") }
        }
    }
}