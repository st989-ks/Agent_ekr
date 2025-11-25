package com.application.agent_ekr.console

/**
 * ConsoleStyler provides ANSI formatting methods for console output.
 * 
 * This object contains utilities for styling console text using ANSI escape codes,
 * including colors, formatting, and structured message blocks.
 */
object ConsoleStyler {
    // ANSI escape codes
    private const val RESET = "\u001B[0m"
    private const val BOLD = "\u001B[1m"
    private const val DIM = "\u001B[2m"
    private const val VERY_DIM = "\u001B[38;5;240m"
    
    // Regular colors
    private const val RED = "\u001B[31m"
    private const val GREEN = "\u001B[32m"
    private const val YELLOW = "\u001B[33m"
    private const val BLUE = "\u001B[34m"
    private const val CYAN = "\u001B[36m"
    private const val GRAY = "\u001B[90m"
    
    // Bright colors
    private const val BRIGHT_RED = "\u001B[91m"
    private const val BRIGHT_GREEN = "\u001B[92m"
    private const val BRIGHT_YELLOW = "\u001B[93m"
    private const val BRIGHT_BLUE = "\u001B[94m"
    private const val BRIGHT_CYAN = "\u001B[96m"
    
    // Backgrounds
    private const val BG_BRIGHT_BLUE = "\u001B[104m"
    
    // Symbols
    private const val CHECKMARK = "✓"
    private const val CROSS = "✗"
    private const val WARNING = "⚠"
    private const val INFO = "ℹ"
    
    /**
     * Formats user message with bold text on bright blue background.
     * 
     * Example: ConsoleStyler.userMessage("Hello") -> bold white text on bright blue background
     * 
     * @param text The message text to format
     * @return Formatted string ready for console output
     */
    fun userMessage(text: String): String = 
        "$BOLD$BG_BRIGHT_BLUE$text$RESET"
    
    /**
     * Formats AI message with bold bright cyan text.
     * 
     * Example: ConsoleStyler.aiMessage("Hello") -> bold bright cyan text
     * 
     * @param text The message text to format
     * @return Formatted string ready for console output
     */
    fun aiMessage(text: String): String = 
        "$BOLD$BRIGHT_CYAN$text$RESET"
    
    /**
     * Formats success message with green color and checkmark prefix.
     * 
     * Example: ConsoleStyler.success("Operation completed") -> "✓ Operation completed" in green
     * 
     * @param text The message text to format
     * @return Formatted string ready for console output
     */
    fun success(text: String): String = 
        "$GREEN$CHECKMARK $text$RESET"
    
    /**
     * Formats error message with red color and cross prefix.
     * 
     * Example: ConsoleStyler.error("Operation failed") -> "✗ Operation failed" in red
     * 
     * @param text The message text to format
     * @return Formatted string ready for console output
     */
    fun error(text: String): String = 
        "$RED$CROSS $text$RESET"
    
    /**
     * Formats warning message with yellow color and warning symbol prefix.
     * 
     * Example: ConsoleStyler.warning("This is a warning") -> "⚠ This is a warning" in yellow
     * 
     * @param text The message text to format
     * @return Formatted string ready for console output
     */
    fun warning(text: String): String = 
        "$YELLOW$WARNING $text$RESET"
    
    /**
     * Formats info message with dim gray color and info symbol prefix.
     * 
     * Example: ConsoleStyler.info("This is information") -> "ℹ This is information" in dim gray
     * 
     * @param text The message text to format
     * @return Formatted string ready for console output
     */
    fun info(text: String): String = 
        "$DIM$GRAY$INFO $text$RESET"
    
    /**
     * Formats debug message with very dim text.
     * 
     * Example: ConsoleStyler.debug("Debug information") -> very dim text
     * 
     * @param text The message text to format
     * @return Formatted string ready for console output
     */
    fun debug(text: String): String = 
        "$VERY_DIM$text$RESET"
    
    /**
     * Creates a horizontal line separator.
     * 
     * Example: ConsoleStyler.separator() -> "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
     * 
     * @return Separator line string
     */
    fun separator(): String = 
        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    
    /**
     * Creates a bordered message block with title and content.
     * 
     * Example: 
     * ConsoleStyler.messageBlock("Title", "Content") -> 
     * ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
     * ┃ Title                                  ┃
     * ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
     * ┃ Content                               ┃
     * ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
     * 
     * @param title The block title
     * @param content The block content (can be multiline)
     * @return Formatted message block string
     */
    fun messageBlock(title: String, content: String): String {
        val width = 40
        val borderTop = "┏${"━".repeat(width)}┓"
        val borderMiddle = "┣${"━".repeat(width)}┫"
        val borderBottom = "┗${"━".repeat(width)}┛"
        
        val titleLine = "┃ ${title.padEnd(width - 2)} ┃"
        
        val contentLines = content.lines().flatMap { line ->
            line.chunked(width - 2).map { chunk ->
                "┃ ${chunk.padEnd(width - 2)} ┃"
            }
        }
        
        return listOf(
            borderTop,
            titleLine,
            borderMiddle
        ) + contentLines + listOf(borderBottom)
    }.joinToString("\n")
}
