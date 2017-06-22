package name.vysoky.gui;

import name.vysoky.epub.Style;
import name.vysoky.epub.StyleReport;
import name.vysoky.epub.StyleSet;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * Style table styleTableModel.
 *
 * @author Jiri Vysoky
 */
public class StyleTableModel extends AbstractTableModel implements StyleReport {

    public static final int COLUMN_TYPE = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_COUNT = 2;
    public static final int COLUMN_ACTION = 3;

    private JProgressBar progressBar;
    private StyleSet styleSet = new StyleSet();

    private String[] columnNames = {
         "Type", "Name", "Count", "Action"
    };

    public StyleTableModel(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Table styleTableModel implementation

    @Override
    public int getRowCount() {
        return styleSet.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Style style = styleSet.get(rowIndex);
        String s = "";
        switch (columnIndex) {
            case COLUMN_TYPE:
                s = style.getType();
                break;
            case COLUMN_NAME:
                s = style.getName();
                break;
            case COLUMN_COUNT:
                s = Integer.toString(style.getCount());
                break;
            case COLUMN_ACTION:
                s = "";
                break;
        }
        return s;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Style report implementation

    @Override
    public void styleClass(String resource, int line, int column, String name) {
        styleSet.styleClass(resource, line, column, name);
        fireTableDataChanged();
    }

    @Override
    public void styleId(String resource, int line, int column, String name) {
        styleSet.styleId(resource, line, column, name);
        fireTableDataChanged();
    }

    @Override
    public void progress(int percentage) {
        styleSet.progress(percentage);
        progressBar.setValue(percentage);
    }
}


