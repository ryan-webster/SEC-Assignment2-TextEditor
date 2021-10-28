/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 16/10/2021
 * Entry point for the program, attempts to parse the keymap file and set up
 * key combinations, as well as initialise objects
 ***/
package edu.curtin.secassignment2;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;
import edu.curtin.parser.*;

import org.program_api.TextEditorEventSource;

public class TextEditor extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        var localeString = getParameters().getNamed().get("locale");
        if(localeString != null) //Set the locale
        {
            String[] parts = localeString.split("-");
            Locale.setDefault(new Locale(parts[0], parts[1]));
        }

        ArrayList<KeyCombination> combos = new ArrayList<>();
        try
        {  //Parse the keymap file
            MyParser parser = new MyParser(new FileInputStream("keymap"));
            combos = parser.dsl();
        }
        catch(ParseException e)
        {
            System.out.println("Parsing error, no key combinations loaded");
        }
        catch (FileNotFoundException e2)
        {
            System.out.println("Keymap file not found");
        }

        TextEditorEventSource events = new TextEditorEventSource();
        PluginHandler ph = new PluginHandler();
        ScriptHandler handler = new ScriptHandler();
        FileIO fileIO = new FileIO();
        LoadSaveUI loadsave = new LoadSaveUI(fileIO);
        UI ui = new UI(primaryStage, events, ph, handler, combos, loadsave);

        ui.display();
    }
}
