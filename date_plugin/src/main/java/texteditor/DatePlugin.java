/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 16/10/2021
 * Adds a button to the gui which when pressed inserts the current date and time
 ***/
package texteditor;

import org.program_api.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DatePlugin implements TextEditorPlugin, ButtonPlugin
{
    private String name;
    private API api;

    @Override
    public void start(API api)
    {
        this.api = api;
        this.name = "Date";
        api.registerButton(name, this);
    }


    @Override
    public String getName()
    {
        return name;
    }


    /**
     * The callback for the button when pressed, inserts the current date and
     * at the caret position
     * implemented from the ButtonPlugin interface
     */
    @Override
    public void buttonCallback()
    {
        ZonedDateTime time = ZonedDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        String timeFormatted = dtf.format(time);
        api.insertTextCaret(timeFormatted);
    }
}
