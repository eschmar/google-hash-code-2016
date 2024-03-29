/**
 * Warehouse
 *
 * Google HashCode 2016
 * Created by Marcel Eschmann and Simone Stefani on 09/02/16.
 */
public class Warehouse extends Location {
    public int number, inventory[];

    public Warehouse(int number, int x, int y, int inventory[]) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.inventory = inventory;
    }

    public boolean hasProduct(int prod) {
        if (prod < 0) {return false;}
        if (this.inventory[prod] > 0) {
            return true;
        }

        return false;
    }
}
