package ch.empa.usagecollector

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.person.fetchoptions.PersonFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.person.search.PersonSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.ProjectFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.search.ProjectSearchCriteria
import kotlinx.datetime.LocalDate

class NewProjectEvenGatherer(override val api: IApplicationServerApi,
                             override val url: String, override val startDate: LocalDate, override val endDate: LocalDate): IEventGatherer {
    override fun gather(token: String): List<InstanceEvent> {
        val searchCriteria = ProjectSearchCriteria()
        val fetchOptions = ProjectFetchOptions().apply {
            withSpace()
            withRegistrator().apply {
                withSpace()
            }
        }
        val result = api.searchProjects(token,  searchCriteria, fetchOptions)
        return result.objects.filter{it.registrationDate.after(DateUtils.toJavaDate(startDate)) && it.registrationDate.before(DateUtils.toJavaDate(endDate))}.map{ it-> NewProjectEvent(DateUtils.toDateTime(it.registrationDate), it?.space?.code ?: "none", url)}
    }
}