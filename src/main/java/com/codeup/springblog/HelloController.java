package com.codeup.springblog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// To kill other processes running on the designated server run
// lsof -t -i :8080
// kill ...

// Takes the place of webservlet / establishes this class is a controller
@Controller
public class HelloController {
    // Takes place of the webpattern / getRequests
    @GetMapping("/hello")
    // Takes place of response declaration
    @ResponseBody
    public String hello() {
        //  You can add html tags within the return ie...<h1>Hello from Spring!</h1>
        return "Hello from Spring!";
    }
    // Can have multiple @GetMapping to other places
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "test";
    }

    //  Path variables w/strings
    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

    //  Path variables w/numbers and custom request mapping which is the longer version of get mapping
    @RequestMapping(path = "/increment/{number}", method = RequestMethod.GET)
    @ResponseBody
    public String addOne(@PathVariable int number) {
        return number + " plus one is " + (number + 1);
    }

    @GetMapping("/join")
    public String showJoinForm() {
        return "join";
    }

    @PostMapping("/join")
    public String joinCohort(@RequestParam(name = "cohort") String cohort, Model model) {
        model.addAttribute("cohort", "Welcome to " + cohort + "!");
        return "join";
    }

}
