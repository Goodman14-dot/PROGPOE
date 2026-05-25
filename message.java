/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.Message;

import java.util.Scanner;
import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
/**
 *Registration and Login System
 * @author Student
 */
public class message {

    public static void main(String[] args) {
            Scanner input = new Scanner(System.in);
                
            //Declare variable
            String username;
            String password;
            String phonenumber; 
            String loginuser;
            String loginpass;
                      
            //user input and loops
            //username        
            System.out.println("Please enter your Username.");
            username = input.nextLine();
            
            if(com.mycompany.Message.Login.checkUsername(username)) {
                System.out.println("Username successfully captured.");
            }else{
                System.out.println("Username is not correctly formatted; Please ensure that your" +
                    " username contains an underscore and is no more than 5 characters in length."); 
            }
            
            //password
            System.out.println("Please enter your Password:");
            password = input.nextLine();
            
            if (loginPassword(password)) {
                System.out.println("Password successfully captured.");
            }else{
                System.out.println("Password is not correctly formatted; Please ensure that the " + 
                    " password contains at least 8 characters, a capital letter, a number, and a special character.");
            }     
            
            //phonenumber
            System.out.println("Please enter your Phone number (e.g +27678886767).");
            phonenumber = input.nextLine();
            
            if (loginPhonenumber(phonenumber)) {
                System.out.println("Phone number successfully added.");
            }else{
                System.out.println("Phone number incorrectly formatted or does not cotain " + 
                    " international code.");
                        
    }
    System.out.println("===Login===");
    System.out.println("Enter your username: ");
    loginuser = input.nextLine();
    
    System.out.println("Enter your password: ");
    loginpass = input.nextLine();
    
    if(     com.mycompany.Message.Login.loginuser(username, password, loginuser, loginpass)) {
        System.out.println("welcome" + username + "good to have you again");
    }else{
        System.out.println("username or password is incorrect please try again");
        
    }    
    }

    private static boolean loginPassword(String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static boolean loginPhonenumber(String phonenumber) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    static class Login {
        
        public Login() {
        }
    }
}

 class Message {

    //Constants
    private static final int    MAX_MESSAGE_LENGTH = 250;
    private static final int    MESSAGE_ID_LENGTH  = 10;
    private static final String JSON_FILE          = "stored_messages.json";

    //Instance variables
    private String messageID;
    private int    messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    //Static collections 
    private static final ArrayList<Message> sentMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    //Constructors 
    //Production constructor – auto-generates a random 10-digit message ID.
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageID     = generateMessageID();
        this.messageHash   = createMessageHash();
    }

    //Test constructor – accepts an explicit message ID so unit tests can
    public Message(String messageID, int messageNumber,
                   String recipient, String messageText) {
        this.messageID     = messageID;
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageHash   = createMessageHash();
    }

    //ID generation 
    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < MESSAGE_ID_LENGTH; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }

    //Validation 
    //Checks that the message ID does not exceed 10 characters.
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= MESSAGE_ID_LENGTH;
    }


    //Checks that the recipient cell number is in valid international format.
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+[0-9]{10,12}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain "
             + "an international code. Please correct the number and try again.";
    }

    //Static helper used by QuickChat to validate a cell number before creating a Message object.
    public static boolean isValidRecipient(String cell) {
        return cell != null && cell.matches("^\\+[0-9]{10,12}$");
    }
 
    //Checks that a message body does not exceed 250 characters.   
    public static String checkMessageLength(String text) {
        if (text == null || text.length() <= MAX_MESSAGE_LENGTH) {
            return "Message ready to send.";
        }
        int excess = text.length() - MAX_MESSAGE_LENGTH;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    //Hash 
     //Creates and returns the message hash in the format
    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2) {
            return "";
        }
        String   twoDigits = messageID.substring(0, 2);
        String[] words     = messageText.trim().split("\\s+");
        String   firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String   lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        messageHash = (twoDigits + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    //Send / Store / Disregard
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid choice. Please select 1, 2, or 3.";
        }
    }

    //JSON storage 
    public void storeMessage() {
        Gson gson = new Gson();
        MessageData data = new MessageData(
            messageID, messageNumber, recipient, messageText, messageHash
        );
        try (FileWriter writer = new FileWriter(JSON_FILE, true)) {
            writer.write(gson.toJson(data) + "\n");
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    //Reporting 
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        StringBuilder sb = new StringBuilder("===== Sent Messages =====\n");
        for (Message m : sentMessages) {
            sb.append("Message ID   : ").append(m.messageID).append("\n");
            sb.append("Message Hash : ").append(m.messageHash).append("\n");
            sb.append("Recipient    : ").append(m.recipient).append("\n");
            sb.append("Message      : ").append(m.messageText).append("\n");
            sb.append("-------------------------\n");
        }
        return sb.toString();
    }

    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    //Returns a formatted string of this message's details/Order: Message ID → Hash → Recipient → Message
    public String displayMessageDetails() {
        return "Message ID   : " + messageID   + "\n"
             + "Message Hash : " + messageHash + "\n"
             + "Recipient    : " + recipient   + "\n"
             + "Message      : " + messageText;
    }

    //Test utility 
    public static void resetMessages() {
        sentMessages.clear();
        totalMessagesSent = 0;
    }

    //Getters
    public String getMessageID()                { return messageID; }
    public String getRecipient()                { return recipient; }
    public String getMessageText()              { return messageText; }
    public String getMessageHash()              { return messageHash; }
    public static int getTotalMessagesSent()    { return totalMessagesSent; }

    //Main
    //Runs a standalone demo of the Message class using the POE test data.
    public static void main(String[] args) {
        System.out.println("=========================");
        System.out.println("   QuickChat – Messages  ");
        System.out.println("=========================\n");

        // Message 1: valid recipient, sent
        Message msg1 = new Message(0, "+27718693002",
            "Hi Mike, can you join us for dinner tonight?");
        System.out.println("--- Message 1 ---");
        System.out.println("Message ID     : " + msg1.getMessageID());
        System.out.println("Message Hash   : " + msg1.getMessageHash());
        System.out.println("Recipient check: " + msg1.checkRecipientCell());
        System.out.println("Length check   : " + checkMessageLength(msg1.getMessageText()));
        System.out.println("Action         : " + msg1.sentMessage(1));
        System.out.println();

        // Message 2: invalid recipient, disregarded
        Message msg2 = new Message(1, "08575975889",
            "Hi Keegan, did you receive the payment?");
        System.out.println("--- Message 2 ---");
        System.out.println("Message ID     : " + msg2.getMessageID());
        System.out.println("Message Hash   : " + msg2.getMessageHash());
        System.out.println("Recipient check: " + msg2.checkRecipientCell());
        System.out.println("Length check   : " + checkMessageLength(msg2.getMessageText()));
        System.out.println("Action         : " + msg2.sentMessage(2));
        System.out.println();

        // Message 3: valid recipient, stored
        Message msg3 = new Message(2, "+27838884567",
            "Please send me the report by end of day.");
        System.out.println("--- Message 3 ---");
        System.out.println("Message ID     : " + msg3.getMessageID());
        System.out.println("Message Hash   : " + msg3.getMessageHash());
        System.out.println("Recipient check: " + msg3.checkRecipientCell());
        System.out.println("Length check   : " + checkMessageLength(msg3.getMessageText()));
        System.out.println("Action         : " + msg3.sentMessage(3));
        System.out.println();

        System.out.println("Total messages sent: " + getTotalMessagesSent());
        System.out.println();
        System.out.println(msg1.printMessages());
    }

    //Inner DTO for JSON serialisation
    private static class MessageData {
        String messageID;
        int    messageNumber;
        String recipient;
        String messageText;
        String messageHash;

        MessageData(String messageID, int messageNumber,
                    String recipient, String messageText, String messageHash) {
            this.messageID     = messageID;
            this.messageNumber = messageNumber;
            this.recipient     = recipient;
            this.messageText   = messageText;
            this.messageHash   = messageHash;
        }
    }
}