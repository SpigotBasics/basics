# Basics

**Basics** is a modular core plugin for modern Spigot versions!

<p align="center">
  <img src="https://static.jeff-media.com/img/basics/logo1_512.png" alt="Basics Logo">

</p>




## Links
- [SpigotMC Discord Thread](https://discord.com/channels/690411863766466590/1196562355912446094)
- [Trello Board](https://trello.com/b/QjvNuXEO/basics)

## Compile
To compile, use `gradlew build` on the root project.

You can also use the `testserver` tasks:

- `copyPluginToTestServer`: Copies the main plugin .jar to your test server
- `copyModule<Name>ToTestServer`: Copies the specific module .jar to your test server
- `copyAllModulesToTestServer`: Copies all modules to the test server - same as running all `copyModule<Name>ToTestServer` tasks
- `copyAllToTestServer`: Copies all modules and the main plugin .jar to your test server - same as running `copyPluginToTestServer` and `copyAllModulesToTestServer`

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

## Create a new module
To create a new module, use the `createModule` task. It will ask you for the module name, which should be `[a-z0-9_-]+`.

## Did you know?
> [!WARNING]\
> Did you know that you can add fancy warnings like this, using `> [!WARNING]`?