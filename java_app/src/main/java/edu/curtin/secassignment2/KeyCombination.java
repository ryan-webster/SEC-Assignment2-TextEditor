/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 18/10/2021
 * Class that holds the information for a key combination loaded from the keymap file
 */
package edu.curtin.secassignment2;

public class KeyCombination
{
    private boolean ctrl;
    private boolean shift;
    private boolean alt;
    private String key;
    private boolean insert;
    private String text;
    private String location;

    public KeyCombination(boolean ctrl, boolean shift, boolean alt, String key,
                          boolean insert, String text, String location)
    {
        this.ctrl = ctrl;
        this.shift = shift;
        this.alt = alt;
        this.key = key;
        this.insert = insert;
        this.text = text;
        this.location = location;
    }

    public boolean getCtrl()
    {
        return ctrl;
    }

    public boolean getShift()
    {
        return shift;
    }

    public boolean getAlt()
    {
        return alt;
    }

    public String getKey()
    {
        return key;
    }

    public boolean getInsert()
    {
        return insert;
    }

    public String getText()
    {
        return text;
    }

    public String getLocation()
    {
        return location;
    }


    @Override
    public String toString()
    {
        return "KeyCombination{" +
                "ctrl=" + ctrl +
                ", shift=" + shift +
                ", alt=" + alt +
                ", key='" + key + '\'' +
                ", insert=" + insert +
                ", text='" + text + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
