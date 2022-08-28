package com.tempims.tempims;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

public class UserInteractions {
    public static User user;

    public static int checkLogin(String usernameInput, String passwordInput) throws IOException {

        String hashedPasswordDB = DBAccess.fetchPassword(usernameInput);
        String hashedPasswordInput = hashPassword(passwordInput);

        if (hashedPasswordDB == null) {
            return -1;
        }

        if (hashedPasswordDB.equals(hashedPasswordInput)) {
            //sql part to get permissions from USERS table
            user = new User(usernameInput, "111111");

            ProcessLogs.recordUserProcess(1, user.username);
            return 1;
        } else {
            return 0;
        }
    }

    public static int checkSignUp(String usernameInput, String passwordInput1, String passwordInput2) throws IOException {

        if (DBAccess.checkUsername(usernameInput) > 0) {
            return -2;//sql part to check username and  return -2 if username already exists
        }


        if (passwordInput1.equals("") || passwordInput2.equals("") || usernameInput.equals("")) { // these parts can not be empty if so returns -1
            return -1;
        }


        if (!passwordInput1.equals(passwordInput2)) { // in case two password inputs doesn't match if so return 0
            return 0;
        }

        ProcessLogs.recordUserProcess(0, usernameInput);
        DBAccess.insertUser(usernameInput, hashPassword(passwordInput1));// sql part to put new user to database and returns 1

        return 1;


    }

    private static String hashPassword(String password) { //password hashing method
        byte[] bytesOfPassword = password.getBytes(StandardCharsets.UTF_8);
        byte[] md5Digest;
        try {
            md5Digest = MessageDigest.getInstance("MD5").digest(bytesOfPassword);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return Base64.getEncoder().encodeToString(md5Digest);
    }

    public static ArrayList<User> getAllUsers() {
        ArrayList<User> usersList = new ArrayList<>();
        //sql part to get all users from USERS table
        //fill the arraylist with new User(username, permissions)
        return usersList;
    }

    public static void changePermissions(String username, String permissions) {
        //sql part to insert new permissions string to USERS db by using username
    }
    public static void changePassword(String username, String password){
        //sql part to change password
    }
    public static void deleteUser(String username){
        //sql part to delete user
    }


}
