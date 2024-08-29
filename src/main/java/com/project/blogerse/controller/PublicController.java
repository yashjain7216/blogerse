package com.project.blogerse.controller;

import com.project.blogerse.entity.BlogEntity;
import com.project.blogerse.entity.User;
import com.project.blogerse.service.BlogerseService;
import com.project.blogerse.service.UserDetailsServiceImpl;
import com.project.blogerse.service.UserService;
import com.project.blogerse.utils.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://127.0.0.1:5500", "https://blogerse-frontend.vercel.app/"}) // Allow CORS for specific origins
@RequestMapping("/public")
public class PublicController {
    private static final Logger log = LoggerFactory.getLogger(PublicController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private BlogerseService blogerseService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/signup")
    public void signup(@RequestBody User user) {
        userService.saveNewUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())
                                              );
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            log.error("Incorrect username or password: {}", e.getMessage());
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Exception occurred while creating authentication token: {}", e.getMessage(), e);
            return new ResponseEntity<>("Authentication failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("id/{ID}")
    public ResponseEntity<BlogEntity> blogById(@PathVariable String ID) {
        Optional<BlogEntity> blogEntity = blogerseService.blogById(ID);
        if (blogEntity.isPresent()) {
            return new ResponseEntity<>(blogEntity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }




}
