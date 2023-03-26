
import java.io.FileWriter;  // Importing libraries for file writing
import java.io.IOException;
import javax.swing.JOptionPane;    // Importing libraries for JOptionPanes

public class ChatGPTCipher2 {
    public static void main(String[] args) throws IOException {
        String message = JOptionPane.showInputDialog("Enter your message: ");  // Asking for user input
        int n = Integer.parseInt(JOptionPane.showInputDialog("Enter a number for the rotation cipher: "));  // Asking for user's rotation cipher
         
        String cipherText = GPTCipher(message, n);  // Applying the GPTCipher encryption to the user's input
         
        int choice = JOptionPane.showConfirmDialog(null, "Do you want to save the output to a file?", "Save option", JOptionPane.YES_NO_OPTION); // Giving user choice to save data
        
        // if user chooses yes, their ciphertext is saved to a file
        if (choice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Data Saved");  // Message to show user the data is saved
            String fileName = JOptionPane.showInputDialog("Enter the filename: "); // Ask user for filename
            FileWriter fw = new FileWriter(fileName + ".txt");  // Writing data to text file
            fw.write("Rotation Cipher: " + n + "\nPlaintext: " + message + "\nCipherText: " + cipherText);
            fw.close();  // Close file writer to save data
        } else if (choice == JOptionPane.NO_OPTION) {  // if user chooses no, their ciphertext will be displayed
            JOptionPane.showMessageDialog(null, cipherText);  // Display cipher text
        }
    }
     
    // Method to apply and return n-rotation cipher
    public static String GPTCipher (String text, int n) {
        StringBuilder cipherText = new StringBuilder();
        /*Taking each character in the string and converting it to its corresponding ASCII code value. 
        If the code is within the range of the lowercase alphabet,
        the character is changed by the n value and returned*/
        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i); 
            int asciiValue = (int) letter;  // converting character to ascii code
            if (asciiValue >= 97 && asciiValue <= 122) {  // if ascii code is within lowercase alphabet range
                letter = (char) (asciiValue + n);  // changing letter by n value
            }
            cipherText.append(letter);  // adding letter to cipherText string
		}
        return cipherText.toString();  // return the cipherText string
    }
     
}