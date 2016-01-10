package twg2.parser.textParserUtils;

/**
 * @author TeamworkGuy2
 * @since 2014-8-10
 */
public class SearchRange {
	int initialRange;
	int low;
	int high;


	public SearchRange() {
	}


	public SearchRange(int lowInclusive, int highExclusive) {
		this.low = lowInclusive;
		this.high = highExclusive;
	}


	public void reset(int highExclusive) {
		this.low = 0;
		this.high = highExclusive - 1;
		this.initialRange = highExclusive - low;
	}


	public void setLimit(int compare, int index) {
		if(compare < 0 && index > low) {
			this.low = index;
		}
	}


	public int getMid() {
		// weighted toward the lower end of the search range as the search range closes upward
		int range = (high - low);
		int mid = ((low + high) >>> 1) - (range > 1 ? (int)(initialRange/range * 0.25f) : 0);
		mid = mid < low ? low : mid;

		// TODO debugging
		System.out.println("range: low=" + low + " high=" + high + " mid=" + mid + " rng=" + range);

		return mid;
	}

}