package com.example.registration.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class AccountgdprDto implements Serializable
{
    @JsonProperty("password")
    private String PassWord;
    @JsonProperty("dateofbirth")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate DateOfBirth;
    @JsonProperty("email")
    private String Email;

    public AccountgdprDto()
    {

    }

    public AccountgdprDto(String passWord, LocalDate dateOfBirth, String email)
    {
        PassWord = passWord;
        DateOfBirth = dateOfBirth;
        Email = email;
    }

    public String getPassWord() {
        return PassWord;
    }

    public LocalDate getDateOfBirth() {
        return DateOfBirth;
    }

    public String getEmail() {
        return Email;
    }

    @Override
    public String toString() {
        return "AccountgdprDto{" +
                "PassWord='" + PassWord + '\'' +
                ", DateOfBirth=" + DateOfBirth +
                ", Email='" + Email + '\'' +
                '}';
    }
}