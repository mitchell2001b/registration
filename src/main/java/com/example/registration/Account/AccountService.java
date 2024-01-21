package com.example.registration.Account;

import com.example.registration.AccountRole.AccountRole;
import com.example.registration.Events.UserCreatedEvent;
import com.example.registration.dtos.AccountDto;
import com.example.registration.kafka.RegistrationProducer;
import com.example.registration.repositories.IAccountRepository;
import com.example.registration.repositories.IAccountRoleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
public class AccountService
{
    private IAccountRepository repoAccount;
    private IAccountRoleRepository repoAccountRole;

    private RegistrationProducer producer;


    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    public AccountService(IAccountRepository accountRepo, IAccountRoleRepository accountRoleRepository, RegistrationProducer registrationProducer) {
        this.repoAccount = accountRepo;
        this.repoAccountRole = accountRoleRepository;
        this.producer = registrationProducer;
    }
    public Account AddUser(AccountDto account)
    {
        try
        {
            AccountRole userRole = repoAccountRole.findRoleByName("user");
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(account.getPassWord());

            LOGGER.info(hashedPassword, " de hash");
            Account accountToInsert = new Account(account.getDateOfBirth(), hashedPassword, account.getEmail(), userRole);
            this.repoAccount.save(accountToInsert);
        }
        catch (Exception e)
        {
            return null;
        }


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

    public void CreateTestAdmin()
    {
        try
        {
            AccountRole userRole = repoAccountRole.findRoleByName("admin");
            AccountDto testAdmin = new AccountDto("admin", LocalDate.now(), "admin@gmail.com");
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(testAdmin.getPassWord());

            Account accountToInsert = new Account(testAdmin.getDateOfBirth(), hashedPassword, testAdmin.getEmail(), userRole);
            this.repoAccount.save(accountToInsert);

            Account account = repoAccount.findLastCreatedAccount();
            UserCreatedEvent event = new UserCreatedEvent(account.GetUserId(), account.GetPassWord(), account.GetDateOfBirth(), account.GetEmail(), account.GetRole().getId(), account.GetRole().getName(), LocalDate.now());
            producer.SendMessage(event);
        }
        catch (Exception e)
        {

        }
    }

    public Optional<Account> SelectAccountById(Long id)
    {
        return this.repoAccount.findById(id);
    }

}