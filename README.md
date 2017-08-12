# Overview
Ganalytics is tiny api layer for any analytics in application. It provides object oriented, typesafe, stricked and testable way for organize work with analytics through the application. More information on [wiki pages](https://github.com/programmerr47/ganalytics/wiki).

# Get library
With gradle:
```
compile 'com.github.programmerr47:ganalytics-core:1.0.0'
```

With maven:
```
<dependency>
  <groupId>com.github.programmerr47</groupId>
  <artifactId>ganalytics-core</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

# Basic Usage
To start with gathering analytics:
1. Create an group or category interface and fill it with necessary annotations:
```kotlin
@Prefix
interface SampleInterface {
    fun method1()
    @NoPrefix fun method1(@Label(LabelConverter::class) String label)
}
```
or
```kotlin
interface SampleGroupInterface {
    @Prefix fun sampleInterface(): SampleInterface
    @Convention(NamingConventions.LOWER_CAMEL_CASE) fun anotherInterface(): AnotherInterface
}
```
For more information about [interfaces](https://github.com/programmerr47/ganalytics/wiki/Interfaces) and [annotations](https://github.com/programmerr47/ganalytics/wiki/Annotations) read linked sections.

2. Prepare `Ganalytics` instance through:

_For group interfaces:_
```kotlin
val ganalytics = Ganalytics({ System.out.print(it) }) {
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
}.createGroup { System.out.print(it) }
```

_For category interfaces:_
```kotlin
val ganalytics = GanalyticsSettings {
    cutOffAnalyticsClassPrefix = false
    prefixSplitter = "_"
    namingConvention = NamingConventions.LOWER_SNAKE_CASE
    labelTypeConverters += TypeConverterPair<DummyDataClass> { it.name } +
            TypeConverterPair<DummyReversedClass> { it.id.toString() }
}.createSingle { System.out.print(it) }
```

3. Pass an interface class to `ganalytics`: 

`val analytics = ganalytics.create(SampleGroupInterface::class)`

4. Now you can use `analytics`. For example:

`analytics.sampleInterface().method1()`

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
