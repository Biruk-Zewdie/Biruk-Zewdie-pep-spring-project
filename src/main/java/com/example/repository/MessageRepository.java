package com.example.repository;

import java.util.*;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Message;

public interface MessageRepository extends JpaRepository <Message, Integer> {

 
    //retrieve all the messages as a list from the database 
    List <Message> findAll ();

    /*
    retrieve a message by using message Id
    the message may not retrieved if the message Id is not exist. 
    */
    Optional <Message> findByMessageId (Integer messageId);

    //remove message from the database if the message already exist in the database.
    void deleteById (Integer messageId);

    //retrieve a list of messages which are posted by a specific account.
    Optional <List<Message>> findByPostedBy (Integer accountId);


    
}
