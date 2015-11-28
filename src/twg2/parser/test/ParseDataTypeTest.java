package twg2.parser.test;

import java.io.IOException;
import java.util.function.BiFunction;

import twg2.parser.textParser.TextParser;
import twg2.parser.textParser.TextParserImpl;

/**
 * @author TeamworkGuy2
 * @since 2015-1-20
 */
public class ParseDataTypeTest {


	public static void readIntTest(BiFunction<TextParser, StringBuilder, Object> parser) {
		@SuppressWarnings("unused")
		long[] vals = new long[] {     132_32,     0_312___4,        0x2_F_3A1L,     0X3aacab3F3L,      0xCAB33BABEL };
		String[] strs = new String[] { "132_32a", "0_312___4 this", "0x2_F_3A1L b", "0X3aacab3F3L is", "0xCAB33BABEL" };
		String[] expd = new String[] { "132_32",  "0_312___4",      "0x2_F_3A1L",   "0X3aacab3F3L",    "0xCAB33BABEL" };

		StringBuilder strB = new StringBuilder();

		for(int i = 0, size = strs.length; i < size; i++) {
			strB.setLength(0);
			@SuppressWarnings("unused")
			Object obj = parser.apply(TextParserImpl.of(strs[i]), strB);
			check(strs[i], strB.toString(), expd[i], "int");
			//System.out.println("type '" + strs[i] + "': " + obj);
		}
	}


	public static void readFloatTest(BiFunction<TextParser, StringBuilder, Object> parser) {
		@SuppressWarnings("unused")
		double[] vals = new double[] { 15_37.4_6e8_2,     -3_4.98e-54D,       0x2_F_3.39b1p1D,     0x32A5.86p143D,      234234.433444D };
		String[] strs = new String[] {"15_37.4_6e8_2 a", "-3_4.98e-54D ths", "0x2_F_3.39b1p1D s", "0x32A5.86p143D__ ", "234234.433444D" };
		String[] expd = new String[] {"15_37.4_6e8_2",   "-3_4.98e-54D",     "0x2_F_3.39b1p1D",   "0x32A5.86p143D",    "234234.433444D" };

		StringBuilder strB = new StringBuilder();

		for(int i = 0, size = strs.length; i < size; i++) {
			strB.setLength(0);
			@SuppressWarnings("unused")
			Object obj = parser.apply(TextParserImpl.of(strs[i]), strB);
			check(strs[i], strB.toString(), expd[i], "float");
			//System.out.println("type '" + strs[i] + "': " + obj);
		}
	}


	public static void check(String initial, String parsed, String expected, String name) {
		if(!expected.equals(parsed)) {
			throw new Error("parsing: " + initial + ", result: " + parsed + ", should be: " + expected);
		}
		//System.out.println("'" + initial + "' to " + name + ": " + parsed);
	}


	public static void main(String[] args) throws IOException {
		new TextParserTest().positionTest();
		/*
		NumericLiteral numParser = new NumericLiteral();
		readIntTest((in, dest) -> numParser.readElement(in, dest));
		readFloatTest((in, dest) -> numParser.readElement(in, dest));

		IntegerLiteral intParser = new IntegerLiteral();
		//readIntTest((in, dest) -> intParser.readElement(in, dest));
		FloatingPointLiteral floatParser = new FloatingPointLiteral();
		//readFloatTest((in, dest) -> floatParser.readElement(in, dest));
		if(3/2 != 3) {
			return;
		}

		char aaa = '\377';
		char aa = '\uu1234';
		char a = '\u1234';
		String st = "\"\\\\a\\\" b\\u012345\"";
		System.out.println(st + " c=" + aa + ", " + (int)aa);

		File file = new File("res/parseInput.txt");
		LineBuffer lineReader = new LineBufferImpl(new BufferedReader(new FileReader(file)), EscapeSequences.unicodeEscapeDecoder(), true);
		List<LineBufferParserWithMarkers> parsers = new ArrayList<LineBufferParserWithMarkers>();
		parsers.add(new CharLiteral());
		parsers.add(StringLiteral.createJavaStringParser(false));
		parsers.add(new WhiteSpaceParser(new ParserHelper()));
		parsers.add(CommentParser.createJavaMultiLineCommentParser(true));

		HashMap<char[], LineBufferParserWithMarkers> markers = new HashMap<>(64, 0.5f);
		for(LineBufferParserWithMarkers parser : parsers) {
			for(String str : parser.getStartMarkers()) {
				markers.put(str.toCharArray(), parser);
			}
		}

		int i = 0;
		StringBuilder strBldrTemp = new StringBuilder();
		while(true) {
			final char[] line = lineReader.nextLine();
			final int lineOff = lineReader.getLineOffset();
			if(line == null) {
				break;
			}
			//System.out.println("Parsing: " + line);
			lineReader.unreadCurrentLine();

			for(Map.Entry<char[], LineBufferParserWithMarkers> entry : markers.entrySet()) {
				char[] str = entry.getKey();
				LineBufferParserWithMarkers parser = entry.getValue();
				// TODO remove
				//System.out.println("comp: " + line + " vs. '" + str + "' " + line.startsWith(str));
				if(StringCompare.startsWith(line, lineOff, str, 0)) {
					parser.readElement(lineReader, strBldrTemp);
					System.out.println(parser.getClass().getSimpleName() + ": " + strBldrTemp.toString());
					break;
				}
			}
			i++;
			if(i > 100) { break; }
		}

		lineReader.close();
		*/
	}

}
