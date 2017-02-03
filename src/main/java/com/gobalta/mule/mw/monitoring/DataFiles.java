package com.gobalta.mule.mw.monitoring;

import java.io.*;

public final class DataFiles {
	private DataFiles() {
		
	}

	private static final String EMPTY_STRING = "";
	
	public static String getLatestDataFileName(String filePath) {
		
		File choice = getLatestDataFile(filePath);

		return (choice == null ? EMPTY_STRING : choice.getName()); 
		
	}
	
	public static File getLatestDataFile(String filePath) {
	    File fl = new File(filePath);
	    File[] files = fl.listFiles(new FileFilter() {          
	        public boolean accept(File file) {
	            return file.isFile();
	        }
	    });
	    long lastMod = Long.MIN_VALUE;
	    File choice = null;
	    for (File file : files) {
	        if (file.lastModified() > lastMod) {
	            choice = file;
	            lastMod = file.lastModified();
	        }
	    }
	    if(choice.isHidden()) choice = null;
	    
	    return choice;
	}
}
