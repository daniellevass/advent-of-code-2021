
fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec8_input.txt")
    println("we have ${input.size} items")

    dec8_part1(input)
    dec8_part2(input)

}
private val oneDigit = 2
private val sevenDigit = 3
private val fourDigit = 4
private val eightDigit = 7

private fun dec8_part1(input: List<String>) {
    val digitsWeCreAbout = arrayListOf<Int>(oneDigit, sevenDigit, fourDigit, eightDigit)
    var counter = 0

    input.map { it ->

        val parts = it.split(" | ")

        val outputValues = parts[1].split(" ").filterNot { it.isBlank() }
        outputValues.map { output ->
            if (digitsWeCreAbout.contains(output.length)) counter++
        }
    }

    println("part1 = $counter")

}

private fun dec8_part2(input: List<String>) {
    var counter = 0
    input.map { record ->
        counter += dec8_processRow(record)
    }

    println("part2 = $counter")
}

fun dec8_processRow(row: String) : Int {
    val parts = row.split(" | ")

    val firstPart = parts[0].split(" ").filterNot { it.isBlank() }
    val outputValues = parts[1].split(" ").filterNot { it.isBlank() }

    val digits = HashMap<Int, String>()

    // populate an indexed map with the easy digits
    firstPart.map {
        if (it.length == oneDigit) digits[1] = it
        if (it.length == fourDigit) digits[4] = it
        if (it.length == sevenDigit) digits[7] = it
        if (it.length == eightDigit) digits[8] = it
    }

    // numbers we still need to find
    // two -> 5 length -> should match 2 characters from a 7? (so does a 5)
    // five = 5 length -> 5 should match 3 digits from a 4 (a 2 should match only 2)
    firstPart.map {
        // we have either a 6 9 or 0
        if (it.length == 6) {
            if (dec8_howManyOverlaps(it, digits[1]!!) == 2) {
                // can be a zero or a one here!
                if (dec8_howManyOverlaps(it, digits[4]!!) == 4) {
                    digits[9] = it
                } else {
                    digits[0] = it
                }

            } else {
                digits[6] = it
            }
        }

        if (it.length == 5) {
            if (dec8_howManyOverlaps(it, digits[1]!!) == 2) {
                digits[3] = it
            } else {

                if (dec8_howManyOverlaps(it, digits[4]!!) == 3) {
                    digits[5] = it
                } else {
                    digits[2] = it
                }

            }
        }
    }

    var stringDigit = ""
    outputValues.map {
        stringDigit += dec8_findKeyForValue(it, digits)
    }

    return stringDigit.toInt()
}

fun dec8_howManyOverlaps(input: String, digit: String) : Int {
    var overlaps = 0

    val inputParts = input.split("").filterNot { it.isBlank() }
    val digitParts = digit.split("").filterNot { it.isBlank()}

    val result = digitParts.filter { inputParts.contains(it)}

    return result.size
}

fun dec8_findKeyForValue(value: String, map: HashMap<Int, String>) : Int {
    var key = -1

    val valueParts =value.split("").filterNot { it.isBlank() }


    map.map { (k, v) ->
        val vParts = v.split("").filterNot { it.isBlank() }

        if (valueParts.containsAll(vParts) && vParts.containsAll(valueParts)) {
            key = k
        }
    }

    return key
}
