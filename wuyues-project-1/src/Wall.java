import java.util.ArrayList;
import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Wall implements Obstacle{

    protected ArrayList<Point> walls = new ArrayList<Point>();
    protected ArrayList<Rectangle> recs = new ArrayList<Rectangle>();

    public void setWalls(ArrayList<Point> walls) {
        for(int i = 0; i < walls.size(); i++){
            Rectangle rectangle = new Rectangle(walls.get(i).x,walls.get(i).y,
                    WALL.getWidth(), WALL.getHeight());
            recs.add(rectangle);
        }
    }
}
