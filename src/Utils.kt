import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Reads entire input txt file.
 */
fun readInputAsText(name: String) = File("src", "$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * A generic graph node
 */
data class GraphNode(val name: String, val x: Int, val y: Int) {
    constructor(name: Char, x: Int, y: Int) : this(name.toString(), x, y)
}

/**
 * Performs a Dijkstra algorithm for a given graph (adjacency list) and start node
 */
class Dijkstra(
    private val graph: Map<GraphNode, List<GraphNode>>,
    private val start: GraphNode
) {
    private val unvisited = graph.keys.toMutableSet()
    private val visited = mutableSetOf<GraphNode>()

    // arbitrary values
    private val nodeDistances = unvisited.associateWith { Int.MAX_VALUE - 1 }.toMutableMap().apply { put(start, 0) }

    private fun visit(curr: GraphNode) {
        val currDist = nodeDistances[curr]!! + 1
        for (node in graph[curr]!!) {
            val nodeDist = nodeDistances[node]!!
            if (node !in visited) {
                // take the smallest
                if (currDist < nodeDist) {
                    nodeDistances[node] = currDist
                }
            }
        }
        visited += curr
        unvisited -= curr
    }

    /**
     * Find the number of steps from start until end
     */
    fun run(end: GraphNode): Int {
        while (end !in visited) {
            val curr = unvisited.minBy { nodeDistances[it]!! }
            visit(curr)
        }
        return nodeDistances[end]!!
    }

    /**
     * Find the number of steps from start to all ends
     */
    fun runAll(ends: Set<GraphNode>): Map<GraphNode, Int> {
        while (!visited.containsAll(ends)) {
            val curr = unvisited.minBy { nodeDistances[it]!! }
            visit(curr)
        }
        return nodeDistances.filterKeys { it in ends }
    }
}
