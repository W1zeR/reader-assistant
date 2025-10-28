# Reader Assistant

A full-stack web application that allows users to save, manage, and share quotes from literary works. Features user authentication, quote moderation, and a responsive interface

## Features

- **User Authentication & Authorization**: Secure JWT-based authentication
- **Quote Management**: Create, read, update, and delete personal quotes
- **Public Sharing**: Publish quotes to a public feed
- **Moderation System**: Admin approval required for public quotes
- **Advanced Filtering**: Sorting, filtering, and pagination

## Application Overview

### Key Screenshots

#### Public Quote Feed (Unauthenticated View)

<kbd> <img src="/docs/interface/1.png" alt="Public Quotes"> </kbd>

*The main page showing all approved public quotes*

#### Private Quotes (Authenticated User View)

<kbd> <img src="/docs/interface/12.png" alt="Private Quotes"> </kbd>

*Personal quote management space for authenticated users*

#### Moderation Queue (Admin View)

<kbd> <img src="/docs/interface/17.png" alt="Moderation Queue"> </kbd>

*Administrative panel for reviewing quotes pending publication*

### Use Case Diagram

<kbd> <img src="/docs/uml/use-case.jpg" alt="Use Case Diagram"> </kbd>

## Project Structure
```
reader-assistant/
├── backend/ # Backend API server
├── docs/ # Documentation
├── frontend/ # Frontend application
│ ├── interface/ # Application screenshots
│ ├── uml/ # UML diagrams
│ └── logical-model/ # Logical data model
├── LICENSE
└── README.md
```

## Quick Start

### Backend Setup
Detailed setup instructions available in [backend/README.md](/backend/README.md)

### Frontend Setup  
Detailed setup instructions available in [frontend/README.md](/frontend/README.md)

## Technology Stack

### Backend
- **Framework**: Spring
- **Database**: PostgreSQL
- **Authentication**: JWT

### Frontend  
- **Framework**: Next.js
- **UI Library**: NextUI
- **Authentication**: NextAuth.js

## API Documentation

Once running, access interactive API docs at: `http://localhost:8081/swagger-ui/index.html`
