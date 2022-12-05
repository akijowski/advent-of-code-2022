data class Assignment(val start: Int, val end: Int) {

    fun fullyContains(other: Assignment): Boolean {
        return start >= other.start && end <= other.end
    }

    fun overlaps(other: Assignment): Boolean {
        return end >= other.start
    }
}

fun toAssignments(ids: List<String>): Pair<Assignment, Assignment> {
    val x = ids.first().split("-")
    val y = ids.last().split("-")
    return Assignment(x.first().toInt(), x.last().toInt()) to Assignment(y.first().toInt(), y.last().toInt())
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.split(",") }
            .map { toAssignments(it) }
            .count { it.first.fullyContains(it.second) || it.second.fullyContains(it.first) }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split(",") }
            .map { toAssignments(it) }
            .count { it.first.overlaps(it.second) && it.second.overlaps(it.first) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
