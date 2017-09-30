package me.zpjiang.Controllers;

import me.zpjiang.Models.User;
import me.zpjiang.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Author: zhipeng
 * Date: 9/28/17 8:45 PM
 */
@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // for test ...
    @GetMapping(path="/users")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
