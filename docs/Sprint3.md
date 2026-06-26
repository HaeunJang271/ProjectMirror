# Sprint 3 — Ch2 플레이 · 연출

> **상태: ✅ 핵심 완료**

---

## 완료

- [x] p01 씬 전환 버그 수정 (`goToScene` + 탭 레이어)
- [x] Chapter 2 JSON (`assets/narrative/ch02/dialogue_trees.json`)
- [x] Ch2 선택지 11종 `choices.json` 인덱스
- [x] **Ch1→Ch2 조건 분기** — `requireFlag` 엔진 + crack/rin/feather 변형 대사
- [x] **Journal 화면** — 대화 로그 (성향 비표시), 우상단 「일기」 진입
- [x] **Ambient 색온도** — 챕터별 기본값 + 선택 성향 반영, 0.5초 트랜지션

---

## 구현 요약

### 조건 분기 (`requireFlag`)

```json
{
  "type": "narration",
  "text": "벽을 따라 금색 얇은 선이 이어진다.",
  "requireFlag": { "key": "f001_crack_state", "equals": "spread" }
}
```

| 플래그 | Ch2 변형 |
|--------|----------|
| `f001_crack_state=spread` | 금색 얇은 선 |
| `f001_crack_state=intact` | 거울 증가 |
| `f001_crack_state=light_line` | 창틈 새 소리 |
| `rin_recorded=true` | 이묵 린 대사 |
| `f005_feather_found=true` | 하린 깃털 대사 |

### Journal

- Room `dialogue_log` 테이블
- 대사·선택 자동 기록
- 새 게임 시 초기화

### Ambient

- Prologue: 약간 따뜻 (+0.08)
- Ch1: 중립 (0)
- Ch2: 차가움 (-0.18, 비)
- 선택 성향에 따라 ±0.04 미세 변화

---

## Ch2 씬 구조

| 씬 ID | 이벤트 | 유형 |
|--------|--------|------|
| c02_intro | EVT-C02-01 비의 복도 | narrative |
| c02_segyeol | EVT-C02-02 세결 | dialogue |
| c02_imuk | EVT-C02-03 이묵 · CHO-FROST | dialogue + choice |
| c02_harin | EVT-C02-04 하린 · CHO-HARIN | dialogue + choice |
| c02_so | EVT-C02-05 소 | dialogue + choice |
| c02_gap | EVT-C02-06 잊힌 틈 · CHO-GAP | narrative + choice |
| c02_end | EVT-C02-07 | chapterComplete |

---

## 연결

- [Chapter2Script.md](./Chapter2Script.md)
- [QuestBible.md](./QuestBible.md)
- [UIFlow.md](./UIFlow.md)
- [Sprint2.md](./Sprint2.md)
