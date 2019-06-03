package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


    //======= Security ===================
    @RequestMapping("/")
    public String listMessages(Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        if (userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }
        return "list";
    }


    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){

        if(result.hasErrors()){
            return "registration";
        }

        else {
            userService.saveUser(user);
            model.addAttribute("result", "User Account Created");
            System.out.println(result);
        }
        return "redirect:/";
    }

    @RequestMapping("/login")
    public String login() {

        return "login";
    }
    @RequestMapping("/secure")
    public String secure(Model model){
        model.addAttribute("user", userService.getUser());
        return "secure";
    }

    // ================ Message ===============
    //======= for Admin
    @GetMapping("/add")
    public String messageForm(Model model) {
        model.addAttribute("message", new Message());
        return "messageform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Message message,
                              BindingResult result,
                              Principal principal){
        String username = principal.getName();
        if(result.hasErrors()){
            return "messageform";
        }

//        message.setUser(userService.getUser());
        message.setUser(userRepository.findByUsername(username));
        messageRepository.save(message);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        model.addAttribute("user", userService.getUser());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        return "messageform";
    }

    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/";
    }
//======================= User Course
// ====================== for Authenticated user
//  @RequestMapping("/deleteByUser/{id}")
//  public String delCourse(Model model) {
//    model.addAttribute("usercourse", userCourseRepository.findAll());
//    if (userService.getUser() != null) {
//      model.addAttribute("user_id", userService.getUser().getId());
//    }
//    return "list";
//  }
//
//  @RequestMapping("/detail/{id}")
//  public String showCar(@PathVariable("id") long id, Model model){
//    model.addAttribute("usercourse", userCoursecarRepository.findById(id).get());
//    return "show";
//  }
//
//  @RequestMapping("/update/{id}")
//  public String updateCar(@PathVariable("id") long id, Model model){
//    model.addAttribute("categories", categoryRepository.findAll());
//    model.addAttribute("car", carRepository.findById(id));
//    return "carForm";
//  }
}
