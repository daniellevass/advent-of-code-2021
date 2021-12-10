
fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec9_input.txt")
    println("we have ${input.size} items")

    val grid = dec9_processGrid(input)
    println("we have a grid ${grid.size} by ${grid.first().size}")

    dec9_part1(grid)
}

private fun dec9_processGrid(input: List<String>): List<List<Int>>{
    val map = arrayListOf<List<Int>>()

    input.map { record->
        val row: List<Int> = record.split("").filterNot { it.isBlank() }.map { it.toInt() }
        map.add(row)
    }

    return map
}

private fun dec9_part1(grid: List<List<Int>>) {

    val maxY = grid.size -1
    val maxX = grid.first().size -1 // we're assuming the grid is always square

    val lowestPoints = arrayListOf<Int>()
    val lowestPointsCoordinates = arrayListOf<Pair<Int, Int>>()

    for (y in 0 .. maxY) {
        for (x in 0 .. maxX){

            var top: Int? = null
            if (y > 0) top = grid[y-1][x]

            var bottom: Int? = null
            if (y < maxY) bottom = grid[y+1][x]

            var left: Int? = null
            if (x > 0) left = grid[y][x-1]

            var right: Int? = null
            if (x < maxX) right = grid[y][x+1]

            val isLowest = dec9_isThisLowest(
                center = grid[y][x],
                top,
                bottom,
                left,
                right
            )

            if (isLowest) {
                lowestPoints.add(grid[y][x])
                lowestPointsCoordinates.add(y to x)
//                println("adding coordinates $y, $x")
            }
        }
    }

    println("lowest points are : $lowestPoints")
    var calculation = 0
    lowestPoints.map {
        calculation += (it +1)
    }

    // is not 1762 or 74038
    println("part1 = $calculation")

    dec9_part2(lowestPointsCoordinates, grid)
}

private fun dec9_isThisLowest(center:Int, top:Int?, bottom:Int?, left:Int?, right:Int?): Boolean{
//    println("we made it to item $center")
    val items = arrayListOf(center, top, bottom, right, left).filterNotNull().sorted()

    if (items.first() == center) {
        // potentially no items lower -> we need to check they aren't *all* the same number

        if (items.last() == center) {
            return false //they're all the same-> so not lower
        } else {
            return true
        }

    } else {
        return false
    }
}

private fun dec9_part2(lowestCoordinates: List<Pair<Int, Int>>, grid: List<List<Int>>) {

    val basins = arrayListOf<List<Pair<Int, Int>>>()
    lowestCoordinates.map {
        basins.add(dec9_calculateSizeOfBasin(it, grid).distinct())
    }

    // find the biggest three items
    // (just need to check *they're* unique)
    val sortedBasins = basins.sortedByDescending { it.size }
    val biggestBasins = arrayListOf(sortedBasins.first())
    var complete = false
    var index = 0
    while (!complete && index < sortedBasins.size) {

        val proposedBasin = sortedBasins[index]

        if (!dec9_checkIfCoordinatesAreAlreadyContained(proposedBasin, biggestBasins)) {
            biggestBasins.add(proposedBasin)
        }

        if (biggestBasins.size == 3) {
            complete = true
        }

        index ++
    }
//    println(biggestBasins)
    var calculation = 0
    biggestBasins.map {
        println(it.size)

        if (calculation == 0) {
            calculation = it.size
        } else {
            calculation *=it.size
        }
    }

    println("part2 = $calculation")
}

private fun dec9_checkIfCoordinatesAreAlreadyContained(proposedBasin: List<Pair<Int, Int>>,
                                                       basins: List<List<Pair<Int, Int>>>) : Boolean {

    basins.map { basin ->
        if (proposedBasin.any { it in basin}) return true
    }

    return false
}


private fun dec9_calculateSizeOfBasin(lowPoint: Pair<Int, Int>, grid: List<List<Int>>) : List<Pair<Int, Int>>{
    val basinCoordinates = arrayListOf<Pair<Int, Int>>(lowPoint)
    val unsearchedCoordinates = arrayListOf<Pair<Int, Int>>(lowPoint)
    var complete = false
    println("calculating size for ${lowPoint.first}, ${lowPoint.second}")

    while (!complete) {

        val newCoordinates = arrayListOf<Pair<Int, Int>>()
        unsearchedCoordinates.map {
            val around = dec9_getItemsAround(it, grid)
            newCoordinates.addAll(dec9_areCoordinatesAroundPartOfBasin(around))
        }

        // we now need to filter coordinated we already have in either our basin coordinated, or unsearched coordinates
        val filteredList = newCoordinates
            .filter { !basinCoordinates.contains(it) }
            .filter { !unsearchedCoordinates.contains(it) }

        basinCoordinates.addAll(unsearchedCoordinates)

        if (filteredList.isEmpty()) {
            // no new coordinates we can finish
            complete = true
        } else {
            basinCoordinates.addAll(unsearchedCoordinates)
            unsearchedCoordinates.clear()
            unsearchedCoordinates.addAll(newCoordinates)
        }
    }

    return basinCoordinates
}

// find all the coordinates nearby
private fun dec9_getItemsAround(center: Pair<Int, Int>, grid: List<List<Int>>): List<Triple<Int, Int, Int>> {
    val maxY = grid.size -1
    val maxX = grid.first().size -1 // we're assuming the grid is always square
    val around = arrayListOf<Triple<Int, Int, Int>>()

    //top
    if (center.first > 0) around.add(Triple((center.first -1), center.second, grid[center.first -1][center.second]))

    //bottom
    if (center.first < maxY) around.add(Triple((center.first +1), center.second, grid[center.first +1][center.second]))

    //left
    if (center.second > 0) around.add(Triple(center.first, (center.second -1), grid[center.first][center.second -1]))

    //right
    if (center.second < maxX) around.add(Triple(center.first, (center.second +1), grid[center.first][center.second +1]))

    return around
}

private fun dec9_areCoordinatesAroundPartOfBasin(around:List<Triple<Int, Int, Int>>): List<Pair<Int, Int>> {
    val basinCoordinates = arrayListOf<Pair<Int, Int>>()

    around.map {
        if (it.third != 9) basinCoordinates.add(it.first to it.second)
    }

    return basinCoordinates
}
