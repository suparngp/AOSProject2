package client.forms; /**
 *
 */

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

        //seek permission and create object
        //on creating change visibility of fields if needed and display message

    }
}
