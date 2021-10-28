import org.program_api.TextModificationObserver
import org.program_api.API


class SmileyFaceHandler(org.program_api.TextModificationObserver):
    def receiveTextCallBack(self, newText):
        face = u"\U0001f60a"
        if ":-)" in newText: # To prevent out of bounds error when adding a smiley face to the end of the text area
            api.updateText(newText.replace(":-)", face))

    def getName(self):
        return ":-)"


print("Starting :-) script")
handler = SmileyFaceHandler()
api.addScript(":-)") #Add the name of the script
api.addTextObserver(handler) #Register as an observer of changes to the text area
