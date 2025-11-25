package com.application.agent_ekr.console

/**
 * CommandResult represents the outcome of a command execution.
 * 
 * This sealed class provides different result types for command execution,
 * including output messages, success, exit, errors, and unknown commands.
 */
sealed class CommandResult {
    /**
     * Output a message to the console
     * 
     * @property message The message to display
     */
    data class Output(val message: String) : CommandResult()
    
    /**
     * Command succeeded silently (no output needed)
     */
    object Success : CommandResult()
    
    /**
     * Exit the application
     */
    object Exit : CommandResult()
    
    /**
     * Command execution resulted in an error
     * 
     * @property message The error message to display
     */
    data class Error(val message: String) : CommandResult()
    
    /**
     * Unknown command was entered
     * 
     * @property command The unknown command name
     */
    data class Unknown(val command: String) : CommandResult()
}
