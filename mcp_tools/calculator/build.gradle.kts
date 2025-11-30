plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}


val buildVersion = project.findProperty("buildVersion").toString()
val isDevelopment = project.hasProperty("isDevelopment")
val buildPackage = "com.application.agent_ekr.mcp_tools.calculator"

val outputBuildDir = layout.buildDirectory.dir("generated/buildconfig")

group = buildPackage
version = buildVersion

application {
    mainClass.set("$buildPackage.MainKt")

    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.logback)

    implementation(libs.ktorServerCio)
    implementation(libs.ktorServerCors)
    implementation(libs.ktorSerializationKotlinxJson)
    implementation(libs.kotlinxSerializationJson)

    implementation(libs.modelcontextprotocol)

}

abstract class GenerateBuildConfigTask : DefaultTask() {
    @get:Input
    abstract val buildVersionProp: Property<String>

    @get:Input
    abstract val developmentMode: Property<Boolean>

    @get:Input
    abstract val buildPackageProp: Property<String>

    @get:OutputDirectory
    abstract val outputDirProp: DirectoryProperty

    @TaskAction
    fun generate() {
        val file = outputDirProp.get().asFile.resolve("BuildConfig.kt")
        file.parentFile.mkdirs()

        val content = """
            package ${buildPackageProp.get()}

            object BuildConfig {
                const val VERSION = "${buildVersionProp.get()}"
                const val IS_DEBUG = ${developmentMode.get()}
                const val BUILD_NUMBER = "${System.currentTimeMillis()}"
            }
        """.trimIndent()

        file.writeText(content)
    }
}

val generateBuildConfig by tasks.registering(GenerateBuildConfigTask::class) {
    buildVersionProp.set(buildVersion)
    developmentMode.set(isDevelopment)
    buildPackageProp.set(buildPackage)
    outputDirProp.set(outputBuildDir)
}

kotlin.sourceSets["main"].kotlin.srcDir(outputBuildDir)
tasks.named("compileKotlin") { dependsOn(generateBuildConfig) }