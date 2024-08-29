package com.project.blogerse.controller;

import com.project.blogerse.entity.BlogEntity;
import com.project.blogerse.entity.User;
import com.project.blogerse.service.BlogerseService;
import com.project.blogerse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"https://blogerse-frontend.vercel.app"})
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogerseService blogerseService;

    @Autowired
    private UserService userService;
    private String convertImageToBase64(MultipartFile image) throws IOException {
        byte[] imageBytes = image.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }


    @GetMapping("/user")
    public ResponseEntity<?> getAllBlogsByUser() {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);

        // Fetch the list of blog entries for the authenticated user
        if (user != null) {
            List<BlogEntity> allBlogs = user.getBlogEntries();
            if (allBlogs != null && !allBlogs.isEmpty()) {
                return new ResponseEntity<>(allBlogs, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No blogs found
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // User not found or not authenticated
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBlogs() {
        try {
            // Fetch the list of all blog entries
            List<BlogEntity> allBlogs = blogerseService.getAllBlogs();

            if (allBlogs != null && !allBlogs.isEmpty()) {
                return new ResponseEntity<>(allBlogs, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No blogs found
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // General error handling
        }
    }


    @PostMapping
    public ResponseEntity<BlogEntity> createBlogs(@RequestParam("title") String title,
                                                  @RequestParam("content") String content,
                                                  @RequestParam("image") MultipartFile image) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            // Convert image to Base64 string
            String imageBase64 = convertImageToBase64(image);

            // Create a new BlogEntity object
            BlogEntity blogEntity = new BlogEntity();
            blogEntity.setTitle(title);
            blogEntity.setContent(content);
            blogEntity.setImage(imageBase64);
            blogerseService.saveEntry(blogEntity, userName);
            return new ResponseEntity<>(blogEntity, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{ID}")
    public ResponseEntity<BlogEntity> blogById(@PathVariable String ID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<BlogEntity> collect = user.getBlogEntries().stream().filter(x -> x.getId().equals(ID)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<BlogEntity> blogEntity = blogerseService.blogById(ID);
            if (blogEntity.isPresent()) {
                return new ResponseEntity<>(blogEntity.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{ID}")
    public ResponseEntity<?> deleteById(@PathVariable String ID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = blogerseService.deleteBlogById(ID, userName);
        if (removed) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{ID}")
    public ResponseEntity<?> updateBlogById(@PathVariable String ID,
                                            @RequestBody BlogEntity newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<BlogEntity> collect = user.getBlogEntries().stream().filter(x -> x.getId().equals(ID)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<BlogEntity> blogEntity = blogerseService.blogById(ID);
            if (blogEntity.isPresent()) {
                BlogEntity oldEntry=blogEntity.get();
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
                blogerseService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
