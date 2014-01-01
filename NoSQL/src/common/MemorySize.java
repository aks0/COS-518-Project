package common;

enum Size {
	KB (1024),
	MB (1048576),
	GB (1073741824);

	private final long numBytes;
	Size(long numBytes) {
		this.numBytes = numBytes;
	}

	public long getNumBytes() {
		return numBytes;
	}

}

public class MemorySize {
	private double coefficient;
	private Size size;
	
	public MemorySize(long numBytes) {
		if (numBytes >= Size.GB.getNumBytes()) {
			this.size = Size.GB;
			this.coefficient = (double)numBytes/Size.GB.getNumBytes();
		}
		else if (numBytes >= Size.MB.getNumBytes()) {
			this.size = Size.MB;
			this.coefficient = (double)numBytes/Size.MB.getNumBytes();
		}
		else {
			this.size = Size.KB;
			this.coefficient = (double)numBytes/Size.KB.getNumBytes();
		}
	}

	public double getCoefficient() {
		return coefficient;
	}

	public Size getSize() {
		return size;
	}
	
	public long getBytes() {
		return (long)coefficient * size.getNumBytes();
	}
}
