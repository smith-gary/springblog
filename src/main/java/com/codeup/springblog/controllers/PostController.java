package com.codeup.springblog.controllers;


import com.codeup.springblog.models.EmailService;
import com.codeup.springblog.models.Post;
import com.codeup.springblog.models.User;
import com.codeup.springblog.repositories.PostRepository;
import com.codeup.springblog.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    // Dependency Injection ///
    private PostRepository postDao;
    private UserRepository userDao;
    private EmailService emailService;

    public PostController(PostRepository postDao, UserRepository userDao, EmailService emailService) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.emailService = emailService;
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
        model.addAttribute("post", postDao.getById(id));
//        Post post1 = new Post(1, "test", "body test");
//        model.addAttribute("post", post1);
        return "posts/show";
    }

    @GetMapping("/posts/create")
    public String postForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/create";
    }

    @PostMapping("/posts/create")
//    public String createPost(@RequestParam(name = "title") String title, @RequestParam(name = "body") String body) {
    public String createPost(@ModelAttribute Post post) {
//        User user = userDao.getById(1L);
//        Post newPost = new Post();
//        newPost.setTitle(title);
//        newPost.setBody(body);
//        newPost.setUser(user);
//        postDao.save(newPost);

        post.setUser(userDao.getById(1L));
        emailService.prepareAndSend(post, "test", "this working or what?");
        postDao.save(post);
        return "redirect:/posts/index";
    }

    @GetMapping("/posts/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
//        model.addAttribute("post", new Post());
        Post post = postDao.getById(id);
        model.addAttribute("post", post);
        return "posts/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String submitEdit(@ModelAttribute Post post, @PathVariable long id) {
        post.setId(id);
        post.setUser(userDao.getById(1L));
//        post.setTitle(post.getTitle());
//        post.setBody(post.getBody());
//        postEdit.setBody(body);
        postDao.save(post);
        return "redirect:/posts/index";
    }

    @GetMapping("/posts/{id}/delete")
    public String delete(@PathVariable long id) {
        postDao.deleteById(id);
        return "redirect:/posts/index";
    }

}
