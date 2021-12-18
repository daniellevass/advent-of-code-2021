import java.lang.Math.abs
import java.lang.Math.min

fun main(args: Array<String>) {

    //target area: x=244..303, y=-91..-54
    val minX = 244
    val maxX = 303
    val minY = -91
    val maxY = -54

    part1(minX, maxX, minY, maxY)

}

private fun determineIfCoordIsInside(coord: Pair<Int, Int>, minX:Int, maxX: Int, minY:Int, maxY:Int ) : Boolean {
    if (coord.first in minX..maxX && coord.second in minY..maxY) return true
    return false
}

private fun determineIfCoordIsOutside(coord: Pair<Int, Int>, minX:Int, maxX: Int, minY:Int, maxY:Int) : Boolean {
    if (coord.first > maxX || coord.second < minY) return true
    return false
}

private fun part1(minX:Int, maxX: Int, minY:Int, maxY:Int) {

    val velocities = arrayListOf<Pair<Int, Int>>()

    for (x in -1000..1000) {
        for (y in -1000..1000) {
            val velocity = (x to y)
            if (checkInitialVelocity(velocity, minX, maxX, minY, maxY)) {
                velocities.add(velocity)
            }
        }
    }

    println("we got ${velocities.size}")

}

// return true if we end up in the box
private fun checkInitialVelocity(velocity: Pair<Int, Int>, minX:Int, maxX: Int, minY:Int, maxY:Int) : Boolean {

    /**
     * Due to drag, the probe's x velocity changes by 1 toward the value 0; that is,
     * it decreases by 1 if it is greater than 0,
     * increases by 1 if it is less than 0,
     * or does not change if it is already 0.
     */

    /**
     * Due to gravity, the probe's y velocity decreases by 1.
     */

    var complete = false
    var step = 0
    var startCoord = (0 to 0)
    var velocityX = velocity.first
    var highestY = 0

    while (!complete) {

        // determine next coord
        val x = startCoord.first + velocityX
        val y = startCoord.second + velocity.second - step
        val nextCoord = (x to y)
//        println("step $step : $nextCoord")
        if (y > highestY) highestY = y

        if (determineIfCoordIsInside(nextCoord, minX, maxX, minY, maxY)) {
            return true
        }

        if (determineIfCoordIsOutside(nextCoord, minX, maxX, minY, maxY)) {
            complete = true
        }

        startCoord = nextCoord
        step++

        // determine the next velocity
        if (velocityX > 0) {
            velocityX --
        } else if (velocityX < 0) {
            velocityX ++
        } else {
            velocityX = 0
        }
    }

    return false
}