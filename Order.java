/**
 * Order
 *
 * Google HashCode 2016
 * Created by Marcel Eschmann and Simone Stefani on 09/02/16.
 */
public class Order extends Location {
    public int number, amount, cluster, items[];
    boolean isDone;

    public Order(int number, int x, int y, int amount, int items[]) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.amount = amount;
        this.items = items;
        this.cluster = -1;
    }
}
