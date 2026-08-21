# Release Process

1. **Branch strategy**: `main` is always kept ready for release. Changes are merged into `main` through pull requests, and feature branches are deleted after merge.

2. **Versioning**: Envy uses semantic versioning.
    - Major versions are reserved for breaking changes.
    - Minor versions are reserved for new features.
    - Patch versions are reserved for bug fixes.

3. **Version declaration**: The project version is declared in `gradle.properties` using `VERSION_NAME`.

4. **Git tags**: Git tags reflect released versions.
    - Version `0.1.0` corresponds to tag `v0.1.0`.

5. **Maven Central namespace**: Artifacts are published under the Maven Central namespace `io.github.allopensource`.

6. **Artifact signing**: Artifacts published to Maven Central are signed with the author's PGP keys. See the Maven Central GPG signing requirements: <https://central.sonatype.org/publish/requirements/gpg/>

7. **Publishing to Maven Central**: Publishing is performed using a manual GitHub Actions workflow. The project uses the [gradle-maven-publish-plugin](https://github.com/vanniktech/gradle-maven-publish-plugin) to simplify publishing to Maven Central from GitHub Actions.