/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 16/10/2021
 * Handles the registering and notifying of observers
 ***/
package org.program_api;

import java.util.ArrayList;

public class TextEditorEventSource
{
    private ArrayList<TextModificationObserver> textChangeObservers;

    public TextEditorEventSource()
    {
        textChangeObservers = new ArrayList<>();
    }

    public void addTextObserver(TextModificationObserver obs)
    {
        textChangeObservers.add(obs);
    }

    // Used to notify observers when the text has been updated
    public void notifyTextObservers(String text)
    {
        for(TextModificationObserver obs : textChangeObservers)
        {
            obs.receiveTextCallBack(text);
        }
    }

}
