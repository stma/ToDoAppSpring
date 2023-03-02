package com.progmatic.appauthdemo.controller;

import com.progmatic.appauthdemo.model.ToDo;
import com.progmatic.appauthdemo.model.ToDoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
//@RequiredArgsConstructor
public class ToDoController {

//    @NonNull
//    @Autowired
    private ToDoRepository toDoRepository;

    public ToDoController(ToDoRepository repo) {
        toDoRepository = repo;
    }

    @GetMapping(path={"/", "", "/home", "/todos"})
    public String listTodos(Model m) {
        List<ToDo> todos = toDoRepository.findAll();
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
        BindingResult bind
    ) {
        if (bind.hasErrors()) {
            return "todo-form";
        }

        ToDo entity = new ToDo(todo.getDesc());
        entity.setDeadline(todo.getDeadline());
        entity.setDone(false);

        toDoRepository.save(entity);

        return "redirect:/home";
    }

}
