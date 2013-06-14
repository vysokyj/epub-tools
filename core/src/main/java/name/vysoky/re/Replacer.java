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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Modify string using sequence of replacing regular expressions.
 */
@SuppressWarnings("unused")
public class Replacer {

    final Logger logger = LoggerFactory.getLogger(Replacer.class);

    private List<ReplacingExpression> replacingExpressions;

    public Replacer(Loader loader) {
        try {
            replacingExpressions = loader.getReplacingExpressions();
        } catch (Exception e) {
            logger.error("Unable to load replacing expressions.", e);
        }
    }

    /**
     * Modify string using regular expression.
     * @param string input string
     * @return modified output string
     */
    public String replace(String string) {
        for (ReplacingExpression e : replacingExpressions) string = e.apply(string);
        return string;
    }
}
