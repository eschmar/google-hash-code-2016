import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Google HashCode 2016
 * <p>
 * Created by Marcel Eschmann on 09/02/16.
 * Edited by Simone Stefani and Marcel Eschmann
 */

public class DeliveryParser {
    private String fileName;
    private String fileExtension;

    private int commandCounter = 0;
    private int rows, cols, droneCount, turns, maxPayload;

    private int productTypes;
    private int products[];

    private int warehouseCount;
    private Warehouse warehouses[];

    private int orderCount;
    private Order orders[];

    private Drone drones[];

    private FileWriter out;

    private static final char CMD_LOAD = 'L';
    private static final char CMD_DELIVER = 'D';
    private static final char CMD_WAIT = 'W';
    private static final char CMD_UNLOAD = 'U';

    public DeliveryParser(String file) {
        this.fileName = file.substring(0, file.lastIndexOf('.'));
        this.fileExtension = file.substring(file.lastIndexOf('.'), file.length());
        this.readInput();
    }

    public void run() {
        // CALC
        for (Order order : this.orders) {
            processOrder(order);
        }


        // OUTPUT
        File outputFile = new File(this.fileName + ".out");

        try {
            this.out = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(this.out);
            bw.newLine();

            // write output here
            for (Drone drone : this.drones) {
                for (String cmd : drone.cmds) {
                    bw.write(cmd);
                    bw.newLine();
                }
            }

            bw.flush();
            bw.close();

        } catch (IOException e) {
            // file not found.
            System.out.printf("[ERROR] " + e.getMessage());
        }

        int processedOrders = 0;
        for (Order o : this.orders) {
            if (o.isDone) {
                processedOrders++;
            }
        }

        System.out.println("\nParsed input file '" + this.fileName + this.fileExtension + "' and wrote output to '" + this.fileName + ".out'");
        System.out.println("Processed orders: " + processedOrders);
        System.out.println("Dimensions: " + this.rows + "x" + this.cols + ". Command count: " + this.commandCounter);
    }

    private void readInput() {
        File inputFile = new File(this.fileName + this.fileExtension);

        try {
            Scanner in = new Scanner(inputFile);

            // BASIC INFO
            this.rows = in.nextInt();
            this.cols = in.nextInt();
            this.droneCount = in.nextInt();
            this.turns = in.nextInt();
            this.maxPayload = in.nextInt();

            // PRODUTCS
            this.productTypes = in.nextInt();
            this.products = new int[this.productTypes];
            for (int i = 0; i < this.productTypes; i++) {
                this.products[i] = in.nextInt();
            }

            // WAREHOUSES
            this.warehouseCount = in.nextInt();
            this.warehouses = new Warehouse[this.warehouseCount];
            int tempX, tempY, tempInventory[];
            for (int i = 0; i < this.warehouseCount; i++) {
                tempX = in.nextInt();
                tempY = in.nextInt();
                tempInventory = new int[productTypes];
                for (int j = 0; j < this.productTypes; j++) {
                    tempInventory[j] = in.nextInt();
                }

                this.warehouses[i] = new Warehouse(i, tempX, tempY, tempInventory);
            }

            // ORDERS
            this.orderCount = in.nextInt();
            this.orders = new Order[this.orderCount];
            int tempAmount;
            Item tempItems[];
            for (int i = 0; i < this.orderCount; i++) {
                tempX = in.nextInt();
                tempY = in.nextInt();
                tempAmount = in.nextInt();
                tempItems = new Item[tempAmount];
                for (int j = 0; j < tempAmount; j++) {
                    int itemValue = in.nextInt();
                    Item newItem = new Item(j, itemValue, products[itemValue]);
                    tempItems[j] = newItem;
                }
                this.orders[i] = new Order(i, tempX, tempY, tempAmount, tempItems);
            }

            // DRONES
            this.drones = new Drone[this.droneCount];
            for (int i = 0; i < this.droneCount; i++) {
                this.drones[i] = new Drone(this.warehouses[0], i, this.turns, this.maxPayload);
            }

            in.close();

        } catch (FileNotFoundException e) {
            // file not found.
            System.out.printf("[ERROR] " + e.getMessage());
        }
    }

    private int distance(Warehouse w, Order o) {
        double distance = Math.sqrt(Math.pow((o.x - w.x), 2) + Math.pow(o.y - w.y, 2));
        return (int) Math.ceil(distance);
    }

    private int distance(Drone w, Order o) {
        double distance = Math.sqrt(Math.pow((o.x - w.x), 2) + Math.pow(o.y - w.y, 2));
        return (int) Math.ceil(distance);
    }

    private int distance(Warehouse w, Drone o) {
        double distance = Math.sqrt(Math.pow((o.x - w.x), 2) + Math.pow(o.y - w.y, 2));
        return (int) Math.ceil(distance);
    }

    private boolean hasProduct(Warehouse w, int prod) {
        return w.inventory[prod] > 0;

    }

    /*
    private boolean orderIsDone(Order order) {
        boolean done = false;
        for (int product : order.items) {
            if (product > -1) {
                done = true;
            }
        }
        return done;
    }
    */

    private void processOrder(Order order) {
        ArrayList<Package> itemPackages = new ArrayList();

        for (int i = 0; i < order.items.length; i++) {
            Item product = order.items[i];
            int minDist = 0;
            boolean existsHash = false;

            for (Drone drone : drones) {
                for (Warehouse warehouse : warehouses) {
                    if (hasProduct(warehouse, product.itemCode)) {
                        int distWO = distance(warehouse, order);
                        int distDO = distance(drone, order);
                        int distWD = distance(warehouse, drone);
                        if (drone.time - (distWD + 1 + distWO + 1) >= 0) {
                            int distSum = distDO + distWD + distWO;
                            if (distSum < minDist || minDist == 0) {
                                product.setBestStrategy(drone, warehouse);
                            }
                        }
                    }
                }
            }
            product.setBestDistWDandWO(distance(product.bestWarehouse, product.bestDrone), distance(product.bestWarehouse, order));

            /*
            bestDrone.addLoadCommand(bestWarehouse, product, 1, bestDistWD, bestWarehouse.x, bestWarehouse.y);
            bestWarehouse.inventory[product]--;
            bestDrone.addDeliverCommand(order, product, 1, bestDistWO, order.x, order.y);
            this.commandCounter += 2;
            */
            int hashCode = product.hashCode();


            for (Package pack : itemPackages) {
                if (pack.hashCode == hashCode && (pack.weight + product.weight) <= maxPayload) {
                    pack.addItem(product);
                    existsHash = true;
                    break;
                }
            }
            if (!existsHash) {
                Package newPackage = new Package(itemPackages.size(), hashCode, product);
                itemPackages.add(newPackage);

            }
        }

        for (Package pack : itemPackages) {
            Drone baseDrone = null;
            Warehouse baseWarehouse = null;
            int baseDistWD = 0;
            int baseDistWO = 0;

            for (Item item : pack.items) {
                baseDrone = item.bestDrone;
                baseWarehouse = item.bestWarehouse;
                baseDistWD = item.bestDistWD;
                baseDistWO = item.bestDistWO;
                item.bestDrone.addLoadCommand(item.bestWarehouse, item.itemCode, 1);
                item.bestWarehouse.inventory[item.itemCode]--;
                this.commandCounter++;
            }
            baseDrone.changePos(baseDistWD, baseWarehouse.x, baseWarehouse.y);

            for (Item item : pack.items) {
                item.bestDrone.addDeliverCommand(order, item.itemCode, 1);

            }
            baseDrone.changePos(baseDistWO, order.x, order.y);
        }

        order.isDone = true;
    }
}
