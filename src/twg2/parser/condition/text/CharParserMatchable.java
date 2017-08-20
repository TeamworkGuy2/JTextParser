package twg2.parser.condition.text;

/** Checks if a character matches the requirements for the first character of a {@link CharParser}
 * @author TeamworkGuy2
 * @since 2015-2-14
 */
public interface CharParserMatchable extends CharParser {

	public CharParserPredicate getFirstCharMatcher();

}
