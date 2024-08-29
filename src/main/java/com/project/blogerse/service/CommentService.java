package com.project.blogerse.service;

import com.project.blogerse.entity.BlogEntity;
import com.project.blogerse.entity.Comment;
import com.project.blogerse.repo.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogerseService blogService; // Assuming you have a BlogService for managing blogs

    public List<Comment> getCommentsByBlogId(String blogId) {
        return commentRepository.findByBlogId(blogId);
    }

    public Comment addComment(String blogId, String content, String author) {
        BlogEntity blog = blogService.blogById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + blogId));

        Comment comment = new Comment();
        comment.setBlog(blog);
        comment.setContent(content);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }


    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }
}
