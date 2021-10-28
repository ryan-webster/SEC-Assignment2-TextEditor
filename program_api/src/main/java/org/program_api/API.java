/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 15/10/2021
 * Defines the available behaviours for a plugin/script
 */
package org.program_api;

public interface API
{
    // For plugins to modify the text that is being edited
    void updateText(String text);

    // Provide the name of a button and the associated callback when pressed
    void registerButton(String name, ButtonPlugin callback);

    // Highlight a specific range of characters within the text
    void highlightText(int start, int end);

    // Insert text at the caret position
    void insertTextCaret(String text);

    // Plugins can register themselves as a function when the provided F key is pressed
    void registerFKey(String key, ButtonPlugin callBack);

    // Ask the users for input using a dialog box
    String askForInput(String title);

    // Plugins can receive the entire text field
    String receiveText();

    // Retrieve the current position of the caret
    int caretPosition();

    // Add the name of a script to the list of loaded scripts
    void addScript(String name);

    // Plugins can be notified whenever the text area is edited
    void addTextObserver(TextModificationObserver obs);

//    void notifyTextObservers(String text);
//    void notifyFKeyObservers(String key);
}
