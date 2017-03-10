/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.serverpackage;

import java.io.*;
import java.net.*;

/** App main server class
 *
 * @author v
 */
public class Server {
    static final int PORT = 8080; 
    
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Server started...");
        
        try {
            Socket socket1 = null;
            Waitroom room = new Waitroom();
            while (true) {
                socket1 = s.accept();
                room.addPlayer(socket1);              
            }
        } finally {
            s.close();
        }
    }
    
}
