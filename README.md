# Study Companion: Capstone Project Starter

Reference starting point for the **Google Romania Android Rapid Prototyping Summer School 2026**, held 13 to 22 July 2026 at the Google Lab, Faculty of Automatic Control and Computers, Politehnica București.

## What this repo is (and isn't)

In the course, we build this app **from scratch during Session 1**. You should not clone this repo as the starting point of your capstone. You should type it yourself so the choices become yours.

This repo exists as:

- A **reference** to compare against if your setup starts drifting.
- A **rescue** if your Gradle sync wedges 20 minutes into Session 1 and cannot be recovered in place.
- **Documentation** of the choices we made and why, for anyone auditing the course.

If you are a student and your project is running end of Session 1, you do not need this repo. Keep going with your own.

## What the starter contains

A minimum viable Android skeleton:

- Kotlin, Jetpack Compose, Material 3
- Version catalog with pinned versions (AGP 9, Kotlin 2.2.x, Compose BOM)
- Two screens: a mock `SignInScreen` and a hardcoded `DeckListScreen`
- Navigation Compose wired between them with correct back-stack handling
- Google-palette Material 3 theme, dynamic color on Android 12+
- Custom `Application` class (empty, ready for Session 2)
- `.gitignore` that keeps `local.properties`, keystores, and IDE files out

It deliberately does NOT contain Firebase, Hilt, ViewModels, a real data layer, App Check, Remote Config, or Crashlytics. Those get added session by session.

## Prerequisites

- Android Studio, latest stable (Narwhal Feature Drop or later)
- JDK 21 (use the embedded JDK unless you have a reason not to)
- An emulator or physical device running API 26+

## Setup

```bash
git clone git@github.com:radum-tix/android-capstone-2026.git capstone
cd capstone
```

Open the project in Android Studio, let Gradle sync, run on your emulator.

Expected behavior: app launches to a "Study Companion" splash with a **Continue** button. Tapping Continue navigates to a "Your decks" screen with three mock cards. Back button exits the app (correct: sign-in is popped from the stack after auth).

If Gradle sync fails, first try `File > Invalidate Caches > Restart`. Version numbers in `gradle/libs.versions.toml` are pinned as of course start; you may need to bump minor versions if you build this months later.

## Important: change the package name

The reference project uses `ro.upb.summer.capstone` as an example. If you are using this repo as your own capstone starting point, **rename the package to your own** before you go further. Suggested convention:

```
com.<yourname>.capstone
```

For example, `com.andreipopescu.capstone`.

To rename in Android Studio: right-click the package in the project tree, `Refactor > Rename`. Also update `applicationId` in `app/build.gradle.kts`, `namespace` in the same file, and any references in `AndroidManifest.xml`.

Why this matters: if you later distribute your app to real testers or push to a Play internal track, `ro.upb.summer.capstone` sits in the lecturer's namespace. Your app should have your identity, not his.

## Session roadmap

What each session adds to this starter:

| Session | Date        | Adds                                                              |
|---------|-------------|-------------------------------------------------------------------|
| 1       | 13 Jul 2026 | ViewModels, `UiState` pattern, restructured data layer            |
| 2       | 15 Jul 2026 | Hilt DI, Firebase Auth (email + Google), auth-gated navigation    |
| 3       | 17 Jul 2026 | Cloud Firestore, security rules, real-time listeners via Flow     |
| 4       | 20 Jul 2026 | Firebase AI Logic with Gemini, structured JSON, multimodal input  |
| 5       | 22 Jul 2026 | Release signing, App Distribution, Remote Config, Crashlytics     |

Each session is a Monday, Wednesday, or Friday, 16:00 to 20:00.

## File structure

```
capstone/
├── build.gradle.kts                    Root Gradle config
├── settings.gradle.kts                 Module + repository config
├── gradle/
│   └── libs.versions.toml              Version catalog (single source of truth)
├── local.properties                    Machine-specific, gitignored
└── app/
    ├── build.gradle.kts                App module config
    └── src/main/
        ├── AndroidManifest.xml
        └── java/ro/upb/summer/capstone/
            ├── MainActivity.kt
            ├── CapstoneApp.kt          Application class, currently empty
            └── ui/
                ├── AppNavigation.kt
                ├── SignInScreen.kt
                ├── DeckListScreen.kt
                └── theme/
                    ├── Color.kt
                    ├── Theme.kt
                    └── Type.kt
```

Flat on purpose. Session 1's second half is where we introduce `data/`, `ui/decklist/`, `ui/signin/` subdirectories. If the starter already had them, the reorganization moment would lose its motivation.

## Design choices worth noting

**`minSdk = 26`.** Chosen so we can ship adaptive icons without PNG density fallbacks and use modern APIs (Photo Picker, adaptive icons, background restrictions) without special-casing older devices. Covers roughly 97% of active Android devices as of course start.

**Mock data is inline in `DeckListScreen.kt`.** Session 1 pulls it out into a proper data layer. Having it inline gives the extraction a concrete starting point.

**Navigation pops sign-in on success.** `popUpTo(SignIn) { inclusive = true }` means once the user reaches DeckList, back exits the app. Correct model for post-auth apps and matches what we'd write in production.

**`enableEdgeToEdge()` in `MainActivity`.** Content extends behind system bars. Modern default in Android 15+.

## Troubleshooting

**Gradle sync fails on first open.** Confirm JDK 21 is selected in `Settings > Build > Gradle > Gradle JDK`. If Android Studio picked JDK 17, sync will fail cryptically. Switch to JDK 21 (embedded or system).

**"Unresolved reference" on Compose imports.** Confirm `compose = true` under `buildFeatures` in `app/build.gradle.kts`. Also confirm you did NOT accidentally remove the Compose BOM `platform(libs.androidx.compose.bom)` line from `dependencies`.

**Dynamic colors don't work.** Only available on Android 12 (API 31) and above. On older devices, the theme falls back to our defined Google-palette scheme. This is expected.

**`./gradlew assembleRelease` fails.** In the starter, it should succeed (unsigned APK). If it doesn't, something in your setup deviates from the reference. Compare against the [manual build guide](../Starter_Manual_Build_Guide.md) if you have it.

## Credits

Course by Radu Marin, Alex Gherghina, and Radu Ciobanu.

Hosted by Google Romania at the Google Lab EG302, Facultatea de Automatică și Calculatoare, Universitatea POLITEHNICA din București.

## License

Educational use. Adapt freely for your own courses. If you use significant portions in another course or workshop, a mention appreciated but not required.
