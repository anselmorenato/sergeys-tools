package org.sergeys.library.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class DirSelectorDialog extends JDialog {

    public static final String DIRECTORY_SELECTED = "DIRECTORY_SELECTED";

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private DirTreePanel dirTreePanel;

    /**
     * Create the dialog.
     */
    public DirSelectorDialog(Window owner) {
        super(owner);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Select Directory");
        //setIconImage(Toolkit.getDefaultToolkit().getImage(DirSelectorDialog.class.getResource("/images/icon.png")));
        setBounds(100, 100, 450, 462);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            dirTreePanel = new DirTreePanel(File.listRoots(), null, "My Machine", "My Home Folder");
            contentPanel.add(dirTreePanel);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doOK();
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doCancel();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

    protected void doCancel() {
        setVisible(false);
    }

    protected void doOK() {
        File selectedDir = dirTreePanel.getSelectedDirectory();
        if(selectedDir != null){
            firePropertyChange(DIRECTORY_SELECTED, null, selectedDir.getAbsolutePath());
        }
        setVisible(false);
    }

}
