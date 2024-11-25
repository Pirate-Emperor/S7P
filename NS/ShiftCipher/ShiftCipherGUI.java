import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class ShiftCipherGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Shift Cipher Encryption and Decryption");
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton encryptButton = new JButton("Encrypt (Shift Cipher)");
        JButton decryptButton = new JButton("Decrypt (Shift Cipher)");

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
            int key = Integer.parseInt(JOptionPane.showInputDialog("Enter the Shift Key:"));
            String encrypted = encryptShiftCipher(content, key);
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
            int key = Integer.parseInt(JOptionPane.showInputDialog("Enter the Shift Key:"));
            String decrypted = decryptShiftCipher(content, key);
            Files.write(Paths.get(outputFileName), decrypted.getBytes());
            JOptionPane.showMessageDialog(null, "Decryption Completed! Decrypted file saved as " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static String encryptShiftCipher(String text, int key) {
        StringBuilder result = new StringBuilder();
        key = key % 26; // Ensure key is within range for alphabet shift

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                result.append((char) ((c - base + key + 26) % 26 + base));
            } else {
                result.append(c); // Keep non-alphabet characters unchanged
            }
        }
        return result.toString();
    }

    public static String decryptShiftCipher(String text, int key) {
        return encryptShiftCipher(text, -key); // Decryption is reverse of encryption
    }
}
