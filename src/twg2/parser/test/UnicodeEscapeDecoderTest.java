package twg2.parser.test;

import org.junit.Assert;
import org.junit.Test;

import twg2.parser.textParserUtils.UnicodeEscapeDecoder;

/**
 * @author TeamworkGuy2
 * @since 2016-1-10
 */
public class UnicodeEscapeDecoderTest {

	@Test
	public void decodeTest() {
		StringBuilder sb = new StringBuilder();
		UnicodeEscapeDecoder.escape("abc\n123", 0, sb);
		Assert.assertEquals("abc\n123", sb.toString());

		sb.setLength(0);
		UnicodeEscapeDecoder.escape("abc\n\\u2460", 0, sb);
		Assert.assertEquals("abc\n\u2460", sb.toString());

		sb.setLength(0);
		UnicodeEscapeDecoder.escape("abc\n\\u1F3B8", 0, sb);
		Assert.assertEquals("abc\n\u1F3B8", sb.toString());
	}

}
