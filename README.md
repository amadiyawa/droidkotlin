# 🚀 Droid Kotlin 1.0

[![Kotlin Version](https://img.shields.io/badge/Kotlin-2.1.x-blue.svg)](https://kotlinlang.org)
[![AGP](https://img.shields.io/badge/AGP-8.x-blue?style=flat)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue?style=flat)](https://gradle.org)

---

## 🏆 Introduction
**DroidKotlin** is a modern, scalable, and modular Android template project designed to help developers build high-quality applications with minimal effort. It integrates best practices in Android development, a robust architecture, and popular libraries for enhanced productivity.

👉 The goal of DroidKotlin is to:
- Provide a clean and scalable codebase.
- Simplify networking, dependency injection, and data persistence.
- Offer a production-ready structure for any Android project.

> **Why DroidKotlin?**  
> This project serves as a comprehensive example for designing well-structured and maintainable Android applications—perfect for large teams and long-term projects.

---

## 📖 Table of Contents
- [Application Scope](#🎯-application-scope)
- [Tech-Stack](#tech-stack)
- [Architecture](#architecture)
- [Logcat debugging](#logcat-debugging)
- [CI Pipeline](#ci-pipeline)
- [Design Decisions](#design-decisions)
- [What This Project Does Not Cover?](#what-this-project-does-not-cover)
- [Getting Started](#getting-started)
- [Upcoming Improvements](#upcoming-improvements)
- [Inspiration](#inspiration)
- [Known Issues](#known-issues)
- [Contribute](#contribute)
- [Author](#author)
- [License](#license)

---

## 🎯 Application Scope
DroidKotlin is designed to:  
✅ Provide a scalable and modular project structure.  
✅ Handle complex data flows using Kotlin Coroutines and StateFlow.  
✅ Simplify REST API integration using Retrofit.  
✅ Manage data persistence with Room and Jetpack components.  
✅ Ensure robust dependency management using Dagger/Hilt.

---

## 🛠️ Tech-Stack

### 💻 100% Kotlin
- **Kotlin** – Modern programming language with concise syntax and robust type safety.
- **Kotlin Coroutines** – Perform asynchronous background operations without blocking the main thread.
- **Kotlin Flow** – Handle data streams and state across app layers.
- **Kotlin Serialization** – Parse JSON data directly into Kotlin objects.
- 🌐 Networking
    - **Retrofit** – Type-safe HTTP client for networking.
    - **Gson** – Convert JSON responses to Kotlin objects.
- 🚀 Jetpack
    - **Compose** – Modern, declarative UI toolkit for building native interfaces.
    - **Navigation** – Handle in-app navigation and deep linking.
    - **Lifecycle** – Manage component lifecycles and state changes.
    - **ViewModel** – Store and manage UI-related data in a lifecycle-aware way.
    - **Room** – Store and query local data using SQLite with compile-time safety.
    - **DataStore** – Store key-value pairs with coroutine support.
- 📲 Dependency Injection
    - **Koin** – Lightweight dependency injection framework designed for Kotlin.
- 🖼️ Image Loading
    - **Coil** – Image loading library with Kotlin coroutines support.
- 🎬 Animation
    - **Lottie** – Render complex vector animations from JSON files.

---

### 🏛️ Modern Architecture
- **MVVM** – Model-View-ViewModel for separation of concerns.
- **Clean Architecture** – Layers of abstraction to separate data, domain, and presentation.
- **Single Activity Architecture** – All navigation handled within a single activity using Navigation Component.
- **Android KTX** – Kotlin extensions for Jetpack and Android APIs.

---

### 🎨 UI
- **Material Design 3** – Consistent UI components and design principles.
- **Dark Theme** – Support for dark theme (Android 10+).
- **Dynamic Theming** – Adapt UI colors based on the device wallpaper (Android 12+).
- **Reactive UI** – State-driven UI rendering with Jetpack Compose.

---

### ✅ CI/CD
- **GitHub Actions** – Automate build, test, and deployment pipelines.
- **Automatic PR Verification** – Run tests and lint checks on pull requests.

---

### 🧪 Testing
- **JUnit 5** – Unit testing framework.
- **MockK** – Kotlin-specific mocking library for tests.
- **Espresso** – UI testing framework (Work in Progress).

---

### 🛡️ Static Analysis Tools (Linters)
- **Ktlint** – Enforce Kotlin coding style.
- **Detekt** – Detect code complexity, code smells, and anti-patterns.
- **Android Lint** – Identify Android-specific issues.

---

### ⚙️ Gradle
- **Gradle Kotlin DSL** – Write build scripts in Kotlin.
- **Gradle Plugins**
    - **Android Gradle** – Standard Android Plugins.
    - **Test Logger** – Format and display test logs.
    - **SafeArgs** – Pass data between navigation destinations.
    - **Android-JUnit5** – Enable JUnit5 testing in Android.
- **Versions Catalog** – Centralized dependency management.
- **Type-Safe Accessors** – Ensure type safety when accessing build properties.

---

### 🛡️ GitHub Automation
- **Renovate** – Automatically update dependencies.
- **Stale** – Close inactive issues and pull requests automatically.

---

### 🛠️ Other Tools
- **Charles Proxy** – Inspect and debug network traffic during development.
- **Timber** – Lightweight logger for debugging.

---

✅ This covers the full tech stack in DroidKotlin, highlighting each component's purpose and function. Let me know if you'd like to refine any part! 😎🔥

## Architecture

## Logcat debugging

## CI Pipeline

## Design Decisions

## What This Project Does Not Cover?

## Getting Started

## Upcoming Improvements

## Inspiration

## Known Issues

## 🙌 Contribute

Contributions are welcome and appreciated! 🎉  
If you’d like to improve DroidKotlin or suggest new features, feel free to open an issue or submit a pull request. Let's make this project even better together!

---

### 💡 How to Contribute
Follow these steps to contribute:

---

### 1. 🚀 Fork the Repository
- Click on the **"Fork"** button at the top right of the repository page.
- This will create a copy of the repository under your GitHub account.

---

### 2. 🖥️ Clone Your Fork
Clone the repository to your local machine using the command below:
```sh
git clone https://github.com/your-username/droidkotlin.git
```

### 3. 🌿 Create a New Branch
Create a new branch for your feature or fix:
```sh
git checkout -b feature/your-feature-name
```
✅ Branch Naming Conventions:
- feature/your-feature-name – For adding new features
- fix/your-fix-name – For fixing bugs
- docs/your-doc-update – For improving documentation

### 4. 🛠️ Set Up the Project
Make sure your development environment is ready:
- Install dependencies using Gradle
- Build the project
- Run the project locally

### 5. ️ ✍️ Make Changes
- Write clean, readable, and well-documented code.
- Follow the project's coding style and naming conventions.
- Keep changes focused on a single feature or fix to simplify code review.
- Test your code thoroughly before committing.

### 6. ️ 💾 Commit Your Changes
Use meaningful and structured commit messages:
```sh
git commit -m "Add: new feature description"
```
✅ Commit Message Format:
- Add: → For new features
- Fix: → For bug fixes
- Refactor: → For code improvements
- Docs: → For documentation updates
Example:
```sh
git commit -m "Fix: Resolve crash on startup"
```

### 7. ️ 🚀 Push to Your Fork
Push your changes to your forked repository:
```sh
git push origin feature/your-feature-name
```

### 8. ️ 🔥 Create a Pull Request (PR)
- Go to the repository on GitHub.
- Click on "Compare & Pull Request."
- Write a clear and descriptive PR title and description:
✅ PR Title Example:
Add: New onboarding screen
✅ PR Description Template:
- Push your changes to your forked repository:
```markdown
### Purpose:
- Add a new onboarding screen for the app.

### Testing:
- Tested on Pixel 4a 5G, API 30

### Side Effects:
- None
```

### 9. ️ 🗣️ Respond to Feedback
- Engage in the code review process.
- Make changes if requested.
- Be respectful and professional when discussing suggestions.

### 10. ️ 🎉 Merge Process
- Once approved, the maintainers will merge the PR into the main branch.
- Celebrate your contribution! 🎉

✅ Types of Contributions
We welcome the following contributions:
- 🐞 Bug Fixes – Found a bug? Let’s fix it!
- 🚀 New Features – Got an idea? Let’s implement it!
- 📝 Documentation – Help improve the project documentation.
- 🎨 UI/UX Improvements – Help refine the user experience.
- 🔍 Code Review – Review and suggest improvements to existing code.
- 🔧 Refactoring – Help clean up the code and improve performance.

🚨 Contribution Guidelines
- Follow the Kotlin Coding Conventions.
- Keep pull requests focused and concise.
- For large changes, create an issue first to discuss the approach.
- Keep commit messages structured and meaningful.
- Be respectful and kind when providing feedback — we're building this together!

❤️ Thank you for considering contributing to DroidKotlin!

## 👨‍💻 Author

**Amadou Iyawa** – Passionate Software Developer specializing in Android and backend development.  
With expertise in Kotlin, Java, Python and JavaScript, Amadou focuses on building scalable and maintainable applications.  
DroidKotlin is a reflection of his passion for clean architecture and efficient development.

---

🌍 **Follow me**
- 🚀 [GitHub](https://github.com/amadiyawa)
- 🐦 [X (Twitter)](https://x.com/amadiyawa)
- 💼 [LinkedIn](https://www.linkedin.com/in/amadiyawa)

---

❤️ This project was created with dedication and care — if you find it helpful, consider giving it a ⭐ on GitHub to show your support!

## License

```
MIT License

Copyright (c) 2025 Amadou Iyawa

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

1. The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
2. Attribution is not required, but it would be appreciated — consider giving the project a ⭐ on GitHub or mentioning it in your project!

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```

---

## 🌟 Additional Terms and Notes from the Author:

This project, **DroidKotlin**, represents countless hours of development and care.  
If you find it helpful, consider giving it a ⭐ on GitHub to support the project and inspire future updates!  
Contributions and feedback are welcome — let's make Android development more powerful together.

**Created and maintained with passion by Amadou Iyawa** ❤️  