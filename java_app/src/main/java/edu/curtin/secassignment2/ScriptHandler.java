/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 16/10/2021
 * Handles the reading of scripts using the python interpreter through jython
 ***/
package edu.curtin.secassignment2;

import org.program_api.*;
import org.python.core.*;
import org.python.util.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;


public class ScriptHandler
{
    private PythonInterpreter interpreter;

    public ScriptHandler()
    {
        interpreter = new PythonInterpreter();
    }

    /**
     * Reads the script from a text file and execute it
     * @param script - the path of the location of the script
     * @param api - api to be used with the script
     */
    public void runScript(String script, API api)
    {
        ResourceBundle bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());
        interpreter.set("api", api); //Bind the api to the script environment
        try
        {   //Read script into plain text
            script = new String(Files.readAllBytes(Paths.get(script)), "UTF-8");
            interpreter.exec(script);
        }
        catch (PyException | IOException e)
        {
            System.out.println(bundle.getString("error") + e.getMessage());
        }
    }
}
