/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gamepackage;

import java.util.ArrayList;

/**
 *
 * @author v
 */
public class StoneGroup {
    private int breaths;
    public ArrayList<Stone> chain;
    public int color;
    private final Game game;
    /**
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @param player 0 black 1 white
     * @param game instance of game
     */
    public StoneGroup(int x, int y, int player, Game game) {
        this.game = game;
        chain = new ArrayList();
        color = player;
        Stone tempStone = new Stone(x,y,player);
        chain.add(tempStone);
    }
    /** Add stone to the group.
     * 
     * @param stone 
     */
    public void addToGroup(Stone stone) {
        chain.add(stone);
    }
    /** Count breaths of a group.
     * 
     */
    public void updateBreaths() {
        int tempBreaths = 0;
        
        for (Stone s : chain) {
            tempBreaths += game.countBreaths(s.X, s.Y);
        }
        breaths = tempBreaths;
    }
    /** Check if stone belongs to this group.
     * 
     * @param stone
     * @return 
     */
    public boolean checkIfIn(Stone stone) {
        for (Stone s : chain) {
            if (s.X == stone.X && s.Y == stone.Y)
                return true;   
        }
        return false;
    }
    /**
     * 
     * @return number of breaths
     */
    public int getBreaths() {
        return breaths;
    }
}