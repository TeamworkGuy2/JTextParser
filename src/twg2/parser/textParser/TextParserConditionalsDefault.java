package twg2.parser.textParser;

import java.io.IOException;
import java.io.UncheckedIOException;

import twg2.arrays.ArrayUtil;
import twg2.functions.predicates.CharPredicate;

/** Default implementations of the conditional {@code nextIf*()} methods in {@link TextParser}
 * @author TeamworkGuy2
 * @since 2016-09-10
 */
public interface TextParserConditionalsDefault extends TextParser, TextParserConditionals {


	@Override
	public default boolean nextIf(char ch) {
		return nextIf(ch, 1, null) == 1;
	}


	@Override
	public default int nextIf(char ch, Appendable dst) {
		return nextIf(ch, 1, dst);
	}


	@Override
	public default int nextIf(char ch, char ch2, Appendable dst) {
		return nextIf(ch, ch2, 1, dst);
	}


	@Override
	public default int nextIf(char[] chars, Appendable dst) {
		return nextIf(chars, 0, chars.length, dst);
	}


	@Override
	public default int nextIf(char[] chars, int off, int len, Appendable dst) {
		return nextIf(chars, off, len, 0, dst);
	}


	@Override
	public default int nextIf(char ch, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;

			while(i < count && hasNext()) {
				if(ch == nextChar()) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public default int nextIf(char ch, char ch2, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char c = 0;

			while(i < count && hasNext()) {
				if(((c = nextChar()) == ch || c == ch2)) {
					if(dst != null) {
						dst.append(c);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public default boolean nextIf(String str) {
		int count = str.length(); //(count == 0 ? Integer.MAX_VALUE : count);
		int i = 0;

		if(!hasNext()) {
			return false;
		}

		for(i = 0; i < count && hasNext() && (nextChar() == str.charAt(i)); i++) {
		}

		if(i != count) {
			unread(i + 1);
			return false;
		}
		return true;
	}


	@Override
	public default int nextIf(char[] chars, int count, Appendable dst) {
		return nextIf(chars, 0, chars.length, count, dst);
	}


	@Override
	public default int nextIf(char[] chars, int off, int len, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if(ArrayUtil.indexOf(chars, off, len, (ch = nextChar())) > -1) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public default int nextIf(CharPredicate condition, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if(condition.test((ch = nextChar()))) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public default int nextIfNot(char c, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if((ch = nextChar()) != c) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public default int nextIfNot(char c, char c2, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if((ch = nextChar()) != c && ch != c2) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public default int nextIfNot(char c, char c2, char c3, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if((ch = nextChar()) != c && ch != c2 && ch != c3) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public default int nextIfNot(char[] chars, int count, Appendable dst) {
		return nextIfNot(chars, 0, chars.length, count, dst);
	}


	@Override
	public default int nextIfNot(char[] chars, int off, int len, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if(ArrayUtil.indexOf(chars, off, len, (ch = nextChar())) == -1) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public default int nextIfNotPrecededBy(char endCh, char escCh, boolean dropEscChars, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char prevCh = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if((ch = nextChar()) != endCh || prevCh == escCh) {
					if(dst != null) {
						if(i > 0 && (!dropEscChars || (prevCh != escCh || ch != endCh))) {
							dst.append(prevCh);
						}
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
				prevCh = ch;
			}

			if(dst != null && i > 0) {
				dst.append(prevCh);
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public default int nextIfNotPrecededBy(char endCh, char escCh, char stopCh, boolean dropEscChars, int count, Appendable dst) {
		return nextIfNotPrecededBy(endCh, escCh, stopCh, stopCh, dropEscChars, count, dst);
	}


	@Override
	public default int nextIfNotPrecededBy(char endCh, char escCh, char stopCh, char stopCh2, boolean dropEscChars, int count, Appendable dst) {
		return nextIfNotPrecededBy(endCh, escCh, stopCh, stopCh2, stopCh2, dropEscChars, count, dst);
	}


	@Override
	public default int nextIfNotPrecededBy(char endCh, char escCh, char stopCh, char stopCh2, char stopCh3, boolean dropEscChars, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char prevCh = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if((ch = nextChar()) != stopCh && ch != stopCh2 && ch != stopCh3 && (ch != endCh || prevCh == escCh)) {
					if(dst != null) {
						if(i > 0 && (!dropEscChars || (prevCh != escCh || ch != endCh))) {
							dst.append(prevCh);
						}
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
				prevCh = ch;
			}

			if(dst != null && i > 0) {
				dst.append(prevCh);
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	/** Read matching characters between two inclusive values
	 * @param lower the lower inclusive character
	 * @param upper the upper inclusive character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store read characters in
	 * @return the number of characters read
	 */
	@Override
	public default int nextBetween(char lower, char upper, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;
	
			while(i < count && hasNext()) {
				if((ch = nextChar()) >= lower && ch <= upper) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	/** Read matching characters between two inclusive ranges of values
	 * @param lower the lower inclusive character
	 * @param upper the upper inclusive character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store read characters in
	 * @return the number of characters read
	 */
	public default int nextBetween(char lower, char upper, char lower2, char upper2, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;
	
			while(i < count && hasNext()) {
				if(((ch = nextChar()) >= lower && ch <= upper) || (ch >= lower2 && ch <= upper2)) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
