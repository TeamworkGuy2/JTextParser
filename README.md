JTextParser
==============

Interfaces, implementations, and helper functions for parsing structured text (such as code).

Package purposes:
* __twg2.parser__: interfaces, constants, and enums for the other packages
* __twg2.parser.condition__: contains interfaces and sub-packages with interfaces that define input stream based parsers, parser factories, and copyable parsers
* __twg2.parser.condition.text__: interfaces for reading matching characters
* __twg2.parser.textFragment__: interfaces and implementations for text fragments (i.e. a sub-string with a begin and end row, column, and absolute offset within a larger source string), interfaces for text sub-string consumption and transformation
* __twg2.parser.textParser__: `TextParser` interface and implementations `TextCharsParser` and `TextIteratorParser`. Text parser can be created from char array, string(s) or PeekableIterator
* __twg2.parser.textParserUtils__: static utility functions to complement textParser package, some functions parse directly from line buffers, others parse strings. For example, `ReadRepeats` contains methods to read a repeating character(s) or range of characters from a source. Utilities include `ReadIsMatching` (to peek ahead in a text parser without consuming input), `ReadMatching`, `ReadNumber`, `ReadUnescape`, and `ReadWhitespace`
* __twg2.parser.textStream__: split Strings into lines via `Supplier<String>` and `Supplier<char[]>` implementations.  Equivalent to `new BufferedReader(new StringReader(str)).lines();`

Check the `twg2.parser.test` package for some examples of the API usage.