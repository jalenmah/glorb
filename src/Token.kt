class Token(
    private val type: TokenType,
    private val lexeme: String,
    private var literal: Any?,
    private var line: Int
) {
    override fun toString(): String {
        return "{ type = $type, lexeme = $lexeme }"
    }
}