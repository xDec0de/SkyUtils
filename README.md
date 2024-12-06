# About SkyUtils

SkyUtils is a free to use open-source multi-platform plugin API to help you
develop your own plugins. SkyUtils focuses on removing the repetitive tasks
that many plugins share while providing a standard, aiming for the highest
performance and reliability at the same time while keeping a detailed
documentation, so plugin creation is as easy as possible.

![SkyUtils banner](https://github.com/user-attachments/assets/24f61e9a-819a-4f0d-b8ff-65930106e2a2)

<div align=center>
<a href="https://www.codefactor.io/repository/github/xdec0de/skyutils"><img src="https://www.codefactor.io/repository/github/xdec0de/skyutils/badge"></a>
<a href="https://app.codacy.com/gh/xDec0de/SkyUtils/"><img src="https://app.codacy.com/project/badge/Grade/2d121db7e16749f49cdc3cdd897da9fe"></a>
<a href="https://github.com/xDec0de/SkyUtils/actions/workflows/build.yml"><img src="https://img.shields.io/github/actions/workflow/status/xDec0de/SkyUtils/build.yml?branch=master"></a>
</div>

## Currently supported platforms

As previously stated, SkyUtils is a multi-platform plugin API, which means
that it is designed to work on multiple platforms. Each platform has its
own module and jar file. Keep in mind that the **shared** module only contains
shared files that are used on all platforms and can't be used as a plugin,
that's the reason why it is **NOT** considered a platform. Now that you know
that detail, here is the list of actual platforms that can be used:

### Server side platforms

- Spigot - Compatible with Paper servers.
- Paper - Extends the Spigot module (Details about this soon)

### Proxy side platforms

- Velocity

## The reliability of SkyUtils

Well, you can never be sure that a program is 100% reliable on any environment
with an almost infinite amount of conditions that Minecraft servers tend to
have. Of course, bugs may appear sooner or later, it's part of the development
process of any program. However, SkyUtils is used by my own plugins which are
tested quite a lot, in fact, I even use it as a part of the core of my own
network, so you don't have to worry about the project being abandoned any time
soon either. Most bugs / performance issues will be solved before a version is
even released or they get reported. But, if you do find a bug, critical or not,
feel free to [contribute](https://github.com/xDec0de/SkyUtils/blob/master/CONTRIBUTING.md)
/ [create an issue](https://github.com/xDec0de/SkyUtils/issues/new/choose) to
fix it!

## The current state of SkyUtils, not ready for production yet

One thing that is important to note is that SkyUtils is currently on the early
stages of development, features can *(And will)* change, methods may be renamed
or removed, and of course issues are expected to appear. This is normal right
now! **API-breaking** changes will happen with a lower and lower frequency as
we approach the first stable release, these changes are necessary right now
that we can change most of the code without worrying about a ton of plugins
breaking, making sure that all features are robust and easy to use in the
better way possible. A great example of this is how the whole command API
changed in order to add a more minimalistic and simple approach to it, this
**WON'T** happen once the first stable build of SkyUtils gets released, so
**don't panic!**
