import Huffman.HuffmanCoding;
import LZW.LZW;
import javax.swing.*;
import java.io.IOException;

public class MyGUI extends JFrame{
    private JButton huffmanCompress;
    private JButton huffmanUncompress;
    private JButton lzwCompress;
    private JButton lzwUncompress;
    private JLabel mainHeader;
    private JLabel huffmanHeader;
    private JLabel lzwHeader;
    private JPanel mainPanel;
    private JButton SELECTFILEButton;
    private JFormattedTextField selectedFile;
    private JLabel selectedLabel;

    public MyGUI() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1250, 500);
        this.setTitle("Huffman Coding and LZW Compression Project - 202028011");

        SELECTFILEButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(null);
            selectedFile.setValue(fc.getSelectedFile().getAbsolutePath());
        });

        huffmanCompress.addActionListener(e -> {
            try {
                HuffmanCoding.compress((String) selectedFile.getValue());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        huffmanUncompress.addActionListener(e -> {
            String file = (String) selectedFile.getValue();
            if(file == null || file.substring(file.length() - 3).compareTo(".hf") != 0){
                JOptionPane.showMessageDialog(null, "Wrong file format.", "File Format Exception", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                HuffmanCoding.uncompress(file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        lzwCompress.addActionListener(e -> {
            try {
                LZW.compress((String) selectedFile.getValue());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        lzwUncompress.addActionListener(e -> {
            String file = (String) selectedFile.getValue();
            if(file == null || file.substring(file.length() - 4).compareTo(".lzw") != 0){
                JOptionPane.showMessageDialog(null, "Wrong file format.", "File Format Exception", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                LZW.decompress(file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        this.add(mainPanel);
        this.setVisible(true);
    }
}
