
public class RowData {
	//-----here i created an object that gets the information in the rows---------//
	
	
	private String mac, ssid, time, type, lat, lon, alt, channel;
	private int  rssi;
	
	//-------constructor of the RowData class-------//
	public RowData(String mac,String ssid, String time, String type, String lat, String lon, String alt, String channel, int rssi ) {
		this.mac= mac;
		this.ssid=ssid;
		this.time=time;
		this.type= type;
		this.lat=lat;
		this.lon=lon;
		this.alt=alt;
		this.channel=channel;
		this.rssi=rssi;
		
		
	}
	
	//--------a function that gets two objects and copies the information from the original object to the other/copy object--------///
	public void copyRowData(RowData o, RowData c) {
		c.setMac(o.getMac());
		c.setSsid(o.getSsid());
		c.setTime(o.getTime());
		c.setType(o.getType());
		c.setLon(o.getLon());
		c.setLat(o.getLat());
		c.setAlt(o.getAlt());
		c.setChannel(o.getChannel());
		c.setRssi(o.getRssi());
	}
	

	//--------function that prints all the information in the row object/ -for personal use--------------// 
	public void print() {
		System.out.println(mac + " ," + ssid+ " ," + time+ " ," +type+ " ," + lon+ " ," + lat+ " ," + alt+ " ," + channel+ " ," + rssi );
	}
	
	
	//------------getters and setter for all variables in row object---------------///
	public String getMac() {
		return mac;
	}


	public void setMac(String mac) {
		this.mac = mac;
	}




	public String getTime() {
		return time;
	}


	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public void setTime(String time) {
		this.time = time;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getLat() {
		return lat;
	}


	public void setLat(String lat) {
		this.lat = lat;
	}


	public String getLon() {
		return lon;
	}


	public void setLon(String lon) {
		this.lon = lon;
	}


	public String getAlt() {
		return alt;
	}


	public void setAlt(String alt) {
		this.alt = alt;
	}


	public String getChannel() {
		return channel;
	}


	public void setChannel(String channel) {
		this.channel = channel;
	}


	public int getRssi() {
		return rssi;
	}


	public void setRssi(int rssi) {
		this.rssi = rssi;
	}


	public String getSsid() {
		return ssid;
	}

}

