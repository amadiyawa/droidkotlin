# DroidKotlin

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.x-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-API%2024+-3DDC84.svg?style=flat&logo=android)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/github/actions/workflow/status/amadiyawa/droidkotlin/ci.yml?branch=main)](https://github.com/amadiyawa/droidkotlin/actions)

A modern, scalable Android template project built with **Clean Architecture**, **Jetpack Compose**, and **Kotlin Coroutines**. Designed for professional development teams and production-ready applications.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Quick Start](#quick-start)
- [Features](#features)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Testing](#testing)
- [Contributing](#contributing)
- [Documentation](#documentation)
- [Roadmap](#roadmap)
- [License](#license)
- [Support](#support)
- [Author](#author)

## Overview

DroidKotlin provides a comprehensive foundation for Android applications, integrating industry best practices, modern architecture patterns, and essential libraries. This template eliminates weeks of initial setup and configuration, allowing developers to focus on building unique features.

### Key Benefits

- **Enterprise Security**: Built-in RBAC (Role-Based Access Control) system
- **Time-Saving**: Skip 3-4 months of foundational setup and architecture
- **Production Ready**: Battle-tested patterns with security-first approach
- **Developer Friendly**: Comprehensive documentation and real-world examples
- **Modular Design**: 8 specialized feature modules with permission-based access
- **Adaptive UI**: Responsive navigation that works on all screen sizes
- **Modern Stack**: Latest Android technologies with security best practices

### What's Included

- ✅ **RBAC Security System** - Role-based access control with module-level permissions
- ✅ **Adaptive Navigation** - Bottom bar, navigation rail, and drawer that adapts to screen size
- ✅ **8 Protected Modules** - Each module defines its own navigation and access roles
- ✅ **Authentication System** - Sign in, sign up, password reset, OTP verification
- ✅ **Modern Data Storage** - DataStore implementation replacing SharedPreferences
- ✅ **Billing Template** - Ready-to-customize payment integration structure
- ✅ **Responsive UI** - Jetpack Compose with Material Design 3
- ✅ **Enterprise Architecture** - Clean Architecture with MVVM and security layers
- ✅ **CI/CD Pipeline** - GitHub Actions for automated testing and deployment
- ✅ **Code Quality Tools** - Comprehensive linting, static analysis, and formatting
- ✅ **Testing Setup** - Unit tests, UI tests, and security testing frameworks

## Architecture

This project implements **Clean Architecture** with **MVVM** pattern and **RBAC security** across multiple modules:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Presentation  │    │     Domain      │    │      Data       │
│                 │    │                 │    │                 │
│  • Compose UI   │◄──►│  • Use Cases    │◄──►│  • Repositories │
│  • ViewModels   │    │  • Entities     │    │  • Data Sources │
│  • Navigation   │    │  • RBAC Rules   │    │  • API Services │
│  • Permissions  │    │  • Repositories │    │  • DataStore    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Security & Navigation Architecture

**Role-Based Access Control (RBAC)**
- Each module defines its required permissions and user roles
- Automatic route protection based on user roles
- Granular access control for features and screens
- Centralized permission management with DataStore

**Adaptive Navigation System**
- **Bottom Navigation** - Optimal for mobile phones (portrait)
- **Navigation Rail** - Perfect for tablets and large phones (landscape)
- **Navigation Drawer** - Scalable for complex navigation hierarchies
- **Smart Adaptation** - Automatically switches based on screen size and orientation

### Module Structure

| Module | Purpose | Security & Navigation |
|--------|---------|---------------------|
| `app` | Main application container | Navigation host, RBAC setup, route protection |
| `feature_auth` | Authentication flows | Public access, redirects after login |
| `feature_billing` | Billing template module | Admin/Premium roles, secure payment flows |
| `feature_onboarding` | User introduction | Public access, first-time user guidance |
| `feature_profile` | User management | User role, profile editing permissions |
| `feature_base` | Shared components | Common UI, permission utilities, DataStore |
| `buildSrc` | Build configuration | Dependency management, security configurations |

## Technology Stack

### Core Framework
- **Kotlin** - 100% Kotlin codebase
- **Coroutines** - Asynchronous programming
- **Flow** - Reactive data streams

### UI & Navigation
- **Jetpack Compose** - Modern declarative UI framework
- **Material Design 3** - Latest design system with dynamic theming
- **Adaptive Navigation** - Bottom bar, rail, and drawer components
- **Navigation Component** - Type-safe navigation with route protection
- **Responsive Design** - Optimized for phones, tablets, and foldables
- **Permission-Based UI** - Components that adapt based on user roles

### Architecture & DI
- **Clean Architecture** - Separation of concerns
- **MVVM Pattern** - Presentation layer architecture
- **Koin** - Lightweight dependency injection
- **Single Activity** - Simplified navigation model

### Data & Security
- **DataStore** - Modern, type-safe preferences and settings storage
- **Room** - Local database with SQLite and encryption support
- **RBAC System** - Role-based access control for features and data
- **Secure Storage** - Encrypted storage for sensitive user data
- **Permission Manager** - Centralized access control system

### Quality & Testing
- **JUnit 5** - Unit testing framework
- **MockK** - Mocking library for Kotlin
- **Espresso** - UI testing framework
- **Detekt** - Static code analysis
- **Ktlint** - Code formatting

### Development Tools
- **Gradle Kotlin DSL** - Type-safe build scripts
- **Version Catalogs** - Centralized dependency management
- **GitHub Actions** - CI/CD automation
- **Timber** - Advanced logging

## Quick Start

### Prerequisites

- **Android Studio** Hedgehog | 2023.1.1 or later
- **JDK** 17 or higher
- **Android SDK** API 24 (Android 7.0) minimum, API 34+ recommended
- **Git** for version control

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/amadiyawa/droidkotlin.git
   cd droidkotlin
   ```

2. **Open in Android Studio**
   - File → Open → Select the `droidkotlin` folder
   - Wait for Gradle sync to complete
   - Resolve any SDK or dependency issues

3. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ./gradlew installDebug
   ```

4. **Customize for your project**
   ```bash
   # Update package names
   # 1. Refactor package in Android Studio
   # 2. Update applicationId in app/build.gradle.kts
   # 3. Update string resources and branding
   # 4. Configure API endpoints in local.properties
   ```

### First Run

After installation, the app demonstrates:
- **Adaptive Navigation** - Experience bottom bar, rail, and drawer navigation
- **RBAC System** - Role-based access to different features and screens
- **Authentication Flow** - Complete sign up/sign in with OTP verification
- **Permission Management** - See how roles control access to features
- **DataStore Integration** - Modern preferences and settings storage
- **Responsive Design** - Test on different screen sizes and orientations

> **Note**: This is a template project showcasing enterprise-grade architecture patterns. Most features demonstrate UI/UX and security patterns rather than full backend integration.

## Features

### Security & Access Control System
- **Role-Based Authentication** - Complete user registration with role assignment
- **Permission Management** - Granular access control for each feature
- **Route Protection** - Automatic navigation guards based on user roles
- **Secure Session Handling** - Token management with automatic refresh
- **Data Encryption** - Sensitive data protection with DataStore encryption

### Adaptive Navigation System
- **Multi-Modal Navigation** - Seamlessly switches between bottom bar, rail, and drawer
- **Screen-Size Aware** - Automatically adapts to device form factor
- **Role-Based Menus** - Navigation items filtered by user permissions
- **Deep Link Support** - Secure deep linking with permission validation

### Modern Data Management
- **DataStore Integration** - Type-safe preferences with encryption support
- **Offline-First Architecture** - Works seamlessly without network connection
- **Real-Time Sync** - Automatic data synchronization when connection restored

### User Experience
- **Adaptive Interface** - UI that responds to screen size and user roles
- **Onboarding Flow** - Role-based introduction with permission setup
- **Profile Management** - Comprehensive user settings with role management
- **Dark Theme** - System-wide dark mode with persistent preferences
- **Accessibility** - Full support for screen readers and accessibility services
- **Offline Support** - Core functionality available without internet connection

## Project Structure

```
droidkotlin/
├── app/                          # Main application module
├── buildSrc/                     # Build logic and dependencies
├── feature_auth/                 # Authentication module
├── feature_base/                 # Shared base components
├── feature_billing/              # Billing and payments
├── feature_onboarding/           # User onboarding
├── feature_profile/              # User profile management
├── gradle/                       # Gradle wrapper
├── .github/workflows/            # CI/CD configuration
└── docs/                         # Project documentation
```

## Configuration

### API Configuration
Create `local.properties` in the root directory:

```properties
# API Configuration
api.base.url="https://api.example.com/"
api.token="your_api_token_here"

# Feature Flags & Security
feature.rbac.enabled=true
feature.adaptive.navigation=true
feature.otp.enabled=true
feature.billing.enabled=false
feature.offline.mode=true

# Security Configuration
security.encryption.enabled=true
security.session.timeout=3600
security.roles.strict.mode=true

# Debug Configuration
debug.network.logging=true
debug.database.export=false
```

### Build Variants
- **Debug** - Development build with debugging tools enabled
- **Release** - Production-ready build with code obfuscation and optimizations
- **Staging** - Pre-production environment for testing

### Environment Setup

1. **API Integration**
   ```kotlin
   // Update base URLs in buildSrc/Dependencies.kt
   object ApiConfig {
       const val BASE_URL_DEV = "https://dev-api.yourapp.com/"
       const val BASE_URL_PROD = "https://api.yourapp.com/"
   }
   ```

2. **Firebase Setup** (Optional)
   - Add `google-services.json` to `app/` directory
   - Configure authentication providers
   - Set up analytics and crashlytics

3. **Signing Configuration**
   ```properties
   # Add to local.properties
   KEYSTORE_PASSWORD=your_keystore_password
   KEY_ALIAS=your_key_alias
   KEY_PASSWORD=your_key_password
   ```

## Testing

Run the test suite:

```bash
# Unit tests
./gradlew test

# UI tests
./gradlew connectedAndroidTest

# Code quality checks
./gradlew detekt ktlintCheck
```

## Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

### Development Workflow

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Standards

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful commit messages
- Add tests for new features
- Update documentation as needed

## Frequently Asked Questions

### Is this a complete application?
No, DroidKotlin is a **template project** that provides structure, architecture, and UI examples. You'll need to integrate with your own backend services and customize the business logic.

### Can I use this for commercial projects?
Yes! The project is licensed under MIT, allowing commercial use. Attribution is appreciated but not required.

### How do I integrate my own API?
1. Update the base URL in `local.properties`
2. Modify the API service interfaces in each feature module
3. Update data models to match your API responses
4. Implement error handling for your specific API

### What about backend services?
This template focuses on the Android app structure. You'll need to provide your own:
- Authentication API (sign up, sign in, password reset)
- OTP service (SMS/email providers)
- Payment processing (Stripe, PayPal, etc.)
- User management system

### Is this suitable for beginners?
DroidKotlin is designed for developers with **intermediate Android knowledge**. Beginners should first learn:
- Kotlin fundamentals
- Android basics (Activities, Fragments)
- Jetpack Compose basics
- MVVM architecture concepts

## Known Limitations

- **Template Nature**: UI flows are demonstrated but not fully functional
- **Backend Required**: No included backend services or API endpoints
- **Payment Integration**: Billing module is structural only, requires payment provider setup
- **Authentication**: Forms and validation implemented, but requires backend authentication service
- **Limited Customization**: Some components may need significant modification for specific use cases

## Getting Help

### Common Issues

**Build failures**: Ensure you're using the correct JDK version and latest Android Studio
**Sync issues**: Try File → Invalidate Caches → Invalidate and Restart
**Module errors**: Check that all modules are properly included in `settings.gradle.kts`

### Community Support

- **GitHub Issues**: Report bugs and request features
- **Discussions**: Ask questions and share experiences
- **Wiki**: Extended documentation and guides

## Roadmap

### Version 1.1
- [ ] Offline-first architecture
- [ ] Advanced caching strategies
- [ ] Performance monitoring
- [ ] Accessibility improvements

### Version 1.2
- [ ] Multi-language support
- [ ] Advanced analytics
- [ ] Push notifications
- [ ] Background sync

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

- **Documentation**: [GitHub Wiki](https://github.com/amadiyawa/droidkotlin/wiki)
- **Issues**: [GitHub Issues](https://github.com/amadiyawa/droidkotlin/issues)
- **Discussions**: [GitHub Discussions](https://github.com/amadiyawa/droidkotlin/discussions)

## Author

**Amadou Iyawa** - *Senior Android Developer*

- GitHub: [@amadiyawa](https://github.com/amadiyawa)
- LinkedIn: [amadiyawa](https://linkedin.com/in/amadiyawa)
- Twitter: [@amadiyawa](https://twitter.com/amadiyawa)

---

**⭐ If this project helps you, please consider giving it a star!**
