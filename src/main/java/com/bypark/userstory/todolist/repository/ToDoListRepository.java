package com.bypark.userstory.todolist.repository;

import com.bypark.userstory.db.MemoryDbRepositoryAbstract;
import com.bypark.userstory.todolist.model.ToDoEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ToDoListRepository extends MemoryDbRepositoryAbstract<ToDoEntity> {

}
