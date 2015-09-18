package textParser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TeamworkGuy2
 * @since 2015-9-17
 */
public interface ParserPos {

	/**
	 * @return absolute character offset, including newlines, from the beginning of the parser input stream, indexed from 0
	 */
	public int getPosition();


	/**
	 * @return current line number, indexed from 1
	 */
	public int getLineNumber();


	/**
	 * @return character offset into current line, indexed from 1
	 */
	public int getColumnNumber();


	public static ParserPos newInst() {
		return new Impl();
	}


	public static ParserPos of(int absoluteOff, int lineNum, int columnPos) {
		return new Impl(absoluteOff, lineNum, columnPos);
	}




	@AllArgsConstructor
	@NoArgsConstructor
	public static class Impl implements ParserPos {
		@Getter @Setter int position;
		@Getter @Setter int lineNumber;
		@Getter @Setter int columnNumber;


		@Override
		public String toString() {
			return "ParserPos: { position: " + position + ", lineNum: " + lineNumber + ", colNumber: " + columnNumber + " }";
		}

	}

}
