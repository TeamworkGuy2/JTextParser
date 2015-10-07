package twg2.parser.textParserUtils;

/**
 * @author TeamworkGuy2
 * @since 2014-8-10
 */
public class SearchRange {
	int initialRange;
	int low;
	int high;


	public void reset(int high) {
		this.low = 0;
		this.high = high-1;
		this.initialRange = high - low;
	}


	public void setLimit(int compare, int index) {
		if(compare < 0 && index > low) {
			this.low = index;
		}
	}


	public int getMid() {
		// weighted toward the lower end of the search range as the search range closes upward
		int range = (high - low);
		int mid = ((low + high) >>> 1) - (range != 0 ? (int)(initialRange/range * (0.25f)) : 0);
		mid = mid < low ? low : mid;
		System.out.println("range: low=" + low + " high=" + high + " mid=" + mid);
		return mid;
	}

}