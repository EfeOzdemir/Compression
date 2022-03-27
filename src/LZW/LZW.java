package LZW;

import BitOperations.BitInput;
import BitOperations.BitOutput;
import java.io.*;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class LZW {

    private static final int bitSize = 12;
    private static final int boundary = 4096;
    private static final int beginIndex = 256;


    public static void compress(String path) throws IOException{
        InputStream is = new FileInputStream( path);
        BitOutput bo = new BitOutput(path, ".lzw");
        HashMap<String, Integer> hm = new HashMap<>();
        int ch;
        String code = "";

        for(int i = 0; i < beginIndex; i++)
            hm.put("" + (char) i, i);

        while ((ch = is.read()) != -1){
            String temp = code + (char) ch;

            if(hm.containsKey(temp))
                code = temp;
            else {
                bo.write(hm.get(code), 12);
                //System.out.println(hm.get(code));
                if(hm.size() < boundary)
                    hm.put(temp, hm.size());
                code = (char) ch + "";
            }
        }
        //System.out.println(hm.get(code));
        bo.write(hm.get(code), bitSize);
        bo.close();
        is.close();
    }

    public static void decompress(String path) throws IOException{
        BitInput bi = new BitInput(path);
        BitOutput bo = new BitOutput(path.substring(0, path.length()-3), "");
        HashMap<Integer, String> hm = new HashMap<>();

        for(int i = 0; i < beginIndex; i++)
            hm.put(i, "" + (char) i );


        int prev = bi.readInt(bitSize);
        int cur;
        bo.writeString(hm.get(prev));
        System.out.println(hm.get(prev));

        while (true){
            try {
                cur = bi.readInt(bitSize);
                if(!hm.containsKey(cur)){
                    hm.put(hm.size(), hm.get(prev) + hm.get(prev).charAt(0));
                    bo.writeString(hm.get(prev) + hm.get(prev).charAt(0));
                    //System.out.println(hm.get(prev) + hm.get(prev).charAt(0));
                }
                else {
                    hm.put(hm.size(), hm.get(prev) + hm.get(cur).charAt(0));
                    bo.writeString(hm.get(cur));
                    //System.out.println(hm.get(cur));

                }
                prev = cur;
            }
            catch (NoSuchElementException e){
                bo.close();
                bi.close();
                return;
            }
        }
    }
}
