# Notes
### IntelliJ
- Before running on IntelliJ Windows; run `make clean` to delete symbolic links.
- Set language level to `7` as the source code doesn't conform to new constraints introduced in Java 8

### Compiler Specification
#### Inheritance
- Inheritance is forbidden for base classes `String`, `Bool`, `Int` and `IO`; Note that inheriting `Object` is legal.
#### Main Class
Every program must have a class Main. Furthermore, the Main class must have a method main that
takes no formal parameters. The main method must be defined in class Main (not inherited from another
class). A program is executed by evaluating (new Main).main()
#### The Rule of Equality
The wrinkle in the rule for equality is that any types may be freely compared except `Int`, `String`
and `Bool`, which may only be compared with objects of the same type.