# Change Log
All notable changes to this project will be documented in this file.
This project does its best to adhere to [Semantic Versioning](http://semver.org/).


--------
###[0.10.0](N/A) - 2016-10-29
#### Added
* twg2.parser.Inclusion - enum { INCLUSION, EXCLUSION } moved from JParserCode

#### Changed
* Move TextFragmentRef Impl and ImplMut sub-classes into separate TextFragmentRefImpl and TextFragmentRefImplMut source files
* Updated jarray-util dependency to jarrays


--------
###[0.9.0](https://github.com/TeamworkGuy2/JTextParser/commit/31345bf6e53f3ab419a0cde0dc99d67277239fa7) - 2016-09-11
#### Added
* twg2.parser.textParser.TextCharsParser - a fully char[] based TextParser implementation, hopefully with better performance than TextIteratorParser
* twg2.parser.textStream.LineCounter - a stateful function that accepts characters and returns line numbers as '\n' characters are encountered and keeps track of a list of offsets where each newline starts
* Added TextFragmentRef.getText(char[], int, int)

#### Changed
* Renamed twg2.parser.textParser TextParserImpl -> TextIteratorParser
* Moved some of the test classes around, made TextParserTest a dual implementation testing class for TextIteratorParser and TextCharsParser
* Split nextIf*() methods out of TextParser interface into new TextParserConditionals interface
  * moved nextIf*() methods from TextIteratorParser (i.e. old TextParserImpl) into TextParserConditionalsDefault interface as default methods since they all just use hasNext(), nextChar(), unread(), and other TextParser methods so they can just be default methods on an interface
  * now TextIteratorParser and TextCharsParser implement this new TextParserConditionalsDefault interface and it's default nextIf*() methods


--------
###[0.8.2](https://github.com/TeamworkGuy2/JTextParser/commit/65c75d28db4c919fd225944762a0ba57e80842d2) - 2016-08-27
#### Changed
* Updated to use latest dependencies and dependency paths


--------
###[0.8.1](https://github.com/TeamworkGuy2/JTextParser/commit/8609d66ef57071440baa27d24faced84fe8e781f) - 2016-06-26
#### Changed
* Improved ReadUnescape documentation and changed ReadMatchingTest
* Switched from versions.md to CHANGELOG.md format, see http://keepachangelog.com/


--------
###[0.8.0](https://github.com/TeamworkGuy2/JTextParser/commit/30313e409b66854c996c35526076fdfc48199775) - 2016-03-02
Moving away from line based parsing toward stream/chunk based parsing

#### Added
* Added LineReader hasPrevChar() and prevChar()

#### Changed
* Renamed ParserStartChars -> CharParserMatchable, which now extends CharParser (instead of old CharParser.WithMarks interface)

#### Removed
* Removed LineReader isUnmodifiedLine(), getLineOffset(), getLineLength(), and getLineRemaining()
* Removed TextParser.hasNextLine()


--------
###[0.7.0](https://github.com/TeamworkGuy2/JTextParser/commit/391997f99f5c4284732417c5bfe44a6430caa072) - 2016-02-23
#### Added
* Added StringLineSupplier from JStreamish and added new CharsLineSupplier and LineSupplier base class
* Added TextParserImpl support for PeekableIterator<char[]> sources in addition to existing PeekableIterator<String> support

#### Changed
* Moved twg2.parser.test package into separate test directory

#### Removed
* Removed LineReader.substring()
* Removed TextParserImpl currentLine and nextLine fields
* Removed TextParserImpl constructors in favor of static factory methods


--------
###[0.6.0](https://github.com/TeamworkGuy2/JTextParser/commit/ce375f75724a7aeae15722fbbd492a5a7530d1d7) - 2016-02-21
Moving toward stream/chunk based parsing rather than line based

#### Changed
* Updated to use latest version of JStreamish
  * Added new StringLineSupplier constructor flags to static TextParserImpl methods

#### Removed
* Removed TextParser.hasNextLine()
* Removed TextParserImpl 'transform' and 'injectNewlines' constructor parameters


--------
###[0.5.1](https://github.com/TeamworkGuy2/JTextParser/commit/384b85ffa5d606e89173e7cdbe0003487f86d7ed) - 2016-01-16
#### Added
* Moved twg2.parser.condition, twg2.parser.condition.text, and twg2.parser.textFragment interfaces and default implementations into this library from the ParserTools library to allow reuse in other libraries such as JParserDataTypeLike


--------
###[0.5.0](https://github.com/TeamworkGuy2/JTextParser/commit/caf97ef82556fbd36f978a74af53d82ae7a6161a) - 2016-01-10
#### Added
* Added additional tests

#### Removed
* Removed unneeded midIndex parameter from ReadMatching.binaryStartsWith()
* Removed up some old commented out debugging statements
