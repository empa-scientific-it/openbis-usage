package ch.empa.usagecollector.auth
import org.jetbrains.kotlinx.dataframe.*
import org.jetbrains.kotlinx.dataframe.io.*
import java.io.File

import org.jetbrains.kotlinx.dataframe.annotations.*
import org.jetbrains.kotlinx.dataframe.api.*

@DataSchema
data class KeePassEntry(
    @ColumnName("Account") val account: String,
    @ColumnName("Username") val username: String,
    @ColumnName("Password") val password: String,
    @ColumnName("Web Site") val url: String
)

fun importCredentials(f: File,  filt: String = "openbis"): CredentialsList{
    val df = DataFrame.readCSV(f, header = listOf("Account", "Username", "Password", "Web Site", "Comments")).filter {  it["Web Site"] != null && it["Password"] != null }
    val creds=  df.cast<KeePassEntry>().toList().filter{it.url.contains(filt)}
    return CredentialsList(creds.map{GenericCredential.Credentials(it.url, it.username, it.password)})
}