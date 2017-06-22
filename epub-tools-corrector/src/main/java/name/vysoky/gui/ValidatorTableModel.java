package name.vysoky.gui;

import com.adobe.epubcheck.api.Report;
import com.adobe.epubcheck.util.FeatureEnum;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * IFDF Validator table styleTableModel.
 *
 * @author Jiri Vysoky
 */
public class ValidatorTableModel extends AbstractTableModel implements Report {

    private int errorCount = 0;
    private int warningCount = 0;
    private int exceptionCount = 0;

    public static final String TYPE_ERROR = "ERROR";
    public static final String TYPE_WARNING = "WARNING";
    public static final String TYPE_EXCEPTION = "EXCEPTION";
    public static final String TYPE_FEATURE = "FEATURE";

    public static final int COLUM_TYPE = 0;
    public static final int COLUMN_RESOURCE = 1;
    public static final int COLUMN_LINE = 2;
    public static final int COLUMN_COLUMN = 3;
    public static final int COLUMN_MESSAGE = 4;
    public static final int COLUMN_ACTION = 5;




    private String[] columnNames = {
       "Type", "Resource", "Line", "Column", "Message", "Action"
    };

    private List<Data> list = new ArrayList<Data>();

    // Table styleTableModel implementation

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Data data = list.get(rowIndex);
        String s = "";
        switch (columnIndex) {
            case COLUM_TYPE:
                s = data.getType() ;
                break;
            case COLUMN_RESOURCE:
                s = data.getResource() ;
                break;
            case COLUMN_LINE:
                s =  data.getLine();
                break;
            case COLUMN_COLUMN:
                s =  data.getColumn();
                break;
            case COLUMN_MESSAGE:
                s =  data.getMessage();
                break;
            case COLUMN_ACTION:
                s = "";
                break;
        }
        return s;
    }

    // Report implementation

    @Override
    public void error(String resource, int line, int column, String message) {
        errorCount++;
        list.add(new Data(TYPE_ERROR, resource, line, column, message));
        fireTableDataChanged();
    }

    @Override
    public void warning(String resource, int line, int column, String message) {
        warningCount++;
        list.add(new Data(TYPE_WARNING, resource, line, column, message));
        fireTableDataChanged();
    }

    @Override
    public void exception(String resource, Exception e) {
        exceptionCount++;
        list.add(new Data(TYPE_EXCEPTION, resource, e.getMessage()));
        fireTableDataChanged();
    }

    @Override
    public void info(String resource, FeatureEnum feature, String value) {
        list.add(new Data(TYPE_FEATURE, resource, feature + ": " + value));
        fireTableDataChanged();
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public int getWarningCount() {
        return warningCount;
    }

    @Override
    public int getExceptionCount() {
        return exceptionCount;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    private class Data {

        private String type = "";
        private String resource = "";
        private String line = "";
        private String column = "";
        private String message = "";

        private Data(String type, String resource, String message) {
            this.type = type;
            this.resource = resource;
            this.message = message;
        }

        private Data(String type, String resource, int line, int column, String message) {
            this(type, resource, message);
            this.line = Integer.toString(line);
            this.column = Integer.toString(column);
        }

        public String getType() {
            return type;
        }

        public String getResource() {
            return resource;
        }

        public String getLine() {
            return line;
        }

        public String getColumn() {
            return column;
        }

        public String getMessage() {
            return message;
        }
    }

}
