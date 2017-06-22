/*
 * Created by JFormDesigner on Wed Jan 30 15:51:56 CET 2013
 */

package name.vysoky.gui;

import name.vysoky.epub.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;


/**
 * Panel with validator list.
 * @author Jiri Vysoky
 */
public class ValidatorPanel extends JPanel {

    private Book book;

    public void checkBook(Book book) {
        this.book = book;
        ValidatorTableModel vtm = new ValidatorTableModel();
        table.setModel(vtm);

        table.setDefaultRenderer(Object.class, new TypeCellRenderer());
        table.addMouseListener(new TableActionMouseListener());

        table.getColumnModel().getColumn(ValidatorTableModel.COLUM_TYPE).setMaxWidth(90);
        table.getColumnModel().getColumn(ValidatorTableModel.COLUMN_RESOURCE).setMaxWidth(250);
        table.getColumnModel().getColumn(ValidatorTableModel.COLUMN_LINE).setMaxWidth(50);
        table.getColumnModel().getColumn(ValidatorTableModel.COLUMN_COLUMN).setMaxWidth(50);
        table.getColumnModel().getColumn(ValidatorTableModel.COLUMN_MESSAGE).setMinWidth(100);
        table.getColumnModel().getColumn(ValidatorTableModel.COLUMN_ACTION).setMaxWidth(90);
        table.getColumnModel().getColumn(ValidatorTableModel.COLUMN_ACTION).setCellRenderer(new TableActionCellRenderer());

        //table.getColumnModel().getColumn(0).setCellRenderer(new TypeCellRenderer());
        new Worker(book, vtm).execute();
    }

    public ValidatorPanel() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		titleLabel = new JLabel();
		tableScrollPane = new JScrollPane();
		table = new JTable();

		//======== this ========
		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {8, 383, 8, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {24, 270, 8, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};

		//---- titleLabel ----
		titleLabel.setText("IDPF Validator");
		titleLabel.setLabelFor(table);
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		add(titleLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		//======== tableScrollPane ========
		{

			//---- table ----
			table.setColumnSelectionAllowed(true);
			tableScrollPane.setViewportView(table);
		}
		add(tableScrollPane, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel titleLabel;
	private JScrollPane tableScrollPane;
	private JTable table;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

    private class Worker extends SwingWorker<Void, Void> {

        final ValidatorTableModel vtm;
        final Book book;

        private Worker(Book book, ValidatorTableModel vtm) {
            this.book = book;
            this.vtm = vtm;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                book.check(vtm);
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
            if (value.equals(ValidatorTableModel.TYPE_ERROR)) c.setBackground(new Color(240, 180, 180));
            if (value.equals(ValidatorTableModel.TYPE_EXCEPTION)) c.setBackground(new Color(240, 180, 240));
            if (value.equals(ValidatorTableModel.TYPE_WARNING)) c.setBackground(new Color(240, 230, 180));
            if (value.equals(ValidatorTableModel.TYPE_FEATURE)) c.setBackground(new Color(180, 240, 180));
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
            this.setText("Edit");
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
                if (col == ValidatorTableModel.COLUMN_ACTION) {
                    System.out.println("Opening editor");
                    String resource = (String) table.getValueAt(row, ValidatorTableModel.COLUMN_RESOURCE);
                    File file = new File(book.getWorkingDirectory(), resource);
                    String line = (String) table.getValueAt(row, ValidatorTableModel.COLUMN_LINE);
                    String column = (String) table.getValueAt(row, ValidatorTableModel.COLUMN_COLUMN);
                    try {
                        // Execute command
                        Runtime.getRuntime().exec("geany" +
                                " --line " + line +
                                " --column " + column +
                                " " + file.getAbsolutePath());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }
    }
}
