
public class Mac_Signal {
	
	public String Mac;
	public int Signal;

	public Mac_Signal(String mac, int Signal)
	{
		this.Signal=Signal;
		this.Mac=mac;
	}
	
	public Mac_Signal(String Mac)
	{
		this.Mac=Mac;
		this.Signal=-10000;
	}

	public String getMac() {
		return Mac;
	}

	public void setMac(String mac) {
		Mac = mac;
	}

	public int getSignal() {
		return Signal;
	}

	public void setSignal(int signal) {
		Signal = signal;
	}

	@Override
	public String toString() {
		return "Mac_Signal [Mac=" + Mac + ", Signal=" + Signal + "]";
	}
	
	
	
}
