/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poe_chatappassignment;
 import com.mycompany.poe_chatappassignment.POE_ChatAppAssignment.Login;

    class login {
        
        //conditions
        public static boolean cheakUsername(String username) {
            return username.contains("_")&& username.length()<=5;
        }
        
        static boolean checkUsername(String username) {
            throw new UnsupportedOperationException("Not supported yet.");
            
        }
        
        static boolean checkPhonenumber(String phonenumber) {
            throw new UnsupportedOperationException("Not supported yet.");
            
        }
        static boolean checkPassword(String password) {
            String pattern = "(?=.*[A-Z])"
                           +"(?=.*//d)"
                           +"(?=.*[^a-z0-9])"
                           +".{8,}";
            return password.matches(pattern);
        }    
        
        static boolean loginuser(String storedusername, String storedpassword, String loginuser, String loginpass) {
        return loginuser.equals(storedusername) && loginpass.equals(storedpassword);
        
        }
        
        }


    

