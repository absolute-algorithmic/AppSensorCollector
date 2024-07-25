This WebSocket server is inspired by the library [AndroidSensorCollector](https://jitpack.io/#ignassew/AndroidSensorCollector) - [Github](https://github.com/ignassew/AndroidSensorCollector)

# What data is being collected

### Device data
- [Manufacturer](https://developer.android.com/reference/android/os/Build#MANUFACTURER)
- [Hardware](https://developer.android.com/reference/android/os/Build#HARDWARE)
- [Model](https://developer.android.com/reference/android/os/Build#MODEL)
- [Bootloader](https://developer.android.com/reference/android/os/Build#BOOTLOADER)
- [Product](https://developer.android.com/reference/android/os/Build#PRODUCT)
- [Tags](https://developer.android.com/reference/android/os/Build#TAGS)
- [Type](https://developer.android.com/reference/android/os/Build#TYPE)
- [User](https://developer.android.com/reference/android/os/Build#USER)
- [Display](https://developer.android.com/reference/android/os/Build#DISPLAY)
- [Board](https://developer.android.com/reference/android/os/Build#BOARD)
- [Brand](https://developer.android.com/reference/android/os/Build#BRAND)
- [Device](https://developer.android.com/reference/android/os/Build#DEVICE)
- [Fingerprint](https://developer.android.com/reference/android/os/Build#FINGERPRINT)
- [Host](https://developer.android.com/reference/android/os/Build#HOST)
- [Id](https://developer.android.com/reference/android/os/Build#ID)
- Version
  * [Release](https://developer.android.com/reference/android/os/Build.VERSION#RELEASE)
  * [Codename](https://developer.android.com/reference/android/os/Build.VERSION#CODENAME)
  * [Incremental](https://developer.android.com/reference/android/os/Build.VERSION#INCREMENTAL)
  * [Sdk_int](https://developer.android.com/reference/android/os/Build.VERSION#SDK_INT)
- Screen
  * [Width](https://developer.android.com/reference/android/util/DisplayMetrics#widthPixels)
  * [Height](https://developer.android.com/reference/android/util/DisplayMetrics#heightPixels)
  * [Pixel density](https://developer.android.com/reference/android/util/DisplayMetrics#density)
- List of available sensors
- Performance micro-benchmarks (e.g. square root of a number ran n times)
- SHA-256 hash of [ANDROID_ID](https://developer.android.com/reference/android/provider/Settings.Secure#ANDROID_ID) and [Build.FINGERPRINT](https://developer.android.com/reference/android/os/Build#FINGERPRINT) combined

### Sensor data

To prevent battery drain, this library will collect at least 1024 events of each type and then turn off. This takes around 4 minutes.

This library collects only following sensor events:
- [Accelerometer](https://developer.android.com/reference/android/hardware/Sensor#TYPE_ACCELEROMETER)
- [Gravity](https://developer.android.com/reference/android/hardware/Sensor#TYPE_GRAVITY)
- [Magnetic Field](https://developer.android.com/reference/android/hardware/Sensor#TYPE_MAGNETIC_FIELD)
- [Gyroscope](https://developer.android.com/reference/android/hardware/Sensor#TYPE_GYROSCOPE)
