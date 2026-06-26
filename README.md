# 🪞 Project MIRROR

**철학적 내러티브 게임** — 정답을 주지 않고, 스스로 **질문**하게 만드는 경험.

Android 1차 · PC 확장 · 장기 운영형 인디 프로젝트.

---

## 한 줄 소개

거울성에서 깨어난 당신. 기억은 없고, 선택만 남는다.  
게임은 판단하지 않는다. 조용히 세계를 바꿀 뿐이다.

---

## 핵심 철학

| 원칙 | 설명 |
|------|------|
| 정답 없음 | 질문만 던짐 |
| 선악 없음 | 성향·점수 UI 없음 |
| 거울의 규칙 | 거짓말 안 함 · 전부 말 안 함 · 질문 없이 말 안 함 |
| 분위기 | 조용함 · 고독 · 사색 · 따뜻함 · 약간의 불안 · 희망 |

→ [docs/Philosophy.md](docs/Philosophy.md)

---

## 개발 원칙

```
기획 → 스토리 → 시스템 → UI → 프로토타입 → 구현
```

1. **기획 완료 전 코드 우선 금지**
2. **docs/가 단일 진실 공급원** — 설정은 문서 먼저
3. **AI 역할 분리** — Creative Director는 코드 안 씀
4. **의미 없는 콘텐츠 금지** — Meaning Guardian 검증
5. **장기 확장** — 챕터·복선·회차 구조 전제

---

## 문서 인덱스

### 세계관 & 내러티브

| 문서 | 용도 |
|------|------|
| [Philosophy.md](docs/Philosophy.md) | 핵심 철학, 질문 풀 |
| [Glossary.md](docs/Glossary.md) | 고유 용어 (거울, 반영, 관리인 등) |
| [StoryBible.md](docs/StoryBible.md) | 스토리·챕터·플레이어 |
| [WorldBible.md](docs/WorldBible.md) | 거울성, 장소, 복선 |
| [Characters.md](docs/Characters.md) | NPC 상세 |
| [SymbolDictionary.md](docs/SymbolDictionary.md) | 상징 10종 |
| [Timeline.md](docs/Timeline.md) | 연대표, 챕터 타임라인 |
| [DialogueGuide.md](docs/DialogueGuide.md) | 대사 톤·금지 표현 |
| [PrologueScript.md](docs/PrologueScript.md) | Prologue 대사 초안 |
| [Chapter2Script.md](docs/Chapter2Script.md) | Chapter 2 대사 초안 |

### 게임 설계

| 문서 | 용도 |
|------|------|
| [GDD.md](docs/GDD.md) | 게임 루프, 성향, 엔딩 |
| [QuestBible.md](docs/QuestBible.md) | 챕터별 이벤트·선택지 |
| [UIFlow.md](docs/UIFlow.md) | 화면 흐름, UX |
| [TechSpec.md](docs/TechSpec.md) | Android, 저장, 아키텍처 |

### 아트 & 사운드

| 문서 | 용도 |
|------|------|
| [ArtGuide.md](docs/ArtGuide.md) | 미술 방향, UI |
| [AudioGuide.md](docs/AudioGuide.md) | BGM, 침묵 연출 |

### 프로젝트 관리

| 문서 | 용도 |
|------|------|
| [Sprint1.md](docs/Sprint1.md) | Sprint 1 (완료) |
| [Sprint2.md](docs/Sprint2.md) | Sprint 2 진행 |
| [Sprint1Review.md](docs/Sprint1Review.md) | Sprint 1 검토 |

### Android

| 경로 | 용도 |
|------|------|
| [android/](android/) | Kotlin 소스 · Gradle |
| [android/README.md](android/README.md) | 빌드·구조 |

### AI 프롬프트 (`prompts/`)

16개 역할 — [조직도·사용법](#mirror-studio) 참조

---

## MIRROR Studio

```
        Creative Director
               │
    ┌──────────┼──────────┐
    │          │          │
  Story     System      Art
    │          │          │
    ├──────────┼──────────┤
    │          │          │
  Sound     World   Programming
    │          │          │
    └──────────┼──────────┘
              QA
```

### Cursor 사용법

```
@prompts/narrative_director.md @docs/StoryBible.md @docs/Glossary.md
"Chapter 3 개요를 작성해줘"
```

### 워크플로

```
작성 → meaning_guardian → world_bible_keeper → lore_archivist → qa_designer
```

---

## 현재 진행

| 항목 | 상태 |
|------|------|
| Sprint 1 | ✅ 완료 ([검토](docs/Sprint1Review.md)) |
| Prologue 대사 | ✅ [PrologueScript.md](docs/PrologueScript.md) |
| Chapter 2 대사 | ✅ [Chapter2Script.md](docs/Chapter2Script.md) |
| Ch1 아트/오디오 | ✅ [ArtGuide](docs/ArtGuide.md) · [AudioGuide](docs/AudioGuide.md) v0.2 |
| Android 스캐폴드 | ✅ [android/](android/) |
| **다음** | Ch1 JSON · Choice 연동 · Sprint 2 마무리 |

---

## 기술 스택 (요약)

Jetpack Compose · Navigation · Room · DataStore · MVVM · Hilt

→ [docs/TechSpec.md](docs/TechSpec.md)

---

## 폴더 구조

```
ProjectMirror/
├── README.md
├── docs/          # 기획·설계 문서
├── android/       # Android 앱 (Compose)
└── prompts/       # AI 역할 프롬프트
```

---

## 라이선스 & 기여

개인 프로젝트. 기여 가이드는 추후 추가.
