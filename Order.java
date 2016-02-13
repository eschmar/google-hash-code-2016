/**
 * Order
 * <p>
 * Google HashCode 2016
 * Created by Marcel Eschmann and Simone Stefani on 09/02/16.
 */
public class Order extends Location implements Comparable<Order> {
    public int number, amount, proximityOrigin, items[];
    boolean isDone;


    public Order(int number, int x, int y, int amount, int items[], int proximityOrigin) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.amount = amount;
        this.items = items;
        this.proximityOrigin = proximityOrigin;
    }

    @Override
    /**
     * Compare orders with respect of distance to origin
     */
    public int compareTo(Order o) {
        if (this.proximityOrigin <= o.proximityOrigin) {
            return -1;
        } else {
            return 1;
        }
    }

}
