# Coding Session: Scala

An exercise project for getting to know Scala.

## Installation

Scala and sbt are provided via a docker container:

_(Hint: Disconnect your company VPN)_

1. Build the image:

   ```sh
   docker build . -t scala-coding-session -f Dockerfile
   ```

2. Start the container, opening a sbt shell:

   ```sh
   docker run -it --rm \
    -v ~/.ivy2:/root/.ivy2 \
    -v ~/.sbt:/root/.sbt \
    -v $PWD:/app \
    -w /app \
    scala-coding-session sbt shell
   ```

   The first start might take some time. You should see a shell displaying something like `sbt:Scala-Intro> ...`

3. Within the sbt shell compile the code to test the setup.

   ```sh
   compile
   ```

## Tips

In the sbt shell:

- Use the `~test` command to execute all tests whenever the source files change.
- Use `run` to execute the `main` Method of the `Main` class.
- You can pass arguements to your program via: `run arg1 arg2 argN`

## Goal

We want to implement a parser and interpreter for **s-expressions**. The parser should be able to parse arbitrary expressions. The interpreter should support the `+`/addition operation:

```sh
> sbt run "(+ (1 2 (+ 4 4)))"
Result: 8
```

### Approach: Model

We use a `case class` to represent the parts we parse from the input program, namely:

- numbers: `(1 2)`
- symbols: `(+)`
- lists: `(+ 1 2 (3))`

All of them are **expressions**. Even a list, it's an expression composed of other expressions.

### Approach: Parser

Scala has a [well-known library](https://github.com/scala/scala-parser-combinators) to construct a parser from multiple smaller parsers. We use this **parser combinator** approach to split our "program" parser into parsers for each expression type. We then compose them using helper constructs from the library. The result is a tree like structure representing our input program.

### Approach: Interpreter

The interpreter recursively evaluates all expressions until it reaches the top of the tree:

```
> (+ 1 (+ 2 3) (+ 5 5) 7)
> (+ 1 5 10 7)
> (23)
```
