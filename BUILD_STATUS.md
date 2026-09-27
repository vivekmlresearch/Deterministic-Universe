# Build status

The source package is complete, but this generation workspace did not contain Gradle, the Android SDK, `aapt2`, or a Java compiler. Therefore no APK was fabricated or relabelled. The included GitHub Actions workflow builds and tests the genuine debug APK in a standard Android build environment.

To obtain the test APK, push the project contents to GitHub, open **Actions → Android production build → Run workflow**, then download the `DeterministicUniverse-test-apk` artifact.
