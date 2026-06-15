package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord;
        List<Node> associatedNodes = new ArrayList<>(); 
    }

    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }
    public void insert(String name, Node node) {
        TrieNode current = root;
        String searchName = name.toLowerCase().trim();
        
        for (char ch : searchName.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
        current.associatedNodes.add(node);
    }
    public List<Node> searchByPrefix(String prefix) {
        List<Node> results = new ArrayList<>();
        TrieNode current = root;
        String searchPrefix = prefix.toLowerCase().trim();

        for (char ch : searchPrefix.toCharArray()) {
            current = current.children.get(ch);
            if (current == null) {
                return results; // Jika prefix tidak ditemukan, return list kosong
            }
        }
        findAllNodes(current, results);
        return results;
    }

    private void findAllNodes(TrieNode node, List<Node> results) {
        if (node.isEndOfWord) {
            results.addAll(node.associatedNodes);
        }
        for (TrieNode child : node.children.values()) {
            findAllNodes(child, results);
        }
    }
}