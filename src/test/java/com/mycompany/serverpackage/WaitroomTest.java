/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.serverpackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author v
 */
public class WaitroomTest {
    
    public WaitroomTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addPlayer method, of class Waitroom.
     */
    @Test
    public void testAddPlayer() throws Exception {
        InetAddress addr = InetAddress.getByName(null);
        Socket socket1 = new Socket(addr, 8080);
        Socket socket2 = new Socket(addr, 8080);
        
        BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        PrintWriter out1 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream())), true);
        out1.println("START19");
        
        BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        PrintWriter out2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream())), true);
        out2.println("START19");
        
        assertEquals(in1.readLine(), "COLORBLACK");
        assertEquals(in2.readLine(), "COLORWHITE");
    }
    
}
