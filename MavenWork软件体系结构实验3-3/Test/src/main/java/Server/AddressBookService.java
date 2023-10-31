package Server;

import Dao.AddressBookDAO;

public class AddressBookService {
    private AddressBookDAO addressBookDAO;

    public AddressBookService(AddressBookDAO addressBookDAO) {
        this.addressBookDAO = addressBookDAO;
    }

    public String getContacts() {
        // 调用数据访问层方法查询联系人信息
        return addressBookDAO.getContacts();
    }

    public boolean addContact(String name, String address, String phone) {
        // 调用数据访问层方法添加联系人
        return addressBookDAO.addContact(name, address, phone);
    }

    public boolean updateContact(String name, String address, String phone) {
        // 调用数据访问层方法更新联系人信息
        return addressBookDAO.updateContact(name, address, phone);
    }

    public boolean deleteContact(String name) {
        // 调用数据访问层方法删除联系人
        return addressBookDAO.deleteContact(name);
    }
}

