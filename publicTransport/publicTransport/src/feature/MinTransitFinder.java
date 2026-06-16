package feature;

import algorithm.BFS;
import model.Graph;
import model.Node;
import java.util.List;
import java.util.Scanner;

public class MinTransitFinder {

    private final Graph graph;

    public MinTransitFinder(Graph graph) {
        this.graph = graph;
    }

    /**
     * Fitur: Cari rute dengan jumlah transit paling sedikit.
     * Dipanggil dari Main.java saat user memilih menu ini.
     */
    public void runFeature(Scanner scanner) {
        System.out.println("\n========================================");
        System.out.println("   CARI RUTE MINIMUM TRANSIT ");
        System.out.println("========================================");

        System.out.print("Masukkan ID halte/stasiun/terminal (ASAL): ");
        String asalId = scanner.nextLine().trim().toUpperCase();

        System.out.print("Masukkan ID halte/stasiun/terminal (TUJUAN): ");
        String tujuanId = scanner.nextLine().trim().toUpperCase();

        // Validasi node ada di graph
        if (!graph.containsNode(asalId)) {
            System.out.println("ERROR: ID '" + asalId + "' tidak ditemukan.");
            return;
        }
        if (!graph.containsNode(tujuanId)) {
            System.out.println("ERROR: ID '" + tujuanId + "' tidak ditemukan.");
            return;
        }

        Node nodeAsal   = graph.getNode(asalId);
        Node nodeTujuan = graph.getNode(tujuanId);

        System.out.println("\nMencari rute dari:");
        System.out.println("  Asal   : [" + asalId + "] " + nodeAsal.getNama());
        System.out.println("  Tujuan : [" + tujuanId + "] " + nodeTujuan.getNama());
        System.out.println("  Algoritma: BFS (Breadth-First Search)");
        System.out.println("----------------------------------------");

        // Jalankan BFS
        BFS.HasilBFS hasil = BFS.cariMinimumTransit(graph, asalId, tujuanId);

        if (!hasil.ditemukan) {
            System.out.println("Tidak ada rute yang tersedia saat ini.");
            System.out.println("(Mungkin ada rute yang sedang nonaktif, coba fitur simulasi.)");
            return;
        }

        // Tampilkan hasil rute
        System.out.println("\nRute ditemukan!");
        System.out.println("Jumlah transit : " + hasil.jumlahTransit + " perpindahan");
        System.out.println();

        List<String> rute = hasil.rute;
        for (int i = 0; i < rute.size(); i++) {
            Node node = graph.getNode(rute.get(i));
            if (i == 0) {
                System.out.println("  " + (i + 1) + ". [" + node.getId() + "] " + node.getNama()
                        + " (" + node.getJenis() + ") <- ASAL");
            } else if (i == rute.size() - 1) {
                System.out.println("  " + (i + 1) + ". [" + node.getId() + "] " + node.getNama()
                        + " (" + node.getJenis() + ") <- TUJUAN");
            } else {
                System.out.println("  " + (i + 1) + ". [" + node.getId() + "] " + node.getNama()
                        + " (" + node.getJenis() + ")");
            }

            // Tampilkan info edge ke node berikutnya
            if (i < rute.size() - 1) {
                tampilkanInfoEdge(rute.get(i), rute.get(i + 1));
            }
        }

        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("RINGKASAN:");
        System.out.println("  Total Waktu  : " + hasil.totalWaktu + " menit");
        System.out.println("  Total Biaya  : Rp " + String.format("%,d", hasil.totalBiaya));
        System.out.println("  Total Transit: " + hasil.jumlahTransit + " perpindahan");
        System.out.println("========================================");
    }

    // Tampilkan info edge antara dua node
    private void tampilkanInfoEdge(String dariId, String keId) {
        for (model.Edge edge : graph.getNeighbors(dariId)) {
            if (edge.isAktif() && edge.getTujuan().getId().equals(keId)) {
                System.out.println( "     >> " + edge.getJenisTransportasi()
                        + " | " + edge.getWaktuMenit() + " menit"
                        + " | Rp " + String.format("%,d", edge.getBiayaRupiah()));
                break;
            }
        }
    }
}