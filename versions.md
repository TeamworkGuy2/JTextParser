--------
####0.8.0
date: 2016-3-2

commit: ?

Moving toward stream/chunk based parsing
* Renamed ParserStartChars -> CharParserMatchable, which now extends CharParser (instead of old CharParser.WithMarks interface)
* Removed LineReader isUnmodifiedLine(), getLineOffset(), getLineLength(), and getLineRemaining()
* Added LineReader hasPrevChar() and prevChar()
* Removed TextParser.hasNextLine()


--------
####0.7.0
date: 2016-2-23

commit: 391997f99f5c4284732417c5bfe44a6430caa072

* Removed LineReader.substring()
* Removed TextParserImpl currentLine and nextLine fields
* Added StringLineSupplier from JStreamish and added new CharsLineSupplier and LineSupplier base class
* Moved twg2.parser.test package into separate test directory
* Removed TextParserImpl constructors in favor of static factory methods
* Added TextParserImpl support for PeekableIterator<char[]> sources in addition to existing PeekableIterator<String> support


--------
####0.6.0
date: 2016-2-21

commit: ce375f75724a7aeae15722fbbd492a5a7530d1d7

Moving toward stream/chunk based parsing rather than line based
* Removed TextParser.hasNextLine()
* Removed TextParserImpl 'transform' and 'injectNewlines' constructor parameters
* Updated to use latest version of JStreamish
  * Added new StringLineSupplier constructor flags to static TextParserImpl methods


--------
####0.5.1
date: 2016-1-16

commit: 384b85ffa5d606e89173e7cdbe0003487f86d7ed

* Moved twg2.parser.condition, twg2.parser.condition.text, and twg2.parser.textFragment interfaces and default implementations into this library from the ParserTools library to allow reuse in other libraries such as JParserDataTypeLike


--------
####0.5.0
date: 2016-1-10

commit: caf97ef82556fbd36f978a74af53d82ae7a6161a

* Added additional tests
* Removed unneeded midIndex parameter from ReadMatching.binaryStartsWith()
* Removed up some old commented out debugging statements