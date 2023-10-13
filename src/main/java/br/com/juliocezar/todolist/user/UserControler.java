package br.com.juliocezar.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserControler {
    

    @Autowired
    private IUserRepository userRepository;

    @PostMapping
    public UserModel create(@RequestBody UserModel userModel) {
      var usercreated = this.userRepository.save(userModel);
      return usercreated;
    }
    }

