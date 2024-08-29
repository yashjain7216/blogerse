package com.project.blogerse.controller;

import com.project.blogerse.entity.Comment;
import com.project.blogerse.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/blog/{blogId}")
    public List<Comment> getCommentsByBlogId(@PathVariable String blogId) {
        return commentService.getCommentsByBlogId(blogId);
    }

    @PostMapping("/blog/{blogId}")
    public Comment addComment(@PathVariable String blogId, @RequestBody Comment comment) {
        return commentService.addComment(blogId, comment.getContent(), comment.getAuthor());
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId);
    }
}
