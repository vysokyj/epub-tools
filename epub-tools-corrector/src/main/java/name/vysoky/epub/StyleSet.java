package name.vysoky.epub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jiri Vysoky
 */
public class StyleSet implements StyleReport {

    private List<Style> list = new ArrayList<Style>();

    @Override
    public void styleClass(String resource, int line, int column, String name) {
        addStyle(new Style(Style.TYPE_CLASS, name));
    }

    @Override
    public void styleId(String resource, int line, int column, String name) {
        addStyle(new Style(Style.TYPE_ID, name));
    }

    @Override
    public void progress(int percentage) {
        // ignored
    }

    public void printReport() {
        for (Style style : list) System.out.println(style.toString());
    }

    public Style get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    private void addStyle(Style style) {
        if (list.contains(style)) {
            Style original = list.get(list.indexOf(style));
            original.increaseCount();
        } else {
            list.add(style);
        }

        Collections.sort(list);
    }

}
