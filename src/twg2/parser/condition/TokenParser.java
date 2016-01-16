package twg2.parser.condition;

/**
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


	@Override
	public default TokenParser<T_INPUT, T_RESULT> recycle() {
		throw new UnsupportedOperationException("TokenParser recycling not supported");
	}


	@Override
	public default TokenParser<T_INPUT, T_RESULT> copyOrReuse() {
		TokenParser<T_INPUT, T_RESULT> filter = null;
		if(this.canRecycle()) {
			filter = this.recycle();
		}
		else {
			filter = this.copy();
		}
		return filter;
	}

}
