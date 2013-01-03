package org.sergeys.gpublish.ui;

import java.awt.EventQueue;

import org.sergeys.gpublish.logic.Settings;

public class GalleryPublisher {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWindow window = new MainWindow();
                    window.getFrame().setVisible(true);
                } catch (Exception e) {
                    Settings.getLogger().error("failed to create main window", e);
                }
            }
        });
    }

}
