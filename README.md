# xebia-mowitnow
This is the technical test for Xebia application.

## Constraints:

* Avoid to consume to much memory (especially OutOfMemory when parsing the file)
* Almost no limit for the mowed area (above Long.MAX_VALUE)

## Where am I now:

* Everything is supposed to be a cartesian coordinate system
* The location is in place :
  * It uses BigInteger so that almost no overflow is possible for the coordinates
  * Unit test included
  * Comment / Javadoc available
* The area system is in place :
  * It is using an interface so that it can change in the futur from a basic rectangle
  * The only available area for now is a rectangle
  * Unit test included
  * Comment / Javadoc available
* The mower orders is in place:
  * They are an enumeration (with an index on the alternate identifier which is the input char)
  * Unit test included
  * Comment / Javadoc available
* The cardinal point is in place for the direction:
  * They are an enumeration (with an index on the alternate identifier which is the input char)
  * Unit test included
  * Comment / Javadoc available
* The mower is in place:
  * Unit test included
  * Comment / Javadoc available
* The input stream parser is in place:
  * It process the file byte after byte (the file content should be ASCII only so we do not care about char on more than one byte)
  * Any unattended char is skipped
  * An empty order line is possible

## TODO:

* Finalize the comment / Javadoc for the parser part
* Finalize the file handling in front of the parser
* Finalize the shell that is in front of the java programs
* Add a logger using the JDK logger

## Other ideas / Interesting refactoring:

* Introduce an event handler inside the parser in order to separate the parser from the command logic
* Use a DSL and a generator for a out of the box parser
