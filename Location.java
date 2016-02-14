/**
 * Location
 *
 * Google HashCode 2016
 * Created by Marcel Eschmann and Simone Stefani on 13/02/16.
 */
abstract public class Location {
    public int x, y;

    public int distanceTo(Location b) {
        double distance = Math.sqrt(Math.pow((this.x - b.x), 2) + Math.pow(this.y - b.y, 2));
        return (int) Math.ceil(distance);
    }
}
