// in this class I will create the KML file.
// line 30 is the output path of the file.
// and line 179 is for testing path.
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

public class MakeKML {
	
	public String path;
	
	public MakeKML(String path)
	{
		this.path=path;
	}
	
	// ****FOR testing in different OS , Change path of UOTPUT of the bellow******
	//OUTPUT PATH:
	static String halfkmloutputpath ="/Users/amihaitorgeman/Desktop/NewMatala/Output/";
	
	
	
	static String kmloutputpath = halfkmloutputpath + "outputKML.kml";

	// import file from PC the and declare line as string and inserting strings list of ArrayLoist.
	public List<ArrayList<String>> inputthefile()
	{
		List<ArrayList<String>> mylist=new ArrayList<ArrayList<String>>();
		String csvfile=this.path;
		BufferedReader br=null;
		String line = "";
		String csvsplitby= ",";
		int count=0;
		
		try {
			// read the file by buffer reader function.
			br=new BufferedReader(new FileReader(csvfile));
			while((line = br.readLine()) != null)
			{
				String[] input = line.split(csvsplitby);
				if(count==0)//skip headlines
				{
					count++;
					continue;
				}
				else // we passed the headlines.
				{
					ArrayList<String> inside = new ArrayList<String>();
					for(int i=0; i<input.length; i++)
					{
						inside.add(input[i]);
					}
					mylist.add(inside);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		finally {
			if(br != null)
			{
			
				try {
					br.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return mylist;
	}
	
	public List<ArrayList<String>> inputthefile(String path)
	{
		List<ArrayList<String>> mylist=new ArrayList<ArrayList<String>>();
		String csvfile=path;
		BufferedReader br=null;
		String line = "";
		String csvsplitby= ",";
		int count=0;
		
		try {
			
			br=new BufferedReader(new FileReader(csvfile));
			while((line = br.readLine()) != null)
			{
				String[] input = line.split(csvsplitby);
				if(count==0)//skip headlines
				{
					count++;
					continue;
				}
				else
				{
					ArrayList<String> inside = new ArrayList<String>();
					for(int i=0; i<input.length; i++)
					{
						inside.add(input[i]);
					}
					mylist.add(inside);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		finally {
			if(br != null)
			{
			
				try {
					br.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return mylist;
	}
	
	// using the filtered list and creating with it KML file:
	public void writetoKML(List<ArrayList<String>> filteredlist) throws FileNotFoundException
	{
		final Kml kml=new Kml();
		Document doc=kml.createAndSetDocument().withName("points");
		for(int i=0; i<filteredlist.size(); i++)
		{
			String position=filteredlist.get(i).get(1)+","+filteredlist.get(i).get(0);
			String time=filteredlist.get(i).get(4);
			String realtime=TimeSet(time);
			Timestamp t=Timestamp.valueOf(realtime);
			Placemark p=KmlFactory.createPlacemark();
			p.createAndSetTimeStamp().addToTimeStampSimpleExtension(t);
			doc.createAndAddPlacemark().withName("point"+i).withOpen(Boolean.TRUE).withTimePrimitive(p.getTimePrimitive())
			.createAndSetPoint().addToCoordinates(position);
		}
		kml.marshal(new File(kmloutputpath));
		
	}
	
	public int wificount(List<ArrayList<String>> mylist)
	{
		List<String> counter=new ArrayList<String>();
		for(int i=0; i<mylist.size(); i++)
		{
			for(int j=6; j<mylist.get(i).size(); j+=4)
			{
				if(!(counter.contains(mylist.get(i).get(j))))
				{
					counter.add(mylist.get(i).get(j));
				}
			}
		}
		return counter.size();
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		MakeKML test=new MakeKML("/Users/amihaitorgeman/Desktop/NewMatala/Output/outputCSV.csv");
		List<ArrayList<String>> test0=test.inputthefile();
	
		// TESTING // 
		
		
		//System.out.println(test0.size());
		//filters:
		//filter by id: lenovo
		//Filters test2=new Filters(test0);
	//	test2.FilterID("Lenovo", false);
		//filter by time: 16:20:00-16:30:00
		
		// User must enter 00 as seconds.
		//test2.FilterTime("11:20:00", "13:30:00", true);
		///test0=test2.Data;
		//InputCsv test4=new InputCsv("/Users/amihaitorgeman/Desktop/NewMatala/Input");
		//test4.makefinaltable(test0);
		test.writetoKML(test0);
	}
	// Fixing the problem with time template (seconds  issue).
	public static String TimeSet(String time)
	{
		if(time.length()==16)// no seconds
		{
			String year=time.substring(6, 10);
			String day=time.substring(0,2);
			String month=time.substring(3,5);
			String ans=year+"-"+month+"-"+day+" "+time.substring(11)+":00";
			return ans;
		}
		else//yes seconds
		{
			String year=time.substring(0, 4);
			String day=time.substring(8,10);
			String month=time.substring(5,7);
			String ans=year+"-"+month+"-"+day+" "+time.substring(11);
			return ans;
		}
	}

}
