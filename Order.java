/**
 * Google HashCode 2016
 *
 * Created by Marcel Eschmann on 09/02/16.
 * Edited by Simone Stefani and Marcel Eschmann
 */

public class Order {
    public int number, x, y, amount;
    boolean isDone;
    public Item items[];

    public Order(int number, int x, int y, int amount, Item items[]) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.amount = amount;
        this.items = items;
    }
}
