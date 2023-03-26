
//Importing packages for JOptionPanes, file output
import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ChatGPTCipher4 {

//Main program
    public static void main (String[] args){

//Declaring variables used in the program
        String inputText;
        String outputText = "";
        String fileName;
        int n;

//Opening a dialog window for text input
        inputText = JOptionPane.showInputDialog("Please enter the text that you would like to be converted: ");

//Opening a dialog window for integer input
        n = Integer.parseInt(JOptionPane.showInputDialog("Please enter the integer n (1-25) for the cipher: "));

//For-loop to run the program through each character of the input text
        for(int i = 0; i < inputText.length(); i++){

//Checking if the character at the specific index is a-z or A-Z
            if(Character.isLetter(inputText.charAt(i))){

//Rotating the character by n positions, using the index.
                int charIndex = inputText.charAt(i);
                charIndex +=n;
                outputText += (char) charIndex;
            }
        else {

//Adding non-letter values to the output text
                outputText += inputText.charAt(i);
            }
        }

//Opening dialog to decide action
        int actionChoice = JOptionPane.showOptionDialog(null, "Choose one of the following actions", "Action Choice",
                JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[] {"Display", "Save to file"}, 0);

//If statement for displaying results	
        if(actionChoice == 0){
            JOptionPane.showMessageDialog(null, "The cipher-text is: \n" + outputText, "Cipher-text",JOptionPane.INFORMATION_MESSAGE);
        }

//If statement for saving result to file				
        else if(actionChoice == 1){

//Opening a dialog window for file name
            fileName = JOptionPane.showInputDialog("Please enter the file name with the appropriate file extension: ");

//Writing data to the file
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(outputText);
                writer.close();

//Confirming the write was successful
                JOptionPane.showMessageDialog(null, "Data successfully written to: " + fileName);
            }

//Catching IOExceptions	
            catch(IOException exception){
                JOptionPane.showMessageDialog(null,"Error! Could not write data to the file");
            }
        }
    }
}