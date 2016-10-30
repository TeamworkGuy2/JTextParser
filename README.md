JTextParser
==============
version: 0.10.0

Contains 4 major packages:
* twg2.parser - interfaces, constants, and enums for the other packages
* twg2.parser.condition - contains interfaces and sub-packages with interfaces that define input stream based parsers, parser factories, and copyable parsers
* twg2.parser.condition.text - interfaces for reading matching characters
* twg2.parser.textFragment - interfaces and implementations for text fragments (i.e. a sub-string with a begin and end row, column, and absolute offset within a larger source string), interfaces for text sub-string consumption and transformation
* twg2.parser.textParser - TextParser interface and implementation, text parser can be created from strings or PeekableIterator
* twg2.parser.textParserUtils - static utility functions to complement textParser, some functions parse directly from line buffers, others parse strings. For example, ReadRepeats contains methods to read a repeating character(s) or range of characters from a source. Utilities include ReadIsMatching (to peek ahead in a text parser without consuming input), ReadNumber, ReadPeek, ReadUnescape, and ReadWhitespace
* twg2.parser.textStream - split Strings into lines via Supplier<String> and Supplier<char[]> implementations.  Equivalent to 'new BufferedReader(new StringReader(str)).lines();'

Check the 'twg2.parser.test' package for some examples of the API usage.