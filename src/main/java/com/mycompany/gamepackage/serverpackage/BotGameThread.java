/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.serverpackage;


import com.mycompany.gamepackage.Game;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Thread serving game against bot.
 * 
 * @author v
 */
public class BotGameThread extends Thread {
    private Socket player;
    private BufferedReader in;
    private PrintWriter out;
    private Game game;
    private int size;
        /** Setting input, output and starting a new thread
         * 
         */
    public BotGameThread(Socket sock, int boardSize) throws IOException {
        player = sock;
        size = boardSize;
        in = new BufferedReader(new InputStreamReader(player.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(player.getOutputStream())), true);
        /** Setting players color BLACK*/        
        out.println("COLORBLACK");

        game = new Game(boardSize);
        System.out.println("Thread started");
        start();        
    }
    
    @Override
    public void run() {
        RandomGener gen = new RandomGener();
        String str;
        int turn = 0; 
        
        try {
            while (!player.isClosed()) {
                str = in.readLine();
                if (str != null) {
                Interpreter interpreter = new Interpreter();
                BlackState black = new BlackState();
                WhiteState white = new WhiteState();
                int x = 0, y = 0;
                int k = 0;
                /** What type of action it is. */
                if (interpreter.getCommandType(str).equals("MOVE")) {
                    x = interpreter.getX(str);
                    y = interpreter.getY(str);
                }
                if (str.equals("EXIT")) {
                    throw new IOException("Player disconnected");
                }
                /** If player passes */
                if (str.equals("PASS")) {
                    out.println("BOTHPASSED1");
                    int select = 0;
                    while (select == 0) {
                        str = in.readLine();
                        if (str.equals("CONFIRM")) {
                            game.resetWinBoard();
                            game.suggestTerritory();
                            double bs = game.countBlackScore();
                            double ws = game.countWhiteScore();
                            System.out.println("B = "+bs);
                            System.out.println("W = "+ws);
                            if (bs > ws) {
                                out.println("EXITWINNER;"+bs+";"+ws);
                            } else {
                                out.println("EXITLOSER;"+bs+";"+ws);
                            } 
                        } else if (str.equals("CONTINUE")) {
                            select++;
                        } else {
                            x = interpreter.getX(str);
                            y = interpreter.getY(str);
                            game.putWinBoard(x, y, 0);
                        }
                    }
                }
                
                /** Black turn, check if move is OK. */
                if (str.startsWith("BD:")) {
                    game.put(x, y, 0);
                    x = gen.Gener.nextInt(size);
                    y = gen.Gener.nextInt(size);
                    while (game.checkRules(x, y, 1) == false) {
                        x = gen.Gener.nextInt(size);
                        y = gen.Gener.nextInt(size);
                    }
                    Thread.sleep(1000);
                    game.put(x, y, 1);
                    str = new String("WD:"+x+";"+y);
                    out.println(str);
                }
            }
            System.out.println("Player disconnected");
            }
        } catch (IOException ex) {
            System.err.println("IO Exception");
             Logger.getLogger(BotGameThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(BotGameThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                    player.close();
            } catch(IOException ex) {
                System. out.println("Socket still active!");
            }
        }
    }
}