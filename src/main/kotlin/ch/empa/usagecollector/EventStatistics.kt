package ch.empa.usagecollector

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaPeriod
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.Serializable
import java.io.File
import java.time.LocalDate


@Serializable
data class AggregateEvent(val event: EventType, val date: kotlinx.datetime.LocalDate, val count: Int)

@Serializable
class EventStatistics(val events: List<AggregateEvent>) {

    companion object{
        fun build(api: IApplicationServerApi, token: String, start: LocalDate, end: LocalDate, period: DatePeriod): EventStatistics{
            val ev = EventLog.buildBetween(api, token, start.toKotlinLocalDate(), end.toKotlinLocalDate().plus(period))
            val stats = (start.datesUntil(end, period.toJavaPeriod())).toList().flatMap{
                val start = it.toKotlinLocalDate()
                val stop = it.plus(period.toJavaPeriod()).toKotlinLocalDate()
                val a = ev.events.filter{ currentEv -> currentEv.date.date in start..stop }.groupBy { evt -> evt.eventCode}.map { (k, v) -> AggregateEvent(k, start, v.count()) }
                a
            }
            return EventStatistics(stats)
        }
    }

    fun writeCSV(f: File){

    }
}