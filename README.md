# 📊 AI-Powered Finance Tracker (Backend – Microservices)

A **microservice-based finance tracker backend** designed for **mobile applications**, focused on **automatic expense tracking** using **AI-driven SMS parsing**.

The system securely authenticates users, processes raw bank SMS messages using a **Large Language Model (LLM)**, extracts structured transaction data, and stores it using an **event-driven architecture powered by Kafka**.

> ⚠️ **Frontend / Android application** is intentionally not included due to hardware limitations.  
> This repository focuses purely on **backend architecture, scalability, and real-world system design**.

---

## 🎯 Project Objectives

- Eliminate manual expense entry  
- Automatically track transactions from bank SMS  
- Handle unstructured SMS formats reliably using AI  
- Build a scalable, decoupled microservice architecture  
- Provide a strong backend foundation for a mobile finance app  

---

## 🚀 Core Features

### 🔐 Authentication Service
- JWT-based authentication  
- Refresh token mechanism  
- Secure token rotation  
- Stateless access token validation  
- Publishes authentication events to Kafka  

---

### 👤 User Service
- Maintains user profile and preferences  
- Listens to authentication events  
- Decoupled from Auth Service using Kafka  
- Stores user-specific metadata  

---

### 🤖 AI-Based SMS Transaction Parsing Service
- Accepts raw SMS messages from the client  
- Uses **LLM (Grok API)** to:
  - Identify whether SMS represents a valid transaction  
  - Extract amount, currency, merchant, and transaction type  
- Handles inconsistent and multi-bank SMS formats  
- Publishes parsed transaction events to Kafka  

---

### 💰 Transaction Service
- Consumes parsed transaction events  
- Stores structured financial data  
- Maintains user expense history  
- Designed for future analytics and reporting  

---

### 🔄 Event-Driven Architecture
- Apache Kafka for asynchronous communication  
- Loose coupling between services  
- Improved scalability and fault isolation  
- Event replay capability  

---

### 🐳 Containerized Setup
- Fully Dockerized using Docker Compose  
- One-command local startup  
- Environment-agnostic deployment  

---

## 🏗️ System Architecture


Each service owns its database and communicates asynchronously via Kafka.

---

## 🔄 End-to-End Data Flow

1. User logs in and receives JWT + Refresh token  
2. Auth Service publishes authentication event  
3. User Service consumes event and manages user profile  
4. Raw SMS message is sent to LLM Parsing Service  
5. LLM validates and extracts transaction details  
6. Parsed transaction event is published to Kafka  
7. Transaction Service stores data in database  

---

## 🧠 LLM Integration

- **LLM Provider:** Grok  
- **Integration Method:** API Key  

### Responsibilities
- Natural language understanding of bank SMS  
- Transaction validation  
- Entity extraction (amount, merchant, currency)  

### Why LLM instead of Regex?
- Bank SMS formats are inconsistent  
- Regex is brittle and unscalable  
- LLM provides semantic understanding and adaptability  

---

## 🛠️ Tech Stack

- **Backend Framework:** Spring Boot (Microservices)  
- **Authentication:** JWT + Refresh Tokens  
- **Messaging:** Apache Kafka  
- **AI / NLP:** Grok LLM API  
- **Databases:** Service-specific databases  
- **Containerization:** Docker, Docker Compose  
- **Communication:** REST APIs + Kafka Events  

---

## 📂 Project Structure

finance-tracker
├── auth-service
├── user-service
├── llm-service
├── transaction-service
├── docker-compose.yml
└── README.md


---

## ▶️ Running the Project Locally

### Prerequisites
- Docker  
- Docker Compose  
- Java 17+  

### Start All Services
```bash
docker-compose up --build
