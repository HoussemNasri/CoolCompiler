# Notes
### IntelliJ
- Before running on IntelliJ Windows; run `make clean` to delete symbolic links.
- Set language level to `7` as the source code doesn't conform to new constraints introduced in Java 8

### Compiler Specification
#### Inheritance
- Inheritance is forbidden for base classes `String`, `Bool`, `Int` and `IO`; Note that inheriting `Object` is legal.
- 