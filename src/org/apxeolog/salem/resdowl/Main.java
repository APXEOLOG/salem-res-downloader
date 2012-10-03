package org.apxeolog.salem.resdowl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Main {

	public static void saveUrl(File file, String urlString) throws MalformedURLException, IOException {
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		System.out.println("Saving " + urlString + " into " + file.toString());
		try	{
			in = new BufferedInputStream(new URL(urlString).openStream());
			fout = new FileOutputStream(file);

			byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null)
				in.close();
			if (fout != null)
				fout.close();
		}
	}
	
	public static void main(String[] args) {
		File resdir = new File("res");
		if (!resdir.exists()) resdir.mkdir();
		
		File listfile = new File("reslist.txt");
		try {
			System.out.println("Reading resource file list...");
			BufferedReader reader = new BufferedReader(new FileReader(listfile));
			ArrayList<String> strings = new ArrayList<String>();
            String text = null;
            while ((text = reader.readLine()) != null) {
            	strings.add(text);
            }
            System.out.println("Found " + String.valueOf(strings.size()) + " files to download...");
            int lastSlash = 0; File fileBuf = null;
            for (String str : strings) {
            	try {
	            	lastSlash = str.lastIndexOf('/');
	            	fileBuf = new File(resdir, str.substring(0, lastSlash));
	            	if (!fileBuf.exists()) fileBuf.mkdirs();
            		fileBuf = new File(resdir, str + ".res");
            		saveUrl(fileBuf, "http://plymouth.seatribe.se/res/" + str + ".res");
            	} catch (Exception ex) {
            		System.out.println("Local Error: " + ex.toString() + " Line: " + ex.getStackTrace()[0].getLineNumber());
            	}
            }
		} catch (Exception ex) {
			System.out.println("Global Error: " + ex.toString());
			System.out.println("\n\n");
			ex.printStackTrace();
		}
	}
}
