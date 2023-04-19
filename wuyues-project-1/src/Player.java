import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Side;
import java.util.ArrayList;

public class Player{

    // position and direction of the Pacman
    protected double x;
    protected double y;
    protected double rotation = 0;
    private Image DOT = new Image("res/dot.png");

    // constructor
    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // speed, 3 pixels per frame
    protected final int speed = 3;

    // image of the player
    protected final Image closeMouth = new bagel.Image("res/pac.png");
    protected final Image openMouth = new bagel.Image("res/pacOpen.png");
    protected final double width = closeMouth.getWidth();
    protected final double height = closeMouth.getHeight();

    // moving methods
    public void moveLeft(){
        this.x = this.x - speed;
    }

    public void moveRight(){
        this.x = this.x + speed;
    }

    public void moveUp(){
        this.y = this.y - speed;
    }

    public void moveDown(){
        this.y = this.y + speed;
    }



    protected Side collide(Point current, Point last, ArrayList<Rectangle> walls){
        Side result = Side.NONE;
        for(int i = 0; i < walls.size(); i++){
            if(walls.get(i).intersectedAt(current, last) != Side.NONE){
                result = walls.get(i).intersectedAt(current, last);
            }
        }
        return result;
    }

    protected boolean eat(ArrayList<Point> dots){
        Rectangle hitBox = new Rectangle(this.x - openMouth.getWidth()/2, this.y - openMouth.getHeight()/2,
                openMouth.getWidth(), openMouth.getHeight());
        for(int i = 0; i < dots.size(); i++){
            Rectangle dot = new Rectangle(dots.get(i).x, dots.get(i).y, DOT.getWidth(), DOT.getHeight());
            if(hitBox.intersects(dot)){
                dots.remove(i);
                return true;
            }
        }
        return false;
    }
}
