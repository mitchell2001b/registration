package com.example.registration.Account;

import com.example.registration.Events.UserCreatedEvent;
import com.example.registration.Events.UserDeletedEvent;
import com.example.registration.dtos.AccountDto;
import com.example.registration.dtos.AccountRoleDto;
import com.example.registration.dtos.AccountgdprDto;
import com.example.registration.kafka.RegistrationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping(path = "api/accounts")
public class AccountController {

    private final AccountService service;

    private final RegistrationProducer registrationProducer;

    @Value("${spring.kafka.topic.name}")
    private String TopicName;
    @Autowired
    public AccountController(AccountService accountService, RegistrationProducer givenRegistrationProducer)
    {
        this.service = accountService;
        this.registrationProducer = givenRegistrationProducer;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> CreateUser(@RequestBody AccountDto newAccount)
    {
        Account account = null;
        try
        {
            account = service.AddUser(newAccount);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{could not create user}");
        }

        UserCreatedEvent event = new UserCreatedEvent(account.GetUserId(), account.GetPassWord(), account.GetDateOfBirth(), account.GetEmail(), account.GetRole().getId(), account.GetRole().getName(), LocalDate.now());
        registrationProducer.SendMessage(event);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("{ \"id\": "+ account.GetUserId() + " }");
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<String> DeleteUser(@PathVariable("Id") Long Id)
    {
        try
        {
            Optional<Account> account = service.SelectAccountById(Id);
            Date date = new Date();
            if(account.isPresent())
            {
                UserDeletedEvent event = new UserDeletedEvent(account.get().GetUserId(), account.get().GetEmail(), date);
                registrationProducer.SendMessageUserDeletion(event);
                service.DeleteUser(account.get());
            }

        }
        catch (Exception e)
        {
            return  ResponseEntity.status((HttpStatus.CONFLICT))
                    .body("Could not delete user");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("User deletion succesfull");
    }
    @GetMapping("/myuserdata/{Id}")
    public ResponseEntity<String> RetrieveAllDataFromUser(@PathVariable Long Id)
    {
        AccountgdprDto dto = null;
        try
        {
            Optional<Account> account = service.SelectAccountById(Id);
            dto = new AccountgdprDto(account.get().GetPassWord(), account.get().GetDateOfBirth(), account.get().GetEmail());

        }
        catch (Exception ex)
        {
            return ResponseEntity.status((HttpStatus.CONFLICT))
                    .body("Could not retrieve user data");
        }

        return ResponseEntity.status((HttpStatus.OK))
                .body(dto.toString());

    }



    @GetMapping(value = "/testcall")
    public String Testcall()
    {
        AccountRoleDto roleDto = new AccountRoleDto(1L,"admin");
        AccountDto dto = new AccountDto(1L, "pass", LocalDate.now(), "email", roleDto);
        UserCreatedEvent event = new UserCreatedEvent(1L, "pass", LocalDate.now(), "email2gmail.com", roleDto.getId(), roleDto.getName(), LocalDate.now());
        registrationProducer.SendMessage(event);

        return "dit is een test bericht";
    }

    @GetMapping(value = "/testcall2")
    public String ClearTopic()
    {
        UserDeletedEvent userDeletedEvent = new UserDeletedEvent(1L, "test@gmail.com", new Date());
        registrationProducer.SendMessageUserDeletion(userDeletedEvent);
        //clearTopicService.clearTopic(TopicName);
        return "Topic cleared";
    }

    @GetMapping(value = "/dummyroles")
    public String SetupDummyRoles()
    {
        try
        {
            service.CreateRoles();
        }
        catch (Exception exception)
        {
            return "Creating dummy roles failed";
        }

        return "Dummy roles created";
    }
}