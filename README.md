# Overview
[ ![Download](https://api.bintray.com/packages/programmerr47/maven/ganalytics-core/images/download.svg) ](https://bintray.com/programmerr47/maven/ganalytics-core/_latestVersion)


Ganalytics is tiny api layer for any analytics in application. It provides an object oriented, typesafe, strict and testable way to organize work with analytics in the application. More information on [wiki pages](https://github.com/programmerr47/ganalytics/wiki).

Here is [latest changelog](https://github.com/programmerr47/ganalytics/releases/tag/v1.1).

Also, you can read these articles for more details:
1) [Introduction to conception](https://medium.com/@programmerr47/declarative-analytics-ganalytics-4cb927b98be8)
2) [V1.1 Changelog details](https://medium.com/@programmerr47/effective-application-analytic-832f950232b9)

# Get library
With gradle:
```
compile 'com.github.programmerr47:ganalytics-core:1.1.0'
```

With maven:
```
<dependency>
  <groupId>com.github.programmerr47</groupId>
  <artifactId>ganalytics-core</artifactId>
  <version>1.1.0</version>
  <type>pom</type>
</dependency>
```

# Basic Usage
To start with gathering analytics:
1. Create an group or category interface and fill it with necessary annotations:
```kotlin
@Prefix
interface CategoryInterface {
    fun action()
    @NoPrefix fun otherAction(@Label(LabelConverter::class) String label)
}
```
or
```kotlin
interface GroupInterface {
    @Prefix fun category(): CategoryInterface
    @Convention(NamingConventions.LOWER_CAMEL_CASE) fun otherCategory(): OtherCategoryInterface
}
```
For more information about [interfaces](https://github.com/programmerr47/ganalytics/wiki/Interfaces) and [annotations](https://github.com/programmerr47/ganalytics/wiki/Annotations) read linked sections.

2. Prepare `Ganalytics` instance through:

_For group interfaces:_
```kotlin
val ganalytics = Ganalytics({ System.out.println(it) /**or do something with received incoming events**/ }) {
     cutOffAnalyticsClassPrefix = false
     prefixSplitter = "_"
     namingConvention = NamingConventions.LOWER_SNAKE_CASE
     labelTypeConverters += TypeConverterPair<DummyDataClass> { it.name } +
             TypeConverterPair<DummyReversedClass> { it.id.toString() } 
}
```
wich is equal to:
```kotlin
val ganalytics = GanalyticsSettings {
    cutOffAnalyticsClassPrefix = false
    prefixSplitter = "_"
    namingConvention = NamingConventions.LOWER_SNAKE_CASE
    labelTypeConverters += TypeConverterPair<DummyDataClass> { it.name } +
            TypeConverterPair<DummyReversedClass> { it.id.toString() }
}.createGroup { System.out.println(it) /**or do something with received incoming events**/ }
```

_For category interfaces:_
```kotlin
val ganalytics = GanalyticsSettings {
    cutOffAnalyticsClassPrefix = false
    prefixSplitter = "_"
    namingConvention = NamingConventions.LOWER_SNAKE_CASE
    labelTypeConverters += TypeConverterPair<DummyDataClass> { it.name } +
            TypeConverterPair<DummyReversedClass> { it.id.toString() }
}.createSingle { System.out.println(it) /** or do something with received incoming events**/ }
```

3. Pass an interface class to `ganalytics`: 

`val analytics = ganalytics.create(GroupInterface::class)`

4. Now you can use `analytics`. For example:

`analytics.category().otherAction("label")`

will print to the standart output: `Event(category=sampleinterface, action=sampleinterface_method1)`

**Note:** instead of `System.out.println` you can pass, for example:

```kotlin 
googleAnalyticsTracker.send(HitBuilders.EventBuilder()
        .setCategory(it.category)
        .setAction(it.action)
        .setLabel(it.label)
        .setValue(it.value)
        .build())
```

Or any analytics method as you want.

For more info of basic usage see samples folder in project.
Also please visit [the wiki pages](https://github.com/programmerr47/ganalytics/wiki) to know more details.
