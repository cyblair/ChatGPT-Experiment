
import javax.swing.*;
import java.io.*;
// import classes to allow use of JOptionPanes and save data to file

public class ChatGPTCipher5
{
   public static void main (String[]args)
   {
      JOptionPane.showMessageDialog(null, "Welcome to the ChatGPTCipher5 program!");
      // define 'text' and 'n' inputs from user
      String text = JOptionPane.showInputDialog(null, "Please enter your text below:");
      int n = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter a value of n below:"));
      // to create cipher-text, call the rotate() method
      String cipherText = rotate(text, n);
      // present user with option to either print cipher-text in window or save to file
      int option = JOptionPane.showConfirmDialog(null, "Would you like to save the cipher-text to a file? Press Yes or No");
      if (option == 0)
      {
         try
         {
            FileWriter fw = new FileWriter("output.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(cipherText);
            // write cipher-text to file
            bw.close();
            JOptionPane.showMessageDialog(null, "The cipher-text has been written to the file 'output.txt'");
         }
         catch(Exception e)
         {
            JOptionPane.showMessageDialog(null, e);
         }
      }
      else
      {
         JOptionPane.showMessageDialog(null, "The cipher-text is:\n" + cipherText);
      }
   }
   
   // method to rotate text by n characters
   private static String rotate(String s, int n)
   {
      // call toString() on input text to put characters in array
      char[] c = s.toCharArray();
      n = n % 26;
      // specify rotation range of 0 to 25
      for (int i=0; i<c.length; i++)
      {
         char ch = c[i];
         if (ch >= 'a' && ch <= 'z')
         // rotate only lowercase characters
         {
            c[i] = (char)((ch-'a'+n)%26 + 'a');
            // formula for rotating character by n
         }
         else if (ch >= 'A' && ch <= 'Z')
         // rotate only uppercase characters
         {
            c[i] = (char)((ch-'A'+n)%26 + 'A');
            // formula for rotating character by n
         }     
      }
      return new String(c);
   }
}