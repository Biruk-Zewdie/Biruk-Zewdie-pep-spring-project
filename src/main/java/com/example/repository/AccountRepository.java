package com.example.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;


public interface AccountRepository extends JpaRepository <Account, Integer> {

    //get account by username (method query)
    Optional <Account> findByUsername(String username);

    //get accounts by their account Id, used to check the user who post a message is an existing user or not.
    Optional <Account> findByAccountId (Integer accountId);

}
