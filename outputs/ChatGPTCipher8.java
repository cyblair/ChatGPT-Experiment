
import javax.swing.JOptionPane; //import library for JOptionPanes
import java.io.BufferedWriter; //import library for BufferedWrite
import java.io.FileWriter; //import library for FileWriter

public class ChatGPTCipher8 {

public static void main(String[] args) {

    // Prompt the user for a text and an integer n
    String text = JOptionPane.showInputDialog("Please enter a text:");
    String nStr = JOptionPane.showInputDialog("Please enter an integer n:");
    // Convert the n from a string to an int
    int n = Integer.parseInt(nStr);

    // Create a new string called cipheredStr
    String cipheredStr = "";

    // Loop through each character of the text
    for (int i = 0; i < text.length(); i++) {
        // Get current character
        char c = text.charAt(i);
        // Check if the character is an alphabetic character
        if (Character.isAlphabetic(c)) {
            int asciiCode = c;
            // Rotation cipher
            asciiCode -= n;
            // Get corresponding character using ASCII codes
            c = (char) asciiCode;
        }
        // Add character to the ciphered string
        cipheredStr += c;
    }

    // Ask the user if they want to see the ciphered text or save it to a file
    String[] options = { "See Text", "Save to File" };
    int showSave = JOptionPane.showOptionDialog(null, "Choose an option", "ChatGPTCipher8", 
        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

    // IF the user wants to see the ciphered text
    if (showSave == 0) {
        System.out.println("Ciphered Text: \n" + cipheredStr);
    }
    // Else, if the user wants to save to a file
    else {
        try {
            // Prompt the user to enter the file name
            String fileName = JOptionPane.showInputDialog("Please enter a file name:");
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(cipheredStr);
            writer.newLine();
            writer.close();
        // Catch any errors
        } catch (Exception e) {
            System.out.println("Error writing to file");
        }
    }
    // Display message to signal the end of the program
    JOptionPane.showMessageDialog(null, "End of program");
  }

}