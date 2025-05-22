# Catapult
**Catapult** is an Android application developed as an exam project for a mobile development course. The app combines a comprehensive cat breed catalog with an engaging quiz feature, allowing users to explore cat breeds and test their knowledge. The project includes a local account system, a global leaderboard, and supports both individual and team implementations, with additional features like multi-account support and avatar selection for team-based realizations.

## Project Overview

The Catapult app provides a rich user experience for cat enthusiasts. It features a catalog for browsing cat breeds with detailed information and images, a quiz with randomized questions across multiple categories, and a leaderboard to track user scores globally. The app uses a local database as the single source of truth for cat data, ensuring fast and reliable access, while leveraging modern Android development practices for a seamless and visually appealing experience.

### Key Features

**Account Management**

- Local Account Creation: Users create a profile with name, nickname (letters, numbers, underscore only), and email (valid format) on first launch.
- Profile Details: View and edit account details, including a history of quiz results (published and unpublished) and the best score.
- Multi-Account Support (Team Only): Create and switch between multiple local accounts, with only one active at a time.
- Avatar Selection (Team Only): Choose an avatar from a gallery during account creation or editing.
- Logout (Team Only): Log out from the active account with a confirmation dialog, reverting to the initial state or switching to another account.

**Cat Breed Catalog**

- Breed List: Browse a searchable list of cat breeds with basic information.
- Breed Details: View detailed information about a selected breed, including temperament, rarity (optional), and behavior widgets (optional).
- Photo Gallery: Display a grid of images for a selected breed, accessible from the breed details screen.
- Photo Viewer: View individual breed images in full-screen mode with swipeable left-right navigation using a pager component.

**Cat Knowledge Quiz**

- Quiz Structure: 20 randomized questions displayed one at a time, full-screen, across three categories (individual projects implement at least one):
- Guess the Fact: Identify breeds or temperaments based on images (e.g., "Which breed is this cat?" or "Which temperament does not belong?").
- Guess the Cat: Select the correct cat image based on a text question (e.g., "Which cat has this temperament?").
- Left or Right Cat: Compare two cat images to determine which breed is heavier or lives longer.

- Time Limit: 5-minute quiz duration, with unanswered questions scored as 0 upon timeout.
- Scoring: Points calculated using the formula UBP = BTO * 2.5 * (1 + (PVT + 120) / MVT), capped at 100, where BTO is the number of correct answers, PVT is remaining time, and MVT is 300 seconds.
- Leaderboard Integration: Share quiz results to a global leaderboard via the Leaderboard API, with an option to cancel the quiz (with confirmation dialog).
- No Backtracking: Answers are final, and the back button triggers a cancel confirmation dialog.

**Leaderboard**

- Global Rankings: Displays all published quiz results sorted by score, showing global rank, nickname, score, and total quizzes played.
- Category Filtering: View results for a specific quiz category (mandatory for team projects, optional for others).

**Additional Features**

- Local Data Storage: Cat breed data is cached locally using Jetpack Room for fast access.
- Material Design 3: Modern UI with edge-to-edge support and dynamic light/dark theme switching.
- Animations: Smooth transitions between quiz questions.
- Unit Testing (Team Only): Tests for ViewModel, Repository, quiz generation logic, and one Compose screen.

### Technologies Used

- **Programming Language**: Kotlin
- **Architecture**: MVI (Model-View-Intent) architecture
- **UI Framework**: Jetpack Compose
- **Data Storage**:
  - Jetpack Room (Local Database)
  - Jetpack DataStore (Simple Data Storage)

**Networking**:
- Retrofit (HTTP Client)
- OkHttp (HTTP & HTTP/2 Client)

- **Dependency Injection**: Hilt
- **Other**: Kotlin Coroutines, Flow, Jetpack Navigation

## Getting Started
### Prerequisites

- Android Studio (latest stable version)
- Android SDK (API level 21 or higher)
- Physical Android device or emulator for testing

### Installation

1. Clone the repository:
```bash
git clone https://github.com/VukadinovicLuka/RMAProjekat.git
```
2. Open the project in Android Studio:
```bash
cd Catapult
```

3. Build and run:
   Build and run the app on an Android device or emulator.

## Usage

- Create an Account: Set up a local user profile with a nickname and email.
- Explore Cat Breeds: Use the catalog to search and view details about various cat breeds, including images and temperaments.
- Take the Quiz: Answer questions about cat breeds, compete against others, and see your score on the global leaderboard.
- Switch Themes: The app supports both light and dark themes, which can be toggled in the system settings.

## Documentation
Complete project documentation, including detailed functional requirements, technical specifications, and API details, is available in the docs/ directory of the repository or in the provided exam project specification (Ispitni Projekat.docx).

##Contact
For questions, feedback, or support, please reach out to luka.zarkovo29@gmail.com.
