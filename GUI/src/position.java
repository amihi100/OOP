// in this class I will save data of latitude, longitude, altitude , ID and time.
public class position {
	



	
	public double Lat;
	public double Lon;
	public double Alt;
	public String ID;
	public String Time;

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(Alt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(Lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(Lon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		position other = (position) obj;
		if (Double.doubleToLongBits(Alt) != Double.doubleToLongBits(other.Alt))
			return false;
		if (Double.doubleToLongBits(Lat) != Double.doubleToLongBits(other.Lat))
			return false;
		if (Double.doubleToLongBits(Lon) != Double.doubleToLongBits(other.Lon))
			return false;
		return true;
	}

	// constructor for type of position.
	public position(double lat,double lon,double alt,String id, String time)
	{
		this.Lat=lat;
		this.Lon=lon;
		this.Alt=alt;
		this.ID=id;
		this.Time=time;
	}
	
	// check if position is equal
	public boolean equalposition(position P)
	{
		if(this.Lat==P.Lat & this.Lon==P.Lon & this.Alt==P.Alt)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public double getLat() {
		return Lat;
	}

	public void setLat(double lat) {
		Lat = lat;
	}

	public double getLon() {
		return Lon;
	}

	public void setLon(double lon) {
		Lon = lon;
	}

	public double getAlt() {
		return Alt;
	}

	public void setAlt(double alt) {
		Alt = alt;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	@Override
	public String toString() {
		return "" + Lat + "," + Lon + "," + Alt + "," + ID + ","  + Time + "";
	}
	
	
	// Calculate the distance bt position.
	public double Distance(position P)
	{
		double plat=Math.pow(this.Lat-P.Lat, 2);
		double plon=Math.pow(this.Lon-P.Lon, 2);
		return Math.sqrt(plat+plon);
	}
	
	
}
