package twg2.parser.textFragment;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

@AllArgsConstructor
public class TextFragmentRefImpl implements TextFragmentRef {
	private final @Getter int offsetStart;
	private final @Getter int offsetEnd;
	private final @Getter int lineStart;
	private final @Getter int columnStart;
	private final @Getter int lineEnd;
	private final @Getter int columnEnd;


	@Override
	public TextFragmentRefImpl copy() {
		TextFragmentRefImpl copy = new TextFragmentRefImpl(offsetStart, offsetEnd, lineStart, columnStart, lineEnd, columnEnd);
		return copy;
	}


	@Override
	public CharSequence getText(char[] chars, int offset, int length) {
		if(offsetStart < offset) { throw new IndexOutOfBoundsException("offset " + offsetStart + ", expected " + offset + " or greater"); }
		if(offsetEnd > offset + length) { throw new IndexOutOfBoundsException("end offset " + offsetEnd + ", expected " + (offset + length) + " or less"); }
		return new String(chars, offsetStart, offsetEnd - offsetStart);
	}


	@Override
	public CharSequence getText(CharSequence chseq) {
		return chseq.subSequence(offsetStart, offsetEnd);
	}


	@Override
	public CharSequence getText(List<? extends CharSequence> lines) {
		val res = TextFragmentRef.getText(offsetStart, offsetEnd, lineStart, lineEnd, columnStart, columnEnd, lines);
		return res;
	}


	@Override
	public String toString(CharSequence chseq) {
		return TextFragmentRef._toString(offsetStart, offsetEnd, lineStart, lineEnd, columnStart, columnEnd, chseq);
	}


	@Override
	public String toString() {
		return TextFragmentRef._toString(offsetStart, offsetEnd, lineStart, lineEnd, columnStart, columnEnd);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columnEnd;
		result = prime * result + columnStart;
		result = prime * result + lineEnd;
		result = prime * result + lineStart;
		result = prime * result + offsetEnd;
		result = prime * result + offsetStart;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TextFragmentRef)) { return false; }
		val frag = (TextFragmentRef)obj;
		return this.offsetStart == frag.getOffsetStart() &&
			this.offsetEnd == frag.getOffsetEnd() &&
			this.columnStart == frag.getColumnStart() &&
			this.columnEnd == frag.getColumnEnd() &&
			this.lineStart == frag.getLineStart() &&
			this.lineEnd == frag.getLineEnd();
	}

}