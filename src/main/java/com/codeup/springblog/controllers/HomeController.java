package com.codeup.springblog.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String landingPage() {
        return "<h1>This is the landing page!</h1>";
    }

    @GetMapping("/home")
    public String welcome() {
        return "home";
    }

    @GetMapping("/roll-dice")
    public String rollDice(){
//    model.addAttribute("number", number);
        return "roll-dice";
    }

    @PostMapping("/roll-dice")
    public String results(@RequestParam(name = "number") int number, Model model) {
        int randNum = (int) (Math.random() * 6) + 1;
        String correct = "You guessed correct with number: " + number;
        String incorrect = "You guessed incorrect with number: " + number;
        if (randNum == number) {
           model.addAttribute("correct", correct) ;
        }else {
            model.addAttribute("incorrect", incorrect);
        }
        return "roll-dice";
    }

    /// Instructor solve ///

//    @GetMapping("/roll-dice")
//    public String showOptions(){
//        return "roll-dice";
//    }
//    @GetMapping("/roll-dice/{n}")
//    public String showResult(@PathVariable int n, Model model){
//        int randNum = (int) (Math.random() * 6) + 1;
//        if(n == randNum) {
//            model.addAttribute("result", "Correct!");
//        } else {
//            model.addAttribute("result", "Incorrect!");
//        }
//        return "roll-dice";
//    }

}
