package client.forms;

import javax.swing.*;

/**
 * Created by suparngupta on 4/9/14.
 */
public class ResultDialog extends javax.swing.JDialog {

    private ResultDialog myself;
    /**
     * Creates new form ResultDialog
     */
    public ResultDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.myself = this;
    }


    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        messageLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        okButton.setText("jButton1");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        messageLabel.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(messageLabel)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(159, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(168, 168, 168))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(okButton)
                                                .addGap(144, 144, 144))))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(messageLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(okButton)
                                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }


    /**
     * Create and display the dialog
     */
    public void showDialog() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                myself.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        myself.setVisible(false);
                    }
                });
                myself.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration

    /**
     * Sets new messageLabel.
     *
     * @param messageLabel New value of messageLabel.
     */
    public void setMessageLabel(JLabel messageLabel) {
        this.messageLabel = messageLabel;
    }

    /**
     * Sets new okButton.
     *
     * @param okButton New value of okButton.
     */
    public void setOkButton(JButton okButton) {
        this.okButton = okButton;
    }

    /**
     * Gets okButton.
     *
     * @return Value of okButton.
     */
    public JButton getOkButton() {
        return okButton;
    }

    /**
     * Gets titleLabel.
     *
     * @return Value of titleLabel.
     */
    public JLabel getTitleLabel() {
        return titleLabel;
    }

    /**
     * Gets messageLabel.
     *
     * @return Value of messageLabel.
     */
    public JLabel getMessageLabel() {
        return messageLabel;
    }

    /**
     * Sets new titleLabel.
     *
     * @param titleLabel New value of titleLabel.
     */
    public void setTitleLabel(JLabel titleLabel) {
        this.titleLabel = titleLabel;
    }
}
