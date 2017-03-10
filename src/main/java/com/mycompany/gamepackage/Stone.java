/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gamepackage;

/** Class of stone with x and y coordinates
 *  and color 0 black, 1 white
 * 
 * @author v
 */
public class Stone {
    int X;
    int Y;
    int color;
    public Stone(int x, int y, int player) {
        X = x;
        Y = y;
        color = player;
    }
}
