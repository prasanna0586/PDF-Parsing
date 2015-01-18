/**
* A Java Program to extract Text from a PDF document and write it to a text file.
*
* @author  Prasanna Kumar Ramachandran
* @version 1.0
* @since   2014-01-18 
*/

package com.pdf.parser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
 
public class PDFParser {
	/*The main method expects three arguments
		1. The input PDF file path
		2. The output text file path
		3. A temporary text file path. Here is where contents are available before stripping off unwanted content.
	  User can optionally ignore stripping off unwanted content. In that case comment removeUnwantedContents 
	  method, but you still may need not pass the third argument which is the path to temp file. 
	  The extracted content would be available in the temp text file in that case*/
	public static void main(String[] args) throws Exception {
		if(args.length == 3) {
			String inputFile = args[0];
			String outputFile = args[1];
			String tempFile = args[2];
			System.out.println("Extract text from PDF starts.....");
			extractTextToTempFile(inputFile, tempFile);
			//Comment removeUnwantedContents method, if you do not want to strip off unwanted content from pdf.
			removeUnwantedContents(tempFile, outputFile);
			System.out.println("Completed.....");
		} else {
			throw new Exception("Please provide the path of input PDF file as "
					+ "First Argument, the path of output text file as Second Argument "
					+ "and the path of temporary text file as Third Argument");
		}
	}
	//Method to extract text from PDF and write it to a temp file
	private static void extractTextToTempFile(String inputFile, String tempFile) {
		InputStream is = null;
		PrintWriter printWriter = null;
		try {
			is = new BufferedInputStream(new FileInputStream(new File(inputFile)));
			Parser parser = new AutoDetectParser();
			ContentHandler handler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			parser.parse(is, handler, metadata, new ParseContext());
			printWriter = new PrintWriter(tempFile);
			printWriter.write(handler.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		} finally {
			if (is != null && printWriter != null) {
				try {
					is.close();
					printWriter.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//Method to strip off unwanted contents from the temp file and write the result to the output file provided by the user
	private static void removeUnwantedContents(String tempFile, String outputFile) {
		BufferedReader br = null;
		StringBuffer stringBuffer = new StringBuffer();
		PrintWriter writer = null;
		try {
			br = new BufferedReader(new FileReader(tempFile));
			String line = null;
			writer = new PrintWriter(outputFile);
			while ((line = br.readLine()) != null) {
				Pattern p = Pattern.compile("[a-zA-Z]");
				if(p.matcher(line).find()) {
					if(line.length() > 3) {
						stringBuffer.append(line);
						stringBuffer.append(System.getProperty("line.separator"));
					}
				} else {
					line = "";
				}
			}
			writer.write(stringBuffer.toString());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br !=null && writer != null)
				try {
					br.close();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}