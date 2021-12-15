import java.lang.Math.abs

fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec14_input.txt")
    println("we have ${input.size} items")
    val processedInput = processInput(input)

    val start = processedInput.first
    val rules = processedInput.second
    println("we start with $start and have ${rules.size} rules")

//    part1(start, rules)
    part2(start, rules)
}

private fun processInput(input: List<String>) : Pair<String, List<Pair<String, String>>> {
    val start = input.first()
    val rules = arrayListOf<Pair<String, String>>()

    for(i in 2 until input.size) {
        val row = input[i].split(" -> ").filterNot {it.isBlank() }
        rules.add(row.first() to row.last())
    }

    return (start to rules)
}

private fun part1(start: String, rules: List<Pair<String, String>>) {

    var processedStart = start
    for (step in 1 .. 10) {
        println("step $step")
        val thingsToDo = arrayListOf<Pair<Int, String>>()
        rules.map { rule ->
            if (processedStart.contains(rule.first)) {
                val indexes = processedStart.indexesOf(rule.first)
                indexes.map { i ->
                    thingsToDo.add(i +1 to rule.second)
                }
            }
        }

        val sortedThingsToDo = thingsToDo.sortedBy { it.first }
//        println(sortedThingsToDo)

        var updatedStart = processedStart
        sortedThingsToDo.mapIndexed { index, pair ->
//            println("inserting ${pair.second} into ${pair.first} plus $index")
            updatedStart = updatedStart.insert(pair.first + index, pair.second)
        }

        processedStart = updatedStart
//        println(processedStart)
    }

    val counts = processedStart.split("")
        .filterNot { it.isBlank() }
        .groupingBy { it }
        .eachCount()

    var smallest: Int? = null
    var largest: Int? = null
    counts.forEach { key, value ->
        if (smallest == null) {
            smallest = value
            largest = value
        } else {
            if (smallest!! > value) smallest = value
            if (largest!! < value) largest = value

        }
    }

//    println(counts)
    println("first = $largest | last = $smallest")
    val calculation = largest!! - smallest!!
    println("part1 = $calculation")

}

private fun part2(start: String, rules: List<Pair<String, String>>) {

    // we can't save strings for part 2 -> it's too expensive break it up into an array
    var map = HashMap<String, Long>()
    for (i in 0 until start.length -1) {
        val key = start.substring(i, i+2)

        if (map.containsKey(key)) {
            val counter = map[key]!!
            map[key] = counter + 1
        } else {
            map[key] = 1
        }
    }
//    println(map)

    // convert the rules to a map for faster lookups
    val ruleMap = HashMap<String, String>()
    rules.map {
        ruleMap[it.first] = it.second
    }

    for (step in 1 .. 40) {
        val updatedMap = HashMap<String, Long>()
        map.forEach { (key, value) ->
            if (ruleMap.contains(key)) {
                // split up the key into two parts
                val key1: String = key.first().toString() + ruleMap[key]!!
                val key2: String = ruleMap[key]!! + key.last().toString()

                if (updatedMap.contains(key1)) {
                    val counter = updatedMap[key1]!!
                    updatedMap[key1] = counter + value
                } else {
                    updatedMap[key1] = value
                }

                if (updatedMap.contains(key2)) {
                    val counter = updatedMap[key2]!!
                    updatedMap[key2] = counter + value
                } else {
                    updatedMap[key2] = value
                }
            }
        }
        map = updatedMap
    }


    val counters = countCharsInMap(map)
    val calculation = (counters.second - counters.first)/2
    println("part2 = $calculation")
}

private fun countCharsInMap(map: HashMap<String, Long>) : Pair<Long, Long> {

    val counters = HashMap<Char, Long>()

    map.forEach { (key, value) ->
        val char1 = key.first()
        val char2 = key.last()

        if (counters.containsKey(char1)) {
            val count = counters[char1]!!
            counters[char1] = count + value
        } else {
            counters[char1] = value
        }

        if (counters.containsKey(char2)) {
            val count = counters[char2]!!
            counters[char2] = count + value
        } else {
            counters[char2] = value
        }

    }

    println(counters)
    val sortedCounters = counters.entries.sortedBy { it.value }

    return (sortedCounters.first().value to sortedCounters.last().value )
}

fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int> {
    tailrec fun String.collectIndexesOf(offset: Int = 0, indexes: List<Int> = emptyList()): List<Int> =
        when (val index = indexOf(substr, offset, ignoreCase)) {
            -1 -> indexes
            else -> collectIndexesOf(index + 1, indexes + index)
        }

    return when (this) {
        null -> emptyList()
        else -> collectIndexesOf()
    }
}

fun String.insert(index: Int, string: String): String {
    return this.substring(0, index) + string + this.substring(index, this.length)
}