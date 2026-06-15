package model;
import java.util.*;

public class Graph {
    private Map<String, List<Edge>> adjList = new HashMap<>();

    public void addNode(String nama) {
        adjList.putIfAbsent(nama, new ArrayList<>());
    }

    public void addEdge(String dari, String ke, int waktu, int biaya) {
        addNode(dari);
        addNode(ke);
        adjList.get(dari).add(new Edge(ke, waktu, biaya));
    }

    // dipakai untuk fitur "simulasikan rute tidak tersedia"
    public void removeEdge(String dari, String ke) {
        List<Edge> edges = adjList.get(dari);
        if (edges != null) {
            edges.removeIf(e -> e.getTujuan().equals(ke));
        }
    }

    public List<Edge> getNeighbors(String nama) {
        return adjList.getOrDefault(nama, new ArrayList<>());
    }

    public boolean containsNode(String nama) {
        return adjList.containsKey(nama);
    }

    public Set<String> getAllNodes() {
        return adjList.keySet();
    }

    public int jumlahNode() {
        return adjList.size();
    }

    public int jumlahEdge() {
        int total = 0;
        for (List<Edge> e : adjList.values()) {
            total += e.size();
        }
        return total;
    }
}