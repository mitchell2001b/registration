package com.example.registration.repositories;

import com.example.registration.Account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IAccountRepository extends JpaRepository<Account, Long>
{
    @Query(value = "SELECT * FROM `account` ORDER BY `id` DESC LIMIT 1", nativeQuery = true)
    public Account findLastCreatedAccount();
}
