package twg2.parser.condition.text;

import twg2.parser.condition.ParserCondition;
import twg2.parser.textFragment.TextFragmentRef;
import twg2.parser.textParser.TextParser;

/** A char parser is used to determine whether a series of characters match a requirement.<br>
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


	@Override
	public CharParser copy();


	/** Recycle this char parser. Resetting it back to its default state. 
	 * Default interface method throws {@link UnsupportedOperationException}
	 * @return This char parser recycled back to its default state
	 */
	@Override
	public default CharParser recycle() {
		throw new UnsupportedOperationException("CharParser recycling not supported");
	}


	@Override
	public default CharParser copyOrReuse() {
		if(this.canRecycle()) {
			return this.recycle();
		}
		else {
			return this.copy();
		}
	}


	/** Read a {@link TextParser} until it {@link #isComplete()} or until {@link #acceptNext(char, TextParser)} returns false.
	 * The text parser {@code buf} is reset to its initial position if a complete token is not found.
	 * @param buf the {@link TextParser} to read input from
	 * @return true if a complete token is read, false if not
	 */
	public default boolean readConditional(TextParser buf) {
		int count = 0;
		while(!this.isComplete()) {
			if(!buf.hasNext()) {
				return false;
			}
			count++;
			char ch = buf.nextChar();
			if(!this.acceptNext(ch, buf)) {
				buf.unread(count);
				return false;
			}
		}
		return true;
	}


	/** Read a {@link TextParser} until it {@link #isComplete()} or until {@link #acceptNext(char, TextParser)} returns false.
	 * The {@code dst} buffer is filled with the accepted input if a complete token is read. The text parser {@code buf} and {@code dst}
	 * buffer are reset to their initial position and length if a complete token is not found.
	 * @param buf the {@link TextParser} to read input from
	 * @param dst the destination buffer to store matched input in
	 * @return true if a complete token is read, false if not
	 */
	public default boolean readConditional(TextParser buf, StringBuilder dst) {
		int off = dst.length();
		int count = 0;
		while(!this.isComplete()) {
			if(!buf.hasNext()) {
				return false;
			}
			count++;
			char ch = buf.nextChar();
			if(!this.acceptNext(ch, buf)) {
				buf.unread(count);
				dst.setLength(off);
				return false;
			}
			dst.append(ch);
		}
		return true;
	}

}
