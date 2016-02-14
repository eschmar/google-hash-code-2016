/**
 * Delivery
 *
 * Google HashCode 2016
 * Created by Marcel Eschmann and Simone Stefani on 09/02/16.
 */
public class Delivery {
    public static void main(String[] args) {
        DeliveryParser parser = new DeliveryParser(args[0]);
        parser.run();
        parser.writeOutput();
        parser.writeStats();
    }
}
