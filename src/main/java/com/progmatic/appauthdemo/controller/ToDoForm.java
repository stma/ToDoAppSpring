package com.progmatic.appauthdemo.controller;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ToDoForm {

    @Size(min = 5, max = 50)
    private String desc;

    @Future
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private LocalDate deadline;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
