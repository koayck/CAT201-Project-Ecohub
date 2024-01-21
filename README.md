# EcoHub - Sustainability Tracker

## Project Description
EcoHub is a sustainability tracker designed to help users monitor and manage their environmental footprint. It provides features for recording and tracking sustainability progress, promoting a more sustainable lifestyle. Track your carbon footprint, and contribute to a greener world with EcoHub.

## Table of Contents
- [Technology Used](#technology-used)
- [Team Members](#team-members)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Contributing](#contributing)

## Technology Used
- Java
- JavaFX (for the GUI)
- Maven (for project management)
- MySQL (for database management)

## Team Members
- **Koay Chun Keat**
  - GitHub: [koayck](https://github.com/koayck)

- **Lai Yicheng**
  - GitHub: [Jisi-A](https://github.com/Jisi-A)

- **Ang De Jie**
  - GitHub: [Dejie1](https://github.com/Dejie1)

- **Lee Ying Shen**
  - GitHub: [JohnasLeee](https://github.com/JohnasLeee)

## Project Structure

ecohub
├─ src
│ └─ main
│ ├─ java
│ │ └─ com
│ │ └─ ecohub
│ │ ├─ controller
│ │ │ ├─ AddRecordController.java
│ │ │ ├─ DashboardController.java
│ │ │ ├─ DeleteRecordController.java
│ │ │ ├─ HomeController.java
│ │ │ ├─ LoginController.java
│ │ │ ├─ RecordController.java
│ │ │ ├─ RecordItemController.java
│ │ │ └─ SignUpController.java
│ │ ├─ dao
│ │ │ ├─ DatabaseUtil.java
│ │ │ ├─ DBUtil.java
│ │ │ ├─ Logger.java
│ │ │ ├─ RecordDAO.java
│ │ │ ├─ UserDAO.java
│ │ │ └─ Utility.java
│ │ ├─ dialog
│ │ │ └─ AlertInfoController.java
│ │ ├─ models
│ │ │ ├─ Record.java
│ │ │ └─ User.java
│ │ ├─ session
│ │ │ └─ UserSession.java
│ │ └─ App.java
│ └─ resources
│ └─ com
│ └─ ecohub
│ ├─ fxml
│ │ ├─ AddRecord.fxml
│ │ ├─ AlertInfo.fxml
│ │ ├─ Dashboard.fxml
│ │ ├─ DeleteRecord.fxml
│ │ ├─ Home.fxml
│ │ ├─ Login.fxml
│ │ ├─ Record.fxml
│ │ ├─ RecordItem.fxml
│ │ └─ SignUp.fxml
│ ├─ img
│ │ ├─ carbon-footprint.png
│ │ ├─ carbon.png
│ │ ├─ earth.png
│ │ ├─ ecohub-logo.png
│ │ ├─ home-banner.png
│ │ ├─ renewable-energy.png
│ │ └─ road-map.png
│ └─ styles
│ └─ theme.css
├─ target
│ └─ ... (compiled classes, generated sources, etc.)
├─ pom.xml
└─ README.md