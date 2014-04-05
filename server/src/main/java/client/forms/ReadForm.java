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

public class ReadForm extends JFrame {
    private JTextField textField;
    private JLabel lblMessage;

    public ReadForm() {
        getContentPane().setLayout(null);

        JLabel lblEnterNameOf = new JLabel("Enter ID of the object to Read");
        lblEnterNameOf.setFont(new Font("Arial", Font.BOLD, 11));
        lblEnterNameOf.setBounds(26, 31, 324, 24);
        getContentPane().add(lblEnterNameOf);

        JLabel Id = new JLabel("Object ID");
        Id.setFont(new Font("Arial", Font.BOLD, 11));
        Id.setBounds(26, 78, 61, 24);
        getContentPane().add(Id);

        textField = new JTextField();
        textField.setBounds(111, 78, 112, 24);
        getContentPane().add(textField);
        textField.setColumns(10);

        lblMessage = new JLabel("Message displayed after submit");
        lblMessage.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblMessage.setBounds(26, 165, 324, 86);
        getContentPane().add(lblMessage);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 11));
        btnSubmit.setBounds(26, 131, 89, 23);
        getContentPane().add(btnSubmit);
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to read the object and display result
                readObject();
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.BOLD, 11));
        btnBack.setBounds(167, 131, 89, 23);
        getContentPane().add(btnBack);
        final JFrame temp = this;
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                temp.dispose();
            }
        });


        this.setVisible(true);
        this.setSize(400, 300);
        this.setTitle("Read Object");

    }

    /**
     *
     */
    protected void readObject() {
        // TODO Auto-generated method stub

    }

}
