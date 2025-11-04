package com.user.project.infrastructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_user_external_project")
public class UserProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "name", length = 120, nullable = false)
    private String name;

    public UserProjectEntity(Long id, UserEntity userEntity, String name) {
        this.id = id;
        this.user = userEntity;
        this.name = name;
    }

    public UserProjectEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
