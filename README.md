# blank-afl-creator
A program that creates a blank AFL with any amount of files below 65536.

# Usage
Run the program in Command Prompt, like so: ``java -jar blank-afl-creator.jar``

It will ask you for the output AFL's file name, as well as the number of files it will support.

Keep in mind that said number should match the AFS you are applying the AFL to. No more, no less.

All of the files in the output AFL start with ``blank`` and consist of numbers starting from 0.

They also have no file extension, although I could add that as an option in the future if the demand is there.