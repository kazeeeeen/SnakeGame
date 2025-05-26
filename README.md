## Snaky Snack Eater

- This Snaky Snack Eater Game is a classic arcade-style game built using Java Swing and AWT libraries. 

- Core Gameplay:
Snake Movement: The snake moves in four directions (up, down, left, right) controlled by arrow keys.

- Food Eating: The snake grows in length each time it eats regular food, which appears at random locations on the board.

# Game Over Conditions:

Colliding with itself.
Hitting the walls of the board.

# Features
Basic Game Mechanics
Grid-Based Movement: The game board is divided into tiles, and all movement is tile-based.

Smooth Animation: Snake movement and growth are visually represented using fill3DRect for better aesthetics.

# Food Types
Regular Food (Red) which spawns randomly and increases score by 1.

Big Food (Orange) which spawns every 30 seconds. It despawns every 10 seconds and when eaten it increases the score by 5.

Poisonous Food (Green) which spawns every 40 second. It despawns every 10 seconds and when eaten it decreases the score by 3.


# Visual Enhancements
Snake Eyes: The snake head includes two eyes to visually distinguish it from the body.

- 3D Tile Effects: Used for food and snake tiles to give a more dynamic and appealing look.

- Score & High Score
Score Tracker: Displays the current score based on snake length.

High Score: Keeps track of the highest score achieved during the session.

- Game Restart
Restart with Spacebar: When the game is over, pressing the Spacebar resets the game instantly:

Snake position and body reset

Food re-placed

Timers restarted

🧠 Technologies Used
Java AWT – for drawing and keyboard handling.

Java Swing – for window creation, timers, and component updates.

-----------------------------------------------------------------

📝 How to Run the Snake Game

Follow these steps to set up and run the Snake Game:

✅ Prerequisites

Ensure you have Java JDK and Visual Studio Code installed.

Make sure the Java Extension Pack is installed in VS Code.

🐍 Steps to Run the Game

Download and Extract the Project

Download the ZIP file of the Snake Game project.

Extract the contents to your desired folder.

Open the Project in VS Code

Navigate to the extracted folder.

Right-click inside the folder and select "Open in Terminal".

In the terminal, type the following command (don’t forget the space between code and .):

code .

Open the Main File

Once Visual Studio Code opens, go to the src folder.

Open the file named App.java.

Run the Program

Click the Play (Run) button in the upper right corner of the VS Code window.

The Snake Game window should appear and begin running.

🔄 Note

Always run the game from the App.java file. Running from other files may result in errors or unexpected behavior.