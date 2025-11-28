package com.application.agent_ekr.console

import com.application.agent_ekr.tools.mcp.MCPServers
import com.application.agent_ekr.tools.mcp.UniversalMCPClient
import org.slf4j.Logger

/**
 * ConsoleCommandHandler processes user commands in the console application.
 *
 * This class handles command parsing, routing, and execution with proper
 * error handling and history tracking.
 *
 * @property logger The logger instance for debugging and error logging
 * @property config The console UI configuration
 */
class ConsoleCommandHandler(
    private val logger: Logger,
    private var config: ConsoleUIConfig
) {
    private val commandHistory = mutableListOf<String>()

    /**
     * Handle a command input string and return the appropriate result.
     *
     * This method parses the input, extracts the command name and arguments,
     * routes to the appropriate handler, and returns the execution result.
     *
     * @param input The raw command input (must start with "/")
     * @return CommandResult indicating the outcome of command execution
     *
     * Example:
     * ```kotlin
     * val result = handler.handleCommand("/help")
     * when (result) {
     *     is CommandResult.Output -> println(result.message)
     *     is CommandResult.Success -> // Command succeeded silently
     *     is CommandResult.Exit -> // Exit the application
     *     is CommandResult.Error -> println("Error: ${result.message}")
     *     is CommandResult.Unknown -> println("Unknown command: ${result.command}")
     * }
     * ```
     */
    suspend fun handleCommand(input: String): CommandResult {
        // Store command in history
        commandHistory.add(input)

        // Parse input: remove "/" prefix, split on first space
        val trimmedInput = input.trim()
        if (!trimmedInput.startsWith("/")) {
            return CommandResult.Error("Command must start with '/'")
        }

        val withoutSlash = trimmedInput.substring(1)
        val parts = withoutSlash.split("\\s+".toRegex(), limit = 2)
        val commandName = parts[0].lowercase()
        val arguments = parts.getOrNull(1) ?: ""

        // Route to appropriate handler
        return when (commandName) {
            "test" -> handleTest(arguments)
            "help" -> handleHelp()
            "debug" -> handleDebug(arguments)
            "clear" -> handleClear()
            "history" -> handleHistory()
            "exit" -> handleExit()
            else -> CommandResult.Unknown(commandName)
        }
    }

    /**
     * Handle the help command - show list of all available commands
     *
     * @return CommandResult.Output with formatted help message
     */
    private fun handleHelp(): CommandResult {
        val helpText = """
            |${ConsoleStyler.command("/test")} — test api experimental
            |${ConsoleStyler.command("/help")} — show this help message
            |${ConsoleStyler.command("/debug")} [on|off|true|false|1|0] — toggle debug mode
            |${ConsoleStyler.command("/clear")} — clear the screen
            |${ConsoleStyler.command("/history")} — show command history
            |${ConsoleStyler.command("/exit")} — exit the application
        """.trimMargin()
        return CommandResult.Output(helpText)
    }

    private suspend fun handleTest(arguments: String): CommandResult {
//        TODO("""
//            To implement extensible tests by the type of call was /test,
//             we got here, and then any names for the tests will be implemented,
//              for example mcptools, that is, the user will enter "/test mcptools"
//               and some experimental work will be performed""")

        return when (arguments) {
            "tools" -> {
                val client = UniversalMCPClient(MCPServers.github())
                CommandResult.Output(client.getTools().joinToString("\n"))
            }

            else -> CommandResult.Output("TODO()")
        }
    }

    /**
     * Handle the debug command - toggle debug mode
     *
     * @param args The debug arguments (on/off/true/false/1/0)
     * @return CommandResult.Output with success message or CommandResult.Error on failure
     */
    private fun handleDebug(args: String): CommandResult {
        val debugArg = args.trim().lowercase()
        val newDebugMode = when (debugArg) {
            "on", "true", "1" -> true
            "off", "false", "0" -> false
            else -> return CommandResult.Error("Invalid debug argument: '$args'. Use: on/off/true/false/1/0")
        }

        // Update config (create new instance since data class is immutable)
        config = config.copy(debugMode = newDebugMode)
        logger.info("Debug mode set to: $newDebugMode")

        val statusMessage = if (newDebugMode) "enabled" else "disabled"
        return CommandResult.Output(ConsoleStyler.success("Debug mode $statusMessage"))
    }

    /**
     * Handle the clear command - clear the screen
     *
     * @return CommandResult.Success (no message needed)
     */
    private fun handleClear(): CommandResult {
        print("\u001B[2J\u001B[H")
        return CommandResult.Success
    }

    /**
     * Handle the history command - show command history
     *
     * @return CommandResult.Output with formatted history or "No history" message
     */
    private fun handleHistory(): CommandResult {
        if (commandHistory.isEmpty()) {
            return CommandResult.Output("No history")
        }

        val historyText = commandHistory.mapIndexed { index, command ->
            "  $index. $command"
        }.joinToString("\n")

        return CommandResult.Output(historyText)
    }

    /**
     * Handle the exit command - exit the application
     *
     * @return CommandResult.Exit
     */
    private fun handleExit(): CommandResult {
        return CommandResult.Exit
    }
}
