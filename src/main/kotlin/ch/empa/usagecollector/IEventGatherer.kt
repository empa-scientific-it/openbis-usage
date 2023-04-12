package ch.empa.usagecollector;

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi;
import kotlinx.datetime.LocalDate

interface IEventGatherer {
    val api: IApplicationServerApi
    val url: String
    val startDate: LocalDate
    val endDate: LocalDate
    fun gather(token: String): List<InstanceEvent>
}
