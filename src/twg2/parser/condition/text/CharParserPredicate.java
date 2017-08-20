package twg2.parser.condition.text;

import java.util.function.BiPredicate;

import twg2.parser.textParser.TextParser;

/** {@link BiPredicate} for characters read from a {@link TextParser}
 * @author TeamworkGuy2
 * @since 2017-08-20
 */
@FunctionalInterface
public interface CharParserPredicate {

	/** Check whether the current character (last call to {@link TextParser#nextChar()}) passes this condition
	 * @param ch the character to test
	 * @param parser the parser the character was read from
	 * @return whether the character allowed by this condition
	 */
	public boolean test(char ch, TextParser parser);

}
