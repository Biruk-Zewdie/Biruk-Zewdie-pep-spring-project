package com.example.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidAccountException;
import com.example.exception.InvalidCredentialException;
import com.example.exception.InvalidMessageException;
import com.example.exception.MessageNotFoundException;
import com.example.exception.UnAuthorizedUserException;
import com.example.exception.duplicateAccountException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController (AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }
    
/****************************************************Handling create account request*****************************************************/
    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@RequestBody Account account){
        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.OK);

    }

    @ExceptionHandler(InvalidAccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    public @ResponseBody String handleInvalidAccountException(InvalidAccountException e){
        return e.getMessage();
    }

    @ExceptionHandler(duplicateAccountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)

    public @ResponseBody String handleDuplicateAccountException(duplicateAccountException e){
        return e.getMessage();
    }

/**************************************************Account authentication request**************************************************/

    @PostMapping("/login")
    public ResponseEntity <Account> authenticateAccount (@RequestBody Account account){

        Account authenticatedAccount = accountService.accountAuthentication(account);
        return new ResponseEntity<>(authenticatedAccount, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)

    public @ResponseBody String handleInvalidCredentialException (InvalidCredentialException e){
        return e.getMessage();
    }


/************************************************** Handle create a new message **************************************************/

    @PostMapping ("/messages")

    public ResponseEntity<Message> createMessage (@RequestBody Message message){
        Message createdMessage = messageService.createNewMessage (message);
        return new ResponseEntity<>(createdMessage, HttpStatus.OK);
    }

    @ExceptionHandler(UnAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    public @ResponseBody String handleunAuthorizedUserException(UnAuthorizedUserException e){
        return e.getMessage();
    }

    @ExceptionHandler (InvalidMessageException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)

    public @ResponseBody String handleInvalidMessageInputException (InvalidMessageException e){
        return e.getMessage();
    }

/******************************************* Handle Get request to retrieve all messages ******************************************/
    @GetMapping("/messages")
    
    public ResponseEntity <List<Message>> getAllMessages(){

        List <Message> allMessages = messageService.getAllMessages();
        return new ResponseEntity<>(allMessages, HttpStatus.OK);
    }

/************************************ Handle Get request to retrieve a message using message ID ***********************************/

    @GetMapping("/messages/{message_id}")

    public ResponseEntity <Message> getMessageById (@PathVariable Integer message_id) {
        Message message = messageService.getMessageById(message_id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

/******************** Handle delete request to remove a message from the database using message ID *******************************/

    @DeleteMapping ("/messages/{message_id}")

    public ResponseEntity <Integer> deleteMessageById (@PathVariable Integer message_id) {
        int deletedRow = messageService.deleteMessageById(message_id);

        if (deletedRow == 1){
            return new ResponseEntity<>(1, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.OK);
        }
        
    }

/******* Handle Patch request to update existing message with new message and save to the database using message ID **************/

    @PatchMapping("/messages/{message_id}")

    public ResponseEntity <Integer> updateMessage (@PathVariable Integer message_id, @RequestBody Message message) {
        int updatedRow = messageService.updateMessage(message_id, message);

        if (updatedRow == 1){
            return new ResponseEntity<>(1, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }

    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    public @ResponseBody String HandleMessageNotFoundException (MessageNotFoundException e){
        return e.getMessage();
    }

/****************************** Handle Get request to retrieve all messages posted by a certain user ***************************/
    @GetMapping("/accounts/{account_id}/messages")
    
    public ResponseEntity <List<Message>> getMessagesByAccount(@ PathVariable Integer account_id){

        Optional <List<Message>> messagesList = messageService.getAllMessagesByAccountId(account_id);

        if (messagesList.isPresent() && !messagesList.get().isEmpty()){
            return new ResponseEntity <> (messagesList.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
    }




}
