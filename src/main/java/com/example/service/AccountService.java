package com.example.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.InvalidAccountException;
import com.example.exception.InvalidCredentialException;
import com.example.exception.duplicateAccountException;
import com.example.repository.AccountRepository;

@Service 
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService (AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
/****************************************************Create a new Account*****************************************************/
    
    public Account createAccount (Account account) {

        /*to successfully create an account the user input should be
            1. the password should be at least 4 characters 
            2. the username shouldn't be blank 
            3. the user should be new/ no duplicate usernames are allowed.
        */ 
        if (account.getPassword().length() < 4 || account.getUsername().isBlank()){
            throw new InvalidAccountException("The password must be at least 4 characters long.");
        }

        if (account.getUsername() == null || account.getUsername().isBlank()){
            throw new InvalidAccountException ("Username can not be null or blank.");
        }

        if (accountRepository.findByUsername(account.getUsername()).isPresent()){
            throw new duplicateAccountException("An account with the specified username already exists.");
        }
    

        return accountRepository.save(account);
    }

/************************************************* Get all Accounts in the database *********************************************/

    public List <Account> getAllAccount (){
        return accountRepository.findAll();
    }

/*************************************************** Credential Authentication **************************************************/

    public Account accountAuthentication (Account account){

        Optional <Account> existedAccount = accountRepository.findByUsername(account.getUsername()); 

        if (!existedAccount.isPresent()){
                
            throw new InvalidCredentialException ("You have entered Invalid username or password.");
        }

        Account accountInDatabase = existedAccount.get();

        if (!accountInDatabase.getPassword().equals(account.getPassword())){

            throw new InvalidCredentialException ("You have entered Invalid username or password.");
        }

        return accountInDatabase;
    }

}
