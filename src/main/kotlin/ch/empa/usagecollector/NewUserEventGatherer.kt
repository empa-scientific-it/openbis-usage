package ch.empa.usagecollector

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.person.fetchoptions.PersonFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.person.search.PersonSearchCriteria
import kotlinx.datetime.LocalDate

class NewUserEventGatherer(override val api: IApplicationServerApi, override val startDate: LocalDate, override val endDate: LocalDate): IEventGatherer {
    override fun gather(token: String): List<InstanceEvent> {
        val searchCriteria = PersonSearchCriteria().apply {
            withAndOperator()
        }
        val fetchOptions = PersonFetchOptions().apply {
            withSpace()
        }
        val result = api.searchPersons(token,  searchCriteria, fetchOptions)
        return result.objects.filter{ filterRegistrationDate(it, startDate, endDate) }.map{ it-> NewPersonEvent(DateUtils.toDateTime(it.registrationDate), it?.space?.code ?: "none")}
    }
}