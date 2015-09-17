JTextParser
==============

Contains 2 major packages:
* textParser - a set of interfaces and implementation for reading a stream of input text lines using conditional methods like nextIf() and nextIfNot()
* parserUtils - static utility functions to complement textParser, some functions parse directly from line buffers, others parse strings. For example, ReadRepeats contains methods to read a repeating character(s) or range of characters from a source. Other utilities include ReadNumber, ReadPeek, ReadUnescape, and ReadWhitespace
