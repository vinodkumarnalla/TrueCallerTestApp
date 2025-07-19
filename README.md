## 🔧 **Tech Stack**

* **Language**: Kotlin
* **UI Toolkit**: Jetpack Compose
* **State Management**:

  * `StateFlow` for reactive state
  * `ViewModel` from Jetpack Architecture Components
* **Dependency Injection**: Hilt (`hiltViewModel()`)
* **Testing**:

  * Jetpack Compose UI Test (`ui-test-junit4`)
  * JUnit 4 (`@Rule`, `@Test`)
* **Scrolling & Layout**: `Column` with `verticalScroll()` and spacing via `Arrangement.spacedBy`
* **Loading/Error Handling**: Displayed through conditional rendering in Compose
* **Build System**: Gradle (with Kotlin DSL or Groovy)

---

## 🧱 **Architecture Overview**

* **MVVM Pattern**:

  * **Model**: Business logic and data parsing live in the ViewModel.
  * **ViewModel**: Emits a `ContentState` using `StateFlow`. Handles `ContentIntent` actions.
  * **View (UI)**: `HomeScreen` is a Composable that observes the ViewModel and updates the UI based on state.

* **Unidirectional Data Flow**:

  * UI dispatches user actions (`FetchContentButtonClicked`) via intents.
  * ViewModel processes the intent, updates the state.
  * UI reacts to `ContentState` changes.

* **Composable Extraction**: UI is modularized (e.g., `Char15thSection`, `ErrorMessage`) for easier reuse and testing.

* **Testability**:

  * UI logic is decoupled from the ViewModel using `HomeScreenContent` to allow pure UI testing.
  * ViewModel intent handling can be unit tested separately.
