package com.example.registration.Account;

import com.example.registration.AccountRole.AccountRole;
import com.example.registration.dtos.AccountDto;
import com.example.registration.repositories.IAccountRepository;
import com.example.registration.repositories.IAccountRoleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

@Service
public class AccountService
{
    private IAccountRepository repoAccount;
    private IAccountRoleRepository repoAccountRole;


    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    public AccountService(IAccountRepository accountRepo, IAccountRoleRepository accountRoleRepository) {
        this.repoAccount = accountRepo;
        this.repoAccountRole = accountRoleRepository;
    }
    public Account AddUser(AccountDto account)
    {
        AccountRole userRole = repoAccountRole.findRoleByName("user");
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(account.getPassWord());

        LOGGER.info(hashedPassword, " de hash");
        Account accountToInsert = new Account(account.getDateOfBirth(), hashedPassword, account.getEmail(), userRole);
        this.repoAccount.save(accountToInsert);

        return repoAccount.findLastCreatedAccount();
    }

    @Transactional
    public void DeleteUser(Account accountToDelete)
    {
        if(accountToDelete != null)
        {
            this.repoAccount.delete(accountToDelete);
        }

    }

    public void CreateRoles()
    {
        AccountRole userRole = new AccountRole(1L, "user");
        AccountRole adminRole = new AccountRole(2L, "admin");
        try
        {
            this.repoAccountRole.save(userRole);
            this.repoAccountRole.save(adminRole);
        }
        catch (Exception exception)
        {

        }

    }

    public Optional<Account> SelectAccountById(Long id)
    {
        return this.repoAccount.findById(id);
    }

}