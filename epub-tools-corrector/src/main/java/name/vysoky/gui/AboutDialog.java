/*
 * Created by JFormDesigner on Thu Jan 31 14:33:24 CET 2013
 */

package name.vysoky.gui;

import java.awt.*;
import javax.swing.*;

/**
 * @author Jiri Vysoky
 */
public class AboutDialog extends JDialog {
	public AboutDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public AboutDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		applicationNameLabel = new JLabel();
		copyrightLabel = new JLabel();

		//======== this ========
		setTitle("About application");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {25, 0, 20, 0};
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {25, 0, 0, 20, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

		//---- applicationNameLabel ----
		applicationNameLabel.setText("ePUB Corrector");
		applicationNameLabel.setFont(new Font("Dialog", Font.BOLD, 18));
		contentPane.add(applicationNameLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//---- copyrightLabel ----
		copyrightLabel.setText("Copyright (c) Ji\u0159\u00ed Vysok\u00fd, 2012");
		contentPane.add(copyrightLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel applicationNameLabel;
	private JLabel copyrightLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
