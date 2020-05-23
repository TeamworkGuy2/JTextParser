package twg2.parser.textFragment;

/** Consumer function which accepts coordinates (offset, length, line number and column number) of a sub-string
 * @see TextTransformer
 * @author TeamworkGuy2
 * @since 2015-5-27
 */
@FunctionalInterface
public interface TextFragmentConsumer {

	/** Receives coordinates for a text sub-string
	 * @param off 0 based absolute offset of the text fragment
	 * @param len length of the text fragment in characters
	 * @param lineStart the starting line number, 0 based
	 * @param columnStart the starting line offset, 0 based
	 * @param lineEnd the ending line number, inclusive, 0 based
	 * @param columnEnd the ending line offset, inclusive, 0 based
	 */
	public void accept(int off, int len, int lineStart, int columnStart, int lineEnd, int columnEnd);

}
