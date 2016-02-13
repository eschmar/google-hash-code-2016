import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by eschmar on 11/02/16.
 */
public class Drone extends Location {
    private static final char CMD_LOAD = 'L';
    private static final char CMD_DELIVER = 'D';
    private static final char CMD_WAIT = 'W';
    private static final char CMD_UNLOAD = 'U';

    public int number, time, payload, maxPayload;
    public ArrayList<String> cmds;
    public Stack<Integer> loaded;

    public Drone(Warehouse w, int number, int time, int maxPayload) {
        this.x = w.x;
        this.y = w.y;
        this.number = number;
        this.time = time;
        this.payload = maxPayload;
        this.maxPayload = maxPayload;
        this.cmds = new ArrayList<String>();
        this.loaded = new Stack<Integer>();
    }

    public void load(Warehouse w, int product) {
        loaded.push(product);
        cmds.add(this.number + " " + CMD_LOAD + " " + w.number + " " + product + " " + 1);
        this.time--;
    }

    public int deliver(Order order) {
        Integer temp;
        int commands = 0;
        while (!loaded.isEmpty()) {
            temp = loaded.pop();
            cmds.add(this.number + " " + CMD_DELIVER + " " + order.number + " " + temp + " " + 1);
            this.time--;
            commands++;
        }

        this.loaded.removeAllElements();
        return commands;
    }

    public void move(Location l) {
        this.time -= this.distanceTo(l);
        this.x = l.x;
        this.y = l.y;
    }

    public boolean canLoad(int weight) {
        return this.payload - weight >= 0;
    }

    public boolean canDeliver(Warehouse warehouse, Order order, Stack<Integer> payload) {
        int distance = 0;
        distance = this.distanceTo(warehouse) + this.distanceTo(order) + 2*payload.size();
        return (this.time - distance >= 0) ? true : false;
    }
}
