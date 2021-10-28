/***
 * Name: Ryan Webster
 * Student Number: 17097248
 * Date: 16/10/2021
 * Plugins can register themselves as an observer of changes in the text, and be
 * notified when the text field is modified
 ***/

package org.program_api;

public interface TextModificationObserver
{
    void receiveTextCallBack(String text);
}
