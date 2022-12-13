fun main() {
// TODO: Clean this up
    fun part1(input: String): Int {
        input.windowed(4).indexOfFirst { it.toCharArray().distinct().size == it.length }
        val seen = mutableSetOf<Char>()
        for (idx in input.indices) {
            val c = input[idx]
            if (c in seen) {
                seen.clear()
            }
            seen += c
            if (seen.size >= 4) {
                return idx + 1
            }
        }
        return 0
    }

    fun part2(input: String): Int {
        val seen = mutableMapOf<Char, Int>()
        var low = 0
        for (high in input.indices) {
            val c = input[high]
            if (c in seen.keys) {
                low = low.coerceAtLeast(seen[c]!!)
                seen.filterValues { it < low }.keys.forEach { seen.remove(it) }
            }
            seen[c] = high
            val window = high - low
            if (window >= 14) {
                return high + 1
            }
        }
        return 0
    }

    fun foo(input: String): Int {
        val seen = mutableMapOf<Char, Int>()
        var low = 0
        for (high in input.indices) {
            val c = input[high]
            if (c in seen.keys) {
                low = low.coerceAtLeast(seen[c]!!)
                seen.filterValues { it < low }.keys.forEach { seen.remove(it) }
            }
            seen[c] = high
            val window = high - low
            if (window >= 4) {
                return high + 1
            }
        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsText("Day06_test")
    check(part1(testInput) == 7)
    check(foo(testInput) == 7)
    buildMap {
        put("bvwbjplbgvbhsrlpgdmjqwftvncz", 5)
        put("nppdvjthqldpwncqszvftbrmjlhg", 6)
        put("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10)
        put("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11)
    }.forEach { (s, c) ->
        check(foo(s) == c) { "expected $s to equal $c" }
    }
    buildMap {
        put("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 19)
        put("bvwbjplbgvbhsrlpgdmjqwftvncz", 23)
        put("nppdvjthqldpwncqszvftbrmjlhg", 23)
        put("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 29)
        put("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 26)
    }.forEach { (s, c) ->
        check(part2(s) == c) { "expected $s to equal $c"}
    }

    val input = readInputAsText("Day06")
    println(part1(input))
    println(part2(input))
}
