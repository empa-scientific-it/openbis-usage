package ch.empa.usagecollector

import ch.empa.usagecollector.auth.importCredentials
import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.datetime.*


import java.io.File
import jetbrains.datalore.plot.PlotSvgExport
import org.jetbrains.kotlinx.dataframe.api.toMap
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.facet.facetWrap
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.intern.toSpec
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.scale.scaleYLog10

fun getStatistics(
    ob: IApplicationServerApi,
    url: String,
    token: String,
    span: DatePeriod = DatePeriod(years = 4)
): EventStatistics {
    val now: Instant = Clock.System.now()
    val end: LocalDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val start = end.minus(span)
    return EventStatistics.build(ob, url, token, start.toJavaLocalDate(), end.toJavaLocalDate(), DatePeriod(days = 1))
}


fun main(args: Array<String>) {
    val credentials = importCredentials(File(args[0]))
    val stats = runBlocking {
        val coros = CoroutineScope(IO).async {

            val res = credentials.credentials.map {
                val ob = it.getService()
                val token = it.getToken()
                getStatistics(ob, it.url, token)
            }
            return@async res

        }
        coros.await()

    }
    val res = EventStatistics(stats.flatMap { it.events })
    val p = letsPlot(res.toDf().toMap()) + geomLine{x="date"; y="count"} + facetWrap("event") + scaleYLog10()
    ggsave(p, "./plot.svg")
    res.writeCSV(File(args[1]))
}