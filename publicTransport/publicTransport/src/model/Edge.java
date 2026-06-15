package model;

public class Edge {
    private String idEdge;
    private Node tujuan;
    private int waktuMenit;
    private int biaya;
    private String jenisTransportasi;
    private boolean aktif;

    // Constructor
    public Edge(String idEdge, Node tujuan, int waktuMenit, int biayaRupiah, String jenisTransportasi) {
        this.idEdge = idEdge;
        this.tujuan = tujuan;
        this.waktuMenit = waktuMenit;
        this.biaya = biayaRupiah;
        this.jenisTransportasi = jenisTransportasi;
        this.aktif = true;
    }

    // Getter
    public String getIdEdge(){ 
        return idEdge; 
    }

    public Node getTujuan(){ 
        return tujuan; 
    }

    public int getWaktuMenit(){ 
        return waktuMenit; 
    }

    public int getBiayaRupiah(){ 
        return biaya; 
    }

    public String getJenisTransportasi(){ 
        return jenisTransportasi; 
    }

    public boolean isAktif(){ 
        return aktif; 
    }

    // Setter
    public void setIdEdge(String idEdge){ 
        this.idEdge = idEdge; 
    }

    public void setTujuan(Node tujuan){ 
        this.tujuan = tujuan; 
    }

    public void setWaktuMenit(int waktuMenit){ 
        this.waktuMenit = waktuMenit; 
    }

    public void setBiayaRupiah(int biayaBaru){ 
        this.biaya = biayaBaru; 
    }

    public void setJenisTransportasi(String jenisTransportasi){ 
        this.jenisTransportasi = jenisTransportasi; 
    }

    public void setAktif(boolean aktif) { 
        this.aktif = aktif; 
    }

    // Untuk print info edge
    @Override
    public String toString() {
        return idEdge + " → " + tujuan.getNama() + 
               " | " + waktuMenit + " menit" + 
               " | Rp " + biaya + 
               " | " + jenisTransportasi +
               (aktif ? "" : " [NONAKTIF]");
    }
}