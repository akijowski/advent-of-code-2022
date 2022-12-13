import java.util.*
import kotlin.collections.ArrayDeque

fun main() {

    fun List<String>.firstBlankIndex(): Int = this.indexOfFirst { it.isBlank() }

    fun toStacks(input: List<String>, splitIdx: Int): List<Stack<Char>> {
        //for i in row, if row[i] == num; for j in start-1 downTo 0, if input[j][i] == char; stack.push(char)
        //for each number in the row, walk to the top and add each letter to the stack
        val boxIdIdx = splitIdx - 1
        val cols = boxIdIdx - 1
        return buildList {
            for (i in input[boxIdIdx].toCharArray().indices) {
                if (input[boxIdIdx][i].isDigit()) {
                    val s = Stack<Char>().apply {
                        for (j in cols downTo 0) {
                            if (i in input[j].indices && input[j][i].isLetter()) {
                                push(input[j][i])
                            }
                        }
                    }
                    // add the stack
                    add(s)
                }
            }
        }
    }

    fun toInstructions(input: String): List<Int> {
        return input.split(" ").filter { it.first().isDigit() }.map { it.toInt() }
    }

    fun moveOneBoxAtATime(instruction: List<Int>, stacks: List<Stack<Char>>) {
        val (move, from, to) = instruction
        for (i in 1..move) {
            stacks[from - 1].pop().let { stacks[to - 1].push(it) }
        }
    }

    fun moveManyBoxesAtATime(instruction: List<Int>, stacks: List<Stack<Char>>) {
        val (move, from, to) = instruction
        val q = ArrayDeque<Char>()
        for (i in 1..move) {
            stacks[from - 1].pop().let { q.addFirst(it) }
        }
        while (q.isNotEmpty()) {
            q.removeFirst().let { stacks[to - 1].push(it) }
        }
    }

    fun part1(input: List<String>): String {
        val splitIdx = input.firstBlankIndex()
        val s = toStacks(input, splitIdx)
        input.drop(splitIdx + 1)
            .map {
                toInstructions(it)
            }
            .forEach { moveOneBoxAtATime(it, s) }
        return s.map { it.peek() }.joinToString("")
    }

    fun part2(input: List<String>): String {
        val splitIdx = input.firstBlankIndex()
        val s = toStacks(input, splitIdx)
        input.drop(splitIdx + 1)
            .map {
            toInstructions(it)
        }
        .forEach { moveManyBoxesAtATime(it, s) }
        return s.map { it.peek() }.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
