package ch.empa.usagecollector.auth

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.systemsx.cisd.common.spring.HttpInvokerUtils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator


@Serializable

sealed class GenericCredential {
    abstract val url: String
    abstract val anonymous: Boolean
    @Serializable
    @SerialName("password")
    class Credentials(
        override val url: String,
        val username: String,
        val password: String
    ) : GenericCredential() {
        override val anonymous: Boolean = false
        override fun getToken(): String {
            val serv = getService()
            println(url)
            println(password)
            return serv.login(username, password)
        }
    }

    @Serializable
    @SerialName("anonymous")
    class AnonymousCredentials(override val url: String) : GenericCredential() {
        override val anonymous: Boolean = true
        override fun getToken(): String {
            val serv = getService()
            return serv.loginAsAnonymousUser()
        }
    }

    fun getService(): IApplicationServerApi {
        val OPENBIS_URL = url + "/openbis/openbis" + IApplicationServerApi.SERVICE_URL
        val ob = HttpInvokerUtils.createServiceStub(IApplicationServerApi::class.java, OPENBIS_URL, 1000)
        return ob
    }

    abstract fun getToken(): String
}


@Serializable
data class CredentialsList(val credentials: List<GenericCredential>)


