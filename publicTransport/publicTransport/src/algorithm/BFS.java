package algorithm;

import model.Graph;
import model.Edge;
import model.Node;

import java.util.*;

public class BFS {

    // Class HasilBFS menyimpan hasil pencarian BFS , termasuk rute yang ditemukan, total waktu, total biaya, jumlah transit, dan apakah rute ditemukan atau tidak
    public static class HasilBFS {
        // urutan node dari asal ke tujuan (list ID node)
        public List<String> rute;      
        // akumulasi waktu dan biaya sepanjang rute (hanya dihitung untuk edge yang aktif)
        public int totalWaktu;
        // total biaya sepanjang rute (hanya dihitung untuk edge yang aktif)
        public int totalBiaya;
        // jumlah perpindahan antar node
        public int jumlahTransit;       
        // status apakah rute ditemukan atau tidak
        public boolean ditemukan;

        public HasilBFS(List<String> rute, int totalWaktu, int totalBiaya, boolean ditemukan) {
            this.rute = rute;
            this.totalWaktu = totalWaktu;
            this.totalBiaya = totalBiaya;
            // jumlah transit adalah jumlah node dalam rute dikurangi 1 (karena tidak dihitung node awal)
            this.jumlahTransit = ditemukan ? rute.size() - 1 : 0;
            this.ditemukan = ditemukan;
        }
    }

    /**
     * Mencari rute dengan jumlah transit MINIMUM dari asal ke tujuan.
     * BFS menjamin jalur dengan jumlah langkah (transit) paling sedikit.
     *
     * Kompleksitas Waktu : O(V + E) — V = jumlah node, E = jumlah edge
     * Kompleksitas Ruang : O(V) — untuk menyimpan visited dan queue
     */
    public static HasilBFS cariMinimumTransit(Graph graph, String asalId, String tujuanId) {

        // Validasi node asal dan tujuan ada di graph
        if (!graph.containsNode(asalId) || !graph.containsNode(tujuanId)) {
            System.out.println("Node asal atau tujuan tidak ditemukan di graph.");
            return new HasilBFS(new ArrayList<>(), -1, -1, false);
        }

        // Jika asal dan tujuan sama, tidak perlu pencarian
        if (asalId.equals(tujuanId)) {
            List<String> rute = new ArrayList<>();
            rute.add(asalId);
            return new HasilBFS(rute, 0, 0, true);
        }

        // Queue menyimpan path (list ID) yang sedang dijelajahi
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> sudahDikunjungi = new HashSet<>();

        // Masukkan path awal berisi hanya node asal
        List<String> pathAwal = new ArrayList<>();
        pathAwal.add(asalId);
        queue.add(pathAwal);
        sudahDikunjungi.add(asalId);

        while (!queue.isEmpty()) {
            List<String> pathSekarang = queue.poll();
            String nodeSekarang = pathSekarang.get(pathSekarang.size() - 1);

            // Cek semua tetangga dari node sekarang
            for (Edge edge : graph.getNeighbors(nodeSekarang)) {

                // Skip edge yang sedang nonaktif (fitur simulasi)
                if (!edge.isAktif()) continue;

                String idTetangga = edge.getTujuan().getId();

                // jika node tetangga nonaktif, skip 
                if (edge.getTujuan().getStatus().equalsIgnoreCase("Nonaktif")) continue;
                // Kalau sudah sampai tujuan, hitung total & kembalikan
                if (idTetangga.equals(tujuanId)) {
                    List<String> pathFinal = new ArrayList<>(pathSekarang);
                    pathFinal.add(idTetangga);

                    // Hitung total waktu dan biaya
                    int[] totalWaktuBiaya = hitungTotalWaktuBiaya(graph, pathFinal);
                    return new HasilBFS(pathFinal, totalWaktuBiaya[0], totalWaktuBiaya[1], true);
                }

                // Kalau belum dikunjungi, tambahkan ke queue
                if (!sudahDikunjungi.contains(idTetangga)) {
                    sudahDikunjungi.add(idTetangga);
                    List<String> pathBaru = new ArrayList<>(pathSekarang);
                    pathBaru.add(idTetangga);
                    queue.add(pathBaru);
                }
            }
        }

        // Tidak ada rute ditemukan
        return new HasilBFS(new ArrayList<>(), -1, -1, false);
    }

    /**
     * Menghitung total waktu dan biaya dari sebuah path (list ID node).
     * Return: int[0] = total waktu, int[1] = total biaya
     */
    public static int[] hitungTotalWaktuBiaya(Graph graph, List<String> path) {
        int totalWaktu = 0;
        int totalBiaya = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            String dari = path.get(i);
            String ke = path.get(i + 1);

            for (Edge edge : graph.getNeighbors(dari)) {
                if (edge.isAktif() && edge.getTujuan().getId().equals(ke)) {
                    totalWaktu += edge.getWaktuMenit();
                    totalBiaya += edge.getBiayaRupiah();
                    break;
                }
            }
        }

        return new int[]{totalWaktu, totalBiaya};
    }
}
