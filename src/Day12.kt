fun main() {
    val directions = listOf(
        -1 to 0,
        1 to 0,
        0 to -1,
        0 to 1
    )

    fun part1(input: List<String>): Int {
        var start: GraphNode? = null
        var end: GraphNode? = null
        // graph weights
        val heights = buildList {
            for (y in input.indices) {
                // row of heights
                val hs = buildList {
                    for (x in input[y].indices) {
                        var c = input[y][x]
                        when(c) {
                            'S' -> {
                                start = GraphNode(c, x, y)
                                c = 'a'
                            }
                            'E' -> {
                                end = GraphNode(c, x, y)
                                c = 'z'
                            }
                        }
                        add(c - 'a')
                    }
                }
                add(hs)
            }
        }
        val adjList = buildMap {
            for (y in input.indices) {
                for (x in input[y].indices) {
                    // nodes that are within 1 step
                    this[GraphNode(input[y][x], x, y)] = buildList {
                        directions.forEach { (dx, dy) ->
                            val a = x + dx
                            val b = y + dy
                            if (b in input.indices && a in input[y].indices) {
                                if (heights[b][a] - heights[y][x] <= 1) {
                                    add(GraphNode(input[b][a], a, b))
                                }
                            }
                        }
                    }
                }
            }
        }
        val g = Dijkstra(adjList, start!!)
        return g.run(end!!)
    }

    fun part2(input: List<String>): Int {
        var start: GraphNode? = null
        val ends = mutableSetOf<GraphNode>()
        // graph weights
        val heights = buildList {
            for (y in input.indices) {
                // row of heights
                val hs = buildList {
                    for (x in input[y].indices) {
                        var c = input[y][x]
                        // go backwards
                        when (c) {
                            'S' -> {
                                ends += GraphNode(c, x, y)
                                c = 'a'
                            }
                            'a' -> ends += GraphNode(c, x, y)
                            'E' -> {
                                start = GraphNode(c, x, y)
                                c = 'z'
                            }
                        }
                        add(c - 'a')
                    }
                }
                add(hs)
            }
        }
        val adjList = buildMap {
            for (y in input.indices) {
                for (x in input[y].indices) {
                    // next steps (going backwards)
                    this[GraphNode(input[y][x], x, y)] = buildList {
                        directions.forEach { (dx, dy) ->
                            val a = x + dx
                            val b = y + dy
                            if (b in input.indices && a in input[y].indices) {
                                // can we step backwards?
                                if (heights[y][x] - heights[b][a] <= 1) {
                                    add(GraphNode(input[b][a], a, b))
                                }
                            }
                        }
                    }
                }
            }
        }
        val g = Dijkstra(adjList, start!!)
        return g.runAll(ends).minOf { it.value }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
