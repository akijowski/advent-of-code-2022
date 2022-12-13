data class Signal(var cycle: Int = 1, var x: Int = 1) {
    var sum = 0
    val screen = MutableList(6) { MutableList(40) { "." } }
    fun process() {
        if (isComputeTime()) {
            sum += cycle * x
        }
        cycle++
    }

    fun processDraw() {
        val hPos = (cycle - 1) % 40
        val vPos = (cycle - 1) / 40
        if (hPos in (x - 1)..(x + 1)) {
            screen[vPos][hPos] = "#"
        }
        cycle++
    }


    private fun isComputeTime() = cycle % 40 == 20
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.fold(Signal()) { sig, ins ->
            if (ins == "noop") sig.process()
            else {
                repeat(2) { sig.process() }
                sig.x += ins.split(" ").last().toInt()
            }
            sig
        }.sum
    }

    fun part2(input: List<String>) {
        println("Display:")
        input.fold(Signal()) { sig, ins ->
            if (ins == "noop") sig.processDraw()
            else {
                repeat(2) { sig.processDraw() }
                sig.x += ins.split(" ").last().toInt()
            }
            sig
        }.screen
            .forEach { println(it.joinToString(separator = " ")) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)

    val input = readInput("Day10")
    println(part1(input))
    part2(input)
}
