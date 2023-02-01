data class RockPos(val x: Int, val y: Int) {
    fun move(jet: Int) = this.copy(x = x + jet)

    fun fallDown(): RockPos = this.copy(y = y - 1)
}

fun lineRock(xStart: Int, y: Int) = (xStart..xStart + 3).map { x -> RockPos(x, y) }
fun plusRock(xStart: Int, y: Int) = listOf(
    RockPos(xStart, y + 1),
    RockPos(xStart + 1, y + 1),
    RockPos(xStart + 2, y + 1),
    RockPos(xStart + 1, y),
    RockPos(xStart + 1, y + 2)
)

fun elRock(xStart: Int, y: Int) = listOf(
    RockPos(xStart, y),
    RockPos(xStart + 1, y),
    RockPos(xStart + 2, y),
    RockPos(xStart + 2, y + 1),
    RockPos(xStart + 2, y + 2)
)

fun pipeRock(xStart: Int, y: Int) = (0..3).map { RockPos(xStart, y + it) }
fun sqRock(xStart: Int, y: Int) =
    listOf(RockPos(xStart, y), RockPos(xStart + 1, y), RockPos(xStart, y + 1), RockPos(xStart + 1, y + 1))

fun makeRock(n: Int, y: Int): List<RockPos> = when (n) {
    0 -> lineRock(2, y)
    1 -> plusRock(2, y)
    2 -> elRock(2, y)
    3 -> pipeRock(2, y)
    4 -> sqRock(2, y)
    else -> error("unknown rock type: $n")
}

fun List<RockPos>.canMove(jet: Int, cave: Set<RockPos>): Boolean {
    val inBound = if (jet == -1) {
        this.minOf { it.x } > 0
    } else {
        this.maxOf { it.x } < 6
    }
    return inBound && this.none { RockPos(it.x + jet, it.y) in cave }
}

fun List<RockPos>.fall() = this.map { it.fallDown() }

fun Set<RockPos>.highestPoint() = this.maxOfOrNull { it.y } ?: 0

fun Set<RockPos>.hasPattern(y: Int): Boolean {
    // ..#.##.
    // ..#.##.
    val hasFirstRow = (0..1).all { RockPos(it, y) !in this }
            && (RockPos(2, y) in this)
            && (RockPos(3, y) !in this)
            && (4..5).all { RockPos(it, y) in this }
            && (RockPos(6, y) !in this)
    val hasSecondRow = (0..1).all { RockPos(it, y + 1) !in this }
            && (RockPos(2, y + 1) in this)
            && (RockPos(3, y + 1) !in this)
            && (4..5).all { RockPos(it, y + 1) in this }
            && (RockPos(6, y + 1) !in this)
    return hasFirstRow && hasSecondRow
}

fun RockPos.canFall(cave: Set<RockPos>) = !cave.contains(this.copy(y = this.y - 1)) && this.y > 1

fun main() {

    fun part1(input: String): Int {
        val jetPattern = input.trim().map { if (it == '<') -1 else 1 }
        val cave = mutableSetOf<RockPos>()
        var time = 0
        (0 until 2022).map { rockNum ->
            val yStart = cave.highestPoint() + 4
            var rock = makeRock(rockNum % 5, yStart)
            var isAtRest = false
            while (!isAtRest) {
                rock = rock.map { piece ->
                    val jet = jetPattern[time % jetPattern.size]
                    if (rock.canMove(jet, cave)) {
                        piece.move(jet)
                    } else {
                        // no-op
                        piece
                    }
                }
                isAtRest = !rock.all { it.canFall(cave) }
                if (!isAtRest) {
                    rock = rock.fall()
                }
                time++
            }
            rock.map { cave += it }
        }
        return cave.highestPoint()
    }

    // TODO: not correct
    fun part2(input: String): Long {
        val jetPattern = input.trim().map { if (it == '<') -1 else 1 }
        val cave = mutableSetOf<RockPos>()
        var time = 0
        val heightHistory = mutableListOf(0)
        (0 until 2022).map { rockNum ->
            val yStart = cave.highestPoint()
            var rock = makeRock(rockNum % 5, yStart)
            var isAtRest = false
            while (!isAtRest) {
                rock = rock.map { piece ->
                    val jet = jetPattern[time % jetPattern.size]
                    if (rock.canMove(jet, cave)) {
                        piece.move(jet)
                    } else {
                        piece
                    }
                }
                isAtRest = !rock.all { it.canFall(cave) }
                if (!isAtRest) {
                    rock = rock.fall()
                }
                time++
            }
            rock.map { cave += it }
            heightHistory += cave.highestPoint()
        }

        // find a loop in the heights over iterations.  Use a heuristic to guess
        val heightDiffs = heightHistory.zipWithNext().map { (a, b) -> b - a}

        // arbitrary starting points
        val loopStart = 200
        val markerLength = 10
        val marker = heightDiffs.subList(loopStart, loopStart + markerLength)

        var loopHeight = -1
        var loopLength = -1
        val heightBeforeLoop = heightDiffs[loopStart-1]
        for (i in loopStart+markerLength until heightDiffs.size) {
            // we found a match
            if (marker == heightDiffs.subList(i, i + markerLength)) {
                loopLength = i - loopStart
                loopHeight = heightHistory[i - 1] + heightBeforeLoop
                break
            }
        }

        val targetRocks = 1_000_000_000_000
        val fullLoops = (targetRocks - loopStart) / loopLength
        val offset = ((targetRocks - loopStart) % loopLength).toInt()
        val extraY = heightHistory[loopStart + offset] - heightBeforeLoop
        return heightBeforeLoop + loopHeight * fullLoops + extraY
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsText("Day17_test")
    check(part1(testInput) == 3068)

    val input = readInputAsText("Day17")
    println(part1(input))
    println(part2(input))
}
