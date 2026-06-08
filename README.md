# AirLinq - Airline Management Microservices Platform

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.x-blue)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![MySQL](https://img.shields.io/badge/Database-MySQL-lightgrey)
![Razorpay](https://img.shields.io/badge/Payment-Razorpay-darkblue)

AirLinq is a full-stack-ready airline booking backend built with Spring Boot microservices. It models a real airline reservation workflow including user authentication, airline onboarding, aircraft and flight operations, pricing, seat inventory, booking, and Razorpay-based payment verification.

The project is designed to demonstrate production-style backend engineering concepts such as service discovery, API gateway routing, JWT authentication, inter-service communication, containerization, and end-to-end booking orchestration.

---

## Table Of Contents

- [Project Overview](#project-overview)
- [Key Features](#key-features)
- [Architecture](#architecture)
- [Microservices](#microservices)
- [Tech Stack](#tech-stack)
- [Core User Flow](#core-user-flow)
- [API Endpoints](#api-endpoints)
- [Docker Setup](#docker-setup)
- [Local Setup](#local-setup)
- [Environment Variables](#environment-variables)
- [Future Improvements](#future-improvements)

---

## Project Overview

AirLinq provides backend APIs for three major actors:

| Actor | Description |
|---|---|
| User | Searches flights, selects fare and seat, creates booking, pays using Razorpay |
| Airline Owner | Creates airline, aircraft, flights, schedules, fares, baggage policies, and seat maps |
| System Admin | Can approve, suspend, or ban airlines |

The complete backend is split into independent services that communicate using OpenFeign and are discovered through Eureka Server. External clients interact only with the API Gateway.

---

## Key Features

### Authentication And Authorization

- JWT-based login and signup
- API Gateway level JWT validation
- Role-based access for users, airline owners, and system admins
- User identity forwarded to services using headers:
  - `X-User-Id`
  - `X-User-Email`
  - `X-User-Role`

### Airline And Aircraft Management

- Airline registration by airline owners
- Airline approval, suspension, and ban support
- Aircraft creation with class-wise seat capacity
- Aircraft status and availability management

### Location Service

- City APIs
- Airport APIs
- Airport lookup by city
- Search and filtering support

### Flight Operations

- Flight creation between two airports
- Flight schedule creation
- Flight instance generation
- Search flight instances by route and date
- Flight status lifecycle support

### Pricing Service

- Fare creation by airline owners
- Fare search by airline, flight, flight instance, cabin class, fare type, and status
- Lowest fare lookup
- Baggage policy management

### Seat Service

- Seat map creation per aircraft
- Seat creation by cabin class and seat type
- Seat instance generation per flight instance
- Seat hold, book, release, block, and unblock flow
- Expired held seat release support

### Booking Service

- Booking creation with passenger details
- Seat hold during booking creation
- Booking confirmation after successful payment
- Booking cancellation and pending booking expiration
- Booking lookup by id, reference, and user

### Payment Service

- Razorpay order creation
- Razorpay payment verification
- Payment status tracking
- Automatic booking confirmation after successful payment

### DevOps

- Dockerized services
- Docker Compose support
- MySQL container setup
- Eureka-based service discovery
- API Gateway as single public entry point

---

## Architecture

```text
Client / Frontend
      |
      v
API Gateway
      |
      +--> User Service
      +--> Airline Service
      +--> Location Service
      +--> Flight Ops Service
      +--> Pricing Service
      +--> Seat Service
      +--> Booking Service
      +--> Payment Service
      |
      v
Eureka Server

Each service owns its own MySQL database.
```

### Inter-Service Communication

| Source Service | Calls |
|---|---|
| Airline Service | User Service |
| Flight Ops Service | Airline Service, Location Service |
| Pricing Service | Airline Service, Flight Ops Service |
| Seat Service | Airline Service, Flight Ops Service |
| Booking Service | Flight Ops Service, Pricing Service, Seat Service |
| Payment Service | Booking Service |

---

## Microservices

| Service | Port | Responsibility |
|---|---:|---|
| Eureka Server | `8761` | Service discovery |
| API Gateway | `8080` | Routing, JWT validation, role authorization |
| User Service | `5001` | Authentication, users, roles |
| Airline Service | `5002` | Airlines and aircraft |
| Booking Service | `5003` | Booking lifecycle |
| Location Service | `5004` | Cities and airports |
| Flight Ops Service | `5005` | Flights, schedules, flight instances |
| Pricing Service | `5006` | Fares and baggage policies |
| Seat Service | `5007` | Seat maps, seats, seat instances |
| Payment Service | `5010` | Razorpay order and payment verification |

---

## Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Spring Cloud Gateway MVC
- Spring Cloud OpenFeign
- Spring Cloud Netflix Eureka
- Hibernate
- Jakarta Validation

### Database

- MySQL
- Database-per-service pattern

### Security

- JWT
- Gateway-based authentication
- Role-based route protection

### Payment

- Razorpay Orders API
- Razorpay payment signature verification

### DevOps

- Docker
- Docker Compose
- Maven multi-module build

---

## Core User Flow

```text
1. User signs up / logs in
2. User searches flight instances
3. User selects fare
4. User selects available seat
5. User creates booking
6. Booking service holds selected seat
7. User creates Razorpay order
8. User completes Razorpay payment
9. Payment service verifies Razorpay signature
10. Payment service confirms booking
11. Booking service books held seat
12. Booking becomes CONFIRMED
```

---

## API Endpoints

All client requests should go through:

```text
http://localhost:8080
```

### Auth APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/auth/signup` | Register user |
| `POST` | `/auth/login` | Login and receive JWT |

### User APIs

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/users/profile` | Get current user profile |
| `GET` | `/api/users/{id}` | Get user by id |
| `GET` | `/api/users` | Get all users |

### Airline APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/airline` | Create airline |
| `GET` | `/api/airline/admin` | Get airline by logged-in owner |
| `GET` | `/api/airline/{id}` | Get airline by id |
| `GET` | `/api/airline/all` | Get all airlines |
| `GET` | `/api/airline/dropdown` | Get active airlines for dropdown |
| `PUT` | `/api/airline` | Update owner airline |
| `DELETE` | `/api/airline/{id}` | Delete airline |
| `POST` | `/api/airline/{id}/approve` | Approve airline |
| `POST` | `/api/airline/{id}/suspend` | Suspend airline |
| `POST` | `/api/airline/{id}/ban` | Ban airline |

### Aircraft APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/aircraft` | Create aircraft |
| `GET` | `/api/aircraft/{id}` | Get aircraft by id |
| `GET` | `/api/aircraft/all` | Get aircraft owned by airline owner |
| `PUT` | `/api/aircraft/{id}` | Update aircraft |
| `DELETE` | `/api/aircraft/{id}` | Delete aircraft |

### Location APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/cities` | Create city |
| `GET` | `/api/cities/{id}` | Get city by id |
| `GET` | `/api/cities/all` | Get all cities |
| `GET` | `/api/cities/search` | Search cities |
| `GET` | `/api/cities/country/countryCode` | Search cities by country code |
| `GET` | `/api/cities/exists/{cityCode}` | Check if city exists |
| `PUT` | `/api/cities/{id}` | Update city |
| `DELETE` | `/api/cities/{id}` | Delete city |
| `POST` | `/api/airport` | Create airport |
| `GET` | `/api/airport/{id}` | Get airport by id |
| `GET` | `/api/airport/all` | Get all airports |
| `GET` | `/api/airport/cityId/{cityId}` | Get airports by city |
| `PUT` | `/api/airport/{id}` | Update airport |
| `DELETE` | `/api/airport/{id}` | Delete airport |

### Flight Ops APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/flight` | Create flight |
| `GET` | `/api/flight/{id}` | Get flight by id |
| `GET` | `/api/flight/airline` | Search flights by airline owner |
| `PUT` | `/api/flight/{id}` | Update flight |
| `POST` | `/api/flight/status/{id}` | Change flight status |
| `DELETE` | `/api/flight/{id}` | Delete flight |
| `POST` | `/api/schedule` | Create flight schedule |
| `GET` | `/api/schedule/{id}` | Get schedule by id |
| `GET` | `/api/schedule/airline` | Get schedules by airline owner |
| `PUT` | `/api/schedule/{id}` | Update schedule |
| `DELETE` | `/api/schedule/{id}` | Delete schedule |
| `POST` | `/api/flight-instance` | Create flight instance |
| `GET` | `/api/flight-instance/{id}` | Get flight instance by id |
| `POST` | `/api/flight-instance/search` | Search flight instances |
| `PUT` | `/api/flight-instance/{id}` | Update flight instance |
| `DELETE` | `/api/flight-instance/{id}` | Delete flight instance |

### Pricing APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/fares` | Create fare |
| `GET` | `/api/fares/{id}` | Get fare by id |
| `GET` | `/api/fares/search` | Search fares |
| `GET` | `/api/fares/flight/{id}` | Get fares by flight id |
| `GET` | `/api/fares/flight/{id}/lowest` | Get lowest fare by flight id |
| `PUT` | `/api/fares/{id}` | Update fare |
| `DELETE` | `/api/fares/{id}` | Delete fare |
| `POST` | `/api/baggage-policies` | Create baggage policy |
| `GET` | `/api/baggage-policies/{id}` | Get baggage policy by id |
| `GET` | `/api/baggage-policies/search` | Search baggage policies |
| `GET` | `/api/baggage-policies/fare/{fareId}` | Get policy by fare |
| `GET` | `/api/baggage-policies/flight-instance/{flightInstanceId}` | Get policy by flight instance and cabin class |
| `PUT` | `/api/baggage-policies/{id}` | Update baggage policy |
| `DELETE` | `/api/baggage-policies/{id}` | Delete baggage policy |

### Seat APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/seat-maps` | Create seat map |
| `GET` | `/api/seat-maps/{id}` | Get seat map by id |
| `GET` | `/api/seat-maps/airline` | Get seat maps by airline |
| `GET` | `/api/seat-maps/aircraft/{aircraftId}` | Get seat maps by aircraft |
| `PUT` | `/api/seat-maps/{id}` | Update seat map |
| `DELETE` | `/api/seat-maps/{id}` | Delete seat map |
| `POST` | `/api/seat` | Create seat |
| `GET` | `/api/seat/{id}` | Get seat by id |
| `GET` | `/api/seat/seatMap/{seatMapId}` | Get seats by seat map |
| `GET` | `/api/seat/seatMapCabin` | Get seats by seat map and cabin |
| `PUT` | `/api/seat/{id}` | Update seat |
| `DELETE` | `/api/seat/{id}` | Delete seat |
| `POST` | `/api/seat-instances/generate` | Generate seat instances for flight instance |
| `GET` | `/api/seat-instances/{id}` | Get seat instance by id |
| `GET` | `/api/seat-instances/flight-instance/{flightInstanceId}` | Get seats by flight instance |
| `GET` | `/api/seat-instances/flight-instance/{flightInstanceId}/available` | Get available seats |
| `GET` | `/api/seat-instances/flight-instance/{flightInstanceId}/available/cabin-class/{cabinClass}` | Get available seats by cabin class |
| `POST` | `/api/seat-instances/{id}/hold` | Hold seat |
| `POST` | `/api/seat-instances/{id}/book` | Book seat |
| `POST` | `/api/seat-instances/{id}/release` | Release seat |
| `POST` | `/api/seat-instances/{id}/block` | Block seat |
| `POST` | `/api/seat-instances/{id}/unblock` | Unblock seat |
| `POST` | `/api/seat-instances/release-expired-held` | Release expired held seats |

### Booking APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/booking` | Create booking |
| `GET` | `/api/booking/{id}` | Get booking by id |
| `GET` | `/api/booking/reference/{bookingReference}` | Get booking by reference |
| `GET` | `/api/booking/user` | Get logged-in user's bookings |
| `POST` | `/api/booking/{id}/confirm` | Confirm booking |
| `POST` | `/api/booking/{id}/cancel` | Cancel booking |
| `POST` | `/api/booking/expire-pending` | Expire pending bookings |

### Payment APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/payment/razorpay/order` | Create Razorpay order |
| `POST` | `/api/payment/razorpay/verify` | Verify Razorpay payment |
| `GET` | `/api/payment/{id}` | Get payment by id |
| `GET` | `/api/payment/booking/{bookingId}` | Get payment by booking |
| `GET` | `/api/payment/user` | Get logged-in user's payments |
| `POST` | `/api/payment/{id}/failed` | Mark payment as failed |

---

## Docker Setup

The project includes Docker support for all services.

### Build And Run

```bash
docker compose up --build
```

### Run In Background

```bash
docker compose up -d --build
```

### Stop Containers

```bash
docker compose down
```

### Stop And Remove Volumes

```bash
docker compose down -v
```

Use `-v` carefully because it removes MySQL data.

---

## Local Setup

### Prerequisites

- Java 17
- Docker Desktop
- Maven or Maven Wrapper
- MySQL, if running services without Docker
- Postman for API testing

### Run With Docker

```bash
docker compose up -d --build
```

Open Eureka dashboard:

```text
http://localhost:8761
```

API Gateway:

```text
http://localhost:8080
```

### Run Manually

Start services in this order:

```text
1. eureka-server
2. api-gateway
3. user-service
4. location-service
5. airline-service
6. flight-ops-service
7. pricing-service
8. seat-service
9. booking-service
10. payment-service
```

---

## Environment Variables

Use environment variables for production secrets.

```env
MYSQL_ROOT_PASSWORD=your_mysql_password
JWT_SECRET_KEY=your_jwt_secret
JWT_EXPIRATION_MS=86400000
RAZORPAY_KEY_ID=your_razorpay_key
RAZORPAY_KEY_SECRET=your_razorpay_secret
RAZORPAY_CURRENCY=INR
RAZORPAY_CALLBACK_URL=http://localhost:5173
```

---

## Project Highlights

- Built using microservice architecture
- Service discovery with Eureka
- Centralized routing and JWT validation through API Gateway
- Database-per-service pattern
- OpenFeign-based service communication
- Razorpay payment integration
- Seat hold and booking lifecycle management
- Dockerized deployment with Docker Compose

---

## Future Improvements

- Add centralized logging with ELK or Loki
- Add distributed tracing with Zipkin or OpenTelemetry
- Add Resilience4j circuit breakers
- Add Flyway database migrations
- Add refresh token support
- Add notification service for email/SMS
- Add CI/CD pipeline with GitHub Actions
- Move MySQL to managed cloud database for production
- Add Kubernetes deployment manifests

---

## Developer

Built with 💗 by Vaishnav Gupta

| Social Media | Username                                                                          |
|--------------|-----------------------------------------------------------------------------------|
| Github       | [github.com/vaishnavgupta](https://github.com/vaishnavgupta)                      |
| LinkedIn     | [linkedin.com/vaishnavgupta](https://www.linkedin.com/in/vaishnavgupta/)          |
| Portfolio    | [vercel.com/vaishnav-gupta-portfolio](https://vaishnav-gupta-portfolio.vercel.app/) |



