package twg2.parser.condition.text;

import java.util.function.BiPredicate;

import twg2.parser.textParser.TextParser;

/** {@link BiPredicate}{@code <char, TextParser>} for characters read from a {@link TextParser}
 * @author TeamworkGuy2
 * @since 2017-08-20
 */
@FunctionalInterface
public interface CharParserPredicate {

	/** Check whether the current character (last call to {@link TextParser#nextChar()}) passes this condition
	 * @param ch the character to test
	 * @param parser the parser the character was read from
	 * @return true if the character is accepted, false if not
	 */
	public boolean test(char ch, TextParser parser);

}
