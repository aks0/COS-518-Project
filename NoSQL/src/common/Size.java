package common;

public enum Size {
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
	
	public String toString() {
		if (this == Size.KB)
			return "KB";
		else if (this == Size.MB)
			return "MB";
		else
			return "GB";
	}

}
