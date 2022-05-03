package com.bypark.userstory.todolist.model;

import com.bypark.userstory.db.MemoryDbEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ToDoEntity extends MemoryDbEntity implements Comparable<ToDoEntity>{

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate regDate;          // todo를 실핼할 날짜

    private String priority;           // 우선순위 (중요도 - S, A, B, C, D)
    private int order;                 // 우선순위 (순서 - 0, 1, 2 ...)
    private String task;               // 제목
    private String status;             // 상태 (진행중, 완료, 취소)

    @Nullable
    private String des;                // 설명

    @Override
    public int compareTo(ToDoEntity o) {
        if(o.order < order){
            return 1;
        } else if(o.order > order){
            return -1;
        } else {
            return 0;
        }
    }
}
