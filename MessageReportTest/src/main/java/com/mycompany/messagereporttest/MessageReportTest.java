/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.messagereporttest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageReportTest {

    //  Fixed IDs (deterministic hashes) 
    private static final String ID_MSG1 = "0000000001"; // hash → 00:0:DIDCAKE
    private static final String ID_MSG2 = "0000000002"; // hash → 00:1:WHERETIME
    private static final String ID_MSG3 = "0000000003"; // hash → 00:2:YOHOOOOGATE
    private static final String ID_MSG4 = "0838884567"; // hash → 08:3:ITTIME
    private static final String ID_MSG5 = "0000000005"; // hash → 00:4:OKYOU

    //  POE test messages 
    private Message msg1; // Sent
    private Message msg2; // Stored
    private Message msg3; // Disregarded
    private Message msg4; // Sent
    private Message msg5; // Stored

    @Before
    public void setUp() {
        // Reset all static state before every test
        Message.resetMessages();

        // Create all 5 messages using fixed IDs for predictable hashes
        msg1 = new Message(ID_MSG1, 0, "+27834557896",
                "Did you get the cake?");

        msg2 = new Message(ID_MSG2, 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");

        msg3 = new Message(ID_MSG3, 2, "+27834484567",
                "Yohoooo, I am at your gate.");

        msg4 = new Message(ID_MSG4, 3, "0838884567",
                "It is dinner time !");

        msg5 = new Message(ID_MSG5, 4, "+27838884567",
                "Ok, I am leaving without you.");

        // Route each message to its correct collection per POE test data
        msg1.sentMessage(1); // Sent
        msg2.sentMessage(3); // Stored
        msg3.sentMessage(2); // Disregarded
        msg4.sentMessage(1); // Sent
        msg5.sentMessage(3); // Stored
    }

    @After
    public void tearDown() {
        Message.resetMessages();
    }

    //  ARRAY POPULATION TESTS

    @Test
    public void testGetSentMessagesArray_ContainsBothSentMessages() {
        String[] sent = Message.getSentMessagesArray();
        assertEquals(2, sent.length);
        assertEquals("Did you get the cake?", sent[0]);
        assertEquals("It is dinner time !", sent[1]);
    }

    @Test
    public void testGetDisregardedMessagesArray_ContainsDisregardedMessage() {
        String[] disregarded = Message.getDisregardedMessagesArray();
        assertEquals(1, disregarded.length);
        assertEquals("Yohoooo, I am at your gate.", disregarded[0]);
    }

    @Test
    public void testGetStoredMessagesArray_ContainsBothStoredMessages() {
        String[] stored = Message.getStoredMessagesArray();
        assertEquals(2, stored.length);
        assertEquals(
            "Where are you? You are late! I have asked you to be on time.",
            stored[0]
        );
        assertEquals("Ok, I am leaving without you.", stored[1]);
    }

    @Test
    public void testGetMessageHashArray_LengthMatchesTotalMessages() {
        String[] hashes = Message.getMessageHashArray();
        assertEquals(5, hashes.length);
    }

    @Test
    public void testGetMessageIDArray_LengthMatchesTotalMessages() {
        String[] ids = Message.getMessageIDArray();
        assertEquals(5, ids.length);
    }

    //  FIND LONGEST MESSAGE

    @Test
    public void testFindLongestMessage_ReturnsMsg2() {
        String longest = Message.findLongestMessage();
        assertEquals(
            "Where are you? You are late! I have asked you to be on time.",
            longest
        );
    }

    //  SEARCH BY MESSAGE ID

    @Test
    public void testSearchByMessageID_Msg4_ReturnsCorrectMessage() {
        String result = Message.searchByMessageID("0838884567");
        assertTrue(
            "Expected result to contain 'It is dinner time !'",
            result.contains("It is dinner time !")
        );
    }

    @Test
    public void testSearchByMessageID_Msg4_ReturnsCorrectRecipient() {
        String result = Message.searchByMessageID("0838884567");
        assertTrue(
            "Expected result to contain recipient '0838884567'",
            result.contains("0838884567")
        );
    }

    @Test
    public void testSearchByMessageID_NotFound_ReturnsNotFoundMessage() {
        assertEquals("Message ID not found.", Message.searchByMessageID("9999999999"));
    }

    //  SEARCH BY RECIPIENT

    @Test
    public void testSearchByRecipient_SharedRecipient_ReturnsBothMessages() {
        String result = Message.searchByRecipient("+27838884567");
        assertTrue(
            "Expected result to contain msg2 text",
            result.contains("Where are you? You are late! I have asked you to be on time.")
        );
        assertTrue(
            "Expected result to contain msg5 text",
            result.contains("Ok, I am leaving without you.")
        );
    }

    @Test
    public void testSearchByRecipient_NotFound_ReturnsNotFoundMessage() {
        assertEquals(
            "No messages found for this recipient.",
            Message.searchByRecipient("+27000000000")
        );
    }

    //  DELETE BY MESSAGE HASH

    @Test
    public void testDeleteMessageByHash_Msg2_ReturnsConfirmation() {
        String hash   = msg2.getMessageHash();
        String result = Message.deleteMessageByHash(hash);
        assertTrue(
            "Expected confirmation to contain the deleted message text",
            result.contains(
                "Where are you? You are late! I have asked you to be on time."
            )
        );
        assertTrue(
            "Expected confirmation to contain 'successfully deleted'",
            result.contains("successfully deleted")
        );
    }

    @Test
    public void testDeleteMessageByHash_Msg2_IsRemovedFromStoredArray() {
        Message.deleteMessageByHash(msg2.getMessageHash());
        String[] stored = Message.getStoredMessagesArray();
        for (String s : stored) {
            assertFalse(
                "Deleted message should not be in stored array",
                s.equals("Where are you? You are late! I have asked you to be on time.")
            );
        }
    }

    @Test
    public void testDeleteMessageByHash_NotFound_ReturnsNotFoundMessage() {
        assertEquals("Message hash not found.", Message.deleteMessageByHash("XX:9:FAKEHASH"));
    }

    //  DISPLAY REPORT

    @Test
    public void testDisplayReport_ContainsMsg1Details() {
        String report = Message.displayReport();
        assertTrue("Report should contain msg1 hash",      report.contains(msg1.getMessageHash()));
        assertTrue("Report should contain msg1 recipient", report.contains("+27834557896"));
        assertTrue("Report should contain msg1 message",   report.contains("Did you get the cake?"));
    }

    @Test
    public void testDisplayReport_ContainsMsg4Details() {
        String report = Message.displayReport();
        assertTrue("Report should contain msg4 hash",      report.contains(msg4.getMessageHash()));
        assertTrue("Report should contain msg4 recipient", report.contains("0838884567"));
        assertTrue("Report should contain msg4 message",   report.contains("It is dinner time !"));
    }

    @Test
    public void testDisplayReport_ContainsStoredMessages() {
        String report = Message.displayReport();
        assertTrue(
            "Report should contain msg2 text",
            report.contains("Where are you? You are late! I have asked you to be on time.")
        );
        assertTrue(
            "Report should contain msg5 text",
            report.contains("Ok, I am leaving without you.")
        );
    }

    //  HASH CORRECTNESS

    @Test
    public void testMessageHashes_AllFiveMessages_CorrectFormat() {
        Message[] messages = { msg1, msg2, msg3, msg4, msg5 };
        String[]  expected = {
            "00:0:DIDCAKE",
            "00:1:WHERETIME",
            "00:2:YOHOOOOGATE",
            "08:3:ITTIME",
            "00:4:OKYOU"
        };

        for (int i = 0; i < messages.length; i++) {
            assertEquals(
                "Hash mismatch for message " + (i + 1),
                expected[i],
                messages[i].getMessageHash()
            );
        }
    }

    //  TOTAL MESSAGES SENT

    @Test
    public void testReturnTotalMessages_OnlySentMessagesCount() {
        assertEquals(2, Message.getTotalMessagesSent());
    }

    private static class Message {

        private static void resetMessages() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static String[] getSentMessagesArray() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static String[] getDisregardedMessagesArray() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static String[] getStoredMessagesArray() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static String[] getMessageHashArray() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static String[] getMessageIDArray() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static String findLongestMessage() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static String searchByMessageID(String string) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static String searchByRecipient(String string) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static String deleteMessageByHash(String hash) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static String displayReport() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static Object getTotalMessagesSent() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        public Message() {
        }

        private Message(String ID_MSG1, int i, String string, String did_you_get_the_cake) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private void sentMessage(int i) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private String getMessageHash() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }
}
