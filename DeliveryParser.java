import java.io.*;
import java.util.Scanner;

/**
 * Created by eschmar on 09/02/16.
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

    private int orderCount, currentOrder = 0;
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
        for (Drone drone : this.drones) {
            processDrone(drone);
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

        System.out.println("\nParsed input file '" + this.fileName + this.fileExtension + "' and wrote output to '" + this.fileName + ".out'");
        System.out.println("Dimensions: " + this.rows + "x" + this.cols + ". Command count: " + this.commandCounter);
    }

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

    private Warehouse getClosestWarehouse(Order o) {
        Warehouse solution = this.warehouses[0];
        int distance = 0, currentDistance;

        for (Warehouse current : this.warehouses) {
            currentDistance = distance(current, o);
            if (distance == 0 || currentDistance < distance) {

                // TODO: CHECK PRODUCT AVAILABILITY!

                solution = current;
                distance = currentDistance;
            }
        }

        return solution;
    }

    private void processDrone(Drone drone) {
        Order order = this.orders[currentOrder];

        while (order.isDone)
            if (order.isDone) {
                order = this.orders[++currentOrder];
            }

        Warehouse closest = getClosestWarehouse(order);
        int distance = distance(closest, order);

        int distToWarehouse = 0;
        int distToOrder = 0;
        for (int product : order.items) {
            distToWarehouse = distance(closest, drone);
            distToOrder = distance(closest, order);

            // skip delivered products
            if (product == -1) {
                continue;
            }

            if (drone.time - (distToWarehouse + 1 + distToOrder + 1) < 0) {
                break;
            }

            drone.addLoadCommand(closest, product, 1, distToWarehouse, closest.x, closest.y);
            drone.addDeliverCommand(order, product, 1, distToOrder, order.x, order.y);
            this.commandCounter += 2;
            product = -1;
        }
    }
}
