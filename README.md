## 🔧 **Tech Stack**

* **Language**: Kotlin
* **UI Toolkit**: Jetpack Compose
* **State Management**:

  * `StateFlow` for reactive state
  * `ViewModel` from Jetpack Architecture Components
* **Dependency Injection**: Hilt (`@HiltViewModel`, `@Inject`)
* **HTML Parsing**: [Jsoup](https://jsoup.org/)
* **Architecture**: MVVM with Clean Architecture principles
* **Testing**:

  * Jetpack Compose UI Test (`ui-test-junit4`)
  * JUnit 4 (`@Rule`, `@Test`)
* **Scrolling & Layout**: `Column` with `verticalScroll()` and spacing via `Arrangement.spacedBy`
* **Loading/Error Handling**: Controlled via `ContentState` and rendered conditionally
* **Build System**: Gradle (Groovy or Kotlin DSL)
* **Annotation Processing**: KSP (`hilt-compiler` via KSP)

---

## 🧱 **Architecture Overview**

* **MVVM + Clean Architecture**:

  * **Model Layer**:

    * Business logic is abstracted into a `UseCase` class.
    * A `Repository` interfaces with a `DataSource`, which uses **Jsoup** for HTML parsing.

  * **ViewModel Layer**:

    * Exposes a `ContentState` via `StateFlow`.
    * Handles UI actions using `ContentIntent`, and communicates with the domain layer (`UseCase`).

  * **UI Layer (View)**:

    * `HomeScreen` observes the `ContentState` and updates UI accordingly.
    * All rendering is done with modular, testable Composables like `Char15thSection`, `ErrorMessage`, etc.

* **Unidirectional Data Flow**:

  * UI sends `ContentIntent` (e.g., `FetchContentButtonClicked`)
  * ViewModel invokes the appropriate UseCase
  * UseCase processes and parses data using Jsoup
  * ViewModel updates `ContentState`
  * UI re-composes based on the new state

* **Composable Extraction**:

  * Reusable UI parts (e.g., `Char15thSection`, `WordFrequenciesSection`) are broken out to make the UI modular and testable.

* **Testability**:

  * `HomeScreenContent` is separated from the ViewModel for **pure UI testing**.
  * Business logic in `UseCase` and `ViewModel` is unit tested independently.
  * Coroutine dispatchers are injected for easier test control.

