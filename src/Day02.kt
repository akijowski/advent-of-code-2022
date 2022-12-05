data class Round(val opponent: String, val you: String) {

    // A, X => Rock
    // B, Y => Paper
    // C, Z => Scissors

    // value = winner
    private val defeats = mapOf(
        "A" to "Y",
        "B" to "Z",
        "C" to "X"
    )

    private val draws = mapOf(
        "A" to "X",
        "B" to "Y",
        "C" to "Z"
    )

    private val scores = mapOf(
        "X" to 1,
        "Y" to 2,
        "Z" to 3
    )

    fun score(): Int {
        return when (you) {
            //win
            defeats[opponent] -> 6 + scores[you]!!
            draws[opponent] -> 3 + scores[you]!!
            else -> 0 + scores[you]!!
        }
    }
}

data class RoundTwo(val opponent: String, val outcome: String) {

    // A Rock
    // B Paper
    // C Scissor

    private val wins = mapOf(
        "A" to "B",
        "B" to "C",
        "C" to "A"
    )

    private val loses = mapOf(
        "A" to "C",
        "B" to "A",
        "C" to "B"
    )

    private val scores = mapOf(
        "A" to 1,
        "B" to 2,
        "C" to 3,
    )

    fun score(): Int {
        return when (outcome) {
            // lose
            "X" -> 0 + scores[loses[opponent]!!]!!
            // draw
            "Y" -> 3 + scores[opponent]!!
            // win
            else -> 6 + scores[wins[opponent]]!!
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.split(" ") }
            .map { Round(it.first(), it.last()) }
            .sumOf { it.score() }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split(" ") }
            .map { RoundTwo(it.first(), it.last())}
            .sumOf { it.score() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
