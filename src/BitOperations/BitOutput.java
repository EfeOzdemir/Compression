package BitOperations;

import java.io.*;

public class BitOutput implements AutoCloseable{
    private int byte_ = 0;
    private int bitCount = 0;
    private final OutputStream os;


    public BitOutput(String fileName, String ext) throws IOException{
        os = new BufferedOutputStream(new FileOutputStream(fileName + ext));
    }

    public void writeBit(boolean bitValue) throws IOException {
        byte_ <<= 1; //Left shift 1 bit to add a new empty bit

        if(bitValue)
            byte_ |= 1; //Makes the new bit 1

        if(++bitCount == 8){
            emptyByte_();
        }
    }

    public void writeByte(byte c) throws IOException{
        int x = Byte.toUnsignedInt(c);
        writeByte(x);
    }

    public void writeByte(int x) throws IOException{
        for(int i = 0; i < 8; i++){
            writeBit((x >> (8 - i - 1) & 1) == 1);
        }
    }

    //Writes the 32-bit integer 00000000 0000000 0000000 0000000
    public void writeInt(int x) throws IOException{
        for(int i = 1; i < 5; i++){
            writeByte(x >> (32 - i * 8) & 0xff);
        }
    }

    public void write(int x, int bits) throws IOException{

        if(bits < 1 || bits > 32) throw new IllegalArgumentException("Illegal bit size.");
        if(x < 0 || x >= (1 << bits)) throw new IllegalArgumentException("Illegal value for the bit size.");

        for(int i = 0; i < bits; i++){
            writeBit((x >> (bits - i - 1) & 1) == 1);
        }
    }

    public void writeString(String s, int bits) throws IOException{
        for(int i : s.toCharArray()){
            write(i, bits);
        }
    }

    public void writeString(String s) throws IOException{
        for(int i : s.toCharArray())
            writeByte(i);
    }

    private void emptyByte_() throws IOException{
        if(bitCount == 0) return;
        if(bitCount > 0)
            byte_ <<= (8 - bitCount);
        os.write(byte_);
        bitCount = 0;
        byte_ = 0;
    }

    public void flush() throws IOException {
        emptyByte_();
        os.flush();
    }

    public void close() throws IOException{
        flush();
        os.close();
    }

}
