package client.forms; /**
 *
 */

/**
 * @author Nimisha
 *
 */

import common.messages.Account;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class UpdateForm extends Gui{

    String id, ownerName;
    Double openingBalance, currentBalance;
    Date createdAt, updatedAt;

    public UpdateForm() {

        lblMessage.setText("Enter details to seek permission to update object");
        lblAccountId.setVisible(true);
        lblName.setVisible(true);
        txtId.setVisible(true);
        txtName.setVisible(true);
        btnSubmit.setVisible(true);
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to read the object name and update it

                updateObject();
            }});

    }
    /**
     *
     */
    protected void updateObject() throws Exception{
        // TODO Auto-generated method stub
        //seek permission to update object.
        ObjectRead read= new ObjectRead(txtId.getText());
        if(read.seekReadPermission("read"))
        {

        }


        // once permission is granted, retrieve results and set text in the fields
        lblMessage.setText("Enters details to update");
        lblAccountId.setVisible(true);
        lblName.setVisible(true);
        lblOpeningBalance.setVisible(true);
        lblCurrentBalance.setVisible(true);
        lblCreatedAt.setVisible(true);
        lblUpdatedAt.setVisible(true);
        txtId.setVisible(true);
        txtName.setVisible(true);
        txtOpbal.setVisible(true);
        txtCurbal.setVisible(true);
        txtCreated.setVisible(true);
        txtUpdated.setVisible(true);
        btnSubmit.setVisible(true);

        //get the account details where id=object id from update form
        //and set all its details in the fields



        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to send updated details to the server

                updateSend();
            }});

    }
    /**
     *
     */
    protected void updateSend() {
        // TODO Auto-generated method stub
        //method to send the updated values
        id=txtId.getText();
        ownerName=txtName.getText();
        String bal=txtOpbal.getText();
        openingBalance=Double.parseDouble(bal);
        Account account=new Account(id);
        account.setId(id);
        account.setOwnerName(ownerName);
        account.setOpeningBalance(openingBalance);
        account.setCurrentBalance(openingBalance);
        createdAt= new Date();
        account.setCreatedAt(createdAt);
        account.setUpdatedAt(createdAt);
        //seek permission and update object

        ObjectCreate obj= new ObjectCreate(id,ownerName, account, "update");
        if(obj.seekPermission(id)){
            lblMessage.setText("Object Updated");
            lblAccountId.setVisible(false);
            lblName.setVisible(false);
            lblOpeningBalance.setVisible(false);
            txtId.setVisible(false);
            txtName.setVisible(false);
            txtOpbal.setVisible(false);
            btnSubmit.setVisible(false);

        }
    }

}
