/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package languagestudygui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author JWill
 */
public class StudyGUI extends JFrame implements ActionListener{
    private JButton translateButton;
    private JTextField textField;
    private JLabel directions;
    private JLabel word;
    private ArrayList <String> englishWords;
    private ArrayList <String> italianWords;
    private Timer timer;
    private Timer answerTimer;
    public Random rng;
    public int randomLangWord;
    public int randomListWord;
    public int num;
    public int num2;
    public StudyGUI(){
        englishWords = new ArrayList<String>();
        italianWords = new ArrayList<String>();
        
        this.englishWords = fileToArrayList("english.txt", englishWords);
        this.italianWords = fileToArrayList("italian.txt", italianWords);
        
        timer = new Timer(5000, this);
        timer.setRepeats(false);
        
        answerTimer = new Timer(3000, new TimerPrompt());
        answerTimer.setRepeats(false);
        
        
        rng = new Random();
        this.randomLangWord = rng.nextInt(2);
        this.randomListWord = rng.nextInt(this.italianWords.size());
        
        /*
            GUI Components
        */
        //JFrame
        this.setTitle("Language GUI");
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();
        layout.gridx = 0;
        layout.gridy = 0;
        layout.insets = new Insets(10, 10, 10, 10);
        
        this.setPreferredSize(new Dimension(450, 150));
        this.setMinimumSize(this.getPreferredSize());
        
        //Top message
        directions = new JLabel("Type the translation into the field below");
        layout.gridx = 225;
        layout.gridy = 0;
        this.add(directions, layout);
        
        //Text field
        textField = new JTextField();
        
        textField.setPreferredSize(new Dimension(225, 20));
        textField.setMinimumSize(textField.getPreferredSize());
        textField.addActionListener(this);
        layout.gridx = 225;
        layout.gridy = 2;
        this.add(textField, layout);
        
        //Right button
        translateButton = new JButton("OK");
        translateButton.addActionListener(this);
        layout.gridx = 440;
        layout.gridy = 2;
        this.add(translateButton, layout);
        
        //Word
        word = new JLabel(chooseWord(randomLangWord, randomListWord));
        layout.gridx = 10;
        layout.gridy = 2;
        this.add(word, layout);
        timer.start();
        num = 0;
        num += 1;
        if(num == 5){
            timer.stop();
            timer.addActionListener(this);
        }
    }
    
    public ArrayList<String> fileToArrayList(String filename, ArrayList<String> wordList){
        try{
            FileInputStream file = new FileInputStream(filename);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                wordList.add(scan.nextLine());
            }
        }catch(FileNotFoundException es){
            es.printStackTrace();
        }
        return wordList;
    }
    
    public String chooseWord(int listnumber, int index){
        String word = null;
        switch(listnumber){
            case 0:
                word = this.englishWords.get(index);
                break;
            case 1:
                word = this.italianWords.get(index);
                break;
        }
        return word;
    }
    
    public boolean checkWord(int listnumber, int index, String inputAnswer){
        boolean check = false;
        switch(listnumber){
            case 0:
                if(inputAnswer.equalsIgnoreCase(italianWords.get(index))){
                    check = true;
                    break;
                }
                break;
            case 1:
                if(inputAnswer.equalsIgnoreCase(englishWords.get(index))){
                    check = true;
                    break;
                }
        }
        return check;
    }
    
    public String realAnswer(int listnumber, int index){
        String translation = "";
        switch(listnumber){
            case 0:
                translation = italianWords.get(index);
                break;
            case 1:
                translation = englishWords.get(index);
                break;
        }
        return translation;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(checkWord(randomLangWord, randomListWord, textField.getText())){
            num = 0;
            answerTimer.start();
            num += 1;
            directions.setText("Correct");
            textField.setEditable(false);
            if(num == 3){
                answerTimer.stop();
                answerTimer.addActionListener(this);
            }
        }else{
            answerTimer.start();
            num = 0;
            num += 1;
            directions.setText("Incorrect! Answer: " + realAnswer(randomLangWord, randomListWord));
            textField.setEditable(false);
            num += 1;
            if(num == 3){
              answerTimer.stop();
              answerTimer.addActionListener(this);
            }
        }
    }
    
    class TimerPrompt implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae){
            randomLangWord = rng.nextInt(2);
            randomListWord = rng.nextInt(italianWords.size());
            directions.setText("Type the translation into the field below");
            word.setText(chooseWord(randomLangWord, randomListWord));
            textField.setEditable(true);
            textField.setText("");
            timer.restart();
            num = 0;
            num += 1;
            if(num == 5){
                timer.stop();
                timer.addActionListener(this);
            }
        }
    }
}
