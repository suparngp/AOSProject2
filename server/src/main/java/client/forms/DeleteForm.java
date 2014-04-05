package client.forms; /**
 *
 */

/**
 * @author Nimisha
 *
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteForm extends JFrame {
    private JTextField textField;

    public DeleteForm() {
        getContentPane().setLayout(null);

        JLabel lblEnterTheName = new JLabel("Enter the name of the object to delete");
        lblEnterTheName.setBounds(32, 35, 346, 22);
        getContentPane().add(lblEnterTheName);

        JLabel Id = new JLabel("Object ID");
        Id.setBounds(32, 83, 62, 22);
        getContentPane().add(Id);

        textField = new JTextField();
        textField.setBounds(104, 84, 105, 20);
        getContentPane().add(textField);
        textField.setColumns(10);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to delete the object
                deleteObject();
            }
        });
        btnSubmit.setBounds(32, 153, 89, 23);
        getContentPane().add(btnSubmit);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(135, 153, 89, 23);
        getContentPane().add(btnBack);
        final JFrame temp = this;
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                temp.dispose();
            }
        });

        this.setVisible(true);
        this.setSize(400, 300);
        this.setTitle("Delete Object");

    }

    /**
     *
     */
    protected void deleteObject() {
        // TODO Auto-generated method stub

    }

}
