import junit.framework.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class Dec10KtTest {

    @Test
    fun dec10_determineIfCharMatches() {
        assertTrue(dec10_determineIfCharMatches("[", "]"))
        assertFalse(dec10_determineIfCharMatches("(", ">"))
    }

    @Test
    fun dec10_findIllegalChar() {
        val input1 = "{([(<{}[<>[]}>{[]{[(<()>" // illegal char is }
        assertEquals(1197, dec10_findIllegalChar(input1))

        val input2 = "[[<[([]))<([[{}[[()]]]" // illegal char is )
        assertEquals(3, dec10_findIllegalChar(input2))

        val input3 = "{[{({}]{}}([{[{{{}}([]" // illegal char is ]
        assertEquals(57, dec10_findIllegalChar(input3))

        val input4 = "[<(<(<(<{}))><([]([]()" // illegal char is )
        assertEquals(3, dec10_findIllegalChar(input4))

        val input5 = "<{([([[(<>()){}]>(<<{{" // illegal char is >
        assertEquals(25137, dec10_findIllegalChar(input5))
    }

    @Test
    fun dec10_finishLine() {
        assertEquals(288957, dec10_finishLine("[({(<(())[]>[[{[]{<()<>>"))
        assertEquals(5566, dec10_finishLine("[(()[<>])]({[<{<<[]>>("))
        assertEquals(1480781, dec10_finishLine("(((({<>}<{<{<>}{[]{[]{}"))
        assertEquals(995444, dec10_finishLine("{<[[]]>}<{[{[{[]{()[[[]"))
        assertEquals(294, dec10_finishLine("<{([{{}}[<[[[<>{}]]]>[]]"))
    }


}