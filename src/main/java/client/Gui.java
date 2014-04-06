package client;
/**
 * @author Nimisha
 *
 */

import client.forms.CreateForm;
import client.forms.DeleteForm;
import client.forms.ReadForm;
import client.forms.UpdateForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame {

    public static void main(String args[]) {
        Gui gui = new Gui();
    }

    JRadioButton Create, Read, Update, Delete, None;

    public Gui() {
        getContentPane().setLayout(null);
        ButtonGroup buttongroup = new ButtonGroup();

        JLabel lblSelect = new JLabel("Select ");
        lblSelect.setFont(new Font("Arial", Font.BOLD, 11));
        lblSelect.setBounds(51, 29, 76, 22);
        getContentPane().add(lblSelect);

        Create = new JRadioButton("CREATE");
        Create.setBounds(51, 69, 109, 23);
        getContentPane().add(Create);
        buttongroup.add(Create);
        Create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CreateForm();
            }
        });


        Read = new JRadioButton("READ");
        Read.setBounds(51, 108, 109, 23);
        getContentPane().add(Read);
        buttongroup.add(Read);
        Read.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ReadForm();
            }
        });

        Update = new JRadioButton("UPDATE");
        Update.setBounds(229, 69, 109, 23);
        getContentPane().add(Update);
        buttongroup.add(Update);
        Update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UpdateForm();
            }
        });

        Delete = new JRadioButton("DELETE");
        Delete.setBounds(229, 108, 109, 23);
        getContentPane().add(Delete);
        buttongroup.add(Delete);
        Delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DeleteForm();
            }
        });

        None = new JRadioButton("NONE");
        None.setBounds(51, 150, 109, 23);
        getContentPane().add(None);
        buttongroup.add(None);
        None.setSelected(true);

        this.setVisible(true);
        this.setSize(400, 300);
        this.setTitle("AOS PROJECT");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


    }


}
