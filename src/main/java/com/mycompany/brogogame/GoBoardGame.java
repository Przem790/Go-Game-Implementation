package com.mycompany.brogogame;

import com.mycompany.gamepackage.Game;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by przem on 24.11.2016.
 */
public class GoBoardGame extends JPanel implements ActionListener {

    public JButton pass,exit,accept,gamecontinue,confirm;
    JTextArea black,white,status;
    JLabel statuslabel,whiteslaves,blackslaves;
    public int XYposition,x,y,state,Xlimit,Ylimit,PixelRange;
    public Double Xpos,Ypos,usercolor,Xposfirst,Yposfirst,XYincre;                                                  //deklaracja współrzędnych
    public ArrayList<Shape> dots,rects;                            //arraylist kółeczek
    public Shape pobranakropka,pobranykwadrat;                                           //zmienna do pobierania kółeczka (nie wiem czy potrzebna)
    private BufferedImage image;                                    //obrazek - plansza
    public Ellipse2D ellipse;                                       //deklaruje Ellipse
    public Double Pointlist[][][];                                   //tablica z pozornymi współrzędnymi
    public Double RealPointlist[][][];                               //z prawdziwimi współrzędnymi co do pixela :D
    public Double Pointrects[][][];
    public Double RealPointrects[][][];
    Point2D point,pointdot,pointrect,pointrect2;
    Color color,colorrect;
    PrintWriter out;
    public int BoardType,Boardsize;
    private Game game;
    Rectangle2D Rectangles;
    private int passmode;

        public GoBoardGame(int colornumber, int BoardType, PrintWriter out) {

            this.BoardType = BoardType;             //muszę stwierdzić jaki rozmiar jest tablicy

            passmode=0;
            if (BoardType == 0) {
                Boardsize = 19;
                Xposfirst=8.4;
                Yposfirst=10.0;
                XYincre=31.2;
                Xlimit=580;
                Ylimit=590;
                PixelRange=9;
            }
            else if (BoardType == 1){
                Boardsize = 13;
                Xposfirst=16.0;
                Yposfirst=17.0;
                XYincre=45.63;
                Xlimit=580;
                Ylimit=590;
                PixelRange=20;
            }
            else{
                Boardsize = 9;
                Xposfirst=19.5;
                Yposfirst=20.5;
                XYincre=53.50;
                Xlimit=460;
                Ylimit=460;
                PixelRange=20;
            }

            game=new Game(Boardsize);
            this.out=out;

            if(colornumber==0)
                usercolor=0.0;              //0 - czarne
            else
                usercolor=1.0;              //1-białe

            if(colornumber==0)
                state=1;                    //czarny zaczyna
            else
                state=0;                    //biały czeka

            addelements();                  //dodaje elementy
            createBoard();                  //mapuję Board

            rects = new ArrayList<>();      //arraylist do rectangle
            dots = new ArrayList<>();       //tworzę araraylista z kropkami (czarne i białe)

            try {
                if(BoardType==0)
                    image = ImageIO.read(new File("C:/GOBoard2.png"));   //załadowanie obrazka i repaint() żeby go załadować
                else if(BoardType==1)
                    image = ImageIO.read(new File("C:/GOBoard13x13.png"));
                else if(BoardType==2)
                    image = ImageIO.read(new File("C:/GOBoard9x9.png"));

                repaint();

            } catch (IOException ex) {                              //jeśli nie uda się załadować obrazka to wyłącza sie
                System.out.println("Error");
                System.exit(0);
            }

            addMouseListener(new GoBoardGame.Recorder());           //dodaje mouse listenera
            addMouseMotionListener(new GoBoardGame.Drawer());
        }

        /** dodaje przyciski , text area i.t.p do panelu **/
        public void addelements(){

            black=new JTextArea();
            black.setEnabled(false);
            black.setText("0");
            Font font = new Font("Serif", Font.ITALIC, 15);
            black.setFont(font);
            black.setForeground(Color.BLACK);
            black.setBackground(Color.DARK_GRAY);
            add(black);

            blackslaves=new JLabel("Black captured:");                      //Jeńczy BIALEGO
            add(blackslaves);

            white=new JTextArea();
            white.setEnabled(false);
            white.setText("0");
            white.setFont(font);
            white.setForeground(Color.BLACK);
            white.setBackground(Color.DARK_GRAY);
            add(white);
            whiteslaves=new JLabel("White captured:");                      //Jeńcy CZARNEGO
            add(whiteslaves);

            status=new JTextArea();
            status.setEnabled(false);
            if(state==1)
                status.setText("Twój Ruch!");
            else
                status.setText("Czekaj na ruch przeciwnika");
            add(status);
            statuslabel=new JLabel("Status:");
            status.setFont(font);
            status.setForeground(Color.BLACK);
            status.setBackground(Color.DARK_GRAY);
            add(statuslabel);


            confirm=new JButton("Confirm");
            pass=new JButton("Pass");
            exit=new JButton("Exit");
            accept=new JButton("Accept");
            accept.setEnabled(false);
            gamecontinue=new JButton("Continue Game");
            gamecontinue.setEnabled(false);
            confirm.setEnabled(false);
            add(confirm);
            add(gamecontinue);
            add(exit);
            add(pass);
            add(accept);
            if(state==0)
                pass.setEnabled(false);
            else
                pass.setEnabled(true);

            setLayout(null);
            if(BoardType==0||BoardType==1){                             //boardy 19,13 różnią się wielkością od 9
                pass.setBounds(600,0,200,50);
                exit.setBounds(600,50,200,50);
                accept.setBounds(600,100,200,50);
                gamecontinue.setBounds(600,150,200,50);
                confirm.setBounds(600,200,200,50);
                statuslabel.setBounds(650,250,100,20);
                status.setBounds(600,270,200,50);
                whiteslaves.setBounds(650,320,100,20);
                white.setBounds(600,340,200,50);
                blackslaves.setBounds(650,390,100,20);
                black.setBounds(600,410,200,50);


            }
            else{
                pass.setBounds(488,0,200,50);
                exit.setBounds(488,50,200,50);
                accept.setBounds(488,100,200,50);
                gamecontinue.setBounds(488,150,200,50);
                confirm.setBounds(488,200,200,50);
                statuslabel.setBounds(540,250,100,20);
                status.setBounds(488,270,200,50);
                whiteslaves.setBounds(540,320,100,20);
                white.setBounds(488,340,200,50);
                blackslaves.setBounds(540,390,100,20);
                black.setBounds(488,410,200,50);

            }
            pass.addActionListener(this);
            exit.addActionListener(this);
            accept.addActionListener(this);
            gamecontinue.addActionListener(this);
            confirm.addActionListener(this);
        }

        /**tworze tablice współrzędnych dla podanej planszy**/
        public void createBoard(){

                RealPointrects = new Double[Boardsize][Boardsize][3];
                Pointrects = new Double[Boardsize][Boardsize][3];
                Pointlist = new Double[Boardsize][Boardsize][3];                       //mapuję listę punktów
                RealPointlist = new Double[Boardsize][Boardsize][3];

                Xpos=Xposfirst;Ypos=Yposfirst;x=0;y=0;
                for(int i=0;i<(Boardsize*Boardsize);i++){

                    if(Ypos<Ylimit){
                        Pointlist[y][x][0]=Xpos;                              //PointList-punkty gdzie ma narysować kółko(potrzebne ze względu na elipse)
                        Pointlist[y][x][1]=Ypos;
                        RealPointlist[y][x][0]=Xpos+12;
                        RealPointlist[y][x][1]=Ypos+10;                       //Realpointlist-punkty do sprawdzania gdzie nacisneliśmy (dokładne)

                        Pointrects[y][x][0]=Xpos+11-XYincre/4;
                        Pointrects[y][x][1]=Ypos+9-XYincre/4;
                        RealPointrects[y][x][0]=Xpos+12;//+XYincre/2;
                        RealPointrects[y][x][1]=Ypos+10;//+XYincre/2;

                        Xpos=Xpos+XYincre;                                    //lecimy rzędami aż dojdziemy do ostatniego x, wtedy przechodzimy do kolejnego rzędu
                        if(Xpos<Xlimit){
                            x++;
                            continue;
                        }
                        else{
                            x=0;y++;
                            Xpos=Xposfirst;
                            Ypos=Ypos+XYincre;
                        }
                    }
                }
        }

        public void sendinfo(String infos){                                   /**ta metoda powinna wysłać stringa do Servera**/
        System.out.println(infos);
        out.println(infos);
        }

        @Override
        protected void paintComponent(Graphics g) {                 //paint komponent czyli załadowanie obrazka i obiektów
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Stroke oldStroke = g2d.getStroke();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(image,0,0,this);

            for(int i=dots.size()-1;i>=0;i--){                      //pobieram wszystkie kropki i je rysuje
                pobranakropka = dots.get(i);

                pointdot=new Point((int)pobranakropka.getBounds().getCenterX(),(int)pobranakropka.getBounds().getCenterY());
                pointfound(pointdot);

                if(passmode==0){
                    if(RealPointlist[y][x][2]==0.0)
                        color=Color.BLACK;
                    else if(RealPointlist[y][x][2]==1.0)
                        color=Color.WHITE;
                }
                else{
                    if(RealPointlist[y][x][2]==0.0){
                        if(Pointlist[y][x][2]==3.0)
                            color=Color.DARK_GRAY;
                        else
                            color=Color.BLACK;
                    }
                    else if(RealPointlist[y][x][2]==1.0)
                        if(Pointlist[y][x][2]==3.0)
                            color=Color.LIGHT_GRAY;
                        else
                            color=Color.WHITE;
                }
                g2d.setStroke(oldStroke);
                g2d.setColor(color);
                g2d.fill(pobranakropka);
                g2d.draw(pobranakropka);

            }


            if(passmode==1){
            for(int i=rects.size()-1;i>=0;i--){                      //pobieram wszystkie recty i je rysuje
                pobranykwadrat = rects.get(i);

                pointrect=new Point((int)pobranykwadrat.getBounds().getCenterX(),(int)pobranykwadrat.getBounds().getCenterY());
                rectfound(pointrect);
                if(RealPointrects[y][x][2]==0.0)
                    colorrect=Color.BLACK;
                else if(RealPointrects[y][x][2]==1.0)
                    colorrect=Color.WHITE;

                g2d.setStroke(new BasicStroke(5));
                g2d.setColor(colorrect);
                g2d.draw(pobranykwadrat);

            }
        }
        }

        @Override
        public Dimension getPreferredSize() {                       //ważna metoda jeśli chcemy żeby nam dobry rozmiar wyświetlało obrazka
            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();
                return new Dimension(width , height );
        }
            return super.getPreferredSize();
    }

        public void draw_dot (Graphics2D g, Double Xpos ,Double Ypos){   //metoda gdzie rysuje kółko-kropke

            ellipse = new Ellipse2D.Double(Xpos,Ypos,20,20);      //koło o promieniu 10
            System.out.println("to jest "+Xpos+" a to Y "+Ypos);        //wyświetlam sobie w konsoli pomocniczo do programowania współrzędne
            //g.draw(ellipse);                                        //rysuje elipse


            String infos;                                           //przesyłam informacje do servera odnośnie punktu
            if(usercolor==0.0)
                infos = new String("BD:"+x+";"+y);                   //tworzę stringa do wysłania
            else
                infos = new String("WD:"+x+";"+y);
            sendinfo(infos);
        }

    public int pointfound(Point2D p) {

        for (int i = 0; i<Boardsize ; i++) {
            for(int k = 0; k<Boardsize;k++){
            if (RealPointlist[i][k][0]>=(p.getX()-PixelRange)&&RealPointlist[i][k][0]<=(p.getX()+PixelRange)){      //sprawdzam czy kliknałem dobrze
                if(RealPointlist[i][k][1]>=(p.getY()-PixelRange)&&RealPointlist[i][k][1]<=(p.getY()+PixelRange)){      //najpierw dla x , potem dla y
                    y=i;
                    x=k;
                    return (1);
                }
            }
            }
        }
        return (-1);

    }

    public int rectfound(Point2D p) {

        for (int i = 0; i<Boardsize ; i++) {
            for(int k = 0; k<Boardsize;k++){
                if (RealPointrects[i][k][0]>=(p.getX()-(XYincre-20))&&RealPointrects[i][k][0]<=(p.getX()+(XYincre-20))){      //sprawdzam czy kliknałem dobrze
                    if(RealPointrects[i][k][1]>=(p.getY()-(XYincre-20))&&RealPointrects[i][k][1]<=(p.getY()+(XYincre-20))){      //najpierw dla x , potem dla y
                        y=i;
                        x=k;
                        return (1);
                    }
                }
            }
        }
        return (-1);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object listener = e.getSource();

        if(listener==pass){
            state=0;
            pass.setEnabled(false);
            status.setText("Czekaj na ruch przeciwnika");
            sendinfo("PASS");
        }
        else if(listener==exit){
            sendinfo("EXIT");
            System.exit(0);
        }
        else if(listener==accept){
            accept.setEnabled(false);
            sendinfo("ACCEPT");
        }
        else if(listener==gamecontinue){
            gamecontinue.setEnabled(false);
            passmode=0;
            repaint();
            sendinfo("CONTINUE");
        }
        else if(listener==confirm){
            gamecontinue.setEnabled(false);
            confirm.setEnabled(false);
            state=0;
            status.setText("Czekaj na przeciwnika\nustawianie!");
            sendinfo("CONFIRM");
        }
    }


    public void interpretation(String achievedString){

        if(!achievedString.equals("ACCEPT")&&!achievedString.equals("ERR")&&!achievedString.equals("PASS")&&!achievedString.startsWith("BOTHPASSED")&&!achievedString.startsWith("CONTINUE")&&!achievedString.startsWith("CONFIRM")&&!achievedString.startsWith("EXIT")){
            String AString = achievedString.substring(3,achievedString.length());
            String[] temp  = AString.split(";");
            x = Integer.parseInt(temp[0]);
            y = Integer.parseInt(temp[1]);

            if(achievedString.charAt(1)=='D'){

                if(passmode==0){
                    if(achievedString.charAt(0)=='B'){
                        RealPointlist[y][x][2]=0.0;
                        Pointlist[y][x][2]=0.0;
                    }
                    else if(achievedString.charAt(0)=='W'){
                        RealPointlist[y][x][2]=1.0;
                        Pointlist[y][x][2]=1.0;
                    }

                    Xpos=Pointlist[y][x][0];
                    Ypos=Pointlist[y][x][1];

                    ellipse = new Ellipse2D.Double(Xpos,Ypos,20,20);

                    if(usercolor==1.0)
                        game.put(x,y,0);
                    else
                        game.put(x,y,1);

                    dots.add(ellipse);
                    checkifdestroyed();
                    repaint();
                    state = 1;
                    status.setText("Twój Ruch!");
                    pass.setEnabled(true);

                    black.setText(""+game.getBlackInmates());
                    white.setText(""+game.getWhiteInmates());
                }
                else {
                    if(Pointlist[y][x][2]==3.0) {
                        Pointlist[y][x][2] = RealPointlist[y][x][2];
                    }
                    else {
                        Pointlist[y][x][2] = 3.0;
                    }
                    repaint();
                }
            }
            else if(achievedString.charAt(1)=='R'){
                if(achievedString.charAt(0)=='B'){
                    RealPointrects[y][x][2]=0.0;
                    Pointrects[y][x][2]=0.0;
                }
                else if(achievedString.charAt(0)=='W'){
                    RealPointrects[y][x][2]=1.0;
                    Pointrects[y][x][2]=1.0;
                }
                Xpos=Pointrects[y][x][0];
                Ypos=Pointrects[y][x][1];
                if(rects.size()!=0) {
                    XYposition=1;
                    for (int z = 0; z < rects.size(); z++) {
                        if (rects.get(z).getBounds().getCenterX() <= (RealPointrects[y][x][0] + 5) && rects.get(z).getBounds().getCenterX() >= (RealPointrects[y][x][0] - 5)) {
                            if (rects.get(z).getBounds().getCenterY() <= (RealPointrects[y][x][1] + 5) && rects.get(z).getBounds().getCenterY() >= (RealPointrects[y][x][1] - 5)) {
                                rects.remove(z);
                                XYposition = -1;
                                repaint();
                            }
                        }
                    }
                        if(XYposition!=-1) {
                            Rectangles = new Rectangle2D.Double(Xpos, Ypos, XYincre / 2, XYincre / 2);
                            rects.add(Rectangles);
                            repaint();
                        }
                }
                else{
                    Rectangles = new Rectangle2D.Double(Xpos, Ypos, XYincre / 2, XYincre / 2);
                    rects.add(Rectangles);
                    repaint();
                }
            }
        }
        else {
            state = 1;
            status.setText("Twój Ruch!");
            pass.setEnabled(true);
        }


    if(achievedString.equals("BOTHPASSED1")){
        game.suggestTerritory();
        passmode=1;
        accept.setEnabled(false);
        status.setText("Ustawiasz terytorium!\nObaj gracze spasowali!");
        pass.setEnabled(false);
        gamecontinue.setEnabled(true);
        confirm.setEnabled(true);
        resetterytory();
    }
    else if(achievedString.equals("BOTHPASSED2")){
        game.suggestTerritory();
        state=0;
        status.setText("Przeciwnik ustawia terytorium!");
        passmode=1;
        accept.setEnabled(false);
        pass.setEnabled(false);
        gamecontinue.setEnabled(false);
        confirm.setEnabled(false);
        resetterytory();
        }
    else if(achievedString.equals("CONTINUE1")){
        game.resetWinBoard();
        resetterytory();
        resetcolors();
        rects.clear();
        state=0;
        passmode=0;
        status.setText("Czekaj na ruch przeciwnika!");
        gamecontinue.setEnabled(false);
        accept.setEnabled(false);
        pass.setEnabled(false);
        confirm.setEnabled(false);
        repaint();
    }
    else if(achievedString.equals("CONTINUE2")){
        game.resetWinBoard();
        resetterytory();
        resetcolors();
        rects.clear();
        passmode=0;
        state=1;
        status.setText("Twój Ruch!\nPrzeciwnik kontyunuował grę!");
        gamecontinue.setEnabled(false);
        accept.setEnabled(false);
        pass.setEnabled(true);
        confirm.setEnabled(false);
        repaint();
    }
    else if(achievedString.equals("CONFIRM")){
        pass.setEnabled(false);
        confirm.setEnabled(true);
        gamecontinue.setEnabled(true);
        status.setText("Twoja kolej ustawiania!");
        //accept.setEnabled(true);
    }
    else if(achievedString.equals("ACCEPT")){
        state=0;
        pass.setEnabled(false);
        confirm.setEnabled(false);
        accept.setEnabled(true);
        gamecontinue.setEnabled(true);
        status.setText("Przeciwnik wyznaczył terytorium\ni zaakceptował!");

    }
    else if(achievedString.startsWith("EXIT")){
        if(achievedString.startsWith("EXITW")){
            String[] temp  = achievedString.split(";");
            JFrame winnerokno = new JFrame ("WYGRALES");
            JLabel result = new JLabel("Wygrałeś");
            JLabel black_result = new JLabel("Wynik czarnego:"+temp[1]);
            JLabel white_result = new JLabel("Wynik białego:"+temp[2]);
            winnerokno.setSize(200,150);
            winnerokno.setLayout(new GridLayout(3,1));
            winnerokno.add(result);
            winnerokno.add(black_result);
            winnerokno.add(white_result);
            winnerokno.setVisible(true);
            winnerokno.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent we) {
                    System.exit(0);
                }
            });
        }
        else{
            String[] temp  = achievedString.split(";");
            JFrame winnerokno = new JFrame ("WYGRALES");
            JLabel result = new JLabel("Przegrałeś");
            JLabel black_result = new JLabel("Wynik czarnego:"+temp[1]);
            JLabel white_result = new JLabel("Wynik białego:"+temp[2]);
            winnerokno.setSize(200,150);
            winnerokno.setLayout(new GridLayout(3,1));
            winnerokno.add(result);
            winnerokno.add(black_result);
            winnerokno.add(white_result);
            winnerokno.setVisible(true);
            winnerokno.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent we) {
                    System.exit(0);
                }
        });
        }


    }
    }

    private class Recorder extends MouseAdapter {                                       //informacje co ma zrobić po kliknieciu myszy
        @Override
        public void mouseClicked(MouseEvent event) {
            Graphics2D g = (Graphics2D) getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (event.getButton() == MouseEvent.BUTTON1&&passmode==0){                               //dla lewego przycisku
                point=event.getPoint();                                                 //pobieram współrzędne kliknięcia
                XYposition=pointfound(point);                                           //pobieram miejsce znajdującej się kropki
                if(XYposition!=(-1)&&state==1){                                                   //jeżeli punkt należy do punktu gdzie możemy umieścić kropke to ją umieszczamy
                    if(game.checkRules(x,y,usercolor.intValue())){
                        if(usercolor==1.0)
                            game.put(x,y,1);
                        else
                            game.put(x,y,0);
                        draw_dot(g,Pointlist[y][x][0],Pointlist[y][x][1]);                      //rysuje elipse dla podanej współrzędnej
                        Pointlist[y][x][2]=usercolor;
                        RealPointlist[y][x][2]=usercolor;
                        dots.add(ellipse);                                                  //dodaje do arraylista obiekt
                        checkifdestroyed();
                        repaint();
                    state=0;
                    status.setText("Czekaj na ruch przeciwnika");
                        pass.setEnabled(false);

                        black.setText(""+game.getBlackInmates());
                        white.setText(""+game.getWhiteInmates());
                }
                else{
                        status.setText(game.ruleInfo());
                    }
            }
            }
            else if(event.getButton() == MouseEvent.BUTTON3&&passmode==1){
                    point=event.getPoint();
                    XYposition=rectfound(point);
                    if(XYposition!=-1&&state==1) {
                        for (int z = 0; z < rects.size(); z++) {
                            if (rects.get(z).getBounds().getCenterX() <= (RealPointrects[y][x][0] + 5) && rects.get(z).getBounds().getCenterX() >= (RealPointrects[y][x][0] - 5)){
                                if (rects.get(z).getBounds().getCenterY() <= (RealPointrects[y][x][1] + 5) && rects.get(z).getBounds().getCenterY() >= (RealPointrects[y][x][1] - 5)) {
                                    rects.remove(z);
                                    repaint();
                                    String infos;                                           //przesyłam informacje do servera odnośnie punktu
                                    if(usercolor==0.0)
                                        infos = new String("BR:"+x+";"+y);                   //tworzę stringa do wysłania
                                    else
                                        infos = new String("WR:"+x+";"+y);
                                    sendinfo(infos);
                                }
                            }
                        }
                        repaint();
                    }

                    XYposition=pointfound(point);
                    if(XYposition!=-1&&state==1){
                        for (int z2 = 0; z2 < dots.size(); z2++) {
                            if (dots.get(z2).getBounds().getCenterX() <= (RealPointlist[y][x][0] + 12) && dots.get(z2).getBounds().getCenterX() >= (RealPointlist[y][x][0] - 12))
                                if (dots.get(z2).getBounds().getCenterY() <= (RealPointlist[y][x][1] + 12) && dots.get(z2).getBounds().getCenterY() >= (RealPointlist[y][x][1] - 12)) {
                                    if(Pointlist[y][x][2]==3.0)
                                        Pointlist[y][x][2]=RealPointlist[y][x][2];
                                    else
                                        Pointlist[y][x][2]=3.0;

                                    String infos;                                           //przesyłam informacje do servera odnośnie punktu
                                    if(usercolor==0.0)
                                        infos = new String("BD:"+x+";"+y);                   //tworzę stringa do wysłania
                                    else
                                        infos = new String("WD:"+x+";"+y);
                                    sendinfo(infos);
                                }
                        }
                    }
            }
        }

    }
    private class Drawer extends MouseMotionAdapter{
        @Override
        public void mouseDragged(MouseEvent e) {
            System.out.println("It workz?"+rects.size()+" state is:"+state+" passmode:"+passmode);
            Graphics2D g = (Graphics2D) getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (SwingUtilities.isLeftMouseButton(e)&&passmode==1&&state==1) {
                pointrect2 = e.getPoint();
                XYposition = rectfound(pointrect2);

                for (int z = 0; z < rects.size(); z++) {
                    if (rects.get(z).getBounds().getCenterX() <= (RealPointrects[y][x][0] + 5) && rects.get(z).getBounds().getCenterX() >= (RealPointrects[y][x][0] - 5)){
                        if (rects.get(z).getBounds().getCenterY() <= (RealPointrects[y][x][1] + 5) && rects.get(z).getBounds().getCenterY() >= (RealPointrects[y][x][1] - 5)) {
                        XYposition=-1;
                    }}
                    else{
                    for (int z2 = 0; z2 < dots.size(); z2++) {
                                if (dots.get(z2).getBounds().getCenterX() <= (RealPointrects[y][x][0] + 12) && dots.get(z2).getBounds().getCenterX() >= (RealPointrects[y][x][0] - 12))
                                    if (dots.get(z2).getBounds().getCenterY() <= (RealPointrects[y][x][1] + 12) && dots.get(z2).getBounds().getCenterY() >= (RealPointrects[y][x][1] - 12)) {
                                        XYposition = -1;
                                    }
                            }
                        }
                }
                if (XYposition != (-1)) {
                    Rectangles = new Rectangle.Double(Pointrects[y][x][0], Pointrects[y][x][1], XYincre/2, XYincre/2);
                    String infos;                                           //przesyłam informacje do servera odnośnie punktu
                    if(usercolor==0.0)
                        infos = new String("BR:"+x+";"+y);                   //tworzę stringa do wysłania
                    else
                        infos = new String("WR:"+x+";"+y);
                    sendinfo(infos);
                    Pointrects[y][x][2]=usercolor;
                    RealPointrects[y][x][2] =usercolor;
                    rects.add(Rectangles);

                    repaint();
                }
            }
        }
    }

    public void checkifdestroyed(){
        for(int k=0;k<dots.size();k++){
            pointdot=new Point((int)dots.get(k).getBounds().getCenterX(),(int)dots.get(k).getBounds().getCenterY());
            pointfound(pointdot);
            if(!game.checkIfExists(x,y)) {
                dots.remove(k);
            }
            else
                continue;
        }

    }

    public void resetterytory(){
        for(int ii=0;ii<Boardsize;ii++){
            for(int iii=0;iii<Boardsize;iii++){
                if(game.winBoardState[iii][ii]==0){
                    Xpos=Pointrects[ii][iii][0];
                    Ypos=Pointrects[ii][iii][1];
                    Pointrects[ii][iii][2]=0.0;
                    RealPointrects[ii][iii][2]=0.0;
                    Rectangles = new Rectangle2D.Double(Xpos, Ypos, XYincre / 2, XYincre / 2);
                    rects.add(Rectangles);
                    repaint();
                }else if(game.winBoardState[iii][ii]==1){
                    Xpos=Pointrects[ii][iii][0];
                    Ypos=Pointrects[ii][iii][1];
                    Pointrects[ii][iii][2]=1.0;
                    RealPointrects[ii][iii][2]=1.0;
                    Rectangles = new Rectangle2D.Double(Xpos, Ypos, XYincre / 2, XYincre / 2);
                    rects.add(Rectangles);
                    repaint();
                }else{
                    Xpos=Pointrects[ii][iii][0];
                    Ypos=Pointrects[ii][iii][1];
                    if(rects.size()!=0) {
                        for (int z = 0; z < rects.size(); z++) {
                            if (rects.get(z).getBounds().getCenterX() <= (RealPointrects[ii][iii][0] + 5) && rects.get(z).getBounds().getCenterX() >= (RealPointrects[ii][iii][0] - 5)) {
                                if (rects.get(z).getBounds().getCenterY() <= (RealPointrects[ii][iii][1] + 5) && rects.get(z).getBounds().getCenterY() >= (RealPointrects[ii][iii][1] - 5)) {
                                    rects.remove(z);
                                    repaint();
                                }
                            }
                        }
                } }

            }
    }}
    public void resetcolors(){
        for(int ii=0;ii<Boardsize-1;ii++){
            for(int iii=0;iii<Boardsize-1;iii++){
                Pointlist[ii][iii][2]=RealPointlist[ii][iii][2];
            }

        }

    }

}


