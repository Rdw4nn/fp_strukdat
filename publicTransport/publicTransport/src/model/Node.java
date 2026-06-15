package model;

public class Node {
    private String id;
    private String nama;
    private String jenis;
    private String area;
    private String fasilitas;
    private String status;

    // Constructor
    public Node(String id, String nama, String jenis, String area, String fasilitas, String status) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.area = area;
        this.fasilitas = fasilitas;
        this.status = status;
    }

    // Getter
    public String getId(){ 
        return id; 
    }

    public String getNama(){
        return nama; 
    }
    
    public String getJenis(){ 
        return jenis;  
    }

    public String getArea(){ 
        return area; 
    }

    public String getFasilitas(){
        return fasilitas; 
    }

    public String getStatus(){ 
        return status; 
    }

    // Setter
    public void setId(String id){ 
        this.id = id; 
    }

    public void setNama(String nama){ 
        this.nama = nama; 
    }

    public void setJenis(String jenis){ 
        this.jenis = jenis; 
    }

    public void setArea(String area){ 
        this.area = area; 
    }

    public void setFasilitas(String fasilitas){ 
        this.fasilitas = fasilitas; 
    }
    
    public void setStatus(String status){ 
        this.status = status; 
    }

    // Untuk print info node
    @Override
    public String toString() {
        return "[" + id + "] " + nama + " (" + jenis + ") - " + area;
    }
}