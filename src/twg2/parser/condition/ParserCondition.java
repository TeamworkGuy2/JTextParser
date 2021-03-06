package twg2.parser.condition;

/** A token parser, used to determine whether a series of input token match a requirement.<br>
 * An instance of this interface must keep track of previous tokens passed to an 'accept' method implemented by sub-classes
 * and return false once the set of tokens forms an invalid sequence.
 * @author TeamworkGuy2
 * @since 2015-12-12
 * @see ParserFactory
 */
public interface ParserCondition {

	/**
	 * @return the name of this parser condition, for display and debugging purposes
	 */
	public String name();

	/**
	 * @return true if this precondition filter has been successfully completed/matched, false if not
	 * @see #isFailed()
	 */
	public boolean isComplete();


	/**
	 * @return true if this precondition cannot accept any further input to 'accept'
	 * That is, 'accept' will return false for any input
	 */
	public boolean isFailed();


	/**
	 * @return Create a copy of this condition
	 */
	public ParserCondition copy();


	/**
	 * @return Whether this condition can be recycled
	 * @see #recycle()
	 */
	public boolean canRecycle();


	/** Recycle this condition. Resetting it back to its default state. 
	 * Default interface method throws {@link UnsupportedOperationException}
	 * @return This condition recycled back to its default state
	 */
	public ParserCondition recycle();


	/** Based on {@link #canRecycle()}, return the result of {@link #recycle()} or {@link #copy()}
	 * @return this condition, reset to its default state, or a new copy of this condition
	 */
	public default ParserCondition copyOrReuse() {
		if(this.canRecycle()) {
			return this.recycle();
		}
		else {
			return this.copy();
		}
	}

}
