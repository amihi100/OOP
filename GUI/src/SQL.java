
public class SQL {
	
	public String IP;
	public String URL;
	public String User;
	public String Password;
	public String Port;
	public String DataBase;
	
	public SQL(String ip,String user,String password,String port,String database)
	{
		this.IP=ip;
		this.User=user;
		this.Password=password;
		this.Port=port;
		this.DataBase=database;
		this.URL="jdbc:mysql://" + ip +":"+ port + "/" + database; 
	}

	
	
	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getPort() {
		return Port;
	}

	public void setPort(String port) {
		Port = port;
	}

	public String getDataBase() {
		return DataBase;
	}

	public void setDataBase(String dataBase) {
		DataBase = dataBase;
	}

}
