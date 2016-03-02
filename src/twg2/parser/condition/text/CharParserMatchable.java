package twg2.parser.condition.text;

import twg2.functions.BiPredicates;
import twg2.parser.textParser.TextParser;

/** Checks if a character matches the requirements for the first character of a {@link CharParser}
 * @author TeamworkGuy2
 * @since 2015-2-14
 */
public interface CharParserMatchable extends CharParser {

	public BiPredicates.CharObject<TextParser> getFirstCharMatcher();

}
