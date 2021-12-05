
fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec5_input.txt")
    println("we have ${input.size} items")

    val list = dec5_processInput(input)
    println(list)

    dec5_part1(list)
}

private fun dec5_processInput(input:List<String>): List<Pair<Int, Int>> {
    val list = arrayListOf<Pair<Int, Int>>()

    input.map {
        val coords = it.split(" -> ")

        val start = coords[0].split(",").map { number -> number.toInt() }
        val end = coords[1].split(",").map {  number -> number.toInt() }

        // check if it's a vertical line
        if (start[0] == end[0]) {
            //x is the same -> y is changing
            val line = arrayListOf<Pair<Int, Int>>()

            // find startY and endY -> could be flipped
            var startY = start[1]
            var endY = end[1]
            if (end[1] < start[1]) {
                startY = end[1]
                endY = start[1]
            }

            for (y in startY .. endY) {
                line.add(start[0] to y)
            }

            list.addAll(line)
        }
        // horizontal line
        if (start[1] == end[1]) {
            //y is the same -> x is changing
            val line = arrayListOf<Pair<Int, Int>>()

            // find startY and endY -> could be flipped
            var startX = start[0]
            var endX = end[0]
            if (end[0] < start[0]) {
                startX = end[0]
                endX = start[0]
            }

            for (x in startX .. endX) {
                line.add(x to start[1])
            }

            list.addAll(line)
        }

        if (start[0] != end[0] && start[1] != end[1]) {
            // we have the problematic diagonal line! ARGH!
             list.addAll(dec5_makeDiagonalLine(
                 x1 = start[0], y1 =start[1],
                 x2 = end[0], y2 = end[1]))
        }
    }

    return list
}

private fun dec5_part1(list: List<Pair<Int, Int>>) {

    val clashes = list.groupingBy { it }
        .eachCount()
        .filter { it.value > 1 }

    println("we got ${clashes.size} clashes")
}

fun dec5_makeDiagonalLine(x1: Int, x2: Int, y1: Int, y2: Int) : List<Pair<Int, Int>> {
    val line = arrayListOf<Pair<Int, Int>>()

    var xStep = 1
    if (x1 > x2) {
        xStep =-1
    }

    var yStep = 1
    if (y1 > y2) {
        yStep = -1
    }

    var y = y1
    var x = x1
    for (delta in 0..Math.abs(Math.max(x1, x2) - Math.min(x1, x2))) {
        line.add(x to y)
        y+= yStep
        x+= xStep
    }

    println("x1=$x1 x2=$x2 y1=$y1 y2=$y2")
    println(line)
    return line
}
