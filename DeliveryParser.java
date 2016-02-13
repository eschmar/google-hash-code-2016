import java.io.*;
import java.util.Scanner;
import java.util.Stack;

/**
 * DeliveryParser
 *
 * Google HashCode 2016
 * Created by Marcel Eschmann and Simone Stefani on 09/02/16.
 */
public class DeliveryParser {
    /**
     * Input file
     */
    private String fileName;
    private String fileExtension;

    /**
     * Parsed input
     */
    private int rows, cols, turns, maxPayload;

    private int productCount, productsNotInStock = 0;
    private int products[];

    private int warehouseCount;
    private Warehouse warehouses[];

    private int orderCount;
    private Order orders[];

    private int droneCount, currentDrone = 0;
    private Drone drones[];

    /**
     * Helpers
     */

    private int commandCounter = 0,
            currentOrder = 0,
            processingDrone = 0;

    private boolean doneFlag = false;


    /**
     * Commands
     */
    private static final char CMD_LOAD = 'L';
    private static final char CMD_DELIVER = 'D';
    private static final char CMD_WAIT = 'W';
    private static final char CMD_UNLOAD = 'U';

    /**
     * Constructor
     * @param file
     */
    public DeliveryParser(String file) {
        this.fileName = file.substring(0, file.lastIndexOf('.'));
        this.fileExtension = file.substring(file.lastIndexOf('.'), file.length());
        this.readInput();
    }

    /**
     * Calculate simulation
     */
    public void run() {
        for (Order order : this.orders) {
            processOrder(order);
        }
    }

    /**
     * Parses input file
     */
    private void readInput() {
        File inputFile = new File(this.fileName + this.fileExtension);

        try {
            Scanner in = new Scanner(inputFile);

            this.rows = in.nextInt();
            this.cols = in.nextInt();
            this.droneCount = in.nextInt();
            this.turns = in.nextInt();
            this.maxPayload = in.nextInt();

            // PRODUTCS
            this.productCount = in.nextInt();
            this.products = new int[this.productCount];
            for (int i = 0; i < this.productCount; i++) {
                this.products[i] = in.nextInt();
            }

            // WAREHOUSES
            this.warehouseCount = in.nextInt();
            this.warehouses = new Warehouse[this.warehouseCount];
            int tempX, tempY, tempInventory[];
            for (int i = 0; i < this.warehouseCount; i++) {
                tempX = in.nextInt();
                tempY = in.nextInt();
                tempInventory = new int[productCount];
                for (int j = 0; j < this.productCount; j++) {
                    tempInventory[j] = in.nextInt();
                }

                this.warehouses[i] = new Warehouse(i, tempX, tempY, tempInventory);
            }

            // ORDERS
            this.orderCount = in.nextInt();
            this.orders = new Order[this.orderCount];
            int tempAmount, tempItems[];
            for (int i = 0; i < this.orderCount; i++) {
                tempX = in.nextInt();
                tempY = in.nextInt();
                tempAmount = in.nextInt();
                tempItems = new int[tempAmount];
                for (int j = 0; j < tempAmount; j++) {
                    tempItems[j] = in.nextInt();
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

    /**
     * Write output to filename.out
     */
    public void writeOutput() {
        FileWriter out;
        File outputFile = new File(this.fileName + ".out");

        try {
            out = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(out);

            // print amount of commands
            bw.write(String.valueOf(this.commandCounter));
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


    }

    /**
     * Show some statistics in the console
     */
    public void writeStats() {
        int processedOrders = 0;
        for (Order o : this.orders) {
            if (o.isDone) {processedOrders++;}
        }
        System.out.println("Products not in stock: " + productsNotInStock);
        System.out.println("\nParsed input file '" + this.fileName + this.fileExtension + "' and wrote output to '" + this.fileName + ".out'");
        System.out.println("Processed orders: " + processedOrders);
        System.out.println("Dimensions: " + this.rows + "x" + this.cols + ". Command count: " + this.commandCounter);
    }

    /**
     * Switch currently used drone to the next
     */
    private void alternateDrones() {
        this.currentDrone = (this.currentDrone + 1) % this.droneCount;
    }

    /**
     * Process one order using multiple drones.
     * @param order
     */
    private void processOrder(Order order) {
        Stack<Integer> partial;
        int itemsToDeliver = order.items.length;
        int prod = 0, prodId, payload = this.maxPayload;

        while (itemsToDeliver > 0) {
            // chose next item
            while (prod < order.items.length -1 && order.items[prod] == -1) {
                prod++;
            }

            prodId = order.items[prod];
            payload = this.maxPayload;

            // find closest warehouse with product
            Warehouse warehouse = getClosestWarehouse(prodId, order);
            if (warehouse == null) {
                // no warehouse has this product in stock!
                productsNotInStock++;
                break;
            }

            // create partial
            partial = new Stack<Integer>();
            partial.push(prodId);
            payload -= this.products[prodId];
            warehouse.inventory[prodId]--;
            order.items[prod] = -1;
            itemsToDeliver--;

            // check for other products to load in this warehouse
            for (int i = prod+1; i < order.items.length; i++) {
                if (order.items[i] == -1) { continue; }

                if (warehouse.hasProduct(order.items[i]) && payload - this.products[order.items[i]] > 0) {
                    partial.push(order.items[i]);
                    payload -= this.products[order.items[i]];
                    order.items[i] = -1;
                    itemsToDeliver--;
                }
            }

            // load partial to next available drone
            int tries = droneCount;
            while (tries > 0 && !this.drones[currentDrone].canDeliver(warehouse, order, partial)) {
                tries--;
                this.alternateDrones();
            }

            if (!this.drones[currentDrone].canDeliver(warehouse, order, partial)) {
                // this should never happen
                break;
            }

            for (int item : partial) {
                this.drones[currentDrone].load(warehouse, item);
                this.commandCounter++;
                warehouse.inventory[item]--;
            }

            this.drones[currentDrone].move(warehouse);

            // deliver
            this.commandCounter += this.drones[currentDrone].deliver(order);
            this.drones[currentDrone].move(order);

            // use next drone
            alternateDrones();
        }

        order.isDone = true;
    }

    /**
     * Find closest warehouse having a specific product in stock
     * @param prod
     * @param order
     * @return
     */
    private Warehouse getClosestWarehouse(int prod, Order order) {
        Warehouse solution = null;
        int distance = 0, currentDistance;

        for (Warehouse current : this.warehouses) {
            if (current.hasProduct(prod)) {
                currentDistance = current.distanceTo(order);

                if (distance == 0 || currentDistance < distance) {
                    solution = current;
                    distance = currentDistance;
                }
            }
        }

        return solution;
    }
}
