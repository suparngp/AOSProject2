package client.forms; /**
 *
 */

/**
 * @author Nimisha
 *
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateForm extends Gui{

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
    protected void updateObject() {
        // TODO Auto-generated method stub
        //seek permission to update object.
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

    }

}
