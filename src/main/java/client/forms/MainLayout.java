package client.forms;

import client.clientNetwork.ClientNode;
import common.messages.Account;
import common.utils.Logger;

/**
 * Created by suparngupta on 4/9/14.
 */
public class MainLayout extends javax.swing.JFrame {

    private MainLayout myself;
    private ClientNode clientNode;
    private Account account;
    private ResultDialog resultDialog;
    /**
     * Creates new form MainLayout
     */
    public MainLayout(int clientId) {
        initComponents();
        myself = this;
        this.clientNode = new ClientNode(clientId);
        this.resultDialog = new ResultDialog(this, true);
    }
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        optionsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        createRadio = new javax.swing.JRadioButton();
        searchField = new javax.swing.JTextField();
        readRadio = new javax.swing.JRadioButton();
        searchButton = new javax.swing.JButton();
        updateRadio = new javax.swing.JRadioButton();
        deleteRadio = new javax.swing.JRadioButton();
        formPanel = new javax.swing.JPanel();
        objectIdLabel = new javax.swing.JLabel();
        objectIdField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        openBalLabel = new javax.swing.JLabel();
        currBalField = new javax.swing.JTextField();
        currBalLabel = new javax.swing.JLabel();
        openBalField = new javax.swing.JTextField();
        formButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("Project 2");

        buttonGroup1.add(createRadio);
        createRadio.setSelected(true);
        createRadio.setText("Create");
        createRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createRadioActionPerformed(evt);
            }
        });

        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });
        searchField.setEnabled(false);

        buttonGroup1.add(readRadio);
        readRadio.setText("Read");
        readRadio.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                readRadioStateChanged(evt);
            }
        });
        readRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readRadioActionPerformed(evt);
            }
        });

        searchButton.setText("Search");
        searchButton.setEnabled(false);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(updateRadio);
        updateRadio.setText("Update");
        updateRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateRadioActionPerformed(evt);
            }
        });

        buttonGroup1.add(deleteRadio);
        deleteRadio.setText("Delete");
        deleteRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRadioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout optionsPanelLayout = new javax.swing.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
                optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(optionsPanelLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(createRadio)
                                        .addComponent(readRadio)
                                        .addComponent(updateRadio)
                                        .addComponent(deleteRadio))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsPanelLayout.createSequentialGroup()
                                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(41, 41, 41))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsPanelLayout.createSequentialGroup()
                                                .addComponent(searchButton)
                                                .addGap(88, 88, 88))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsPanelLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(186, 186, 186))
        );
        optionsPanelLayout.setVerticalGroup(
                optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(optionsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(createRadio)
                                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(readRadio)
                                        .addComponent(searchButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(updateRadio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(deleteRadio)
                                .addContainerGap())
        );

        formPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N

        objectIdLabel.setText("Object ID");

        objectIdField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                objectIdFieldActionPerformed(evt);
            }
        });

        nameLabel.setText("Name");

        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });

        openBalLabel.setText("Opening Balance");

        currBalLabel.setText("Current Balance");

        formButton.setText("OK");
        formButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout formPanelLayout = new javax.swing.GroupLayout(formPanel);
        formPanel.setLayout(formPanelLayout);
        formPanelLayout.setHorizontalGroup(
                formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(formPanelLayout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(currBalLabel)
                                        .addComponent(openBalLabel)
                                        .addComponent(nameLabel)
                                        .addComponent(objectIdLabel))
                                .addGap(29, 29, 29)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(formButton)
                                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(objectIdField)
                                                .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                                                .addComponent(currBalField)
                                                .addComponent(openBalField)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formPanelLayout.setVerticalGroup(
                formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(formPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(objectIdLabel)
                                        .addComponent(objectIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nameLabel)
                                        .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(openBalLabel)
                                        .addComponent(openBalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(currBalLabel)
                                        .addComponent(currBalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(formButton)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(formPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(formPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {
        searchAction();
    }

    private void createRadioActionPerformed(java.awt.event.ActionEvent evt) {
        enableSearch(false);
        formButton.setEnabled(true);
    }

    private void objectIdFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void readRadioActionPerformed(java.awt.event.ActionEvent evt) {
        enableSearch(true);
        formButton.setEnabled(false);
    }

    private void readRadioStateChanged(javax.swing.event.ChangeEvent evt) {
        // TODO add your handling code here:
    }

    private void updateRadioActionPerformed(java.awt.event.ActionEvent evt) {
        enableSearch(true);
        formButton.setEnabled(true);
    }

    private void deleteRadioActionPerformed(java.awt.event.ActionEvent evt) {
        enableSearch(true);
        formButton.setEnabled(true);
    }

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {
        searchAction();
    }

    private void formButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(createRadio.isSelected()){
            createAction();
        }
        else if(updateRadio.isSelected()){
            updateAction();
        }
        else if(deleteRadio.isSelected()){
            deleteAction();
        }
        else{
            Logger.error("Unknown option selected");
        }
    }

    public void searchAction() {
        String key = searchField.getText();
        if(key == null || key.isEmpty()){
            return;
        }
        try{
            this.account = this.clientNode.read(key);
            updateForm();
        }

        catch(Exception e){
            Logger.error("Unable to read the object", e);
        }

    }

    public void readAction(){
        Logger.log("Read action called");
    }

    public void updateAction(){
        Logger.log("Update Action called");
    }
    public void createAction(){
        Logger.log("Create action called");
        String objectId = objectIdField.getText();
        String name = nameField.getText();
        Double openingBal = Double.parseDouble(openBalField.getText());
        Double currBal = Double.parseDouble(currBalField.getText());

        try{
            this.account = this.clientNode.create(objectId, name, openingBal, currBal);
            if(this.account != null){
                updateForm();
                this.resultDialog.getTitleLabel().setText("Success");
                this.resultDialog.getMessageLabel().setText("Object has been successfully created");
                this.resultDialog.showDialog();
            }
            else{
                this.resultDialog.getTitleLabel().setText("Error");
                this.resultDialog.getMessageLabel().setText("Object could not be created");
                this.resultDialog.showDialog();
            }

        }
        catch(Exception e){
            Logger.error("Unable to create object", e);
            this.resultDialog.getTitleLabel().setText("Error");
            this.resultDialog.getMessageLabel().setText("Object has been successfully created");
        }

    }
    public void deleteAction(){
        Logger.log("delete action called");
    }

    private void enableSearch(boolean flag){
        searchField.setEnabled(flag);
        searchButton.setEnabled(flag);
    }

    private void updateForm(){
        if(this.account == null){
            return;
        }
        this.nameField.setText(account.getOwnerName());
        this.objectIdField.setText(account.getId());
        this.currBalField.setText(String.valueOf(account.getCurrentBalance()));
        this.openBalField.setText(String.valueOf(account.getOpeningBalance()));
    }
    /**
     * Display the form
     */
    public void showScreen() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                myself.setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton createRadio;
    private javax.swing.JTextField currBalField;
    private javax.swing.JLabel currBalLabel;
    private javax.swing.JRadioButton deleteRadio;
    private javax.swing.JButton formButton;
    private javax.swing.JPanel formPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField objectIdField;
    private javax.swing.JLabel objectIdLabel;
    private javax.swing.JTextField openBalField;
    private javax.swing.JLabel openBalLabel;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JRadioButton readRadio;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JRadioButton updateRadio;
    // End of variables declaration
}
