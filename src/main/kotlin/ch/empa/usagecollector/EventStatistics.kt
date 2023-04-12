package ch.empa.usagecollector

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import java.io.File
import java.time.LocalDate
import org.jetbrains.kotlinx.dataframe.*
import org.jetbrains.kotlinx.dataframe.api.add
import org.jetbrains.kotlinx.dataframe.api.insert
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.writeCSV

@Serializable
data class AggregateEvent(val event: EventType, val space: String, val instance: String, val date: kotlinx.datetime.Instant, val count: Int)


data class AggregationKey(val event: EventType, val space: String, val instance: String)
@Serializable
class EventStatistics(val events: List<AggregateEvent>) {

    companion object{
        fun build(api: IApplicationServerApi, url: String, token: String, start: LocalDate, end: LocalDate, period: DatePeriod): EventStatistics{
            val ev = EventLog.buildLogBetween(api, url, token, start.toKotlinLocalDate(), end.toKotlinLocalDate().plus(period))
            val stats = (start.datesUntil(end, period.toJavaPeriod())).toList().flatMap{
                val start = it.toKotlinLocalDate()
                val stop = it.plus(period.toJavaPeriod()).toKotlinLocalDate()
                val a = ev.events.filter{ currentEv -> currentEv.date.date in start..stop }.groupBy { evt -> AggregationKey(evt.eventCode, evt.space ?: "", evt.instance ?: "") }.map { (k, v) -> AggregateEvent(k.event, k.space, k.instance, start.atStartOfDayIn(
                    TimeZone.currentSystemDefault()), v.count()) }
                a
            }
            println(stats)
            return EventStatistics(stats.sortedBy { it.date })
        }
    }

    fun writeCSV(f: File){
        events.toDataFrame()
    }

    fun toDf(): DataFrame<AggregateEvent>{
        return events.toDataFrame()
    }

}