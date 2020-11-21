package twg2.parser.condition.text;

/** Checks if a character matches the requirements for the first character of a {@link CharParser}
 * @author TeamworkGuy2
 * @since 2015-2-14
 */
public interface CharParserMatchable extends CharParser {

	/**
	 * @return a {@link CharParserPredicate} which tests whether a char matches the beginning of this parser
	 */
	public CharParserPredicate getFirstCharMatcher();


	/**
	 * @return an array of chars which match the beginning of this parser or null if there is not a representative list of characters to match the beginning of this char parser
	 */
	public char[] getFirstChars();

}
