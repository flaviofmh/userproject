package com.user.project.User.Project.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.List;

public class Issue {
    private final Integer status;
    private LocalDateTime timestamp;
    private final String type;
    private final String title;
    private final String detail;
    private final String userMessage;
    private final List<Field> fields;

    private Issue(Builder b) {
        this.status = b.status;
        this.timestamp = b.timestamp;
        this.type = b.type;
        this.title = b.title;
        this.detail = b.detail;
        this.userMessage = b.userMessage;
        this.fields = b.fields;
    }

    public Integer getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getDetail() { return detail; }
    public String getUserMessage() { return userMessage; }
    public List<Field> getFields() { return fields; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Integer status;
        private LocalDateTime timestamp;
        private String type;
        private String title;
        private String detail;
        private String userMessage;
        private List<Field> fields;

        public Builder status(Integer status) { this.status = status; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder detail(String detail) { this.detail = detail; return this; }
        public Builder userMessage(String userMessage) { this.userMessage = userMessage; return this; }
        public Builder fields(List<Field> fields) { this.fields = fields; return this; }

        public Issue build() { return new Issue(this); }
    }

    public static class Field {
        private final String name;
        private final String userMessage;

        private Field(Builder b) {
            this.name = b.name;
            this.userMessage = b.userMessage;
        }

        public String getName() { return name; }
        public String getUserMessage() { return userMessage; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private String name;
            private String userMessage;

            public Builder name(String name) { this.name = name; return this; }
            public Builder userMessage(String userMessage) { this.userMessage = userMessage; return this; }

            public Field build() { return new Field(this); }
        }
    }
}
