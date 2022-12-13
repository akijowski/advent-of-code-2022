private val priorityList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

fun main() {
    // TODO: This is better
    fun priority(groups: List<String>): Int {
        val chars = groups.flatMap { it.toSet() }
        val sharedItem = chars.groupingBy { it }.eachCount().maxBy { it.value }.key
        return if (sharedItem.isUpperCase()) (sharedItem - 'A') + 27 else (sharedItem - 'a') + 1
    }

    fun part1Other(input: List<String>): Int {
        val leftSide = mutableSetOf<Char>()
        val rightSide = mutableSetOf<Char>()
        var sum = 0

        for (ruck in input) {
            var left = 0
            var right = ruck.length - 1

            while (left < right) {
                leftSide.add(ruck[left])
                rightSide.add(ruck[right])
                left++
                right--
            }

            leftSide.retainAll(rightSide)
            val match = leftSide.first()
            val i = priorityList.indexOfFirst { it == match } + 1
            sum += i
            leftSide.clear()
            rightSide.clear()
        }

        return sum
    }

    fun part1(input: List<String>): Int {
        return input.map {it.chunked(it.length / 2)}.sumOf { priority(it) }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { priority(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1Other(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
