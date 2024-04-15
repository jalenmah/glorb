import TokenType.*

class Lexer(private val source: String) {
    private val tokens = ArrayList<Token>()
    private var start = 0
    private var current = 0
    private var line = 1

    fun scanTokens(): List<Token> {
        println("Scanning..")
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        println("Scan completed")
        tokens.add(Token(EOF, "", null, line))
        return tokens
    }

    private fun scanToken() {
        val type = when (val at = advance()) {
            // Single-line characters
            '(' -> LEFT_PAREN
            ')' -> RIGHT_PAREN
            '{' -> LEFT_BRACE
            '}' -> RIGHT_BRACE
            ',' -> COMMA
            '.' -> DOT
            '-' -> MINUS
            '+' -> PLUS
            ';' -> SEMICOLON
            '*' -> STAR

            // Multi-line characters
            '!' -> if (match('=')) BANG_EQUAL else BANG
            '=' -> if (match('=')) EQUAL_EQUAL else EQUAL
            '<' -> if (match('=')) LESS_EQUAL else LESS
            '>' -> if (match('=')) GREATER_EQUAL else GREATER
            '/' -> if (match('/')) {
                while (peek() != '\n' && !isAtEnd()) advance()
                null
            } else SLASH

            // Literals
            '"' -> {
                buildStringLiteral()
                null
            }

            // Escape characters
            ' ' -> null
            '\r' -> null
            '\t' -> null
            '\n' -> {
                line++
                null
            }

            else -> {
                err(line, "Unexpected character '$at' at $current")
                null
            }
        }

        if (type != null) {
            addToken(type)
        }
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any?) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    // Consumes current character & moves to next
    private fun advance(): Char {
        current++
        return source[current - 1]
    }

    // Advances if next character matches
    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false

        current++
        return true
    }

    private fun peek(): Char {
        if (isAtEnd()) return '\u0000'
        return source[current]
    }

    private fun buildStringLiteral() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }

        if (isAtEnd()) {
            err(line, "Unterminated string")
            return
        }

        advance()

        // Take the value inside the string quotations
        val value = source.substring(start + 1, current - 1)
        addToken(STRING, value)
    }
}