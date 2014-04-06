package client.forms; /**
 *
 */

/**
 * @author Nimisha
 *
 */

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class DeleteForm extends Gui{

    public DeleteForm() {
        lblMessage.setText("Enter details to delete object");
        lblAccountId.setVisible(true);
        lblName.setVisible(true);
        txtId.setVisible(true);
        txtName.setVisible(true);
        btnSubmit.setVisible(true);
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to delete the object
                deleteObject();
            }
        });

    }
    /**
     *
     */
    protected void deleteObject() {
        // TODO Auto-generated method stub
        //access permission to delete object and perform delete of granted
    }

}
