A GUI text editor. 
    - Uses a custom domain specific language, parsed with JavaCC to load custom key combinations from the keymap file
    - Custom API designed for plugins of the text editor app
    - Dynamic loading of custom plugins using the Reflection API 
    - Loading of user scripts with Jython

Running the program:

To run the program with the default locale, from the root project enter:
    ./gradlew run

Locale is optional and can be specified by using:
    ./gradlew run --args='--locale=xx-XX'

e.g., ./gradlew run --args='--locale=jp-JP' to use the "Japanese" translation


