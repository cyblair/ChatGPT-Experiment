
//Program title
public class ChatGPTCipher6 {

    //Main method to run when launched
    public static void main(String[] args) {

        //Create String variable to store the plaintext input
        String plaintext = JOptionPane.showInputDialog(
                "Please enter the plaintext you wish to be put through a cipher-rotation");

        //Repeat the above step until the user wishes to stop
        while (plaintext != null){

            //Create an integer to store the cipher-rotation wanted by the user
            int shiftValue = Integer.parseInt(JOptionPane.showInputDialog(
                    "Please enter the cipher-rotation number you would like to use"));

            //Ensure the rotation is between 0 and 25
            while (shiftValue < 0 || shiftValue > 25) {
                shiftValue = Integer.parseInt(JOptionPane.showInputDialog(
                        "Error: the rotation number must be between 0 and 25, please enter the desired cipher-rotation again"));
            }

            //Convert the plaintext to characters and store each character in an array
            char[] messageChars = plaintext.toCharArray();

            //For each character in the array of characters
            for (int i = 0; i < messageChars.length; i++) {

                //If the character is alphabetical...
                if (Character.isAlphabetic(messageChars[i])) {

                    //...perform a cipher rotation on the character
                    messageChars[i] = (char) (messageChars[i] + shiftValue);

                    //If the character crosses past the end of the alphabet, loop back to the beginning
                    if (messageChars[i] > 'z') {
                        messageChars[i] = (char) (messageChars[i] - 26);
                    }
                }
            }

            //Convert the array of rotated characters to a String
            String rotatedString = String.valueOf(messageChars);

            //Offer the user a choice between displaying the rotated text on-screen, or saving it to a file
            String[] options = {"Display Text", "Save to File"};
            int showOrSave = JOptionPane.showOptionDialog(null,
                    "What do you want to do with the cipher-rotated text?", "GPTCipher6",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            //If the user wishes to display the rotated text on-screen, output the rotated text in a dialog box
            if (showOrSave == 0) {
                JOptionPane.showMessageDialog(null, rotatedString);
            }

            //If the user wishes to save to file, browse to a location and save the rotated text
            else if (showOrSave == 1) {
                JFileChooser chooser = new JFileChooser();
                int filePath = chooser.showSaveDialog(null);

                String fileName = chooser.getSelectedFile().getAbsolutePath();

                try {
                    Files.write(Paths.get(fileName), rotatedString.getBytes());
                    System.out.println("Saved!");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Repeat the prompt for user input until the user wishes to stop
            plaintext = JOptionPane.showInputDialog("Please enter the plaintext you wish to be put through a cipher-rotation");
        }
    }
}