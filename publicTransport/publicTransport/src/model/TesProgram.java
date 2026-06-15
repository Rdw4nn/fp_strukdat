package model;
public class TesProgram {
    public static void main(String[] args) {
        Graph g = new Graph();

        g.addEdge("Halte A", "Halte B", 10, 3000);
        g.addEdge("Halte B", "Halte C", 5, 2000);
        g.addEdge("Halte A", "Halte C", 20, 4000);
        g.addEdge("Halte C", "Halte D", 8, 2500);
        g.addEdge("Halte B", "Halte D", 15, 1000);

        System.out.println("Jumlah node: " + g.jumlahNode());
        System.out.println("Jumlah edge: " + g.jumlahEdge());
        System.out.println();

        Djikstra.bandingkanRute(g, "Halte A", "Halte D");

        System.out.println();
        System.out.println("Simulasi: edge Halte B -> Halte D dihapus");
        g.removeEdge("Halte B", "Halte D");
        Djikstra.bandingkanRute(g, "Halte A", "Halte D");
    }
}