# Basics

**Basics** is a modular core plugin for modern Spigot versions!

<p align="center">
  <img src="https://static.jeff-media.com/img/basics/transparent-with-outline_384.png" alt="Basics Logo">
</p>




## Links
- [SpigotMC Discord Thread](https://discord.com/channels/690411863766466590/1196562355912446094)
- [Trello Board](https://trello.com/b/QjvNuXEO/basics)
- [Discord Server](https://discord.gg/GDaTvneshw)

<a href="https://discord.gg/GDaTvneshw">![image](https://github.com/SpigotBasics/basics/assets/1122571/e815f75d-7e86-4142-a00a-22b5b093d81e)</a>

## Compile
To compile, use `gradlew build`.

You can also use `gradlew distribution`,
which will create the plugin .jar and all modules in a drag-and-droppable layout into `build/dist`.
You can also use `gradlew zipDistribution` to get the whole project .zip file.

For running Basics locally, you can also use the `testserver` tasks:

- `copyPluginToTestServer`: Copies the main plugin .jar to your test server
- `copyModule<Name>ToTestServer`: Copies the specific module .jar to your test server
- `copyAllModulesToTestServer`: Copies all modules to the test server - same as running all `copyModule<Name>ToTestServer` tasks
- `copyAllToTestServer`: **Copies all modules and the main plugin .jar to your test server** - same as running `copyPluginToTestServer` and `copyAllModulesToTestServer`

The path to your test server can be specified using a gradle property called "testserver.path" using one of the following methods:
- as command line argument (`gradlew copyAllToTestServer -Ptestserver.path=/my/test/server`)
- in the `gradle.properties` file in your gradle home directory (usually `~/.gradle`): `testserver.path=/my/test/server`

## Todo
Check the [Trello Board](https://trello.com/b/QjvNuXEO/basics)!

## Documentation
The documentation can be found here:

- [Dokka HTML](https://hub.jeff-media.com/javadocs/basics-core/html)
- [Javadoc HTML](https://hub.jeff-media.com/javadocs/basics-core/javadoc)

It is not often updated, so better create your own using `gradlew dokkaHtml` or `gradlew dokkaJavadoc` in the core project.

## Note to Contributors
Contributions are always welcome from anyone!


## Create a new module
Modules can be written in Java or Kotlin. To create a new module, use the `createModule` task.
It will ask you which language (`kotlin` or `java`) you want to use, asks you for the module name, which should be `[a-z0-9_-]+`.

<!--## Custom forks of other repositories used
- ACF
  - `co.aikar:acf-paper:0.5.1-SNAPSHOT` -> `com.github.spigotbasics:acf-paper:0.5.1-SNAPSHOT`
  - Fixes the "cannot get locale" error message when using ACF on modern Spigot versions.
  - [Our Fork](https://github.com/SpigotBasics/acf) | [Original](https://github.com/aikar/commands)
  - EDIT: FIXED in 0.5.1-SNAPSHOT as of 25th Jan 2024-->
