import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class SensorBeacon(val x: Int, val y: Int, val isBeacon: Boolean = false) {

    fun dist(other: SensorBeacon): Int {
        // manhattan distance
        return abs(x - other.x) + abs(y - other.y)
    }

    fun contains(a: Int, b: Int, other: SensorBeacon): Boolean {
        val man = dist(other)
        val xLo = (x - man) + abs(y - b)
        val xHi = (x + man) - abs(y - b)
        return a in xLo..xHi
    }
}

data class Sensor(val coord: Coord = Coord(), val beacon: Coord = Coord()) {
    fun fromStrings(x: String, y: String, bx: String, by: String) = Sensor(
        Coord(x.trim().toLong(), y.trim().toLong()),
        Coord(bx.trim().toLong(), by.trim().toLong()),
    )

    fun manhattan(): Long = abs(coord.x - beacon.x) + abs(coord.y - beacon.y)

    fun covers(other: Coord) = abs(other.x - coord.x) + abs(other.y - coord.y) <= manhattan()

    fun circumference() = buildList {
        val dist = manhattan()
        val lowerY = (coord.y - dist - 1)
        val upperY = (coord.y + dist + 1)
        for (y in lowerY..upperY) {
            val rem = dist - abs(coord.y - y) + 1
            add(Coord(coord.x - rem, y))
            if (rem > 0) {
                add(Coord(coord.x + rem, y))
            }
        }
    }.distinct()
}

data class Boundary(val start: Int, val end: Int) {
    fun overlaps(other: Boundary) = end >= other.start
}

data class Coord(val x: Long = 0, val y: Long = 0) {
    operator fun plus(other: Coord): Coord = Coord(x + other.x, y + other.y)
}

fun main() {

    val sensorRegex = """^.*x=(-?\d+), y=(-?\d+)$""".toRegex()
    val re = """^Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()
    fun part1(input: List<String>, target: Int): Int {
        val m = buildMap {
            input.map { it.split(":") }.forEach { (s, b) ->
                val (sx, sy) = sensorRegex.matchEntire(s.trim())?.destructured
                    ?: throw IllegalArgumentException("invalid input: $s")
                val (bx, by) = sensorRegex.matchEntire(b.trim())?.destructured
                    ?: throw IllegalArgumentException("invalid input: $b")
                val sensor = SensorBeacon(sx.toInt(), sy.toInt())
                val beacon = SensorBeacon(bx.toInt(), by.toInt(), isBeacon = true)
                put(sensor, beacon)
            }
        }
        val beacons = m.values.toSet()
        // store ranges for y.  At each insert: combine the overlaps
        val otherIdea = mutableMapOf<Int, MutableList<Boundary>>()
        m.entries.forEach { (s, b) ->
            val dist = s.dist(b)
            for (y in (s.y - dist)..(s.y + dist)) {
                val xlo = (s.x - dist) + abs(s.y - y)
                val xhi = (s.x + dist) - abs(s.y - y)
                val newBound = Boundary(xlo, xhi)
                val existing = otherIdea[y] ?: mutableListOf()
                existing.add(newBound)
                otherIdea[y] = existing
            }
        }
        val x = otherIdea.entries.associate { (y, boundaries) ->
            y to boundaries.sortedBy { it.start }.fold(mutableListOf<Boundary>()) { acc, b ->
                if (acc.isEmpty()) {
                    acc.add(b)
                } else {
                    var boundary = b
                    val existingLowest = acc.minBy { it.start }
                    val existingHighest = acc.maxBy { it.end }
                    if (boundary.overlaps(existingLowest) && existingLowest.overlaps(boundary)) {
                        val lo = min(boundary.start, existingLowest.start)
                        val hi = max(boundary.end, existingLowest.end)
                        acc.remove(existingLowest)
                        boundary = Boundary(lo, hi)
                    } else if (boundary.overlaps(existingHighest) && existingHighest.overlaps(boundary)) {
                        val lo = min(boundary.start, existingHighest.start)
                        val hi = max(boundary.end, existingHighest.end)
                        acc.remove(existingHighest)
                        boundary = Boundary(lo, hi)
                    }
                    acc.add(boundary)
                }
                acc
            }
        }
        val y = x.entries.associate { (y, boundaries) ->
            y to boundaries.fold(0) { acc, (s, e) -> acc + ((e - s) + 1) - beacons.count { it.y == y } }
        }
        return y[target]!!
    }

    fun part2(input: List<String>): Long {
        val maxCoord = 4_000_000
        val sensors = input.map {
            val (sx, sy, bx, by) = re.matchEntire(it)?.destructured ?: throw IllegalArgumentException("error: $it")
            Sensor().fromStrings(sx, sy, bx, by)
        }
        val res = sensors
            .asSequence()
            .flatMap { sensor -> sensor.circumference() }
            .filter { coord -> coord.x in 0..maxCoord && coord.y in 0..maxCoord }
            .find { coord ->sensors.none { sensor -> sensor.covers(coord) } }
        return if (res != null) { (res.x * maxCoord) + res.y } else { 0L }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
//part1(listOf("Sensor at x=8, y=7: closest beacon is at x=2, y=10"), 12)
//check(part1(testInput, 10) == 26)

//    val cs = Sensor(Coord(1L, 1L), Coord(3L, 3L)).circumference()
    val input = readInput("Day15")
//    println(part1(input, 2_000_000))
    println(part2(input))
}
