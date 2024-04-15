import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

var hadError = false

// page 47
fun main(args: Array<String>) {
    if (args.size > 1) {
        println("Usage: glorb [script]")
        exitProcess(64)
    } else if (args.size == 1) {
        runFile(args[0])
    } else {
        runPrompt()
    }
}

fun err(line: Int, message: String) {
    System.err.println("Error [line $line] $message")
}

private fun runFile(path: String) {
    val bytes = Files.readAllBytes(Paths.get(path))
    run(String(bytes, Charset.defaultCharset()))

    if (hadError) {
        exitProcess(65)
    }
}

private fun runPrompt() {
    val input = InputStreamReader(System.`in`)
    val reader = BufferedReader(input)

    while (true) {
        print("> ")
        val line = reader.readLine() ?: break

        run(line)
        hadError = false
    }
}

private fun run(source: String) {
    val lexer = Lexer(source)
    val tokens = lexer.scanTokens()

    for (token in tokens) {
        println(token)
    }
}


