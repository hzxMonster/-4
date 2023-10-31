package com.zy.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import Control.HomeController;
import Server.AddressBookService;
import Dao.AddressBookDAO;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class Application implements ErrorController {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public HomeController homeController(AddressBookService addressBookService) {
        return new HomeController(addressBookService);
    }

    @Bean
    public AddressBookService addressBookService(AddressBookDAO addressBookDAO) {
        return new AddressBookService(addressBookDAO);
    }

    @Bean
    public AddressBookDAO addressBookDAO() {
        return new AddressBookDAO();
    }

    // 添加以下方法来处理/error路径
    @RequestMapping("/error")
    public String handleError() {
        // 处理错误逻辑，可以返回自定义的错误页面或其他处理方式
        return "errorPage"; // 返回自定义的错误页面
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
