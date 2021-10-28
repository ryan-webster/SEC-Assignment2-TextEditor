/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 26/10/2021
 * Provides the user interface for loading and saving files.
 */
package edu.curtin.secassignment2;

import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoadSaveUI
{
    private Dialog<String> encodingDialog;
    private ResourceBundle bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());
    private FileIO fileIO;

    public LoadSaveUI(FileIO fileIO)
    {
        this.fileIO = fileIO;
    }

    /**
     * Provides a window to select a file to be loaded into the program
     */
    public String loadFileMenu()
    {
        String text = "";
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle(bundle.getString("load_file"));
        File f = fileDialog.showOpenDialog(new Stage());
        if(f != null) //If a file is selected
        {
            String encoding = getEncoding();
            try
            {
                text = fileIO.load(f, encoding);
            }
            catch (IOException e)
            {
                System.out.println(bundle.getString("error_loading"));
            }
        }
        return text;
    }

    /**
     * Provides menu for saving to a file with the specified encoding
     */
    public void saveFileMenu(String text)
    {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle(bundle.getString("save_file"));
        File f = fileDialog.showSaveDialog(new Stage());
        if(f != null) //If a file is selected
        {
            String encoding = getEncoding();
            if(encoding != null)
            {
                try
                {
                    fileIO.save(f, text, encoding);
                }
                catch (IOException e)
                {
                    System.out.println(bundle.getString("error_saving"));
                }
            }
        }
    }


    /**
     * Ask the user for the encoding to use
     * @return encoding
     * Reference: Method obtained from COMP3003 Worksheet 8
     */
    private String getEncoding()
    {
        if(encodingDialog == null)
        {
            var encodingComboBox = new ComboBox<String>();
            var content = new FlowPane();
            encodingDialog = new Dialog<>();
            encodingDialog.setTitle(bundle.getString("select_encoding"));
            encodingDialog.getDialogPane().setContent(content);
            encodingDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            encodingDialog.setResultConverter(
                    btn -> (btn == ButtonType.OK) ? encodingComboBox.getValue() : null);
            content.setHgap(8);
            content.getChildren().setAll(new Label(bundle.getString("select_encoding")), encodingComboBox);
            encodingComboBox.getItems().setAll("UTF-8", "UTF-16", "UTF-32");
            encodingComboBox.setValue("UTF-8");
        }
        return encodingDialog.showAndWait().orElse(null);
    }

}
