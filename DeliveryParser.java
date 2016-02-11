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

    private int orderCount;
    private Order orders[];

    private Drone drones[];

    private FileWriter out;

    private static final String CMD_LOAD = "LOAD";
    private static final String CMD_DELIVER = "DELIVER";
    private static final String CMD_WAIT = "WAIT";
    private static final String CMD_UNLOAD = "UNLOAD";

    public DeliveryParser(String file) {
        this.fileName = file.substring(0, file.lastIndexOf('.'));
        this.fileExtension = file.substring(file.lastIndexOf('.'), file.length());
        this.readInput();
    }

    public void run() {
        File outputFile = new File(this.fileName + ".out");

        try {
            this.out = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(this.out);
            this.out.write("" + this.commandCounter);
            bw.flush();
            bw.close();

        } catch (IOException e) {
            // file not found.
            System.out.printf("[ERROR] " + e.getMessage());
        }

        System.out.println("\nParsed input file '" + this.fileName + this.fileExtension + "' and wrote output to '" + this.fileName + ".out'");
        System.out.println("\nDimensions: " + this.rows + "x" + this.cols + ". Command count: " + this.commandCounter);
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

            // DRONES
            this.drones = new Drone[this.droneCount];
            for (int i = 0; i < this.droneCount; i++) {
                this.drones[i] = new Drone(this.turns, this.maxPayload);
            }

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

                this.warehouses[i] =  new Warehouse(tempX, tempY, tempInventory);
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
                for (int j = 0; j < this.productTypes; j++) {
                    tempItems[j] = in.nextInt();
                }

                this.orders[i] =  new Order(tempX, tempY, tempAmount, tempItems);
            }

            in.close();

        } catch (FileNotFoundException e) {
            // file not found.
            System.out.printf("[ERROR] " + e.getMessage());
        }
    }

//    private void parseHorizontalLines(BufferedWriter bw) {
//        for (int i = 0; i < this.rows; i++) {
//            int j = 0;
//            while (j < this.cols) {
//                if (inputArray[i][j] == CELL_PAINTED) {
//                    int start = j;
//                    while (j < this.cols && this.inputArray[i][j] == CELL_PAINTED) {
//                        j++;
//                    }
//
//                    j--;
//                    this.commandCounter++;
//
//                    try {
//                        bw.newLine();
//                        bw.write(CMD_LINE + " " + i + " " + start + " " + i + " " + j);
//                    }catch (IOException e) {
//                        // error while writing
//                        System.out.printf("[ERROR] " + e.getMessage());
//                    }
//                }
//
//                j++;
//            }
//        }
//    }
}
