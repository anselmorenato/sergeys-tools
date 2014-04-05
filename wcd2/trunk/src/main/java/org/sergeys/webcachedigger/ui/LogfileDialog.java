package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public class LogfileDialog extends JDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextPane textPane;
    //JTextPane textPane; // for windowbuilder


    /**
     * Create the dialog.
     */
    public LogfileDialog(JFrame owner) {

        super(owner);

        setTitle(Messages.getString("LogfileDialog.0") + Settings.getSettingsDirPath() + File.separator + Settings.LOG_FILE); //$NON-NLS-1$

        setBounds(100, 100, 800, 500);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            {
                textPane = new JTextPane(){
                    // http://tips4java.wordpress.com/2009/01/25/no-wrap-text-pane/
                    private static final long serialVersionUID = 1L;

                    public boolean getScrollableTracksViewportWidth()
                    {
                        return getUI().getPreferredSize(this).width
                            <= getParent().getSize().width;
                    }
                };
                scrollPane.setViewportView(textPane);
            }
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton btnClose = new JButton(Messages.getString("LogfileDialog.1")); //$NON-NLS-1$
                btnClose.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doClose();
                    }

                });
                {
                    JButton btnRefresh = new JButton(Messages.getString("LogfileDialog.2")); //$NON-NLS-1$
                    btnRefresh.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            doRefresh();
                        }
                    });
                    buttonPane.add(btnRefresh);
                }
                buttonPane.add(btnClose);
            }
        }
    }

    protected void doClose() {
        setVisible(false);
    }

    protected void doRefresh() {
        try {
            textPane.setText(""); //$NON-NLS-1$
            StyledDocument doc = textPane.getStyledDocument();

            Style styleError = textPane.addStyle("error", null); //$NON-NLS-1$
            StyleConstants.setForeground(styleError, Color.red);
            Style styleWarning = textPane.addStyle("warning", null); //$NON-NLS-1$
            StyleConstants.setForeground(styleWarning, Color.magenta);
            Style styleDebug = textPane.addStyle("debug", null); //$NON-NLS-1$
            StyleConstants.setForeground(styleDebug, Color.gray);

            BufferedReader br = new BufferedReader(new FileReader(Settings.getSettingsDirPath() + File.separator + Settings.LOG_FILE));
            String str;
            while((str = br.readLine()) != null){
                if(str.startsWith("ERR") || str.startsWith("SEVERE")){ //$NON-NLS-1$ //$NON-NLS-2$
                    doc.insertString(doc.getLength(), str + "\n", styleError); //$NON-NLS-1$
                }
                else if(str.startsWith("WARN")){ //$NON-NLS-1$
                    doc.insertString(doc.getLength(), str + "\n", styleWarning); //$NON-NLS-1$
                }
                else if(str.startsWith("DEBUG") || str.startsWith("FINE")){	// FINER, FINEST //$NON-NLS-1$ //$NON-NLS-2$
                    doc.insertString(doc.getLength(), str + "\n", styleDebug); //$NON-NLS-1$
                }
                else{
                    doc.insertString(doc.getLength(), str + "\n", null); //$NON-NLS-1$
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            Settings.getLogger().error("failed to read log file", e); //$NON-NLS-1$
        } catch (IOException e) {
            Settings.getLogger().error("failed to read log file", e); //$NON-NLS-1$
        } catch (BadLocationException e) {
            Settings.getLogger().error("failed to read log file", e); //$NON-NLS-1$
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if(visible){
            doRefresh();
        }
    }

}
