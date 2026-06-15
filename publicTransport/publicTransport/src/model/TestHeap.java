package model;

public class TestHeap {

    public static void main(String[] args) {

        MinHeap heap =
            new MinHeap(20);

        Node a =
            new Node(
                "S01",
                "Gubeng",
                "Kereta",
                "",
                "",
                ""
            );

        Node b =
            new Node(
                "S03",
                "Wonokromo",
                "Kereta",
                "",
                "",
                ""
            );

        Node c =
            new Node(
                "S04",
                "Waru",
                "Kereta",
                "",
                "",
                ""
            );

        heap.insert(a, 20);
        heap.insert(b, 5);
        heap.insert(c, 12);

        while(!heap.isEmpty()) {

            MinHeap.HeapElement h =
                heap.extractMin();

            System.out.println(
                h.node.getNama()
                + " : "
                + h.value
            );
        }
    }
}