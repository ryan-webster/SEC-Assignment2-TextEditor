/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 18/10/2021
 * Class that deals with the saving and loading of text files.
 */
package edu.curtin.secassignment2;

import java.io.*;

public class FileIO
{
    /**
     * Read the file with the specified encoding, and return the contents of the file
     */
    public String load(File file, String encoding) throws IOException
    {
        StringBuilder text = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), encoding);
        try (BufferedReader br = new BufferedReader(isr))
        {
            int c = 0;
            while ((c = br.read()) != -1)
            {
                text.append((char)c);
            }
        }
        return text.toString();
    }

    /**
     * Save the provided string to a file with the specified encoding
     */
    public void save(File file, String text, String encoding) throws  IOException
    {
        OutputStreamWriter osr = new OutputStreamWriter(new FileOutputStream(file), encoding);
        try(BufferedWriter bw = new BufferedWriter(osr))
        {
            bw.write(text);
        }
    }
}
