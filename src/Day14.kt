import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

data class SandPoint(val x: Int = 0, val y: Int = 0) {

    fun fromString(s: String, separator: String = ","): SandPoint {
        val (a, b) = s.trim().split(separator)
        return SandPoint(a.toInt(), b.toInt())
    }

    operator fun plus(other: SandPoint) = SandPoint(x + other.x, y + other.y)
    operator fun minus(other: SandPoint) = SandPoint(x - other.x, y - other.y)
    operator fun times(f: Int) = SandPoint(x * f, y * f)

    fun diff(other: SandPoint): Int {
        val mx = abs(x - other.x)
        val my = abs(y - other.y)
        return max(mx, my)
    }

    fun dir(other: SandPoint): SandPoint {
        val sx = (x - other.x).sign
        val sy = (y - other.y).sign
        return SandPoint(sx, sy)
    }
}

fun main() {

    // find the next move, or null of it comes to rest.
    fun getNextDirection(sand: SandPoint, rocksAndSand: Set<SandPoint>): SandPoint? {
        return listOf(
            SandPoint(0, 1),
            SandPoint(-1, 1),
            SandPoint(1, 1)
        ).firstOrNull { sand + it !in rocksAndSand }
    }


    fun buildRockWalls(start: SandPoint, end: SandPoint): List<SandPoint> {
        val diff = start.diff(end)
        val dir = end.dir(start)
        return (0..diff).map { start + (dir * it) }
    }

    // convert a path list(x,y) to rock walls
    fun pathToRocks(path: List<SandPoint>): List<SandPoint> =
        path.zipWithNext().flatMap { (p1, p2) -> buildRockWalls(p1, p2) }

    fun part1(input: List<String>, print: Boolean = false): Int {
        val paths = input.map { it.split("->").map { c -> c.trim() }.map { p -> SandPoint().fromString(p) } }
        val rocks = paths.flatMap { path -> pathToRocks(path).toSet() }.toSet()
        val maxY = rocks.maxOf { it.y }

        val rocksAndSand = rocks.toMutableSet()
        var done = false
        var count = 0
        while (!done) {
            var sand = SandPoint(500, 0)
            while (true) {
                // filled to the top
                if (sand.y > maxY) {
                    done = true
                    break
                }
                val nextDir = getNextDirection(sand, rocksAndSand)
                if (nextDir == null) {
                    rocksAndSand.add(sand)
                    count++
                    break
                } else {
                    sand += nextDir
                }
            }
        }
        if (print) {
            val minX = rocksAndSand.minOf { it.x }
            val maxX = rocksAndSand.maxOf { it.x }
            val map = List(maxY) { CharArray((maxX - minX)) { '.' } }
            rocksAndSand.forEach { (x, y) -> map[y-1][x-minX] = 'o' }
            rocks.forEach { (x, y) -> map[y-1][x-minX] = '#' }
            map.forEach { println(it.joinToString(" ")) }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val rocks = input
            .map { it.split("->").map { c -> c.trim() }.map { p -> SandPoint().fromString(p) } }
            .flatMap { path -> pathToRocks(path).toSet() }.toSet()
        val l = rocks.minOf { it.x }
        val r = rocks.maxOf { it.x }
        val maxY = rocks.maxOf { it.y }
        val start = SandPoint(500, 0)
        val floor = ((l - 250)..(r + 250)).map { SandPoint(it, maxY + 2) }
        val rocksAndSand = (rocks + floor).toMutableSet()

        var done = false
        var count = 0
        while (!done) {
            var sand = start
            while (true) {
                val nextDir = getNextDirection(sand, rocksAndSand)
                if (nextDir == null) {
                    rocksAndSand.add(sand)
                    count++
                    if (sand == start) {
                        done = true
                    }
                    break
                } else {
                    sand += nextDir
                }
            }
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput, true) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

