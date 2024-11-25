import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PlayfairCipherGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Playfair Cipher Encryption and Decryption");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton encryptButton = new JButton("Encrypt (Playfair Cipher)");
        JButton decryptButton = new JButton("Decrypt (Playfair Cipher)");

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
            String inputFileName = "plaintext.txt";
            String outputFileName = "cipher.txt";
            String content = new String(Files.readAllBytes(Paths.get(inputFileName)));
            String key = JOptionPane.showInputDialog("Enter the Key for Playfair Cipher:");
            String cleanedContent = cleanInput(content);
            String encrypted = encryptPlayfairCipher(cleanedContent, key);
            Files.write(Paths.get(outputFileName), encrypted.getBytes());
            JOptionPane.showMessageDialog(null, "Encryption Completed! Encrypted file saved as " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static void performDecryption() {
        try {
            String inputFileName = "cipher.txt";
            String outputFileName = "recover.txt";
            String content = new String(Files.readAllBytes(Paths.get(inputFileName)));
            String key = JOptionPane.showInputDialog("Enter the Key for Decryption (Playfair Cipher):");
            String decrypted = decryptPlayfairCipher(content, key);
            Files.write(Paths.get(outputFileName), decrypted.getBytes());
            JOptionPane.showMessageDialog(null, "Decryption Completed! Decrypted file saved as " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static String cleanInput(String text) {
        return text.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    public static String encryptPlayfairCipher(String text, String key) {
        char[][] matrix = generatePlayfairMatrix(key);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = (i + 1 < text.length()) ? text.charAt(i + 1) : 'X';
            result.append(encryptPair(a, b, matrix));
        }
        return result.toString();
    }

    public static String decryptPlayfairCipher(String text, String key) {
        char[][] matrix = generatePlayfairMatrix(key);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = (i + 1 < text.length()) ? text.charAt(i + 1) : 'X';
            result.append(decryptPair(a, b, matrix));
        }
        return result.toString();
    }

    public static char[][] generatePlayfairMatrix(String key) {
        StringBuilder sb = new StringBuilder(key);
        Set<Character> usedChars = new LinkedHashSet<>(); 
        for (char c : sb.toString().toUpperCase().toCharArray()) {
            if (Character.isLetterOrDigit(c) && !usedChars.contains(c)) {
                usedChars.add(c);
            }
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            if (!usedChars.contains(c) ) { 
                usedChars.add(c);
            }
        }
        
        for (char c = '0'; c <= '9'; c++) {
            if (!usedChars.contains(c)) {
                usedChars.add(c);
            }
        }

        char[][] matrix = new char[6][6];
        int index = 0;
        for (char c : usedChars) {
            matrix[index / 6][index % 6] = c;
            System.out.print(c);
            index++;
        }
        System.out.println();
        return matrix;
    }

    public static String encryptPair(char a, char b, char[][] matrix) {
        int[] pos1 = findPosition(a, matrix);
        int[] pos2 = findPosition(b, matrix);
        if (pos1[0] == pos2[0]) {
            return "" + matrix[pos1[0]][(pos1[1] + 1) % 6] + matrix[pos2[0]][(pos2[1] + 1) % 6];
        } else if (pos1[1] == pos2[1]) {
            return "" + matrix[(pos1[0] + 1) % 6][pos1[1]] + matrix[(pos2[0] + 1) % 6][pos2[1]];
        } else {
            return "" + matrix[pos1[0]][pos2[1]] + matrix[pos2[0]][pos1[1]];
        }
    }

    public static String decryptPair(char a, char b, char[][] matrix) {
        int[] pos1 = findPosition(a, matrix);
        int[] pos2 = findPosition(b, matrix);
        if (pos1[0] == pos2[0]) {
            return "" + matrix[pos1[0]][(pos1[1] + 5) % 6] + matrix[pos2[0]][(pos2[1] + 5) % 6];
        } else if (pos1[1] == pos2[1]) {
            return "" + matrix[(pos1[0] + 5) % 6][pos1[1]] + matrix[(pos2[0] + 5) % 6][pos2[1]];
        } else {
            return "" + matrix[pos1[0]][pos2[1]] + matrix[pos2[0]][pos1[1]];
        }
    }

    public static int[] findPosition(char c, char[][] matrix) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (matrix[i][j] == c) {
                    return new int[] { i, j };
                }
            }
        }
        throw new IllegalArgumentException("Character not found in matrix.");
    }
}
