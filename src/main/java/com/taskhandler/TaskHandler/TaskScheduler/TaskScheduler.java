package com.taskhandler.TaskHandler.TaskScheduler;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;
import com.taskhandler.TaskHandler.entity.Task;
import com.taskhandler.TaskHandler.repository.TaskRepository;

@Component
public class TaskScheduler {
    @Autowired
    private TaskRepository taskRepository;

    @SuppressWarnings("null")
    @Scheduled(fixedRate = 10000) // Check every 10 seconds
    public void scheduleTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepository.findAll();

        for (Task task : tasks) {
            if (task.getStatus().equals("scheduled")) {
                if (task.getType().equals("one-time") && task.getExecutionTime().isBefore(now.plusSeconds(10))) {
                    executeTask(task);
                } else if (task.getType().equals("recurring")) {
                    CronExpression cron = CronExpression.parse(task.getCronExpression());
                    LocalDateTime nextExecution = cron.next(task.getLastExecutionTime() == null ? now : task.getLastExecutionTime());

                    if (nextExecution.isBefore(now.plusSeconds(10))) {
                        executeTask(task);
                    }
                }
            }
        }
    }

    private void executeTask(Task task) {
        // Perform the task execution logic here
        task.setStatus("executed");
        task.setLastExecutionTime(LocalDateTime.now());
        taskRepository.save(task);

        // Log task execution or perform any other necessary action
    }
}

