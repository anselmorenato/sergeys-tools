package org.sergeys.gpublish.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.sergeys.gpublish.logic.Settings;

public class LogfileDialog extends JDialog {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JTextPane textPane;

    public LogfileDialog(JFrame owner) {
        super(owner);

        setTitle("Log - " + Settings.getSettingsDirPath() + File.separator + "log.txt");

        setBounds(0, 0, 600, 400);

        JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        textPane = new JTextPane(){
            // http://tips4java.wordpress.com/2009/01/25/no-wrap-text-pane/
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            public boolean getScrollableTracksViewportWidth()
            {
                return getUI().getPreferredSize(this).width
                    <= getParent().getSize().width;
            }
        };
        scrollPane.setViewportView(textPane);

        JPanel panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        getContentPane().add(panel, BorderLayout.SOUTH);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doRefresh();
            }
        });
        panel.add(btnRefresh);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doClose();
            }
        });
        panel.add(btnClose);
    }

    protected void doClose() {
        setVisible(false);
    }

    protected void doRefresh() {
        try {
            textPane.setText("");
            StyledDocument doc = textPane.getStyledDocument();

            Style styleError = textPane.addStyle("error", null);
            StyleConstants.setForeground(styleError, Color.red);
            Style styleWarning = textPane.addStyle("warning", null);
            StyleConstants.setForeground(styleWarning, Color.magenta);
            Style styleDebug = textPane.addStyle("debug", null);
            StyleConstants.setForeground(styleDebug, Color.gray);

            BufferedReader br = new BufferedReader(new FileReader(Settings.getSettingsDirPath() + File.separator + "log.txt"));
            String str;
            while((str = br.readLine()) != null){
                if(str.startsWith("ERR")){
                    doc.insertString(doc.getLength(), str + "\n", styleError);
                }
                else if(str.startsWith("WARN")){
                    doc.insertString(doc.getLength(), str + "\n", styleWarning);
                }
                else if(str.startsWith("DEBUG")){
                    doc.insertString(doc.getLength(), str + "\n", styleDebug);
                }
                else{
                    doc.insertString(doc.getLength(), str + "\n", null);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            Settings.getLogger().error("failed to read log file", e);
        } catch (IOException e) {
            Settings.getLogger().error("failed to read log file", e);
        } catch (BadLocationException e) {
            Settings.getLogger().error("failed to read log file", e);
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
