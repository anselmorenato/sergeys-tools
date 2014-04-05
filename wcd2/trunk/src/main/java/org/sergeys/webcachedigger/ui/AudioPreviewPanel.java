package org.sergeys.webcachedigger.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import org.sergeys.library.swing.ScaledImage;
import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Mp3Utils;
import org.sergeys.webcachedigger.logic.Settings;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javax.swing.ImageIcon;

public class AudioPreviewPanel extends AbstractFilePreviewPanel {

    // for property listeners
    public static final String PROPERTY_FILE_TO_PLAY = "AudioPreviewPanel_PROPERTY_FILE_TO_PLAY";  //$NON-NLS-1$

    JPanel panelTop;
    JPanel panelProperties;
    JPanel panelArtwork;
    //JComboBox<String> comboBoxLanguage;
    JComboBox comboBoxLanguage; // 1.6

    private ArrayList<Locale> locales;
    //Properties charsets;

    public AudioPreviewPanel() {

        Mp3Utils.getInstance().setDecodeStrings(false);

        setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        setLayout(new BorderLayout(0, 0));


        panelTop = new JPanel();
        panelTop.setLayout(new BorderLayout(0, 0));
        add(panelTop, BorderLayout.NORTH);

        panelProperties = new JPanel();
        panelTop.add(panelProperties, BorderLayout.CENTER);

//        JScrollPane scrollPane = new JScrollPane(panelProperties);
//        add(scrollPane, BorderLayout.CENTER);


        GridBagLayout gbl_panelProperties = new GridBagLayout();
        //gbl_panelProperties.columnWidths = new int[]{1, 3, 0};
        gbl_panelProperties.rowHeights = new int[]{0, 0};
        gbl_panelProperties.columnWeights = new double[]{1.0, 3.0};
        gbl_panelProperties.rowWeights = new double[]{0.0, Double.MIN_VALUE};

        panelProperties.setLayout(gbl_panelProperties);

        // eclipse designer

        JLabel lblKey = new JLabel("key"); //$NON-NLS-1$
        lblKey.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_lblKey = new GridBagConstraints();
        gbc_lblKey.anchor = GridBagConstraints.NORTHEAST;
        gbc_lblKey.insets = new Insets(0, 0, 0, 5);
        gbc_lblKey.gridx = 0;
        gbc_lblKey.gridy = 0;


        panelProperties.add(lblKey, gbc_lblKey);

        JTextPane txtpnValue = new JTextPane();
        txtpnValue.setText("value long long value value long long value value long long value value long long value value long long value " + //$NON-NLS-1$
                "value long long value value long long value value long long value value long long value value long long value "); //$NON-NLS-1$
        txtpnValue.setEditable(false);

        txtpnValue.setFont(UIManager.getFont("Label.font")); //$NON-NLS-1$
        // bug here http://stackoverflow.com/questions/613603/java-nimbus-laf-with-transparent-text-fields
        txtpnValue.setOpaque(false);
        txtpnValue.setBorder(BorderFactory.createEmptyBorder());
        txtpnValue.setBackground(new Color(0,0,0,0));

        GridBagConstraints gbc_txtpnValue = new GridBagConstraints();
        gbc_txtpnValue.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtpnValue.anchor = GridBagConstraints.NORTH;
        gbc_txtpnValue.gridx = 1;
        gbc_txtpnValue.gridy = 0;

        //gbc_txtpnValue.gridwidth = GridBagConstraints.REMAINDER;


        panelProperties.add(txtpnValue, gbc_txtpnValue);

        JPanel panelCharsets = new JPanel();
        panelTop.add(panelCharsets, BorderLayout.SOUTH);

        JLabel lblFixEncoding = new JLabel(Messages.getString("AudioPreviewPanel.lblFixEncoding.text")); //$NON-NLS-1$
        panelCharsets.add(lblFixEncoding);

        //comboBoxLanguage = new JComboBox<String>();
        comboBoxLanguage = new JComboBox(); // 1.6
        comboBoxLanguage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doEncodingChanged(e);
            }
        });
        panelCharsets.add(comboBoxLanguage);

        JPanel panelControl = new JPanel();
        add(panelControl, BorderLayout.SOUTH);

        JButton btnPlay = new JButton(""); //$NON-NLS-1$
        btnPlay.setIcon(new ImageIcon(AudioPreviewPanel.class.getResource("/images/player_play.png"))); //$NON-NLS-1$
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doPlay(e);
            }
        });
        btnPlay.setEnabled(Settings.getInstance().isExternalPlayerConfigured());
        panelControl.add(btnPlay);

        panelArtwork = new JPanel();
        add(panelArtwork, BorderLayout.CENTER);
        panelArtwork.setLayout(new BorderLayout(0, 0));


        locales = new ArrayList<Locale>();
        comboBoxLanguage.addItem(Messages.getString("AudioPreviewPanel.NoLangChanges")); //$NON-NLS-1$
        comboBoxLanguage.setSelectedIndex(0);
        locales.add(null);

        for(Object lang: Mp3Utils.getInstance().getDecodingLanguages()){
            Locale l = new Locale(lang.toString());
            comboBoxLanguage.addItem(l.getDisplayName());
            if(lang.toString().equals(Settings.getInstance().getMp3tagsLanguage())){
                comboBoxLanguage.setSelectedItem(l.getDisplayName());
            }
            locales.add(l);
        }
    }

    protected void doEncodingChanged(ActionEvent e) {

        if(getCachedFile() == null){
            return;
        }

        int selectedIndex = comboBoxLanguage.getSelectedIndex();
        if(selectedIndex > 0){

            Locale l = locales.get(selectedIndex);
            Settings.getInstance().setMp3tagsLanguage(l.getLanguage());
        }
        else if(selectedIndex == 0){
            Settings.getInstance().setMp3tagsLanguage(null);
        }

        this.setCachedFile(getCachedFile());

        panelProperties.revalidate();
    }

    protected void doPlay(ActionEvent e) {
        firePropertyChange(PROPERTY_FILE_TO_PLAY, null, getCachedFile());
    }


    /**
     *
     * @param label
     * @param value
     * @param row
     * @return next row number
     */
    private int addRow(String label, String value, int row){

        if(value != null && !value.isEmpty()){
        //if(value != null){

            value = Mp3Utils.getInstance().decode(value);

            JLabel lblKey = new JLabel(label);
            lblKey.setHorizontalAlignment(SwingConstants.TRAILING);
            GridBagConstraints gbc_lblKey = new GridBagConstraints();
            gbc_lblKey.anchor = GridBagConstraints.NORTHEAST;
            gbc_lblKey.insets = new Insets(0, 0, 0, 5);
            gbc_lblKey.gridx = 0;
            gbc_lblKey.gridy = row;

            panelProperties.add(lblKey, gbc_lblKey);

            JTextPane txtpnValue = new JTextPane();
            txtpnValue.setText(value);
            txtpnValue.setEditable(false);
            txtpnValue.setFont(UIManager.getFont("Label.font")); //$NON-NLS-1$

            // bug here http://stackoverflow.com/questions/613603/java-nimbus-laf-with-transparent-text-fields
            txtpnValue.setOpaque(false);
            txtpnValue.setBorder(BorderFactory.createEmptyBorder());
            txtpnValue.setBackground(new Color(0,0,0,0));

            GridBagConstraints gbc_txtpnValue = new GridBagConstraints();
            gbc_txtpnValue.anchor = GridBagConstraints.NORTH;
            gbc_txtpnValue.fill = GridBagConstraints.HORIZONTAL;
            gbc_txtpnValue.gridx = 1;
            gbc_txtpnValue.gridy = row;

            panelProperties.add(txtpnValue, gbc_txtpnValue);

            return ++row;
        }
        else{
            return row;
        }

    }



    @Override
    public void setCachedFile(CachedFile cachedFile) {

        super.setCachedFile(cachedFile);

        if(Settings.getInstance().getMp3tagsLanguage() == null){
            Mp3Utils.getInstance().setDecodeStrings(false);
        }
        else{
            Mp3Utils.getInstance().setDecodeStrings(true);
            Mp3Utils.getInstance().setDecodeLanguage(Settings.getInstance().getMp3tagsLanguage());
        }

        panelProperties.removeAll();
        panelArtwork.removeAll();

        int row = 0;

        try {
            Mp3File mp3 = new Mp3File(cachedFile.getAbsolutePath());

            if(mp3.hasId3v2Tag()){
                ID3v2 id3v2 = mp3.getId3v2Tag();

                row = addRow("", id3v2.getArtist(), row); //$NON-NLS-1$
                row = addRow("", id3v2.getTitle(), row); //$NON-NLS-1$

                if(row > 0){
                    row = addRow(" ", " ", row); //$NON-NLS-1$ //$NON-NLS-2$
                }

                row = addRow(Messages.getString("AudioPreviewPanel.album"), id3v2.getAlbum(), row); //$NON-NLS-1$
                row = addRow(Messages.getString("AudioPreviewPanel.track"), id3v2.getTrack(), row); //$NON-NLS-1$
                row = addRow(Messages.getString("AudioPreviewPanel.year"), id3v2.getYear(), row); //$NON-NLS-1$
                row = addRow(Messages.getString("AudioPreviewPanel.genre"), id3v2.getGenreDescription(), row); //$NON-NLS-1$
                row = addRow(Messages.getString("AudioPreviewPanel.originalArtist"), id3v2.getOriginalArtist(), row); //$NON-NLS-1$

                row = addRow(Messages.getString("AudioPreviewPanel.composer"), id3v2.getComposer(), row); //$NON-NLS-1$
                row = addRow(Messages.getString("AudioPreviewPanel.copyright"), id3v2.getCopyright(), row); //$NON-NLS-1$
                row = addRow(Messages.getString("AudioPreviewPanel.encoder"), id3v2.getEncoder(), row); //$NON-NLS-1$
                row = addRow(Messages.getString("AudioPreviewPanel.comment"), id3v2.getComment(), row);                 //$NON-NLS-1$
                row = addRow(Messages.getString("AudioPreviewPanel.iTunesComment"), id3v2.getItunesComment(), row);                                 //$NON-NLS-1$

                row = addRow(Messages.getString("AudioPreviewPanel.url"), id3v2.getUrl(), row); //$NON-NLS-1$
                //row = addRow("Version:", id3v2.getVersion(), row);

                byte[] bytes = id3v2.getAlbumImage();

                if(bytes != null){
                    Image img = ImageIO.read(new ByteArrayInputStream(bytes));
                    if(img != null){
                        ScaledImage artwork = new ScaledImage(img, false);
                        panelArtwork.add(artwork);
                    }
                }
            }
            else if(mp3.hasId3v1Tag()){
                ID3v1 id3v1 = mp3.getId3v1Tag();

                row = addRow("", id3v1.getArtist(), row); //$NON-NLS-1$
                row = addRow("", id3v1.getTitle(), row); //$NON-NLS-1$
                if(row > 0){
                    row = addRow(" ", " ", row); //$NON-NLS-1$ //$NON-NLS-2$
                }

                row = addRow(Messages.getString("AudioPreviewPanel.album"), id3v1.getAlbum(), row); //$NON-NLS-1$
                row = addRow(Messages.getString("AudioPreviewPanel.track"), id3v1.getTrack(), row); //$NON-NLS-1$
                row = addRow(Messages.getString("AudioPreviewPanel.year"), id3v1.getYear(), row); //$NON-NLS-1$
                row = addRow(Messages.getString("AudioPreviewPanel.genre"), id3v1.getGenreDescription(), row); //$NON-NLS-1$

                row = addRow(Messages.getString("AudioPreviewPanel.comment"), id3v1.getComment(), row); //$NON-NLS-1$

                //row = addRow(":", id3v1.getVersion(), row);
            }

            if(row > 0){
                row = addRow(" ", " ", row); //$NON-NLS-1$ //$NON-NLS-2$
            }

            row = addRow("", mp3.getChannelMode(), row);    // channel mode //$NON-NLS-1$
            row = addRow("Layer", mp3.getLayer(), row); //$NON-NLS-1$
            row = addRow("Mode extension", mp3.getModeExtension(), row); //$NON-NLS-1$
            //row = addRow("version", mp3.getVersion(), row);
            row = addRow("Bitrate", String.valueOf(mp3.getBitrate()), row); //$NON-NLS-1$
            //row =
                    addRow("Sample rate", String.valueOf(mp3.getSampleRate()), row); //$NON-NLS-1$

        } catch (UnsupportedTagException e) {
            Settings.getLogger().error("", e); //$NON-NLS-1$
        } catch (InvalidDataException e) {
            Settings.getLogger().error("", e); //$NON-NLS-1$
        } catch (IOException e) {
            Settings.getLogger().error("", e); //$NON-NLS-1$
        }
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
