import java.lang.Math.abs

fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec13_input.txt")
    println("we have ${input.size} items")

    val processedInput = processInput(input)
    var grid = processedInput.first
    val folds = processedInput.second
    println("we have ${folds.size} folds")
    println("we have ${grid.countDots()} dots")

    folds.map { fold->
        val axis = fold.first
        val digit = fold.second

        if (axis == "x") {
            val updatedGrid = foldGridAlongXAxis(grid, digit)
            grid = updatedGrid
        } else {
            val updatedGrid = foldGridAlongYAxis(grid, digit)
            grid = updatedGrid
        }
    }

    println("we have ${grid.countDots()} dots")
    grid.printDigits()
}

private class Grid {
    val map = HashMap<Pair<Int, Int>, Boolean>()

    fun setCoordinate(x: Int, y: Int) {
        map[x to y] = true
    }

    fun countDots() : Int {
        return map.size
    }

    fun printDigits() {
        for (y in 0.. 10) {

            var row = ""

            for (x in 0 .. 50) {
                if (map.containsKey(x to y)){
                    row += "#"
                } else {
                    row += " "
                }
            }
            println(row)
        }
    }
}

private fun processInput(input: List<String>) : Pair<Grid, ArrayList<Pair<String, Int>>> {

    val grid = Grid()
    var isGridMode = true
    val folds = arrayListOf<Pair<String, Int>>()

    input.map {
        if (it.isBlank()) {
            // swap
            isGridMode = false
        } else if (isGridMode) {
            val items = it.split(",").filterNot { i -> i.isBlank() }
            grid.setCoordinate(items.first().toInt(), items.last().toInt())
        } else {
            val items = it.split("=").filterNot { i -> i.isBlank() }
            var axis = "y"
            if (items.first().contains("x")) {
                axis = "x"
            }

            folds.add(axis to items.last().toInt())
        }
    }

    return (grid to folds)
}

// folding along x axis means y values stay the same,
// x values bigger than the number become smaller
// e.g. if we fold along x=6,
//                      (7,0) becomes (5,0) -> 6-7 = -1 -> y-1 = 5
//                      (8,0) becomes (4,0) -> 6-8 = -2 -> y-2 = 4
//                      (9,0) becomes (3,0)
private fun foldGridAlongXAxis(grid: Grid, xAxis: Int) : Grid {
    val updatedGrid = Grid()

    grid.map.forEach { (key, _) ->
        var x = key.first
        val y = key.second

        if (x > xAxis) {
            val updatedX = xAxis - abs(xAxis - x)
            x = updatedX
        }

        updatedGrid.setCoordinate(x, y)
    }

    return updatedGrid
}

private fun foldGridAlongYAxis(grid: Grid, yAxis: Int) : Grid {
    val updatedGrid = Grid()

    grid.map.forEach { (key, _) ->
        val x = key.first
        var y = key.second

        if (y > yAxis) {
            val updatedY = yAxis - abs(yAxis - y)
            y = updatedY
        }

        updatedGrid.setCoordinate(x, y)
    }

    return updatedGrid
}
