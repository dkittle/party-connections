package ca.kittle.util

import kotlinx.coroutines.*

/**
 * Executes a use case defined by the provided lambda expression [block] in a suspendable manner.
 * This method is intended to be used within the containing function to handle common logic for executing use cases.
 * The [block] represents the code that performs the use case logic and returns a result of type [T].
 * This method uses the supervisorScope to create a new coroutine scope that can handle exceptions and cancellation.
 * After the [block] completes or throws an exception, this method cancels any active child coroutines to ensure proper cleanup.
 *
 * @param block the lambda expression representing the use case logic to be executed.
 * @return the result of executing the use case.
 */
suspend fun <T> executeUseCase(block: suspend () -> T): T = supervisorScope {
    try {
        block()
    } finally {
        // If any of the lazy coroutines are not started or completed, cancel them
        // Without this, the supervisorScope will never exit if one of the coroutines was never started
        this.coroutineContext.cancelChildren()
    }
}

/**
 * Executes a suspend block of code and returns the result.
 * It uses CoroutineScope so the first exception will short-curcuit the rest of the lambda.
 *
 * @param block The suspend block of code to be executed.
 * @return The result of executing the suspend block of code.
 */
suspend fun <T> useCaseResponse(block: suspend () -> T): T = coroutineScope {
    block()
}

/**
 * Wrappers for Deferred jobs. Taken from
 * https://github.com/Kotlin/kotlinx.coroutines/issues/2130
 * SalomonBrys' suggestions on how to clean up await() for multiple results.
 */
@ExperimentalCoroutinesApi
suspend fun <T> Deferred<T>.awaitResult(): Result<T> {
    join()
    return runCatching { getCompleted() }
}

@ExperimentalCoroutinesApi
suspend fun <T : Any> Deferred<T>.awaitOrNull(): T? =
    awaitResult().getOrNull()

object Maybe {
    suspend operator fun <T> invoke(block: suspend () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
