# Project MIRROR — Audio Guide

> **v0.2** — Chapter 1 오디오 브리프 확정
> 유지: `prompts/sound_director.md` | 감정: `prompts/emotion_designer.md`

---

## 사운드 철학

**침묵도 연출이다.** 슬픈 음악 ≠ 슬픔. 비 ≠ 우울.

---

## Chapter 1 장면별 오디오

### Prologue

| 씬 | BGM | SFX | 침묵 | dB 참고 |
|----|-----|-----|------|---------|
| P-01 어둠 | **없음** | — | 전체 | -60 LUFS 느낌 |
| P-02 문 | 없음 | 손잡이 금속(따뜻 톤 vs 차가운 톤) | 문 열기 전 2초 | SFX -24 |
| P-03 빛 | 없음 | 문 hinges 무소음 | — | |
| P-04 실루엣 | 없음 | — | **5초** | 핵심 |
| P-05 달 | 없음 | 먼 풍 ambient | 1초 | |
| P-06 홀 | 없음 | 발걸음 석조+물 | 페이드 인 | |

---

### LOC-001 기억의 홀

| 항목 | 스펙 |
|------|------|
| BGM | **없음** |
| Ambient | 저주파 hum 40Hz (거의 무의식) |
| SFX | 발걸음(석조), 물방울(랜덤 8~15초), 거울 미세 진동(선택 후) |
| 금지 | 신비로운 choir, 하프 |

---

### LOC-002 이름의 서재

| 항목 | 스펙 |
|------|------|
| BGM | 없음 |
| SFX | 종이 넘김, 깃펜 scratch, 창틀 바람 |
| 린 대화 | Voice 없음 — 텍스트만 (향후 VO 옵션) |

---

### LOC-003 닫힌 창가

| 항목 | 스펙 |
|------|------|
| BGM | 없음 |
| SFX | 새 지저귐 **멀리** (L/R 페이드), 의자 삐걱 1회 |
| 감정 | 답답함이 아닌 **고요** |

---

### LOC-004 금이 간 홀 ⭐

| 항목 | 스펙 |
|------|------|
| BGM | **절대 없음** |
| SFX | CHO-A: 유리 금 미세 crack / CHO-B: 발걸음만 / CHO-C: 거울 low tone 1회 |
| 침묵 | 선택 전 **3초**, 선택 후 **2초** |
| sound_director | "여기서 음악 넣지 마세요" |

---

### Chapter 1 종료

| 항목 | 스펙 |
|------|------|
| BGM | 없음 |
| SFX | 문 열림(나무), 창밖 wind 1초 어두워짐 |
| 예고 | Ch2 빗소리 0.5초 fade in → cut |

---

## 성향 반영 (Ch1)

| 성향 | 오디오 변화 |
|------|-------------|
| 공감↑ | NPC 대사 후 reverb tail +0.1s |
| 의심↑ | 거울 SFX 0.3s delay |
| 용기↑ | crack SFX 날카롭지 않게 유지 (두려움 유발 X) |
| 호기심↑ | ambient 고역 +5% |

**UI에 표시 없음.**

---

## 효과음 라이브러리 (Ch1)

| ID | 설명 | 파일명 |
|----|------|--------|
| SFX_DOOR_WARM | 따뜻한 손잡이 | door_handle_warm.wav |
| SFX_DOOR_COLD | 차가운 손잡이 | door_handle_cold.wav |
| SFX_STEP_STONE | 석조 발걸음 | step_stone_*.wav |
| SFX_WATER_DROP | 물방울 | water_drop_*.wav |
| SFX_MIRROR_CRACK | 금 | mirror_crack_soft.wav |
| SFX_BIRD_DIST | 먼 새 | bird_distant.wav |
| SFX_PAPER | 종이 | paper_turn.wav |
| SFX_SILENCE | — | (재생 안 함) |

---

## BGM 레이어 (중반 이후 예비)

Ch1에는 **BGM 트랙 미사용**. Ch3부터 레이어 방식 도입:

- Layer 0: silence
- Layer 1: pad (호기심↑)
- Layer 2: single note motif (희생↑)

---

## 접근성

- 모든 SFX에 자막 대체: `[물방울 소리]` `[먼 새소리]`
- 침묵 구간: 시각적 펄스 없음 (공포 유발 X)

---

## 에셋 체크리스트 (Ch1)

- [ ] SFX 8종 위 표
- [ ] Ambient hall_hum.loop
- [ ] Mix: 전체 -18 LUFS target, dynamic range 유지

---

## 연결

- [ArtGuide.md](./ArtGuide.md) · [PrologueScript.md](./PrologueScript.md)
