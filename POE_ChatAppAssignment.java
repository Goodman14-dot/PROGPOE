/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poe_chatappassignment;

import java.util.Scanner;
import com.mycompany.poe_chatappassignment.POE_ChatAppAssignment.Login;
/**
 *Registration and Login System
 * @author Student
 */
public class POE_ChatAppAssignment {


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
            
            if(login.checkUsername(username)) {
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
    
    if(login.loginuser(username, password, loginuser, loginpass)) {
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
    