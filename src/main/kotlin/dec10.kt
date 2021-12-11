
fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec10_input.txt")
    println("we have ${input.size} items")

    dec10_part1(input)
}

private fun dec10_part1(input: List<String>) {
    var count = 0
    val incompleteItems = arrayListOf<String>()

    input.map {
        val increment = dec10_findIllegalChar(it)
        count += increment

        if (increment == 0) incompleteItems.add(it)
    }

    println("part1 = $count")

    dec10_part2(incompleteItems)
}

private fun dec10_part2(input: List<String>) {

    val items = arrayListOf<Long>()

    input.map {
        items.add(dec10_finishLine(it))
    }

    println("we got ${items.size} items")

    val middleIndex = (items.size/2)
    val middle = items.sorted()[middleIndex]

    println("part2 = $middle")
}

private val bracketScore = 3
private val squareBracketScore = 57
private val squigglyBracketScore = 1197
private val triangleBracketScore = 25137

private fun determineScoreForChar(char: String): Int {
    return when (char) {
        ")" -> bracketScore
        "]" -> squareBracketScore
        "}" -> squigglyBracketScore
        ">" -> triangleBracketScore
        else -> 0
    }
}

private fun determineScoreForStart(char: String): Long {
    return when (char) {
        "(" -> 1
        "[" -> 2
        "{" -> 3
        "<" -> 4
        else -> 0
    }
}

fun dec10_determineIfCharMatches(start: String, end: String ): Boolean {
    when (start) {
        "(" -> return end == ")"
        "[" -> return end == "]"
        "{" -> return end == "}"
        "<" -> return end == ">"
    }
    return false
}

private val startElements = arrayListOf("(", "{", "[", "<")

fun dec10_findIllegalChar(input: String) : Int {
    val parts = input.split("").filterNot { it.isBlank() }

    var found = false
    var index = 0
    val processedParts = arrayListOf<String>()
    var value = 0

    while(index < parts.size && !found) {

        val part = parts[index]

        if (startElements.contains(part)) {
            processedParts.add(part)
        } else {
            //is end element -> does it match the last record
           val start = processedParts.last()

            if (dec10_determineIfCharMatches(start, part)) {
                processedParts.removeLast()
            } else {
                // invalid char
                found = true
                value = determineScoreForChar(part)
                println("invalid char = $part costing $value")
            }

        }

        index++
    }

    return value
}

fun dec10_finishLine(line: String) : Long {
    val parts = line.split("").filterNot { it.isBlank() }
    val processedParts = arrayListOf<String>()

    parts.map {
        if (startElements.contains(it)) {
            processedParts.add(it)
        } else {
            // we can assume the parts match because they're not invalid!
            processedParts.removeLast()
        }
    }

    // processed parts should now have a list of incomplete starts
    var calculation = 0L
    processedParts.reversed().map {

        calculation *= 5
//        println("multiplied by 5 = $calculation")

        val increment = determineScoreForStart(it)
//        println("$it increments by $increment")
        if (calculation == 0L) {
            calculation = increment
        } else {
            calculation += increment
        }

//        println("new calculation = $calculation")
    }
    return calculation
}
