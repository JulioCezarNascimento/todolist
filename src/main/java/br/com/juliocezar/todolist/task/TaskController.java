package br.com.juliocezar.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocezar.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("tasks")
public class TaskController {

   @Autowired
   private ITaskRepository taskRepository;

   @PostMapping("")
   public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {

      taskModel.setIdUser((UUID) request.getAttribute("idUser"));

      var currentDate = LocalDateTime.now();

      if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
         return ResponseEntity.status(400).body("Data de inicio ou término da tarefa deve ser maior que a data atual");
      }

      if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
         return ResponseEntity.status(400).body("Data de término deve ser maior que a data de início.");
      }

      var taskCreated = this.taskRepository.save(taskModel);

      return ResponseEntity.status(200).body(taskCreated);
      

   }

   @GetMapping("")
   public List<TaskModel> getTasks(HttpServletRequest request) {

      var idUser = request.getAttribute("idUser");
      return this.taskRepository.findByIdUser((UUID) idUser);

   }

   @PutMapping("{id}")
   public ResponseEntity updateTasks(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {

      var task = this.taskRepository.findById(id).orElse(null);

      if(task == null){
         ResponseEntity.status(404).body("Tarefa não existe");
      }

      if(!task.getIdUser().equals(request.getAttribute("idUser"))){
         ResponseEntity.status(400).body("Usuário não tem permisão para alterar essa tarefa");
      }

     Utils.copyNonNullProperties(taskModel, task);
      var taskUpdated =  this.taskRepository.save(task);

      return ResponseEntity.ok().body(taskUpdated);

   }

}