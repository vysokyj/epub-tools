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
 * Style class identified in book.
 */
public class Style implements Comparable<Style> {

    public static final int TYPE_ID = 0;
    public static final int TYPE_CLASS = 1;

    protected String name;
    protected int count = 0;
    protected int type = 0;

    public Style(int type, String name) {
        this.type = type;
        this.name = name;
        this.count = 1;
    }

    public void increaseCount() {
        count++;
    }
    public String getType() {
        switch (type) {
            case TYPE_ID:
                return "ID";
            case TYPE_CLASS:
                return "CLASS";
            default:
                return "UNKNOWN";

        }
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Style " + getType() + " '" + name + "' - " + count + " usages";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj != null && obj instanceof Style) {
            Style other = (Style) obj;
            return (this.type == other.type && this.name.equals(other.name));
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Style o) {
        return o.count - this.count;
    }
}
