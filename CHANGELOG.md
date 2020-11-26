# Change Log
All notable changes to this project will be documented in this file.
This project does its best to adhere to [Semantic Versioning](http://semver.org/).


--------
### [0.18.0](N/A) - 2020-11-26
#### `ParserFactory.returnParser(T)` interface default no-op method added, implementations are not required but it is available to override for performance optimizations

#### Changed
* `ParserCondition` `recycle()` default implementation removed, so implementors don't forget
* `CharParser.readConditional()` default implementations adjusted to optimize for case with and without destination buffer
* Documentation improvements

#### Removed
* `ParserCondition.canRecycleAll()` static methods
* `TextParserConditionals` and `TextParserConditionalsDefault` `nextIf(CharCategory, ...)` methods removed so that `jrange` dependency could be removed
* `ReadIsMatching.isNext(TextParser, CharCategory, int)` for same reason so that `jrange` dependency could be removed
* As a replacement for `CharCategory` parameters call methods which accept `CharPredicate` and pass `CharSearcher::contains` (i.e. `CharCategory.getCharCondition()::contains`)


--------
### [0.17.0](https://github.com/TeamworkGuy2/JTextParser/commit/983af3ad09232a2f180fc55c0fd63a2c2a382653) - 2020-11-21
#### Added
* `char[] getFirstChars()` to CharParserMatchable interface, implementations can return null, consumers can fall back on `getFirstCharMatcher()`. This is meant for certain performance optimizations.

#### Changed
* `LineSupplier` private fields changed to protected for easier sub-classing
* Protected `LineSupplier.readCharsOrString()` changed to `readLineIntoBuffer()` with new parameter and return type (see method documentation)
* Improved some documentation


--------
### [0.16.0](https://github.com/TeamworkGuy2/JTextParser/commit/09cd09c391cbf9e80545a3cb8e0eab2e43ea3e0f) - 2020-05-23
#### Changed
* Renamed `TextConsumer` to `TextFragmentConsumer` and removed the `text` parameter to allow underlying buffers and `getParserDestination()` to be removed for performance
* `ReadIsMatching` methods now correctly skip calling `TextParser` `unread()` if no results were read

#### Removed
* Removed `CharParser` `getParserDestination()` and `setParserDestination()` in favor of the caller handling the source text and extracting sub-strings based on `getMatchedTextCoords()`, hopefully for more optimized implementations


--------
### [0.15.0](https://github.com/TeamworkGuy2/JTextParser/commit/847e1027b58b486a0bc9b0c1947d29bbd6ffbd24) - 2020-05-03
#### Changed
* Added documentation to several methods

#### Fixed
* `readCount()` implementation in both `TextCharsParser` and `TextIteratorParser`

#### Removed
* Removed unused `twg2.parser.ParseConstants`, contained two static char arrays
* Moved `twg2.parser.Inclusion` to `JTextTokenizer` library


--------
### [0.14.0](https://github.com/TeamworkGuy2/JTextParser/commit/06ef2ad386893405f95e60ce9095259c1c0b68f8) - 2020-04-28
#### Changed
* Removed unnecessary `dropEscChars` parameter from several `ReadIsMatching` methods
* `ReadMatching.FromString` inner class static methods flattened into parent `ReadMatching` class
* `ReadMatching.binaryStartsWith()` added `keyOffset` parameter
* `ReadUnescape.Default` inner class static methods flattened into parent `ReadUnescape` class
* `ReadUnescape` and `ReadWhitespace` removed default constructor, static class with only static methods
* `SlidingStringView.length()` renamed `getPosition()` and return type changed from long to int
* `TextCharsParser.prevChar()` simplified, hopefully better performance
* Improve documentation
* Improved unit tests

#### Fixed
* Several bugs uncovered by unit tests
  * `TextCharsParser.skip()` not tracking newlines skipped, slight performance penalty
  * `TextFragmentRef` fixed issue with `dst` parameter calling `merge()` and `span()`
  * `TextFragmentRef.getText()` inconsistent parameters, added a new parameter
  * `TextParserConditionalsDefault.nextIfNotPrecededBy()` failing if `dst` buffer was null (which is allowed)
  * `ReadRepeats.readRepeat()` several overloads were failing due to a missing loop termination condition
  * `SlidingStringView` offsets properly tracked when inputs larger than cache size are appended

#### Removed
* `ReadBoundedPattern` since it was unused across other projects (JTextTokenizer and JParserCode)
* `ReadPeek` because it only had two methods and only `peekNext(LineReader)` was used elsewhere and it was only 2 lines


--------
### [0.13.3](https://github.com/TeamworkGuy2/JTextParser/commit/079f9c6e5ae26bc21d9ae2a8c1ea4ebb9d0d4538) - 2019-03-25
#### Added
* Added some documentation
* Additional unit tests and unit test cleanup

#### Changed
* Added `ParserCondition.canRecycleAll(ParserCondition[] conds, int off, int len)`
* Internal optimization of `TextCharsParser.nextChar()` (one less method call)


--------
### [0.13.2](https://github.com/TeamworkGuy2/JTextParser/commit/a7103f2d533aa110341623773299c5d95cbd3517) - 2019-03-21
#### Changed
* Removed lombok dependency
* Updated to latest version of test-checks


--------
### [0.13.1](https://github.com/TeamworkGuy2/JTextParser/commit/c31b58693aa14e03949d2265cb8d18c578b0f3bc) - 2017-11-11
#### Fixed
* Patched `TextIteratorParser.prevChar()` bug


--------
### [0.13.0](https://github.com/TeamworkGuy2/JTextParser/commit/b73e5c774f379c6a2202ab2088834f8de9412cbd) - 2017-10-22
#### Changed
* Removed unnecessary parameters from `TextCharsParser` static `of(...)` methods
* Refactored `LineCounter` to support new `unread()` method and move to next line afte reading '\n' instead of the next character after the newline
* Added `LineCounter getLineNumbers()` method to TextParser interface and TextIteratorParser class

#### Fixed
* Fixed bug when repeatedly calling `LineCounter.getRawCompletedLineOffset()`.  Last line offset was getting added over and over each time method was called.


--------
### [0.12.0](https://github.com/TeamworkGuy2/JTextParser/commit/0cf40b362a720263fe38f7583af40a512020e8da) - 2017-08-20
#### Added
* CharParserPredicate interface (in place of `BiPredicates.CharObject<>`)

#### Changed
* Update dependency jfunc@0.3.0
* CharParserMatchable.getFirstCharMatcher() returns new `CharParserPredicate` interface instead of `BiPredicates.CharObject<>`
* Switched all `Predicates.Char` references to `CharPredicate`
* LineCounter no longer implements Functions.CharReturnInt since that interface no longer exists
* Renamed LineCounter apply(char) -> read(char)


--------
### [0.11.0](https://github.com/TeamworkGuy2/JTextParser/commit/e631d9d151351285d5e68e9d8ff8414078fd0e22) - 2016-12-02
#### Added
* TextFragmentRef.span(), similar to merge() but fragments don't need to be adjacent/overlapping
* TextFragmentRef toStartPositionDisplayText() and toEndPositionDisplayText();

#### Changed
* Renamed CharParser getCompletedMatchedTextCoords() -> getMatchedTextCoords()


--------
### [0.10.0](https://github.com/TeamworkGuy2/JTextParser/commit/4bb5366437e458c1edb6b1234fdfe8694c025a9f) - 2016-10-29
#### Added
* twg2.parser.Inclusion - enum { INCLUSION, EXCLUSION } moved from JParserCode

#### Changed
* Move TextFragmentRef Impl and ImplMut sub-classes into separate TextFragmentRefImpl and TextFragmentRefImplMut source files
* Updated jarray-util dependency to jarrays


--------
### [0.9.0](https://github.com/TeamworkGuy2/JTextParser/commit/31345bf6e53f3ab419a0cde0dc99d67277239fa7) - 2016-09-11
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
### [0.8.2](https://github.com/TeamworkGuy2/JTextParser/commit/65c75d28db4c919fd225944762a0ba57e80842d2) - 2016-08-27
#### Changed
* Updated to use latest dependencies and dependency paths


--------
### [0.8.1](https://github.com/TeamworkGuy2/JTextParser/commit/8609d66ef57071440baa27d24faced84fe8e781f) - 2016-06-26
#### Changed
* Improved ReadUnescape documentation and changed ReadMatchingTest
* Switched from versions.md to CHANGELOG.md format, see http://keepachangelog.com/


--------
### [0.8.0](https://github.com/TeamworkGuy2/JTextParser/commit/30313e409b66854c996c35526076fdfc48199775) - 2016-03-02
Moving away from line based parsing toward stream/chunk based parsing

#### Added
* Added LineReader hasPrevChar() and prevChar()

#### Changed
* Renamed ParserStartChars -> CharParserMatchable, which now extends CharParser (instead of old CharParser.WithMarks interface)

#### Removed
* Removed LineReader isUnmodifiedLine(), getLineOffset(), getLineLength(), and getLineRemaining()
* Removed TextParser.hasNextLine()


--------
### [0.7.0](https://github.com/TeamworkGuy2/JTextParser/commit/391997f99f5c4284732417c5bfe44a6430caa072) - 2016-02-23
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
### [0.6.0](https://github.com/TeamworkGuy2/JTextParser/commit/ce375f75724a7aeae15722fbbd492a5a7530d1d7) - 2016-02-21
Moving toward stream/chunk based parsing rather than line based

#### Changed
* Updated to use latest version of JStreamish
  * Added new StringLineSupplier constructor flags to static TextParserImpl methods

#### Removed
* Removed TextParser.hasNextLine()
* Removed TextParserImpl 'transform' and 'injectNewlines' constructor parameters


--------
### [0.5.1](https://github.com/TeamworkGuy2/JTextParser/commit/384b85ffa5d606e89173e7cdbe0003487f86d7ed) - 2016-01-16
#### Added
* Moved twg2.parser.condition, twg2.parser.condition.text, and twg2.parser.textFragment interfaces and default implementations into this library from the ParserTools library to allow reuse in other libraries such as JParserDataTypeLike


--------
### [0.5.0](https://github.com/TeamworkGuy2/JTextParser/commit/caf97ef82556fbd36f978a74af53d82ae7a6161a) - 2016-01-10
#### Added
* Added additional tests

#### Removed
* Removed unneeded midIndex parameter from ReadMatching.binaryStartsWith()
* Removed up some old commented out debugging statements
