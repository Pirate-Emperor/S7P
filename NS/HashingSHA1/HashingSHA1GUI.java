import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.security.*;

public class HashingSHA1GUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("SHA-1 Hashing");
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton hashFileButton = new JButton("Hash File (SHA-1)");
        JButton hashTextButton = new JButton("Hash Text (SHA-1)");

        hashFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performFileHashing();
            }
        });

        hashTextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performTextHashing();
            }
        });

        JPanel panel = new JPanel();
        panel.add(hashFileButton);
        panel.add(hashTextButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void performFileHashing() {
        try {
            String inputFileName = JOptionPane.showInputDialog("Enter the name of the file to hash:");
            String content = new String(Files.readAllBytes(Paths.get(inputFileName)));
            String hash = computeSHA1Hash(content);

            JOptionPane.showMessageDialog(null, "SHA-1 Hash: " + hash);

            String outputFileName = "HashResult.txt";
            Files.write(Paths.get(outputFileName), hash.getBytes());
            JOptionPane.showMessageDialog(null, "Hash saved to " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static void performTextHashing() {
        try {
            String text = JOptionPane.showInputDialog("Enter the text to hash:");
            if (text == null || text.isEmpty()) {
                throw new IllegalArgumentException("Input text cannot be empty!");
            }
            String hash = computeSHA1Hash(text);

            JOptionPane.showMessageDialog(null, "SHA-1 Hash: " + hash);

            String outputFileName = "HashResult.txt";
            Files.write(Paths.get(outputFileName), hash.getBytes());
            JOptionPane.showMessageDialog(null, "Hash saved to " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static String computeSHA1Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = sha1.digest(input.getBytes());

        // Convert bytes to hexadecimal representation
        StringBuilder hash = new StringBuilder();
        for (byte b : hashBytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }
}
