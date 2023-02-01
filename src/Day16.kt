data class Valve(val flow: Int, val next: List<String>)

data class Path(val valves: List<Valve>, val opened: Map<Valve, Int>) {
    fun total(): Int = opened.map { (v, t) -> (30 - t) * v.flow }.sum()
}

fun main() {

    val fre = """^Valve (\w+) has flow rate=(\d+)$""".toRegex()
    val lre = """^[a-z\s]+([A-Z,\s]+)$""".toRegex()

    // DFS and find the "best" each time.  Ew.
    fun part1(input: List<String>): Int {
        val adjList = buildMap {
            input.map {
                val s1 = it.substringBefore(";")
                val s2 = it.substringAfter(";")
                val (r, f) = fre.matchEntire(s1)?.destructured ?: throw IllegalArgumentException("invalid: $it")
                val next = lre.matchEntire(s2)?.groups?.last()?.value ?: throw IllegalArgumentException("invalid: $it")
                put(r, Valve(f.trim().toInt(), next.split(",").map { s -> s.trim() }))
            }
        }
        val maxOpened = adjList.values.count {it.flow > 0}
        val start = adjList["AA"]!!
        val startPath = Path(listOf(start), HashMap())
        var allPaths = listOf(startPath)
        var bestPath = startPath

        var time = 1

        while (time < 30) {
            val newPaths = mutableListOf<Path>()

            for (curr in allPaths) {
                // no more work to do
                if (curr.opened.size == maxOpened) {
                    continue
                }
                val currentLast = curr.valves.last()
                val currentValves = curr.valves

                // open valve option
                if (currentLast.flow > 0 && !curr.opened.containsKey(currentLast)) {
                    val opened = curr.opened.toMutableMap()
                    opened[currentLast] = time
                    val possibleValves = currentValves + currentLast
                    val possibleOpenedPath = Path(possibleValves, opened)
                    newPaths.add(possibleOpenedPath)
                }

                // move to valve option
                val possiblePaths = currentLast.next.map { next ->
                    val possibleValve = adjList[next] ?: error("could not find valve $next")
                    val possibleValves = currentValves + possibleValve
                    val possiblePath = Path(possibleValves, curr.opened)
                    possiblePath
                }
                newPaths.addAll(possiblePaths)
            }

            // arbitrary truncation
            allPaths = newPaths.sortedByDescending { it.total() }.take(10_000)

            if (allPaths.first().total() > bestPath.total()) {
                bestPath = allPaths.first()
            }
            time++
        }

        return bestPath.total()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 1651)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
