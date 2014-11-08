package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.sergeys.library.rss.Feed;
import org.sergeys.library.rss.FeedMessage;
import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;

public class ChangesDialog extends JDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    JTextPane txtpnChanges;

    /**
     * Create the dialog.
     */
    public ChangesDialog(Frame owner) {
        super(owner);
        setTitle(Messages.getString("ChangesDialog.ChangesTitle")); //$NON-NLS-1$

        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JScrollPane scrollPane = new JScrollPane();
            contentPanel.add(scrollPane);
            {
                txtpnChanges = new JTextPane();
                txtpnChanges.setEditable(false);
                txtpnChanges.setText("---"); //$NON-NLS-1$
                scrollPane.setViewportView(txtpnChanges);
            }
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK"); //$NON-NLS-1$
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doOK(e);
                    }
                });
                okButton.setActionCommand("OK"); //$NON-NLS-1$
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }
    }

    public void createText(Feed feed){

        HTMLEditorKit kit = new HTMLEditorKit();
        txtpnChanges.setEditorKit(kit);
        HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();


        // http://stackoverflow.com/questions/3470683/insert-html-into-the-body-of-an-htmldocument
//		Element[] roots = doc.getRootElements(); // #0 is the HTML element, #1 the bidi-root
//		Element body = null;
//		for( int i = 0; i < roots[0].getElementCount(); i++ ) {
//		    Element element = roots[0].getElement( i );
//		    if( element.getAttributes().getAttribute( StyleConstants.NameAttribute ) == HTML.Tag.BODY ) {
//		        body = element;
//		        break;
//		    }
//		}


        StringBuilder sb = new StringBuilder();

        try {
            sb.append(String.format("<h2>%s</h2>", feed.getTitle())); //$NON-NLS-1$

            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);

            for(FeedMessage msg: feed.getMessages()){

                if(msg.getPubDate().after(Settings.getInstance().getSavedVersionTimestamp())){

                    String str = String.format(
                            "<p style='text-indent: 15px;'><b>%s</b></p>" + //$NON-NLS-1$
                            "<p style='text-indent: 15px;'>%s</p><br/>", //$NON-NLS-1$
                            //new SimpleDateFormat().format(msg.getPubDate()),
                            df.format(msg.getPubDate()),
                            msg.getDescription());

                    sb.append(str);
                }
            }

        } catch (Exception e) {
            Settings.getLogger().error("", e); //$NON-NLS-1$
        }


        try {
            kit.insertHTML(doc, 0, sb.toString(), 0, 0, null);

//			StringWriter sw = new StringWriter();
//			HTMLWriter wr = new HTMLWriter(sw, doc);
//			wr.write();
//System.out.println(sw);
        } catch (IOException e) {
            Settings.getLogger().error("", e); //$NON-NLS-1$
        } catch (BadLocationException e) {
            Settings.getLogger().error("", e); //$NON-NLS-1$
        }

        txtpnChanges.setStyledDocument(doc);
    }

    protected void doOK(ActionEvent e) {
        setVisible(false);
    }

}
