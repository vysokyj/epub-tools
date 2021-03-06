/*
 * Created by JFormDesigner on Thu Jan 31 16:21:08 CET 2013
 */

package name.vysoky.gui;

import name.vysoky.epub.Book;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @author Jiri Vysoky
 */
public class TidyPanel extends JPanel {
	public TidyPanel() {
		initComponents();
	}

    public void tidyBook(Book book) throws IOException {
        new Worker(book).execute();
    }

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		titleLabel = new JLabel();
		progressBar = new JProgressBar();

		//======== this ========
		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {24, 0, 24, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {24, 32, 0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0, 1.0E-4};

		//---- titleLabel ----
		titleLabel.setText("Tidy:");
		add(titleLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		add(progressBar, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel titleLabel;
	private JProgressBar progressBar;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

    private class Worker extends SwingWorker<Void, Void> {

        final Book book;

        private Worker(Book book) {
            this.book = book;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                progressBar.setValue(0);
                book.tidy();
                progressBar.setValue(100);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
