/**
 * Created by eschmar on 11/02/16.
 */
public class Order {
    public int number, x, y, amount, items[];
    boolean isDone;

    public Order(int number, int x, int y, int amount, int items[]) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.amount = amount;
        this.items = items;
    }
}
