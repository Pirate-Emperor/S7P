import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class RailFenceCipherGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rail Fence Cipher Encryption and Decryption");
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton encryptButton = new JButton("Encrypt (Rail Fence Cipher)");
        JButton decryptButton = new JButton("Decrypt (Rail Fence Cipher)");

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
            int key = Integer.parseInt(JOptionPane.showInputDialog("Enter the Key for Rail Fence Cipher:"));
            String encrypted = encryptRailFenceCipher(content, key);
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
            int key = Integer
                    .parseInt(JOptionPane.showInputDialog("Enter the Key for Decryption (Rail Fence Cipher):"));
            String decrypted = decryptRailFenceCipher(content, key);
            Files.write(Paths.get(outputFileName), decrypted.getBytes());
            JOptionPane.showMessageDialog(null, "Decryption Completed! Decrypted file saved as " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static String encryptRailFenceCipher(String text, int key) {
        char[][] rail = new char[key][text.length()];
        boolean dirDown = false;
        int row = 0, col = 0;

        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == key - 1)
                dirDown = !dirDown;

            rail[row][col++] = text.charAt(i);

            if (dirDown)
                row++;
            else
                row--;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                if (rail[i][j] != 0)
                    result.append(rail[i][j]);
            }
        }
        return result.toString();
    }

    public static String decryptRailFenceCipher(String cipher, int key) {
        char[][] rail = new char[key][cipher.length()];
        boolean dirDown = false;
        int row = 0, col = 0;

        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0 || row == key - 1)
                dirDown = !dirDown;

            rail[row][col++] = '*';

            if (dirDown)
                row++;
            else
                row--;
        }

        int index = 0;
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < cipher.length(); j++) {
                if (rail[i][j] == '*' && index < cipher.length()) {
                    rail[i][j] = cipher.charAt(index++);
                }
            }
        }

        StringBuilder result = new StringBuilder();
        dirDown = false;
        row = 0;
        col = 0;

        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0 || row == key - 1)
                dirDown = !dirDown;

            if (rail[row][col] != 0)
                result.append(rail[row][col++]);

            if (dirDown)
                row++;
            else
                row--;
        }

        return result.toString();
    }
}
