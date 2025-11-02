package com.user.project.User.Project.domain.model;

public class UserProjects {

    private Long id;
    private User user;
    private String name;

    public UserProjects() {
    }

    public UserProjects(Long id, User user, String name) {
        this.id = id;
        this.user = user;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
