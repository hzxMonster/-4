
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddressBook extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/contacts?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "hzx030913";
    private JTextArea contactsTextArea;

    public AddressBook() {
        super("Personal Address Book");

        // 创建连接到数据库
        Connection connection;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 创建Statement对象
        Statement statement;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to create database statement.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
                // 查询数据库中的联系人信息
                String contacts = getContactsFromDatabase(statement);
                contactsTextArea.setText(contacts);
            }
        });
        buttonPanel.add(displayButton);

        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 获取用户输入的联系人信息
                String name = JOptionPane.showInputDialog(null, "Enter name:");
                String address = JOptionPane.showInputDialog(null, "Enter address:");
                String phone = JOptionPane.showInputDialog(null, "Enter phone:");

                // 添加联系人到数据库
                boolean addResult = addContactToDatabase(statement, name, address, phone);
                if (addResult) {
                    JOptionPane.showMessageDialog(null, "Contact added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add contact.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Contact");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 获取用户输入的联系人姓名
                String name = JOptionPane.showInputDialog(null, "Enter name:");

                // 检查联系人是否存在
                if (!checkContactExists(statement, name)) {
                    JOptionPane.showMessageDialog(null, "Contact does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 获取用户输入的新联系人信息
                String address = JOptionPane.showInputDialog(null, "Enter new address:");
                String phone = JOptionPane.showInputDialog(null, "Enter new phone:");

                // 更新联系人在数据库中的信息
                boolean updateResult = updateContactInDatabase(statement, name, address, phone);
                if (updateResult) {
                    JOptionPane.showMessageDialog(null, "Contact updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update contact.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Contact");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 获取用户输入的联系人姓名
                String name = JOptionPane.showInputDialog(null, "Enter name:");

                // 检查联系人是否存在
                if (!checkContactExists(statement, name)) {
                    JOptionPane.showMessageDialog(null, "Contact does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 从数据库中删除联系人
                boolean deleteResult = deleteContactFromDatabase(statement, name);
                if (deleteResult) {
                    JOptionPane.showMessageDialog(null, "Contact deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete contact.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private static String getContactsFromDatabase(Statement statement) {
        StringBuilder contacts = new StringBuilder();

        try {
            // 执行查询语句
            ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts");

            // 处理查询结果
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                contacts.append("Name: ").append(name).append(", Address: ").append(address).append(", Phone: ").append(phone).append("\n");
            }

            // 关闭结果集
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve contacts from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return contacts.toString();
    }

    private static boolean addContactToDatabase(Statement statement, String name, String address, String phone) {
        try {
            // 执行插入语句
            int rowsAffected = statement.executeUpdate("INSERT INTO contacts (name, address, phone) VALUES ('" + name + "', '" + address + "', '" + phone + "')");

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add contact to database.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static boolean updateContactInDatabase(Statement statement, String name, String address, String phone) {
        try {
            // 执行更新语句
            int rowsAffected = statement.executeUpdate("UPDATE contacts SET address = '" + address + "', phone = '" + phone + "' WHERE name = '" + name + "'");

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update contact in database.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static boolean deleteContactFromDatabase(Statement statement, String name) {
        try {
            // 执行删除语句
            int rowsAffected = statement.executeUpdate("DELETE FROM contacts WHERE name = '" + name + "'");

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete contact from database.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static boolean checkContactExists(Statement statement, String name) {
        try {
            // 执行查询语句
            ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts WHERE name = '" + name + "'");

            // 检查是否有结果
            boolean exists = resultSet.next();

            // 关闭结果集
            resultSet.close();

            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to check contact existence in database.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AddressBook().setVisible(true);
            }
        });
    }
}
