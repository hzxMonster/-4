
import java.sql.*;
import java.util.Scanner;

public class Server {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/contacts?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "hzx030913";

    public static void main(String[] args) {
        try {
            // 连接到数据库
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 创建Statement对象
            Statement statement = connection.createStatement();

            // 处理客户端的请求
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // 显示菜单
                System.out.println("Personal Address Book System");
                System.out.println("1. Display Contacts");
                System.out.println("2. Add Contact");
                System.out.println("3. Update Contact");
                System.out.println("4. Delete Contact");
                System.out.println("0. Exit");

                // 获取用户输入的选项
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 读取换行符

                // 处理用户选择
                switch (choice) {
                    case 1:
                        // 查询数据库中的联系人信息
                        String contacts = getContactsFromDatabase(statement);
                        // 打印联系人信息
                        System.out.println(contacts);
                        break;
                    case 2:
                        // 获取用户输入的联系人信息
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter address: ");
                        String address = scanner.nextLine();
                        System.out.print("Enter phone: ");
                        String phone = scanner.nextLine();

                        // 添加联系人到数据库
                        boolean addResult = addContactToDatabase(statement, name, address, phone);
                        // 打印结果
                        if (addResult) {
                            System.out.println("Contact added successfully!");
                        } else {
                            System.out.println("Failed to add contact.");
                        }
                        break;
                    case 3:
                        // 获取用户输入的联系人信息
                        System.out.print("Enter name: ");
                        name = scanner.nextLine();
                        System.out.print("Enter new address: ");
                        address = scanner.nextLine();
                        System.out.print("Enter new phone: ");
                        phone = scanner.nextLine();

                        // 更新联系人在数据库中的信息
                        boolean updateResult = updateContactInDatabase(statement, name, address, phone);
                        // 打印结果
                        if (updateResult) {
                            System.out.println("Contact updated successfully!");
                        } else {
                            System.out.println("Failed to update contact.");
                        }
                        break;
                    case 4:
                        // 获取用户输入的联系人姓名
                        System.out.print("Enter name: ");
                        name = scanner.nextLine();

                        // 从数据库中删除联系人
                        boolean deleteResult = deleteContactFromDatabase(statement, name);
                        // 打印结果
                        if (deleteResult) {
                            System.out.println("Contact deleted successfully!");
                        } else {
                            System.out.println("Failed to delete contact.");
                        }
                        break;
                    case 0:
                        // 关闭连接并退出.
                        statement.close();
                        connection.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getContactsFromDatabase(Statement statement) throws SQLException {
        StringBuilder contacts = new StringBuilder();

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

        return contacts.toString();
    }

    private static boolean addContactToDatabase(Statement statement, String name, String address, String phone) throws SQLException {
        // 执行插入语句
        int rowsAffected = statement.executeUpdate("INSERT INTO contacts (name, address, phone) VALUES ('" + name + "', '" + address + "', '" + phone + "')");

        return rowsAffected > 0;
    }

    private static boolean updateContactInDatabase(Statement statement, String name, String address, String phone) throws SQLException {
        // 执行更新语句
        int rowsAffected = statement.executeUpdate("UPDATE contacts SET address = '" + address + "', phone = '" + phone + "' WHERE name = '" + name + "'");

        return rowsAffected > 0;
    }

    private static boolean deleteContactFromDatabase(Statement statement, String name) throws SQLException {
        // 执行删除语句
        int rowsAffected = statement.executeUpdate("DELETE FROM contacts WHERE name = '" + name + "'");

        return rowsAffected > 0;
    }
}

