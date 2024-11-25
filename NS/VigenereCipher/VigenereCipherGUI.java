import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class VigenereCipherGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Vigenere Cipher Encryption and Decryption");
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton encryptButton = new JButton("Encrypt (Vigenere Cipher)");
        JButton decryptButton = new JButton("Decrypt (Vigenere Cipher)");

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
            String key = JOptionPane.showInputDialog("Enter the Vigenere Cipher Key (Alphabetic characters only):");
            if (!key.matches("[a-zA-Z]+")) {
                throw new IllegalArgumentException("Key must contain only alphabetic characters.");
            }
            String encrypted = encryptVigenereCipher(content, key);
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
            String key = JOptionPane.showInputDialog("Enter the Vigenere Cipher Key (Alphabetic characters only):");
            if (!key.matches("[a-zA-Z]+")) {
                throw new IllegalArgumentException("Key must contain only alphabetic characters.");
            }
            String decrypted = decryptVigenereCipher(content, key);
            Files.write(Paths.get(outputFileName), decrypted.getBytes());
            JOptionPane.showMessageDialog(null, "Decryption Completed! Decrypted file saved as " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static String encryptVigenereCipher(String text, String key) {
        StringBuilder result = new StringBuilder();
        key = key.toUpperCase(); // Normalize key to uppercase
        int keyIndex = 0;

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                int shift = key.charAt(keyIndex % key.length()) - 'A';
                result.append((char) ((c - base + shift + 26) % 26 + base));
                keyIndex++;
            } else {
                result.append(c); // Keep non-alphabet characters unchanged
            }
        }
        return result.toString();
    }

    public static String decryptVigenereCipher(String text, String key) {
        StringBuilder result = new StringBuilder();
        key = key.toUpperCase(); // Normalize key to uppercase
        int keyIndex = 0;

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                int shift = key.charAt(keyIndex % key.length()) - 'A';
                result.append((char) ((c - base - shift + 26) % 26 + base));
                keyIndex++;
            } else {
                result.append(c); // Keep non-alphabet characters unchanged
            }
        }
        return result.toString();
    }
}
