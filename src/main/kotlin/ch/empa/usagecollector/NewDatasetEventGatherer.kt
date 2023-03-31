package ch.empa.usagecollector

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.fetchoptions.DataSetFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.search.DataSetSearchCriteria
import kotlinx.datetime.LocalDate

class NewDatasetEventGatherer(override val api: IApplicationServerApi, override val startDate: LocalDate, override val endDate: LocalDate): IEventGatherer {
    override fun gather(token: String): List<InstanceEvent> {
        val searchCriteria = DataSetSearchCriteria().apply {
            withAndOperator().apply {
                withRegistrationDate().thatIsLaterThan(DateUtils.toJavaDate(startDate))
                withRegistrationDate().thatIsEarlierThan(DateUtils.toJavaDate(endDate))
            }

        }
        val fetchOptions = DataSetFetchOptions().apply {
            withExperiment().apply {
                withProject().apply {
                    withSpace()
                }
            }
            withRegistrator()
            withSample().apply {
                withSpace()
            }
            withPhysicalData()
        }
        val results = api.searchDataSets(token, searchCriteria, fetchOptions)
        val res = results.objects.map {
            NewDatasetEvent(DateUtils.toDateTime(it.registrationDate), it?.sample?.space?.code ?: it?.experiment?.project?.space?.code ?: "something",  it.physicalData.size)
        }
        return res
    }
}