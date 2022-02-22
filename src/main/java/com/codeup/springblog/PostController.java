package com.codeup.springblog;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    @GetMapping("/posts")
    @ResponseBody
    public String allPosts() {
        return "Here are all the posts!";
    }

    @GetMapping("/posts/{id}")
    @ResponseBody
    public String postById(@PathVariable long id) {
        return "Here is the post from id: " + id;

    }

    @GetMapping("/posts/create")
    @ResponseBody
    public String postForm() {
        return "view the form for creating a post";
    }

    @PostMapping("/posts/create")
    @ResponseBody
    public String createPost() {
        return "Create new post!";
    }
}
