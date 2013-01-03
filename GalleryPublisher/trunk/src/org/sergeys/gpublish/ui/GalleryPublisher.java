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
                    
                    // set actual location here, to not to confuse eclipse windowbuilder editor
                    window.getFrame().setBounds(
                    		Settings.getInstance().getWinPosition().width,
                    		Settings.getInstance().getWinPosition().height,
                    		Settings.getInstance().getWinSize().width,
                    		Settings.getInstance().getWinSize().height);

                    
                    window.getFrame().setVisible(true);
                } catch (Exception e) {
                    Settings.getLogger().error("failed to create main window", e);
                }
            }
        });
    }

}
