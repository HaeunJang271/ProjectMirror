# Project MIRROR — Tech Spec

> Android 기술 설계. 유지: `prompts/technical_director.md`
> **v0.1** — Sprint 2 착수용

---

## 개요

| 항목 | 내용 |
|------|------|
| 플랫폼 | Android 8.0+ (API 26+) |
| 언어 | Kotlin |
| UI | Jetpack Compose |
| 아키텍처 | MVVM + Clean Architecture (경량) |
| DI | Hilt |
| 비동기 | Coroutines + Flow |

---

## 모듈 구조 (권장)

```
ProjectMirror/
├── app/                    # Application, NavHost, Theme
├── core/
│   ├── common/             # Result, Dispatcher, 확장
│   ├── database/           # Room
│   └── datastore/          # DataStore
├── domain/
│   ├── model/              # PlayerState, Choice, Chapter
│   └── repository/         # 인터페이스
├── data/
│   ├── repository/         # 구현체
│   ├── local/              # DAO, Entity
│   └── content/            # JSON/번들 내러티브 에셋
└── feature/
    ├── prologue/
    ├── chapter/
    ├── dialogue/
    └── settings/
```

---

## 아키텍처

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Compose UI │ ──▶ │  ViewModel   │ ──▶ │  UseCase     │
│   Screen     │ ◀── │  StateFlow   │ ◀── │  (선택적)    │
└──────────────┘     └──────┬───────┘     └──────┬───────┘
                            │                    │
                            ▼                    ▼
                     ┌──────────────┐     ┌──────────────┐
                     │ Repository   │ ──▶ │ Room / Store │
                     └──────────────┘     └──────────────┘
```

### 레이어 규칙

| 레이어 | 책임 | 금지 |
|--------|------|------|
| UI | 표시, 입력, 연출 트리거 | 성향 수치 표시 |
| ViewModel | UI State, 이벤트 | Room 직접 접근 |
| Domain | 비즈니스 규칙 | Android 의존 |
| Data | 영속화, 에셋 로드 | Compose 의존 |

---

## Navigation

```kotlin
// 개념적 라우트
sealed class Route(val route: String) {
    data object Title : Route("title")
    data object Prologue : Route("prologue/{sceneId}")
    data object Chapter : Route("chapter/{chapterId}")
    data object Scene : Route("scene/{sceneId}")
    data object Dialogue : Route("dialogue/{nodeId}")
    data object Journal : Route("journal")
    data object Settings : Route("settings")
    data object SaveLoad : Route("save/{slot}")
}
```

- **Navigation Compose** + 단일 Activity
- 딥링크: 챕터/씬 ID로 디버그 점프 (debug 빌드만)

---

## 저장 구조

### 1. DataStore — 메타·성향·회차 (UI 비노출)

```kotlin
// PlayerMetaPreferences
playthrough_count: Int
current_chapter: String          // "prologue" | "ch01" ...
current_scene: String
last_auto_save_at: Long

// DispositionWeights (내부만)
empathy: Int        // -100..100
curiosity: Int
order: Int
freedom: Int
sacrifice: Int
self_centered: Int
doubt: Int
courage: Int
```

### 2. Room — 슬롯 저장·선택 이력

```kotlin
@Entity(tableName = "save_slots")
data class SaveSlotEntity(
    @PrimaryKey val slot: Int,           // 1..3
    val chapterId: String,
    val sceneId: String,
    val snapshotJson: String,          // WorldFlags 스냅샷
    val updatedAt: Long
)

@Entity(tableName = "choice_log")
data class ChoiceLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val choiceId: String,              // e.g. "CH01_CRACK_A"
    val chapterId: String,
    val timestamp: Long,
    val slot: Int?
)

@Entity(tableName = "foreshadow_flags")
data class ForeshadowEntity(
    @PrimaryKey val flagId: String,     // "F-001"
    val state: String,                 // "planted" | "variant_a" | "resolved"
    val metadata: String?              // JSON: crack_spread=true 등
)
```

### 3. WorldFlags (snapshotJson 내부)

```json
{
  "f001_crack_state": "spread|intact|light_line",
  "f002_door_handle": "warm|cold",
  "f003_silhouette_delay_ms": 500,
  "f004_rin_name_filled": false,
  "f005_feather_found": false,
  "npc_flags": {
    "rin_trust": true,
    "gaon_window": "closed"
  },
  "ambient": {
    "color_temp_shift": 0.005
  }
}
```

### 저장 시점

| 트리거 | 저장 종류 |
|--------|-----------|
| 챕터 시작 | 자동 (슬롯 0 = 오토) |
| 핵심 선택 직후 | 자동 + ChoiceLog |
| 메뉴 수동 | 슬롯 1~3 |
| 앱 백그라운드 | 자동 (씬 단위) |

---

## 내러티브 데이터

초기: **JSON 번들** (`assets/narrative/`)

```
assets/narrative/
├── prologue/
│   └── scenes.json
├── ch01/
│   ├── scenes.json
│   └── dialogue_trees.json
└── choices.json
```

```json
{
  "id": "CH01_CRACK_MIRROR",
  "type": "explicit",
  "options": [
    {
      "id": "A",
      "label": "손을 댄다",
      "disposition_delta": { "courage": 6, "curiosity": 4 },
      "foreshadow": { "F-001": "spread" }
    }
  ]
}
```

추후: Remote Config 또는 DLC 패치 고려.

---

## UI 기술 (Compose)

| 화면 | Composable | 비고 |
|------|------------|------|
| 타이틀 | `TitleScreen` | 신규/이어하기 |
| 내러티브 | `NarrativeScreen` | 풀스크린 텍스트+배경 |
| 대화 | `DialogueScreen` | 이름+본문+선택지 |
| 탐색 | `ExplorationScreen` | 핫스팟 탭 |
| 일기 | `JournalScreen` | ChoiceLog + 대사 로그 |
| 설정 | `SettingsScreen` | 자막, 저장 |

### 연출 파라미터 (성향 반영)

```kotlin
data class AmbientState(
    val colorTemperature: Float,  // -1f..1f
    val vignetteAlpha: Float,
    val mirrorDelayMs: Int,
    val bgmLayerMask: Int
)
```

ViewModel이 `DispositionRepository`에서 읽어 UI에 전달. **숫자 UI 없음.**

---

## 의존성 (Gradle 개념)

```kotlin
// Compose BOM, Navigation, Hilt, Room, DataStore
implementation("androidx.compose.material3:material3")
implementation("androidx.navigation:navigation-compose")
implementation("androidx.room:room-ktx")
implementation("androidx.datastore:datastore-preferences")
implementation("com.google.dagger:hilt-android")
```

---

## 테스트

| 대상 | 방식 |
|------|------|
| ChoiceLog 기록 | Unit |
| Foreshadow 상태 전이 | Unit |
| 성향 delta 누적 | Unit |
| UI 스냅샷 | Compose Test (선택 화면) |

---

## 보안·프라이버시

- 전부 로컬 저장. 계정·서버 없음
- Analytics: opt-in만 (추후)

---

## 구현 순서 (Sprint 2+)

1. 프로젝트 스캐폴드 + Hilt + Nav
2. DataStore + Room 스키마
3. Prologue JSON + DialogueScreen
4. Choice 적용 + Foreshadow
5. Chapter 1 씬 연결
6. Ambient 연출 훅

---

## 연결

- [GDD.md](./GDD.md) · [UIFlow.md](./UIFlow.md) · [QuestBible.md](./QuestBible.md)
