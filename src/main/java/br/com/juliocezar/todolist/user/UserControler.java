package br.com.juliocezar.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("user")
public class UserControler {

   @Autowired
   private IUserRepository userRepository;

   @PostMapping("")
   public ResponseEntity create(@RequestBody UserModel userModel){

      var userExist = this.userRepository.findByUsername(userModel.getUsername());

      if(userExist != null){
          return ResponseEntity.status(400).body("Usuário já existe");
      };

      var hashPassword = BCrypt.withDefaults()
                        .hashToString(12, userModel.getPassword()
                        .toCharArray());

      userModel.setPassword(hashPassword);

      var userCreated = this.userRepository.save(userModel);
      System.out.println(userCreated);
      return ResponseEntity.status(201).body(userCreated);

   }

}
