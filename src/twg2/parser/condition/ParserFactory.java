package twg2.parser.condition;

/** This interface contains a factory method to create {@link ParserCondition} implementations.
 * @author TeamworkGuy2
 * @since 2015-2-9
 */
public interface ParserFactory<T extends ParserCondition> {

	/** Whether this parser factory creates compound parser conditions.
	 * @return true if the element parsed by this condition can contain sub-elements, i.e. if other elements can be parsed from within the source of this element,
	 * false if this condition cannot contain sub-elements
	 */
	public boolean isCompound();


	/** Each call creates a new {@code ParserCondition}
	 * @return a new {@link ParserCondition} that match against this {@code ParserFactory}
	 */
	public T createParser();


	/** Return an existing {@link ParserCondition} for reuse if possible.
	 * Implementations are not required to do anything if they so choose.<br>
	 * Factories which wish to implement parser reuse or pooling can provide an implementation.
	 * So it is recommended that callers always call this method to benefit from any
	 * performance optimizations by the implementation.
	 * @param parser the parser condition to return
	 */
	public default void returnParser(T parser) {
		// no-op
	}

}
