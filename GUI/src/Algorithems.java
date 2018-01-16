
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

public class Algorithems {

	public List<ArrayList<String>> Data;

	public Algorithems()
	{
		MakeKML test=new MakeKML("/Users/amihaitorgeman/Desktop/NewMatala/Output/first.csv");
		this.Data=test.inputthefile();
	}
	public Algorithems(List<ArrayList<String>> Data)
	{
		this.Data=Data;
	}

	//algo 1: the input is Mac adress and the output is its estimated location
	public Point MacLocation(String MAC) // algo 1
	{
	//mylist = list of macs identical to the input mac.
		// signals = list of signals and their indexes that correspond with the mylist macs
		List<ArrayList<String>> Mylist=new ArrayList<ArrayList<String>>();
		List<S_id> signals=new ArrayList<S_id>();
		for(int i=0; i<Data.size(); i++)
		{
			for(int j=6; j<Data.get(i).size(); j+=4)
			{
				if(MAC.equals(Data.get(i).get(j)))
				{
					if(!Mylist.contains(Data.get(i)))
					{
						Mylist.add(Data.get(i));
						int signal;
						if(Data.get(i).get(j+2).length()==4)
						{
							String temp=Data.get(i).get(j+2);
							String finish=temp.substring(0,3);
							signal=Integer.parseInt(finish);
						}
						else if(Data.get(i).get(j+2).length()==5)
						{
							String temp=Data.get(i).get(j+2);
							String finish=temp.substring(0,4);
							signal=Integer.parseInt(finish);
						}
						else
						{
							signal=Integer.parseInt(Data.get(i).get(j+2));
						}
						S_id temp=new S_id(signal,Mylist.size()-1);
						signals.add(temp);
					}
				}
			}
		}
		if(Mylist.isEmpty())
		{
			Point ans=new Point();
			return ans;
		}
		S_idbubbleSort(signals);
		double[] Weight=new double[signals.size()];
		Point[] Pointarr=new Point[Mylist.size()];
		for(int i=0; i<signals.size(); i++)
		{
			Weight[i]=1.0/(Math.pow(signals.get(i).getSignal(), 2));
			double Lat=Double.parseDouble(Mylist.get(signals.get(i).getIndex()).get(0)); // 0=lat
			double Lon=Double.parseDouble(Mylist.get(signals.get(i).getIndex()).get(1)); 	// 1=lon
			double Alt=Double.parseDouble(Mylist.get(signals.get(i).getIndex()).get(2));   //2= alt
			Point point=new Point(Lat,Lon,Alt);
			Pointarr[i]=point;
		}
		Point answer=WeightPoint(Pointarr,Weight);
		return answer;
	}

	
	public Point WeightPoint(Point[] pointarr, double[] weight)
	{
		Point WCenter=new Point();  
		double Latmone=0;
		double Latmehane=0;
		for(int i=0; i<weight.length; i++)
		{
			Latmone+=(pointarr[i].getLat())*(weight[i]);
			Latmehane+=weight[i];
		}
		WCenter.lat=Latmone/Latmehane;//calculate centered weight Lat
		double Lonmone=0;
		double Lonmehane=0;
		for(int i=0; i<weight.length; i++)
		{
			Lonmone+=(pointarr[i].getLon())*(weight[i]);
			Lonmehane+=weight[i];
		}
		WCenter.lon=Lonmone/Lonmehane;//calculate centered weight Lon
		double Altmone=0;
		double Altmehane=0;
		for(int i=0; i<weight.length; i++)
		{
			Altmone+=(pointarr[i].getAlt())*(weight[i]);
			Latmehane+=weight[i];
		}
		WCenter.alt=Altmone/Latmehane;//calculate centered weight Alt
		return WCenter;
	}

	//algo 2= calculate the user location based on given couples of mac and signals
	public Point GetUserLocation(Mac_Signal[] arr) // algo 2
	{
		//run on the input arr of Mac_Signal and for each object i find all the equal macs and add their signals
		List<ArrayList<Mac_Signal>> mylist=new ArrayList<ArrayList<Mac_Signal>>();
		for(int k=0; k<arr.length; k++)
		{
			ArrayList<Mac_Signal> inner=new ArrayList<Mac_Signal>();
			for(int i=0; i<this.Data.size(); i++)
			{
				for(int j=6; j<this.Data.get(i).size(); j+=4)
				{
					if(Data.get(i).get(j).equals(arr[k].getMac()))
					{
						String mac=Data.get(i).get(j);
						int signal;
						if(Data.get(i).get(j+2).length()==4)
						{
							String temp=Data.get(i).get(j+2);
							String finish=temp.substring(0,3);
							signal=Integer.parseInt(finish);
						}
						else if(Data.get(i).get(j+2).length()==5)
						{
							String temp=Data.get(i).get(j+2);
							String finish=temp.substring(0,4);
							signal=Integer.parseInt(finish);
						}
						else
						{
							signal=Integer.parseInt(Data.get(i).get(j+2));
						}
						Mac_Signal temp=new Mac_Signal(mac,signal);
						inner.add(temp);
					}

				}
			}
			// if list is empty , I set the signal to be -10000 to count it as very small weight 
			if(inner.isEmpty())
			{
				Mac_Signal temp0=new Mac_Signal(arr[k].getMac());
				inner.add(temp0);
			}
			mylist.add(inner);
		}
		List<Integer> indexes=new ArrayList<Integer>();
		for(int i=0; i<mylist.size(); i++)
		{
		    int index=indexofclosest(arr[i].getSignal(),mylist.get(i));
		    indexes.add(index);
		}
		String[] myMacs=new String[arr.length];
		double[] Weight=new double[arr.length];
		for(int i=0; i<mylist.size(); i++)
		{
			myMacs[i]=mylist.get(i).get(indexes.get(i)).getMac();
			Weight[i]=1.0/(Math.pow(mylist.get(i).get(indexes.get(i)).getSignal(), 2));
		}
		Point[] WPoints=new Point[arr.length];
		for(int i=0; i<WPoints.length; i++)
		{
			WPoints[i]=MacLocation(myMacs[i]);
		}
		Point answer=WeightPoint(WPoints,Weight);
		return answer;
	}
	
	public static void main(String[] args) throws FileNotFoundException {	
	
		Algorithems test=new Algorithems();
		//test algo 1
		String Mac="10:fe:ed:62:9e:1a";
		Point testpoint=test.MacLocation(Mac);
		System.out.println(testpoint);
		String fakemac="10:fe:ed:61:9e:1a";
		Point testfake=test.MacLocation(fakemac);
		System.out.println(testfake);
		//test algo 2
		String Mac1="14:ae:db:3d:b1:52";
		String Mac2="00:1d:aa:7c:8c:f8";
		String Mac3="0a:8d:db:65:89:a9";
		int Signal1=-87;
		int Signal2=-79;
		int Signal3=-88;
		Mac_Signal[] test2=new Mac_Signal[3];
		test2[0]=new Mac_Signal(Mac1,Signal1);
		test2[1]=new Mac_Signal(Mac2,Signal2);
		test2[2]=new Mac_Signal(Mac3,Signal3);
		Point testalgo2=test.GetUserLocation(test2);
		System.out.println(testalgo2);
	}

	
	
	public static void S_idbubbleSort(List<S_id> arr)
	{  
		int n = arr.size(); 
		S_id temp;
		for(int i=0; i < n; i++)
		{  
			for(int j=1; j < (n-i); j++)
			{ 
				int n1=(arr.get(j-1).signal);
				int n2=(arr.get(j).signal);
				if(n1 < n2){  
					//swap elements  
					temp = arr.get(j-1); 
					arr.set(j-1, arr.get(j));  
					arr.set(j, temp);
				}  

			}  
		}  
	}

	
	public static int indexofclosest(int signal,ArrayList<Mac_Signal> list)
	{
		int index=-1;
		int min=Integer.MAX_VALUE;
		int maxmin=Integer.MAX_VALUE;
		for(int i=0; i<list.size(); i++)
		{
			min=Math.abs(list.get(i).getSignal()-signal);
			if(min<maxmin)
			{
				maxmin=min;
				index=i;
			}
		}
		return index;
	}
}
