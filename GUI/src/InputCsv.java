// this is the main class of the project (before GUI)

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;



public class InputCsv {
	
	
	//File ,import package working with files.
	public File file;
	
	// Constructor gets the address/ folder path of the file.
	public InputCsv(String path)
	{
		this.file=new File(path);
	}
	
	
	// ****FOR testing in different OS , Change path of INPUT and UOTPUT of the bellow*******
	//INPUT PATH:
	static String inputpath = "/Users/amihaitorgeman/Desktop/NewMatala/Input";
	// OUTPUT PATH:
	static String halfoutputpath = "/Users/amihaitorgeman/Desktop/NewMatala/Output/";

	
	
	
	static String outputpath = halfoutputpath +"outputCSV.csv";
	// 2 static var to deal with the ID problem of scanning device in wiglewife file.
	static ArrayList<String> IDlist=new ArrayList<String>();
	static ArrayList<Integer> IDsplit=new ArrayList<Integer>();
	
	
	
	// Read the wiggle wifi files and insert them to List of ArrayList String
	public List<ArrayList<String>> importfiles() throws IOException
	{
		List<ArrayList<String>> inputfiles=new ArrayList<ArrayList<String>>();
		File f=this.file;
		FilenameFilter textFilter = new FilenameFilter() {
			// checking if the this is csv file.
			public boolean accept(File dir,String name) {
			// name of the csv file
			String lowercaseName = name.toLowerCase();
			if(lowercaseName.endsWith(".csv"))
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		};
	
		// create array in order to run on the folder files.
		File[] files = f.listFiles(textFilter);
		for(File file: files)
		{
			if(file.isDirectory())
			{
				
			}
			else
			{
				
			}
			
			// string of the address of any file in the folder.
			String csvfilepath=file.getCanonicalPath();
			BufferedReader br=null;
			String line= "";
			String csvSplitBy= ",";
			
			try {
				int count=0;// this count will help me know if in in ID line or headlines line
				br= new BufferedReader(new FileReader(csvfilepath));
				while((line = br.readLine()) != null)
				{
					//use comma as separator
					String[] Getline= line.split(csvSplitBy);
					if(count==0)
					{
						String ID=Getline[2];
						IDlist.add(ID);
						IDsplit.add(inputfiles.size());
						count++;
					}
					else if(count==1)
					{
						//skip headlines line
						count++;
					}
					else
					{
						//here i actually read the data from the files
						ArrayList<String> insides=new ArrayList<String>();
						for(int i=0; i<=10; i++)
						{
							insides.add(Getline[i]);
						}
						inputfiles.add(insides);
					}
				}
				
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally {
				if(br!=null)
				{
					try {
						br.close();
					} catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
		
		// sort to make each line sorted by signal strength.
		bubbleSort(inputfiles);
		return inputfiles;
	}
	
	// read the lists if ArrayList from files and inserting all the data to order hashmap.
	public HashMap<position,List<Scan>> makemap(List<ArrayList<String>> list)
	{
		HashMap<position,List<Scan>> map=new HashMap();
		int counter=0;
		int max=IDsplit.size();
		for(int i=0; i<list.size(); i++)
		{
			double lat=Double.parseDouble(list.get(i).get(6));
			double lon=Double.parseDouble(list.get(i).get(7));
			double alt=Double.parseDouble(list.get(i).get(8));
			String ID=IDlist.get(counter);
			if(counter<max-1)
			{
				if(i==IDsplit.get(counter+1) & i!=0)
				{
					//by this, if i know when to switch to the next id
					counter++;
					ID=IDlist.get(counter);
				}
			}
			String time=list.get(i).get(3);
			position P=new position(lat,lon,alt,ID,time);
			if(map.containsKey(P))
			{
				continue;
			}
			else
			{
				ArrayList<Scan> scanlist=new ArrayList<Scan>();
				int countscans=1;
				for(int j=0; j<list.size() & countscans<=10; j++)
				{
					double lat1=Double.parseDouble(list.get(j).get(6));
					double lon1=Double.parseDouble(list.get(j).get(7));
					double alt1=Double.parseDouble(list.get(j).get(8));
					String Time1=list.get(j).get(3);
					position temp=new position(lat1,lon1,alt1,ID,Time1);
					if(P.equalposition(temp)==true & countscans!=11)
					{
						String SSID=list.get(j).get(1);
						String MAC=list.get(j).get(0);
						int freq=Integer.parseInt(list.get(j).get(4));
						int Signal=Integer.parseInt(list.get(j).get(5));
						Scan S=new Scan(SSID,MAC,freq,Signal);
						scanlist.add(S);
						countscans++;
					}
				}
				map.put(P, scanlist);
			}
		}
		return map;
	}
	
	// take the hashmap and write all details to csv. 
	public void makefinaltable(HashMap<position,List<Scan>> map) throws IOException
	{
		FileWriter writer = new FileWriter(outputpath);
		List<String> headlines = new ArrayList<>();
		headlines.add("Latitude");
		headlines.add("Longitude");
		headlines.add("Altitude");
		headlines.add("ID");
		headlines.add("Time");
		for(int i=1; i<=10; i++)
		{
			headlines.add("SSID"+i);
			headlines.add("MAC"+i);
			headlines.add("Frequency"+i);
			headlines.add("Signal"+i);
		}
		String collectheadlines = headlines.stream().collect(Collectors.joining(","));
		writer.write(collectheadlines);
		writer.write("\n");
		for(Entry<position, List<Scan>> entry: map.entrySet())
		{
			String collect=entry.getKey()+","+entry.getValue();
			writer.write(collect);
			writer.write("\n");
		}
		writer.close();
	}
	
	public void makefinaltable(List<ArrayList<String>> filteredlist) throws IOException
	{
		FileWriter writer = new FileWriter(outputpath);
		List<String> headlines = new ArrayList<>();
		headlines.add("Latitude");
		headlines.add("Longitude");
		headlines.add("Altitude");
		headlines.add("ID");
		headlines.add("Time");
		for(int i=1; i<=10; i++)
		{
			headlines.add("SSID"+i);
			headlines.add("MAC"+i);
			headlines.add("Frequency"+i);
			headlines.add("Signal"+i);
		}
		String collectheadlines = headlines.stream().collect(Collectors.joining(","));
		writer.write(collectheadlines);
		writer.write("\n");
		for(int i=0; i<filteredlist.size(); i++)
		{
			String Collect="";
			for(int j=0; j<filteredlist.get(i).size(); j++)
			{
				Collect = Collect + filteredlist.get(i).get(j)+",";
			}
			writer.write(Collect);
			writer.write("\n");
		}
		writer.close();
	}
	
	
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//TESTING//
		
		
		
		InputCsv test=new InputCsv(inputpath);
		List<ArrayList<String>> testlist=test.importfiles();
		HashMap<position,List<Scan>> test2=test.makemap(testlist);
		test.makefinaltable(test2);
		
		
		//System.out.println(test2.size());
//		System.out.println(testlist.size());
		//System.out.println(IDlist);
		//System.out.println(IDsplit);

		
		
	}
	
	static void bubbleSort(List<ArrayList<String>> list)
	{
		int n=list.size();
		ArrayList<String> temp=new ArrayList<String>();
		for(int i=0; i<n; i++)
		{
			for(int j=1; j< (n-1); j++)
			{
				int num1=Integer.parseInt(list.get(j-1).get(5));
				int num2=Integer.parseInt(list.get(j).get(5));
				if(num1<num2)
				{
					//swap elements
					temp = list.get(j-1);
					list.set(j-1, list.get(j));
					list.set(j, temp);
				}
			}
		}
	}

}
