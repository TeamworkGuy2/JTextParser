package twg2.parser.textFragment;

/** A function which takes a {@link CharSequence} with offset and length and returns a result
 * @see TextConsumer
 * @author TeamworkGuy2
 * @since 2015-5-27
 */
@FunctionalInterface
public interface TextTransformer<T> {

	public T apply(CharSequence text, int textOff, int textLen);

}
