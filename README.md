# SaxiInventory

📱 Inventory Management App - School CRUD Project

🎥 Demo

<div align="center">
    <video src="https://github.com/user-attachments/assets/60a4372c-a19d-4b31-8ab6-5f4b04f20e44" width="400" controls>
        Your browser does not support the video tag.
    </video>
</div>



📝 Overview
The Inventory Management App is an Android application designed for school inventory tracking. Users can view stock levels, manage transactions for purchases and sales, and access supplier details with their respective products. The app seamlessly integrates with a backend service running on XAMPP via Retrofit API calls.

🚀 Tech Stack

UI → Jetpack Compose (Modern declarative UI)

Dependency Injection → Koin (Lightweight DI)

API Handling → Retrofit (Networking)

Architecture → MVI (Model-View-Intent)

Database → MySQL (Hosted via XAMPP backend)

State Management → Kotlin Coroutines & Flow

Navigation → Jetpack Navigation

🔥 Features

✅ View Inventory → Browse all available stock items

✅ Track Product Availability → Check stock levels dynamically

✅ Manage Transactions → Purchase & sell products

✅ Supplier Management → View supplier details & linked products

✅ API Integration → Backend service for inventory data CRUD operations

✅ Composable UI → Fully optimized Jetpack Compose screens

✅ Offline Support (Optional) → Cache data for better availability

🔗 Backend API Integration
This app communicates with a backend service running on XAMPP. Ensure your backend is running:

🏛 Architecture (MVI)
The app follows MVI Architecture for reactive UI updates & separation of concerns:

User Action → Intent → ViewModel → Repository → API → UI State

This ensures smooth UI interactions and reduces complexity in handling data updates.

🛠 Development Tools

Postman → API Testing

Android Studio → Development IDE

Kotlin Coroutines & Flow → Async state management

GitHub CI/CD → Continuous integration
