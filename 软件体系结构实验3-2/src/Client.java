
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class Client extends JFrame {
    private AddressBookService addressBookService;
    private JTextArea contactsTextArea;

    public Client() {
        super("Personal Address Book");

        // 创建业务层对象
        addressBookService = new AddressBookService();

        // 创建GUI界面
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        contactsTextArea = new JTextArea();
        contactsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(contactsTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton displayButton = new JButton("Display Contacts");
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 处理显示联系人按钮点击事件
                displayContacts();
            }
        });
        buttonPanel.add(displayButton);

        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 处理添加联系人按钮点击事件
                addContact();
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Contact");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 处理更新联系人按钮点击事件
                updateContact();
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Contact");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 处理删除联系人按钮点击事件
                deleteContact();
            }
        });
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void displayContacts() {
        // 查询联系人信息并更新文本区域
        String contacts = addressBookService.getContacts();
        contactsTextArea.setText(contacts);
    }

    private void addContact() {
        // 弹出输入对话框获取联系人信息
        String name = JOptionPane.showInputDialog(null, "Enter name:");
        String address = JOptionPane.showInputDialog(null, "Enter address:");
        String phone = JOptionPane.showInputDialog(null, "Enter phone:");

        // 添加联系人
        boolean addResult = addressBookService.addContact(name, address, phone);
        if (addResult) {
            JOptionPane.showMessageDialog(null, "Contact added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add contact.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateContact() {
        // 弹出输入对话框获取联系人姓名
        String name = JOptionPane.showInputDialog(null, "Enter name:");

        // 检查联系人是否存在
        if (!addressBookService.checkContactExists(name)) {
            JOptionPane.showMessageDialog(null, "Contact does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 弹出输入对话框获取新的联系人信息
        String address = JOptionPane.showInputDialog(null, "Enter new address:");
        String phone = JOptionPane.showInputDialog(null, "Enter new phone:");

        // 更新联系人信息
        boolean updateResult = addressBookService.updateContact(name, address, phone);
        if (updateResult) {
            JOptionPane.showMessageDialog(null, "Contact updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update contact.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteContact() {
        // 弹出输入对话框获取联系人姓名
        String name = JOptionPane.showInputDialog(null, "Enter name:");

        // 检查联系人是否存在
        if (!addressBookService.checkContactExists(name)) {
            JOptionPane.showMessageDialog(null, "Contact does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 删除联系人
        boolean deleteResult = addressBookService.deleteContact(name);
        if (deleteResult) {
            JOptionPane.showMessageDialog(null, "Contact deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to delete contact.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
}
