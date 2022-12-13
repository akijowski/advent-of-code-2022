data class Directory(val name: String, val parent: Directory? = null) {
    private val files = mutableMapOf<String, Int>()
    val children = mutableMapOf<String, Directory>()

    fun addFile(size: Int, name: String) = files.putIfAbsent(name, size)

    fun addChild(directory: Directory) = children.putIfAbsent(directory.name, directory)

    fun getSize(): Int = files.values.sum() + children.values.sumOf { it.getSize() }
}

fun String.isCD() = startsWith("$ cd")
fun String.isDir() = startsWith("dir")
fun String.isFile() = first().isDigit()
fun String.toFile() = split(" ").first().toInt() to split(" ")[1]

fun main() {

    fun buildTree(input: List<String>): Directory {
        // cd ==> add dir to stack OR pop from stack
        // ls ==> dir contents
        // dir _x_ ==> child dir to curr dir
        // file ==> add to file size for dir
        val root = Directory("/")
        var cwd = root
        input.drop(1).forEach {
            when {
                it.isDir() -> cwd.addChild(Directory(it.drop(3).trim(), parent = cwd))
                it.isFile() -> cwd.addFile(it.toFile().first, it.toFile().second)
                it == "$ cd .." -> cwd = cwd.parent!!
                it.isCD() -> cwd = cwd.children[it.drop(4).trim()]!!
            }
        }
        return root
    }

    fun part1(input: List<String>): Int {
        val root = buildTree(input)
        val q = ArrayDeque<Directory>().apply { addFirst(root) }
        var total = 0
        while (q.isNotEmpty()) {
            val d = q.removeFirst()
            val size = d.getSize()
            if (size <= 100_000) {
                total += size
            }
            d.children.values.forEach { q.addFirst(it) }
        }
        return total
    }

    fun part2(input: List<String>): Int {
        val root = buildTree(input)
        val targetSize = 30_000_000 - (70_000_000 - root.getSize())
        var total = Int.MAX_VALUE
        val q = ArrayDeque<Directory>().apply { addFirst(root) }
        while (q.isNotEmpty()) {
            val d = q.removeFirst()
            val size = d.getSize()
            if (size >= targetSize) {
                total = total.coerceAtMost(size)
            }
            d.children.values.forEach { q.addFirst(it) }
        }
        return total
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
