
public class Point {
	
	public double lat;
	public double lon;
	public double alt;
	
	public Point()
	{
		this.lat=-1.0;
		this.lon=-1.0;
		this.alt=-1.0;
	}
	
	public Point(double lat, double lon, double alt)
	{
		this.lat=lat;
		this.lon=lon;
		this.alt=alt;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getAlt() {
		return alt;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

	@Override
	public String toString() {
		return "Point [lat=" + lat + ", lon=" + lon + ", alt=" + alt + "]";
	}
	

}
