fun List<Char>.distance(): Int = if (isNotEmpty()) size else 1

fun List<Char>.takeUntilEquals(target: Int): List<Char> {
    val list = mutableListOf<Char>()
    for (c in this) {
        list.add(c)
        if (c.digitToInt() >= target) {
            break
        }
    }
    return list
}

fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        val perimeter = 2 * ((input.size - 2) + input.first().length)
        val rowLength = input.first().length - 1
        for (y in 1 until input.size - 1) {
            for (x in 1 until rowLength) {
                // TODO this can be cleaned up
                val tree = input[y][x].digitToInt()
                val maxLeft = input[y].toCharArray().take(x).maxOf { it.digitToInt() }
                val maxRight = input[y].toCharArray().takeLast(rowLength - x).maxOf { it.digitToInt() }
                val maxTop = input.take(y).maxOf { it[x].digitToInt() }
                val maxBottom = input.takeLast(input.size - 1 - y).maxOf { it[x].digitToInt() }
                if ((tree > maxLeft) || (tree > maxRight) || (tree > maxTop) || (tree > maxBottom)) {
                    count++
                }
            }
        }
        return count + perimeter
    }

    fun part2(input: List<String>): Int {
        val rowLength = input.first().length - 1
        var maxScore = 0
        for (y in 1 until input.size - 1) {
            for (x in 1 until rowLength) {
                val tree = input[y][x].digitToInt()

                val treesLeft = input[y].toCharArray().take(x).reversed().takeUntilEquals(tree)
                val treesRight = input[y].toCharArray().takeLast(rowLength - x).takeUntilEquals(tree)
                val treesTop = input.take(y).map { it[x] }.reversed().takeUntilEquals(tree)
                val treesBottom = input.takeLast(input.size - 1 - y).map { it[x] }.takeUntilEquals(tree)

                val score = treesLeft.distance() * treesRight.distance() * treesTop.distance() * treesBottom.distance()

                maxScore = maxScore.coerceAtLeast(score)
            }
        }
        return maxScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
