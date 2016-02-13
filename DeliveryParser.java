import java.io.*;
import java.util.Scanner;

/**
 * Created by eschmar on 09/02/16.
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

    private int productCount;
    private int products[];

    private int warehouseCount;
    private Warehouse warehouses[];

    private int orderCount;
    private Order orders[];

    private int droneCount;
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

        System.out.println("\nParsed input file '" + this.fileName + this.fileExtension + "' and wrote output to '" + this.fileName + ".out'");
        System.out.println("Processed orders: " + processedOrders);
        System.out.println("Dimensions: " + this.rows + "x" + this.cols + ". Command count: " + this.commandCounter);
    }



    private Warehouse getClosestWarehouse(Order o) {
        Warehouse solution = this.warehouses[0];
        int distance = 0, currentDistance;

        for (Warehouse current : this.warehouses) {
            currentDistance = current.distanceTo(o);
            if (distance == 0 || currentDistance < distance) {

                // TODO: CHECK PRODUCT AVAILABILITY!

                solution = current;
                distance = currentDistance;
            }
        }

        return solution;
    }



    private Warehouse getClosestWarehouseForProd(Order o, int prod) {
        Warehouse solution = null;
        int distance = 0, currentDistance;

        for (Warehouse current : this.warehouses) {
            if (current.hasProduct(prod)) {
                currentDistance = current.distanceTo(o);

                if (distance == 0 || currentDistance < distance) {
                    solution = current;
                    distance = currentDistance;
                }
            }
        }

        return solution;
    }

    private void processOrder(Order order) {
        Drone drone = this.drones[processingDrone];

        Warehouse closest;
        int distToWarehouse = 0;
        int distToOrder = 0;

        for (int product : order.items) {
            closest = getClosestWarehouseForProd(order, product);

            if (closest == null) {
                continue;
            }

            distToWarehouse = closest.distanceTo(drone);
            distToOrder = closest.distanceTo(order);

            while (drone.time - (distToWarehouse + 1 + distToOrder + 1) < 0) {
                if (processingDrone == this.drones.length - 1) {
                    break;
                }

                processingDrone++;
                drone = this.drones[processingDrone];
                distToWarehouse = closest.distanceTo(drone);
                distToOrder = closest.distanceTo(order);
            }

            drone.addLoadCommand(closest, product, 1, distToWarehouse, closest.x, closest.y);
            closest.inventory[product]--;
            drone.addDeliverCommand(order, product, 1, distToOrder, order.x, order.y);
            this.commandCounter += 2;
        }

        order.isDone = true;
    }
}
