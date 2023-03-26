
//This is the ChatGPTCipher7 java program

import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;

public class ChatGPTCipher7 {
    public static void main(String[] args) {
        //Ask user for the text to be ciphered
        String text = JOptionPane.showInputDialog("Enter the text to be ciphered:");
        
        //Ask user for the rotation key number
        int key = Integer.parseInt(JOptionPane.showInputDialog("Enter the key number (1-26):"));
        
        //Validate the entered key number
        while (key < 1 || key > 26) {
            key = Integer.parseInt(JOptionPane.showInputDialog("Invalid key number. Please enter a key number between 1-26:"));
        }
        
        //Create cipher-text by looping through each character in the entered text
        String cipherText = "";
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            
            //Ignoring characters that are not part of the alphabet, rotate the character of the alphabet
            if (c >= 'a' && c <= 'z') {
                c = (char) (c + key);
                if (c > 'z') {
                    c = (char) (c - 26);
                }
            } else if (c >= 'A' && c <= 'Z') {
                c = (char) (c + key);
                if (c > 'Z') {
                    c = (char) (c - 26);
                }
            }
            
            //Append characters to the cipher-text
            cipherText += c;
        }
        
        //Introduce the output options
        Object[] outputOptions = {"Display on Screen", "Save to File"};
        
        //Ask user to select output option
        int outputOption = JOptionPane.showOptionDialog(null, "Choose an output option", "Output Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, outputOptions, outputOptions[0]);
        
        //If selected output is Display on Screen, show the cipher-text on screen
        if (outputOption == 0) {
            JOptionPane.showMessageDialog(null, cipherText);
        //If selected output is Save to File, write cipher-text to file
        } else if (outputOption == 1) {
            try {
                String filename = JOptionPane.showInputDialog("Please enter the file name:");
                //Write to file, naming it as the entered file name
                FileWriter fileWriter = new FileWriter(filename + ".txt");
                fileWriter.write(cipherText);
                fileWriter.close();
                JOptionPane.showMessageDialog(null, "File saved.");
            //Print error message for IOException
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "An error occurred saving the file:" + e);
            }
        }
    }
}