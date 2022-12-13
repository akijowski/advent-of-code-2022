import kotlin.math.abs
import kotlin.math.sign

data class Knot(var x: Int, var y: Int) {
    fun isAboveOrBelow(other: Knot): Boolean {
        // if more than 1 away in y
        return (x == other.x) && abs(y - other.y) > 1
    }

    fun isLeftOrRightOf(other: Knot): Boolean {
        // if more than 1 away in x
        return (y == other.y) && abs(x - other.x) > 1
    }

    fun isDiagonalOf(other: Knot): Boolean {
        // if more than 2 away in x + y
        return abs(x - other.x) + abs(y - other.y) > 2
    }
}

fun Knot.asPair(): Pair<Int, Int> = x to y

fun main() {
    val directions = mapOf(
        "R" to (1 to 0),
        "L" to (-1 to 0),
        "D" to (0 to -1),
        "U" to (0 to 1)
    )

    fun follow(head: Knot, tail: Knot): Knot {
        return when {
            head.isAboveOrBelow(tail) -> tail.copy(y = tail.y + (head.y - tail.y).sign)
            head.isLeftOrRightOf(tail) -> tail.copy(x = tail.x + (head.x - tail.x).sign)
            head.isDiagonalOf(tail) -> Knot(
                tail.x + (head.x - tail.x).sign,
                tail.y + (head.y - tail.y).sign
            )

            else -> tail
        }
    }

    fun part1(input: List<String>): Int {
        val h = Knot(0, 0)
        var t = Knot(0, 0)
        val tailVisited = mutableSetOf(h.asPair())

        input.forEach {
            val (dir, n) = it.split(" ")
            val (dx, dy) = directions[dir]!!
            for (i in 0 until n.toInt()) {
                h.x += dx
                h.y += dy
                t = follow(h, t)
                tailVisited.add(t.asPair())
            }
        }
        return tailVisited.size
    }

    fun part2(input: List<String>): Int {
        val h = Knot(0, 0)
        val ks = MutableList(9) { Knot(0, 0) }
        val tailVisited = mutableSetOf(h.asPair())
        input.forEach {
            val (dir, n) = it.split(" ")
            val (dx, dy) = directions[dir]!!
            for (i in 0 until n.toInt()) {
                h.x += dx
                h.y += dy
                // move tail
                for (j in ks.indices) {
                    val head = if (j == 0) h else ks[j - 1]
                    val curr = ks[j]
                    follow(head, curr).let { newPos -> ks[j] = newPos }
                }
                tailVisited.add(ks.last().asPair())
            }
        }
        return tailVisited.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    val testInput2 = readInput("Day09_test_2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
