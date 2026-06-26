# Project MIRROR — Game Design Document

> 작성·유지: `prompts/gameplay_designer.md` | 검토: `prompts/creative_director.md`, `prompts/meaning_guardian.md`
> 상태: **스캐폴드** — 상세 설계는 AI 프롬프트로 점진적 작성

---

## 프로젝트 개요

| 항목 | 내용 |
|------|------|
| 프로젝트명 | Project MIRROR |
| 장르 | 철학 · 심리 · 선택 기반 내러티브 · 어드벤처 |
| 플랫폼 | Android, PC (확장 가능) |
| 목표 플레이타임 | TBD |
| 핵심 철학 | [Philosophy.md](./Philosophy.md) |

---

## 핵심 게임 루프 (초안)

```
플레이어 행동 → 선택 → 세계 변화 → 거울 변화 → 플레이어 감정 → 다음 선택
```

---

## 설계 항목 체크리스트

| 항목 | 상태 | 담당 프롬프트 |
|------|------|---------------|
| 핵심 게임 루프 | 초안 | gameplay_designer |
| 플레이 흐름 | 미작성 | gameplay_designer |
| 진행 방식 | 미작성 | gameplay_designer |
| 저장 방식 | 미작성 | technical_director |
| UI 컨셉 | 미작성 | art_director |
| 선택 시스템 | 미작성 | gameplay_designer |
| 숨겨진 성향 시스템 | 미작성 | gameplay_designer |
| 플레이어 경험 | 미작성 | emotion_designer |
| 반복 플레이 요소 | 미작성 | gameplay_designer |
| 엔딩 구조 | 미작성 | narrative_director |
| 게임 규칙 | 미작성 | gameplay_designer |
| 접근성 | 미작성 | gameplay_designer |
| 분위기 | 초안 | creative_director |

---

## 선택 시스템 원칙

- 선택을 **맞추는** 게임이 아님
- 숫자·성향 수치 **노출 없음**
- 선택 → 세계·NPC·거울·음악·대사의 **미세한 변화**
- 게임은 변화의 **이유를 설명하지 않음**

### 숨겨진 성향 (초안)

공감 · 호기심 · 질서 · 자유 · 희생 · 자기중심성 · 의심 · 용기

> 각 성향의 반영 방식은 `prompts/gameplay_designer.md`로 상세 설계

---

## 엔딩 철학

- 좋은 엔딩 / 나쁜 엔딩 **없음**
- 플레이어 **가치관을 반영**한 엔딩
- 반복 플레이 전용 장면 존재 가능

---

## UI 원칙

플레이어가 숫자를 보지 않아도 변화를 느끼도록:

- 색감 · 음악 · 효과음 · 연출 · 카메라 · 대사 · 조명

---

## 개발 원칙

```
기획 → 스토리 → 시스템 → UI → 프로토타입 → 구현
```

기획이 완성되기 전에는 코드 작성을 우선하지 않습니다.

---

## 향후 확장 가능성

- TBD

---

## 연결 문서

- [StoryBible.md](./StoryBible.md)
- [WorldBible.md](./WorldBible.md)
- [ArtGuide.md](./ArtGuide.md)
- [AudioGuide.md](./AudioGuide.md)
