package com.user.project.User.Project.infrastructure.exception;

public enum IssueType {

    ERR_SYSTEM("/err-sytem", "System error"),
    INVALID_PARAMETER("/invalid-parameter", "Invalid parameter"),
    UNKNOW_MESSAGE("/unknow-message", "Unknown message"),
    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
    ERR_BUSINESS("/err-business", "Business error");

    private String title;
    private String uri;

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }

    IssueType(String path, String title) {
        this.uri = "https://userproject.com" + path;
        this.title = title;
    }

}
