package com.bypark.userstory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoModifyRequest {
    private Long index;

    @Nullable
    private String task;

    @Nullable
    private String des;

    @Nullable
    private String status;

    @Nullable
    private String priority;

    @Nullable
    private int order;

}
