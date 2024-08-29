package com.project.blogerse.service;

import com.project.blogerse.entity.User;
import com.project.blogerse.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger=
            LoggerFactory.getLogger(UserService.class);

    public void saveAdmin(User userEntry) {
        userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword()));
        userEntry.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(userEntry);
    }
    @Autowired
    private MailService mailService;
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public static final PasswordEncoder passwordEncoder=
            new BCryptPasswordEncoder();

    public boolean saveNewUser(User userEntry){
        try {
            userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword()));
            userEntry.setRoles(Arrays.asList("USER"));
            userRepository.save(userEntry);
            log.info("User saved successfully: {}", userEntry.getUserName());
            mailService.sendRegistrationEmail(userEntry.getEmail());
            return true;

        }
        catch (Exception e){
            log.error("Error occured for {}. User already exists!",
                    userEntry.getUserName());
            return false;
        }
    }
    public void saveUser(User userEntry){
        userRepository.save(userEntry);
    }

    public Optional<User> blogById(String Id){
        return userRepository.findById(Id);
    }

    public void deleteBlogById(String Id)
    {
        userRepository.deleteById(Id);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
}
