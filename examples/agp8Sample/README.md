# Transfuse AGP 8 Sample

A minimal Android app that applies Transfuse under a modern toolchain and dexes to an APK:

- **Gradle 8.7**, **Android Gradle Plugin 8.5.2**, **JDK 17**
- `compileSdk 34`, `minSdk 21`
- Transfuse `@Activity` (`HelloTransfuse`) that injects a **`jakarta.inject`** `@Singleton` (`Greeter`)

## Build

```bash
export ANDROID_HOME=/path/to/android-sdk   # needs platform 34 and build-tools 35
./gradlew :app:assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk` (contains the generated `HelloTransfuseActivity`).

## AGP 8 setup notes (Transfuse specifics)

Migrating a Transfuse app to AGP 8 needs a few settings that older (dx / maven-android) builds handled implicitly:

1. **`android.nonFinalResIds=false`** (in `gradle.properties`) — AGP 8 makes `R` ids non-final by
   default; Transfuse binds resources through annotations (`@Layout(R.layout.main)`,
   `@View(R.id.greeting)`), which require constant `R` ids.
2. **`transfuse-api` on the `annotationProcessor` path** — AGP 8 removed `includeCompileClasspath`,
   so the processor's own runtime types (which the shaded processor jar excludes) must be added
   explicitly. `android` and the JAXB runtime are on the processor path for the same reason.
3. **`androidManifestFile` argument → a package-bearing manifest** — AGP 8 no longer allows a
   `package` attribute in `src/main/AndroidManifest.xml` (it uses the module `namespace`), but
   Transfuse derives the `R` class package from a manifest package. `app/transfuse/AndroidManifest.xml`
   supplies that package to the processor only; AGP never sees it.
4. **Manifest processing is off** here; the generated `HelloTransfuseActivity` is declared statically
   in `src/main/AndroidManifest.xml`.
