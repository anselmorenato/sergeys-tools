package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.Messages;

public class FileDetailsPanel extends JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    JLabel lblFileName;
    JLabel lblFileSize;
    JLabel lblFiletype;
    JPanel panelPreview;

    private PropertyChangeListener listener;

    private Hashtable<String, AbstractFilePreviewPanel> panelByFileType = new Hashtable<String, AbstractFilePreviewPanel>();

    JPanel panelTop;

    /**
     * This is the default constructor
     * @param webCacheDigger
     */
    public FileDetailsPanel(PropertyChangeListener listener) {
        super();

        this.listener = listener;

        setLayout(new BorderLayout(30, 0));

        panelTop = new JPanel();

        panelTop.setVisible(false);

        panelTop.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(panelTop, BorderLayout.NORTH);
        GridBagLayout gbl_panelTop = new GridBagLayout();
        gbl_panelTop.columnWidths = new int[]{0, 0, 0};
        gbl_panelTop.rowHeights = new int[]{0, 0,0,0};
        gbl_panelTop.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gbl_panelTop.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panelTop.setLayout(gbl_panelTop);

        JLabel lblFname = new JLabel(Messages.getString("FileDetailsPanel.name")); //$NON-NLS-1$
        GridBagConstraints gbc_lblFname = new GridBagConstraints();
        gbc_lblFname.anchor = GridBagConstraints.EAST;
        gbc_lblFname.insets = new Insets(0, 0, 5, 5);
        gbc_lblFname.gridx = 0;
        gbc_lblFname.gridy = 0;
        panelTop.add(lblFname, gbc_lblFname);

        lblFileName = new JLabel("filename"); //$NON-NLS-1$
        GridBagConstraints gbc_lblFileName = new GridBagConstraints();
        gbc_lblFileName.anchor = GridBagConstraints.WEST;
        gbc_lblFileName.insets = new Insets(0, 0, 5, 0);
        gbc_lblFileName.gridx = 1;
        gbc_lblFileName.gridy = 0;
        panelTop.add(lblFileName, gbc_lblFileName);

        JLabel lblFsize = new JLabel(Messages.getString("FileDetailsPanel.size")); //$NON-NLS-1$
        GridBagConstraints gbc_lblFsize = new GridBagConstraints();
        gbc_lblFsize.anchor = GridBagConstraints.EAST;
        gbc_lblFsize.insets = new Insets(0, 0, 5, 5);
        gbc_lblFsize.gridx = 0;
        gbc_lblFsize.gridy = 2;
        panelTop.add(lblFsize, gbc_lblFsize);

        lblFileSize = new JLabel("filesize"); //$NON-NLS-1$
        GridBagConstraints gbc_lblFilesize = new GridBagConstraints();
        gbc_lblFilesize.anchor = GridBagConstraints.WEST;
        gbc_lblFilesize.insets = new Insets(0, 0, 5, 0);
        gbc_lblFilesize.gridx = 1;
        gbc_lblFilesize.gridy = 2;
        panelTop.add(lblFileSize, gbc_lblFilesize);

        JLabel lblType = new JLabel(Messages.getString("FileDetailsPanel.type")); //$NON-NLS-1$
        GridBagConstraints gbc_lblType = new GridBagConstraints();
        gbc_lblType.anchor = GridBagConstraints.EAST;
        gbc_lblType.insets = new Insets(0, 0, 0, 5);
        gbc_lblType.gridx = 0;
        gbc_lblType.gridy = 3;
        panelTop.add(lblType, gbc_lblType);

        lblFiletype = new JLabel("filetype"); //$NON-NLS-1$
        GridBagConstraints gbc_lblFiletype = new GridBagConstraints();
        gbc_lblFiletype.anchor = GridBagConstraints.WEST;
        gbc_lblFiletype.gridx = 1;
        gbc_lblFiletype.gridy = 3;
        panelTop.add(lblFiletype, gbc_lblFiletype);

        panelPreview = new JPanel();
        panelPreview.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(panelPreview, BorderLayout.CENTER);

        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(384, 285);
        this.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
    }

    public void setFile(CachedFile file){

        panelPreview.setVisible(false);
        remove(panelPreview);

        if(file != null){

            panelTop.setVisible(true);

            lblFileName.setText(file.getName());
            lblFileSize.setText(String.valueOf(file.length()));
            lblFiletype.setText(file.getMimeType());

            AbstractFilePreviewPanel preview;
            if(panelByFileType.containsKey(file.getMimeType())){
                preview = panelByFileType.get(file.getMimeType());
            }
            else{
                AbstractFilePreviewPanel newPreview = AbstractFilePreviewPanel.createFilePreviewPanel(file.getMimeType(), listener);
                panelByFileType.put(file.getMimeType(), newPreview);
                preview = newPreview;
            }

            preview.setCachedFile(file);
            panelPreview = preview;

            panelPreview.setBorder(new EmptyBorder(5, 5, 5, 5));
            add(panelPreview, BorderLayout.CENTER);
            
            panelPreview.setVisible(true);
        }
        else{
            panelTop.setVisible(false);
        }

        invalidate();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(CachedFile.SELECTED_FILE)){
            CachedFile file = (CachedFile)evt.getNewValue();
            setFile(file);
        }
    }

}

