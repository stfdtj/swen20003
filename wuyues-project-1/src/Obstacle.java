import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

public interface Obstacle {

    Image WALL = new Image("res/wall.png");
    Image GHOST_RED = new Image("res/ghostRed.png");

    void setWalls(ArrayList<Point> obstaclePosition);
}
