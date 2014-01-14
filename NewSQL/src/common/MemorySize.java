package common;

public class MemorySize {
	private double coefficient;
	private Size size;
	
	public MemorySize(double c, Size size) {
		this.coefficient = c;
		this.size = size;
		
		coefficient = Rounder.round(coefficient, 3);
	}
	
	public MemorySize(long numBytes) {
		if (numBytes >= Size.GB.getNumBytes()) {
			size = Size.GB;
			coefficient = (double)numBytes/Size.GB.getNumBytes();
		}
		else if (numBytes >= Size.MB.getNumBytes()) {
			size = Size.MB;
			coefficient = (double)numBytes/Size.MB.getNumBytes();
		}
		else {
			size = Size.KB;
			coefficient = (double)numBytes/Size.KB.getNumBytes();
		}
		
		coefficient = Rounder.round(coefficient, 3);
	}

	public double getCoefficient() {
		return coefficient;
	}

	public Size getSize() {
		return size;
	}
	
	public long getBytes() {
		return (long)(coefficient * size.getNumBytes());
	}
	
	public String toString() {
		return coefficient + size.toString();
	}
}
