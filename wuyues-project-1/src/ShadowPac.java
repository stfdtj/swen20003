import bagel.*;
import bagel.util.Point;
import bagel.util.Side;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 1, 2023
 *
 * Please enter your name below
 * Wuyue Shen
 */
public class ShadowPac extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    protected static final int SCORE_PER_DOT = 10;
    private final static String GAME_TITLE = "SHADOW PAC";
    private final static String INSTRUCTION1 = "PRESS SPACE TO START";
    private final static String INSTRUCTION2 = "USE ARROW KEYS TO MOVE";
    private final static  String SCORE = "SCORE";
    private final Image HEART = new Image("res/heart.png");
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Image DOT = new Image("res/dot.png");
    private final Image GHOST = new Image("res/ghostRed.png");
    private final Image WALL = new Image("res/wall.png");
    private final Font font64 = new Font("res/FSO8BITR.TTF", 64);
    private final Font font24 = new Font("res/FSO8BITR.TTF", 24);
    private final Font font20 = new Font("res/FSO8BITR.TTF", 20);
    private static ArrayList<Point> ghosts = new ArrayList<Point>();
    private static ArrayList<Point> walls = new ArrayList<Point>();
    private static ArrayList<Point> dots = new ArrayList<Point>();
    private static ArrayList<Point> lives = new ArrayList<Point>();
    private int score = 0;
    private static int target = 0;
    private static Player pacman;
    private static Point startPoint;


    public ShadowPac(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * Method used to read file and create objects (you can change
     * this method as you wish).
     */
    private static void readCSV(String level) {
        String line;
        try{
            BufferedReader br = new BufferedReader(new FileReader(level));
            while((line = br.readLine()) != null){
                String[] temp = line.split(",");
                Point p;
                if(temp[0].equals("Wall")){
                    p = new Point(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
                    walls.add(p);
                }else if(temp[0].equals("Ghost")){
                    p = new Point(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
                    ghosts.add(p);
                }else if(temp[0].equals("Dot")){
                    p = new Point(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
                    dots.add(p);
                }else{
                    pacman = new Player(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
                    startPoint = new Point(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowPac game = new ShadowPac();

        // read level information
        String levelX = "res/level0.csv";
        readCSV(levelX);
        // initialize life
        Image temp = new Image("res/heart.png");
        for(int i = 0; i < 3; i++){
            lives.add(new Point(900 + i * temp.getWidth(), 10));
        }
        // set target score
        target = SCORE_PER_DOT * dots.size();
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    // detect whether the game start, switch time gap, which picture to show
    private boolean start = false;
    private int gap = 15;
    private boolean open = false;


    @Override
    protected void update(Input input) {

        // set walls and enemies
        Wall wall = new Wall();
        Enemy enemy = new Enemy();
        wall.walls = walls;
        wall.setWalls(walls);
        enemy.redGhosts = ghosts;
        enemy.setWalls(ghosts);


        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        // indicate game start
        if(input.wasPressed(Keys.SPACE)){
            start = true;
        }

        // if false, display title page
        if(start == false){
            font64.drawString(GAME_TITLE, 260, 250);
            font24.drawString(INSTRUCTION1, 320, 440);
            font24.drawString(INSTRUCTION2, 320, 470);
        }else if(start == true && score != target && lives.size() != 0){
            font20.drawString(SCORE, 25, 25);
            walls.forEach((p) -> WALL.drawFromTopLeft(p.x, p.y));
            dots.forEach((p) -> DOT.drawFromTopLeft(p.x, p.y));
            ghosts.forEach((p) -> GHOST.drawFromTopLeft(p.x, p.y));
            for(int i = 0; i < lives.size(); i++){
                HEART.drawFromTopLeft(900 + i*30, 10);
            }

            // draw pacman's action
            if (gap == 15){
                open = false;
            }
            if (gap <= 15 && open == false){
                pacman.closeMouth.draw(pacman.x, pacman.y,
                        new DrawOptions().setRotation(pacman.rotation));
                gap -= 1;
            }
            if(gap == 0){
                open = true;
            }
            if (gap <= 15 && open == true){
                pacman.openMouth.draw(pacman.x, pacman.y,
                        new DrawOptions().setRotation(pacman.rotation));
                gap += 1;
            }

            if (input.wasPressed(Keys.DOWN) || input.isDown(Keys.DOWN)) {
                pacman.rotation = Math.PI / 2;
                Point p = new Point(pacman.x, pacman.y);
                Point lastP = new Point(pacman.x, pacman.y + pacman.speed + pacman.height);
                if(pacman.collide(p, lastP, enemy.redHitBox) != Side.NONE){
                    lives.remove(lives.size() - 1);
                    pacman.x = startPoint.x;;
                    pacman.y = startPoint.y;
                }
                if(pacman.collide(p, lastP, wall.recs) != Side.TOP){
                    pacman.moveDown();
                }
            }

            if (input.wasPressed(Keys.UP) || input.isDown(Keys.UP)) {
                pacman.rotation = -Math.PI / 2;
                Point p = new Point(pacman.x, pacman.y);
                Point lastP = new Point(pacman.x, pacman.y - pacman.speed - pacman.height);
                if(pacman.collide(p, lastP, enemy.redHitBox) != Side.NONE){
                    lives.remove(lives.size() - 1);
                    pacman.x = startPoint.x;;
                    pacman.y = startPoint.y;
                }
                if(pacman.collide(p, lastP, wall.recs) != Side.BOTTOM){
                    pacman.moveUp();
                }
            }

            if (input.wasPressed(Keys.LEFT) || input.isDown(Keys.LEFT)) {
                pacman.rotation = Math.PI;
                Point p = new Point(pacman.x , pacman.y);
                Point lastP = new Point(pacman.x - pacman.speed - pacman.width, pacman.y);
                if(pacman.collide(p, lastP, enemy.redHitBox) != Side.NONE){
                    lives.remove(lives.size() - 1);
                    pacman.x = startPoint.x;;
                    pacman.y = startPoint.y;
                }
                if(pacman.collide(p, lastP, wall.recs) != Side.RIGHT){
                    pacman.moveLeft();
                }
            }

            if (input.wasPressed(Keys.RIGHT) || input.isDown(Keys.RIGHT)) {
                pacman.rotation = 0;
                Point p = new Point(pacman.x, pacman.y);
                Point lastP = new Point(pacman.x + pacman.speed + pacman.width, pacman.y);
                if(pacman.collide(p, lastP, enemy.redHitBox) != Side.NONE){
                    lives.remove(lives.size() - 1);
                    pacman.x = startPoint.x;;
                    pacman.y = startPoint.y;
                }
                if(pacman.collide(p, lastP, wall.recs) != Side.LEFT){
                    pacman.moveRight();
                }
            }

            if(pacman.eat(dots) == true){
                score += SCORE_PER_DOT;
            }
            font20.drawString(Integer.toString(score), 130, 25);
        }else if(score == target){
            font64.drawString("WELL DONE!", 300, 360);
        }else if(lives.size() == 0){
            font64.drawString("GAME OVER!", 300, 360);
        }
    }

}

