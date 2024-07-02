package com.taskhandler.TaskHandler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskhandler.TaskHandler.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
}
