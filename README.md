WIP: Essentials-like "core" plugin for modern Spigot versions (no 1.8!) called "Basics"

https://discord.com/channels/690411863766466590/1196562355912446094

## Compile
To compile, use `gradlew shadowJar` on the root project.

Check out the test module on how to add modules.

The plugin project may depend on individual modules to make it easier to load them until we got a proper module loader.

## Todo
### Module class loading
Builtin modules shall be compiled into normal .jars and then, instead of getting shaded, shall be included in the resources
as .jar file. The BasicsPluginImpl should then create a separate classloader for each module with its own classloader
as parent classloader. Each module can then have a basics-module.yml file similar to plugin.yml
(main-class, version, name, description, etc). This will allow modules to include their own resources
without having name name conflicts regarding classes and resources.

### Storage API
We'll need a storage API that provides a common interface for storing data in different backends (must at least support flatfile, mysql, and sqlite)