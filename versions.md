--------
####0.6.0
date: 2016-2-21

commit: ?

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