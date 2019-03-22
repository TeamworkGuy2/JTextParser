package twg2.parser.textParser;

/** Represents the absolution and line/column position of a text parser within text stream
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




	public static class Impl implements ParserPos {
		int position;
		int lineNumber;
		int columnNumber;

		public Impl() {
		}


		public Impl(int position, int lineNumber, int columnNumber) {
			this.position = position;
			this.lineNumber = lineNumber;
			this.columnNumber = columnNumber;
		}


		@Override
		public int getPosition() {
			return position;
		}


		public void setPosition(int position) {
			this.position = position;
		}


		@Override
		public int getLineNumber() {
			return lineNumber;
		}


		public void setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
		}


		@Override
		public int getColumnNumber() {
			return columnNumber;
		}


		public void setColumnNumber(int columnNumber) {
			this.columnNumber = columnNumber;
		}


		@Override
		public String toString() {
			return "ParserPos: { position: " + position + ", lineNum: " + lineNumber + ", colNumber: " + columnNumber + " }";
		}

	}

}
