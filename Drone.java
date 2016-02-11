/**
 * Created by eschmar on 11/02/16.
 */
public class Drone {
    public int time, payload, maxPayload, loaded[];
    public String cmds[];

    public Drone(int time, int maxPayload) {
        this.time = time;
        this.payload = 0;
        this.maxPayload = maxPayload;
    }
}
