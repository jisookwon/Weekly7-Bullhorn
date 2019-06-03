package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import javax.jws.WebParam;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;


@Controller
public class HomeController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    CloudinaryConfig cloudc;


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
//        return "redirect:/";
    }

    // ================ Message ===============
    //======= for Admin
    @GetMapping("/add")
    public String messageForm(Model model) {
        model.addAttribute("message", new Message());
        model.addAttribute("user", userService.getUser());

        return "messageform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Message message,
                              BindingResult result,
                              Principal principal,
                              Model model,
                              @RequestParam("file") MultipartFile file){

        String username = principal.getName();
        if(result.hasErrors()){
            return "messageform";
        }
        if (file.isEmpty()){
            return "redirect:/add";
        }
        try{
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            message.setHeadshot(uploadResult.get("url").toString());
            messageRepository.save(message);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/add";
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
    //=============== Cloudinary

}
