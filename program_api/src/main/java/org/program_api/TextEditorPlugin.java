/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 16/10/2021
 * Interface that all plugins must implement, plugins must provide a start()
 * method to bind themselves to the program and provide a name which is used
 * when displaying the plugins
 */
package org.program_api;


public interface TextEditorPlugin
{
    void start(API api);
    String getName(); //Name of the plugin
}
