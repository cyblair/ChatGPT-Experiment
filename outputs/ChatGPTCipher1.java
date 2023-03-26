
import javax.swing.JOptionPane;
import java.io.*;

public class ChatGPTCipher1 {
    // A main method to act as the starting point of the program
    public static void main(String[] args) {
        // Prompt the user to enter their text
        String textInput = JOptionPane.showInputDialog("Please enter your text here:");
        
        // Prompt the user for the rotation int
        String rotationInput = JOptionPane.showInputDialog("Please enter your rotation int here:");
        int rotationInt = Integer.parseInt(rotationInput);
        
        // Call on the rot13 method to cipher the text
        String cipherText = rot13(textInput, rotationInt);

        // Set up options array for JOptionPane
        Object[] options = {"Display Cipher-Text", "Save to File"};

        // Present the user with the options
        int userInput = JOptionPane.showOptionDialog(null, cipherText, "Choose an Option", 
                                                JOptionPane.DEFAULT_OPTION, 
                                                JOptionPane.PLAIN_MESSAGE,
                                                null, 
                                                options, 
                                                options[0]);

        // Choose to display the cipher-text 
        if (userInput == 0) {
            JOptionPane.showMessageDialog(null, cipherText);
        // Choose to save the cipher-text
        } else {
            JOptionPane.showMessageDialog(null, "Saving your cipher-text to a file now");
            
            // Get the current working directory
            String curDir = System.getProperty("user.dir");
            // Create the output path 
            String outputPath = curDir + "/encodeFile.txt";
            // Create the output stream to write data
            try {
                BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputPath));
                outputWriter.write(cipherText);
                outputWriter.close();
                // Informs the user that the file is created
                JOptionPane.showMessageDialog(null, "Cipher-text saved to a file in " + outputPath);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public static String rot13(String textInput, int rotationInt) {
        // Declare a new StringBuilder to build the cipher-text
        StringBuilder cipheredText = new StringBuilder();

        // Iterate through the characters in the text
        for (char c : textInput.toCharArray()) {
            // Push non-alphabetical letters through without alteration
            if (Character.isAlphabetic(c) == false) {
                cipheredText.append(c);
            } else {
                // Determine the numerical value of each alphabet letter
                int charValue = (int) c;
                charValue += rotationInt;

                // If the numerical value of the letter is larger than the length of the ASCII alphabet, subtract 26 to put the letter into
                // the beginning/end of the alphabet
                if (charValue > (int)'z') {
                    charValue -= 26; 
                }
                // If the numerical value of the letter is smaller than the length of the ASCII alphabet, add 26 to put the letter into
                // the beginning/end of the alphabet
                else if (charValue < (int)'a') {
                    charValue += 26;
                }
                // Append the letter to the cipher-text
                char cipherChar = (char) charValue;
                cipheredText.append(cipherChar);
            }
        }
        // Return the completed ciphered text
        return cipheredText.toString();
    }
}