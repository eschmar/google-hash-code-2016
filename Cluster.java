import java.awt.*;
import java.util.ArrayList;

/**
 * Cluster
 *
 * Google HashCode 2016
 * Created by Marcel Eschmann and Simone Stefani on 14/02/16.
 */

public class Cluster {
    public ArrayList<Order> points;
    public Point centroid;
    public int id;

    public Cluster(int id) {
        this.points = new ArrayList<>();
        this.centroid = null;
        this.id = id;
    }
}
