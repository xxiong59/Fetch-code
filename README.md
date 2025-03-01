# Fetch Data Demo App

## Overview
This Android application retrieves data from the Fetch API endpoint (https://fetch-hiring.s3.amazonaws.com/hiring.json) and displays it in an organized, user-friendly interface. The app groups items by their `listId`, sorts them first by `listId` and then by the number in the item name, and filters out any items with null or empty names.

## Technical Implementation

### Architecture
The app follows the MVVM (Model-View-ViewModel) architecture pattern with Repository pattern for clean separation of concerns:

- **Model**: Data classes that represent the JSON structure
- **View**: Activity and RecyclerView adapters to display the data
- **ViewModel**: Manages UI-related data and handles business logic
- **Repository**: Single source of truth for data operations

### Key Components

#### Data Model
```kotlin
data class FetchDemoDataItem(
    val id: Int,
    val listId: Int,
    val name: String?
)
```

#### Repository
The repository fetches data from the API, filters out items with null or empty names, and sorts them properly:
- Uses Retrofit for network operations
- Implements error handling with Result class
- Performs data processing (filtering, sorting, grouping)

#### ViewModel
The ViewModel exposes LiveData for the UI to observe:
- Grouped data structure
- Loading state
- Error messages
- Helper methods to access data by listId

#### UI Components
- **GroupedDataAdapter**: Custom RecyclerView adapter that handles grouped data with headers
- **ItemDividerDecoration**: Custom decoration for drawing separator lines between items
- **Header layouts**: Distinct design for group headers

### Libraries Used
- **Retrofit**: For network operations
- **Gson**: For JSON parsing
- **Kotlin Coroutines**: For asynchronous operations
- **AndroidX**: For modern Android components
- **LiveData**: For observable data pattern
- **ViewModel**: For lifecycle-aware data management

## Setup Instructions
1. Clone the repository
2. Open the project in Android Studio
3. Build and run the app on an emulator or physical device

## Requirements
- Android Studio 4.0+
- Minimum SDK: API 28
- Target SDK: API 34
- compileSdk: API 35
