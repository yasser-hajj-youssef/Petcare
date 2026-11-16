# 🐾 PetCare — Pet Care Routine & Reminder Log

PetCare is an Android application designed to help pet owners organize their animals' daily care routines, manage care tasks, and track completed activities.

This project is developed as part of our university Android Development course.

## Features

The current working version includes:

- **Pet Registration:** Add pets with a name, species, and optional birth year.
- **Pet List:** View registered pets and access their details.
- **Care Task Management:** Create daily, weekly, or monthly care tasks.
- **Task Completion:** Mark care tasks as completed.
- **Completion History:** Track previously completed activities.
- **Next Due Date:** Calculate when a care task should be performed again.
- **Reminder Configuration:** Select a preferred reminder time.
- **Local Persistence:** Store pets, tasks, and history using Room Database.

**Note:** Actual scheduled notifications and WorkManager integration are planned for future development.

## Technologies Used

- Kotlin
- Jetpack Compose
- Material 3
- MVVM Architecture
- Room Database (SQLite)
- Kotlin Coroutines
- Flow and StateFlow
- Navigation Compose
- Git and GitHub

## Project Architecture

PetCare follows the MVVM architecture with a Repository pattern.

```text
Jetpack Compose UI
        |
        v
     ViewModel
        |
        v
     Repository
        |
        v
        DAO
        |
        v
   Room Database
```

Room provides observable data through Flow, allowing the ViewModel to expose updated state to the Compose interface.

## How to Run the Application

### Requirements

- Android Studio
- Android SDK installed through Android Studio
- Android emulator or physical Android device
- Internet connection for the initial Gradle dependency download

### Installation

**1. Clone the repository**

Open a terminal and execute:

```bash
git clone https://github.com/yasser-hajj-youssef/Petcare.git
```

**2. Open the project**

- Launch Android Studio.
- Select **Open**.
- Navigate to the cloned `Petcare` folder.
- Select the project directory.

**3. Synchronize Gradle**

Wait for Android Studio to download the dependencies and complete Gradle synchronization.

**4. Select a device**

Choose an Android emulator or connect a physical Android phone with USB debugging enabled.

**5. Run the application**

Select the `app` run configuration and click **Run ▶**.

### Testing the Working Feature

1. Open PetCare.
2. Press the + button to add a pet.
3. Enter a pet name and choose its species.
4. Save the pet.
5. Select the pet from the list.
6. Add a daily care task, such as Feed.
7. Press **Mark done**.
8. Verify that the completion history and next due date update.
9. Close and reopen the application to verify that the information remains stored.

## Database Structure

PetCare uses three Room entities:

| Entity | Purpose |
|---|---|
| `PetEntity` | Stores pet information |
| `CareTaskEntity` | Stores care tasks and reminder settings |
| `CareLogEntity` | Stores task completion history |

Task completion uses a Room transaction to update the last completion timestamp and insert a history record consistently.

## Team Members

**Yasser Hajj Youssef — Pet Registration & Navigation**

- Pet registration and validation
- Pet list interface
- Pet ViewModels
- PetDao and PetRepository
- Screen navigation

**Tony Yammine — Care Tasks & History**

- Care task creation and completion
- Pet detail screen
- Care ViewModel and Repository
- Completion history
- Next due date calculation
- Reminder-time configuration

**Shared Responsibilities**

Database entities, application architecture, integration, and Git collaboration.

## Future Improvements

- Scheduled reminder notifications
- WorkManager for periodic background checks
- Editing and deleting pets and tasks
- Improved user interface
- Additional testing and reliability improvements

## Project Status

**Android Development Midterm — Working Feature Completed**

The current version demonstrates pet registration, care task management, local persistence, and completion history.
