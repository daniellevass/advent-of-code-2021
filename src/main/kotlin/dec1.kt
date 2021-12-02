
fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfInts("src/main/resources/dec1_input.txt")

    println("we have ${input.size} items")

    dec1_part1(input)
    dec2_part2(input)
}

fun dec1_part1(input: List<Int>) {
    var counter = 0

    for (i in 1 until input.size) {
        if (input[i] > input[i-1]) counter++
    }

    println("part1 counter: $counter")
}

fun dec2_part2(input: List<Int>) {

    // create windows
    val windows = arrayListOf<Int>()
    for (i in 0 .. (input.size -3)) {
        val window = input[i] + input[i+1] + input[i+2]
        windows.add(window)
    }

    // compare windows
    var counter = 0
    for (i in 1 until windows.size) {
        if (windows[i] > windows[i-1]) counter++
    }

    println("part2 counter: $counter")
}

