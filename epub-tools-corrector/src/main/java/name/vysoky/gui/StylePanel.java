/*
 * Created by JFormDesigner on Thu Feb 14 14:31:19 CET 2013
 */

package name.vysoky.gui;

import name.vysoky.epub.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * @author Jiri Vysoky
 */
public class StylePanel extends JPanel {

    private Book book;
    private StyleTableModel styleTableModel;


    public void checkBook(Book book) {
        this.book = book;
        styleTableModel = new StyleTableModel(progressBar);

        table.setModel(styleTableModel);
        table.setDefaultRenderer(Object.class, new TypeCellRenderer());


        table.getColumnModel().getColumn(0).setMaxWidth(90);
        table.getColumnModel().getColumn(1).setMaxWidth(250);
        table.getColumnModel().getColumn(2).setMaxWidth(50);
        table.getColumnModel().getColumn(3).setMaxWidth(90);
        table.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRenderer());

        table.addMouseListener(new TableActionMouseListener());
        //table.getColumnModel().getColumn(0).setCellRenderer(new TypeCellRenderer());
        new Worker(book, styleTableModel).execute();
    }

    public StylePanel() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		titleLabel = new JLabel();
		tableScroll = new JScrollPane();
		table = new JTable();
		progressBar = new JProgressBar();

		//======== this ========
		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {8, 0, 8, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {8, 0, 0, 0, 8, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0, 0.0, 0.0, 1.0E-4};

		//---- titleLabel ----
		titleLabel.setText("Style list sorted by count:");
		add(titleLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		//======== tableScroll ========
		{
			tableScroll.setViewportView(table);
		}
		add(tableScroll, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		add(progressBar, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel titleLabel;
	private JScrollPane tableScroll;
	private JTable table;
	private JProgressBar progressBar;
	// JFormDesigner - End of variables declaration  //GEN-END:variables


    private class Worker extends SwingWorker<Void, Void> {

        final StyleTableModel model;
        final Book book;

        private Worker(Book book, StyleTableModel model) {
            this.book = book;
            this.model = model;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                book.findCSSClasses(model);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class TypeCellRenderer extends DefaultTableCellRenderer {

        public TypeCellRenderer() {
            setOpaque(true); //MUST do this for background to show up.
        }

        public Component getTableCellRendererComponent(
                JTable table, Object object,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component c = super.getTableCellRendererComponent(table, object,
                    isSelected, hasFocus,
                    row, column);

            String value = (String) table.getValueAt(row, 0);
            if (value.equals("ID")) c.setBackground(new Color(240, 180, 180));
            if (value.equals("CLASS")) c.setBackground(new Color(180, 240, 180));
            return c;
        }
    }

    private class TableActionCellRenderer extends JButton implements TableCellRenderer {
        public TableActionCellRenderer() {
            setOpaque(true); //MUST do this for background to show up.
        }
        public Component getTableCellRendererComponent(
                JTable table, Object object,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            this.setText("Delete");
            if (isSelected) {
                this.setForeground(table.getSelectionForeground());
                this.setBackground(table.getSelectionBackground());
            } else {
                this.setForeground(table.getForeground());
                this.setBackground(UIManager.getColor("Button.background"));
            }
            return this;
        }
    }

    private class TableActionMouseListener extends MouseAdapter {
        @Override public void mouseClicked(MouseEvent e) {
            int col = table.getColumnModel().getColumnIndexAtX(e.getX());
            int row = e.getY() / table.getRowHeight();
            if (row < table.getRowCount() && row >= 0 && col < table.getColumnCount() && col >= 0) {
                if (col == StyleTableModel.COLUMN_ACTION) {
                    String styleName = (String) table.getValueAt(row, StyleTableModel.COLUMN_NAME);
                    try {
                        book.removeCSSClass(styleName);
                        StylePanel.this.checkBook(book);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}
