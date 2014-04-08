package client.forms; /**
 *
 */

import common.messages.Account;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
/**
 * @author Nimisha
 * This displays a form to enter object details so that a new object is created.
 */

class CreateForm  extends Gui{

    String id, ownerName;
    Double openingBalance;
    Date createdAt;

    public CreateForm(){
        lblAccountId.setVisible(true);
        lblName.setVisible(true);
        lblOpeningBalance.setVisible(true);
        //lblCreatedAt.setVisible(true);
        txtId.setVisible(true);
        txtName.setVisible(true);
        txtOpbal.setVisible(true);
        //txtCreated.setVisible(true);
        btnSubmit.setVisible(true);
        lblMessage.setText("Enter details to create object");


        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to read the object and display result
                createObject();
            }});
    }

    /**
     *
     */
    protected void createObject() {
        // TODO Auto-generated method stub
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
        //seek permission and create object

        ObjectCreate obj= new ObjectCreate(id,ownerName, account, "create");

        if(obj.seekPermission(id)){

            lblMessage.setText("Object Created");
            lblAccountId.setVisible(false);
            lblName.setVisible(false);
            lblOpeningBalance.setVisible(false);
            txtId.setVisible(false);
            txtName.setVisible(false);
            txtOpbal.setVisible(false);
            btnSubmit.setVisible(false);

        }


        //on creating change visibility of fields if needed and display message


    }
}
