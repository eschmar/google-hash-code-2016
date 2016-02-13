import java.util.ArrayList;

/**
 * Google HashCode 2016
 *
 * Created by Marcel Eschmann on 09/02/16.
 * Edited by Simone Stefani and Marcel Eschmann
 */

public class Package {
    public int id;
    public int hashCode;
    public int weight;
    public ArrayList<Item> items;
    public boolean delivered;

    public Package(int id, int hashCode, Item item) {
        this.id = id;
        this.hashCode = hashCode;
        this.weight = 0;
        items = new ArrayList<>();
        items.add(item);
        this.delivered = false;
    }

    public void addItem(Item item) {


        items.add(item);
    }
}
