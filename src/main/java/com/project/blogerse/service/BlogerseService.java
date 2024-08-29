package com.project.blogerse.service;

import com.project.blogerse.entity.BlogEntity;
import com.project.blogerse.entity.User;
import com.project.blogerse.repo.BlogerseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
//@Slf4j
public class BlogerseService {

    private static final Logger log = LoggerFactory.getLogger(BlogerseService.class);
    @Autowired
    private BlogerseRepository blogerseRepository;

    @Autowired
    UserService userService;

    @Transactional
    public void saveEntry(BlogEntity blogEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            blogEntry.setDate(LocalDateTime.now());
            BlogEntity save = blogerseRepository.save(blogEntry);
            user.getBlogEntries().add(save);
            userService.saveUser(user);
        }
        catch (Exception e){

            throw new RuntimeException("An error occured while saving the " +
                    "entry!",e);
        }
    }
    public void saveEntry(BlogEntity blogEntry) {
            blogerseRepository.save(blogEntry);
    }

    public Optional<BlogEntity> blogById(String Id) {
        return blogerseRepository.findById(Id);
    }

    public List<BlogEntity> getAllBlogs() {
        return blogerseRepository.findAll(); // Assuming BlogRepository extends JpaRepository or CrudRepository
    }

    @Transactional
    public boolean deleteBlogById(String Id, String userName) {
        boolean removed=false;
        try
        {
            User user = userService.findByUserName(userName);
            removed = user.getBlogEntries().removeIf(x -> x.getId().equals(Id));
            if (removed) {
                userService.saveUser(user);
                blogerseRepository.deleteById(Id);
            }
        }
        catch (Exception e){
            log.error("Error ",e);
            throw new RuntimeException("An error occured while deleting the " +
                    "entry");
        }
        return removed;
    }



}
