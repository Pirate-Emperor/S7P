import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HillCipherGUI {
    public static int mod = 29;
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hill Cipher Encryption and Decryption");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton encryptButton = new JButton("Encrypt (Hill Cipher)");
        JButton decryptButton = new JButton("Decrypt (Hill Cipher)");

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

            String key = JOptionPane.showInputDialog("Enter the Key for Hill Cipher:");
            int[][] keyMatrix = generateKeyMatrix(key);

            String cleanedContent = cleanInput(content);
            String encrypted = encryptHillCipher(cleanedContent, keyMatrix);
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

            String key = JOptionPane.showInputDialog("Enter the Key for Decryption (Hill Cipher):");
            int[][] keyMatrix = generateKeyMatrix(key);
            int[][] inverseKeyMatrix = invertKeyMatrix(keyMatrix);

            String decrypted = decryptHillCipher(content, inverseKeyMatrix);
            Files.write(Paths.get(outputFileName), decrypted.getBytes());

            JOptionPane.showMessageDialog(null, "Decryption Completed! Decrypted file saved as " + outputFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static String cleanInput(String text) {
        return text.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    public static int[][] generateKeyMatrix(String key) {
        key = cleanInput(key);
        int matrixSize = (int) Math.sqrt(key.length());

        int[][] keyMatrix = new int[matrixSize][matrixSize];
        int index = 0;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                keyMatrix[i][j] = key.charAt(index) % 65;
                index++;
            }
        }
        return keyMatrix;
    }

    public static String encryptHillCipher(String text, int[][] keyMatrix) {
        int n = keyMatrix.length;
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < text.length(); i += n) {
            int[] vector = new int[n];
            for (int j = 0; j < n; j++) {
                if (i + j < text.length()) {
                    vector[j] = text.charAt(i + j) % 65;
                } else {
                    vector[j] = 'X' % 65;  
                }
            }
            int[] encryptedVector = multiplyMatrixVector(keyMatrix, vector);
            for (int val : encryptedVector) {
                result.append((char) ((val % mod) + 65));
            }
        }
        return result.toString();
    }

    public static String decryptHillCipher(String text, int[][] inverseKeyMatrix) {
        int n = inverseKeyMatrix.length;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += n) {
            int[] vector = new int[n];
            for (int j = 0; j < n; j++) {
                vector[j] = text.charAt(i + j) % 65;
            }
            int[] decryptedVector = multiplyMatrixVector(inverseKeyMatrix, vector);
            for (int val : decryptedVector) {
                result.append((char) ((val % mod) + 65));
            }
        }
        return result.toString();
    }

    public static int[] multiplyMatrixVector(int[][] matrix, int[] vector) {
        int n = matrix.length;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = 0;
            for (int j = 0; j < n; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }

    public static int powMod(int a, int p, int m){
        int resu=1;
        int base=a;
        while(p>0)
        {
            if (p%2==1)
            {
                resu=(resu*base)%m;
            }
            p/=2;
            base=(base*base)%m;
        }
        return resu;
    }
    public static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1; 
    }
    
    
    public static int determinant(int[][] matrix) {
        int det = matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) -
                  matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]) +
                  matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
        return det;
    }
    
    public static int[][] cofactorMatrix(int[][] matrix) {
        int[][] cofactor = new int[3][3];
        cofactor[0][0] = matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1];
        cofactor[0][1] = -(matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]);
        cofactor[0][2] = matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0];
        cofactor[1][0] = -(matrix[0][1] * matrix[2][2] - matrix[0][2] * matrix[2][1]);
        cofactor[1][1] = matrix[0][0] * matrix[2][2] - matrix[0][2] * matrix[2][0];
        cofactor[1][2] = -(matrix[0][0] * matrix[2][1] - matrix[0][1] * matrix[2][0]);
        cofactor[2][0] = matrix[0][1] * matrix[1][2] - matrix[0][2] * matrix[1][1];
        cofactor[2][1] = -(matrix[0][0] * matrix[1][2] - matrix[0][2] * matrix[1][0]);
        cofactor[2][2] = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        return cofactor;
    }

    
    public static int[][] transpose(int[][] matrix) {
        int[][] transposed = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                transposed[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }

    
    public static int[][] invertMatrix(int[][] matrix) {
        int determinant = determinant(matrix) % mod;
        if (determinant < 0) {
            determinant += mod;
        }

        int detInverse = powMod(determinant, mod-2, mod); // Base Mod value is 29 as the mod needs to prime to have inverse of the determinant

        
        int[][] cofactorMatrix = cofactorMatrix(matrix);
        int[][] adjugateMatrix = transpose(cofactorMatrix);

        
        int[][] inverseMatrix = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                inverseMatrix[i][j] = (adjugateMatrix[i][j] * detInverse) % mod;
                if (inverseMatrix[i][j] < 0) {
                    inverseMatrix[i][j] += mod;
                }
            }
        }
        return inverseMatrix;
    }

    public static int[][] invertKeyMatrix(int[][] matrix) {
        int[][] inverseMatrix = invertMatrix(matrix);
        return inverseMatrix; 
    }
}
