/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 16/10/2021
 * Adds a button to the gui which when pressed prompts the user for the text to
 * be searched for, and highlights the text if found after the current caret position
 */
package texteditor;

import org.program_api.*;

import java.text.Normalizer;

public class FindPlugin implements TextEditorPlugin,  ButtonPlugin
{
    private API api;
    private String name;

    @Override
    public void start(API api)
    {
        this.api = api;
        this.name = "Find";
        api.registerButton("Find", this);
        api.registerFKey("F3", this);
    }

    @Override
    public String getName()
    {
        return name;
    }


    /**
     * The callback for the button, asks for user for the text to be searched for
     * through the api, and highlights it if it is found
     */
    @Override
    public void buttonCallback()
    {
        String searchTxt = api.askForInput("Enter Text to Find");
        String allTxt = api.receiveText();
        int caretPos = api.caretPosition();

        if (allTxt.length() > 0 && searchTxt != null)
        {
            String textFromCaret = allTxt.substring(caretPos);
            String allTxtNormalised = Normalizer.normalize(textFromCaret, Normalizer.Form.NFKC);
            String searchNormalised = Normalizer.normalize(searchTxt, Normalizer.Form.NFKC);

            int subStrIndex = allTxtNormalised.indexOf(searchNormalised);
            if (subStrIndex != -1) //The search term was found in the normalised text
            {
                int beginIdx = subStrIndex + caretPos;
                int endIdx = subStrIndex + caretPos + searchTxt.length();
                api.highlightText(beginIdx, endIdx);
            }
        }
    }
}
