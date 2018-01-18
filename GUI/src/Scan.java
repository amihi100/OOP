// in this class I will save data of SSID, MAC, Frequency and signal.

public class Scan {

	public String SSID;
	public String MAC;
	public int Freq;
	public int Signal;
	
	// constructor
	public Scan(String ssid,String mac,int f,int s)
	{
	
		this.SSID=ssid;
		this.MAC=mac;
		this.Freq=f;
		this.Signal=s;
	}
	
	//print data as string.
	@Override
	public String toString()
	{
		return "" + SSID + "," + MAC + "," + Freq + "," + Signal + "";
		
		}
}
