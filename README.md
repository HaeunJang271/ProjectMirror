# 🪞 Project MIRROR — MIRROR Studio

철학적 내러티브 게임 **Project MIRROR**의 AI 크리에이티브 스튜디오입니다.

플레이어에게 정답을 주지 않고, **스스로 질문하게 만드는** 게임을 만듭니다.

---

## MIRROR Studio 조직도

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

---

## 폴더 구조

```
ProjectMirror/
├── docs/                    # 세계관 단일 진실 공급원
│   ├── Philosophy.md        # 핵심 철학
│   ├── GDD.md               # 게임 디자인 문서
│   ├── StoryBible.md        # 스토리 바이블
│   ├── WorldBible.md        # 세계관 바이블
│   ├── SymbolDictionary.md  # 상징 사전
│   ├── Timeline.md          # 연대표
│   ├── Characters.md        # 등장인물
│   ├── DialogueGuide.md     # 대사 가이드
│   ├── ArtGuide.md          # 아트 가이드
│   └── AudioGuide.md        # 오디오 가이드
│
└── prompts/                 # AI 역할 프롬프트
    ├── creative_director.md
    ├── narrative_director.md
    ├── philosophy_consultant.md
    ├── meaning_guardian.md      ⭐ 의미의 수호자
    ├── symbol_designer.md
    ├── emotion_designer.md
    ├── art_director.md
    ├── sound_director.md
    ├── gameplay_designer.md
    ├── world_bible_keeper.md
    ├── lore_archivist.md
    ├── qa_designer.md
    ├── indie_producer.md
    ├── technical_director.md
    ├── concept_artist.md
    └── playtester.md
```

---

## AI 역할 요약

| 역할 | 프롬프트 | 하는 일 |
|------|----------|---------|
| Creative Director | `creative_director.md` | "MIRROR다운가?" 최종 판단. 코드 금지 |
| Narrative Director | `narrative_director.md` | 스토리, NPC, 대사, 복선 |
| Philosophy Consultant | `philosophy_consultant.md` | 철학 질문·사고실험 설계 |
| **Meaning Guardian** | `meaning_guardian.md` | **"왜?"** — 의미 없으면 삭제/재설계 |
| Symbol Designer | `symbol_designer.md` | 거울·꽃·물 등 모든 상징 |
| Emotion Designer | `emotion_designer.md` | 플레이어 감정·여운 |
| Art Director | `art_director.md` | 미술·UI·색채 |
| Sound Director | `sound_director.md` | BGM·침묵·효과음 |
| Gameplay Designer | `gameplay_designer.md` | 퍼즐·선택·성향 시스템 |
| World Bible Keeper | `world_bible_keeper.md` | 설정 충돌 검토 |
| Lore Archivist | `lore_archivist.md` | 세계관 백과사전 |
| QA Designer | `qa_designer.md` | 스토리·설정 QA |
| Indie Producer | `indie_producer.md` | 스프린트·일정 |
| Technical Director | `technical_director.md` | Android 개발만 |
| Concept Artist | `concept_artist.md` | 콘셉트 아트 프롬프트 |
| Playtester | `playtester.md` | 페르소나별 플레이 피드백 |

---

## Cursor에서 사용법

1. 작업할 **역할의 프롬프트**를 `@prompts/xxx.md`로 첨부
2. 관련 **docs**를 함께 첨부 (`@docs/Philosophy.md` 등)
3. AI가 같은 세계관을 공유하는 팀원처럼 작업

### 예시 워크플로

```
새 NPC 아이디어
  → narrative_director (작성)
  → meaning_guardian ("왜 이 NPC?")
  → world_bible_keeper (설정 충돌)
  → lore_archivist (아카이브 등록)
  → qa_designer (모순 검사)
```

---

## 핵심 철학 (요약)

- 정답 없음 · 선악 없음 · 점수 없음
- 거울은 거짓말하지 않지만 모든 진실을 말하지도 않음
- 분위기: 조용함 · 고독 · 사색 · 따뜻함 · 약간의 불안감 · 희망

→ 상세: [docs/Philosophy.md](docs/Philosophy.md)

---

## 개발 원칙

```
기획 → 스토리 → 시스템 → UI → 프로토타입 → 구현
```

기획 완성 전 코드 우선 금지.

---

## 플랫폼

Android (1차) · PC (확장)

기술 스택: Jetpack Compose · Navigation · Room · DataStore · MVVM · Hilt

---

## 현재 진행 (Sprint 1)

| 항목 | 상태 |
|------|------|
| Sprint 1 계획 | ✅ [docs/Sprint1.md](docs/Sprint1.md) |
| Chapter 1 (첫 반사) | ✅ NPC 3명, 장소 4곳 |
| GDD 선택 시스템 | ✅ v0.2 |
| 상징 10종 | ✅ 확정 |
| 다음 | Sprint 1 검토 → Chapter 2 |
