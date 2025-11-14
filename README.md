# JavaBee - Spelling Bee Game

A Java-based clone of the popular New York Times Spelling Bee word puzzle game. Players form words using letters from a honeycomb arrangement of seven unique letters, with the constraint that all words must include the center (yellow) letter.

## Game Overview

JavaBee challenges players to create as many valid English words as possible using:
- **7 unique letters** arranged in a honeycomb pattern
- **1 center letter** (yellow) that must appear in every word
- **6 outer letters** (gray) that are optional

## Features

- **Dynamic Word Validation**: Real-time dictionary checking against a comprehensive English word list
- **Smart Scoring System**: 
  - 4-letter words: 1 point
  - Longer words: 1 point per letter
  - **Pangrams** (words using all 7 letters): Bonus 7 points
- **Progressive Titles**: Earn titles from "Beginner" to "Genius" as your score increases
- **Randomized Puzzles**: Each game generates a new set of letters with 30-110 possible words
- **User-Friendly Interface**: 
  - Click letters or use keyboard input
  - Visual feedback for valid/invalid letters
  - Word bank tracking all discovered words
  - Animated error messages

## Game Rules

### Valid Words Must:
1. Be at least **4 letters long** (max 19 letters)
2. Contain the **center (yellow) letter**
3. Use only letters from the **7 hives**
4. Be found in the **dictionary file**
5. Not be **previously guessed**

### Invalid Elements:
- Letter **'S'** never appears in any puzzle (following NY Times Spelling Bee rules)
- Puzzles always contain **at least one vowel**

## Installation & Setup

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- `dictionary.txt` file containing valid English words (one per line)

### Running the Game
```bash
# Compile the Java files
javac GameLauncher.java JavaBeeLogic.java JavaBeeGUI.java

# Launch the game
java GameLauncher
```

## How to Play

### Input Methods:
1. **Keyboard**: Type letters directly, press `Enter` to submit, `Backspace` to delete
2. **Mouse**: Click on letter hexagons, or use the Delete/Enter buttons

### Controls:
- **Enter/Click Enter Button**: Submit your word
- **Backspace/Click Delete Button**: Remove last letter
- **ESC**: Quit the game

### Scoring Titles:
Progress through ranks based on your percentage of maximum possible points:
- **Beginner** (0%)
- **Good Start** (2%)
- **Moving Up** (5%)
- **Good** (8%)
- **Solid** (15%)
- **Nice** (25%)
- **Great** (40%)
- **Amazing** (50%)
- **Genius** (70%+)

## Project Structure

```
├── GameLauncher.java      # Entry point - launches the game
├── JavaBeeLogic.java      # Core game logic and validation
├── JavaBeeGUI.java        # Graphics and user interface
└── dictionary.txt         # Word list (not included)
```

## Technical Implementation

### Key Algorithms:
- **Letter Generation**: Random selection ensuring no duplicates, no 'S', and at least one vowel
- **Word Validation**: Multi-stage checking (length, center letter, valid letters, dictionary, uniqueness)
- **Score Calculation**: Variable point system with pangram detection
- **Puzzle Generation**: Automatic regeneration if valid word count falls outside 30-110 range

### Data Structures:
- `formableWords[]`: Array storing all possible valid words for the current puzzle
- `enteredWords`: ArrayList tracking player's discovered words
- Hive objects: Custom polygon-based hexagon components

## Error Messages

- **"Too short..."**: Word has fewer than 4 letters
- **"Too long..."**: Word exceeds 19 letters
- **"Missing yellow letter..."**: Center letter not included
- **"Contains non-hive letter..."**: Invalid letter used
- **"Already in word bank..."**: Word previously guessed
- **"Not in dictionary..."**: Word not found in word list

## Future Enhancements

Potential improvements:
- Save/load game state
- Daily puzzle mode
- Hint system
- Statistics tracking
- Multiplayer support

## Credits

- GUI framework: Computer Science Department starter code
- Game logic & implementation: [Your Name]
- Inspired by: The New York Times Spelling Bee

## License

This project is for educational purposes. Spelling Bee is a trademark of The New York Times Company.

---

**Note**: You must provide your own `dictionary.txt` file. Common sources include SCOWL (Spell Checker Oriented Word Lists) or other English word databases.
