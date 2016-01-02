package twg2.parser.textParserUtils;

import twg2.arrays.ArrayUtil;

/**
 * @author TeamworkGuy2
 * @since 2015-6-12
 */
public class SlidingStringView {
	private long currentZeroActualOffset;
	private StringBuilder cache;
	private int maxSize;


	public SlidingStringView(int maxSize) {
		this(0, maxSize);
	}


	public SlidingStringView(long currentOffset, int maxSize) {
		this(currentOffset, maxSize, new StringBuilder(maxSize > 4096 ? 4096 : maxSize));
	}


	public SlidingStringView(long currentOffset, int maxSize, StringBuilder cache) {
		if(maxSize < 1) {
			throw new IllegalArgumentException("maxSize must be greater than 0: " + maxSize);
		}
		this.currentZeroActualOffset = currentOffset;
		this.maxSize = maxSize;
		this.cache = cache;
	}


	public long length() {
		return currentZeroActualOffset + cache.length();
	}


	public int getCacheSize() {
		return cache.length();
	}


	/**
	 * @param startIndex inclusive
	 * @param endIndex exclusive
	 */
	public String substring(int startIndex, int endIndex) {
		int curSize = getCacheSize();
		if(startIndex < currentZeroActualOffset) {
			throw new IndexOutOfBoundsException("start index to small, min=" + currentZeroActualOffset + ", max=" + (currentZeroActualOffset + curSize) + ", startIndex=" + startIndex);
		}
		if(endIndex > currentZeroActualOffset + curSize) {
			throw new IndexOutOfBoundsException("end index to large, min=" + currentZeroActualOffset + ", max=" + (currentZeroActualOffset + curSize) + ", endIndex=" + endIndex);
		}

		return cache.substring((int)(startIndex - currentZeroActualOffset), (int)(endIndex - currentZeroActualOffset));
	}


	public String getContent() {
		return cache.toString();
	}


	public void append(char ch) {
		// if the length of this single input is larger than the buffer, only insert the last N values equal to the cache size
		makeRoomFor(1);
		this.cache.append(ch);
	}


	public void append(char[] chars) {
		append(chars, 0, chars.length);
	}


	public void append(char[] chars, int off, int len) {
		ArrayUtil.checkBoundsThrows(chars, off, len);

		// if the length of this single input is larger than the buffer, only insert the last N values equal to the cache size
		if(len > maxSize) {
			int overflowSize = (len - maxSize);
			off += overflowSize;
			currentZeroActualOffset += overflowSize;
			len = maxSize;
			cache.setLength(0);
		}
		makeRoomFor(len);
		this.cache.append(chars, off, len);
	}


	public void append(CharSequence str) {
		append(str, 0, str.length());
	}


	public void append(CharSequence str, int off, int len) {
		// if the length of this single input is larger than the buffer, only insert the last N values equal to the cache size
		if(len > maxSize) {
			int overflowSize = (len - maxSize);
			off += overflowSize;
			currentZeroActualOffset += overflowSize;
			len = maxSize;
			cache.setLength(0);
		}
		makeRoomFor(len);
		this.cache.append(str, off, off + len);
	}


	private final void makeRoomFor(int len) {
		int newSize = getCacheSize() + len;
		if(newSize > maxSize) {
			int trimCount = newSize - maxSize;
			currentZeroActualOffset += (trimCount);
			cache.delete(0, trimCount);
		}
	}


	@Override
	public String toString() {
		return "offset=" + currentZeroActualOffset + ", " + cache.toString();
	}

}
