/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 15/10/2021
 * Handles the loading of plugins using reflection
 ***/
package edu.curtin.secassignment2;

import org.program_api.API;
import org.program_api.TextEditorPlugin;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class PluginHandler
{
    private ArrayList<String> pluginNames; //Store the name of loaded plugins

    public PluginHandler()
    {
        this.pluginNames = new ArrayList<>();
    }

    public ArrayList<String> getPlugins()
    {
        return pluginNames;
    }

    /**
     * loads and starts the plugin using reflection
     * @param name
     * @param api
     * @return - the name of the plugin if successfully loaded
     */
    public String loadPlugin(String name, API api)
    {
        ResourceBundle bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());
        String pluginName = null;
        try
        {
            Class<?> pluginClass = Class.forName(name);
            Constructor<?> con = pluginClass.getConstructor();
            TextEditorPlugin plugin = (TextEditorPlugin)con.newInstance();
            plugin.start(api);
            pluginName = plugin.getName();
            pluginNames.add(pluginName); //Add name to list of plugins
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(bundle.getString("class_not_found") + name);
        }
        catch (NoSuchMethodException nsm )
        {
            System.out.println(bundle.getString("method_not_found") + name);
        }
        catch (InvocationTargetException | IllegalAccessException | InstantiationException e)
        {
            System.out.println(bundle.getString("error") + e.getMessage());
        }
        // Return name to be displayed in the listview in the UI
        return pluginName;
    }
}
