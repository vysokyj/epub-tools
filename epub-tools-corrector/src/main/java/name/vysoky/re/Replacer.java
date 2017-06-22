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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Replace string using sequence of replacing regular expressions.
 */
@SuppressWarnings("unused")
public class Replacer {

    private Locale locale;
    private List<Expression> replacingExpressions;
    private String resource;

    private static boolean verbose = false;

    public static final String RESOURCE_ENCODING = "UTF-8";
    public static final String RESOURCE_EXTENSION = ".re";

    private String preparedComment;

    public Replacer(Locale locale) {
        this.locale = locale;
        this.replacingExpressions = new ArrayList<Expression>();
        this.resource = locale.toString() + RESOURCE_EXTENSION;
        if (isVerbose()) System.out.println("Using text corrector for locale: " + locale.getDisplayName() +
                " - resource " + resource);
        parseExpressionFile();
    }

    public static boolean isVerbose() {
        return verbose;
    }

    public static void setVerbose(boolean b) {
        verbose = b;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getResource() {
        return resource;
    }

    /**
     * Correct string using regular replacingExpressions.
     * @param sting input string
     * @return corrected output
     */
    public String correct(String sting) {
        for (Expression e : replacingExpressions) sting = e.apply(sting);
        return sting;
    }

    private void parseLine(String line) {
        if (line.trim().isEmpty()) return;
        if (line.trim().startsWith("#")) {
            preparedComment = line;
        } else {
            try {
                Expression ex = new Expression(preparedComment, line);
                replacingExpressions.add(ex);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            preparedComment = null;
        }
    }
    private void parseExpressionFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        BufferedReader reader = null;
        try {
            InputStream is = classLoader.getResourceAsStream(resource);
            reader = new BufferedReader(new InputStreamReader(is, RESOURCE_ENCODING));
            String line;
            while ((line = reader.readLine()) != null) parseLine(line);
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding exception!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Unable write streams!");
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e){
                System.err.println("Unable to close streams!");
                e.printStackTrace();
            }
        }
    }
}
