# HackerNews

Android sample for fetching HackerNews stories using some latest Jetpack components

## Environment and third-party libraries

* IDE: Android Studio Hedgehog | 2023.1.1
* Language: Kotlin 1.9.20 with coroutines 1.7.1
* Dependency Injection: Hilt 2.49
* Jetpack compose to render UI
* Retrofit 2.9.0 + Moshi 1.14.0 + Logging Interceptor 5.0.0 for API request
* Paging 3.2.1 for pagination
* Stetho 1.5.1 for app inspector
* Espresso 3.5.1, Mockito 5.8.0 for instrumentation test

## Source code structure

Source code is located in `app/src` directory and separated in the below directories

* androidTest
    * implementations for instrumentation tests
* debug
    * implementations for debug build app
* main
    * core implementations for the app
* release
    * implementation for release build app
* test
    * implementations for unit tests

The core implementations structure is as follows

```
com.example.hackernews
├── app
├── data
├── di
├── domain
└── ui

```

* app
    * Implement for Hilt Application and define the inspector interface
* data
    * Define the Retrofit API and response's data model
* domain
    * Implement the data synchronization between data layer and UI layer
    * Implement the pagination when fetching the news
* di
    * Define the dependency injections modules should be used in the app
    * Separate the implementations for `InspectorModule` and `NetworkModule` between debug build
      and release build for debug purpose
* ui
    * Presentation layer
    * The UI of the app is as follows
      ![App UI](docs/hackernew_app_ui.png)
    * Implement the Jetpack compose functions to render UI
    * Implement the ViewModel to handle UI interaction
    * User can click on
        * the news item to read the associated link in the browser app
        * the refresh button to reload the news
        * the play button to scroll to the most highly ranked news position

## Instrumentation test

- Notes: connect to your Android device or simulator before executing

```sh
./gradlew uninstallAll
./gradlew connectedAndroidTest
```

## Build And Install

### Debug build

```sh
./gradlew installDebug
```

### Release build

* Create keystore file and signing.properties file

```sh
mkdir keystore
cd keystore/
keytool -genkey -v -keystore your_release.jks -alias your_key_alias -keyalg RSA -keysize 2048 -validity 10000 -deststoretype pkcs12
```

* Edit release_signing.properties file with below format

```properties
keyAlias=(your key alias)
keyPassword=(your key password)
storeFile=(your store file name)
storePassword=(your store password)
```

* Build and install release version

```sh
./gradlew installRelease
```

