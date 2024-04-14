import TokenType.*

class Lexer(val source: String) {
    val tokens = ArrayList<Token>()
    var start = 0
    var current = 0
    var line = 1

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
            '!' -> if (match('=')) BANG_EQUAL else BANG
            '=' -> if (match('=')) EQUAL_EQUAL else EQUAL
            '<' -> if (match('=')) LESS_EQUAL else LESS
            '>' -> if (match('=')) GREATER_EQUAL else GREATER
            else -> {
                err(line, "Unexpected character $at")
                null
            }
        }

        if (type !== null) {
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

    private fun advance(): Char {
        current++
        return source[current - 1]
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false

        current++
        return true
    }
}