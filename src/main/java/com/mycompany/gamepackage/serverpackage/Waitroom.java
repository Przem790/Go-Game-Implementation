/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.serverpackage;

import com.mycompany.botimpleme.BotClient;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/** Waiting room to pair players that want to play on the same
 *  type of board
 *
 * @author v
 */
public class Waitroom {
    /** waitingX - players that wait for opponent for board X size
     *  lastingGames - games containing two players and board 
     *                  type that take part in a game
     */
    private ArrayList<Socket> waiting9 = new ArrayList();
    private ArrayList<Socket> waiting13 = new ArrayList();
    private ArrayList<Socket> waiting19 = new ArrayList();
    private ArrayList<Party> lastingGames = new ArrayList();
    
    public Waitroom() {       
    }
    /** Add new player to waiting of chosen type
     * 
     * @param newPlayer socket of new player
     * @throws IOException 
     */
    public void addPlayer(Socket newPlayer) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(newPlayer.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(newPlayer.getOutputStream())), true);
        String command = in.readLine();
        System.out.println(command);
        if (command.equals("START9")) {
            waiting9.add(newPlayer);
            System.out.println("added9");
        } else if (command.equals("START13")) {
            waiting13.add(newPlayer);
            System.out.println("added13");
        } else if (command.equals("START19")) {
            waiting19.add(newPlayer);
            System.out.println("added19");
        } else if (command.equals("BOTSTART9")) {
            new BotGameThread(newPlayer,9);
        } else if (command.equals("BOTSTART13")) {
            new BotGameThread(newPlayer,13);
        } else if (command.equals("BOTSTART19")) {
            new BotGameThread(newPlayer,19);
        } else {
            System.out.println("Something went wrong!");
        }
        updatePlayers();
        startPossibleGames();
    }
    private void updatePlayers() {
        for (int i = 0; i < waiting9.size(); i++) {
            if (waiting9.get(i).isClosed()) {
                waiting9.remove(i);
            }
        }
        for (int i = 0; i < waiting13.size(); i++) {
            if (waiting13.get(i).isClosed()) {
                waiting13.remove(i);
            }
        }
        for (int i = 0; i < waiting13.size(); i++) {
            if (waiting13.get(i).isClosed()) {
                waiting13.remove(i);
            }
        }
    }
    
    private void startPossibleGames() throws IOException {
        /** Start 9-board games if there are enough waiting players */
        if (waiting9.size() > 1) {
            new ServOneThread(waiting9.get(0), waiting9.get(1), 9);
            waiting9.remove(0);
            waiting9.remove(0);
        } else {
            System.out.println("no more 9players");
        }
        /** Start 13-board games if there are enough waiting players */
        if (waiting13.size() > 1) {
            new ServOneThread(waiting13.get(0), waiting13.get(1), 13);
            waiting13.remove(0);
            waiting13.remove(0);
        } else {
            System.out.println("no more 13players");
        }
        /** Start 19-board games if there are enough waiting players */
        if (waiting19.size() > 1) {
            new ServOneThread(waiting19.get(0), waiting19.get(1), 19);
            waiting19.remove(0);
            waiting19.remove(0);
        } else {
            System.out.println("no more 19players");
        }
    }
}
