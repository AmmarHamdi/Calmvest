# Calmvest

> A calm, beginner-friendly savings and auto-investing mobile application for everyday users.

## Overview

Calmvest helps users who don't understand trading to build savings and investments effortlessly through automatic round-ups of daily card transactions. Every purchase gets rounded up to the next euro, and once the accumulated savings reach a configurable threshold, an investment order is automatically created — aligned with the user's personal life goal.

**No complex charts. No trading UX. Just calm, purposeful saving.**

---

## Architecture

```
calmvest/
├── android/        # Kotlin + Jetpack Compose Android app
└── backend/        # Kotlin + Spring Boot REST API
```

### Backend — Kotlin + Spring Boot (Modular Monolith)

```
backend/
├── domain/         # Pure domain models, repository interfaces, ports (no Spring deps)
├── application/    # Use-case services (@Service, @Transactional)
├── infrastructure/ # JPA entities, Spring Data repos, external integration stubs
├── api/            # REST controllers, DTOs, exception handling
└── app/            # Spring Boot entry point, Flyway migrations, configuration
```

**Key decisions:**
- `Money` is represented as `Long` minor units (cents) — no floating point anywhere in domain or DB
- Idempotency keys on all financial operations (transaction import, reserve entries, investment orders)
- Audit fields (`createdAt`, `updatedAt`) on all entities
- Flyway owns the schema (`ddl-auto: validate`)
- External providers (open banking, custody) abstracted behind domain interfaces
- `@Transactional` only at service level, never in controllers or repositories

### Android — Kotlin + Jetpack Compose (Clean Architecture)

```
android/
├── app/                      # Application class, MainActivity, Navigation
├── core/
│   ├── domain/               # Domain models, repository interfaces, use cases
│   ├── data/                 # Retrofit API, DTOs, mappers, repository implementations
│   └── ui/                   # Material 3 theme, shared Compose components
└── feature/
    ├── onboarding/            # Onboarding, goal setup, investment mode selection
    ├── dashboard/             # Main dashboard with reserve balance and goal progress
    ├── goals/                 # Goals list and detail with progress tracking
    ├── transactions/          # Transaction history with round-up highlights
    ├── portfolio/             # Portfolio summary with investment orders
    └── settings/              # Round-up rule configuration, plan pause/resume
```

**Key decisions:**
- MVVM: `@HiltViewModel` + `StateFlow<UiState>` (sealed `Loading` / `Success` / `Error`)
- `Money` value class with `Long` minor units — `formatEuros()` for all display
- Hilt for DI with `@Binds` interfaces to implementations
- Coroutines + Flow throughout; `viewModelScope.launch` in ViewModels
- Material 3 with calm green palette (primary `#2D6A4F`)
- Bottom navigation with 5 tabs (Dashboard, Goals, Activity, Portfolio, Settings)
- Anti-impulsive safety controls: confirmation dialogs for withdrawal, plan pause

---

## Domain Model

| Entity | Purpose |
|---|---|
| `User` | App user with KYC status |
| `BankAccount` | Linked bank account via open banking consent |
| `Transaction` | Card transaction with computed round-up amount |
| `RoundUpRule` | Per-user configuration: monthly cap, threshold, pause |
| `ReserveEntry` | Accumulated round-up pot entry (idempotent) |
| `Goal` | Life goal (travel, emergency fund, car, house…) |
| `InvestmentOrder` | Auto or manual investment order (idempotent, audited) |
| `Portfolio` | User's investment portfolio summary |

---

## REST API (MVP)

Base path: `/api/v1`

| Method | Path | Description |
|---|---|---|
| `POST` | `/users` | Create user |
| `GET` | `/users/{id}` | Get user |
| `GET` | `/users/{id}/bank-accounts` | List bank accounts |
| `POST` | `/users/{id}/bank-accounts` | Link bank account |
| `GET` | `/users/{id}/transactions` | List transactions |
| `POST` | `/users/{id}/transactions` | Import transaction (idempotent) |
| `GET/PUT` | `/users/{id}/round-up-rule` | Get/update round-up configuration |
| `GET` | `/users/{id}/reserve` | Get reserve balance |
| `GET/POST` | `/users/{id}/goals` | List/create goals |
| `PUT` | `/users/{id}/goals/{goalId}` | Update goal |
| `GET` | `/users/{id}/portfolio` | Get portfolio summary |
| `GET/POST` | `/users/{id}/investment-orders` | List/trigger investment orders |

---

## Database Schema

Managed by Flyway migrations (`backend/app/src/main/resources/db/migration/`):

- `V1` — `users`
- `V2` — `bank_accounts`
- `V3` — `transactions` (with `idempotency_key` unique constraint)
- `V4` — `round_up_rules`
- `V5` — `reserve_entries` (with `idempotency_key` unique constraint)
- `V6` — `goals`
- `V7` — `investment_orders` (with `idempotency_key` unique constraint)
- `V8` — `portfolios`

---

## Android Screens

| Screen | Description |
|---|---|
| Onboarding | 3-page value proposition carousel |
| Goal Setup | Choose goal type + target amount |
| Investment Mode | Safe / Bitcoin / Diversified selection |
| Dashboard | Reserve balance, goal progress, recent round-ups |
| Goals | Goal list with progress bars + add goal FAB |
| Goal Detail | Circular progress, pause/withdraw controls |
| Transactions | Transaction history with round-up highlights |
| Portfolio | Investment summary, gain/loss, order history |
| Settings | Toggle round-up, set monthly cap, pause plan |
| Round-Up Rule | Detailed cap and threshold configuration |

---

## Investment Modes

| Mode | Description |
|---|---|
| **Safe** | Low-risk bonds and savings instruments |
| **Bitcoin** | Pure Bitcoin exposure, higher volatility |
| **Diversified** | Global ETFs spread across markets |

---

## Safety Controls

- **Monthly cap** — limits total round-up accumulation per calendar month
- **Pause plan** — suspends round-ups until a chosen date
- **Investment threshold** — minimum reserve before triggering auto-investment
- **Withdrawal confirmation** — multi-step confirmation dialog (anti-impulsive UX)
- **Idempotency** — all financial operations are safe to retry

---

## Getting Started

### Backend Prerequisites
- JDK 21+
- PostgreSQL 15+
- Gradle 8+

```bash
cd backend
# Configure database
export DATABASE_URL=jdbc:postgresql://localhost:5432/calmvest
export DATABASE_USER=calmvest
export DATABASE_PASSWORD=calmvest

./gradlew :app:bootRun
```

### Android Prerequisites
- Android Studio Hedgehog or newer
- Android SDK 34

```bash
cd android
./gradlew assembleDebug
```

---

## Technical Stack

### Backend
- Kotlin 1.9 + JVM 21
- Spring Boot 3.x
- PostgreSQL 15
- Flyway (schema migrations)
- Spring Data JPA + Hibernate

### Android
- Kotlin 1.9
- Jetpack Compose + Material 3
- Hilt (DI)
- Retrofit + Moshi (API client)
- Coroutines + StateFlow
- Navigation Compose

---

## Coding Conventions

- Use `data class` with immutable `val` fields everywhere
- Never use `Float` or `Double` for monetary values — use `Money` (Long minor units)
- All public APIs return `Result<T>` or throw typed domain exceptions
- Repository implementations in `infrastructure`, interfaces in `domain`
- Annotate all financial service methods with `@Transactional`
- Use idempotency keys (UUID) for all write operations on financial entities
- Compose screens receive only stable state objects from ViewModel

---

## Testing Strategy

- **Domain**: Unit tests for `Money` arithmetic, `RoundUpRule` logic, `ReserveEntry` accumulation
- **Application**: Service tests with mocked repositories (MockK)
- **API**: MockMvc integration tests for controller request/response validation
- **Infrastructure**: `@DataJpaTest` slice tests for repository queries
- **Android ViewModels**: `TestCoroutineDispatcher` + `StateFlow` collection tests
- **Compose Screens**: Compose UI tests with `createComposeRule()` for key flows
