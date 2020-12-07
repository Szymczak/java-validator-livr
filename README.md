[![](https://jitci.com/gh/umotif-public/java-livr-lib/svg)](https://jitci.com/gh/umotif-public/java-livr-lib)

# java-livr-lib

The reason for the fork is to add the ability to publish the app via jitpack.  

Changes in this fork:

- tests migrated to kotlin
- dependency on guava removed
- gradle files changes

# Add it to your build.gradle:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
Step 2. Add the dependency
dependencies {
        implementation 'com.github.umotif-public:java-livr-lib:Tag'
}
```