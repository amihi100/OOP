

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Filters {

	public List<ArrayList<String>> Data;

	public Filters(List<ArrayList<String>> data)
	{
		this.Data=data;
	}
	public Filters()
	{
		this.Data=new ArrayList<ArrayList<String>>();
	}
	
	//this function filter my data by range of locations
	public void FilterLoc(double latmin,double latmax,double lonmin,double lonmax,double altmin,double altmax,boolean not)
	{
		List<ArrayList<String>> myans=new ArrayList<ArrayList<String>>();
		if(!not) // not button is not clicked
		{
			for(int i=0; i<this.Data.size(); i++)
			{
				double lat=Double.parseDouble(this.Data.get(i).get(0));
				double lon=Double.parseDouble(this.Data.get(i).get(1));
				double alt=Double.parseDouble(this.Data.get(i).get(2));
				if((lat>=latmin && lat<=latmax) && (lon>=lonmin && lon<=lonmax) && (alt>=altmin && alt<=altmax))
				{
					myans.add(this.Data.get(i));
				}
			}
			this.Data=myans;
		}
		else //  not button is clicked
		{
			for(int i=0; i<this.Data.size(); i++)
			{
				double lat=Double.parseDouble(this.Data.get(i).get(0));
				double lon=Double.parseDouble(this.Data.get(i).get(1));
				double alt=Double.parseDouble(this.Data.get(i).get(2));
				if((lat<=latmin || lat>=latmax) || (lon<=lonmin || lon>=lonmax) || (alt<=altmin || alt>=altmax))
				{
					myans.add(this.Data.get(i));
				}
			}
			this.Data=myans;
		}
	}
	
	//this function filter my data by ID name
	public void FilterID(String ID,boolean not)
	{
		List<ArrayList<String>> myans=new ArrayList<ArrayList<String>>();
		if(!not) // not button is not clicked
		{
			for(int i=0; i<this.Data.size(); i++)
			{
				if(this.Data.get(i).get(3).contains(ID))
				{
					myans.add(this.Data.get(i));
				}
			}
			this.Data=myans;
		}
		else // not button is clicked
		{
			for(int i=0; i<Data.size(); i++)
			{
				if(!(Data.get(i).get(3).contains(ID)))
				{
					myans.add(this.Data.get(i));
				}
			}
			this.Data=myans;
		}
	}

	//this function filter my data by time range
	public void FilterTime(String MinTime,String MaxTime,boolean not)
	{
		List<ArrayList<String>> myans=new ArrayList<ArrayList<String>>();
		LocalTime minimum=LocalTime.of(Integer.parseInt(MinTime.substring(0,2)),Integer.parseInt(MinTime.substring(3,5)),Integer.parseInt(MinTime.substring(6)));
		LocalTime maximum=LocalTime.of(Integer.parseInt(MaxTime.substring(0,2)),Integer.parseInt(MaxTime.substring(3,5)),Integer.parseInt(MaxTime.substring(6)));
		if(!not) //not button is not clicked
		{
			for(int i=0; i<this.Data.size(); i++)
			{
				LocalTime datatime=LocalTime.of(Integer.parseInt(this.Data.get(i).get(4).substring(11,13)),Integer.parseInt(Data.get(i).get(4).substring(14,16)));
				if(datatime.isAfter(minimum) && datatime.isBefore(maximum))
				{
					myans.add(this.Data.get(i));
				}
			}
			this.Data=myans;
		}
		else // not button is clicked
		{
			for(int i=0; i<this.Data.size(); i++)
			{
				LocalTime datatime=LocalTime.of(Integer.parseInt(this.Data.get(i).get(4).substring(11,13)),Integer.parseInt(Data.get(i).get(4).substring(14,16))/*,Integer.parseInt(Data0.get(i).get(4).substring(17,19))*/);
				if(datatime.isAfter(maximum) || datatime.isBefore(minimum))
				{
					myans.add(this.Data.get(i));
				}
			}
			this.Data=myans;
		}
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//filter test is found on main function on class MakeKML
	}
	
	
	public static List<ArrayList<String>> MergeData(List<ArrayList<String>> Data1, List<ArrayList<String>> Data2)
	{
		List<ArrayList<String>> Merged=new ArrayList<ArrayList<String>>();
		for(int i=0; i<Data1.size(); i++)
		{
			Merged.add(Data1.get(i));
		}
		for(int j=0; j<Data2.size(); j++)
		{
			if(!(Merged.contains(Data2.get(j))))
			{
				Merged.add(Data2.get(j));
			}
		}
		return Merged;
	}

}
