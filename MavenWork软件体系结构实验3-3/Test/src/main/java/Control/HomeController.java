package Control;
import Server.AddressBookService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class HomeController {
    private AddressBookService addressBookService;

    public HomeController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @GetMapping
    public String home() {
        // 查询联系人信息并返回，使用<br>进行换行
        String contacts = addressBookService.getContacts().replace("\n", "<br>");
        return contacts;
    }


    @GetMapping("/addContact")
    public String addContact(@RequestParam String name, @RequestParam String address, @RequestParam String phone) {
        // 添加联系人
        boolean addResult = addressBookService.addContact(name, address, phone);

        return "Contact added successfully";
    }

    @GetMapping("/updateContact")
    public String updateContact(@RequestParam String name, @RequestParam String address, @RequestParam String phone) {
        // 更新联系人信息
        boolean updateResult = addressBookService.updateContact(name, address, phone);

        return "Contact updated successfully";
    }

    @GetMapping("/deleteContact")
    public String deleteContact(@RequestParam String name) {
        // 删除联系人
        boolean deleteResult = addressBookService.deleteContact(name);

        return "Contact deleted successfully";
    }
}
