package com.mycompany.serverpackagetest;


/**
 * Created by przem on 12.12.2016.
 */

import com.mycompany.brogogame.GoBoardGame;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ClientTest{
    StringWriter out = new StringWriter();
    PrintWriter printOut = new PrintWriter(out);
    GoBoardGame mygameinstance1=new GoBoardGame(0,0,printOut);
    GoBoardGame mygameinstance2=new GoBoardGame(0,1,printOut);
    GoBoardGame mygameinstance3=new GoBoardGame(0,2,printOut);
    GoBoardGame mygameinstance4=new GoBoardGame(1,0,printOut);
    GoBoardGame mygameinstance5=new GoBoardGame(1,1,printOut);
    GoBoardGame mygameinstance6=new GoBoardGame(1,2,printOut);


    @Test
    public void constructorTest(){


        Assert.assertEquals(mygameinstance1.BoardType,0);
        Assert.assertEquals(mygameinstance2.BoardType,1);
        Assert.assertEquals(mygameinstance3.BoardType,2);
        Assert.assertEquals(mygameinstance4.BoardType,0);
        Assert.assertEquals(mygameinstance5.BoardType,1);
        Assert.assertEquals(mygameinstance6.BoardType,2);

        Assert.assertEquals(mygameinstance1.state,1);
        Assert.assertEquals(mygameinstance4.state,0);

        Assert.assertEquals(mygameinstance1.RealPointlist.length,19);
        Assert.assertEquals(mygameinstance2.RealPointlist.length,13);
        Assert.assertEquals(mygameinstance3.RealPointlist.length,9);
        Assert.assertEquals(mygameinstance1.Pointlist.length,19);
        Assert.assertEquals(mygameinstance2.Pointlist.length,13);
        Assert.assertEquals(mygameinstance3.Pointlist.length,9);
        Assert.assertEquals(mygameinstance1.RealPointrects.length,19);
        Assert.assertEquals(mygameinstance2.RealPointrects.length,13);
        Assert.assertEquals(mygameinstance3.RealPointrects.length,9);
        Assert.assertEquals(mygameinstance1.Pointrects.length,19);
        Assert.assertEquals(mygameinstance2.Pointrects.length,13);
        Assert.assertEquals(mygameinstance3.Pointrects.length,9);

    }
    @Test
    public void drawdotTest(){
        Graphics2D g21 = (Graphics2D)mygameinstance1.getGraphics();
        mygameinstance1.draw_dot(g21,8.4,10.0);                                    //326 zmutowałem draw ellipse
        Graphics2D g22 = (Graphics2D)mygameinstance2.getGraphics();
        mygameinstance1.draw_dot(g22,16.0,17.0);                                    //326 zmutowałem draw ellipse
        Graphics2D g23 = (Graphics2D)mygameinstance3.getGraphics();
        mygameinstance1.draw_dot(g23,19.5,20.5);                                    //326 zmutowałem draw ellipse
        Graphics2D g24 = (Graphics2D)mygameinstance4.getGraphics();
        mygameinstance1.draw_dot(g24,8.4,10.0);                                    //326 zmutowałem draw ellipse
        Graphics2D g25 = (Graphics2D)mygameinstance5.getGraphics();
        mygameinstance1.draw_dot(g25,16.0,17.0);                                    //326 zmutowałem draw ellipse
        Graphics2D g26 = (Graphics2D)mygameinstance6.getGraphics();
        mygameinstance1.draw_dot(g26,19.5,20.5);                                    //326 zmutowałem draw ellipse

        mygameinstance1.checkifdestroyed();
        mygameinstance2.checkifdestroyed();
        mygameinstance3.checkifdestroyed();
    }

    @Test
    public void pointfoundTest(){
        Point2D p1 = new Point(8,10);
        Point2D p2 = new Point(16,17);
        Point2D p3 = new Point(19,20);
        mygameinstance1.pointfound(p1);
        mygameinstance2.pointfound(p2);
        mygameinstance3.pointfound(p3);
        Assert.assertEquals(mygameinstance1.x,0);
        Assert.assertEquals(mygameinstance1.y,19);
        Assert.assertEquals(mygameinstance2.x,0);
        Assert.assertEquals(mygameinstance2.y,0);
        Assert.assertEquals(mygameinstance3.x,0);
        Assert.assertEquals(mygameinstance3.y,0);


    }
    @Test
    public void rectsTest(){
        Rectangle2D myrectangele1= new Rectangle2D.Double(mygameinstance1.Pointrects[0][0][0],mygameinstance1.Pointrects[0][0][1],mygameinstance1.XYincre/2,mygameinstance1.XYincre/2);
        Rectangle2D myrectangele2= new Rectangle2D.Double(mygameinstance2.Pointrects[0][0][0],mygameinstance2.Pointrects[0][0][1],mygameinstance2.XYincre/2,mygameinstance2.XYincre/2);
        Rectangle2D myrectangele3= new Rectangle2D.Double(mygameinstance3.Pointrects[0][0][0],mygameinstance3.Pointrects[0][0][1],mygameinstance3.XYincre/2,mygameinstance3.XYincre/2);
        mygameinstance1.rects.add(myrectangele1);
        mygameinstance2.rects.add(myrectangele2);
        mygameinstance3.rects.add(myrectangele3);
        Assert.assertEquals(mygameinstance1.rects.size(),1);
        Assert.assertEquals(mygameinstance2.rects.size(),1);
        Assert.assertEquals(mygameinstance3.rects.size(),1);
        mygameinstance1.resetterytory();
        mygameinstance2.resetterytory();
        mygameinstance3.resetterytory();
        Assert.assertEquals(mygameinstance1.rects.size(),0);
        Assert.assertEquals(mygameinstance2.rects.size(),0);
        Assert.assertEquals(mygameinstance3.rects.size(),0);
        mygameinstance1.resetcolors();
        mygameinstance2.resetcolors();
        mygameinstance3.resetcolors();

    }

    @Test
    public void interpretationTest(){
        mygameinstance1.interpretation("ACCEPT");
        mygameinstance1.interpretation("CONFIRM");
        mygameinstance1.interpretation("BD;13;15");
        mygameinstance1.interpretation("WD;13;15");
        Assert.assertEquals(mygameinstance1.dots.size(),2);



    }
    }
