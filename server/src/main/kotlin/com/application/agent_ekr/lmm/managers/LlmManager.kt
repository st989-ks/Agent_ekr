package com.application.agent_ekr.lmm.managers

interface LlmManager {

    fun countTokens(input: List<String>, model: String): Int

    fun checkAI(text: String, model: String): Boolean

    fun generateEmbeddings(input: List<String>): List<DoubleArray>

    fun uploadFile(file: ByteArray): String

    fun deleteFile(fileId: String): Boolean

    fun validateFunction(functionDescription: Map<String, Any>): Boolean

    fun processChatCompletions(messages: List<Map<String, Any>>, model: String): String

    fun getAvailableModels(): List<String>

    fun getFileInfo(fileId: String): Map<String, Any>

    fun getFileContent(fileId: String): ByteArray

    fun monitorTokenConsumption(model: String): Int

    fun getAccountBalance(): Double

    fun submitBatchJob(batchData: ByteArray): String

    fun getBatchJobStatus(jobId: String): Map<String, Any>

    fun getMessageStream(message:String)
}