import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TranspositionCipherGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Transposition Cipher Encryption and Decryption");
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton encryptButton = new JButton("Encrypt (Transposition Cipher)");
        JButton decryptButton = new JButton("Decrypt (Transposition Cipher)");

        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performEncryption();
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performDecryption();
            }
        });

        JPanel panel = new JPanel();
        panel.add(encryptButton);
        panel.add(decryptButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void performEncryption() {
        try {
            String inputFileName = "Plaintext.txt";
            String outputFileName = "Cipher.txt";
            String content = new String(Files.readAllBytes(Paths.get(inputFileName)));
            String key = JOptionPane.showInputDialog("Enter the Key for Columnar Transposition:");

            String encrypted = encryptColumnar(content, key);
            Files.write(Paths.get(outputFileName), encrypted.getBytes());

            JOptionPane.showMessageDialog(null, "Encryption Completed! Encrypted file saved as " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static void performDecryption() {
        try {
            String inputFileName = "Cipher.txt";
            String outputFileName = "Recover.txt";
            String content = new String(Files.readAllBytes(Paths.get(inputFileName)));
            String key = JOptionPane.showInputDialog("Enter the Key for Columnar Transposition Decryption:");

            String decrypted = decryptColumnar(content, key);
            Files.write(Paths.get(outputFileName), decrypted.getBytes());

            JOptionPane.showMessageDialog(null, "Decryption Completed! Decrypted file saved as " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static String encryptColumnar(String text, String key) {
        int columns = key.length();
        int rows = (int) Math.ceil((double) text.length() / columns);

        char[][] grid = new char[rows][columns];
        int index = 0;

        // Fill grid row by row
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (index < text.length()) {
                    grid[r][c] = text.charAt(index++);
                } else {
                    grid[r][c] = ' '; // Padding with spaces
                }
            }
        }

        // Sort the key to determine column order
        List<Integer> columnOrder = getColumnOrder(key);

        // Read columns in the order determined by the key
        StringBuilder result = new StringBuilder();
        for (int col : columnOrder) {
            for (int r = 0; r < rows; r++) {
                result.append(grid[r][col]);
            }
        }

        return result.toString();
    }

    public static String decryptColumnar(String cipher, String key) {
        int columns = key.length();
        int rows = (int) Math.ceil((double) cipher.length() / columns);

        char[][] grid = new char[rows][columns];
        int index = 0;

        // Sort the key to determine column order
        List<Integer> columnOrder = getColumnOrder(key);

        // Fill the grid column by column based on the sorted key
        for (int col : columnOrder) {
            for (int r = 0; r < rows; r++) {
                if (index < cipher.length()) {
                    grid[r][col] = cipher.charAt(index++);
                }
            }
        }

        // Read the grid row by row
        StringBuilder result = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                result.append(grid[r][c]);
            }
        }

        return result.toString().trim(); // Trim padding spaces
    }

    private static List<Integer> getColumnOrder(String key) {
        List<Integer> order = new ArrayList<>();
        List<Character> sortedKey = new ArrayList<>();

        // Add key characters to a list and sort
        for (char c : key.toCharArray()) {
            sortedKey.add(c);
        }
        Collections.sort(sortedKey);

        // Generate column order based on the sorted key
        for (char c : sortedKey) {
            order.add(key.indexOf(c));
        }

        return order;
    }
}
