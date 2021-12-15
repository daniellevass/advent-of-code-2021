import java.lang.Math.abs

fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec15_input.txt")
    println("we have ${input.size} items")
//    val processedInput = processInput(input)
//    println(processedInput)

    //63 is incorrect
    //594 too high
    //part1(processedInput, input.size)

//    part2(processedInput, input.size)
    val processedInput = processInputPart2(input)
    part2(processedInput, (input.size*5))


}

private fun processInput(input: List<String>): HashMap<Pair<Int, Int>, Int> {
    val map = HashMap<Pair<Int, Int>, Int>()
    input.mapIndexed { y, row ->
        val items = row.split("").filterNot { it.isBlank() }.map { it.toInt() }
        items.mapIndexed { x, item -> map.put(y to x, item)  }
    }
    return map
}

private fun processInputPart2(input: List<String>): HashMap<Pair<Int, Int>, Int> {
    val map = HashMap<Pair<Int, Int>, Int>()
    val gridSize = input.size
    input.mapIndexed { y, row ->
        val items = row.split("").filterNot { it.isBlank() }.map { it.toInt() }
        items.mapIndexed { x, item ->

            // put original in
            map[y to x] = item

            // put first row
            for (increaseHorizontal in 1 .. 4) {
                var updatedItem = item + increaseHorizontal
                if (updatedItem > 9) updatedItem %= 9
                map[y to x+(gridSize * increaseHorizontal)] = updatedItem
            }

            // put second/third/fourth/fifth row
            for (increaseVertical in 1.. 4) {
                for(increaseH in 0..4) {
                    var updatedItem = item + increaseVertical + increaseH
                    if (updatedItem > 9) updatedItem %= 9
                    map[y+ (gridSize *increaseVertical) to x+(gridSize * increaseH)] = updatedItem
                }
            }
        }
    }
    return map
}

private data class Dec15Node(val x: Int, val y: Int, val risk: Int)

private fun part1_doesntwork(map: Map<Pair<Int, Int>, Int>, gridSize: Int) {

    // you can either move down/right/up/left but ensure we haven't visited it before
    val toVisit = ArrayList<Pair<Dec15Node, ArrayList<Dec15Node>>>()
    val potentialRoutes = arrayListOf<List<Dec15Node>>()

    val startRisk = map[0 to 0]!!
    toVisit.add(Dec15Node(0, 0, startRisk) to arrayListOf())

    val biggestX = gridSize -1
    val biggestY = gridSize -1

    while (toVisit.isNotEmpty()) {
        val firstNode = toVisit.first()
        toVisit.removeFirst()

        val at = firstNode.first
        val currentVisited = firstNode.second
        currentVisited.add(at)
//        println("we're at $at")

        // check if we're at the end
        if (at.x == biggestX && at.y == biggestY) {
//            println(currentVisited)
            potentialRoutes.add(currentVisited)
//            println("we have ${potentialRoutes.size}")
        } else {

            var top:Dec15Node? = null
            if (map.containsKey(at.x to at.y-1)){
                top = Dec15Node(at.x, at.y-1, map[at.x to at.y-1]!!)
            }

            var bottom:Dec15Node? = null
            if (map.containsKey(at.x to at.y+1)){
                bottom = Dec15Node(at.x, at.y+1, map[at.x to at.y+1]!!)
            }

            var right:Dec15Node? = null
            if (map.containsKey(at.x+1 to at.y)){
                right = Dec15Node(at.x+1, at.y, map[at.x+1 to at.y]!!)
            }

            var left:Dec15Node? = null
            if (map.containsKey(at.x-1 to at.y)){
                left = Dec15Node(at.x-1, at.y, map[at.x-1 to at.y]!!)
            }

            val nextCandidates = arrayListOf(top, bottom, right, left).filterNotNull()

            nextCandidates.map { candidate ->
                if (currentVisited.contains(candidate)) {
                    // ignore we've been here before
                } else {
                    if (candidate.risk < 7) {
                        val array = arrayListOf<Dec15Node>()
                        array.addAll(currentVisited.copyOf())
                        toVisit.add(candidate to array)
                    }
                }
            }
        }
    }

    println("we have ${potentialRoutes.size}")
}

private fun convertMapToPaths(input:HashMap<Pair<Int, Int>, Int>,
                              gridXMax: Int, gridYMax:Int,
                              gridXMin: Int, gridYMin:Int,
)
: Map<Pair<String, String>, Int> {
    // convert input to list of paths
    val paths = hashMapOf<Pair<String, String>, Int>()

    input.forEach { (key, danger) ->
        val endCoordinate = "${key.first},${key.second}"
//        println("end coordinate = $endCoordinate")

        if (key.second > gridXMin) {
            val leftCoordinate = "${key.first},${key.second-1}"
            paths[leftCoordinate to endCoordinate] = danger
//            println("adding left $leftCoordinate $endCoordinate to $danger")
        }

        if (key.first > gridYMin) {
            val topCoordinate = "${key.first-1},${key.second}"
            paths[topCoordinate to endCoordinate] = danger
//            println("adding top $topCoordinate $endCoordinate to $danger")
        }

        if (key.first < gridYMax) {
            val bottom = "${key.first+1},${key.second}"
            paths[bottom to endCoordinate] = danger
//            println("adding bottom #bottom $endCoordinate to $danger")
        }

        if (key.second < gridXMax) {
            val right = "${key.first},${key.second+1}"
            paths[right to endCoordinate] = danger
//            println("adding right $right $endCoordinate to $danger")
        }

    }

    return paths
}

private fun part1(input:HashMap<Pair<Int, Int>, Int>, gridSize: Int) {
    val paths = convertMapToPaths(input, gridSize, gridSize, 0, 0)

    val shortestPathTree = dijkstra(Graph(weights = paths), "0,0")
//    println("made shortest path tree")
    val endCoord = "${gridSize-1},${gridSize-1}"
    val shortestPath = shortestPath(shortestPathTree, "0,0", endCoord)
//    println(shortestPath)

    var count = 0
    for (i in 1 until shortestPath.size) {
        val key = (shortestPath[i-1] to shortestPath[i])
        count += paths[key]!!
    }

//    println("part1 = $count")
}

private fun countShortestPath(paths: Map<Pair<String, String>, Int>, shortestPath: List<String>) : Int {
    var count = 0
    for (i in 1 until shortestPath.size) {
        val key = (shortestPath[i-1] to shortestPath[i])
        count += paths[key]!!
    }
    return count
}

private fun part2_broken(input:HashMap<Pair<Int, Int>, Int>, gridSize: Int) {
    // so the problem is we have too many route options -> let's divide the grid up into 4? sections
    // save the distance between each edge coordinate

    val splits = 2

    // for grid1
    val grid1 = HashMap<Pair<Int, Int>, Int>() // top left
    val grid2 = HashMap<Pair<Int, Int>, Int>() // top right
    val grid3 = HashMap<Pair<Int, Int>, Int>() // bottom left
    val grid4 = HashMap<Pair<Int, Int>, Int>() // bottom right

    input.forEach { (key, danger) ->
        if (key.first < (gridSize / splits) || key.second < (gridSize / splits)) {
            grid1[key] = danger
        } else if (key.first >= (gridSize / splits) || key.second < (gridSize / splits)) {
            grid2[key] = danger
        } else if (key.first < (gridSize / splits) || key.second >= (gridSize / splits)) {
            grid3[key] = danger
        } else {
            grid4[key] = danger
        }
    }

    val paths1 = convertMapToPaths(grid1, gridSize/splits, gridSize/splits, 0, 0)
    val tree1 = dijkstra(Graph(weights = paths1), "0,0")

    val completePaths = HashMap<Pair<String, String>, Int>()

    // we need to do all top and left coordinates to bottom and right coordinates
    for (x in 0 until (gridSize / splits)) {
        val start = "0,$x"
        // all bottom ones
        val end = "${(gridSize/2) -1},$x"
        val shortestPath = countShortestPath(paths1, shortestPath(tree1, start, end))
        completePaths[start to end] = shortestPath
    }

    println(completePaths)
}

private fun part2(input:HashMap<Pair<Int, Int>, Int>, gridSize: Int) {

    val simplifiedPaths = HashMap<Pair<String, String>, Int>()
    for (y in 0 until gridSize step 10) {
        for (x in 0 until gridSize step 10) {
            println("adding paths for y=$y and x=$x")
            var maxX = x +10
            var maxY = y+10

            if (maxX == gridSize) maxX--
            if (maxY== gridSize)maxY--

            simplifiedPaths.putAll(findSimplifiedPathsFor(x, maxX, y, maxY, input))
        }
    }

    println("we have ${simplifiedPaths.size}")

    val tree = dijkstra(Graph(simplifiedPaths), "0,0")
    val shortestPathTree = shortestPath(tree, "0,0", "${gridSize -1},${gridSize -1}")

//    println("shortest path has length = ${shortestPathTree.size}")
    println(shortestPathTree)
    println(countShortestPath(simplifiedPaths, shortestPathTree))


}

private fun findSimplifiedPathsFor(minX: Int, maxX: Int, minY: Int, maxY: Int,
                                   input: HashMap<Pair<Int, Int>, Int>): HashMap<Pair<String, String>, Int> {
    val simplifiedPaths = HashMap<Pair<String, String>, Int>()
    // let's chunk our board into 10 by 10 grids
    val items = getItemsInRange(minX, maxX, minY, maxY, input)
//    println("we have ${items.size} items")
    val paths = convertMapToPaths(items, maxX, maxY, minX, minY)
//    println("we have ${paths.size} paths")

//    println(paths)

//    println("got tree")

    val edges = arrayListOf<Pair<Int, Int>>()
    for (x in minX..maxX) {
        // add top
        edges.add(minY to x)
        // bottom
        edges.add(maxY to x)
    }

    for (y in minY..maxY) {
        // add left
        edges.add(y to minX)
        // right
        edges.add(y to maxX)
    }

    for (start in edges) {
        for (end in edges) {
            if (start != end) {
                val startCoord = "${start.first},${start.second}"
                val endCoord = "${end.first},${end.second}"
                val tree = dijkstra(Graph(weights = paths), startCoord)
                val shortestPath = shortestPath(tree, startCoord, endCoord)
                simplifiedPaths[startCoord to endCoord] = countShortestPath(paths, shortestPath)
            }
        }
    }


    return simplifiedPaths
}

private fun getItemsInRange(minX: Int, maxX: Int, minY: Int, maxY: Int,
                            input: HashMap<Pair<Int, Int>, Int>) : HashMap<Pair<Int, Int>, Int>{
    val grid = HashMap<Pair<Int, Int>, Int>()
    input.forEach { (key, danger) ->
        // if key is in our range - we can accept it into our board!
        if (key.second in minX..maxX
            && key.first in minY..maxY ) {
            grid[key] = danger
        }
    }
    return grid
}



private fun <T> List<Pair<T, T>>.getUniqueValuesFromPairs(): Set<T> = this
    .map { (a, b) -> listOf(a, b) }
    .flatten()
    .toSet()

private fun <T> List<Pair<T, T>>.getUniqueValuesFromPairs(predicate: (T) -> Boolean): Set<T> = this
    .map { (a, b) -> listOf(a, b) }
    .flatten()
    .filter(predicate)
    .toSet()

private data class Graph<T>(
    val vertices: Set<T>,
    val edges: Map<T, Set<T>>,
    val weights: Map<Pair<T, T>, Int>
) {
    constructor(weights: Map<Pair<T, T>, Int>): this(
        vertices = weights.keys.toList().getUniqueValuesFromPairs(),
        edges = weights.keys
            .groupBy { it.first }
            .mapValues { it.value.getUniqueValuesFromPairs { x -> x !== it.key } }
            .withDefault { emptySet() },
        weights = weights
    )
}

private fun <T> dijkstra(graph: Graph<T>, start: T): Map<T, T?> {
    val S: MutableSet<T> = mutableSetOf() // a subset of vertices, for which we know the true distance

    /*
     * delta represents the length of the shortest distance paths
     * from start to v, for v in vertices.
     *
     * The values are initialized to infinity, as we'll be getting the key with the min value
     */
    val delta = graph.vertices.map { it to Int.MAX_VALUE }.toMap().toMutableMap()
    delta[start] = 0

    val previous: MutableMap<T, T?> = graph.vertices.map { it to null }.toMap().toMutableMap()

    while (S != graph.vertices) {
        // let v be the closest vertex that has not yet been visited

        val v: T = delta
            .filter { !S.contains(it.key) }
            .minByOrNull { it.value }!!
            .key

//        println("v=$v")
        graph.edges.getValue(v).minus(S).forEach { neighbor ->
                if (v != neighbor) {
//                    println("asking for $v and $neighbor")
                    val d = delta.getValue(v)
                    val g = graph.weights.getValue(Pair(v, neighbor))

                    val newPath = d + g

                    if (newPath < delta.getValue(neighbor)) {
                        delta[neighbor] = newPath
                        previous[neighbor] = v
                    }
                }
        }

        S.add(v)
    }

    return previous.toMap()
}

private fun <T> shortestPath(shortestPathTree: Map<T, T?>, start: T, end: T): List<T> {
    fun pathTo(start: T, end: T): List<T> {
        if (shortestPathTree[end] == null) return listOf(end)
        return listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
    }

    return pathTo(start, end)
}


