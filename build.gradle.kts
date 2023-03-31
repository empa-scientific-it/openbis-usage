plugins {

    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin)

    alias(libs.plugins.dockerCompose)

    alias(libs.plugins.fatJar)

    //Serialisation
    alias(libs.plugins.kotlin.serialization)

    // Apply the application plugin to add support for building a CLI application in Java.
    application

    id("maven-publish")
}

group = "ch.empa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    ivy {
        url = uri("https://sissource.ethz.ch/openbis/openbis-public/openbis-ivy/-/raw/main/")
        patternLayout {

            artifact("[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]")
            ivy("[organisation]/[module]/[revision]/ivy.xml")
        }
}

dependencies {
    testImplementation(kotlin("test"))
    // Align versions of all Kotlin components
    implementation(libs.kotlin.bom)
    // Use the Kotlin JDK 8 standard library.
    implementation(libs.kotlin.stdlib)

    // Use the Kotlin test library.
    implementation(libs.kotlin.test)
    // Use the Kotlin JUnit integration.
    implementation(libs.kotlin.test.junit)

    //Openbis v3 API
    implementation(libs.openbis)
    implementation(libs.openbis.common)
    implementation(libs.openbis.commonbase)
    implementation(libs.kotlinx.serialization)

    //Jackson
    implementation(libs.jackson.databind)

    //Email validation
    implementation(libs.jakarta.mail)

    //Command line
    implementation(libs.kotlinx.cli)

    }





}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(15)
}

application {
    mainClass.set("MainKt")
}