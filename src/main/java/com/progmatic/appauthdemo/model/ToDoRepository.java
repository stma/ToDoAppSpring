package com.progmatic.appauthdemo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    @Transactional
    @Modifying
    @Query("update ToDo t set t.done = ?1 where t.id = ?2")
    int updateDoneById(Boolean done, Long id);
}
