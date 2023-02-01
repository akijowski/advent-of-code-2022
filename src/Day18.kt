import kotlin.math.max
import kotlin.math.min

data class Point3D(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Point3D) = Point3D(x + other.x, y + other.y, z + other.z)

    fun within(minPos: Point3D, maxPos: Point3D) =
        (x in minPos.x..maxPos.x) && (y in minPos.y..maxPos.y) && (z in minPos.z..maxPos.z)

    fun minimum(other: Point3D) = Point3D(min(x, other.x), min(y, other.y), min(z, other.z))
    fun maximum(other: Point3D) = Point3D(max(x, other.x), max(y, other.y), max(z, other.z))
}

// flood fill, collecting each point within the boundary
fun flood(lava: Set<Point3D>, pos: Point3D, minPos: Point3D, maxPos: Point3D): Set<Point3D> {
    val water = mutableSetOf<Point3D>()

    val q = ArrayDeque<Point3D>().apply { addFirst(pos) }
    val outsideMin = minPos + Point3D(-1, -1, -1)
    val outsideMax = maxPos + Point3D(1, 1, 1)

    while (q.isNotEmpty()) {
        val curr = q.removeFirst()

        if (curr !in water) {
            water += curr
            listOf(
                Point3D(-1, 0, 0),
                Point3D(1, 0, 0),
                Point3D(0, -1, 0),
                Point3D(0, 1, 0),
                Point3D(0, 0, -1),
                Point3D(0, 0, 1)
            ).forEach {
                val n = curr + it
                if (n !in lava && n !in water && n.within(outsideMin, outsideMax)) {
                    q.addFirst(n)
                }
            }
        }
    }
    return water
}

fun main() {
    fun part1(input: List<String>): Int {
        val droplets = input.map {
            val (x, y, z) = it.trim().split(",").map { p -> p.toInt() }
            Point3D(x, y, z)
        }.toSet()
        return droplets
            .flatMap { d ->
                listOf(
                    Point3D(-1, 0, 0),
                    Point3D(1, 0, 0),
                    Point3D(0, -1, 0),
                    Point3D(0, 1, 0),
                    Point3D(0, 0, -1),
                    Point3D(0, 0, 1)
                ).map { n ->
                    n + d
                }
            }.count { it !in droplets }
    }

    fun part2(input: List<String>): Int {
        val droplets = input.map {
            val (x, y, z) = it.trim().split(",").map { p -> p.toInt() }
            Point3D(x, y, z)
        }.toSet()

        val minPos = droplets.fold(
            Point3D(
                Int.MAX_VALUE,
                Int.MAX_VALUE,
                Int.MAX_VALUE
            )
        ) { acc, d -> acc.minimum(d) } + Point3D(-1, -1, -1)

        val maxPos = droplets.fold(
            Point3D(
                Int.MIN_VALUE,
                Int.MIN_VALUE,
                Int.MIN_VALUE
            )
        ) { acc, d -> acc.maximum(d) } + Point3D(1, 1, 1)
        val water = flood(droplets, minPos, minPos, maxPos)

        // area without "air".  Ones that can be filled with water
        return droplets
            .flatMap { d ->
                listOf(
                    Point3D(-1, 0, 0),
                    Point3D(1, 0, 0),
                    Point3D(0, -1, 0),
                    Point3D(0, 1, 0),
                    Point3D(0, 0, -1),
                    Point3D(0, 0, 1)
                ).map { n -> n + d }
            }
            .count { it !in droplets && it in water }
    }

    // sanity check
    val example = setOf(Point3D(1, 1, 1), Point3D(2, 1, 1))
    // neighbors that are not part of the original set
    println(example.flatMap { e ->
        listOf(
            Point3D(-1, 0, 0),
            Point3D(1, 0, 0),
            Point3D(0, -1, 0),
            Point3D(0, 1, 0),
            Point3D(0, 0, -1),
            Point3D(0, 0, 1)
        ).map { n ->
            n + e
        }
    }.count { it !in example })
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 64)
    check(part2(testInput) == 58)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
