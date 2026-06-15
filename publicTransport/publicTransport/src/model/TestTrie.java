package model;

import java.util.List;

public class TestTrie {

    public static void main(String[] args) {

        Trie trie = new Trie();

        Node n1 =
            new Node(
                "S01",
                "Stasiun Surabaya Gubeng",
                "Kereta",
                "Surabaya Timur",
                "Toilet",
                "Aktif"
            );

        Node n2 =
            new Node(
                "S02",
                "Stasiun Surabaya Pasar Turi",
                "Kereta",
                "Surabaya Utara",
                "Toilet",
                "Aktif"
            );

        Node n3 =
            new Node(
                "B08",
                "Halte Diponegoro",
                "Bus",
                "Surabaya Pusat",
                "Toilet",
                "Aktif"
            );

        trie.insert(n1.getNama(), n1);
        trie.insert(n2.getNama(), n2);
        trie.insert(n3.getNama(), n3);

        List<Node> hasil =
            trie.searchByPrefix("Sta");

        System.out.println(
            "Hasil Prefix 'Sta'"
        );

        for(Node n : hasil){

            System.out.println(
                n.getNama()
            );
        }
    }
}