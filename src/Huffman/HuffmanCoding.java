package Huffman;

import BitOperations.BitInput;
import BitOperations.BitOutput;
import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class HuffmanCoding {

    private HuffmanCoding(){}

    private static class Node implements Comparable<Node>{

        private final int count;
        private final Byte c;
        private Node left, right;

        public Node(Byte c, int count){
            this.c = c;
            this.count = count;
        }

        @Override
        public int compareTo(Node o) {
            return this.count - o.count;
        }
    }

    private static void generate(Node root, java.util.Map<Byte, String> codes, String str){
        if(root == null) return;

        if(root.left == null && root.right == null){
            codes.put(root.c, str.length() > 0 ? str : "1");
        }

        generate(root.left, codes, str + "0");
        generate(root.right, codes, str + "1");
    }

    public static void compress(String fileName) throws IOException{
        byte[] data = java.nio.file.Files.readAllBytes(Paths.get(fileName));
        HashMap<Byte, Integer> counts = new HashMap<>();

        for (byte cur : data) {
            if (counts.containsKey(cur))
                counts.put(cur, counts.get(cur) + 1);
            else
                counts.put(cur, 1);
        }

        PriorityQueue<Node> q = new PriorityQueue<>();
        for(Map.Entry<Byte, Integer> entry : counts.entrySet()){
            q.add(new Node(entry.getKey(), entry.getValue()));
        }


        while (q.size() > 1){
            Node cur1 = q.poll();
            Node cur2 = q.poll();

            Node parent = new Node(null, cur1.count + cur2.count);
            parent.left = cur1;
            parent. right = cur2;
            q.add(parent);
        }

        Node root = q.poll();

        HashMap<Byte, String> codes = new HashMap<>();
        generate(root, codes, "");

        StringBuilder sb = new StringBuilder();
        for (byte datum : data) {
            sb.append(codes.get(datum));
        }

        writeCompressedFile(root, counts, codes, sb.toString(), fileName);
    }

    public static void uncompress(String fileName) throws IOException{
        BitInput bi = new BitInput(fileName);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(fileName.substring(0, fileName.length()-3)));
        int bitSize = bi.readInt();
        Node codeTree = readTree(bi);
        Node trav = codeTree;
        boolean bit;

        for(int i = 0; i <= bitSize; i++){
            bit = bi.readBit();
            if(trav.left == trav.right){
                os.write(trav.c);
                trav = codeTree;
            }

            if(bit)
                trav = trav.right;
            else
                trav = trav.left;
        }

        bi.close();
        os.flush();
        os.close();
    }

    private static void writeMapTree(Node root, BitOutput bo) throws IOException {
        if(root == null) return;

        if(root.left == root.right){
            bo.writeBit(true);
            bo.writeByte(root.c);
        }
        else
            bo.writeBit(false);

        writeMapTree(root.left, bo);
        writeMapTree(root.right, bo);
    }

    private static void writeCompressedFile(Node head, HashMap<Byte, Integer> counts, HashMap<Byte, String> codes, String compressedCode, String fname) throws IOException {
        try (BitOutput os = new BitOutput(fname, ".hf")){
            os.writeInt(calcBitCount(counts, codes));
            writeMapTree(head, os);
            for(char c : compressedCode.toCharArray()){
                os.writeBit(c == '1');
            }
        }
    }

    private static Node readTree(BitInput bi){
        boolean bit = bi.readBit();
        Node tmp;
        if(bit)
            tmp = new Node(bi.readByte(), -1);
        else{
            tmp = new Node(null, -1);
            tmp.left = readTree(bi);
            tmp.right = readTree(bi);
        }
        return tmp;
    }

    private static int calcBitCount(HashMap<Byte, Integer> counts, HashMap<Byte, String> codes){
        int size = 0;
        for(Map.Entry<Byte, Integer> b : counts.entrySet()){
            size += b.getValue() * codes.get(b.getKey()).length();
        }
        return size;
    }
}
