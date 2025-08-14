package com.aprendendo.login.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_role")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    private String name;

    public Role() {}

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum Values{
        ROLE_ADMIN(1L),
        ROLE_BASIC(2L);

        long id;

        Values(Long id){
            this.id = id;
        }

        public Long getRoleId(){
            return id;
        }

        Role toRole() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'toRole'");
        }
    }
}
