# Get library
---

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
    @Prefix fun method1(): SampleInterface1
    @Convention(NamingConventions.LOWER_CAMEL_CASE) fun method2(): SampleInterface2
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

For more info of basic usage see samples folder in project.
Also please visit [the wiki pages](https://github.com/programmerr47/ganalytics/wiki) to know more details.
