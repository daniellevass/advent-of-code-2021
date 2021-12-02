
fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec2_input.txt")

    println("we have ${input.size} items")

    dec2_part1(input)

    dec2_part2(input)
}

fun dec2_part1(input: List<String>) {

    var horizontal = 0
    var depth = 0

    for (i in input.indices) {

        val instructions = input[i].split(" ")

        when (instructions[0]) {
            "up" -> depth-= instructions[1].toInt()
            "down" -> depth+= instructions[1].toInt()
            "forward" -> horizontal += instructions[1].toInt()
            else -> println(instructions[0])
        }
    }

    println("horizontal=$horizontal depth=$depth multiplied=${horizontal * depth}")
}

fun dec2_part2(input: List<String>) {
    var horizontal = 0
    var depth = 0
    var aim = 0

    for (i in input.indices) {

        val instructions = input[i].split(" ")

        when (instructions[0]) {
            "up" -> aim-= instructions[1].toInt()
            "down" -> aim+= instructions[1].toInt()
            "forward" -> {
                horizontal += instructions[1].toInt()
                depth += aim * instructions[1].toInt()
            }
            else -> println(instructions[0])
        }
    }

    println("horizontal=$horizontal depth=$depth aim=$aim multiplied=${horizontal * depth}")
}
