package ru.jordan.food_storage.model.Error;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ApiError {
    private String errorCode;

    private String details;

    private UUID id;

    private OffsetDateTime datetime;

    private String request;
}
