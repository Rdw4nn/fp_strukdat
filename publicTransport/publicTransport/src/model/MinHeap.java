package model;

public class MinHeap {
   public static class HeapElement {
        public Node node;
        public int value; // Bisa diisi waktu_menit atau biaya_rupiah tergantung kriteria Dijkstra

        public HeapElement(Node node, int value) {
            this.node = node;
            this.value = value;
        }
    }

    private HeapElement[] heap;
    private int size;
    private int capacity;

    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.heap = new HeapElement[capacity];
        this.size = 0;
    }

    private int parent(int pos) { return (pos - 1) / 2; }
    private int leftChild(int pos) { return (2 * pos) + 1; }
    private int rightChild(int pos) { return (2 * pos) + 2; }

    public boolean isEmpty() { return size == 0; }

    public void insert(Node node, int value) {
        if (size >= capacity) return;

        heap[size] = new HeapElement(node, value);
        int current = size;
        size++;

        // Up-heapify
        while (current > 0 && heap[current].value < heap[parent(current)].value) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public HeapElement extractMin() {
        if (size == 0) return null;

        HeapElement min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        
        if (size > 0) {
            minHeapify(0);
        }
        return min;
    }

    private void minHeapify(int pos) {
        int left = leftChild(pos);
        int right = rightChild(pos);
        int smallest = pos;

        if (left < size && heap[left].value < heap[smallest].value) {
            smallest = left;
        }
        if (right < size && heap[right].value < heap[smallest].value) {
            smallest = right;
        }
        if (smallest != pos) {
            swap(pos, smallest);
            minHeapify(smallest);
        }
    }

    private void swap(int fpos, int spos) {
        HeapElement tmp = heap[fpos];
        heap[fpos] = heap[spos];
        heap[spos] = tmp;
    }
 
}
