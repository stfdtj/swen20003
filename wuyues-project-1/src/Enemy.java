import java.util.ArrayList;
import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Side;

public class Enemy implements Obstacle {

    protected ArrayList<Point> redGhosts = new ArrayList<Point>();
    protected ArrayList<Rectangle> redHitBox = new ArrayList<Rectangle>();

    public void setWalls(ArrayList<Point> obstaclePosition){
        for(int i = 0; i < obstaclePosition.size(); i++){
            Rectangle rectangle = new Rectangle(obstaclePosition.get(i).x,obstaclePosition.get(i).y,
                    GHOST_RED.getWidth()+2, GHOST_RED.getHeight()+2);
            redHitBox.add(rectangle);
        }
    }
}
