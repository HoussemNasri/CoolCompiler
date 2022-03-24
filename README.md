# Cool Compiler
I wrote this compiler while attending CS143[^cs] compiler construction course on *edx.org*.
The course is divided into four assignments. In each assignment, we implement one phase of the compilation process. All phases are being tested separately using automated scripts.

## The Cool Programming Language
Cool, an acronym for *Classroom Object Oriented Language*. While *small* enough for a one term project, Cool still has many of the features of modern programming languages, including *objects*, *automatic memory management*, *strong static typing* and simple *reflection*.
### Features
As the primary purpose of Cool is instruction, it lacks many of the features common to other, more general programming languages. For instance, the language supports less than comparisons but not greater than. The syntax is very much stripped down, and the *standard library* contains only a few basic classes. Separate compilation is not supported, though the compiler does support multiple source files as input. Every Cool program must define a class Main which must have a no argument main method in which execution flow begins. Namespaces are not supported.
### Examples
> Hello World:

``` cool
class Main inherits IO {
  main() : Object {
    out_string("Hello, world!\n")
  };
};
```

> A simple program for computing factorials:

``` cool
class Main inherits IO {
  main(): Object {{
    out_string("Enter an integer greater-than or equal-to 0: ");

    let input: Int <- in_int() in
      if input < 0 then
        out_string("ERROR: Number must be greater-than or equal-to 0\n")
      else {
        out_string("The factorial of ").out_int(input);
        out_string(" is ").out_int(factorial(input));
        out_string("\n");
      }
      fi;
  }};

  factorial(num: Int): Int {
    if num = 0 then 1 else num * factorial(num - 1) fi
  };
};
```
## Built with
- [Java](https://dev.java/) - The main programming language
- [JLex](https://www.cs.princeton.edu/~appel/modern/java/JLex/) - Scanner generator
- [Java-CUP](http://www2.cs.tum.edu/projects/cup/) - Parser generator
- [Perl](https://www.perl.org/) - Testing, but why perl?
<!-- GETTING STARTED -->
## Getting Started
### Prerequisites 
- [Java 8+](https://openjdk.java.net/install/)
- [MIPS](https://www.mips.com/develop/tools/compilers/)
### Download
See the [releases](https://github.com/HoussemNasri/CoolCompiler/releases) page for the latest build

[^cs]:
    [CS143: Compilers][CS143] - Stanford's course on the practical and theoretical aspects of compiler construction
      
[CS143]: https://www.edx.org/course/compilers 

