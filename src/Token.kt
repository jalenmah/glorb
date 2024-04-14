class Token(val type: TokenType, val lexeme: String, var literal: Any?, var line: Int) {
    override fun toString(): String {
        return "{ type = $type, lexeme = $lexeme }"
    }
}