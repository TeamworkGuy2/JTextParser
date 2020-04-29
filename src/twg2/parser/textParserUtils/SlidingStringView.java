package twg2.parser.textParserUtils;

import twg2.arrays.ArrayUtil;

/**
 * @author TeamworkGuy2
 * @since 2015-6-12
 */
public class SlidingStringView {
	private int currentZeroActualOffset;
	private StringBuilder cache;
	private int maxSize;


	public SlidingStringView(int maxSize) {
		this(0, maxSize);
	}


	public SlidingStringView(int currentOffset, int maxSize) {
		this(currentOffset, maxSize, new StringBuilder(maxSize > 100000 ? 100000 : maxSize));
	}


	public SlidingStringView(int currentOffset, int maxSize, StringBuilder cache) {
		if(maxSize < 1) {
			throw new IllegalArgumentException("maxSize must be greater than 0: " + maxSize);
		}
		this.currentZeroActualOffset = currentOffset;
		this.maxSize = maxSize;
		this.cache = cache;
	}


	public int getPosition() {
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
		makeRoomFor(1);
		this.cache.append(ch);
	}


	public void append(char[] chars) {
		append(chars, 0, chars.length);
	}


	/** Append sub-array of characters to this string view.
	 * If the length of this input is larger than the buffer, only the last N values equal to the cache size are added.
	 * The internal offset is adjusted to reflect the full number of characters added.
	 */
	public void append(char[] chars, int off, int len) {
		ArrayUtil.checkBoundsThrows(chars, off, len);

		makeRoomFor(len);

		int overflowSize = len - maxSize;
		if(overflowSize > 0) {
			off += overflowSize;
			len = maxSize;
		}

		this.cache.append(chars, off, len);
	}


	/**
	 * @see {{@link #append(CharSequence, int, int)}
	 */
	public void append(CharSequence str) {
		append(str, 0, str.length());
	}


	/** Append sub-string to this string view.
	 * If the length of this input is larger than the buffer, only the last N values equal to the cache size are added.
	 * The internal offset is adjusted to reflect the full number of characters added.
	 */
	public void append(CharSequence str, int off, int len) {
		makeRoomFor(len);

		int overflowSize = len - maxSize;
		if(overflowSize > 0) {
			off += overflowSize;
			len = maxSize;
		}

		this.cache.append(str, off, off + len);
	}


	private final void makeRoomFor(int len) {
		int newSize = getCacheSize() + len;
		if(newSize > maxSize) {
			int trimCount = newSize - maxSize;
			currentZeroActualOffset += trimCount;

			if(len >= maxSize) {
				cache.setLength(0);
			}
			else {
				cache.delete(0, trimCount);
			}
		}
	}


	@Override
	public String toString() {
		return "offset=" + currentZeroActualOffset + ", " + cache.toString();
	}

}
