package com.application.agent_ekr

import kotlin.coroutines.cancellation.CancellationException


object Utils {

    inline fun <T, R> T.runCatchingSuspend(block: T.() -> R): Result<R> {
        return try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw  e
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
}