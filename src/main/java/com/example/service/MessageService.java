package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.InvalidMessageException;
import com.example.exception.MessageNotFoundException;
import com.example.exception.UnAuthorizedUserException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService (MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createNewMessage (Message message){

        /* The following condition must be fulfilled in order to the message successfully posted
            1. The message text must not be blank or null.
            2. The message text character length should be less than 255 characters long
            3. The message can't be posted with unregistered user accounts. Only existing accounts can post a message.

            * if the above three conditions are fulfilled the will be posted, if not throw an exception to be handled by the user.
        */

        if (message.getMessageText() == null || message.getMessageText().isBlank()){
            throw new InvalidMessageException ("The message shouldn't be null or blank.");
        }

        if (message.getMessageText().length() >= 255){
            throw new InvalidMessageException("The length of the message text should be less than 255 characters.");
        }

        if (!accountRepository.findByAccountId(message.getPostedBy()).isPresent()){
            throw new UnAuthorizedUserException ("Only existing users can post a message.");
        }


        return messageRepository.save(message);

    }
/***************************************** Retrieve all messages from the database **************************************/
    public List <Message> getAllMessages (){
        return messageRepository.findAll();
    } 

/******************************************* Retrieve message using Message ID ********************************************/

    public Message getMessageById (Integer messageId){

        Optional <Message> optionalMessage = messageRepository.findByMessageId(messageId);
        if (optionalMessage.isPresent()){
            return optionalMessage.get();
        }
        return null;
    }
/******************************************* Remove message if the message exists ********************************************/

    public Integer deleteMessageById (Integer messageId){

        Optional <Message> optionalMessage = messageRepository.findByMessageId(messageId);

        if (optionalMessage.isPresent()){
            messageRepository.deleteById(messageId);
            return 1;    // 1 row deleted
        }
        return 0;
        
    }

/******************************************* Update message if the message exists ********************************************/

    public Integer updateMessage (Integer messageId, Message updatedMessage){

        /* In order to successfully update a message, the follownig conditions must be fulfilled 
            1. The new message text must not be null or blank.
            2. The length of the message text must not be greater than 255 characters.
            3. The message to be updated must already exist in our database.

            * If the above conditions are fulfilled, the message will be succussfully updated and the 
              the response body will contain (1) which is the number of rows updated.
            * If it will throw an exception.
        */
            if (updatedMessage.getMessageText() == null || updatedMessage.getMessageText().isBlank()){
                throw new InvalidMessageException("The message shouldn't be null or blank.");
            }

            if (updatedMessage.getMessageText().length() >= 255){
                throw new InvalidMessageException("The length of the message text should be less than 255 characters.");
            }

            if (!messageRepository.findByMessageId(messageId).isPresent()){
                throw new MessageNotFoundException("The message that you inquired is not found in our database.");
            }

            Optional <Message> optionalMessage = messageRepository.findByMessageId(messageId);
            
            if (optionalMessage.isPresent()){

                Message existingMessage = optionalMessage.get();
                existingMessage.setMessageText(updatedMessage.getMessageText());
                existingMessage.setPostedBy(updatedMessage.getPostedBy());
                existingMessage.setTimePostedEpoch(updatedMessage.getTimePostedEpoch());
                messageRepository.save(existingMessage);
                return 1;
            }
            return 0;
    }

/********************* Retrieve all messages from a specific user account if the account already exists ********************/

    public Optional <List<Message>> getAllMessagesByAccountId (Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

}
