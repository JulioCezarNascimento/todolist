package br.com.juliocezar.todolist.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserControler {
    
    @PostMapping("/")
    public void createUser(@RequestBody UserModel userModel) {
        System.out.println(userModel.name);
    }
}
