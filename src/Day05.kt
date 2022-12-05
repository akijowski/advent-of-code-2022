import java.util.*
import kotlin.collections.ArrayDeque

fun main() {
    fun toStacks(input: List<String>, splitIdx: Int): List<Stack<Char>> {
        //for i in row, if row[i] == num; for j in start-1 downTo 0, if input[j][i] == char; stack.push(char)
        val stacks = mutableListOf<Stack<Char>>()
        val boxIdIdx = splitIdx - 1
        val cols = boxIdIdx - 1
        stacks.apply {
            for (i in input[boxIdIdx].toCharArray().indices) {
                if (input[boxIdIdx][i].isDigit()) {
                    val s = Stack<Char>().apply {
                        for (j in cols downTo 0) {
                            if (i in input[j].indices && input[j][i].isLetter()) {
                                push(input[j][i])
                            }
                        }
                    }
                    add(s)
                }
            }
        }
        return stacks
    }

    fun toInstructions(input: String): List<Int> {
        return input.split(" ")
            .filter { it.first().isDigit() }
            .map { d -> d.toInt() }
    }

    fun moveOneBoxAtATime(instruction: List<Int>, stacks: List<Stack<Char>>) {
        for (i in 1..instruction.first()) {
            stacks[instruction[1]-1].pop().let{ stacks[instruction[2]-1].push(it)}
        }
    }

    fun moveManyBoxesAtATime(instruction: List<Int>, stacks: List<Stack<Char>>) {
        val q = ArrayDeque<Char>()
        for (i in 1..instruction.first()) {
            stacks[instruction[1] - 1].pop().let { q.addFirst(it) }
        }
        while (q.isNotEmpty()) {
            q.removeFirst().let { stacks[instruction[2] - 1].push(it) }
        }
    }

    fun part1Test(input: List<String>): String {
        val splitIdx = input.indexOfFirst { it.isBlank() }
        val t = toStacks(input, splitIdx)
        val instructions = input.drop(splitIdx + 1).map {
            toInstructions(it)
        }
        instructions.forEach { moveOneBoxAtATime(it, t)}
        val x = t.map { it.peek() }
        return x.joinToString("")
    }

    fun part1(input: List<String>): String {
        val splitIdx = input.indexOfFirst { it.isBlank() }
        val s = toStacks(input, splitIdx)
        input.drop(splitIdx + 1).map {
            toInstructions(it)
        }.forEach { moveOneBoxAtATime(it, s)}
        return s.map { it.peek() }.joinToString("")
    }

    fun part2Test(input: List<String>): String {
        val splitIdx = input.indexOfFirst { it.isBlank() }
        val t = toStacks(input, splitIdx)
        val instructions = input.drop(splitIdx + 1).map {
            toInstructions(it)
        }
        instructions.forEach { moveManyBoxesAtATime(it, t)}
        val x = t.map { it.peek() }
        return x.joinToString("")
    }

    fun part2(input: List<String>): String {
        val splitIdx = input.indexOfFirst { it.isBlank() }
        val s = toStacks(input, splitIdx)
        val instructions = input.drop(splitIdx + 1).map {
            toInstructions(it)
        }
        instructions.forEach { moveManyBoxesAtATime(it, s)}
        return s.map { it.peek() }.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1Test(testInput) == "CMZ")
    check(part2Test(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
