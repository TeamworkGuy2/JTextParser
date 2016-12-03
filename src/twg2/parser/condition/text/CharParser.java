package twg2.parser.condition.text;

import twg2.parser.condition.ParserCondition;
import twg2.parser.textFragment.TextFragmentRef;
import twg2.parser.textParser.TextParser;

/** A char parser, used to determine whether a series of characters match a requirement.<br>
 * A {@code CharParser} is stateful and must keep track of previous characters passed to {@link #acceptNext(char, TextParser)}
 * and return false once the set of characters forms an invalid sequence.
 * @author TeamworkGuy2
 * @since 2015-2-10
 */
public interface CharParser extends ParserCondition {

	/**
	 * @param ch the character to parse
	 * @return true if the char was accepted, false if not.
	 * If false is returned, this {@code CharParser} enters a failed state and
	 * will not return true for any further inputs
	 */
	public boolean acceptNext(char ch, TextParser pos);


	/**
	 * @return the {@link TextParser} coordinates span from the first matching character to the last matching character
	 */
	public TextFragmentRef getMatchedTextCoords();


	/**
	 * @return a {@link StringBuilder} containing all of the characters matched by {@link #acceptNext(char, TextParser)}
	 */
	public StringBuilder getParserDestination();


	/** Set the destination buffer where {@link #acceptNext(char, TextParser)} stores matching characters
	 * @param parserDestination the destination buffer to use
	 */
	public void setParserDestination(StringBuilder parserDestination);


	@Override
	public CharParser copy();


	@Override
	public default CharParser recycle() {
		throw new UnsupportedOperationException("CharParser recycling not supported");
	}


	@Override
	public default CharParser copyOrReuse() {
		CharParser filter = null;
		if(this.canRecycle()) {
			filter = this.recycle();
		}
		else {
			filter = this.copy();
		}
		return filter;
	}


	public default boolean readConditional(TextParser buf) {
		return readConditional(buf, null);
	}


	public default boolean readConditional(TextParser buf, StringBuilder dst) {
		int off = dst != null ? dst.length() : 0;
		int count = 0;
		while(!this.isComplete()) {
			if(!buf.hasNext()) {
				return false;
			}
			count++;
			char ch = buf.nextChar();
			if(!this.acceptNext(ch, buf)) {
				buf.unread(count);
				if(dst != null) {
					dst.setLength(off);
				}
				return false;
			}
			if(dst != null) {
				dst.append(ch);
			}
		}
		return true;
	}

}
