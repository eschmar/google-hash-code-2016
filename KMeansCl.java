import com.sun.org.apache.xpath.internal.operations.Or;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * KMeansCl
 *
 * Google HashCode 2016
 * Created by Marcel Eschmann and Simone Stefani on 14/02/16.
 */

public class KMeansCl {
    private int numClusters, numOrders, maxX, maxY;
    private Order[] orders;
    public ArrayList<Cluster> clusters;

    public KMeansCl(int numClusters, int numOrders, int maxX, int maxY, Order[] orders) {
        this.numClusters = numClusters;
        this.numOrders = numOrders;
        this.maxX = maxX;
        this.maxY = maxY;
        this.orders = orders;
        this.clusters = new ArrayList<>();
    }

    /**
     * Creates an array of clusters with a random centroid for each cluster
     */
    public void setup() {
        for (int i = 0; i < numClusters; i++) {
            Cluster newCluster = new Cluster(i);
            Point newCentroid = randomPoint(maxX, maxY);
            newCluster.centroid = newCentroid;
            clusters.add(newCluster);
        }
    }

    /**
     * Generate a random point. The coordinates and boundaries are integers
     * @param maxX
     * @param maxY
     * @return
     */
    public static Point randomPoint(int maxX, int maxY) {
        Random rnd = new Random();
        int x = maxX * rnd.nextInt();
        int y = maxY * rnd.nextInt();
        return new Point(x, y);
    }

    /**
     * Get an array with the current centroid for each cluster in clusters
     * @return
     */
    private ArrayList<Point> getCentroids() {
        ArrayList<Point> centroids = new ArrayList<>(numClusters);
        for (Cluster cls : clusters) {
            Point newPoint = cls.centroid;
            centroids.add(newPoint);
        }
        return centroids;
    }

    /**
     * Assign an order to a specific cluster according to the distance
     */
    private void assignCluster() {
        double minDist = -1;
        int cluster = 0;

        for (Order ord : orders) {
            for (int i = 0; i < numClusters; i++) {
                Cluster c = clusters.get(i);
                double distance = Point.distance(ord.x, ord.y, c.centroid.x, c.centroid.y);
                if (minDist < 0 || distance < minDist) {
                    minDist = distance;
                    cluster = i;
                }
            }
            ord.cluster = cluster;
            clusters.get(cluster).points.add(ord);
        }
    }


    private void clearClusters() {
        for(Cluster cls : clusters) {
            cls.points.clear();
        }
    }

    /**
     * Recompute the centroid averaging the distance sum with all the orders
     * that where assigned to the cluster
     */
    private void computeCentroids() {
        for (Cluster cls : clusters) {
            int sumX = 0, sumY = 0;
            ArrayList<Order> list = cls.points;
            int numOrders = list.size();

            for (Order ord : list) {
                sumX += ord.x;
                sumY += ord.y;
            }

            if (numOrders > 0) {
                int newX = sumX / numOrders;
                int newY = sumY / numOrders;
                cls.centroid.x = newX;
                cls.centroid.y = newY;

            }
        }
    }

    public void computeClusters() {
        boolean done = false;
        while (!done) {
            clearClusters();
            ArrayList<Point> prevCentroids = getCentroids();
            assignCluster();
            computeCentroids();
            ArrayList<Point> currCentroids = getCentroids();

            double distance = 0;
            for (int i = 0; i < prevCentroids.size(); i++) {
                distance += centroidsDist(prevCentroids.get(i), currCentroids.get(i));
            }

            // TODO: This convergence criteria is too strict?
            // Define threshold or maximum number of iterations
            if (distance == 0) {
                done = true;
            }
        }
    }

    public double centroidsDist(Point p1, Point p2) {
        return Point.distance(p1.x, p1.y, p2.x, p2.y);
    }
}
