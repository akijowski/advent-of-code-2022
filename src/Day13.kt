import kotlinx.serialization.json.*

class PacketComparator: Comparator<JsonElement> {
    override fun compare(o1: JsonElement, o2: JsonElement): Int {
        var a = o1; var b = o2
        if (a is JsonArray && b is JsonArray) {
            // compare lists
            for (i in 0 until a.size.coerceAtMost(b.size)) {
                val res = compare(a[i], b[i])
                // not equal
                if (res != 0) {
                    return res
                }
            }
            // left ran out
            if (a.size < b.size) {
                return -1
            }
            // right ran out
            if (a.size > b.size) {
                return 1
            }
            return 0
        }
        if (a is JsonArray) {
            b = JsonArray(listOf(b))
            return compare(a, b)
        }
        if (b is JsonArray) {
            a = JsonArray(listOf(a))
            return compare(a, b)
        }
        return a.jsonPrimitive.int compareTo b.jsonPrimitive.int
    }
}

fun main() {

    val pc = PacketComparator()

    fun correct(pairs: List<String>): Boolean {
        val (first, second) = pairs
        val a = Json.decodeFromString(JsonArray.serializer(), first)
        val b = Json.decodeFromString(JsonArray.serializer(), second)
        val comp = pc.compare(a, b)
        return comp == -1
    }

    fun part1(input: List<String>): Int {
        return input
            .filter { it.isNotBlank() }
            .chunked(2)
            .mapIndexed { idx, codePairs ->
                if (correct(codePairs)) idx+1 else 0
            }.sum()
    }

    fun part2(input: List<String>): Int {
        val updatedList = input.toMutableList().apply {
            add("[[2]]")
            add("[[6]]")
        }
        var s1 = 0
        var s2 = 0
        updatedList
            .filter { it.isNotBlank() }
            .map { Json.decodeFromString(JsonArray.serializer(), it)}
            .sortedWith(pc)
            .forEachIndexed { index, jsonArray ->
                if (jsonArray.toString() == "[[2]]") {
                    s1 += (index + 1)
                }
                if (jsonArray.toString() == "[[6]]") {
                    s2 += (index + 1)
                }
            }
        return s1 * s2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
