import java.util.ArrayList;

/**
 * Created by eschmar on 11/02/16.
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
    }

    public void addLoadCommand(Warehouse w, int val1, int val2, int distance, int newX, int newY) {
        cmds.add(this.number + " " + CMD_LOAD + " " + w.number + " " + val1 + " " + val2);
        this.time -= distance + 1;
        this.x = newX;
        this.y = newY;
    }

    public void addDeliverCommand(Order o, int val1, int val2, int distance, int newX, int newY) {
        cmds.add(this.number + " " + CMD_DELIVER + " " + o.number + " " + val1 + " " + val2);
        this.time -= distance + 1;
        this.x = newX;
        this.y = newY;
    }
}
