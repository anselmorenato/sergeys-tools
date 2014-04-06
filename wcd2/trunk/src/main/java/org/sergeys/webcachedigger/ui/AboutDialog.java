package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.sergeys.library.swing.SystemPropertiesTable;
import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;

public class AboutDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JTabbedPane jTabbedPaneCenter = null;
    private JPanel jPanelBottom = null;
    private JButton jButtonOK = null;
    private JPanel jPanelAbout = null;
    private JLabel jLabelTitle = null;
    private JLabel jLabelVersion = null;
    private JLabel jLabelAuthor = null;
    private JPanel jPanelSystem = null;
    private JPanel jPanelLibraries = null;
    private JScrollPane jScrollPaneSystemProperties = null;
    private JTable jTableSystemProperties = null;
    private JButton jButtonSave = null;
    private JPanel jPanelSystemActions = null;
    private JTextPane textPaneLibs;
    private JScrollPane scrollPane;
    private JLabel lblJvm;

    /**
     * @param owner
     */
    public AboutDialog(Frame owner) {
        super(owner);
        setIconImage(Toolkit.getDefaultToolkit().getImage(AboutDialog.class.getResource("/images/icon.png"))); //$NON-NLS-1$
        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(500, 300);
        this.setPreferredSize(new Dimension(500, 300));
        this.setTitle(Messages.getString("AboutDialog.About")); //$NON-NLS-1$
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJTabbedPaneCenter(), BorderLayout.CENTER);
            jContentPane.add(getJPanelBottom(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jTabbedPaneCenter
     *
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getJTabbedPaneCenter() {
        if (jTabbedPaneCenter == null) {
            jTabbedPaneCenter = new JTabbedPane();
            jTabbedPaneCenter.addTab(Messages.getString("AboutDialog.About"), null, getJPanelAbout(), null); //$NON-NLS-1$
            jTabbedPaneCenter.addTab(Messages.getString("AboutDialog.System"), null, getJPanelSystem(), null); //$NON-NLS-1$
            jTabbedPaneCenter.addTab(Messages.getString("AboutDialog.Libraries"), null, getJPanelLibraries(), null); //$NON-NLS-1$
        }
        return jTabbedPaneCenter;
    }

    /**
     * This method initializes jPanelBottom
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelBottom() {
        if (jPanelBottom == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setVgap(0);
            flowLayout.setAlignment(FlowLayout.RIGHT);
            jPanelBottom = new JPanel();
            jPanelBottom.setLayout(flowLayout);
            jPanelBottom.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            jPanelBottom.add(getJButtonOK(), null);
        }
        return jPanelBottom;
    }

    /**
     * This method initializes jButtonOK
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonOK() {
        if (jButtonOK == null) {
            jButtonOK = new JButton();
            jButtonOK.setText(Messages.getString("AboutDialog.OK")); //$NON-NLS-1$
            jButtonOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                }
            });
        }
        return jButtonOK;
    }

    /**
     * This method initializes jPanelAbout
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelAbout() {
        if (jPanelAbout == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.insets = new Insets(0, 0, 5, 0);
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 2;
            jLabelAuthor = new JLabel();
            jLabelAuthor.setText(Messages.getString("AboutDialog.SergeySelivanov")); //$NON-NLS-1$
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.insets = new Insets(0, 0, 5, 0);
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 1;
            jLabelVersion = new JLabel();
            //jLabelVersion.setText("Dec 2 2009: " + CachedFile.junkMessage());
            //jLabelVersion.setText("Dec 26 2009");
            jLabelVersion.setText(Settings.getInstance().getVersionDisplay());
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new Insets(0, 0, 5, 0);
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            jLabelTitle = new JLabel();
            jLabelTitle.setText("Web Cache Digger"); //$NON-NLS-1$
            jPanelAbout = new JPanel();
            jPanelAbout.setLayout(new GridBagLayout());
            jPanelAbout.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            jPanelAbout.add(jLabelTitle, gridBagConstraints1);
            jPanelAbout.add(jLabelVersion, gridBagConstraints2);
            jPanelAbout.add(jLabelAuthor, gridBagConstraints3);
            GridBagConstraints gbc_lblJvm = new GridBagConstraints();
            gbc_lblJvm.gridx = 0;
            gbc_lblJvm.gridy = 3;
            getLblJvm().setText(
                    System.getProperties().getProperty("java.runtime.name") + " " + //$NON-NLS-1$ //$NON-NLS-2$
                            System.getProperties().getProperty("java.runtime.version")); //$NON-NLS-1$
            jPanelAbout.add(getLblJvm(), gbc_lblJvm);
        }
        return jPanelAbout;
    }

    /**
     * This method initializes jPanelSystem
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelSystem() {
        if (jPanelSystem == null) {
            jPanelSystem = new JPanel();
            jPanelSystem.setLayout(new BorderLayout());
            jPanelSystem.add(getJScrollPaneSystemProperties(), BorderLayout.CENTER);
            jPanelSystem.add(getJPanelSystemActions(), BorderLayout.SOUTH);
        }
        return jPanelSystem;
    }

    /**
     * This method initializes jPanelLibraries
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelLibraries() {
        if (jPanelLibraries == null) {
            jPanelLibraries = new JPanel();
            jPanelLibraries.setLayout(new BorderLayout());
            //jPanelLibraries.add(getTextPaneLibs(), BorderLayout.CENTER);
            jPanelLibraries.add(getScrollPane_1(), BorderLayout.CENTER);
            //jPanelLibraries.add(getTextPane(), BorderLayout.CENTER);
        }
        return jPanelLibraries;
    }

    /**
     * This method initializes jScrollPaneSystemProperties
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPaneSystemProperties() {
        if (jScrollPaneSystemProperties == null) {
            jScrollPaneSystemProperties = new JScrollPane();
            jScrollPaneSystemProperties.setViewportView(getJTableSystemProperties());
        }
        return jScrollPaneSystemProperties;
    }

    /**
     * This method initializes jTableSystemProperties
     *
     * @return javax.swing.JTable
     */
    private JTable getJTableSystemProperties() {
        if (jTableSystemProperties == null) {
            jTableSystemProperties = new SystemPropertiesTable(Messages.getString("AboutDialog.Property"), Messages.getString("AboutDialog.Value")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return jTableSystemProperties;
    }

    /**
     * This method initializes jButtonSave
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonSave() {
        if (jButtonSave == null) {
            jButtonSave = new JButton();
            jButtonSave.setEnabled(false);
            jButtonSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                }
            });
            jButtonSave.setText(Messages.getString("AboutDialog.SaveToFile") + "...");  //$NON-NLS-1$ //$NON-NLS-2$
        }
        return jButtonSave;
    }

    /**
     * This method initializes jPanelSystemActions
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelSystemActions() {
        if (jPanelSystemActions == null) {
            FlowLayout flowLayout1 = new FlowLayout();
            flowLayout1.setAlignment(FlowLayout.RIGHT);
            jPanelSystemActions = new JPanel();
            jPanelSystemActions.setLayout(flowLayout1);
            jPanelSystemActions.add(getJButtonSave(), null);
        }
        return jPanelSystemActions;
    }

    private JTextPane getTextPaneLibs() {
        if (textPaneLibs == null) {
            textPaneLibs = new JTextPane();
            textPaneLibs.addHyperlinkListener(new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    doHyperlinkUpdate(e);
                }
            });
            textPaneLibs.setEditable(false);

            try {
                textPaneLibs.setPage(AboutDialog.class.getResource("/resources/libraries.html")); //$NON-NLS-1$
            } catch (IOException e) {
                Settings.getLogger().error("", e); //$NON-NLS-1$
            }

        }
        return textPaneLibs;
    }

    protected void doHyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
            //SimpleLogger.logMessage(e.getURL().toString());
            Settings.getLogger().info(e.getURL().toString());

            if(Desktop.isDesktopSupported()){
                Desktop dt = Desktop.getDesktop();
                if(dt.isSupported(Desktop.Action.BROWSE)){
                    try {
                        dt.browse(e.getURL().toURI());
                    } catch (IOException e1) {
                        Settings.getLogger().error("", e1); //$NON-NLS-1$
                    } catch (URISyntaxException e1) {
                        Settings.getLogger().error("", e1); //$NON-NLS-1$
                    }
                }
            }
        }
    }

    private JScrollPane getScrollPane_1() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane(getTextPaneLibs());
        }
        return scrollPane;
    }
    private JLabel getLblJvm() {
        if (lblJvm == null) {
            lblJvm = new JLabel(Messages.getString("AboutDialog.lblJvm.text")); //$NON-NLS-1$
        }
        return lblJvm;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
