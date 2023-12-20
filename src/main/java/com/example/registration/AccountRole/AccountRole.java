package com.example.registration.AccountRole;

import jakarta.persistence.*;

@Entity
@Table(name = "accountrole")
public class AccountRole
{
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique=true)
    private String Name;

    public AccountRole()
    {

    }

    public  AccountRole(Long id, String name)
    {
        Id = id;
        Name = name;
    }
    public AccountRole(String name)
    {
        Name = name;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }
}