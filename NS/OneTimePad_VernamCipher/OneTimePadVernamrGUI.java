import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.Random;

public class OneTimePadVernamrGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("One-Time Pad Cipher Encryption and Decryption");
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton encryptButton = new JButton("Encrypt (One-Time Pad)");
        JButton decryptButton = new JButton("Decrypt (One-Time Pad)");

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
            String outputCipherFile = "Cipher.txt";
            String outputKeyFile = "Key.txt";
            String content = new String(Files.readAllBytes(Paths.get(inputFileName)));

            String key = generateRandomKey(content);
            String encrypted = encryptOneTimePad(content, key);

            Files.write(Paths.get(outputCipherFile), encrypted.getBytes());
            Files.write(Paths.get(outputKeyFile), key.getBytes());

            JOptionPane.showMessageDialog(null,
                    "Encryption Completed! Files saved as " + outputCipherFile + " and " + outputKeyFile);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static void performDecryption() {
        try {
            String cipherFile = "Cipher.txt";
            String keyFile = "Key.txt";
            String outputFileName = "Recover.txt";

            String cipherText = new String(Files.readAllBytes(Paths.get(cipherFile)));
            String key = new String(Files.readAllBytes(Paths.get(keyFile)));

            if (cipherText.length() != key.length()) {
                throw new IllegalArgumentException("Ciphertext and key lengths do not match!");
            }

            String decrypted = decryptOneTimePad(cipherText, key);
            Files.write(Paths.get(outputFileName), decrypted.getBytes());

            JOptionPane.showMessageDialog(null, "Decryption Completed! Decrypted file saved as " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static String encryptOneTimePad(String text, String key) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            char k = key.charAt(i);

            if (Character.isLowerCase(c)) {
                result.append((char) ((c - 'a' + (k - 'a')) % 26 + 'a'));
            } else if (Character.isUpperCase(c)) {
                result.append((char) ((c - 'A' + (k - 'A')) % 26 + 'A'));
            } else {
                result.append(c); // Keep non-alphabet characters unchanged
            }
        }
        return result.toString();
    }

    public static String decryptOneTimePad(String cipher, String key) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < cipher.length(); i++) {
            char c = cipher.charAt(i);
            char k = key.charAt(i);

            if (Character.isLowerCase(c)) {
                result.append((char) ((c - 'a' - (k - 'a') + 26) % 26 + 'a'));
            } else if (Character.isUpperCase(c)) {
                result.append((char) ((c - 'A' - (k - 'A') + 26) % 26 + 'A'));
            } else {
                result.append(c); // Keep non-alphabet characters unchanged
            }
        }
        return result.toString();
    }

    public static String generateRandomKey(String text) {
        StringBuilder key = new StringBuilder();
        Random random = new Random();

        for (char c : text.toCharArray()) {
            if (Character.isLowerCase(c)) {
                key.append((char) (random.nextInt(26) + 'a')); // Generate random lowercase letter
            } else if (Character.isUpperCase(c)) {
                key.append((char) (random.nextInt(26) + 'A')); // Generate random uppercase letter
            } else {
                key.append(c); // Non-alphabetic characters are kept unchanged
            }
        }
        return key.toString();
    }
}
