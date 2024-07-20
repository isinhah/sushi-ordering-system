package com.sushi.api.exceptions.handler;

import java.time.LocalDateTime;

public class ValidationExceptionDetails extends ExceptionResponse {
    private final String fields;
    private final String fieldsMessage;

    public ValidationExceptionDetails(String title, int status, String details, String developerMessage, LocalDateTime timestamp, String fields, String fieldsMessage) {
        super(title, status, details, developerMessage, timestamp);
        this.fields = fields;
        this.fieldsMessage = fieldsMessage;
    }

    public String getFields() {
        return fields;
    }

    public String getFieldsMessage() {
        return fieldsMessage;
    }
}
