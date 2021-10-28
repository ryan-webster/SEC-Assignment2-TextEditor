/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 14/10/2021
 * Provides the user interface for the text editor
 */
package edu.curtin.secassignment2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.program_api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class UI implements API
{
    private TextArea textArea = new TextArea();
    private ToolBar toolBar;
    private Stage stage;

    private ObservableList<String> scripts;
    private TextEditorEventSource tes;
    private PluginHandler ph;
    private ScriptHandler sh;
    private LoadSaveUI loadsave;

    private HashMap<String, ButtonPlugin> fKeyEvents; //Stores the F Key and the callback when pressed
    private ArrayList<KeyCombination> combos; //Key combinations from the keymap file

    private ResourceBundle bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());

    public UI(Stage stage, TextEditorEventSource events, PluginHandler ph,
              ScriptHandler handler, ArrayList<KeyCombination> combos, LoadSaveUI loadsave)
    {
        this.stage = stage;
        this.ph = ph;
        this.tes = events;
        this.sh = handler;
        this.combos = combos;
        this.loadsave = loadsave;
        this.fKeyEvents = new HashMap<>();
        this.scripts = FXCollections.observableArrayList();
    }

    public void display()
    {
        stage.setTitle(bundle.getString("title"));
        stage.setMinWidth(800);

        // Create toolbar
        Button loadPlugin = new Button(bundle.getString("loadplugin_btn"));
        Button loadScript = new Button(bundle.getString("loadscript_btn"));
        Button loadBtn = new Button(bundle.getString("load_button"));
        Button saveBtn = new Button(bundle.getString("save_button"));

        toolBar = new ToolBar(loadBtn, saveBtn, loadPlugin, loadScript);

        // Subtle user experience tweaks
        toolBar.setFocusTraversable(false);
        toolBar.getItems().forEach(btn -> btn.setFocusTraversable(false));
        textArea.setStyle("-fx-font-family: 'monospace'"); // Set the font

        BorderPane mainBox = new BorderPane();
        mainBox.setTop(toolBar);
        mainBox.setCenter(textArea);
        Scene scene = new Scene(mainBox);        

        loadBtn.setOnAction(event -> //Load text from file
                textArea.setText(loadsave.loadFileMenu()));
        saveBtn.setOnAction(event -> //Save text area to file
                loadsave.saveFileMenu(textArea.getText()));

        loadScript.setOnAction(event -> loadScriptMenu());
        loadPlugin.setOnAction(event -> loadPluginMenu());

        // Notify text change listeners
        textArea.textProperty().addListener((object, oldValue, newValue) ->
                tes.notifyTextObservers(textArea.getText()));

        // Check if the key pressed matches any of the provided key combinations
        scene.setOnKeyPressed(this::checkKeyCombinations);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }


    /**
     * Gets the line number of the caret's current position
     * @return index of the beginning of the line
     */
    private int getLineNum()
    {
        int beginningOfLine = 0;
        for (int i = 0; i < textArea.getText().length(); i++)
        {
            //Reached the caret, return the position of the beginning of the line
            if(textArea.getCaretPosition() == i)
            {
                return beginningOfLine;
            }
            if(textArea.getText().charAt(i) == '\n')
            {
                beginningOfLine = i + 1;
            }
        }
        return beginningOfLine;
    }


    /**
     * Insert text at the given position
     * @param pos - index of the beginning of the current line
     * @param insertStr - text to be inserted at the beginning of the line
     */
    private void insertStartLine(int pos, String insertStr)
    {
        String allText = textArea.getText();
        int caretPos = textArea.getCaretPosition();
        String updated = allText.substring(0, pos) + insertStr + allText.substring(pos);
        textArea.setText(updated);
        textArea.positionCaret(caretPos + insertStr.length());
    }


    /**
     * Remove the provided text from the beginning of the current line
     * @param pos - the index of the start of the line
     * @param removeStr - string to be removed
     */
    private void removeStartLine(int pos, String removeStr)
    {
        String allText = textArea.getText();
        int caretPos = textArea.getCaretPosition();
        // If the line begins with the given text
        if(allText.startsWith(removeStr, pos))
        {
            //If the caret is at the beginning of the first line in the text area
            if(caretPos < removeStr.length())
            {
                String updated = allText.substring(removeStr.length());
                textArea.setText(updated);
                textArea.positionCaret(0);
            }
            else
            {
                String substring = allText.substring(caretPos - removeStr.length(), caretPos);
                String updated = allText.substring(0, pos) + allText.substring(pos + removeStr.length());
                textArea.setText(updated);
                //Handle case where the caret is located in the text that is to be removed
                if(substring.contains("\n"))
                {
                    int newLineBegin = allText.lastIndexOf("\n", caretPos);
                    // Set caret position to be the beginning of the line
                    textArea.positionCaret(newLineBegin + 1);
                }
                else
                {
                    textArea.positionCaret(caretPos - removeStr.length());
                }
            }
        }
    }


    /**
     * Provide the menu for loading a plugin, and display all currently loaded plugins
     */
    private void loadPluginMenu()
    {
        Button addBtn = new Button(bundle.getString("loadplugin_btn"));
        ToolBar toolBar = new ToolBar(addBtn);

        ArrayList<String> pluginList = ph.getPlugins();
        ObservableList<String> list = FXCollections.observableArrayList(pluginList);
        ListView<String> listView = new ListView<>(list);

        addBtn.setOnAction(event ->
                askForPlugin(list));

        BorderPane box = new BorderPane();
        box.setTop(toolBar);
        box.setCenter(listView);

        Dialog dialog = new Dialog();
        dialog.setTitle(bundle.getString("plugin_menu"));
        dialog.setHeaderText(bundle.getString("current_plugins"));
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }


    /**
     * Create and show the menu for loading a script,
     * and show the list of the currently loaded scripts
     */
    private void loadScriptMenu()
    {
        Button addBtn = new Button(bundle.getString("add_script"));
        ToolBar toolBar = new ToolBar(addBtn);

        ListView<String> listView = new ListView<>(this.scripts);
        addBtn.setOnAction(event ->
                loadScriptFile());

        BorderPane box = new BorderPane();
        box.setTop(toolBar);
        box.setCenter(listView);
        Dialog dialog = new Dialog();
        dialog.setTitle(bundle.getString("script_menu"));
        dialog.setHeaderText(bundle.getString("current_scripts"));
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }


    /**
     * Provide the menu for entering the name of the plugin
     * @param list - observable list of the names of the loaded plugins
     */
    private void askForPlugin(ObservableList<String> list)
    {
        var dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("plugin"));
        dialog.setHeaderText(bundle.getString("plugin_name"));
        var inputStr = dialog.showAndWait().orElse(null);
        if(inputStr != null)
        {
            //loadPlugin returns the name of the plugin if successfully loaded
            String name = ph.loadPlugin(inputStr, this);
            if(name != null)
            {
                list.add(name);
            }
        }
    }


    /**
     * Ask the user for the location of the script
     */
    private void loadScriptFile()
    {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle(bundle.getString("select_script"));
        File f = fileDialog.showOpenDialog(new Stage());
        if(f != null) //If a file is selected
        {
            sh.runScript(f.toString(), this);
        }
    }


    /**
     ** Checks if the currently pressed key combination matches a key in the list
     ** of combinations
     */
    private void checkKeyCombinations(KeyEvent keyEvent)
    {
        for(KeyCombination func : combos)
        {
            boolean ctrl = func.getCtrl();
            boolean shift = func.getShift();
            boolean alt = func.getAlt();
            String key = func.getKey().toUpperCase();
            if (checkModifiers(ctrl, shift, alt, keyEvent))
            {
                if (key.equals(keyEvent.getCode().getName()))
                {
                    processKeyCombination(func);
                }
            }
        }
    }


    /**
     ** Check if the modifier key combination matches the currently pressed keys
     **/
    private boolean checkModifiers(boolean ctrl, boolean shift, boolean alt, KeyEvent keyEvent)
    {
        return ctrl == keyEvent.isControlDown() &&
               shift == keyEvent.isShiftDown() &&
               alt == keyEvent.isAltDown();
    }


    /**
     * Process the key combination
     * @param combo - the key combination that matches the currently pressed keys
     */
    private void processKeyCombination(KeyCombination combo)
    {
        if(combo.getInsert()) //Insert text
        {
            if(combo.getLocation().equals("start of line"))
            {
                insertStartLine(getLineNum(), combo.getText());
            }
            else //Insert at caret
            {
                insertTextCaret(combo.getText());
            }
        }
        else //Remove text
        {
            if(combo.getLocation().equals("start of line"))
            {
                removeStartLine(getLineNum(), combo.getText());
            }
            else // Remove at caret
            {
                removeTextCaret(combo.getText());
            }
        }
    }

    /**
     * Removes the given string ending at the caret
     * @param toRemove - the string to be removed from the text area
     */
    private void removeTextCaret(String toRemove)
    {
        String allText = textArea.getText();
        int caretPos = textArea.getCaretPosition();
        // If the provided string is to the left of the caret
        if(allText.startsWith(toRemove, caretPos - toRemove.length()))
        {
            String newText = allText.substring(0, caretPos - toRemove.length()) + allText.substring(caretPos);
            textArea.setText(newText);
            textArea.positionCaret(caretPos - toRemove.length());
        }
    }


    // Replace the text in the text area
    @Override
    public void updateText(String text)
    {
        this.textArea.setText(text);
    }

    // Highlight the text in the specified range
    @Override
    public void highlightText(int start, int end)
    {
        textArea.selectRange(start, end);
    }

    // Insert text to the caret position
    @Override
    public void insertTextCaret(String text)
    {
        int caret = textArea.getCaretPosition();
        textArea.insertText(caret, text);
        textArea.positionCaret(caret + text.length());
    }

    // Allow plugins to register a function key and the associated callback
    @Override
    public void registerFKey(String key, ButtonPlugin callBack)
    {
        fKeyEvents.put(key, callBack);
    }

    //Add a new button to gui and add the functionality of the button
    @Override
    public void registerButton(String name, ButtonPlugin plugin)
    {
        Button newButton = new Button(name);
        newButton.setOnAction(event -> {
            plugin.buttonCallback();
        });
        toolBar.getItems().add(newButton);
    }

    // Ask user for input and return it
    @Override
    public String askForInput(String title)
    {
        var dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(bundle.getString("enter_text"));
        return dialog.showAndWait().orElse(null);
    }

    // Return the entire text field
    @Override
    public String receiveText()
    {
        return textArea.getText();
    }

    // Return the caret position
    @Override
    public int caretPosition()
    {
        return textArea.getCaretPosition();
    }

    // Add the name of a script to the list of scripts
    @Override
    public void addScript(String name)
    {
        scripts.add(name);
    }

    // Set the plugin as an observer of the text area
    @Override
    public void addTextObserver(TextModificationObserver obs)
    {
        tes.addTextObserver(obs);
    }
}