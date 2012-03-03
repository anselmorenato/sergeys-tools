package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.sergeys.coverfinder.logic.ImageSearchResult;
import org.sergeys.library.swing.DisabledPanel;
import org.sergeys.library.swing.ScaledImage;

public class ResultImagePanel
extends JPanel
//extends DisabledPanel
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String SELECTED_IMAGE_PROPERTY = "SELECTED_IMAGE_PROPERTY"; //$NON-NLS-1$

    ScaledImage scaledImage;
    JLabel lblDimension;
    JLabel lblSize;
    ImageSearchResult imgResult;

    /**
     * Create the panel.
     */
    public ResultImagePanel() {

        setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        setLayout(new BorderLayout(0, 0));

        scaledImage = new ScaledImage((Image) null, false);
        scaledImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doClicked(e);
            }
        });
        scaledImage.setPreferredSize(new Dimension(100, 100));
        add(scaledImage, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        lblDimension = new JLabel(Messages.getString("ResultImagePanel.Dimension")); //$NON-NLS-1$
        lblDimension.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblDimension);

        lblSize = new JLabel(Messages.getString("ResultImagePanel.Size")); //$NON-NLS-1$
        lblSize.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSize);

    }

    protected void doClicked(MouseEvent e) {
        firePropertyChange(SELECTED_IMAGE_PROPERTY, null, imgResult);
    }



    public ResultImagePanel(ImageSearchResult imgResult){
        this();

        this.imgResult = imgResult;

        if(this.imgResult.hasThumbnailImage()){
            scaledImage.setImage(this.imgResult.getThumbnailImage());
        }
        lblDimension.setText(String.format("%dx%d", imgResult.getWidth(), imgResult.getHeight())); //$NON-NLS-1$
        lblSize.setText((imgResult.getFileSize() > 0) ? String.format(Messages.getString("ResultImagePanel.Bytes"), imgResult.getFileSize()) : ""); //$NON-NLS-1$ //$NON-NLS-2$
        revalidate();
    }

    public void startDownloadFullImage(){

        // download full image in background

        if(this.imgResult.hasFullImage()){
            this.disabler.setEnabled(true);
        }
        else{
            this.disabler.setEnabled(false);

            Executor exec = Executors.newSingleThreadExecutor();
            exec.execute(new Runnable(){
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    ResultImagePanel.this.imgResult.downloadFullImage();
                    //SwingUtilities.invokeLater
                    ResultImagePanel.this.fullImageDownloaded();
                }});
        }
    }

    DisabledPanel disabler;
    public void setDisabledPanel(DisabledPanel disabler){
        this.disabler = disabler;
    }

    protected void fullImageDownloaded() {
        disabler.setEnabled(true);
    }
}
