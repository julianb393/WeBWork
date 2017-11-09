package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ExtractData {

	/**
     * Returns ArrayList of ArrayList of Assignment Question data.
     * @param file
     * @return
     */
    public static ArrayList<ArrayList<String>> getAssignmentQData(File file) {
        
        ArrayList<String> orderedQInfo1 = new ArrayList<>();
        ArrayList<String> orderedQInfo2 = new ArrayList<>();
        ArrayList<String> orderedQInfo3 = new ArrayList<>();
        ArrayList<String>orderedQInfo4 = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        data.add(orderedQInfo1);
        data.add(orderedQInfo2);
        data.add(orderedQInfo3);
        data.add(orderedQInfo4);
        String line;
        String[] placeHolder = new String[4];
        
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            line = br.readLine();
            while(line != null) {
                placeHolder = line.split(",");
                data.get(0).add(placeHolder[0]);
                data.get(1).add(placeHolder[1]);
                data.get(2).add(placeHolder[2]);
                data.get(3).add(placeHolder[3]);
                line = br.readLine();
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println(data);
        return data;
    }
    
    /**
     * Returns a String array of the assignment
     * information, located in it's respective
     * assignment csv file.
     * 
     * [(Un)released/Due-date/date of creation]
     * 
     * @param fileName: String name of the assignment's csv file.
     */
    public static String[] getAssignmentInfo(String fileName) {
    	
    	String[] info = new String[3];
    	
    	try {
    		FileReader fr = new FileReader(fileName);
    		BufferedReader br = new BufferedReader(fr);
    		// Check first cell for unreleased
    		String line = br.readLine();
    		info = line.split(",");
    		br.close();
    		fr.close();
    		
    	} catch (IOException e) {
    		e.printStackTrace();
        }
    	
    	return info;
    }
}
