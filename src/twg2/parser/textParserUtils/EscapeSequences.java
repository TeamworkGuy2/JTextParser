package twg2.parser.textParserUtils;

import java.util.function.Function;

/** Static decoder functions for character data such as escape sequences (i.e. {@code \t, \r, \n}, etc.)
 * @author TeamworkGuy2
 * @since 2014-9-1
 */
public final class EscapeSequences {
	public static final char newline = '\n';
	public static final char escapeStart = '\\';
	public static final char unicodeStart = 'u';
	/** the valid escape characters that can come after a '\', other than unicode escape sequences */
	public static final char[] escapeIdentifiers = new char[] { 't', 'b', 'n', 'r', 'f', '\'', '\"', '\\' };
	/** the escape characters created by the escape sequences in {@link #escapeIdentifiers} */
	public static final char[] escapeChars = new char[] { '\t', '\b', '\n', '\r', '\f', '\'', '\"', '\\' };
	public static final char[] unicodeLiterals = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final UnicodeEscapeDecoder defaultDecoderInst = new UnicodeEscapeDecoder();


	private EscapeSequences() { throw new AssertionError("cannot instantiate static class EscapeSequences"); }


	/** Returns a function that decodes unicode escape sequences and escape identifiers in a string.<br>
	 * The escape sequences (e.g. {@code \ b, \ t}) must be in the Java 8 language specification (§3.10.6) format.
	 * Note: octal escape sequences are not supported.
	 * The unicode escape sequences must be in the format ({@code \ u x x x}) specified by the
	 * Java 8 language specification (§3.3) - Unicode Escapes
	 * @return the input string with all unicode escape sequences replace with corresponding characters
	 */
	public static final Function<String, String> unicodeEscapeDecoder() {
		return defaultDecoderInst;
	}

}
