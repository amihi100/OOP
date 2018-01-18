// this class made for algorithm 1 
// getting signal and index to calculate


public class Signal_index {

	public int signal;
	public int index;
	
	// constructor 
	public Signal_index(int signal, int index)
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
