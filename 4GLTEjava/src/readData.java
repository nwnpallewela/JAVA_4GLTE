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
public class readData {
    private Scanner x;
    public String run(){
        
        openFile();
        String data=readFile();
        closeFile();
        return data;
    }
    public void openFile(){
        try{
            x = new Scanner(new File("data.txt"));
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public String readFile(){
        
        String data="";
        String bit="";
        while(x.hasNext()){
            bit=x.next();
             data=data.concat(bit);
          
        }
        
        return data;
    }
    public void closeFile(){
        x.close();
    }
}