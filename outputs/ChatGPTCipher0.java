
import javax.swing.JOptionPane;
import java.io.File;
import java.io.PrintWriter;

public class ChatGPTCipher0 {
    
    public static void main(String args[]) throws Exception {
        
        // Prompt user for input text and store it in variables
        String inputText = JOptionPane.showInputDialog("Please enter the text to cipher:");
        String inputKey = JOptionPane.showInputDialog("Please enter a numerical rotation for the key:");
        int key = Integer.parseInt(inputKey);
        
        // Perform ciphers on input text
        String outputText = cipherText(inputText, key);
        
        // Ask user if they want the cipher displayed or saved to a file
        String outputChoice = JOptionPane.showInputDialog("Would you like to display to the cipher as text (1), or save to a file (2)?");
        int outChoice = Integer.parseInt(outputChoice);
        
        // Execute corresponding action
        switch (outChoice) {
            
            // Display cipher on screen
            case 1:
                JOptionPane.showMessageDialog(null, outputText);
                break;
                
            // Save output to a file
            case 2:
                JOptionPane.showMessageDialog(null, "Saving to new file...");
                File outFile = new File("output.txt");
                PrintWriter writer = new PrintWriter(outFile);
                writer.println(outputText);
                writer.close();
                break;
                
            // Default statement
            default:
                JOptionPane.showMessageDialog(null, "Incorrect choice. Please try again.");
                break;
        }
    }
    
    // Function to perform rotation cipher
    public static String cipherText(String inputText, int key) {
        
        String outputText = "";
        int len = inputText.length();
        
        for (int i = 0; i < len; i++) {
            
            // Check if char is letter
            char c = inputText.charAt(i);
            if (c >= 'a' && c <= 'z') {
                
                // Perform rotation on lower-case char
                c = (char) (c + key);
                if (c > 'z') {
                    c = (char) (c - 'z' + 'a' - 1);
                }
                outputText += c;
                
            } else if (c >= 'A' && c <= 'Z') {
                
                // Perform rotation on upper-case char
                c = (char) (c + key);
                if (c > 'Z') {
                    c = (char) (c - 'Z' + 'A' - 1);
                }
                outputText += c;
            } else {
                
                // Other characters remain unchanged
                outputText += c;
            }
        }
        return outputText;
    }
}