package com.bypark.userstory.dto;

import com.bypark.userstory.todolist.model.ToDoEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ToDoResponse {
    private LocalDate regDate;
    private String task;
    private String description;
    private String status;
    private String priority;
    private int order;

    public ToDoResponse(ToDoEntity toDoEntity){
        this.regDate = toDoEntity.getRegDate();
        this.task = toDoEntity.getTask();
        this.description = toDoEntity.getDes();
        this.status = toDoEntity.getStatus();
        this.priority = toDoEntity.getPriority();
        this.order = toDoEntity.getOrder();
    }
}
