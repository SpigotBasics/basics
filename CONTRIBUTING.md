# Contributing to Basics

First and foremost, thanks for taking your time to contribute!️

Any type of contribution is welcome and valued. Basics has got a list of guidelines to be followed whilst contributing for the sake of easier maintenance of
the project. Take a look at the **[table of contents](#table-of-contents)** for reference. The community looks forward to your contributions!

Please note that the baseline for all contributions is the [Code of Conduct](./CODE_OF_CONDUCT.md).

# Table of contents

- **[How to contribute](#how-to-contribute)**
- **[How to suggest a feature](#how-to-suggest-a-feature)**
- **[How to report a bug](#how-to-report-a-bug)**
- **[Code styling](#code-styling)**

How to contribute
===
Once again, thanks for being with us! If you are here to contribute new code, extra documentation, or anything else, this paragraph is for you.

### Expectations
You should not be contributing if you have nothing to bring to the table. Here are the general guidelines:

#### What's not welcome
Contributions that...
* only focus on fixing code style;
* only optimize something;
* only add unit tests
  are **not welcome**.<br>

Additionally, please note that contributions that largely refactor a big part of the code just for the sake of structuring are **frowned upon**. Care to document the new code structure in the commit message for it to have a chance to be accepted.

#### What's welcome
Contributions that...
* fix bugs;
* add new API methods or fix its implementations;
* add features;
* refactor a minor but tedious part of the code (such as removing a clearly redundant variable in 100 places)
  are **welcome**.

Additionally, please note that ideally contributions should not introduce any breaking changes to the API. If you're unsure, open an issue to discuss it.

#### What should every contribution have

1) Any added code must be tested and proven to work.
2) The code should be as optimized as possible, but not to the point of unreadability.
3) If Kotlin can be used, it should be used. While Java code is still welcome, the project is mainly focused on Kotlin and utilizes plenty Kotlin-specific features.

Additionally, there are some strict guidelines regarding code logic:
1) Never use `BukkitScheduler`. Basics uses `BasicsModule#eventBus` for task scheduling.
2) For command creation, use `BasicsModule#commandFactory`.
3) Never check permissions with strings. Instead, use `BasicsModule#permissionManager` to register actual permissions with descriptions.
4) The recommended approach for configuration options is doing `val configOption get() = config.get(...)`.
5) Modules should be as small as possible, but not absurdly small. For example, you should split `/home` and `/warp`, but `/home` and `/sethome` should be in the same module.
6) Each and every message should be configurable with MiniMessage. Please take a look at how the existing messages are handled.

### Creating a PR
This project relies on the Git VCS for convenience. As such, you're expected to be familiar with it and how to create pull requests.

### Legal notice
Developer Certificate of Origin, version 1.1
```
Developer Certificate of Origin
Version 1.1

Copyright (C) 2004, 2006 The Linux Foundation and its contributors.

Everyone is permitted to copy and distribute verbatim copies of this
license document, but changing it is not allowed.


Developer's Certificate of Origin 1.1

By making a contribution to this project, I certify that:

(a) The contribution was created in whole or in part by me and I
    have the right to submit it under the open source license
    indicated in the file; or

(b) The contribution is based upon previous work that, to the best
    of my knowledge, is covered under an appropriate open source
    license and I have the right under that license to submit that
    work with modifications, whether created in whole or in part
    by me, under the same open source license (unless I am
    permitted to submit under a different license), as indicated
    in the file; or

(c) The contribution was provided directly to me by some other
    person who certified (a), (b) or (c) and I have not modified
    it.

(d) I understand and agree that this project and the contribution
    are public and that a record of the contribution (including all
    personal information I submit with it, including my sign-off) is
    maintained indefinitely and may be redistributed consistent with
    this project or the open source license(s) involved.
```
### Commit message
A commit message must be fairly small, yet informative.
1) The first line should be the general commit idea.
2) The second line should be left empty.
3) Everything else should be wrapped to not be longer than 90 characters - if it is, put it on a new line (except for URLs).
4) If the commit message has any general links or such information (such as "References"), they should be put on the last lines.

Example of a good commit message:
```
Added SNBT Arguments and Selector Arguments

SNBT arguments and selector arguments are now supported in the command system.
```

How to suggest a feature
===
Suggesting features is also contribution in a way! That said, there are some guidelines for suggesting features.
### Where to suggest features
You can suggest features in [GitHub Issues](https://github.com/SpigotBasics/Basics/issues).
### A good feature suggestion
What makes a good feature suggestion is the research behind it.
1) Make sure that there's no (recent) issue already open on that topic.
2) Really make sure that the feature isn't already there. The feature you are requesting might be a configuration tweak on an already existing feature.

How to report a bug
===
Please report bugs via [GitHub Issues](https://github.com/SpigotBasics/Basics/issues). Before opening
an issue, please also make sure that there's no (recent) issue already open on that bug.

Code styling
===
Are you ready to make your first contribution to Spigot Basics? Well then, have at it! While there are no strict code styling guidelines, there are some important things to note:

1) **Consistency** - the code should be consistent. If you're adding a new method to a class, make sure that the method is styled the same way as the rest of the methods in the class.
2) **Readability** - the code should be readable. Your code should be readable not only to you, but to anyone else who might be reading it. This means that you should avoid using overly complex code, and you should document your code if it's not clear what it does.
3) **Documentation** - if you're adding a new method to a class, make sure to document it. Documenting your code is important, as it makes it easier for others to understand what your code does. If you're not sure how to document your code, take a look at the existing code in the project to see how it's done.
4) **Linting** - before you submit your code, make sure to run the linter by using `ktlintFormat`. If you only want to check for linting errors, use `ktlintCheck`.
