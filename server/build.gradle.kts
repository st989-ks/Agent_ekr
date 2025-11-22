plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}

group = "com.application.agent_ekr"
version = "1.0.0"
application {
    mainClass.set("com.application.agent_ekr.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktorServerCore)
    implementation(libs.ktorServerNetty)

    implementation(libs.ktorClientCore)
    implementation(libs.ktorClientCio)
    implementation(libs.ktorSerializationKotlinxJson)
    implementation(libs.ktorClientContentNegotiation)
    implementation(libs.ktorClientLogging)
    implementation(libs.ktorClientSerialization)
    implementation(libs.kotlinxSerializationJson)

    testImplementation(libs.ktorServerTestHost)
    testImplementation(libs.kotlinTestJunit)
}