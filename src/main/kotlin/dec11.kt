
fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec11_input.txt")
    println("we have ${input.size} items")

    val grid = dec11_processInput(input)
//    println(grid)
//    dec11_part1(grid)
    dec11_part2(grid)
}

private fun dec11_processInput(input: List<String>) : HashMap<Pair<Int, Int>, Int> {
    val grid = HashMap<Pair<Int, Int>, Int>()

    input.mapIndexed { y, s ->
        val row = s.split("").filterNot { it.isBlank() }.map { it.toInt() }

        row.mapIndexed { x, octopus ->
            grid.put(y to x, octopus)
        }

    }

    return grid
}

private fun dec11_part1(grid: HashMap<Pair<Int, Int>, Int>) {

    val numberOfSteps = 100
    var currentGrid = grid
    var flashes = 0

    for(step in 1 .. numberOfSteps) {
        println("starting step $step")

        var complete = false

        // start by incrementing them all by 1
        val incrementedGrid = dec11_incremeentAllByOne(currentGrid)

        var startGrid = incrementedGrid
        while (! complete) {
            // update all 9s to -1s to indicate it flashed just now
            val updatedGrid = dec11_setNinesToFlashes(startGrid)

            // check if we have any new flashes
            if (dec11_countOctopusesInGridWithNumber(updatedGrid, -1) == 0) {
                complete = true
            } else {
                // update all items near -1s, and -1s to zeros
                startGrid = dec11_processFlashes(updatedGrid)
            }
        }
        val newFlashes = dec11_countOctopusesInGridWithNumber(startGrid, 0)
        println("$newFlashes new flashes")
        flashes += newFlashes
        currentGrid = startGrid
    }

    println("part1 = $flashes")
}

private fun dec11_part2(grid: HashMap<Pair<Int, Int>, Int>) {

    var gameComplete = false
    var step = 1

    var currentGrid = grid
    while (!gameComplete) {
//        println("starting step $step")

        var complete = false
        // start by incrementing them all by 1
        val incrementedGrid = dec11_incremeentAllByOne(currentGrid)

        var startGrid = incrementedGrid
        while (! complete) {
            // update all 9s to -1s to indicate it flashed just now
            val updatedGrid = dec11_setNinesToFlashes(startGrid)

            // check if we have any new flashes
            if (dec11_countOctopusesInGridWithNumber(updatedGrid, -1) == 0) {
                complete = true
            } else {
                // update all items near -1s, and -1s to zeros
                startGrid = dec11_processFlashes(updatedGrid)
            }
        }
        val newFlashes = dec11_countOctopusesInGridWithNumber(startGrid, 0)
//        println("$newFlashes new flashes")
        currentGrid = startGrid

        if (newFlashes == 100) {
            println("part2 = $step")
            gameComplete = true
        }

        step ++
    }

}

private fun dec11_incremeentAllByOne(input: HashMap<Pair<Int, Int>, Int>) : HashMap<Pair<Int, Int>, Int> {
    val updatedInput = HashMap<Pair<Int, Int>, Int>()

    input.forEach { (key, value) ->
        updatedInput[key] = (value +1)
    }

    return updatedInput
}

private fun dec11_setNinesToFlashes(input: HashMap<Pair<Int, Int>, Int>): HashMap<Pair<Int, Int>, Int> {
    val updatedInput = HashMap<Pair<Int, Int>, Int>()

    input.forEach { (key, value) ->

        if (value > 9) {
            updatedInput[key] = -1
        } else {
            updatedInput[key] = value
        }
    }

    return updatedInput
}

private fun dec11_processFlashes(input: HashMap<Pair<Int, Int>, Int>): HashMap<Pair<Int, Int>, Int> {
    val updatedInput = HashMap<Pair<Int, Int>, Int>()

    input.forEach { (key, value) ->
        val y = key.first
        val x = key.second

        if (value == 0) {
            // ignore as we flashed
            updatedInput[key] = 0
        } else if (value == -1) {
            updatedInput[key] = 0
        } else {
            var flashesNearby = 0
            // update top
            if (input.containsKey(y-1 to x)
                         && input[y-1 to x] == -1 ) flashesNearby ++

            // bottom
            if (input.containsKey(y+1 to x)
                         && input[y+1 to x] == -1 ) flashesNearby ++

            // right
            if (input.containsKey(y to x+1)
                         && input[y to x+1] == -1 ) flashesNearby ++

            // left
            if (input.containsKey(y to x-1)
                         && input[y to x-1] == -1 ) flashesNearby ++

            // top right
            if (input.containsKey(y-1 to x+1)
                         && input[y-1 to x+1] == -1 ) flashesNearby ++

            // bottom right
            if (input.containsKey(y+1 to x+1)
                         && input[y+1 to x+1] == -1 ) flashesNearby ++

            // bottom left
            if (input.containsKey(y+1 to x-1)
                         && input[y+1 to x-1] == -1 ) flashesNearby ++

            // top left
            if (input.containsKey(y-1 to x-1)
                         && input[y-1 to x-1] == -1 ) flashesNearby ++

            updatedInput[key] = value + flashesNearby
        }
    }

    return updatedInput
}

private fun dec11_countOctopusesInGridWithNumber(input: HashMap<Pair<Int, Int>, Int>, number: Int): Int {
    var counter = 0

    input.forEach { (_, value) ->
        if (value == number) counter++
    }

    return counter
}