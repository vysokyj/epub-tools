package name.vysoky.gui;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

import javax.swing.*;

/**
 * Swing launcher.
 *
 * @author Jiri Vysoky
 */
public class Launcher {

    public static boolean isRunningOnMac() {
        return System.getProperty("os.name").toLowerCase().startsWith("mac");
    }

    public static void main(String[] args) {
        try {
            if (isRunningOnMac()) setMacLook("swing-mac");
            else setCommonLook();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainFrame().setVisible(true);
    }

    private static void setMacLook(String appName)throws ClassNotFoundException,
            UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    private static void setCommonLook() throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
    }
}
