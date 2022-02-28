package com.codeup.springblog;


import com.codeup.springblog.repositories.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    // Dependency Injection ///
    private PostRepository postDao;

    public PostController(PostRepository postDao){
        this.postDao = postDao;
    }

    @GetMapping("/posts/index")
    public String allPosts(Model model) {
        model.addAttribute("allPosts", postDao.findAll());
//        List<Post> all = new ArrayList<>();
//        all.add(new Post(1, "test", "body test"));
//        all.add(new Post(2, "test2", "body test2"));
//        model.addAttribute("allPosts", all);
        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String postById(@PathVariable long id, Model model) {
        model.addAttribute("post", postDao.findById(id));
//        Post post1 = new Post(1, "test", "body test");
//        model.addAttribute("post", post1);
        return "posts/show";
    }

    @GetMapping("/posts/create")
    public String postForm() {
        return "posts/create";
    }

    @PostMapping("/posts/create")
    public String createPost(@RequestParam(name = "title") String title, @RequestParam(name = "body") String body) {
        Post newPost = new Post(title, body);
        postDao.save(newPost);
        return "redirect:/posts/index";
    }

    @GetMapping("/posts/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
        Post postEdit = postDao.getById(id);
        model.addAttribute("postEdit", postEdit);
        return "posts/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String submitEdit(@RequestParam(name = "title") String title, @RequestParam(name = "body") String body, @PathVariable long id) {
        Post postEdit = postDao.getById(id);
        postEdit.setTitle(title);
        postEdit.setBody(body);
        postDao.save(postEdit);
        return "redirect:/posts/index";
    }

    @GetMapping("/posts/{id}/delete")
    public String delete(@PathVariable long id) {
        Post postDelete = postDao.getById(id);
        postDao.delete(postDelete);
        return "redirect:/posts/index";
    }

}
