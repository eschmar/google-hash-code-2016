/**
 * Delivery
 * Created by eschmar on 08/02/16.
 */
public class Delivery {
    public static void main(String[] args) {
        DeliveryParser parser = new DeliveryParser(args[0]);
        parser.run();
        parser.writeOutput();
        parser.writeStats();
    }
}
