/*
  Used links:
  Find file in folder
  https://stackoverflow.com/questions/17697646/how-to-detect-if-a-filewith-any-extension-exist-in-java
  https://stackoverflow.com/questions/5751335/using-file-listfiles-with-filenameextensionfilter
  
  CSV to XML with JAVA

: https://stackoverflow.com/questions/12120055/conversion-of-csv-to-xml-with-java
  https://stackoverflow.com/questions/47183982/converting-csv-file-to-kml
  *
  *
  */



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class main {

	//--------decleration of global variables-------------//
	private static String mac, ssid, date, time, type, lat, lon, alt, channel;
	private static int rssi;
	private static BufferedReader br;


	public static void main (String[] args) throws IOException{

		//------------declaring and creating of arrays--------------///
		ArrayList<RowData> lines = new ArrayList<>(); // this array holds all of the Data lines in the wiggle wifi csv file
		ArrayList<RowData> linesTop10 = new ArrayList<>();  // this arrayList holds my objects with the top 10 rssi


		int counter = 0; //counter that will count how many lines are in csv file that i exported from wiggle wifi.
		ArrayList<String> headLines=new ArrayList<String>(); //array that will recieve the 2nd line in my csv file and hold my new csv's header 

		String line = "";

		//-----------------reading the csv files that i have from the wiggle wifi app-------------------//
		/* find the folder
		create array of files in this folder
		Iterate through all the files and insert into array all the text from the csv files.*/


		File folder = new File("/Users/amihaitorgeman/Desktop/Today");

		File[] listOfFiles = folder.listFiles();{ 
			for (int i = 0; i < listOfFiles.length; i++) {
				File file = listOfFiles[i];
				if (file.isFile() && file.getName().endsWith(".csv")) {
					FileReader in = new FileReader(listOfFiles[i]);
					br = new BufferedReader(in);

					//------------the program is reading the csv file line after line-----------//
					// the while loop runs if the current line is not null.
					while ((line = br.readLine()) != null) {   

						//saving each variable in an array of string, getting the word by splitting the string after the "," token.
						String[] getWord = line.split(","); 

						//----------------saving the variables from each line so i can later save them in my RowData object--------//
						if(counter == 0) {
							//if counter is == 0 than i am in the first line of the csv file that contains information i dont need
							//so on the first line the program reads it without saving. 
						}
						else if(counter == 1){
							//if counter is == 1 then i am reading the line that contains all the headers such as: mac, ssid, time, etc' etc'..
							//so if my program is reading line 2 than it will save the headers in a arraylist "headlines that contains strings.
							for(int j=0; j<11 ; j++) {
								headLines.add(getWord[j]);
							}
						}
						else{
							//if my counter is over line 2 than i am reading the data in each line.
							//i have a loop that is running 10 times to get all the data in each row
							for (int j = 0; j < 11; j++) {
								// the switch case allows me to save the data as variables so i can later put them into my RowData object
								switch (j){
								case 0:  //case 0 is mac
									mac = getWord[j];
									break;

								case 1:  //case 1 is ssid
									ssid = getWord[j];
									break;

									// skipped case 2, was not needed.

								case 3: //case 3 is time
									time = getWord[j];
									break;

								case 4:   //case 4 is channel
									channel = getWord[j];
									break;

								case 5:  // case 5 is rssi
									rssi = Integer.parseInt(getWord[j]);
									break;

								case 6:	// case 6 is lat
									lat = getWord[j];
									break;

								case 7:	// case 7 is lon
									lon = getWord[j];
									break;

								case 8: // case 8 is alt
									alt = getWord[j];
									break;
									
								case 10: // case 10 is Type
									type = getWord[j];
									break;
								}								
							}

							//----------before while loop ends----------//
							//i save all the data in my RowData object and save it in my arraylist "lines".
							// D is our temp object line that goes into lines - the arrayLists that contain rowData objects.
							RowData D = new RowData(mac, ssid, time, type, lat, lon, alt, channel, rssi);
							lines.add(D);

						}

						//the counter counts the number of lines i have in my file.
						counter++;

						//-----------end of while, loops on to the next line in the wiggle wifi csv file ---------//
					}
					in.close();
				}


			}


			//----------this is bubble sort----------------//
			//after i have all my data saved in my RowData array list i sort the objects by their RSSI.

			//created a temporary object  
			RowData temp = new RowData(mac, ssid, time, type, lat, lon, alt, channel, rssi);
			//bubble sort//
			for (int i2 = 0; i2 < counter-2; i2++) {
				for (int j = 1; j < ((counter-2) - i2); j++) {
					if (lines.get(j-1).getRssi() < lines.get(j).getRssi()) {						      
						temp.copyRowData(lines.get(j-1), temp);
						lines.get(j-1).copyRowData(lines.get(j), lines.get(j-1));
						lines.get(j).copyRowData(temp, lines.get(j));
					}
				}
			}
			//---------------now my "lines" arraylist is sorted by its rssi---------------------//


			
			//---------------part 2 in matala 0 - creating new csv that displays the data with the top10 rssi-------------------------//
			
			//saving headlines to string in order to write to the new csv file that will contain the top 10
			String head = headLines.get(0) + "," +
						headLines.get(1) + "," +
						headLines.get(3) + "," +
						headLines.get(10) + "," +
						headLines.get(7) + "," +
						headLines.get(6) + "," +
						headLines.get(8) + "," +
						headLines.get(4) + "," +
						headLines.get(5);

			
			
				// declare temp variable of empty string to insert rowData into string format so i can write to my new csv.
				String top10Data = "";
				
				// for loop does A. saves rowData objects to new to top 10 arrayList B. saves the objects of the top10 in string format so we can print it.
				for(int i = 0; i< 10; i++) {
					
					//creating new rowData object, that contains the data from the top 10 rssi
					RowData r = new RowData(lines.get(i).getMac(), 
							lines.get(i).getSsid(),
							lines.get(i).getTime() ,
							lines.get(i).getType(),
							lines.get(i).getLon(),
							lines.get(i).getLat(),
							lines.get(i).getAlt(),
							lines.get(i).getChannel(),
							lines.get(i).getRssi());
					linesTop10.add(i, r); //this line adds in the arraylist "linestop10" in index i, the temp data "r";
					
					//saving all rowData to string so that i can write to the csv file.
					top10Data += "\n" + linesTop10.get(i).getMac() + "," +
							linesTop10.get(i).getSsid() + "," +
							linesTop10.get(i).getTime() + "," +
							linesTop10.get(i).getType() + "," +
							linesTop10.get(i).getLon() + "," +
							linesTop10.get(i).getLat() + "," +
							linesTop10.get(i).getAlt() + "," +
							linesTop10.get(i).getChannel() + "," +
							linesTop10.get(i).getRssi();
				}
				
				
			try {
				// output must be difrente folder than the input. else we get lovely exception. 
				//create file writer and declare file path
				FileWriter writer = new FileWriter("/Users/amihaitorgeman/Desktop/today/output/finaltest3.csv");
				writer.write(head); //write table headers
				writer.write("\n");
				writer.write(top10Data);// write to csv file top 10 data items
				writer.close();

				//this is to catch Exception and wrong files
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}




		}
		
		
		//---------------part 3 in matala 0 - creating new kml that displays the data with the top10 rssi to use in google maps-------------------------//
		writeFileKML(linesTop10, "/Users/amihaitorgeman/Desktop/today/output/KMLtest3.kml");
		
		
		//---------------------end of main---------------------//
	}




//------------------part 3 of matala 0------------------------//	
 static void writeFileKML(ArrayList<RowData> rowData, String fileName) {

		    ArrayList<String> content = new ArrayList<String>();

		    String kmlstart = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		            "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n"
		            + "<document>\n";

		    content.add(kmlstart);

		    String kmlend = "</document>\n"+"</kml>";

		    try{
		        FileWriter fw = new FileWriter(fileName);
		        BufferedWriter bw = new BufferedWriter(fw);

		        for (int i = 1; i < rowData.size(); i++) {

		            String kmlelement ="<Placemark>\n" +
		                    "<name>"+rowData.get(i).getMac()+"</name>\n" +
		                    "<description>"+rowData.get(i).getSsid()+"</description>\n" +
		                    "<Point>\n" +
		                    "<coordinates>" + rowData.get(i).getLon() + "," + rowData.get(i).getLat() + "</coordinates>" +
		                    "</Point>\n" +
		                    "</Placemark>\n";
		            content.add(kmlelement);
		        }
		        content.add(kmlend);
		        String csv = content.toString();//.replaceAll(",", "");
		        bw.write(csv);
		        bw.close();
		    } 
		    catch (IOException e) {
		        e.printStackTrace();
		    }
		}
 }

