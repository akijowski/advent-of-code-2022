import java.util.*

fun main() {
    fun sorted(input: List<String>): SortedSet<Int> {
        var sum = 0
        val ts = TreeSet(Comparator.naturalOrder<Int>().reversed()).apply {
            input.map {
                if (it.isBlank()) {
                    this.add(sum)
                    sum = 0
                } else {
                    sum += it.toInt()
                }
            }
            if (sum != 0) {
                this.add(sum)
            }
        }
        return ts
    }
    fun part1(input: List<String>): Int {
        val ts = sorted(input)
//        println(ts.joinToString())
        return ts.first()
    }

    fun part2(input: List<String>): Int {
        return sorted(input).take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
