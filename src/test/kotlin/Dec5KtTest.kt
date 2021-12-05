import junit.framework.Assert.assertTrue
import org.junit.Test

class Dec5KtTest {

    @Test
    fun test_dec5_makeDiagonalLine() {
        //1,1 -> 3,3
        val line1 = dec5_makeDiagonalLine(x1 = 1, x2 = 3, y1 = 1, y2 = 3)
        val expected1 = arrayListOf<Pair<Int, Int>>(1 to 1, 2 to 2, 3 to 3)
        assertTrue(line1.containsAll(expected1))
        assertTrue(expected1.containsAll(line1))


        //9,7 -> 7,9 covers points 9,7, 8,8, and 7,9.
        val line2 = dec5_makeDiagonalLine(x1 = 9, y1 = 7, x2=7, y2=9)
        val expected2 = arrayListOf(9 to 7, 8 to 8, 7 to 9)
        println(line2)
        assertTrue(line2.containsAll(expected2))
        assertTrue(expected2.containsAll(line2))
    }

}