/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gamepackage;

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
public class StoneGroupTest {
    
    public StoneGroupTest() {
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
     * Test of addToGroup method, of class StoneGroup.
     */
    @Test
    public void testAddToGroup() {
        System.out.println("addToGroup");
        Game game = new Game(19);
        Stone stone = new Stone(0,0,1);
        StoneGroup instance = new StoneGroup(0,1,1,game);
        instance.addToGroup(stone);
        
        assertNotNull(instance.chain);
        assertEquals(instance.chain.size(), 2);
    }

    /**
     * Test of updateBreaths method, of class StoneGroup.
     */
    @Test
    public void testUpdateBreaths() {
        System.out.println("updateBreaths");
        Game game = new Game (13);
        game.put(0,0,1);
        game.put(0,1,1);
        StoneGroup instance = new StoneGroup(0,0,1,game);
        instance.addToGroup(new Stone(0,1,1));
        instance.updateBreaths();
        
        assertEquals(instance.getBreaths(), 3);
    }

    /**
     * Test of checkIfIn method, of class StoneGroup.
     */
    @Test
    public void testCheckIfIn() {
        System.out.println("checkIfIn");
        Game game = new Game(9);
        Stone stone = new Stone(0,0,0);
        StoneGroup instance = new StoneGroup(0,0,1,game);
        instance.addToGroup(stone);
        boolean expResult = true;
        boolean result = instance.checkIfIn(stone);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getBreaths method, of class StoneGroup.
     */
    @Test
    public void testGetBreaths() {
        System.out.println("getBreaths");
        Game game = new Game(9);
        StoneGroup instance = new StoneGroup(0,0,0,game);
        instance.updateBreaths();
        int expResult = 2;
        int result = instance.getBreaths();
        
        assertEquals(expResult, result);
    }
    
}
