
public class Scan {

	public String SSID;
	public String MAC;
	public int Freq;
	public int Signal;
	
	//בנאי שיוצר טיפוס מסוג scan
	public Scan(String ssid,String mac,int f,int s)
	{
	
		this.SSID=ssid;
		this.MAC=mac;
		this.Freq=f;
		this.Signal=s;
	}
	
	@Override
	public String toString()
	{
		return "" + SSID + "," + MAC + "," + Freq + "," + Signal + "";
		
		}
}
