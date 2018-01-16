
public class S_id {

	public int signal;
	public int index;
	
	public S_id(int signal, int index)
	{
		this.signal=signal;
		this.index=index;
	}

	@Override
	public String toString() {
		return "S_id [signal=" + signal + ", index=" + index + "]";
	}

	public int getSignal() {
		return signal;
	}

	public void setSignal(int signal) {
		this.signal = signal;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
