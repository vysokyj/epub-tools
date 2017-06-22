/*
 * Created by JFormDesigner on Wed Jan 30 15:21:12 CET 2013
 */

package name.vysoky.gui;

import name.vysoky.epub.Book;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

/**
 * Main frame.
 * @author Jiri Vysoky
 */
public class MainFrame extends JFrame {

    public MainFrame() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		openFileMenuItem = new JMenuItem();
		compressAndQuitMenuItem = new JMenuItem();
		runMenu = new JMenu();
		correctorMenuItem = new JMenuItem();
		tidyMenuItem = new JMenuItem();
		listStylesMenuItem = new JMenuItem();
		validatorMenuItem = new JMenuItem();
		helpMenu = new JMenu();
		menuItem1 = new JMenuItem();
		centralCardPanel = new JPanel();
		emptyPanel = new JPanel();
		validatorPanel = new ValidatorPanel();
		tidyPanel = new TidyPanel();
		correctorPanel = new CorrectorPanel();
		stylePanel = new StylePanel();
		openAction = new OpenAction();
		saveAndQuitAction = new SaveAndQuitAction();
		aboutAction = new AboutAction();
		correctAction = new CorrectAction();
		tidyAction = new TidyAction();
		validateAction = new ValidateAction();
		listStylesAction = new ListStyleAction();

		//======== this ========
		setTitle("ePUB Corrector");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(640, 480));
		setName("mainFrame");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== menuBar ========
		{

			//======== fileMenu ========
			{
				fileMenu.setText("File");
				fileMenu.setMnemonic('F');

				//---- openFileMenuItem ----
				openFileMenuItem.setAction(openAction);
				openFileMenuItem.setText("Open");
				openFileMenuItem.setMnemonic('O');
				openFileMenuItem.setIcon(UIManager.getIcon("Tree.openIcon"));
				openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
				fileMenu.add(openFileMenuItem);

				//---- compressAndQuitMenuItem ----
				compressAndQuitMenuItem.setMnemonic('C');
				compressAndQuitMenuItem.setAction(saveAndQuitAction);
				compressAndQuitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
				compressAndQuitMenuItem.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
				compressAndQuitMenuItem.setText("Save and quit");
				fileMenu.add(compressAndQuitMenuItem);
			}
			menuBar.add(fileMenu);

			//======== runMenu ========
			{
				runMenu.setText("Run");
				runMenu.setMnemonic('R');

				//---- correctorMenuItem ----
				correctorMenuItem.setMnemonic('C');
				correctorMenuItem.setAction(correctAction);
				correctorMenuItem.setEnabled(false);
				runMenu.add(correctorMenuItem);

				//---- tidyMenuItem ----
				tidyMenuItem.setMnemonic('T');
				tidyMenuItem.setAction(tidyAction);
				tidyMenuItem.setEnabled(false);
				runMenu.add(tidyMenuItem);

				//---- listStylesMenuItem ----
				listStylesMenuItem.setAction(listStylesAction);
				listStylesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK));
				listStylesMenuItem.setEnabled(false);
				runMenu.add(listStylesMenuItem);

				//---- validatorMenuItem ----
				validatorMenuItem.setMnemonic('V');
				validatorMenuItem.setAction(validateAction);
				validatorMenuItem.setEnabled(false);
				runMenu.add(validatorMenuItem);
			}
			menuBar.add(runMenu);

			//======== helpMenu ========
			{
				helpMenu.setText("Help");
				helpMenu.setMnemonic('H');

				//---- menuItem1 ----
				menuItem1.setMnemonic('A');
				menuItem1.setAction(aboutAction);
				helpMenu.add(menuItem1);
			}
			menuBar.add(helpMenu);
		}
		setJMenuBar(menuBar);

		//======== centralCardPanel ========
		{
			centralCardPanel.setLayout(new CardLayout());

			//======== emptyPanel ========
			{
				emptyPanel.setLayout(new CardLayout());
			}
			centralCardPanel.add(emptyPanel, "emptyCard");
			centralCardPanel.add(validatorPanel, "validatorCard");
			centralCardPanel.add(tidyPanel, "tidyCard");
			centralCardPanel.add(correctorPanel, "correctorCard");
			centralCardPanel.add(stylePanel, "styleCard");
		}
		contentPane.add(centralCardPanel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openFileMenuItem;
	private JMenuItem compressAndQuitMenuItem;
	private JMenu runMenu;
	private JMenuItem correctorMenuItem;
	private JMenuItem tidyMenuItem;
	private JMenuItem listStylesMenuItem;
	private JMenuItem validatorMenuItem;
	private JMenu helpMenu;
	private JMenuItem menuItem1;
	private JPanel centralCardPanel;
	private JPanel emptyPanel;
	private ValidatorPanel validatorPanel;
	private TidyPanel tidyPanel;
	private CorrectorPanel correctorPanel;
	private StylePanel stylePanel;
	private OpenAction openAction;
	private SaveAndQuitAction saveAndQuitAction;
	private AboutAction aboutAction;
	private CorrectAction correctAction;
	private TidyAction tidyAction;
	private ValidateAction validateAction;
	private ListStyleAction listStylesAction;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

    private Book book;

	private class OpenAction extends AbstractAction {
		private OpenAction() {
			// JFormDesigner - Action initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
			putValue(NAME, "Open");
			// JFormDesigner - End of action initialization  //GEN-END:initComponents
		}

		public void actionPerformed(ActionEvent e) {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) return true;
                    return f.getName().endsWith(".epub");
                }

                @Override
                public String getDescription() {
                    return "ePUB files";
                }
            });
            int result = fileChooser.showOpenDialog(MainFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                book = new Book(file);
                try {
                    book.extract();
                    tidyMenuItem.setEnabled(true);
                    correctorMenuItem.setEnabled(true);
                    validatorMenuItem.setEnabled(true);
                    listStylesMenuItem.setEnabled(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                setTitle("ePUB Corrector - " + file.getName());

            }
		}
	}

	private class SaveAndQuitAction extends AbstractAction {
		private SaveAndQuitAction() {
			// JFormDesigner - Action initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
			putValue(NAME, "Save and quit");
			// JFormDesigner - End of action initialization  //GEN-END:initComponents
		}

		public void actionPerformed(ActionEvent e) {
            try {
                if (book != null && book.isExtracted()) book.compress();
                System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}

	private class AboutAction extends AbstractAction {
		private AboutAction() {
			// JFormDesigner - Action initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
			putValue(NAME, "About");
			// JFormDesigner - End of action initialization  //GEN-END:initComponents
		}

		public void actionPerformed(ActionEvent e) {
             new AboutDialog(MainFrame.this).setVisible(true);
		}
	}

	private class ValidateAction extends AbstractAction {
		private ValidateAction() {
			// JFormDesigner - Action initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
			putValue(NAME, "Validate");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
			// JFormDesigner - End of action initialization  //GEN-END:initComponents
		}

		public void actionPerformed(ActionEvent e) {
            try {
                showCard("validatorCard");
                validatorPanel.checkBook(book);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
		}
	}

	private class TidyAction extends AbstractAction {
		private TidyAction() {
			// JFormDesigner - Action initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
			putValue(NAME, "Tidy");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK));
			// JFormDesigner - End of action initialization  //GEN-END:initComponents
		}

		public void actionPerformed(ActionEvent e) {
            try {
                showCard("tidyCard");
                book.tidy();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
		}
	}

	private class CorrectAction extends AbstractAction {
		private CorrectAction() {
			// JFormDesigner - Action initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
			putValue(NAME, "Correct");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
			// JFormDesigner - End of action initialization  //GEN-END:initComponents
		}

		public void actionPerformed(ActionEvent e) {
            try {
                showCard("correctorCard");
                correctorPanel.correctBook(book);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
		}
	}

    private void showCard(String cardName) {
        CardLayout cl = (CardLayout) centralCardPanel.getLayout();
        cl.show(centralCardPanel, cardName);
    }

	private class ListStyleAction extends AbstractAction {
		private ListStyleAction() {
			// JFormDesigner - Action initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
			putValue(NAME, "List styles");
			// JFormDesigner - End of action initialization  //GEN-END:initComponents
		}

		public void actionPerformed(ActionEvent e) {
            showCard("styleCard");
            stylePanel.checkBook(book);
		}
	}
}
