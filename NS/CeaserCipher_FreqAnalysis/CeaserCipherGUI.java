import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CeaserCipherGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ceaser Cipher Encryption and Decryption");
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton encryptButton = new JButton("Encrypt (Ceaser Cipher)");
        JButton decryptButton = new JButton("Decrypt (Ceaser Cipher)");
        JButton bruteForceButton = new JButton("Cryptanalysis - Brute Force");
        JButton freqAnalysisButton = new JButton("Cryptanalysis - Frequency Analysis");

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

        bruteForceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performBruteForceAttack();
            }
        });

        freqAnalysisButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performFrequencyAnalysis();
            }
        });

        JPanel panel = new JPanel();
        panel.add(encryptButton);
        panel.add(decryptButton);
        panel.add(bruteForceButton);
        panel.add(freqAnalysisButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void performEncryption() {
        try {
            String inputFileName = "Plaintext.txt";
            String outputFileName = "Cipher.txt";
            String content = new String(Files.readAllBytes(Paths.get(inputFileName)));
            int key = Integer.parseInt(JOptionPane.showInputDialog("Enter the Key for Ceaser Cipher (1-25):"));
            String encrypted = encryptCeaserCipher(content, key);
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
            int key = Integer.parseInt(JOptionPane.showInputDialog("Enter the Key for Decryption (1-25):"));
            String decrypted = decryptCeaserCipher(content, key);
            Files.write(Paths.get(outputFileName), decrypted.getBytes());
            JOptionPane.showMessageDialog(null, "Decryption Completed! Decrypted file saved as " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static void performBruteForceAttack() {
        try {
            String inputFileName = "Cipher.txt";
            String content = new String(Files.readAllBytes(Paths.get(inputFileName)));
            StringBuilder results = new StringBuilder();
            int probableKey = -1;
            int maxReadableScore = Integer.MIN_VALUE;
    
            for (int key = 1; key < 26; key++) {
                String decrypted = decryptCeaserCipher(content, key);
                int readableScore = calculateReadableScore(decrypted); 
    
                results.append("Key ").append(key).append(": ").append(decrypted).append("\n");
    
                if (readableScore > maxReadableScore) {
                    maxReadableScore = readableScore;
                    probableKey = key;
                }
            }
    
            results.append("\nProbable Key: ").append(probableKey).append("\n");
    
            JTextArea textArea = new JTextArea(results.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new java.awt.Dimension(1600, 900));
            JOptionPane.showMessageDialog(null, scrollPane, "Brute Force Attack Results", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static int calculateReadableScore(String text) {
        String[] commonWords = {"the", "be", "to", "of", "and", "a", "in", "that", "have", "I"};
        int score = 0;
        for (String word : commonWords) {
            if (text.contains(word)) {
                score++;
            }
        }
        return score;
    }
    
    // Fact 1: 'e' is the most frequent letter in the English language
    public static void performFrequencyAnalysis() {
        try {
            String inputFileName = "Cipher.txt";
            String content = new String(Files.readAllBytes(Paths.get(inputFileName)));
            Map<Character, Integer> freqMap = new HashMap<>();
    
            for (char c : content.toCharArray()) {
                if (Character.isLetter(c)) {
                    c = Character.toLowerCase(c);
                    freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
                }
            }
    
            List<Map.Entry<Character, Integer>> list = new ArrayList<>(freqMap.entrySet());
            list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
    
            char mostFrequentChar = list.get(0).getKey();
            char assumedE = 'e';
            int probableKey = mostFrequentChar - assumedE;
    
            if (probableKey < 0) {
                probableKey += 26;
            }
    
            StringBuilder results = new StringBuilder();
            results.append("Most Frequent Character: ").append(mostFrequentChar).append("\n");
            results.append("Probable Key (Based on Frequency Analysis): ").append(probableKey).append("\n");
    
            JTextArea textArea = new JTextArea(results.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new java.awt.Dimension(1600, 900));
            JOptionPane.showMessageDialog(null, scrollPane, "Frequency Analysis Results", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
    

    public static String encryptCeaserCipher(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                result.append((char) ((ch - base + key) % 26 + base));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static String decryptCeaserCipher(String text, int key) {
        return encryptCeaserCipher(text, 26 - key);
    }
}
