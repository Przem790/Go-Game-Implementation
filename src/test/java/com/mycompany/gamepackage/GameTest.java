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
public class GameTest {
    
    public GameTest() {
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
     * Test of checkRules method, of class Game.
     */
    @Test
    public void testCheckRules() {
        System.out.println("checkRules");
        int x = 0;
        int y = 0;
        int player = 0;
        Game instance = new Game(9);
        instance.put(0,0,1);
        boolean expResult = false;
        boolean result = instance.checkRules(x, y, player);
        
        assertEquals(expResult, result);
    }
        @Test
    public void testRuleSuicide() {
        Game game = new Game(19);
        game.put(1, 2, 0);
        game.put(2, 1, 0);
        game.put(2, 3, 0);
        game.put(3, 1, 0);
        game.put(3, 3, 0);
        game.put(4, 2, 0);
        game.put(3, 2, 1);
        
        assertEquals(false, game.checkRules(2, 2, 1));
        if (game.checkRules(2, 2, 1) == true)
            game.put(2, 2, 1);
        assertEquals(true, game.checkIfExists(3, 2));
        assertEquals(false, game.checkIfExists(2, 2));
    }

    /**
     * Test of ruleInfo method, of class Game.
     */
    @Test
    public void testRuleInfo() {
        System.out.println("ruleInfo");
        Game instance = new Game(9);
        String expResult = "OK";
        String result = instance.ruleInfo();
        assertEquals(expResult, result);
    }

    /**
     * Test of put method, of class Game.
     */
    @Test
    public void testPut() {
        System.out.println("put");
        int x = 0;
        int y = 0;
        int player = 0;
        Game instance = new Game(9);
        instance.put(x, y, player);
        
        assertEquals(instance.checkIfExists(x, y), true);
    }

    /**
     * Test of ruleKo method, of class Game.
     */
    @Test
    public void testRuleKo() {
        System.out.println("ruleKo");
        Game game = new Game(9);
        game.put(0, 0, 0);
        game.put(1, 1, 0);
        game.put(2, 1, 1);
        game.put(3, 0, 1);
        game.put(2, 0, 0);
        game.put(1, 0, 1);
        assertEquals(false, game.ruleKo(2, 0, 0));
        assertEquals(true, game.ruleKo(5, 5, 0));
    }

    /**
     * Test of countBreaths method, of class Game.
     */
    @Test
    public void testCountBreaths() {
        System.out.println("countBreaths");
        int x = 0;
        int y = 0;
        Game instance = new Game(9);
        int expResult = 2;
        int result = instance.countBreaths(x, y);
        assertEquals(expResult, result);
    }

    /**
     * Test of getBlackInmates method, of class Game.
     */
    @Test
    public void testGetBlackInmates() {
        System.out.println("getBlackInmates");
        Game game = new Game(9);
        game.put(0, 0, 0);
        game.put(1, 0, 1);
        game.put(0, 1, 1);
        int expResult = 1;
        int result = game.getBlackInmates();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWhiteInmates method, of class Game.
     */
    @Test
    public void testGetWhiteInmates() {
        System.out.println("getWhiteInmates");
        Game game = new Game(9);
        game.put(0, 0, 1);
        game.put(1, 0, 0);
        game.put(0, 1, 0);
        int expResult = 1;
        int result = game.getWhiteInmates();
        assertEquals(expResult, result);
    }

    /**
     * Test of checkIfExists method, of class Game.
     */
    @Test
    public void testCheckIfExists() {
        System.out.println("checkIfExists");
        int x = 0;
        int y = 0;
        Game instance = new Game(9);
        boolean expResult = false;
        boolean result = instance.checkIfExists(x, y);
        assertEquals(expResult, result);;
    }

    /**
     * Test of resetWinBoard method, of class Game.
     */
    @Test
    public void testResetWinBoard() {
        System.out.println("resetWinBoard");
        Game instance = new Game(9);
        instance.winBoardState[0][0] = 1;
        instance.resetWinBoard();
        
        assertEquals(instance.winBoardState[0][0], -1);
    }

    /**
     * Test of putWinBoard method, of class Game.
     */
    @Test
    public void testPutWinBoard() {
        System.out.println("putWinBoard");
        int x = 0;
        int y = 0;
        int player = 0;
        Game instance = new Game(9);
        instance.putWinBoard(x, y, player);
        
        assertEquals(instance.winBoardState[x][y], player);
    }

    /**
     * Test of countWinBreaths method, of class Game.
     */
    @Test
    public void testCountWinBreaths() {
        System.out.println("countWinBreaths");
        int x = 0;
        int y = 0;
        Game instance = new Game(9);
        instance.winBoardState[0][1] = 0;
        instance.winBoardState[1][0] = 0;
        int expResult = 0;
        int result = instance.countWinBreaths(x, y);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of suggestTerritory method, of class Game.
     */
    @Test
    public void testSuggestTerritory() {
        Game game = new Game(9);
        game.put(0, 0, 1);
        game.put(1, 0, 1);
        game.put(2, 0, 1);
        game.put(3, 0, 1);
        game.put(0, 1, 1);
        game.put(0, 2, 1);
        game.put(0, 3, 1);
        game.put(1, 3, 1);
        game.put(2, 3, 1);
        game.put(3, 1, 1);
        game.put(3, 2, 1);
        game.put(3, 3, 1);
        game.put(7, 7, 0);
        
        game.resetWinBoard();
        game.suggestTerritory();
        assertEquals(10.5, game.countWhiteScore(),1);
        assertEquals(0, game.countBlackScore(),1);
    }

    /**
     * Test of countWhiteScore method, of class Game.
     */
    @Test
    public void testCountWhiteScore() {
        Game instance = new Game(9);
        instance.winBoardState[0][0] = 1;
        instance.winBoardState[0][1] = 1;
        instance.winBoardState[0][2] = 1;
        instance.winBoardState[0][3] = 1;
        double expResult = 10.5;
        double result = instance.countWhiteScore();
        
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of countBlackScore method, of class Game.
     */
    @Test
    public void testCountBlackScore() {
        Game instance = new Game(9);
        instance.winBoardState[0][0] = 0;
        instance.winBoardState[0][1] = 0;
        instance.winBoardState[0][2] = 0;
        instance.winBoardState[0][3] = 0;
        double expResult = 4.0;
        double result = instance.countBlackScore();
        
        assertEquals(expResult, result, 0.0);
    }
    
}
