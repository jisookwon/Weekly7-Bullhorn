package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Executable;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Override
    public void run(String... strings) throws Exception{
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        Role adminRole = roleRepository.findByRole("ADMIN");
        Role userRole = roleRepository.findByRole("USER");

        User user = new User("jim@jim.com", "Jim", "Jimmerson", true, "jim",passwordEncoder.encode("password"));
        user.setRoles(Arrays.asList(userRole));
        userRepository.save(user);

        user = new User("admin@admin.com", "Admin", "User", true, "admin",passwordEncoder.encode("password"));
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);

        //======== my data=======
        Message message = new Message("My favorite puppy", "so cute!", "5/31", "Tori");
        message.setUser(user);
        messageRepository.save(message);

        message = new Message("My favorite food", "so dekecious!", "6/1", "Tori");
        message.setUser(user);
        messageRepository.save(message);

        message = new Message("My favorite weather", "so beautiful!", "6/2", "Tori");
        message.setUser(user);
        messageRepository.save(message);
    }
}
