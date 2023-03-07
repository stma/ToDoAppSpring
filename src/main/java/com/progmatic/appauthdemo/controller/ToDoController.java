package com.progmatic.appauthdemo.controller;

import com.progmatic.appauthdemo.auth.AppUserService;
import com.progmatic.appauthdemo.model.AppUser;
import com.progmatic.appauthdemo.model.ToDo;
import com.progmatic.appauthdemo.model.ToDoRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
//@RequiredArgsConstructor
public class ToDoController {

//    @NonNull
//    @Autowired
    private ToDoRepository toDoRepository;
    private AppUserService userDetailsService;

    public ToDoController(
        ToDoRepository repo,
        AppUserService uds
    ) {
        userDetailsService = uds;
        toDoRepository = repo;
    }

    @GetMapping(path={"/", "/home"})
    public String homePage() {
        return "home";
    }

    @GetMapping(path="/todos")
    public String listTodos(
        Model m,
        Principal principal
    ) {
        AppUser owner = (AppUser) userDetailsService.loadUserByUsername(principal.getName());
        List<ToDo> todos = toDoRepository.findByOwner(owner);
        m.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/todo")
    public String addTodo(
            Model m
    ) {
        m.addAttribute("newtodo", new ToDoForm());
        return "todo-form";
    }

    @PostMapping("/todo")
    public String saveTodo(
        @ModelAttribute("newtodo")
        @Validated
        ToDoForm todo,
        BindingResult bind,
        Principal user
    ) {
        if (bind.hasErrors()) {
            return "todo-form";
        }

        AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(user.getName());

        ToDo entity = new ToDo(todo.getDesc());
        entity.setDeadline(todo.getDeadline());
        entity.setDone(false);
        entity.setOwner(appUser);

        toDoRepository.save(entity);

        return "redirect:/home";
    }

    @PostMapping("/todo-delete")
    public String todoDelete(
            @RequestParam("todo_id") Long id,
            Principal user
    ) {
        AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(user.getName());
        toDoRepository.deleteByIdAndOwner(id, appUser);
        return "redirect:/home";
    }

    @PostMapping("/todo-done")
    public String todoDone(
            @RequestParam("todo_id") Long id,
            @RequestParam(value = "done", required = false) Boolean done,
            Principal user
    ) {
        done = done != null;
        AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(user.getName());
        toDoRepository.updateDoneByIdAndOwner(done, id, appUser);
        return "redirect:/home";
    }
}
