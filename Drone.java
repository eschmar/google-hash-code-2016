import java.util.ArrayList;

/**
 * Google HashCode 2016
 *
 * Created by Marcel Eschmann on 09/02/16.
 * Edited by Simone Stefani and Marcel Eschmann
 */

public class Drone {
    private static final char CMD_LOAD = 'L';
    private static final char CMD_DELIVER = 'D';
    private static final char CMD_WAIT = 'W';
    private static final char CMD_UNLOAD = 'U';

    public int x,y;
    public int number, time, payload, maxPayload, loaded[];
    public ArrayList<String> cmds;

    public Drone(Warehouse w, int number, int time, int maxPayload) {
        this.x = w.x;
        this.y = w.y;
        this.number = number;
        this.time = time;
        this.payload = 0;
        this.maxPayload = maxPayload;
        this.cmds = new ArrayList<String>();
    }

    public void addLoadCommand(Warehouse w, int val1, int val2) {
        cmds.add(this.number + " " + CMD_LOAD + " " + w.number + " " + val1 + " " + val2);
        this.time--;
    }

    public void addDeliverCommand(Order o, int val1, int val2) {
        cmds.add(this.number + " " + CMD_DELIVER + " " + o.number + " " + val1 + " " + val2);
        this.time--;
    }

    public void changePos(int distance, int newX, int newY) {
        this.time -= distance;
        this.x = newX;
        this.y = newY;
    }
}
