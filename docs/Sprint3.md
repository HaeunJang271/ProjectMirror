# Sprint 3 — Ch2 플레이 · 연출

> **상태: 진행 중**

---

## 완료

- [x] p01 씬 전환 버그 수정 (`goToScene` + 탭 레이어)
- [x] Chapter 2 JSON (`assets/narrative/ch02/dialogue_trees.json`)
- [x] Ch2 선택지 11종 `choices.json` 인덱스

---

## 남은 작업

- [ ] Ch1→Ch2 분기 대사 (rin_recorded, f005, crack 상태)
- [ ] Prologue ~ Ch2 전체 플로우 QA
- [ ] Journal 화면
- [ ] Ambient 연출 (색온도)

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
- [Sprint2.md](./Sprint2.md)
