
// Import the JOptionPanes 
import javax.swing.JOptionPane;

// Define the ChatGPTCipher9 class
public class ChatGPTCipher9 {
	
	// Define the main method
	public static void main(String[] args) {

		// Declare and define the input string
		String initialText = JOptionPane.showInputDialog("Please enter a text:"); 
		
		// Declare and define the integer 'n' 
		int n = Integer.parseInt(JOptionPane.showInputDialog("Please enter an integer:")); 
		
		// Declare the cipher-text string
		String cipherText;

		// Create the caesarCipher object 
		CaesarCipher cc = new CaesarCipher();
		
		// Call the caesarCipher method to generate the cipher-text
		cipherText = cc.caesarCipher(initialText, n); 
		
		// Ask the user whether they wanted to display or save the cipher-text
		Object[] options = {"Display", "Save to File"}; 
		int choice = JOptionPane.showOptionDialog(null, "What would you like to do with the cipher-text?", 
											"Choose an Option", 
											JOptionPane.DEFAULT_OPTION, 
											JOptionPane.QUESTION_MESSAGE, 
											null, 
											options, 
											options[0]); 
		
		// Based on the user input, print or save the cipher-text
		if(choice == 0){ 
			// Display the cipher-text
			JOptionPane.showMessageDialog(null, "Your cipher-text is:\n" + cipherText); 
			
		}else if (choice == 1){ 
			// Save the cipher-text to a file 
			System.out.println("Saving cipher-text to file...");
			SaveFile sf = new SaveFile(); 
			sf.save(cipherText);
			System.out.println("Cipher-text saved!");
		}
	}
} 

// Define the CaesarCipher class 
class CaesarCipher {
	
	// Define the caesarCipher method 
	public String caesarCipher(String text, int n) {
		
		// Declare and initialize an empty string 
		String cipherText = ""; 
		
		// Loop through the original string 
		for(int i = 0; i < text.length(); i++) {
			
			// Check if the current character is an alphabet
			if (Character.isAlphabetic(text.charAt(i))) {
				
				// Get the character code 
				int c = (int) text.charAt(i); 
				
				// To make sure we wrap around the end of the alphabet
				if (Character.isLowerCase(text.charAt(i))) {
					if (c + n > 'z') 
						c = 'a' +  n - ('z' - c + 1);
					else
						c += n;
				}
				else if (Character.isUpperCase(text.charAt(i))) {
					if (c + n > 'Z') 
						c = 'A' + n - ('Z' - c + 1);
					else
						c += n;
				}
				
				// Append the new character code to the cipher-text string
				cipherText += (char) c; 	
			} else {
				// If the character is not an alphabet, 
				// it will just be copied over to the cipher-text string
				cipherText += text.charAt(i);
			}
		}
		
		// Return the cipher-text
		return cipherText; 
	}
	
}

// Define the SaveFile class
class SaveFile{
	
	// Define the save method
	public void save(String cipherText) {

		// Declare and define the file path
		String filePath = JOptionPane.showInputDialog("Please enter the file path:"); 

		// Write the file, encoded with UTF-8
		try{ 
				BufferedWriter outWriter = new BufferedWriter(new OutputStreamWriter( 
															new FileOutputStream(filePath),
															"UTF-8")); 
				outWriter.write(cipherText); 
				outWriter.close();
				
		 // In case of an error, catch the exception and print an error message	
		} catch (IOException e){ 
			System.out.println("Error writing to file");
		} 
	} 
}