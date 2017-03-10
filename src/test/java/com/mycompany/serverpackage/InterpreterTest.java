/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.serverpackage;

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
public class InterpreterTest {
    
    public InterpreterTest() {
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
     * Test of getCommandType method, of class Interpreter.
     */
    @Test
    public void testGetCommandType() {
        System.out.println("getCommandType");
        String command = "BD:121;2e";
        Interpreter instance = new Interpreter();
        String expResult = "MOVE";
        String result = instance.getCommandType(command);
        assertEquals(expResult, result);
    }

    /**
     * Test of getX method, of class Interpreter.
     */
    @Test
    public void testGetX() {
        System.out.println("getX");
        String command = "WB:1;13";
        Interpreter instance = new Interpreter();
        int expResult = 1;
        int result = instance.getX(command);
        assertEquals(expResult, result);
    }

    /**
     * Test of getY method, of class Interpreter.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        String command = "BD:1;11";
        Interpreter instance = new Interpreter();
        int expResult = 11;
        int result = instance.getY(command);
        assertEquals(expResult, result);
    }
    
}
