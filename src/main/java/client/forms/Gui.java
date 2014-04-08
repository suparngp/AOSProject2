package client.forms;
/**
 * @author Nimisha
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Gui extends JFrame{


    JRadioButton Create, Read, Update, Delete, None;
    public JTextField txtId, txtName, txtOpbal,txtCurbal,txtCreated,txtUpdated;
    public JLabel lblAccountId,lblName,lblOpeningBalance, lblCurrentBalance, lblCreatedAt, lblUpdatedAt;
    public JButton btnSubmit;
    protected static JLabel lblMessage;

    public Gui() {
        getContentPane().setLayout(null);
        ButtonGroup buttongroup= new ButtonGroup();

        JLabel lblSelect = new JLabel("Select ");
        lblSelect.setFont(new Font("Arial", Font.BOLD, 11));
        lblSelect.setBounds(51, 11, 76, 22);
        getContentPane().add(lblSelect);

        Create = new JRadioButton("CREATE");
        Create.setBounds(51, 58, 109, 23);
        getContentPane().add(Create);
        buttongroup.add(Create);
        Create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CreateForm();
            }});


        Read = new JRadioButton("READ");
        Read.setBounds(51, 95, 109, 23);
        getContentPane().add(Read);
        buttongroup.add(Read);
        Read.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ReadForm();
            }});

        Update = new JRadioButton("UPDATE");
        Update.setBounds(229, 57, 109, 23);
        getContentPane().add(Update);
        buttongroup.add(Update);
        Update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UpdateForm();
            }});

        Delete = new JRadioButton("DELETE");
        Delete.setBounds(229, 95, 109, 23);
        getContentPane().add(Delete);
        buttongroup.add(Delete);
        Delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DeleteForm();
            }});

        None = new JRadioButton("NONE");
        None.setBounds(51, 134, 109, 23);
        getContentPane().add(None);
        buttongroup.add(None);
        None.setSelected(true);

        lblAccountId = new JLabel("Account ID");
        lblAccountId.setBounds(51, 220, 76, 22);
        getContentPane().add(lblAccountId);
        lblAccountId.setVisible(false);

        txtId = new JTextField();
        txtId.setBounds(162, 221, 137, 20);
        getContentPane().add(txtId);
        txtId.setColumns(10);
        txtId.setVisible(false);

        lblName = new JLabel("Name");
        lblName.setBounds(51, 264, 62, 20);
        getContentPane().add(lblName);
        lblName.setVisible(false);

        txtName = new JTextField();
        txtName.setBounds(162, 264, 137, 20);
        getContentPane().add(txtName);
        txtName.setColumns(10);
        txtName.setVisible(false);

        lblOpeningBalance = new JLabel("Opening Balance");
        lblOpeningBalance.setBounds(51, 310, 99, 22);
        getContentPane().add(lblOpeningBalance);
        lblOpeningBalance.setVisible(false);

        txtOpbal = new JTextField();
        txtOpbal.setBounds(162, 311, 137, 20);
        getContentPane().add(txtOpbal);
        txtOpbal.setColumns(10);
        txtOpbal.setVisible(false);

        lblCurrentBalance = new JLabel("Current Balance");
        lblCurrentBalance.setBounds(51, 356, 99, 22);
        getContentPane().add(lblCurrentBalance);
        lblCurrentBalance.setVisible(false);

        txtCurbal = new JTextField();
        txtCurbal.setBounds(162, 357, 137, 20);
        getContentPane().add(txtCurbal);
        txtCurbal.setColumns(10);
        txtCurbal.setVisible(false);

        lblCreatedAt = new JLabel("Created At");
        lblCreatedAt.setBounds(51, 404, 99, 22);
        getContentPane().add(lblCreatedAt);
        lblCreatedAt.setVisible(false);

        txtCreated = new JTextField();
        txtCreated.setBounds(162, 405, 137, 20);
        getContentPane().add(txtCreated);
        txtCreated.setColumns(10);
        txtCreated.setVisible(false);

        lblUpdatedAt = new JLabel("Updated At");
        lblUpdatedAt.setBounds(51, 447, 99, 22);
        getContentPane().add(lblUpdatedAt);
        lblUpdatedAt.setVisible(false);

        txtUpdated = new JTextField();
        txtUpdated.setBounds(162, 448, 137, 20);
        getContentPane().add(txtUpdated);
        txtUpdated.setColumns(10);
        txtUpdated.setVisible(false);

        btnSubmit = new JButton("SUBMIT");
        btnSubmit.setBounds(129, 500, 89, 23);
        getContentPane().add(btnSubmit);

        lblMessage = new JLabel("");
        lblMessage.setBounds(51, 171, 342, 29);
        getContentPane().add(lblMessage);
        btnSubmit.setVisible(false);

        this.setVisible(true);
        this.setSize(419, 572);
        this.setTitle("AOS PROJECT");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


    }
}
