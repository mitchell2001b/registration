package com.example.registration.Events;

import java.io.Serializable;
import java.util.Date;

public class UserDeletedEvent implements Serializable
{
    private Long Id;
    private String Email;

    private Date Createdat;

    public UserDeletedEvent(Long id, String email, Date createdat) {
        Id = id;
        Email = email;
        Createdat = createdat;
    }

    public UserDeletedEvent()
    {

    }

    public Long getId() {
        return Id;
    }

    public String getEmail() {
        return Email;
    }

    public Date getCreatedat() {
        return Createdat;
    }

    @Override
    public String toString() {
        return "UserDeletedEvent{" +
                "Id=" + Id +
                ", Email='" + Email + '\'' +
                ", Createdat=" + Createdat +
                '}';
    }
}