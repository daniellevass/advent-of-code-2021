
fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec12_input.txt")
    println("we have ${input.size} items")

    val processed = processInput(input)

    part2(processed)
}

private fun part1(input: HashMap<String, ArrayList<String>>) {
    var counter = 0
    val toVisit = ArrayList<Pair<String, ArrayList<String>>>()

    toVisit.add("start" to arrayListOf())

    while(toVisit.isNotEmpty()) {

        val firstNode = toVisit.first()
        toVisit.removeFirst()

        val at = firstNode.first
        val currentVisited = firstNode.second

        currentVisited.add(at)

        if (at == "end") {
            println(currentVisited)
            counter++
        } else {
            val candidates = input[at]!!

            candidates.map {
                if (it.first().isUpperCase()){
                    val array = arrayListOf<String>()
                    array.addAll(currentVisited.copyOf())
                    toVisit.add(it to array)
                } else {


                    if (currentVisited.contains(it)) {
                        // ignore we've been here before
                    } else {
                        val array = arrayListOf<String>()
                        array.addAll(currentVisited.copyOf())
                        toVisit.add(it to array)
                    }
                }
            }
        }
    }

    println("part1 = $counter")
}

private fun part2(input: HashMap<String, ArrayList<String>>) {
    var counter = 0
    val toVisit = ArrayList<Pair<String, ArrayList<String>>>()

    toVisit.add("start" to arrayListOf())

    while(toVisit.isNotEmpty()) {

        val firstNode = toVisit.first()
        toVisit.removeFirst()

        val at = firstNode.first
        val currentVisited = firstNode.second

        currentVisited.add(at)

        if (at == "end") {
//            println(currentVisited)
            counter++
        } else {
            val candidates = input[at]!!

            candidates.map { candidate ->
                if (candidate.first().isUpperCase()){
                    val array = arrayListOf<String>()
                    array.addAll(currentVisited.copyOf())
                    toVisit.add(candidate to array)
                } else {

                    // count all the clashes
                    val tempCopy = arrayListOf<String>()
                    tempCopy.addAll(currentVisited.copyOf())
                    tempCopy.add(candidate)

                    val copyWithoutCapitals = tempCopy
                        .filter { it.first().isLowerCase() }

                    var entry = 0
                    val clashes = copyWithoutCapitals
                        .groupingBy { it }
                        .eachCount()
                        .filter { it.value > 1 }
                        .mapValues {  if (it.value > entry) entry = it.value }

                    if (clashes.size > 1 || entry > 2) {
                        // ignore we've been here before
                    } else {
                        val array = arrayListOf<String>()
                        array.addAll(currentVisited.copyOf())
                        toVisit.add(candidate to array)
                    }
                }
            }
        }
    }

    println("part2 = $counter")
}

private fun processInput(input: List<String>): HashMap<String, ArrayList<String>> {
    val output = HashMap<String, ArrayList<String>>()

    input.map { row ->
        val parts = row.split("-").filterNot { it.isBlank() }

        val key = parts[0]
        val value = parts[1]

        if (value != "start") {
            if (output.containsKey(key)) {
                //append to the array
                val array = output[key]!!
                array.add(value)
                output[key] = array
            } else {
                output[key] = arrayListOf(value)
            }
        }

        if (key != "start") {
            if (output.containsKey(value)) {
                //append to the array
                val array = output[value]!!
                array.add(key)
                output[value] = array
            } else {
                output[value] = arrayListOf(key)
            }
        }

    }

    return output
}