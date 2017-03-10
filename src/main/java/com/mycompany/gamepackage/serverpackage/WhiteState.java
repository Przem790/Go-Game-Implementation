/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.serverpackage;

import com.mycompany.gamepackage.Game;
import java.io.PrintWriter;

/** If white stones make an action
 *
 * @author v
 */
public class WhiteState extends State {
    /**
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @param game instance of game
     * @param toBlack output that is sent to black stones
     * @param toWhite output that is sent to white stones
     */
    public int putStone(int x, int y, Game game, PrintWriter toBlack, PrintWriter toWhite) {
        if (game.checkRules(x, y, 1) == true) {
            game.put(x, y, 1);
            toWhite.println("OK");
            toBlack.println("WD:"+x+";"+y);
            return 1;
        } else {
            toWhite.println("ERR");
            return 0;
        }  
    }
}
