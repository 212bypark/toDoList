package com.bypark.userstory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoRegisterRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate regDate;

    private String task;

    @Nullable
    private String des;
}
