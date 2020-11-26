package twg2.parser.condition;

/** A token parser, used to determine whether a series of input token match a requirement.<br>
 * An instance of this interface must keep track of previous tokens passed to {@link #acceptNext(Object)}
 * and return false once the set of tokens forms an invalid sequence.
 * @author TeamworkGuy2
 * @since 2015-12-12
 * @param <T_INPUT> the type of input tokens parsed by this parser
 * @param <T_RESULT> the type of result object that parsed data is store in
 */
public interface TokenParser<T_INPUT, T_RESULT> extends ParserCondition {

	/**
	 * @param token the token to parse
	 * @return true if the char was accepted, false if not.
	 * If false is returned, this {@code CharParser} enters a failed state and
	 * will not return true for any further inputs
	 */
	public boolean acceptNext(T_INPUT token);


	/**
	 * @return a destination object containing the parsed data
	 */
	public T_RESULT getParserResult();


	@Override
	public TokenParser<T_INPUT, T_RESULT> copy();


	/** Recycle this condition. Resetting it back to its default state. 
	 * Default interface method throws {@link UnsupportedOperationException}
	 * @return This condition recycled back to its default state
	 */
	@Override
	public default TokenParser<T_INPUT, T_RESULT> recycle() {
		throw new UnsupportedOperationException("TokenParser recycling not supported");
	}


	@Override
	public default TokenParser<T_INPUT, T_RESULT> copyOrReuse() {
		if(this.canRecycle()) {
			return this.recycle();
		}
		else {
			return this.copy();
		}
	}

}
