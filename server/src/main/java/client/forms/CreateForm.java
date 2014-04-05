package client.forms; /**
 *
 */

/**
 * @author Nimisha
 * This displays a form to enter object details so that a new object is created.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateForm extends JFrame {
    private JTextField idValue;
    private JTextField nameValue;
    private JTextField textField;

    public CreateForm() {
        getContentPane().setLayout(null);

        JLabel Name = new JLabel("Name");
        Name.setFont(new Font("Arial", Font.BOLD, 11));
        Name.setBounds(32, 109, 53, 20);
        getContentPane().add(Name);

        JLabel lblEnterDetailsTo = new JLabel("Enter details to create a new object");
        lblEnterDetailsTo.setFont(new Font("Arial", Font.BOLD, 11));
        lblEnterDetailsTo.setBounds(32, 25, 334, 26);
        getContentPane().add(lblEnterDetailsTo);

        idValue = new JTextField();
        idValue.setBounds(131, 62, 140, 20);
        getContentPane().add(idValue);
        idValue.setColumns(10);

        JLabel id = new JLabel("Object ID");
        id.setFont(new Font("Arial", Font.BOLD, 11));
        id.setBounds(32, 62, 65, 20);
        getContentPane().add(id);

        nameValue = new JTextField();
        nameValue.setBounds(131, 109, 140, 20);
        getContentPane().add(nameValue);
        nameValue.setColumns(10);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 11));
        btnSubmit.setBounds(32, 208, 89, 23);
        getContentPane().add(btnSubmit);
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to create the object
                createObject();
            }
        });

        JButton Back = new JButton("Back");
        Back.setFont(new Font("Arial", Font.BOLD, 11));
        Back.setBounds(170, 208, 101, 23);
        getContentPane().add(Back);

        JLabel lblOpeningBalance = new JLabel("Opening Balance");
        lblOpeningBalance.setFont(new Font("Arial", Font.BOLD, 11));
        lblOpeningBalance.setBounds(32, 156, 101, 20);
        getContentPane().add(lblOpeningBalance);

        textField = new JTextField();
        textField.setBounds(131, 156, 140, 20);
        getContentPane().add(textField);
        textField.setColumns(10);
        final JFrame temp = this;
        Back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                temp.dispose();
            }
        });


        this.setVisible(true);
        this.setSize(400, 300);
        this.setTitle("Create Object");

    }

    String id, name;
    double openingBal = 0.0;

    /**
     * On clicking submit, the values are taken and stored
     */
    protected void createObject() {
        // TODO Auto-generated method stub
        id = idValue.getText();
        name = nameValue.getText();
        String bal = textField.getText();
        openingBal = Double.parseDouble(bal);

    }
}
