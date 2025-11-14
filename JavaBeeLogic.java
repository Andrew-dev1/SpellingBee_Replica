import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;
import java.awt.event.KeyEvent;
import java.io.*;

//Handles the logic for the JavaBee game
public class JavaBeeLogic{
   
   
   //Name of dictionary file (containing English words to validate guesses against)
   private static final String DICTIONARY_FILENAME = "dictionary.txt";   
    
   //Total number of hives in the game
   public static final int HIVE_COUNT = 7;
      
   //Required Min/Max length for a valid player guess
   public static final int MIN_WORD_LENGTH = 4;
   public static final int MAX_WORD_LENGTH = 19;    
   
   
   //Required Min/Max number of formable words for a randomized hive
   public static final int MIN_FORMABLE = 30;
   public static final int MAX_FORMABLE = 110;    
   
   //Collection of various letters (vowels only, consonants only, all letters)
   public static final String VOWEL_CHARS = "AEIOU";  
   public static final String CONSONANT_CHARS = "BCDFGHJKLMNPQRSTVWXYZ";
   public static final String ALL_CHARS = VOWEL_CHARS + CONSONANT_CHARS;
   
   //The various score title thresholds and their respective titles
   public static final double[] TITLE_PERCENTS = {0, 0.02, 0.05, 0.08, 0.15, 0.25, 0.4, 0.5, 0.7};
   public static final String[] TITLE_NAMES = {"Beginner", "Good Start", "Moving Up", 
      "Good", "Solid", "Nice", "Great", 
      "Amazing", "Genius"};
   
   //Text for different error messages that occur for various invalid inputs
   private static final String ERROR_TOO_LONG = "Too long...";
   private static final String ERROR_TOO_SHORT = "Too short...";
   private static final String ERROR_MISSING_CENTER = "Missing yellow letter...";
   private static final String ERROR_INVALID_LETTER = "Contains non-hive letter...";   
   private static final String ERROR_ALREADY_FOUND = "Already in word bank...";  
   private static final String ERROR_NOT_WORD = "Not in dictionary...";
   
   //Character codes for the enter and backspace key press
   public static final char ENTER_KEY = KeyEvent.VK_ENTER;
   public static final char BACKSPACE_KEY = KeyEvent.VK_BACK_SPACE;  
   
     
   
   //Use me for generating random numbers (see https://docs.oracle.com/javase/8/docs/api/java/util/Random.html)!
   private static final Random rand = new Random(); 


   
   
   //An  array storing all of the formable words given the chosen hive letters
   public static String[] formableWords = new String[MAX_FORMABLE];   
   
   //The maximum number of points possible given the game's chosen hive letters
   public static int possiblePoints = 0;
   
   

   // This function gets called ONCE when the game is very first launched
   // before the user has the opportunity to do anything.
   // 
   // Randomly chooses hive letters that meet the following criteria:
   //  -must be seven letters long
   //  -cannot have duplicate letters
   //  -cannot have an 'S' as one of its letters
   //  -must contain AT LEAST one vowel character (AEIOU) 
   //  -must have between 30 and 110 formable words (inclusive) that can be made
   //  -index 0 is center hive letter, rest are outer hive letters
   //   (additionally: if the array only contains one vowel, it should be 
   //    possible for the vowel to be in any hive, including the center)
   public static char[] initGame(){
      char[] letters = generateCharArray();
      while(verifyVowels(letters) || checkFormableWords(letters)){
         letters = generateCharArray();
      }


      JavaBeeGUI.setTitle(TITLE_NAMES[0]);

      return letters; 
   }
   
   public static char generateRandomLetter(){
      int randomInt = rand.nextInt(65,91);
      return (char) randomInt;
      
   }

   public static char[] generateCharArray(){
      char[] letters = new char[7];
      String letterString = ""; 
      for(int i = 0; i < letters.length; i++)
      {
         char randomletter = generateRandomLetter();
         String temp = "" + randomletter; 
         while(letterString.contains(temp) || temp.equals("S")){ 
            randomletter = generateRandomLetter();
            temp = "" + randomletter; 
         }
         letters[i] = randomletter;
         letterString += temp; 
      }
      return letters;
   }

   // checks if the given array has vowels. returns true if it has none
   // and returns false if it has at least one
   public static boolean verifyVowels(char[] lettersArr){
      String[] vowels = {"A","E","I","O","U"};
      int verification = 0;
      String letters = Arrays.toString(lettersArr);

      for(String vowel: vowels){
         if(! letters.contains(vowel))
               verification += 1;
      }
      if(verification == 5){
         return true;
      }
      return false;
   }

   // checks if there are between 30 to 110 formable words from the letters available
   // if there is, return false. if not, return true
   public static boolean checkFormableWords(char[] lettersArr){
      try{
         File file = new File(DICTIONARY_FILENAME);
         Scanner flow = new Scanner(file);
         String let = Arrays.toString(lettersArr).replaceAll(", ", "");
         String middleHive = let.substring(1,2);
         int wordsIndex = 0;

         while(flow.hasNextLine()){
            String word = flow.nextLine();            
            if(word.contains(middleHive) && word.matches(let + "+")){
                  if(wordsIndex < 110){
                     formableWords[wordsIndex] = word;
                     wordsIndex += 1;
                  }
                  else
                     break; 
               }
            
         }

         flow.close();
         if(wordsIndex > 29 ){
            return false;
         }

         Arrays.fill(formableWords, null);
         return true;

      }
      catch(FileNotFoundException e){
         System.err.println("This file does not exist");
         System.exit(1);
         return false;
      }
      
   }
      
   // gives a response based on which key is pressed on the GUI
   public static void playerInput(char key){
      String guess = JavaBeeGUI.getGuessStr();
      int guessLength = guess.length();

      if(ALL_CHARS.indexOf(key) >= 0){
         if(guessLength == MAX_WORD_LENGTH){
           JavaBeeGUI.wiggleGuess();
           JavaBeeGUI.displayError(ERROR_TOO_LONG);
         }
         else
            JavaBeeGUI.setGuess((guess + key));
         
      }
      else if(key == BACKSPACE_KEY )
      {
         if(guessLength != 0)
            JavaBeeGUI.setGuess(guess.substring(0, guessLength-1));
      }
      else{
         if(guessValidation(guess)){
            addingGuess(guess);
            appropriateTitle();
         }
         
      }
      
   }
   
   // adds the word to the word bank after checking to validate it
   public static void addingGuess(String guessedWord){
      int points = pointsCalculations(guessedWord);
      JavaBeeGUI.addToGuessBank(guessedWord, points);
      JavaBeeGUI.setGuess("");
      
   }

   // makes sure that the given String fits the requirements of 
   // being between 4-19 letters long, has the center letter, contains
   // only letters within the hives, and is not in the word bank already
   public static boolean guessValidation(String guess){
      String middle = String.valueOf(JavaBeeGUI.getYellowHiveChar());
      if(guess.length() < 4){
         JavaBeeGUI.displayError(ERROR_TOO_SHORT);
      }
      else if(! guess.contains(middle)){
         JavaBeeGUI.displayError(ERROR_MISSING_CENTER);
      }
      else if(invalidLetterCheck()){
         JavaBeeGUI.displayError(ERROR_INVALID_LETTER);
      }
      else if(repeatedWord(guess)){
         JavaBeeGUI.displayError(ERROR_ALREADY_FOUND);
      }
      else if(dictionaryValidation()){
         JavaBeeGUI.displayError(ERROR_NOT_WORD);
      }
      else{
         return true;
      }
      JavaBeeGUI.wiggleGuess();
      return false;
   }

   // checks for invalid letters within the guessed word
   // if a letter is not in one of the hives, the function returns true
   public static boolean invalidLetterCheck(){
      char[] guessArray = JavaBeeGUI.getGuessArr();
      String hiveLetters = Arrays.toString(JavaBeeGUI.getAllHiveChars());
      
      for(int i = 0; i < guessArray.length; i++){
         String letter = String.valueOf(guessArray[i]);
            if(! hiveLetters.contains(letter)){
               return true;
            }
         
      }
      
      return false;
   }

   // goes through the word bank to see if guess matches any of the 
   // existing words. If a match is found, return true and if not, return false
   public static boolean repeatedWord(String guess){
      String[] guessBank = JavaBeeGUI.getGuessBank();
      for(String g: guessBank){
         if(guess.equals(g.toUpperCase()))
            return true;
      }
      return false;
   }

   // checks if the guess is in the array of formable words
   // return false if it's already in guessed
   public static boolean dictionaryValidation(){
      String guessedWord = JavaBeeGUI.getGuessStr();
      for(int i = 0; i<formableWords.length; i++){
         String word = formableWords[i];
         if(word != null && word.equals(guessedWord))
            return false;
      }
      return true;
   }

   // calculates an number value based on the length and letters used
   // inside the guess
   public static int pointsCalculations(String guess){
      int length = guess.length();
      int points2 = 0;
      char[] letters = JavaBeeGUI.getAllHiveChars();

      if(length == 4)
         return 1;
      else{
         for(char letter: letters){
            if(guess.contains(String.valueOf(letter)))
               points2 += 1;
         }
         if(points2 == 7)
            return length + 7;
         else
            return length;
         
      }
      

   }

   // sets the title based on the percent of points currently acquired
   public static void appropriateTitle(){
      double percentage = (double) JavaBeeGUI.getScore()/ possiblePoints;
      int titleIndex = 0;
      for(double number: TITLE_PERCENTS){
         if(percentage >= number)
            titleIndex += 1;
         else{
            titleIndex -= 1;
            break;
         }
      }
      JavaBeeGUI.setTitle(TITLE_NAMES[titleIndex]);
   }

   // calculates the maximum number of points based on the array 
   // of words that can be formed with the letters
   public static void findingTotalPoints(){
      for(int i = 0; i<formableWords.length; i++){
         if(formableWords[i]== null)
            break;
         else
            possiblePoints += pointsCalculations(formableWords[i]);
         
      }
   }

}
