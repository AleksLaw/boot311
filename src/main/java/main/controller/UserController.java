package main.controller;


import main.model.Role;
import main.model.User;
import main.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String greeting() {
        return "hello";
    }


    @GetMapping("/adminPage")
    public ModelAndView listUsers() {
        Iterable<User> list = userRepo.findAll();
        ModelAndView modelAndView = new ModelAndView("adminPage");
        modelAndView.getModelMap().addAttribute("listUsers", list);
        return modelAndView;
    }
    @PostMapping ("/adminPage")
    public ModelAndView viewAdminPage() {
        Iterable<User> list = userRepo.findAll();
        ModelAndView modelAndView = new ModelAndView("adminPage");
        modelAndView.getModelMap().addAttribute("listUsers", list);
        return modelAndView;
    }

    @GetMapping("/addUserPage")
    public String addUserPage() {
        return "addUserPage";
    }

    @PostMapping("/add")
    public String addUser(User user, @RequestParam("role") String role) {
        Set<Role> userRoles = getRoles(role);
        User userNew = new User();
        userNew.setUserRoles(userRoles);
        userNew.setName(user.getName());
        userNew.setPassword(user.getPassword());
        userRepo.save(userNew);
        return "redirect:/adminPage";
    }

    @GetMapping("/editUserPage")
    public ModelAndView viewEditPage(@RequestParam("id") Long id) {
        Iterable<User> list = userRepo.findAllById(Collections.singleton(id));
        ModelAndView modelAndView = new ModelAndView("editUserPage");
        modelAndView.getModelMap().addAttribute("listUsers", list);
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editUser(User user, @RequestParam("role") String role) {
        User byId = userRepo.findById(user.getId()).get();
        Set<Role> userRoles = getRoles(role);
        byId.setUserRoles(userRoles);
        byId.setName(user.getName());
        byId.setPassword(user.getPassword());
        userRepo.save(byId);
        return "adminPage";
    }

    @PostMapping("/delete")
    public String delUser(@RequestParam("id") Long id) {
        userRepo.deleteById(id);
        return "adminPage";
    }

    @GetMapping("/delUserPage")
    public ModelAndView viewDelPage(@RequestParam("id") Long id) {
        Iterable<User> list = userRepo.findAllById(Collections.singleton(id));
        ModelAndView modelAndView = new ModelAndView("delUserPage");
        modelAndView.getModelMap().addAttribute("listUsers", list);
        return modelAndView;
    }


    @RequestMapping(value = "/userPageInfo", method = RequestMethod.GET)
    public ModelAndView printWelcome() {
        Iterable<User> list = userRepo.findAll();
        ModelAndView modelAndView = new ModelAndView("userPageInfo");
        modelAndView.getModelMap().addAttribute("listUsers", list);
        return modelAndView;
    }

    private Set<Role> getRoles(@RequestParam("role") String role) {
        Set<Role> userRoles = new HashSet<>();
        String[] split = role.split(",");
        for (String s : split) {
            userRoles.add(Role.valueOf(s));
        }
        return userRoles;
    }
//    @GetMapping("login")
//    public String login() {
//        return "adminPage";
//    }
}
