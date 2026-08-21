# Contributing

Thanks for stopping by. Any kind of help is welcome — a bug report, an idea, a doc fix, or a pull request.

## Get in touch

- [Issues](https://github.com/allopensource/envy/issues) — bugs and feature requests
- [Discussions](https://github.com/allopensource/envy/discussions) — questions, feedback, or just saying you're using envy

For bigger changes, a quick issue first helps. Small fixes can go straight to a PR.

## Hacking locally

Needs JDK 17+.

```bash
git clone https://github.com/allopensource/envy.git
cd envy
./gradlew build
./gradlew test
```

Before opening a PR, run `./gradlew ktlintFormat` if you changed Kotlin code.

## License

Contributions are licensed under the [MIT License](LICENSE), same as the project.
