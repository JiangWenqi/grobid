package org.grobid.core.utilities;

import org.grobid.core.test.EngineTest;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@Ignore
public class TextUtilitiesTest extends EngineTest{

    @Test
    public void testHTMLEncode_complete() throws Exception {
        String test = "Dé&amps, C & Bidule, D.;";
        String result = TextUtilities.HTMLEncode(test);
        assertThat("Dé&amp;amps, C &amp; Bidule, D.;", is(result));
    }

    @Test
    public void testHTMLEncode_partial() throws Exception {
        String test = "Dé&amps, C &";
        String result = TextUtilities.HTMLEncode(test);
        assertThat("Dé&amp;amps, C &amp;", is(result));
    }

    @Test
    public void testDephynization_withoutSpaces() {
        assertThat(TextUtilities.dehyphenize("This is hype-\nnized.We are here."),
                is("This is hypenized.We are here."));
        assertThat(TextUtilities.dehyphenize("This is hype-\nnized. We are here."),
                is("This is hypenized. We are here."));
    }

    @Test
    public void testDephynization_withSpaces() {
        assertThat(TextUtilities.dehyphenize("This is hype- \n nized. We are here."), is("This is hypenized. We are here."));
        assertThat(TextUtilities.dehyphenize("This is hype- \nnized. We are here."), is("This is hypenized. We are here."));
        assertThat(TextUtilities.dehyphenize("This is hype - \n nized. We are here."), is("This is hypenized. We are here."));
    }

    @Test
    public void testDephynization_withDigits_shouldNotDephypenize() {
        assertThat(TextUtilities.dehyphenize("This is 1234-\n44A. Patent."), is("This is 1234-44A. Patent."));
        assertThat(TextUtilities.dehyphenize("This is 1234 - \n44A. Patent."), is("This is 1234-44A. Patent."));
    }

    @Test
    public void testDephynization_citation() {
        assertThat(TextUtilities.dehyphenize("Anonymous. Runtime process infection. Phrack, 11(59):ar-\n" +
                        "            ticle 8 of 18, December 2002."),
                is("Anonymous. Runtime process infection. Phrack, 11(59):article 8 of 18, December 2002."));
    }

    @Test
    public void testDephynizationHard_withoutSpaces() {
        assertThat(TextUtilities.dehyphenizeHard("This is hype-\nnized.We are here."),
                is("This is hypenized.We are here."));
        assertThat(TextUtilities.dehyphenizeHard("This is hype-\nnized. We are here."),
                is("This is hypenized. We are here."));
    }

    @Test
    public void testDephynizationHard_withSpaces() {
        assertThat(TextUtilities.dehyphenizeHard("This is hype- \n nized. We are here."), is("This is hypenyzed. We are here."));
        assertThat(TextUtilities.dehyphenizeHard("This is hype- \nnized. We are here."), is("This is hypenyzed. We are here."));
        assertThat(TextUtilities.dehyphenizeHard("This is hype - \n nized. We are here."), is("This is hypenyzed. We are here."));
    }

    @Test
    public void testDephynizationHard_withDigits_shouldNotDephypenize() {
        assertThat(TextUtilities.dehyphenizeHard("This is 1234-\n44A. Patent."), is("This is 1234-44A. Patent."));
        assertThat(TextUtilities.dehyphenizeHard("This is 1234 - \n44A. Patent."), is("This is 1234 - 44A.Patent."));
    }

    @Test
    public void testDephynizationHard_citation() {
        assertThat(TextUtilities.dehyphenizeHard("Anonymous. Runtime process infection. Phrack, 11(59):ar-\n+ " +
                        "            ticle 8 of 18, December 2002."),
                is("Anonymous. Runtime process infection. Phrack, 11(59):article 8 of 18, December 2002."));
    }

    @Test
    public void testPrefix() {
        String word = "Grobid";
        assertEquals("", TextUtilities.prefix(word, 0));
        assertEquals("G", TextUtilities.prefix(word, 1));
        assertEquals("Gr", TextUtilities.prefix(word, 2));
        assertEquals("Gro", TextUtilities.prefix(word, 3));
        assertEquals("Grob", TextUtilities.prefix(word, 4));

        assertEquals("Grobid", TextUtilities.prefix(word, 6));

        assertEquals("Grobid", TextUtilities.prefix(word, 100));

        assertEquals(null, TextUtilities.prefix(null, 0));
        assertEquals(null, TextUtilities.prefix(null, 1));
    }

    @Test
    public void testSuffixes() {
        String word = "Grobid";
        assertEquals("", TextUtilities.suffix(word, 0));
        assertEquals("d", TextUtilities.suffix(word, 1));
        assertEquals("id", TextUtilities.suffix(word, 2));
        assertEquals("bid", TextUtilities.suffix(word, 3));
        assertEquals("obid", TextUtilities.suffix(word, 4));

        assertEquals("Grobid", TextUtilities.suffix(word, 6));

        assertEquals("Grobid", TextUtilities.suffix(word, 100));

        assertEquals(null, TextUtilities.suffix(null, 0));
        assertEquals(null, TextUtilities.suffix(null, 1));
    }

    @Test
    public void testWordShape() {
        testWordShape("This", "Xxxx", "Xx");
        testWordShape("Equals", "Xxxx", "Xx");
        testWordShape("O'Conor", "X'Xxxx", "X'Xx");
        testWordShape("McDonalds", "XxXxxx", "XxXx");
        testWordShape("any-where", "xx-xxx", "x-x");
        testWordShape("1.First", "d.Xxxx", "d.Xx");
        testWordShape("ThisIsCamelCase", "XxXxXxXxxx", "XxXxXxXx");
        testWordShape("This:happens", "Xx:xxx", "Xx:x");
        testWordShape("ABC", "XXX", "X");
        testWordShape("AC", "XX", "X");
        testWordShape("A", "X", "X");
        testWordShape("Ab", "Xx", "Xx");
        testWordShape("AbA", "XxX", "XxX");
        testWordShape("uü", "xx", "x");
        testWordShape("Üwe", "Xxx", "Xx");
    }

    private void testWordShape(String orig, String expected, String expectedTrimmed) {
        assertEquals(expected, TextUtilities.wordShape(orig));
        assertEquals(expectedTrimmed, TextUtilities.wordShapeTrimmed(orig));
    }
}
