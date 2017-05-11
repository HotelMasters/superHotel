package pv168.hotelmasters.superhotel.backend;

import javax.swing.*;
import java.awt.*;

/**
 * @author Gabriela Godiskova
 */
public class UserInterface {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTable table1;
    private JButton editButton;
    private JButton deleteButton;
    private JTextPane addAGuestTextPane;
    private JTextPane nameTextPane;
    private JTextField textField1;
    private JTextPane addressTextPane;
    private JTextPane dateOfBirthTextPane;
    private JTextPane creditCardNumTextPane;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton addButton;
    private JTextPane nameTextPane1;
    private JTextField textField5;
    private JTextPane capacityTextPane;
    private JTextField textField6;
    private JTextField textField7;
    private JTextPane priceTextPane;
    private JTable table2;
    private JButton editButton3;
    private JButton deleteButton1;
    private JButton addButton2;
    private JTextPane addARoomTextPane;
    private JTextPane makeAnAccommodationTextPane;
    private JTextArea guestTextArea;
    private JTextArea roomTextArea;
    private JTextArea dateFromTextArea;
    private JTextArea dateToTextArea;
    private JTextField textField8;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField textField9;
    private JTable table3;
    private JButton editButton2;
    private JButton deleteButton2;
    private JButton makeButton;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;

    public static void main(String[] args) {
        JFrame frame = new JFrame("UserInterface");
        frame.setMinimumSize(new Dimension(800,600));
        frame.setContentPane(new UserInterface().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
