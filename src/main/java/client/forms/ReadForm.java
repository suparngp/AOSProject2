package client.forms; /**
 *
 */

/**
 * @author Nimisha
 *
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReadForm extends Gui{

    public ReadForm() {

        lblAccountId.setVisible(true);
        lblName.setVisible(true);
        txtId.setVisible(true);
        txtName.setVisible(true);
        btnSubmit.setVisible(true);
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to read the object and display result
                readObject();
            }});
        lblMessage.setText("Enter details to read object values.");



    }
    /**
     *
     */
    protected void readObject() {
        // TODO Auto-generated method stub
        //sends object Id to server to seek permission to read and retrieve data
        //once permission is granted and information is retrieved then the fields become visible




        lblMessage.setText("Object values are: ");
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
        btnSubmit.setVisible(false);

    }

}
