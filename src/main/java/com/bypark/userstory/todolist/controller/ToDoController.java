package com.bypark.userstory.todolist.controller;

import com.bypark.userstory.dto.ToDoModifyRequest;
import com.bypark.userstory.dto.ToDoRegisterRequest;
import com.bypark.userstory.dto.ToDoResponse;
import com.bypark.userstory.todolist.model.ToDoEntity;
import com.bypark.userstory.todolist.services.ToDoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@AllArgsConstructor
@RestController
@RequestMapping("/")
@Slf4j
public class ToDoController {

    private final ToDoService toDoService;

    // API2: todo를 신규 생성함
    @PostMapping
    public ResponseEntity<ToDoResponse> create(@RequestBody ToDoRegisterRequest request) {

        if (ObjectUtils.isEmpty(request.getRegDate())) {
            return ResponseEntity.badRequest().build();
        }

        if (ObjectUtils.isEmpty(request.getTask())) {
            return ResponseEntity.badRequest().build();
        }

        ToDoEntity result = this.toDoService.add(request);
        log.info("TODO CREATED: "+result.getIndex());

        return ResponseEntity.ok(new ToDoResponse(result));
    }

    // API4: todo를 삭제함
    @DeleteMapping("{id}")
    public ResponseEntity<ToDoResponse> delete(@PathVariable Long id) {
        // 해당 id delete
        if(toDoService.searchById(id)) {
            return ResponseEntity.badRequest().build();
        } else {
            toDoService.delete(id);
            log.info("TODO DELETED: "+id);
            return ResponseEntity.status(201).build();
        }
    }

    // API1: 특정 날짜의 모든 todo들을 우선순위로 정렬하여 반환함
    @GetMapping("{date}")
    public ResponseEntity<?> getAllList(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        log.info("TODO READ: "+date);
        return ResponseEntity.ok().body(toDoService.findAll(date));
    }

    // API3: 특정 todo를 변경함 (task, description, 우선순위(priority, order), 상태)
    @PatchMapping("/patch")
    public ResponseEntity<ToDoResponse> update(@RequestBody ToDoModifyRequest request) {
        if(toDoService.searchById(request.getIndex())) {
            return ResponseEntity.badRequest().build();
        } else {
            log.info("TODO MODIFIED: "+request.getIndex());
            ToDoEntity result = this.toDoService.modify(request);
            return ResponseEntity.ok(new ToDoResponse(result));
        }
    }
}
