![feature-graphic](art/feature-graphic.png)
Cari Runners blog reader with Android Architecture Components

This is a mobile app that uses Android Architecture Components with Dagger 2.

[![Build Status](https://travis-ci.org/fabiolee/android-run.svg?branch=master)](https://travis-ci.org/fabiolee/android-run)

## App Links
https://carirunners-57b89.firebaseapp.com

## Functionality
The app is composed of 4 main screens.
### PageFragment
This fragment displays a list of posts.
![PageFragment](art/PageFragment.png)
### PostFragment
This fragment displays the details of a post.
![PostFragment](art/PostFragment.png)
### FavoriteFragment
This fragment displays a list of favorite posts.
![FavoriteFragment](art/FavoriteFragment.png)
### SettingsFragment
This fragment displays the settings.
![SettingsFragment](art/SettingsFragment.png)
### PushNotification
The FCM push notification.
![PushNotification](art/PushNotification.png)

## Setup
Create a `base/fabric.properties` file and include your own Fabric `apiKey` and `apiSecret`.
Example:
```
apiKey=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
apiSecret=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```
Create a `cert/keystore.properties` file and include your own keystore `storeFile`, `storePassword`, `keyAlias` and `keyPassword`.
Example:
```
storeFile=../cert/keystore.jks
storePassword=xxxxxxxx
keyAlias=xxxxxxxx
keyPassword=xxxxxxxx
```
## Building
You can open the project in Android studio and press run.
## Testing
The project uses both instrumentation tests that run on the device
and local unit tests that run on your computer.
To run both of them and generate a coverage report, you can run:

`./gradlew fullCoverageReport` (requires a connected device or an emulator)

### Device Tests
#### UI Tests
The projects uses Espresso for UI testing. Since each fragment
is limited to a ViewModel, each test mocks related ViewModel to
run the tests.
#### Database Tests
The project creates an in memory database for each database test but still
runs them on the device.

### Local Unit Tests
#### ViewModel Tests
Each ViewModel is tested using local unit tests with mock Repository
implementations.
#### Repository Tests
Each Repository is tested using local unit tests with mock web service and
mock database.
#### Webservice Tests
The project uses [MockWebServer][mockwebserver] project to test REST api interactions.


## Libraries
* [Android Support Library][support-lib]
* [Android Architecture Components][arch]
* [Android Data Binding][data-binding]
* [Firebase Cloud Messaging][fcm]
* [Google Analytics for Firebase][firebase-analytics]
* [Google Play Services][google-services]
* [Dagger 2][dagger2] for dependency injection
* [Retrofit][retrofit] for REST api communication
* [Logging Interceptor][logging-interceptor] for logging REST api
* [Glide][glide] for image loading
* [jsoup][jsoup] for Java HTML parser
* [Crashlytics][crashlytics] for crash reporting
* [Stetho][stetho] for debug bridge
* [Timber][timber] for logging
* [espresso][espresso] for UI tests
* [mockito][mockito] for mocking in tests


[mockwebserver]: https://github.com/square/okhttp/tree/master/mockwebserver
[support-lib]: https://developer.android.com/topic/libraries/support-library/index.html
[arch]: https://developer.android.com/arch
[data-binding]: https://developer.android.com/topic/libraries/data-binding/index.html
[fcm]: https://firebase.google.com/docs/cloud-messaging/
[firebase-analytics]: https://firebase.google.com/docs/analytics/
[google-services]: https://developer.android.com/google/index.html
[espresso]: https://google.github.io/android-testing-support-library/docs/espresso/
[dagger2]: https://google.github.io/dagger
[retrofit]: http://square.github.io/retrofit
[logging-interceptor]: https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
[glide]: https://github.com/bumptech/glide
[jsoup]: https://jsoup.org
[crashlytics]: https://fabric.io
[stetho]: https://github.com/facebook/stetho
[timber]: https://github.com/JakeWharton/timber
[mockito]: http://site.mockito.org
