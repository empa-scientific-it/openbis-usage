import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IRegistrationDateHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.ProjectFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.search.ProjectSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.service.CustomASServiceExecutionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.service.fetchoptions.CustomASServiceFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.service.search.CustomASServiceSearchCriteria
import ch.systemsx.cisd.common.spring.HttpInvokerUtils
import java.time.Period



fun main(args: Array<String>) {
    val URL = "http://openbis-empa-lab402.ethz.ch/openbis/openbis" + IApplicationServerApi.SERVICE_URL
    val ob = HttpInvokerUtils.createServiceStub(IApplicationServerApi::class.java, URL, 1000)
    val token = ob.login("basi_admin", "eiVo2lee")
    val crit = CustomASServiceSearchCriteria().withAndOperator()
    val fo = CustomASServiceFetchOptions()
    val res = ob. 	searchCustomASServices(token, crit, fo)
    val serviceOptions = CustomASServiceExecutionOptions().withParameter("method", "sendCountActiveUsersEmail").withParameter("params", 2)
    val projectSearchCriteria = ProjectSearchCriteria().withAndOperator()
    val fetchOptions = ProjectFetchOptions().apply {
        withAttachments()
        withHistory()
        withLeader()
        withExperiments().apply {
            withAttachments()
            withDataSets()
            withSamples()
        }
    }
    val result = ob.searchProjects(token, projectSearchCriteria, fetchOptions )
    print(result.objects.map{it -> it.registrationDate})
}