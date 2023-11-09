import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
class PizzaOrderGUI extends JFrame implements ActionListener {

    private  JButton addData;
    private JButton printInvoice;
    private JButton remove;
    private JTextField paymentTextField, balanceTextField, totalTextField;
    private JComboBox comboBox, sizeComboBox, flavorComboBox;
    private JTable table;
    ArrayList <String> order = new ArrayList<>();
    private int totalBill;

    DefaultTableModel m;

    public void actionPerformed(ActionEvent action) {
        if (action.getSource() == addData) {
            String pizzaSize = sizeComboBox.getSelectedItem().toString();
            String pizzaFlavor = flavorComboBox.getSelectedItem().toString();
            int price = 0;
            if (pizzaSize.equalsIgnoreCase("Small")){
                price = 500;
            } else if (pizzaSize.equalsIgnoreCase("Medium")) {
                price = 800;

            } else if (pizzaSize.equalsIgnoreCase("large")) {
                price = 1000;

            } else if (pizzaSize.equalsIgnoreCase("Extra Large")) {
                price = 1400;

            }
            String q = comboBox.getSelectedItem().toString();
            int tot = 0;
            tot = price * Integer.parseInt(q);
            m.addRow(new Object[]{pizzaFlavor,pizzaSize, price, q, tot});
            totalBill += tot;

            order.add(pizzaFlavor);
            order.add(pizzaSize);
            order.add(q);
            order.add(Integer.toString(tot));

            totalTextField.setText(Integer.toString(totalBill));
        }

        if (action.getSource() == printInvoice) {
            if(!paymentTextField.getText().isEmpty()) {
                if (!balanceTextField.getText().isEmpty()) {

                    int sum = 0;
                    for (int i = 1; i < table.getRowCount(); i++) {
                        sum = sum + Integer.parseInt(table.getValueAt(i, 4).toString());
                    }
                    totalTextField.setText(Integer.toString(sum));

                    try {
                        int pay = Integer.parseInt(paymentTextField.getText());
                        int tot = Integer.parseInt(totalTextField.getText());

                        int bal = pay - tot;
                        balanceTextField.setText(String.valueOf(bal));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    new Invoice();
                }
            }else {
                JOptionPane.showMessageDialog(this,"Enter Payment Amount");
            }
        }


        if (action.getSource() == remove) {
            int selectedRow =table.getSelectedRow();
            if (selectedRow != -1){

            m.removeRow(selectedRow);
            int sum = 0;
            for (int i = 1; i < table.getRowCount(); i++) {
                sum = sum + Integer.parseInt(table.getValueAt(i, 4).toString());
            }
            totalTextField.setText(Integer.toString(sum));
            }
        }
    }

    public PizzaOrderGUI() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // North Panel (Header)
        JPanel northPanel = new JPanel();
        JLabel header = new JLabel("Pizza Ordering");
        header.setFont(new Font("Arial", Font.BOLD, 20));
        northPanel.add(header);
        c.add(northPanel, BorderLayout.NORTH);

        JPanel westPanel = new JPanel(new GridLayout(10, 1));

        JLabel sizeLabel = new JLabel("Sizes");
        String[] sizeString = {"Small", "Medium", "Large", "Extra Large"};
        sizeComboBox = new JComboBox(sizeString);

        JLabel flavorLabel = new JLabel("Flavors");
        String[] str = {"Pepperoni", "Margherita", "Veggie", "Meat Lovers"};
        flavorComboBox = new JComboBox(str);

        sizeComboBox.setPreferredSize(new Dimension(100, 10)); // Adjust the preferred size
        flavorComboBox.setPreferredSize(new Dimension(100, 10)); // Adjust the preferred size

        westPanel.add(new JLabel());
        westPanel.add(sizeLabel);
        westPanel.add(sizeComboBox);
        westPanel.add(new JLabel());
        westPanel.add(flavorLabel);
        westPanel.add(flavorComboBox);
        c.add(westPanel, BorderLayout.WEST);

        // Center Panel (Order Details Table)
        JPanel centerPanel = new JPanel(new BorderLayout());
        Object[][] itemName = {{}};
        String[] colName = {"Flavor","Size", "Price", "Quantity", "Total"};
        m = new DefaultTableModel(null,colName);
        table = new JTable(m);
        centerPanel.add(new JScrollPane(table)); // Use a scroll pane for the table
        c.add(centerPanel, BorderLayout.CENTER);

        // East Panel (Total Price)
        JPanel eastPanel = new JPanel(new GridLayout(3, 1));
        JLabel total = new JLabel("Total");
        totalTextField = new JTextField();
        totalTextField.setEditable(false); // Make it read-only

        JLabel payment = new JLabel("Payment");
        paymentTextField = new JTextField();
        paymentTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(!paymentTextField.getText().isEmpty()) {
                    if (e.getKeyCode() == 10) {
                        int payment =Integer.parseInt(paymentTextField.getText());
                        if (payment>=totalBill){
                            int balance =payment-totalBill;
                            balanceTextField.setText(Integer.toString(balance));
                        }else {
                            JOptionPane.showMessageDialog(null,"Invalid Payment Input");
                        }
                    }
                }
            }
        });
        JLabel balance = new JLabel("Balance");
        balanceTextField = new JTextField();
        balanceTextField.setEditable(false); // Make it read-only
        eastPanel.add(total);
        eastPanel.add(totalTextField);
        eastPanel.add(payment);
        eastPanel.add(paymentTextField);
        eastPanel.add(balance);
        eastPanel.add(balanceTextField);
        c.add(eastPanel, BorderLayout.EAST);

        // South Panel (Buttons, Quantity, and Flavor)
        JPanel southPanel = new JPanel();
        addData = new JButton("Add");
        addData.addActionListener(this);
        printInvoice = new JButton("Print Receipt");
        printInvoice.addActionListener(this);
        String[] arr = {"1", "2", "3", "4", "5"};
        comboBox = new JComboBox<>(arr);
        JLabel quantity = new JLabel("Quantity");
        remove = new JButton("Remove");
        remove.addActionListener(this);


        southPanel.add(addData);
        southPanel.add(printInvoice);
        southPanel.add(remove);
//        southPanel.add(orderButton);
        southPanel.add(quantity);
        southPanel.add(comboBox);

        c.add(southPanel, BorderLayout.SOUTH);

        setTitle("Pizza Ordering System");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }



    class Invoice extends JFrame {
        JTextArea invoiceData;
        StringBuilder stringBuilder;

        Invoice() {
            setTitle("Pizza Shop Invoice");
            setLayout(new BorderLayout());
            setSize(800, 500);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setVisible(true);

            StringBuilder stringBuilder = new StringBuilder();

            // Generate a unique invoice ID (you can use a more robust method)
            String invoiceID = "INV-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            // Get the current date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String currentDate = dateFormat.format(new Date());
            String currentTime = timeFormat.format(new Date());

            // Prepare the invoice header
            String header = "Invoice ID: " + invoiceID + "\nDate: " + currentDate + "\nTime: " + currentTime + "\n\n";

            // Prepare the invoice content
            String pizzaFormat = "%-12s | %-12s | %-15s | %-12s\n";
            String invoiceContent = "\tPizza\t|\tSize\t|\tQuantity\t|\tPrice\t|";

            StringBuilder content = new StringBuilder();
            int counter = 0;
            for (int i = 0; i < order.size(); i++) {
                content.append("\t").append(order.get(i)).append("\t|");
                counter++;
                if (counter == 4) {
                    content.append("\n");
                    counter = 0;
                }
            }

            // Get the total bill, payment, and balance (you should update this part)
            String totalBill = totalTextField.getText(); // Replace with actual total
            String payment = paymentTextField.getText(); // Replace with actual payment
            String balance = balanceTextField.getText(); // Replace with actual balance

            invoiceData = new JTextArea(header + invoiceContent + "\n"+ content.toString()+"\n" + "\t\t\t\t\t\t\t  Total Bill : " + totalBill + "\n" + "\t\t\t\t\t\t\t  Payment : " + payment + "\n" + "\t\t\t\t\t\t\t  Balance : " + balance);
            invoiceData.setBounds(10, 10, 480, 400);
            add(invoiceData,BorderLayout.CENTER);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        new PizzaOrderGUI();
    }
}
