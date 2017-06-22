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

package name.vysoky.epub;

/**
 * Command line option.
 */
public enum Option {

    HELP(
            'h',
            "help",
            "Print this help and ignore other command."
    ),
    VERBOSE(
            'v',
            "verbose",
            "Produce more verbose output."
    ),
    EXTRACT(
            'x',
            "extract",
            "Extract EPUB file to working directory."
    ),
    COMPRESS(
            'p',
            "package",
            "Package working directory to EPUB file."
    ),
    CORRECT(
            'c',
            "correct",
            "Correct text parts using current language corrector."
    ),
    TIDY(
            't', "tidy",
            "Clean and format XHTML code with TIDY."
    ),
    CHECK(
            'a',
            "check",
            "Check ePUB using Adobe EpubCheck tool."
    ),
    LIST_STYLES(
            "list-styles",
            "List style usage statistics."
    ),
    REMOVE_CLASS(
            "remove-class",
            "Remove style class, add class name as parameter.",
            true
    );

    private Character shortSwitch;
    private String longSwitch;
    private boolean parametrised;
    private String description;

    Option(String longSwitch, String description) {
        this(null, longSwitch, description, false);
    }

    Option(String longSwitch, String description, boolean parametrised) {
        this(null, longSwitch, description, parametrised);
    }

    Option(Character shortSwitch, String longSwitch, String description) {
        this(shortSwitch, longSwitch, description, false);
    }

    Option(Character shortSwitch, String longSwitch, String description, boolean parametrised) {
        this.shortSwitch = shortSwitch;
        this.longSwitch = longSwitch;
        this.parametrised = parametrised;
        this.description = description;
    }

    public Character getShortSwitch() {
        return shortSwitch;
    }

    public String getLongSwitch() {
        return longSwitch;
    }

    public boolean isParametrised() {
        return parametrised;
    }

    @SuppressWarnings("unused")
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (shortSwitch != null) sb.append("-").append(shortSwitch).append(' ');
        if (longSwitch != null) sb.append("--").append(longSwitch);
        while (sb.length() <= 15) sb.append(' ');
        sb.append(description);
        return sb.toString();
    }
}
