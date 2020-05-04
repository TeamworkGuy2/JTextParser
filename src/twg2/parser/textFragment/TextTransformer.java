package twg2.parser.textFragment;

/** A function which takes a {@link CharSequence} with offset and length and returns a result
 * @see TextConsumer
 * @author TeamworkGuy2
 * @since 2015-5-27
 */
@FunctionalInterface
public interface TextTransformer<T> {

	/** Transform a text sub-sequence
	 * @param text the {@link CharSequence}
	 * @param textOff offset (inclusive) into {@code text} at which valid input begins
	 * @param textLen the number of valid '{@code text}' characters starting from '{@code textOff}'
	 * @return the transformed result
	 */
	public T apply(CharSequence text, int textOff, int textLen);

}
