package feature;

import model.Trie;
import model.Node;
import java.util.List;
import java.util.Scanner;

public class PrefixSearcher {

    private final Trie trie;

    public PrefixSearcher(Trie trie) {
        this.trie = trie;
    }

    public void runFeature(Scanner scanner) {

        System.out.println("\n=== PENCARIAN HALTE / STASIUN / TERMINAL ===");

        System.out.print("Masukkan awalan nama: ");

        String input = scanner.nextLine();

        List<Node> hasilPencarian =
                trie.searchByPrefix(input);

        if (hasilPencarian.isEmpty()) {

            System.out.println(
                "Data tidak ditemukan."
            );

            return;
        }

        System.out.println(
            "\nHasil pencarian:"
        );

        int nomor = 1;

        for (Node node : hasilPencarian) {

            System.out.printf(
                "%d. [%s] %s\n",
                nomor++,
                node.getId(),
                node.getNama()
            );

            System.out.println(
                "   Jenis : "
                + node.getJenis()
            );

            System.out.println(
                "   Area  : "
                + node.getArea()
            );

            System.out.println(
                "   Status: "
                + node.getStatus()
            );

            System.out.println();
        }
    }
}