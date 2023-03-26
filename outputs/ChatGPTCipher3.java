
//import the necessary packages
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;

public class ChatGPTCipher3 {

	public static void main(String[] args) {

		//declare a String variable and assign it the value of the user's text entered with a JOptionPane
		String userText = JOptionPane.showInputDialog("Please enter a text");

		//declare an int variable and assign it the value of the number entered with a JOptionPane
		int n = Integer.parseInt(JOptionPane.showInputDialog("Please enter an integer n"));

		//create an empty StringBuilder
		StringBuilder sb = new StringBuilder();

		//iterate through the text and append the cipher-text character to the StringBuilder
		for (int i = 0; i < userText.length(); i++) {
			char character = userText.charAt(i);
			if (Character.isLetter(character)) {
				char newCharacter = (char) (character + n);
				sb.append(newCharacter);
			} else {
				sb.append(character);
			}
		}

		//declare a String and assign it the value of the StringBuilder
		String cipherText = sb.toString();

		//declare a String to hold the user's input
		String userInput;

		//present the user with two options: to have the cipher-text displayed or saved to a file
		userInput = JOptionPane.showInputDialog(null, "Would you like the cipher-text displayed or saved to a file? Type 'display' or 'save'");

		//if the user chooses to display the cipher-text, a JOptionPane is used to print it out
		if (userInput.equals("display")) {
			JOptionPane.showMessageDialog(null, cipherText);
		}

		//if the user chooses to save the cipher-text, a FileWriter is used to write and save the text to a file
		if (userInput.equals("save")) {
			try {
				FileWriter fw = new FileWriter("ciphertext.txt");
				fw.write(cipherText);
				fw.close();
				JOptionPane.showMessageDialog(null, "Cipher-text was saved to file 'ciphertext.txt'");
			} catch (IOException e) {
				System.out.println("An error occurred");
				e.printStackTrace();
			}
		}
	}
}