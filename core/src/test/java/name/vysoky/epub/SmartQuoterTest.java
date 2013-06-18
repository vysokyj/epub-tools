package name.vysoky.epub;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Smart quoter test.
 *
 * @author Jiri Vysoky
 */
public class SmartQuoterTest {

    private SmartQuoter smartQuoter = new SmartQuoter('\u201E','\u201C','\u201A','\u2018');

    @Test
    public void testGetNearChars() {
        assertEquals("e\"s", smartQuoter.getNearChars("te\"st".toCharArray(), 2));
        assertEquals("s\"t", smartQuoter.getNearChars("tes\"t".toCharArray(), 3));
        assertEquals("t\"\n", smartQuoter.getNearChars("test\"".toCharArray(), 4));
        assertEquals("\n\"t", smartQuoter.getNearChars("\"test".toCharArray(), 0));
        assertEquals(" \"t", smartQuoter.getNearChars(" \"test".toCharArray(), 1));
        assertEquals("t\" ", smartQuoter.getNearChars("test\" ".toCharArray(), 4));
    }

    @Test
    public void testIsProbablyLeadingDoubleQuote() {
        assertTrue("Case 1", smartQuoter.isProbablyLeadingDoubleQuote("test \"test\" test".toCharArray(), 5));
        assertTrue("Case 2", smartQuoter.isProbablyLeadingDoubleQuote("\"test\" test".toCharArray(), 0));
        assertTrue("Case 3", smartQuoter.isProbablyLeadingDoubleQuote("\"Test\" test".toCharArray(), 0));
    }

    @Test
    public void testIsProbablyTrailingDoubleQuote() {
        assertTrue("Case 01", smartQuoter.isProbablyTrailingDoubleQuote("test \"test\" test".toCharArray(), 10));
        assertTrue("Case 02", smartQuoter.isProbablyTrailingDoubleQuote("test \"test\"".toCharArray(), 10));
        assertTrue("Case 03", smartQuoter.isProbablyTrailingDoubleQuote("test \"Test.\" test".toCharArray(), 11));
        assertTrue("Case 04", smartQuoter.isProbablyTrailingDoubleQuote("test \"Test?\" test".toCharArray(), 11));
        assertTrue("Case 05", smartQuoter.isProbablyTrailingDoubleQuote("test \"Test!\" test".toCharArray(), 11));
        assertTrue("Case 06", smartQuoter.isProbablyTrailingDoubleQuote("test \"Test,\" test".toCharArray(), 11));
        assertTrue("Case 07", smartQuoter.isProbablyTrailingDoubleQuote("test \"Test;\" test".toCharArray(), 11));
        assertTrue("Case 08", smartQuoter.isProbablyTrailingDoubleQuote("test \"Test\". test".toCharArray(), 10));
        assertTrue("Case 09", smartQuoter.isProbablyTrailingDoubleQuote("test \"test\", test".toCharArray(), 10));
        assertTrue("Case 10", smartQuoter.isProbablyTrailingDoubleQuote("test \"test\"? test".toCharArray(), 10));
        assertTrue("Case 11", smartQuoter.isProbablyTrailingDoubleQuote("test \"test\"! test".toCharArray(), 10));
        assertTrue("Case 12", smartQuoter.isProbablyTrailingDoubleQuote("test \"test\"; test".toCharArray(), 10));
    }
}
