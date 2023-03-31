package ch.empa.usagecollector

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.event.EventType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.event.fetchoptions.EventFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.event.search.EventSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.ProjectFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.search.ProjectSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.service.CustomASServiceExecutionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.service.fetchoptions.CustomASServiceFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.service.search.CustomASServiceSearchCriteria

import ch.systemsx.cisd.common.spring.HttpInvokerUtils
import kotlinx.datetime.*
import java.time.LocalDate

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.io.File

fun main(args: Array<String>) {
    val URL = "http://openbis-empa-lab402.ethz.ch/openbis/openbis" + IApplicationServerApi.SERVICE_URL
    val ob = HttpInvokerUtils.createServiceStub(IApplicationServerApi::class.java, URL, 1000)
    val token = ob.login("basi_admin", "eiVo2lee")
    val projectSearchCriteria = ProjectSearchCriteria().withAndOperator()
    val fetchOptions = ProjectFetchOptions().apply {
        withAttachments()
        withHistory()
        withLeader()
        withSpace()
        withExperiments().apply {
            withAttachments()
            withDataSets()
            withSamples()
        }
    }
    val now: Instant = Clock.System.now()
    val end: kotlinx.datetime.LocalDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val start = end.minus(DatePeriod(days = 2 * 365))
    val ev = EventLog.buildBetween(ob, token, start, end)
    val stat = EventStatistics.build(ob, token, start.toJavaLocalDate(), end.toJavaLocalDate(), DatePeriod(days = 1))
    println(stat)
    ev.write(File("./logs/events.log"))


}