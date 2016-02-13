/**
 * Google HashCode 2016
 *
 * Created by Marcel Eschmann on 09/02/16.
 * Edited by Simone Stefani and Marcel Eschmann
 */

public class Item {
    public int id;
    public int itemCode;
    public int weight;
    public Drone bestDrone;
    public Warehouse bestWarehouse;
    public int bestDistWD;
    public int bestDistWO;

    public Item(int id, int item, int weight) {
        this.id = id;
        this.itemCode = item;
        this.weight = weight;
    }

    public void setBestStrategy(Drone bestDrone, Warehouse bestWarehouse) {
        this.bestDrone = bestDrone;
        this.bestWarehouse = bestWarehouse;
    }

    public void setBestDistWDandWO(int bestDistWD, int bestDistWO) {
        this.bestDistWD = bestDistWD;
        this.bestDistWO = bestDistWO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (bestDrone != null ? !bestDrone.equals(item.bestDrone) : item.bestDrone != null) return false;
        return bestWarehouse != null ? bestWarehouse.equals(item.bestWarehouse) : item.bestWarehouse == null;

    }

    @Override
    public int hashCode() {
        int result = bestDrone != null ? bestDrone.hashCode() : 0;
        result = 31 * result + (bestWarehouse != null ? bestWarehouse.hashCode() : 0);
        return result;
    }
}
