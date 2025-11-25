package com.application.agent_ekr.console

/**
 * ConsoleUIConfig manages console behavior and display settings.
 *
 * This data class provides configuration options for controlling console output,
 * including debug information, API logs, and color usage.
 */
data class ConsoleUIConfig(
    /**
     * Enable debug information display in console
     */
    val debugMode: Boolean = false,

    /**
     * Show API request/response logs
     */
    val showApiLogs: Boolean = false,

    /**
     * Enable ANSI color codes in console output
     */
    val useColors: Boolean = true,

    /**
     * Maximum number of characters for debug context output
     */
    val maxDebugContextLength: Int = 500
) {
    companion object {
        /**
         * Creates a production configuration with minimal output.
         *
         * Example usage:
         * ```kotlin
         * val config = ConsoleUIConfig.production()
         * ```
         *
         * @return ConsoleUIConfig with debugMode=false, showApiLogs=false, useColors=true
         */
        fun production(): ConsoleUIConfig = ConsoleUIConfig(
            debugMode = false,
            showApiLogs = false,
            useColors = true,
            maxDebugContextLength = 500
        )

        /**
         * Creates a development configuration with full debug output.
         *
         * Example usage:
         * ```kotlin
         * val config = ConsoleUIConfig.development()
         * ```
         *
         * @return ConsoleUIConfig with debugMode=true, showApiLogs=true, useColors=true
         */
        fun development(): ConsoleUIConfig = ConsoleUIConfig(
            debugMode = true,
            showApiLogs = true,
            useColors = true,
            maxDebugContextLength = 500
        )
    }
}
