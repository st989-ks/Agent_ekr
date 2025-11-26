plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
}

group = "com.application.agent_ekr"
version = "1.0.0"

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)

    implementation(libs.ktorClientCore)
    implementation(libs.ktorClientCio)
    implementation(libs.ktorSerializationKotlinxJson)
    implementation(libs.ktorClientContentNegotiation)
    implementation(libs.ktorClientLogging)
    implementation(libs.ktorClientSerialization)
    implementation(libs.kotlinxSerializationJson)

    implementation(libs.modelcontextprotocol)

    testImplementation(libs.ktorServerTestHost)
    testImplementation(libs.kotlinTestJunit)
}