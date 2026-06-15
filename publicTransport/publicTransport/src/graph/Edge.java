package graph;

public class Edge {
    private String tujuan;
    private int waktu;
    private int biaya;
 
    public Edge(String tujuan, int waktu, int biaya) {
        this.tujuan = tujuan;
        this.waktu = waktu;
        this.biaya = biaya;
    }
 
    public String getTujuan() {
        return tujuan;
    }
 
    public int getWaktu() {
        return waktu;
    }
 
    public int getBiaya() {
        return biaya;
    }
    
}
