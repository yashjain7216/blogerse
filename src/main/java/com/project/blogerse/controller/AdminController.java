package com.project.blogerse.controller;


import com.project.blogerse.entity.User;
import com.project.blogerse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;


    @GetMapping("/all-users")
    public String getAllUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        return "admin_get_users"; // Thymeleaf template name
    }
//    @GetMapping("/all-users")
//    public ResponseEntity<?> getAllUsers() {
//        List<User> allUsers = userService.getAllUsers();
//        if (allUsers != null && !allUsers.isEmpty()) {
//            return new ResponseEntity<>(allUsers, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
    @PostMapping("/admin-signup")
    public void createAdmin(@RequestBody User user){
        userService.saveAdmin(user);
    }
}
