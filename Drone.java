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

//    public void addLoadCommand(Warehouse w, int val1, int val2, int distance, int newX, int newY) {
//        cmds.add(this.number + " " + CMD_LOAD + " " + w.number + " " + val1 + " " + val2);
//        this.time--;
//    }
//
//    public void addDeliverCommand(Order o, int val1, int val2, int distance, int newX, int newY) {
//        cmds.add(this.number + " " + CMD_DELIVER + " " + o.number + " " + val1 + " " + val2);
//        this.time--;
//    }




    public int load(Warehouse w, Stack<Integer> load) {
        this.loaded = load;
        for (Integer product : this.loaded) {
            cmds.add(this.number + " " + CMD_LOAD + " " + w.number + " " + product + " " + 1);
            w.inventory[product]--;
            this.time--;
        }

        return this.loaded.size();
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
