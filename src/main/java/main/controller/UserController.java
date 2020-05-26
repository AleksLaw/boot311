package main.controller;


import main.model.User;
import main.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

@Controller
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/all")
    public ModelAndView listUsers() {
        User user = new User("Vasya", "Pupkin");
        userRepo.save(user);
        Iterable<User> all = userRepo.findAll();
        ModelAndView modelAndView = new ModelAndView("all");
        modelAndView.getModelMap().addAttribute("allUsers", all);
        return modelAndView;
    }


    @GetMapping("/")
    public String greeting(
            @RequestParam(name="name", required=false, defaultValue="World") String name,
            Model model
    ) {
        model.addAttribute("name", name);
        return "greeting";
    }
}
