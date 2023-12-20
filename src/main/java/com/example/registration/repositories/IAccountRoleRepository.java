package com.example.registration.repositories;

import com.example.registration.AccountRole.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IAccountRoleRepository extends JpaRepository<AccountRole, Integer>
{
    @Query(value = "SELECT * FROM accountrole where name = ?1", nativeQuery = true)
    public AccountRole findRoleByName(String name);
}