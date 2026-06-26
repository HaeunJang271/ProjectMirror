# Project MIRROR — Art Guide

> **v0.2** — Chapter 1 아트 브리프 확정
> 유지: `prompts/art_director.md` | 콘셉트: `prompts/concept_artist.md`

---

## 미술 방향

### 키워드

조용함 · 고독 · 사색 · 따뜻함 · 약간의 불안감 · 희망 · 몽환적

### 금지

- 과도한 화려함 · 판타지 클리셰 · 선악 색상 코딩 · HUD·성향 수치

---

## 색채 시스템

| 팔레트 | HEX (참고) | 용도 |
|--------|------------|------|
| Mirror Blue | `#3D4F5F` | 복도 기본, 천장 빛 |
| Stone Gray | `#5C5A57` | 석조 벽 |
| Warm Dust | `#8B7E6A` | 서재, 종이 |
| Question Blue | `#4A6B8A` | 파란 꽃 1점만 — 희망 아님 |
| Crack Gold | `#9A8B6E` | F-001 금 (저채도 금색) |
| Void Black | `#0D0D0F` | Prologue, 금이 간 홀 |

**성향 반영:** `color_temp_shift` ±0.05 — 공감↑ 따뜻 / 의심↑ 차가움 (체감 미세)

---

## 캐릭터 (Chapter 1)

| NPC | 실루엣 | 색 | 표정 |
|-----|--------|-----|------|
| 린 | 작은 책, 안경 | 회갈+검정 | 눈 피함 |
| 가온 | 의자에 웅크림 | 옅은 갈색 | 창만 봄 |
| 세결 | 도구 가방, 키 큼 | 어두운 옷 | 거울 회피 |

**스타일:** 반실사 2D 일러스트 또는 수채 저채도. 선명한 윤곽선 금지.

---

## Chapter 1 환경 브리프

### LOC-001 기억의 홀

| 항목 | 스펙 |
|------|------|
| 구도 | 1점 소실, 복도 깊이 |
| 벽 | 낡은 석조, 균열 미세 |
| 거울 | 양쪽 수천 개, 프레임 통일, 반사 흐림 |
| 바닥 | 얕은 웅덩이, 천장 빛 반사 |
| 조명 | 천장 틈 푸른 빛, 그림자 길게 |
| VFX | 물결 0.5Hz 미세 |

**콘셉트 프롬프트 (EN):**
```
Endless stone corridor, thousands of antique mirrors, soft blue light from ceiling cracks,
shallow water on floor reflecting blurred figure, dreamlike, low saturation, melancholic not sad,
Project MIRROR game background, no characters, cinematic
```

---

### LOC-002 이름의 서재

| 항목 | 스펙 |
|------|------|
| 크기 | 작은 방, 클로즈업 가능 |
| 소품 | 먼지 낀 책, 잉크병, 빈 칸 있는 명부 |
| 꽃 | 창틀 틈 파란 꽃 **1송이만** |
| 조명 | 창틀 가로줄 빛, 따뜻한 회갈 |

---

### LOC-003 닫힌 창가

| 항목 | 스펙 |
|------|------|
| 구성 | 의자 1, 닫힌 창 1, 미니멀 |
| 밖 | 흐릿한 하늘, 새 실루엣 없음 (소리만) |
| 소품 | 책상 아래 깃털 (F-005, 작게) |

---

### LOC-004 금이 간 홀

| 항목 | 스펙 |
|------|------|
| 중앙 | 대형 거울 1, 세로 금 1줄 |
| 조명 | 거울에서만 희미한 빛 — 주변 어둠 |
| UI | 선택지 시 화면 가장자리만 0.5% 밝기 변화 |

---

## UI 아트

| 요소 | 스펙 |
|------|------|
| 대화창 | 하단 30%, 반투명 `#1A1A1A` 85% |
| 폰트 | 명조/세리프 계열, 자막 M 16sp |
| 선택 버튼 | 테두리만, 채움 없음, 탭 시 미세 글로우 |
| 챕터 카드 | 중앙 정렬, 흰 텍스트, 배경 블러 |

---

## Prologue 비주얼

| 씬 | 비주얼 |
|----|--------|
| P-01~02 | 검정 95%, 문 윤곽만 |
| P-03~04 | 빛 틈, 실루엣 (플레이어와 지연) |
| P-05 | 달 원형 흐림 |
| P-06 | LOC-001 페이드 인 |

---

## 에셋 체크리스트 (Ch1)

- [ ] BG_LOC_001_hall.png (2048×1024)
- [ ] BG_LOC_002_study.png
- [ ] BG_LOC_003_window.png
- [ ] BG_LOC_004_crack.png
- [ ] CHAR_rin_portrait.png
- [ ] CHAR_gaon_portrait.png
- [ ] CHAR_segyeol_portrait.png
- [ ] UI_dialogue_panel.9.png
- [ ] FX_water_ripple sprite

---

## 연결

- [AudioGuide.md](./AudioGuide.md) · [SymbolDictionary.md](./SymbolDictionary.md)
