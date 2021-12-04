
fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec3_input.txt")

    println("we have ${input.size} items")

    dec3_part1(input)
}

data class Dec3Record(var zeros: Int, var ones: Int)

private fun dec3_createEmptyRecords(): ArrayList<Dec3Record> = arrayListOf(
    Dec3Record(0, 0),
    Dec3Record(0, 0),
    Dec3Record(0, 0),
    Dec3Record(0, 0),
    Dec3Record(0, 0),
    Dec3Record(0, 0),
    Dec3Record(0, 0),
    Dec3Record(0, 0),
    Dec3Record(0, 0),
    Dec3Record(0, 0),
    Dec3Record(0, 0),
    Dec3Record(0, 0)
)

fun dec3_part1(input: List<String>) {

    val records = dec3_generateRecords(input)

    println(records)

    var mostPopular = ""
    var leastPopular = ""

    for (r in records.indices) {
        if (records[r].ones > records[r].zeros) {
            mostPopular +="1"
            leastPopular += "0"
        } else {
            mostPopular += "0"
            leastPopular += "1"
        }
    }
    println("most popular= $mostPopular | least popular= $leastPopular")

    val mostPopularInt = mostPopular.toInt(2)
    val leastPopularInt = leastPopular.toInt(2)

    println("most popular= $mostPopularInt | least popular= $leastPopularInt")

    val multiplication = mostPopularInt * leastPopularInt
    println("multiplication=$multiplication")

    dec3_part2(input, records)
}

fun <T> List<T>.copyOf(): List<T> {
    val original = this
    return mutableListOf<T>().apply { addAll(original) }
}

fun dec3_part2(input:List<String>, records: List<Dec3Record>) {

    // to find oxygen level

    var inputCopy = input.copyOf()
    var recordCopy = records.copyOf()
    var index = 0
    var found = false
    val possibleOxygenInput = arrayListOf<String>()

    while (index < input[0].length && !found) {
        var matcher = "1"
        if (recordCopy[index].zeros > recordCopy[index].ones) matcher = "0"
        for (i in inputCopy.indices) {
            // if the item matches - add it to possibleOxygenInput
            if (inputCopy[i].substring(index, index+1) == matcher) possibleOxygenInput.add(inputCopy[i])
        }

        if (possibleOxygenInput.size == 1) {
            found = true
        } else {
            recordCopy = dec3_generateRecords(possibleOxygenInput)
            inputCopy = possibleOxygenInput.copyOf()
//            println("possible oxygen input = $possibleOxygenInput")
            possibleOxygenInput.clear()
        }
        index++
    }

    val oxygenInput = possibleOxygenInput.first().toInt(2)
    println("oxygen = $oxygenInput")


    inputCopy = input.copyOf()
    recordCopy = records.copyOf()
    index = 0
    found = false
    val possibleCo2 = arrayListOf<String>()

    while (index < input[0].length && !found) {
        var matcher = "0"
        if (recordCopy[index].zeros > recordCopy[index].ones) matcher = "1"
        for (i in inputCopy.indices) {
            // if the item matches - add it to possibleOxygenInput
            if (inputCopy[i].substring(index, index+1) == matcher) possibleCo2.add(inputCopy[i])
        }

        if (possibleCo2.size == 1) {
            found = true
        } else {
            recordCopy = dec3_generateRecords(possibleCo2)
            inputCopy = possibleCo2.copyOf()
//            println("possible co2 input = $possibleCo2")
            possibleCo2.clear()
        }
        index++
    }

    val co2Input = possibleCo2.first().toInt(2)
    println("co2 = $co2Input")

    val multiplication = co2Input * oxygenInput
    println("part2 = $multiplication")


}

private fun dec3_generateRecords(input: List<String> ) : List<Dec3Record> {
    var records = dec3_createEmptyRecords()

    // find most popular items
    for (i in input.indices) {
        val parts = input[i].split("")

        for (p in parts.indices) {
            if (parts[p].isNotEmpty()) {
                val record = records[p-1]

                if (parts[p] == "0") record.zeros ++
                if (parts[p] == "1") record.ones ++

                records[p-1] = record
            }
        }
    }
    return records
}
