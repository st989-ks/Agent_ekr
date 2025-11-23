package com.application.agent_ekr.lmm.managers

sealed class GigaChatApiException(
    message: String,
    cause: Throwable? = null
): Exception(message, cause) {

    class Authentication(
        cause: Throwable? = null
    ) : GigaChatApiException("Authentication failed", cause)

    class TokenExpired(
        cause: Throwable? = null
    ) : GigaChatApiException("Token has expired", cause)

    class Models(
        cause: Throwable? = null
    ) : GigaChatApiException("Models empty", cause)

    class ValidationException(
        val errors: Map<String, String> = emptyMap(),
    cause: Throwable? = null
    ) : GigaChatApiException("Validation failed", cause)

    class ModelNotFoundException(
        val modelId: String,
        cause: Throwable? = null
    ) : GigaChatApiException("Model '$modelId' not found", cause)

    class ProcessingException(
        val statusCode: Int? = null,
        cause: Throwable? = null
    ) : GigaChatApiException("Request processing failed status statusCode=$statusCode", cause)

    class RateLimitException(
        val retryAfter: Long? = null,
        cause: Throwable? = null
    ) : GigaChatApiException("Too many requests retryAfter=$retryAfter", cause)

    class ServerException(
        cause: Throwable? = null
    ) : GigaChatApiException("Internal server error 500", cause)
}