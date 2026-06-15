package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Djikstra {
    public static class HasilRute {
        public List<String> rute;
        public int totalBobot;
        public boolean ditemukan;
 
        public HasilRute(List<String> rute, int totalBobot, boolean ditemukan) {
            this.rute = rute;
            this.totalBobot = totalBobot;
            this.ditemukan = ditemukan;
        }
    }
 
    // mode: "waktu" atau "biaya"
    public static HasilRute cari(Graph graph, String asal, String tujuan, String mode) {
        if (!graph.containsNode(asal) || !graph.containsNode(tujuan)) {
            return new HasilRute(new ArrayList<>(), -1, false);
        }
 
        Map<String, Integer> jarak = new HashMap<>();
        Map<String, String> sebelum = new HashMap<>();
        Set<String> visited = new HashSet<>();
 
        for (String node : graph.getAllNodes()) {
            jarak.put(node, Integer.MAX_VALUE);
        }
        jarak.put(asal, 0);
 
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(jarak::get));
        pq.add(asal);
 
        while (!pq.isEmpty()) {
            String sekarang = pq.poll();
            if (visited.contains(sekarang)) continue;
            visited.add(sekarang);
 
            if (sekarang.equals(tujuan)) break;
 
            for (Edge e : graph.getNeighbors(sekarang)) {
                int bobot = mode.equals("biaya") ? e.getBiaya() : e.getWaktu();
                int jarakBaru = jarak.get(sekarang) + bobot;
 
                if (jarakBaru < jarak.get(e.getTujuan())) {
                    jarak.put(e.getTujuan(), jarakBaru);
                    sebelum.put(e.getTujuan(), sekarang);
                    pq.add(e.getTujuan());
                }
            }
        }
 
        if (jarak.get(tujuan) == Integer.MAX_VALUE) {
            return new HasilRute(new ArrayList<>(), -1, false);
        }
 
        List<String> rute = new ArrayList<>();
        String node = tujuan;
        while (node != null) {
            rute.add(node);
            node = sebelum.get(node);
        }
        Collections.reverse(rute);
 
        return new HasilRute(rute, jarak.get(tujuan), true);
    }
 
    // fitur "bandingkan dua kriteria rute"
    public static void bandingkanRute(Graph graph, String asal, String tujuan) {
        HasilRute rTercepat = cari(graph, asal, tujuan, "waktu");
        HasilRute rTermurah = cari(graph, asal, tujuan, "biaya");
 
        System.out.println("Rute tercepat (berdasarkan waktu):");
        if (rTercepat.ditemukan) {
            System.out.println("  " + rTercepat.rute);
            System.out.println("  Total waktu: " + rTercepat.totalBobot + " menit");
        } else {
            System.out.println("  Tidak ada rute.");
        }
 
        System.out.println("Rute termurah (berdasarkan biaya):");
        if (rTermurah.ditemukan) {
            System.out.println("  " + rTermurah.rute);
            System.out.println("  Total biaya: Rp" + rTermurah.totalBobot);
        } else {
            System.out.println("  Tidak ada rute.");
        }
    }
}
