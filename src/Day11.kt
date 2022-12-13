data class Monkey(
    val startingItems: List<Long>,
    val opExpr: String,
    val divisor: Long,
    val testTrueMonkey: Int,
    val testFalseMonkey: Int,
) {
    // do not count starting items
    var count = 0
    val items = startingItems.toMutableList()

    fun round(item: Long, gcd: Long? = null): Pair<Int, Long> {
        var worry = calcWorry(item)
        worry = if (gcd != null) {
            worry % gcd
        } else {
            worry / 3
        }
        val monkey = if (worry % divisor == 0L) testTrueMonkey else testFalseMonkey
        return monkey to worry
    }

    private fun calcWorry(item: Long): Long {
        count++
        return if (opExpr == "* old") {
            item * item
        } else {
            val (op, v) = opExpr.split(" ")
            when (op) {
                "*" -> item * v.toInt()
                else -> item + v.toInt()
            }
        }
    }
}

fun List<Monkey>.calcHighestProduct(num: Int) =
    map { it.count.toLong() }.sorted().takeLast(num).reduce { a, b -> a * b }

fun main() {

    val startItemsRegex = """^Starting items: (.+)$""".toRegex()
    val opRegex = """^Operation: new = old (.+)$""".toRegex()
    val divisorRegex = """^Test: divisible by (\d+)$""".toRegex()
    val trueRegex = """^If true: throw to monkey (\d+)$""".toRegex()
    val falseRegex = """^If false: throw to monkey (\d+)$""".toRegex()

    fun toMonkey(inputs: List<String>): Monkey {
        val startItems = startItemsRegex
            .matchEntire(inputs[1])?.groups?.last()?.value?.split(",")
            ?.map { s -> s.trim() }
            ?.map { s -> s.toLong() }
            ?: throw IllegalArgumentException("incorrect input: ${inputs[1]}")
        val opExpr = opRegex.matchEntire(inputs[2])?.groups?.last()?.value?.trim()
            ?: throw IllegalArgumentException("incorrect input: ${inputs[2]}")
        val divisorExpr = divisorRegex.matchEntire(inputs[3])?.groups?.last()?.value?.toLong()
            ?: throw IllegalArgumentException("incorrect input: ${inputs[3]}")
        val trueMonkey = trueRegex.matchEntire(inputs[4])?.groups?.last()?.value?.toInt()
            ?: throw IllegalArgumentException("incorrect input: ${inputs[4]}")
        val falseMonkey = falseRegex.matchEntire(inputs[5])?.groups?.last()?.value?.toInt()
            ?: throw IllegalArgumentException("incorrect input: ${inputs[5]}")
        return Monkey(startItems, opExpr, divisorExpr, trueMonkey, falseMonkey)
    }

    fun getMonkeys(input: List<String>): List<Monkey> {
        return input
            .filter { it.isNotBlank() }
            .map { it.trim() }
            .chunked(6)
            .map { toMonkey(it) }
    }

    fun part1(input: List<String>): Long {
        val monkeys = getMonkeys(input)
        repeat(20) {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    val (nextMonkey, output) = monkey.round(item)
                    monkeys[nextMonkey].items.add(output)
                }
            }
        }
        return monkeys.calcHighestProduct(2)
    }

    fun part2(input: List<String>): Long {
        val monkeys = getMonkeys(input)
        val gcd = monkeys.map { it.divisor }.reduce { a, b -> a * b }
        repeat(10_000) {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    val (nextMonkey, output) = monkey.round(item, gcd)
                    monkeys[nextMonkey].items.add(output)
                }
            }
        }
        return monkeys.calcHighestProduct(2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
