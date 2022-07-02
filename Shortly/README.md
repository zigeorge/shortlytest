# Shortly Android application
This application is an interview assessment of MAYD

# Description
This application provides a user interface to enter URL to shorten. User can also see a list of
previously shortened URLs, copy and delete them.

# Technologies used
Android Studio Chipmunk | 2021.2.1 Patch 1
Build #AI-212.5712.43.2112.8609683, built on May 18, 2022
Runtime version: 11.0.12+0-b1504.28-7817840 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
Linux 5.13.0-51-generic
GC: G1 Young Generation, G1 Old Generation
Memory: 4096M
Cores: 8
Registry: external.system.auto.import.disabled=true, ide.instant.shutdown=false, ide.balloon.shadow.size=0
Non-Bundled Plugins: dev.patrickpichler.darculaPitchBlackTheme (1.0.0), xyz.sheba.developers.commit-template (1.0.1), Batch Scripts Support (1.0.13), Dart (212.5744), CMD Support (1.0.5), org.intellij.plugins.markdown (212.5457.16), org.jetbrains.kotlin (212-1.6.21-release-334-AS5457.46), io.flutter (68.1.2), ru.adelf.idea.dotenv (2022.1)
Current Desktop: ubuntu:GNOME

# Android versions
>The minimum Android SDK version is 21
>The compile SDK version is 32
>The target SDK version is 32

# It uses the following libraries
- Jetbrains Kotlin std lib 1.5.30
- Androidx 1.4.0
- coroutines 1.5.2
- lifecycle 2.4.0
- room 2.3.0
- sdp 1.1.0
- retrofit 2.9.0
- dagger-hilt 2.38.1

# Build
To build the project add a keystore.properties file with the following values. Then sync and rebuild
```storePassword=abcd1234lifeisbeautiful
keyPassword=abcd1234lifeisbeautiful
keyAlias=shortlyapp
storeFile=../shortlyapp.jks
```

# Disclaimer
I've used kotlin to write this application as it provides an easy and simpler syntax than Java. Besides that kotlin provides support for coroutines which makes it very easy to write android lifecycle aware processes.
- **Coroutine** - Kotlin's coroutine enables asynchronous and non blocking programming. Coroutines are aware of lifecycle so there's no chance of threads processing out of contexts. Coroutines suspend functions makes it looks like I'm writing a synchronous program hence, the classes looks cleaner. Live data and Flow helps implementing the observer pattern, which I could've done using Rx. At first I used Rx with retrofit and room but the code at viewModel looked clumsier. So I've decided to migrate to coroutine and livedata.
- **Retrofit** - Retrofit makes it is easy to manipulate and maintain a clean architecture and it enables network calling with a very minimum amount of codes. It also supports implementation with coroutines and liveData.
- **Room** - The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite. It also work with coroutines and helps maintaining the clean architecture.
- **Hilt** - Dagger-hilt enables dependency inversion one of the 5 principles of SOLID. It helped me separate my business logics from repository, dao and API layer. It enhances the scalability of the project so that in future if want to implement a different UI I'd merely have to make any change in the technical part of this project.
- **SDP** - The sdp library auto generates dimen properties for all possible screen sizes, so that I do not have to write dimen files for each screen sizes. Well it does have its benefits along with some disadvantages such as I had to re calculate all measurements from Figma file according to SDPs measurements. Also it takes a lot of compile-time memories as well.

# Testing
I've written unit test for the ShortlyViewModel class to verify if all the functions are behaving properly. Also written test for a util class. To test the room database I'd have to write instrumented unit test as database testing needed a context which can only be provided through instrumentation.

# GIT Flow
I've maintained standard of git flow during the development process.
Now sync and rebuild.

# Conclusion
This has been a great experience for me. I've learned many things while writing this assesment. I've learned how RxJava works and also learned how to migrate a project from Rx to live data and coroutine. I hope you like the project.
