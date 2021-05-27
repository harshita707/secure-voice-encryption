package com.application.chatroomsv2.AES;

import java.util.Base64;
import java.util.Scanner;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AES_Main {

    public static String toHexString(byte[] ba) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ba.length; i++)
            str.append(String.format("%x", ba[i]));
        return str.toString();
    }

    public static String fromHexString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }

    public static String encryptMessage(String message, String keyBase) {

        //fixing message length
        int k = message.length();
        if (k < 16) {
            for (int i = 0; i < (16 - k); i++) {
                message = message.concat("-");
            }
        }

        String key = getHashKey.get128bitKey(keyBase);
        String st = toHexString(message.getBytes());
        AES aes = new AES(st, key);
        String et = aes.encrypt();
        return et;

    }

    public static String decryptMessage(String message, String keyBase){
        String key = getHashKey.get128bitKey(keyBase);
        AES aes1 = new AES(message, key);
        String pt = aes1.decrypt();
        pt = fromHexString(pt);
        return pt.replaceAll("-*$", "");

    }

//    public static void main(String[] args) {
//
//        String key = "";//"7a52514c722b7349723872584b6a612f";// "01234567890123450123456789012444"7a52514c722b7349723872584b6a612f
//        String s;
//
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter String:");
//        s = sc.nextLine();
//        // System.out.println(s.length());
//        int k = s.length();
//        if (k < 16) {
//            for (int i = 0; i < (16 - k); i++) {
//                s = s.concat("-");
//            }
//        }
//        KeyGenerator Gen;
//        SecretKey secretKey;
//        String encodedKey;
//        try {
//            Gen = KeyGenerator.getInstance("AES");
//            Gen.init(128);
//            secretKey = Gen.generateKey();
//            encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//            key = toHexString(encodedKey.getBytes());
//            key = key.substring(0, 32);
//            System.out.println(key + " " + key.length());
//        } catch (Exception e) {
//        }
//
//        String st = toHexString(s.getBytes());
//        AES aes = new AES(st, key);
//        String et = aes.encrypt();
//        System.out.println("encrypted text:" + et);
//
//        AES aes1 = new AES(et, key);
//        String pt = aes1.decrypt();
//        pt = fromHexString(pt);
//        System.out.println("decrypted text:" + pt.replaceAll("-*$", ""));
//    }

}
