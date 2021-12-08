import junit.framework.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

class Dec8KtTest {

    @Test
    fun dec8_processRow() {
        val input1 = "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe"
        assertEquals(8394, dec8_processRow(input1) )
    }

    @Test
    fun dec8_howManyOverlaps() {
        val inputFive = "cdfbe"
        val inputTwo = "gcdfa"
        val inputFour ="eafb"

        assertEquals(2, dec8_howManyOverlaps(inputTwo, inputFour))
        assertEquals(3, dec8_howManyOverlaps(inputFive, inputFour))
    }

}