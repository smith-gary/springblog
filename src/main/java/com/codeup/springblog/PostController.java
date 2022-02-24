package com.codeup.springblog;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    @GetMapping("/posts/index")
    public String allPosts(Model model) {
        List<Post> all = new ArrayList<>();
        all.add(new Post(1, "test", "body test"));
        all.add(new Post(2, "test2", "body test2"));

        model.addAttribute("allPosts", all);
        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String postById(@PathVariable long id, Model model) {
        Post post1 = new Post(1, "test", "body test");
        model.addAttribute("post", post1);
        return "posts/show";
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
