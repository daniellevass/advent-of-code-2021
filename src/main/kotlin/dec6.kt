import java.math.BigInteger

fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec6_input.txt")
    println("we have ${input.size} items")

    val fish = input.first().split(",").filterNot { it.isBlank() }.map { it.toInt() }
    println("we start with ${fish.count()} fish")
    dec6_part2(fish)

}
 private fun dec6_part1( fish: List<Int>) {

     var currentFish = fish
     for (day in 0 .. 256) {

         val newFish = dec6_iterateLoop(currentFish)
         println("day $day we have ${currentFish.size} fish")
//         println(newFish)
         currentFish = newFish
     }
 }

 private fun dec6_iterateLoop(fish: List<Int>) : List<Int>{
     val newFish = arrayListOf<Int>()

     fish.map { f->
         if (f == 0) {
             newFish.add(8) // create a baby fish
             newFish.add(6) // restart it's timer
         } else {
             newFish.add(f-1)
         }
     }

     return newFish
 }

private fun dec6_part2(fish: List<Int>) {

   var currentMap = dec6_part2_processMap(fish)

    for (day in 1 .. 256) {

        val newMap = dec6_part2_iterateLoop(currentMap)
        val newCount = dec6_part2_countItemsInMap(newMap)
        println("day $day we have $newCount fish")
        currentMap = newMap
    }

}

private fun dec6_createEmptyMap(): HashMap<Int, Long> {
    val map = HashMap<Int, Long>()

    for (i in 0 .. 8) {
        map[i] = 0L
    }
    return map
}

private fun dec6_part2_processMap(fish: List<Int>): HashMap<Int, Long> {
    val map = dec6_createEmptyMap()

    // create a map of the count of fish in each day
    val groupedItems = fish
        .groupingBy { it }
        .eachCount()

    for (item in groupedItems) {
        map[item.key] = item.value.toLong()
    }

    return map;
}

private fun dec6_part2_iterateLoop(map: HashMap<Int, Long>): HashMap<Int, Long> {
    val newMap = dec6_createEmptyMap()

    val itr = map.keys.iterator()
    while (itr.hasNext()) {
        val key = itr.next()
        val value: Long = map[key]!! // we stuffed with zeros so we should always have a value

        if (key == 0) {
            newMap[8] = value // create new lantern fish
            val currentAdult6s = newMap[6]!!
            newMap[6] = value + currentAdult6s
        } else {
            val currentAdultsInSlot = newMap[key-1]!!
            newMap[key-1] = currentAdultsInSlot + value
        }
    }

    return newMap
}

private fun dec6_part2_countItemsInMap(map:HashMap<Int, Long>): Long {
    var count:Long = 0
    val itr = map.keys.iterator()
    while (itr.hasNext()) {
        val key = itr.next()
        val value: Long = map[key]!!

        count+=value
    }

    return count
}