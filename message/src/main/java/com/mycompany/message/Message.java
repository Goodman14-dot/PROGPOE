/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.message;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Message {

    //  Constants 
    private static final int    MAX_MESSAGE_LENGTH = 250;
    private static final int    MESSAGE_ID_LENGTH  = 10;
    private static final String JSON_FILE          = "stored_messages.json";

    //  Instance variables 
    private String messageID;
    private int    messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // ── Static runtime collections (dynamic, no hard-coding) ─────────────────
    private static final ArrayList<Message> sentMessages        = new ArrayList<>();
    private static final ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static final ArrayList<Message> storedMessages      = new ArrayList<>();
    private static int totalMessagesSent = 0;

    //  CONSTRUCTORS

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageID     = generateMessageID();
        this.messageHash   = createMessageHash();
    }

    public Message(String messageID, int messageNumber,
                   String recipient, String messageText) {
        this.messageID     = messageID;
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageHash   = createMessageHash();
    }

    //  ID GENERATION

    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < MESSAGE_ID_LENGTH; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }

    //  VALIDATION

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= MESSAGE_ID_LENGTH;
    }

    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+[0-9]{10,12}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain "
             + "an international code. Please correct the number and try again.";
    }

    public static boolean isValidRecipient(String cell) {
        return cell != null && cell.matches("^\\+[0-9]{10,12}$");
    }

    // Validates the length of a proposed message body.

    public static String checkMessageLength(String text) {
        if (text == null || text.length() <= MAX_MESSAGE_LENGTH) {
            return "Message ready to send.";
        }
        int excess = text.length() - MAX_MESSAGE_LENGTH;
        return "Message exceeds 250 characters by " + excess
             + "; please reduce the size.";
    }

    //  HASH CREATION

    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2) {
            return "";
        }

        String   twoDigits = messageID.substring(0, 2);
        String[] words     = messageText.trim().split("\\s+");

        // First alphanumeric word
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");

        // Last word that contains alphanumeric characters (skip punctuation-only words)
        String lastWord = "";
        for (int i = words.length - 1; i >= 0; i--) {
            String stripped = words[i].replaceAll("[^a-zA-Z0-9]", "");
            if (!stripped.isEmpty()) {
                lastWord = stripped;
                break;
            }
        }

        messageHash = (twoDigits + ":" + messageNumber + ":"
                    + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    //  SEND / STORE / DISREGARD

    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                disregardedMessages.add(this);
                return "Press 0 to delete the message.";
            case 3:
                storedMessages.add(this);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid choice. Please select 1, 2, or 3.";
        }
    }

    //  JSON STORAGE (PART 2)

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

    //  PART 2 – REPORTING

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

    public String displayMessageDetails() {
        return "Message ID   : " + messageID   + "\n"
             + "Message Hash : " + messageHash + "\n"
             + "Recipient    : " + recipient   + "\n"
             + "Message      : " + messageText;
    }

    //  PART 3 – ARRAY POPULATION
 
    public static String[] getSentMessagesArray() {
        String[] arr = new String[sentMessages.size()];
        for (int i = 0; i < sentMessages.size(); i++) {
            arr[i] = sentMessages.get(i).messageText;
        }
        return arr;
    }

    public static String[] getDisregardedMessagesArray() {
        String[] arr = new String[disregardedMessages.size()];
        for (int i = 0; i < disregardedMessages.size(); i++) {
            arr[i] = disregardedMessages.get(i).messageText;
        }
        return arr;
    }

    public static String[] getStoredMessagesArray() {
        String[] arr = new String[storedMessages.size()];
        for (int i = 0; i < storedMessages.size(); i++) {
            arr[i] = storedMessages.get(i).messageText;
        }
        return arr;
    }

    public static String[] getMessageHashArray() {
        ArrayList<Message> all = getAllMessages();
        String[] arr = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {
            arr[i] = all.get(i).messageHash;
        }
        return arr;
    }

    public static String[] getMessageIDArray() {
        ArrayList<Message> all = getAllMessages();
        String[] arr = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {
            arr[i] = all.get(i).messageID;
        }
        return arr;
    }

    public static String[] getStoredMessagesFromJSON() {
        ArrayList<String> list = new ArrayList<>();
        Gson gson = new Gson();
        try (BufferedReader reader = new BufferedReader(new FileReader(JSON_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    MessageData data = gson.fromJson(line, MessageData.class);
                    list.add(data.messageText);
                }
            }
        } catch (IOException e) {
            System.out.println("No stored messages file found.");
        }
        return list.toArray(new String[0]);
    }

    //  PART 3 – STORED MESSAGES SUB-MENU FEATURES

    public static String displayStoredMessages(String senderName) {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder sb = new StringBuilder("===== Stored Messages =====\n");
        for (Message m : storedMessages) {
            sb.append("Sender    : ").append(senderName).append("\n");
            sb.append("Recipient : ").append(m.recipient).append("\n");
            sb.append("---------------------------\n");
        }
        return sb.toString().trim();
    }

    public static String findLongestMessage() {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);

        if (all.isEmpty()) {
            return "No messages found.";
        }

        Message longest = all.get(0);
        for (Message m : all) {
            if (m.messageText.length() > longest.messageText.length()) {
                longest = m;
            }
        }
        return longest.messageText;
    }

    public static String searchByMessageID(String id) {
        for (Message m : getAllMessages()) {
            if (m.messageID.equals(id)) {
                return "Recipient : " + m.recipient + "\nMessage   : " + m.messageText;
            }
        }
        return "Message ID not found.";
    }

    public static String searchByRecipient(String recipientNumber) {
        ArrayList<Message> pool = new ArrayList<>();
        pool.addAll(sentMessages);
        pool.addAll(storedMessages);

        StringBuilder sb = new StringBuilder();
        for (Message m : pool) {
            if (m.recipient.equals(recipientNumber)) {
                sb.append(m.messageText).append("\n");
            }
        }
        return sb.length() > 0
            ? sb.toString().trim()
            : "No messages found for this recipient.";
    }

    public static String deleteMessageByHash(String hash) {
        // Search stored messages first
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).messageHash.equals(hash)) {
                String text = storedMessages.get(i).messageText;
                storedMessages.remove(i);
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        // Then search sent messages
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).messageHash.equals(hash)) {
                String text = sentMessages.get(i).messageText;
                sentMessages.remove(i);
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    public static String displayReport() {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);

        if (all.isEmpty()) {
            return "No messages to report.";
        }

        StringBuilder sb = new StringBuilder("========== Message Report ==========\n");
        for (Message m : all) {
            sb.append("Message Hash : ").append(m.messageHash).append("\n");
            sb.append("Recipient    : ").append(m.recipient).append("\n");
            sb.append("Message      : ").append(m.messageText).append("\n");
            sb.append("------------------------------------\n");
        }
        return sb.toString().trim();
    }

    //  PRIVATE HELPERS

    private static ArrayList<Message> getAllMessages() {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(disregardedMessages);
        all.addAll(storedMessages);
        return all;
    }

    //  TEST UTILITY

    public static void resetMessages() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        totalMessagesSent = 0;
    }

    //  GETTERS

    public String getMessageID()             { return messageID; }
    public String getRecipient()             { return recipient; }
    public String getMessageText()           { return messageText; }
    public String getMessageHash()           { return messageHash; }
    public static int getTotalMessagesSent() { return totalMessagesSent; }

    //  MAIN – STANDALONE DEMO

    public static void main(String[] args) {
        System.out.println("=========================");
        System.out.println("=     QuickChat – Messages     =");
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

    //  INNER DTO FOR JSON SERIALISATION

    static class MessageData {
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
