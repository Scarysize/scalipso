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

3. Within the sbt shell execute unit tests

   ```sh
   test
   ```

## Tips

In the sbt shell:

- Use the `~test` command to execute all tests whenever the source files change.
- Use `run` to execute the `main` Method of the `Main` class.
- You can pass arguements to your program via: `run arg1 arg2 argN`
