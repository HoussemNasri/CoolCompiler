# Notes
### IntelliJ
- Before running on IntelliJ Windows; run `make clean` to delete symbolic links.
- Set language level to `7` as the source code doesn't conform to new constraints introduced in Java 8

### Compiler Specification
#### Inheritance
- Inheritance is forbidden for base classes `String`, `Bool`, `Int`; Note that inheriting `Object` and `IO` is legal but redefinition is forbidden.
#### Main Class
Every program must have a class Main. Furthermore, the Main class must have a method main that
takes no formal parameters. The main method must be defined in class Main (not inherited from another
class). A program is executed by evaluating (new Main).main()
#### The Rule of Equality
The wrinkle in the rule for equality is that any types may be freely compared except `Int`, `String`
and `Bool`, which may only be compared with objects of the same type.
#### Why separate Inherited features and non-inherited ones?
When we want to print a class we call `features.dump_with_types()`.

###### Tasks Accomplished #1
- Created a list of inherited features in classes that gets loaded at initialization.
###### Tasks Accomplished #2
- Gather type information for negation expressions (~)
- Detect user defined classes redefinition
- Gather type information for assignment expressions and detect invalid type assignment
- Detect invalid type for let expression initialization
