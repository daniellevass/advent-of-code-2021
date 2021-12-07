
import kotlin.math.abs

fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec7_input.txt")
    println("we have ${input.size} items")

    val crabs = input.first().split(",").filterNot { it.isBlank() }.map { it.toInt() }.sorted()
    println("we have ${crabs.size} crabs")

    dec7_part1(crabs)
}

private fun dec7_part1(crabs: List<Int>) {

    var currentSmallestFuel = dec7_alignCrabs(crabs, crabs.last())

    // loop through each possible index between where crab1 is, and the crabN
    for (i in crabs.first() until crabs.last()) {

        //calculate how much energy it would cost to get all crabs there
        val newCost = dec7_alignCrabs(crabs, i)
        if (newCost < currentSmallestFuel) {
            currentSmallestFuel = newCost
        }
    }

    println("part1 = $currentSmallestFuel")
}

private fun dec7_alignCrabs(crabs: List<Int>, position: Int): Int {
    var count = 0
    crabs.map {
        // e.g. if it is 10 and we want to get to 20 it will cost us 10
        // e.g. if it is 30 and we want to get to 20 it will cost us 10
        val steps = abs(it - position)

        for (step in 1.. steps) {
            count += step
        }

    }
    return count
}
