# Project MIRROR — Quest Bible

> 챕터별 이벤트·선택지·분기 관리. 유지: `prompts/narrative_director.md`, `prompts/gameplay_designer.md`
> **v0.2** — Prologue, Ch1, Ch2

---

## 관리 규칙

| 필드 | 설명 |
|------|------|
| `EVT-XXX` | 이벤트 ID |
| `CHO-XXX` | 명시적 선택 ID |
| `IMP-XXX` | 암시적 선택 ID |
| 성향 | `+키:값` 내부만, UI 비노출 |
| 복선 | F-XXX 상태 변경 |

---

## Prologue 「틈」

> 대사 전문: [PrologueScript.md](./PrologueScript.md)

| ID | 이벤트 | 씬 | 복선 |
|----|--------|-----|------|
| EVT-P01 | 어둠 속 각성 | P-01 | — |
| EVT-P02 | 문 발견 | P-02 | F-002 |
| EVT-P03 | 빛과 실루엣 | P-03 | — |
| EVT-P04 | 지연된 반영 | P-04 | F-003 |
| EVT-P05 | 달빛 | P-05 | — |
| EVT-P06 | 기억의 홀 진입 | P-06 | Ch1 시작 |

### 선택지

| ID | 유형 | 조건 | 옵션 | 성향 | 플래그 |
|----|------|------|------|------|--------|
| IMP-P02 | 암시적 | EVT-P02 | 5초 내 문 터치 | order+3 | f002=warm |
| IMP-P02b | 암시적 | EVT-P02 | 15초+ 대기 | doubt+4 | f002=cold |
| CHO-P02 | 명시적 | EVT-P02 | 문을 연다 / 조금 더 선다 | courage+3 / doubt+3 | — |

---

## Chapter 1 「첫 반사」

**허브:** LOC-001 | **질문:** Q-002, Q-006

### 이벤트 흐름

| 순서 | ID | 이벤트 | 장소 | 필수 |
|------|-----|--------|------|------|
| 1 | EVT-C01-01 | 홀 도착, 웅덩이 관찰 | LOC-001 | ✅ |
| 2 | EVT-C01-02 | 린 만남 | LOC-002 | ✅ |
| 3 | EVT-C01-03 | 가온 만남 | LOC-003 | ✅ |
| 4 | EVT-C01-04 | 세결 조우 | LOC-001~004 | ⬜ |
| 5 | EVT-C01-05 | 금 간 거울 | LOC-004 | ✅ |
| 6 | EVT-C01-06 | Ch2 문 개방 | LOC-001 | ✅ |

### EVT-C01-02 — 린

| ID | 유형 | 옵션 | 성향 | 플래그 |
|----|------|------|------|--------|
| CHO-C01-RIN-A | 명시적 | 이름을 적어준다 | sacrifice+5, empathy+4 | rin_recorded=true |
| CHO-C01-RIN-B | 명시적 | 이름을 말하지 않는다 | self+4, doubt+3 | rin_recorded=false |
| CHO-C01-RIN-C | 명시적 | 린의 빈 칸을 가리킨다 | curiosity+5 | f004=noticed |

### EVT-C01-03 — 가온

| ID | 유형 | 옵션 | 성향 | 플래그 |
|----|------|------|------|--------|
| CHO-C01-GAON-A | 명시적 | 창문을 열어줄까 묻는다 | empathy+4 | gaon_asked=true |
| CHO-C01-GAON-B | 명시적 | 조용히 앉아 함께 듣는다 | empathy+6 | gaon_silent=true |
| IMP-C01-FEATHER | 암시적 | 책상 아래 깃털 터치 | curiosity+4 | f005=found |

### EVT-C01-05 — 금 간 거울 ⭐

| ID | 유형 | 옵션 | 성향 | F-001 |
|----|------|------|------|-------|
| CHO-C01-CRACK-A | 명시적 | 손을 댄다 | courage+6, curiosity+4 | spread |
| CHO-C01-CRACK-B | 명시적 | 지나친다 | order+5, doubt+4 | intact |
| CHO-C01-CRACK-C | 명시적 | 거울을 등진다 | empathy+5, freedom+4 | light_line |

**선행:** EVT-C01-04 완료 시 세결 대사 변형 1줄 추가

### 챕터 종료 조건

- CHO-C01-CRACK 필수
- EVT-C01-02, 03 필수 (순서 자유)

---

## Chapter 2 「비의 자리」

**허브:** LOC-005 | **질문:** Q-001 — 기억이 사라져도 동일한 사람인가?
**플레이타임:** 40~50분

### 이벤트 흐름

| 순서 | ID | 이벤트 | 장소 | 필수 |
|------|-----|--------|------|------|
| 1 | EVT-C02-01 | 비 시작, 복도 진입 | LOC-005 | ✅ |
| 2 | EVT-C02-02 | 세결 재회 (창틀 수리) | LOC-005 | ✅ |
| 3 | EVT-C02-03 | 이묵 만남 — 서리 글씨 | LOC-006 | ✅ |
| 4 | EVT-C02-04 | 하린 만남 — 없는 기억 | LOC-005 | ✅ |
| 5 | EVT-C02-05 | 소와 조우 — 관리인? | LOC-005 | ⬜ |
| 6 | EVT-C02-06 | 잊힌 틈 — 핵심 선택 | LOC-007 | ✅ |
| 7 | EVT-C02-07 | Ch3 문 | LOC-005 | ✅ |

### EVT-C02-03 — 이묵 (서리)

| ID | 유형 | 옵션 | 성향 | 플래그 |
|----|------|------|------|--------|
| CHO-C02-FROST-A | 명시적 | 서리를 지운다 | order+5, self+3 | frost_wiped=true |
| CHO-C02-FROST-B | 명시적 | 그대로 둔다 | empathy+4, doubt+3 | frost_kept=true |
| CHO-C02-FROST-C | 명시적 | 자신의 이름을 적는다 | sacrifice+6 | frost_named=true, f006=planted |

### EVT-C02-04 — 하린

| ID | 유형 | 옵션 | 성향 | 플래그 |
|----|------|------|------|--------|
| CHO-C02-HARIN-A | 명시적 | 하린의 기억을 믿는다 | empathy+5 | harin_believed=true |
| CHO-C02-HARIN-B | 명시적 | "그건 내 기억이 아니야" | doubt+6, courage+3 | harin_denied=true |
| CHO-C02-HARIN-C | 명시적 | 대답하지 않는다 | order+4 | harin_silent=true |

### EVT-C02-06 — 잊힌 틈 ⭐

**철학:** Q-001 — 웅덩이에 얼굴이 비침 → 증발

| ID | 유형 | 옵션 | 성향 | F-006 |
|----|------|------|------|-------|
| CHO-C02-GAP-A | 명시적 | 웅덩이를 바라본다 | curiosity+5 | puddle_seen=true |
| CHO-C02-GAP-B | 명시적 | 눈을 감는다 | doubt+5 | puddle_refused=true |
| CHO-C02-GAP-C | 명시적 | 손을 담근다 | courage+6, empathy+3 | puddle_touched=true |

**연동:** CHO-C01-CRACK-A + CHO-C02-GAP-C → 복도 벽 금 패턴과 동일 (F-006 회수 Ch5)

### 챕터 종료 조건

- CHO-C02-FROST, CHO-C02-GAP 필수
- EVT-C02-03, 04 필수

---

## 분기 매트릭스 (Ch1→Ch2)

| Ch1 선택 | Ch2 변화 |
|----------|----------|
| CHO-C01-CRACK-A | 비의 복도 벽에 금색 얇은 선 |
| CHO-C01-CRACK-B | 복도 거울 더 많음 |
| CHO-C01-CRACK-C | 창문 틈 새 1마리 (소리만) |
| rin_recorded=true | 이묵이 "린에게서 들었어요" 대사 |
| f005=found | 하린이 깃털 언급 |

---

## 복선 퀘스트 추적

| ID | 심기 | 회수 | 퀘스트 연동 |
|----|------|------|-------------|
| F-001 | CHO-C01-CRACK | Ch7 | EVT-C01-05 |
| F-002 | IMP-P02 / CHO-P02 | Ch4 | EVT-P02 |
| F-003 | EVT-P04 | Ch7 | EVT-P04 |
| F-004 | CHO-C01-RIN-C | Ch4 | EVT-C01-02 |
| F-005 | IMP-C01-FEATHER | Ch3 | EVT-C01-03 |
| F-006 | CHO-C02-FROST-C | Ch5 | EVT-C02-06 |

---

## 연결

- [PrologueScript.md](./PrologueScript.md)
- [StoryBible.md](./StoryBible.md) · [GDD.md](./GDD.md)
- [TechSpec.md](./TechSpec.md) — choice ID 구현
