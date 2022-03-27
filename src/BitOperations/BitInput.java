package BitOperations;

import java.io.*;
import java.util.NoSuchElementException;

public class BitInput implements AutoCloseable{

    private int byte_;
    private int bitCount;
    private static final int EOF = -1;
    private final InputStream is;

    public BitInput(String fileName) throws FileNotFoundException {
        is = new FileInputStream(fileName);
        newByte();
    }

    public boolean readBit(){

        if(!hasNextBit()) throw new NoSuchElementException("EOF");

        boolean byt = ((byte_ >> (--bitCount)) & 1) == 1;

        if(bitCount == 0){
            newByte();
        }
        return byt;
    }

    public byte readByte(){
        int tmp;
        if(bitCount == 8){
            tmp = byte_ & 0xff;
            newByte();
        }
        else{
            tmp = byte_ << (8 - bitCount);
            int shift = bitCount;
            newByte();
            bitCount = shift;
            tmp |= byte_ >>> shift;
            tmp &= 0xff;
        }
        return (byte) tmp;
    }
    //00000000 0000000 0000000 00000000
    public int readInt(){
        int x = 0;
        for(int i = 0; i < 4; i++){
            x <<= 8;
            x |= readByte();
        }
        return x;
    }

    public int readInt(int bitSize){
        if(bitSize < 1 || bitSize > 32) throw new IllegalArgumentException("Bit size is too much.");
        if(bitSize == 32) return readInt();

        int x = 0;
        for(int i = 0; i < bitSize; i++){
            x <<= 1;
            if(readBit())
                x |= 1;
        }
        return x;
    }

    private void newByte(){
        try {
            byte_ = is.read();
            bitCount = 8;
        } catch (IOException e) {
            byte_ = EOF;
            bitCount = -1;
        }
    }

    private boolean hasNextBit(){
        return byte_ != EOF;
    }

    public void close() throws IOException{
        is.close();
    }
}
