class Token(
    private val type: TokenType,
    private val lexeme: String,
    private var literal: Any?,
    private var line: Int
) {
    override fun toString(): String {
        return "Token { type = $type, lexeme = $lexeme${if (literal != null) ", literal = $literal" else ""} }"
    }
}