import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DigitalSignatureGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Digital Signature - RSA Asymmetric Encryption");
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton signButton = new JButton("Sign Message (Digital Signature)");
        JButton verifyButton = new JButton("Verify Signature");

        signButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performSigning();
            }
        });

        verifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performVerification();
            }
        });

        JPanel panel = new JPanel();
        panel.add(signButton);
        panel.add(verifyButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void performSigning() {
        try {
            // Load the plaintext message
            String inputFileName = "Plaintext.txt";
            String content = new String(Files.readAllBytes(Paths.get(inputFileName)));

            // Generate RSA KeyPair (Private and Public Keys)
            KeyPair keyPair = generateRSAKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // Sign the message
            String signature = signMessage(content, privateKey);

            // Save the signature and public key for verification later
            Files.write(Paths.get("Signature.txt"), signature.getBytes());
            Files.write(Paths.get("PublicKey.txt"), publicKey.getEncoded());

            JOptionPane.showMessageDialog(null, "Message Signed and Signature Saved!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static void performVerification() {
        try {
            // Load the message and signature
            String inputFileName = "Plaintext.txt";
            String content = new String(Files.readAllBytes(Paths.get(inputFileName)));

            String signature = new String(Files.readAllBytes(Paths.get("Signature.txt")));
            byte[] publicKeyBytes = Files.readAllBytes(Paths.get("PublicKey.txt"));

            // Convert the public key back from byte array
            PublicKey publicKey = KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            // Verify the signature
            boolean isValid = verifySignature(content, signature, publicKey);

            if (isValid) {
                JOptionPane.showMessageDialog(null, "Signature Verified Successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Signature Verification Failed.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    // Method to generate RSA Key Pair (Private and Public Keys)
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // 2048 bit key length for RSA
        return keyPairGenerator.generateKeyPair();
    }

    // Method to sign the message using the private key
    public static String signMessage(String message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes); // Encode signature in Base64
    }

    // Method to verify the signature using the public key
    public static boolean verifySignature(String message, String signatureStr, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
        return signature.verify(signatureBytes); // Returns true if signature matches
    }
}
