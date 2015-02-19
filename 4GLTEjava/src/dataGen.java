/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.*;
import java.io.*;
/**
 *
 * @author User
 */
public class dataGen {
    private Formatter x;
    public void run(int size){
        Random bit = new Random();
        
        Integer number=0;
        
        List<Character> data= new ArrayList<Character>(); 
        
        for (int i = 0; i < size; i++) {
           number=bit.nextInt(2);
        	//number=1;
            data.add(number.toString().charAt(0));
        }
       
        String datawrite = data.toString();
         
        openFile();
        addRecords(datawrite);
        closeFile();
        
    }
    public void openFile(){
        try{
            x= new Formatter("data.txt");
        }catch(FileNotFoundException e){
            System.out.println("Error");
        }
    }

    
    public void addRecords(String data){
         data=data.replace(',', ' ');
         data=data.replace(']', ' ');
          data=data.replace('[', ' ');
        
         x.format("%s", data.trim());
    }
    public void closeFile(){
        x.close();
    }
}