package ch.empa.usagecollector

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleSearchCriteria
import kotlinx.datetime.LocalDate

class NewObjectEventGatherer(override val api: IApplicationServerApi, override val url: String, override val startDate: LocalDate, override val endDate: LocalDate): IEventGatherer {

    override fun gather(token: String): List<InstanceEvent> {
        val searchCriteria = SampleSearchCriteria().apply {
            withAndOperator().apply {
                withRegistrationDate().thatIsLaterThan(DateUtils.toJavaDate(startDate))
                withRegistrationDate().thatIsEarlierThan(DateUtils.toJavaDate(endDate))
            }
        }
        val fetchOptions = SampleFetchOptions().apply {
            withSpace()
            withExperiment()
            withProject()
            withRegistrator().apply {
                withSpace()
            }
        }
        val result = api.searchSamples(token, searchCriteria, fetchOptions)
        return result.objects.map{it -> NewObjectEvent(DateUtils.toDateTime(it.registrationDate), it?.space?.code, it?.experiment?.code, url)}
    }
}