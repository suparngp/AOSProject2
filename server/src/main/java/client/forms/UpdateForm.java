package client.forms; /**
 *
 */

/**
 * @author Nimisha
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateForm extends JFrame {
    private JTextField textField;

    public UpdateForm() {
        getContentPane().setLayout(null);

        JLabel message = new JLabel("Enter Object ID to Update");
        message.setFont(new Font("Arial", Font.BOLD, 11));
        message.setBounds(31, 29, 350, 21);
        getContentPane().add(message);

        JLabel Id = new JLabel("Object ID");
        Id.setBounds(31, 72, 46, 21);
        getContentPane().add(Id);

        textField = new JTextField();
        textField.setBounds(136, 71, 137, 21);
        getContentPane().add(textField);
        textField.setColumns(10);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(23, 127, 89, 23);
        getContentPane().add(btnSubmit);
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to read the object name and update it
                updateObject();
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(155, 127, 89, 23);
        getContentPane().add(btnBack);
        final JFrame temp = this;
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                temp.dispose();
            }
        });

        this.setVisible(true);
        this.setSize(400, 300);
        this.setTitle("Update Object");

    }

    /**
     *
     */
    protected void updateObject() {
        // TODO Auto-generated method stub

    }

}
