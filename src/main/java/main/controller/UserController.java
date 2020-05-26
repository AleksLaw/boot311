package main.controller;


import main.model.Role;
import main.model.User;
import main.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/adminPageInfo")
    public ModelAndView listUsers() {

        Iterable<User> list = userRepo.findAll();
        ModelAndView modelAndView = new ModelAndView("adminPage");
        modelAndView.getModelMap().addAttribute("listUsers", list);
        return modelAndView;
    }

    @GetMapping("/addUserPage")
    public String addUserPage() {
        return "addUserPage";
    }

    @PostMapping("add")
    public String addUser(User user, @RequestParam("role") String role) {
        Set<Role> userRoles = getRoles(role);
        User userNew = new User();
        userNew.setUserRoles(userRoles);
        userNew.setName(user.getName());
        userNew.setPassword(user.getPassword());
        userRepo.save(userNew);
        return "redirect:/adminPageInfo";
    }

    @PostMapping("/editUserPage")
    public ModelAndView viewEditPage(@RequestParam("id") Long id) {
        Iterable<User> list = userRepo.findAllById(Collections.singleton(id));
        ModelAndView modelAndView = new ModelAndView("editUserPage");
        modelAndView.getModelMap().addAttribute("listUsers", list);
        return modelAndView;
    }

    @PostMapping("/edit")
    public String updateUser(User user, @RequestParam("role") String role) {
        User byId = userRepo.findById(user.getId()).get();
        Set<Role> userRoles = getRoles(role);
        byId.setUserRoles(userRoles);
        byId.setName(user.getName());
        byId.setPassword(user.getPassword());
        userRepo.save(byId);
        return "redirect:/adminPageInfo";
    }
    @PostMapping("/delete")
    public String delUser(@RequestParam("id") Long id) {
        userRepo.deleteById(id);
        return "redirect:/adminPageInfo";
    }

    @PostMapping("/delUserPage")
    public ModelAndView viewDelPage(@RequestParam("id") Long id) {
        Iterable<User> list = userRepo.findAllById(Collections.singleton(id));
        ModelAndView modelAndView = new ModelAndView("delUserPage");
        modelAndView.getModelMap().addAttribute("listUsers", list);
        return modelAndView;
    }

    @GetMapping("/")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model
    ) {
        model.addAttribute("name", name);
        return "greeting";
    }

    private Set<Role> getRoles(@RequestParam("role") String role) {
        Set<Role> userRoles = new HashSet<>();
        String[] split = role.split(",");
        Role ss = Role.valueOf(split[0]);
        for (String s : split) {
            userRoles.add(Role.valueOf(s));
        }
        return userRoles;
    }
}
