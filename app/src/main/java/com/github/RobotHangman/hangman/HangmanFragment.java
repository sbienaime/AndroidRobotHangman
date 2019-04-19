package com.github.RobotHangman.hangman;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class HangmanFragment extends Fragment {


    private String word;
    public ArrayList<String> mWordList;
    private ImageView imageView;
    private TextView WrongGuesses;
    private FormEditText input;
    private char guess;
    private TextView Word;
    private String WrongGuessesInfo;
    private String WordDashes;
    private int error;
    private String TAG = "HangmanFragment";
    private Scanner scanner;
    private Button submit;
    private Button resetbtn;
    private void resetGame(){
        input.requestFocus();
        imageView.setImageResource(R.drawable.error0);
        WrongGuessesInfo ="";
        WrongGuesses.setText(WrongGuessesInfo);
        word = pickRandomWord(mWordList);
        WordDashes =hideWord(word);
        Word.setText(WordDashes);
        error=0;

        submit.setVisibility(View.VISIBLE);
        resetbtn.setVisibility(View.GONE);

    }

    public HangmanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_main, container, false);





        fillList();
        imageView = (ImageView) mainView.findViewById(R.id.gallows);
        WrongGuesses = (TextView) mainView.findViewById(R.id.wrongletters);
        input = (FormEditText) mainView.findViewById(R.id.input);
        Word = (TextView) mainView.findViewById(R.id.the_word);
        submit = (Button) mainView.findViewById(R.id.submitbtn);
        resetbtn = (Button) mainView.findViewById(R.id.resetbtn);
        resetGame();



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guess =getLetter(input);

                if(guess !='\u0000') {

                    String temp = WordDashes;
                    WordDashes = findCharInWord(word, guess, WordDashes);

                    if (WordDashes.equals(temp)) {

                        if (WrongGuessesInfo.equals("")) {
                            WrongGuessesInfo = WrongGuessesInfo + String.valueOf(guess);
                        } else {
                            WrongGuessesInfo = WrongGuessesInfo + ", " + String.valueOf(guess);
                        }
                        WrongGuesses.setText(WrongGuessesInfo);

                        increaseError();
                    } else {

                        Word.setText(WordDashes);
                        input.setText("");
                    }
                    input.requestFocus();
                    endGame();
                }
            }
        });


        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });






        return mainView;
    }

    public char getLetter(FormEditText input)
    {
        boolean valid=input.testValidity();
        if (valid)
        {
            return input.getText().toString().toLowerCase().charAt(0);
        }
        else
            return '\u0000';
    }

    public String pickRandomWord(ArrayList<String> list) {

        int length = list.size();
        Random random = new Random();
        int index = random.nextInt(length);
        String word = list.get(index);
        word = word.toLowerCase();
        String temp = "";
        // add spaces in between letter to make it look pretty
        for (int i = 0; i < word.length(); i++) {
            temp = temp + word.charAt(i) + " ";
        }
        return temp;
    }

    public String findCharInWord(String word, char letter, String returnable) {

            for (int index =0; index < word.length();index++) {
                if (word.charAt(index) == letter) {
                    char[] temp = returnable.toCharArray();
                    temp[index] = letter;
                    returnable = String.valueOf(temp);
                }
            }
        return returnable;
    }

    public String hideWord(String word){
        String dashes="";
        for(int i=0;i<word.length();i++){
            if(!isLowercaseCharacter(word.charAt(i)))
                dashes=dashes+word.charAt(i);
            else
                dashes=dashes+"_";
        }
        return dashes;
    }

    private void increaseError(){


        error+=1;
        if(error<6){
            String id = "error"+String.valueOf(error);
            imageView.setImageResource(getResources().getIdentifier(id,"drawable","com.github.darthjoey91.hangman"));
            input.setText("");
            Word.setText(WordDashes);
        }
    }

    public void endGame(){
        if(word.equals(WordDashes))
        {
            WrongGuesses.setText(R.string.game_over);
            Word.setText(R.string.game_win);
            imageView.setImageResource(R.drawable.win);
            submit.setVisibility(View.GONE);
            resetbtn.setVisibility(View.VISIBLE);
        }else if (error>=6){
            Word.setText(word);
            WrongGuesses.setText(R.string.game_over);
            imageView.setImageResource(R.drawable.error6);
            submit.setVisibility(View.GONE);
            resetbtn.setVisibility(View.VISIBLE);
        }
    }

    public void fillList(){

        mWordList = new ArrayList<String>();




        //Handles if file was empty.
        if(mWordList.isEmpty()){
            useDefaultWordlist();
        }

    }


    public void useDefaultWordlist(){
        scanner = new Scanner(getResources().openRawResource(R.raw.wordlist));


        try {
            while (scanner.hasNext()) {
                mWordList.add(scanner.next());
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public boolean isLowercaseCharacter(char a){
        String test = String.valueOf(a);
        return test.matches("\\p{Ll}");
    }

}




