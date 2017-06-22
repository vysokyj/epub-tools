/*
 * ePUB Corrector - https://github.com/vysokyj/epub-corrector/
 *
 * Copyright (C) 2012 Jiri Vysoky
 *
 * ePUB Corrector is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * ePUB Corrector is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cobertura; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package name.vysoky.re;

import org.junit.*;

import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.*;


public class ExpressionTests {

    private Replacer corrector;

    @Before
    public void runBeforeEveryTest() throws IOException {
        corrector = new Replacer(new Locale("cs", "CZ"));
    }

    @After
    public void runAfterEveryTest() {

    }

    @Test
    public void baseTest() {
        String os = "\"Text v uvozovkách\" - s pomlčkou.";
        String rs = "&bdquo;Text v&nbsp;uvozovkách&ldquo; &ndash; s&nbsp;pomlčkou.";
        assertEquals(rs, corrector.correct(os));
    }

    @Test
    public void complicatedTest() {
        String os = "Jiný text \"Text v uvozovkách - s&nbsp;pomlčkou.\"," +
                " a „další“ test" +
                " a v něm další test a i k tomu komplikovaný.";
        String rs = "Jiný text &bdquo;Text v&nbsp;uvozovkách &ndash; s&nbsp;pomlčkou.&ldquo;," +
                " a&nbsp;&bdquo;další&ldquo; test" +
                " a&nbsp;v&nbsp;něm další test a&nbsp;i&nbsp;k&nbsp;tomu komplikovaný.";
        assertEquals(rs, corrector.correct(os));
    }

    /**
     * Split re may appear if re contains nested html element.
     */
    @Test
    public void splitStringTest() {
        String os1 = "Jiný text: \"Text v uvozovkách";
        String os2 = " - s&nbsp;pomlčkou.\", a „další“ test.";
        String rs1 = "Jiný text: &bdquo;Text v&nbsp;uvozovkách";
        String rs2 = " &ndash; s&nbsp;pomlčkou.&ldquo;, a&nbsp;&bdquo;další&ldquo; test.";
        assertEquals(rs1, corrector.correct(os1));
        assertEquals(rs2, corrector.correct(os2));
    }
}
