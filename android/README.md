# Project MIRROR — Android

Kotlin · Jetpack Compose · MVVM · Hilt · Room · DataStore

→ 설계: [../docs/TechSpec.md](../docs/TechSpec.md) · UX: [../docs/UIFlow.md](../docs/UIFlow.md)

---

## 구조

```
app/src/main/java/com/projectmirror/
├── di/                 # Hilt
├── navigation/         # NavHost, Routes
├── domain/model/       # Disposition, WorldFlags, Narrative
├── data/
│   ├── local/          # Room
│   ├── datastore/      # 성향·챕터 (UI 비노출)
│   └── repository/
└── ui/
    ├── theme/
    └── screens/        # Title, Narrative, Dialogue, Prologue VM
```

`TechSpec`의 멀티모듈은 Sprint 3+에서 `:core`, `:domain` 분리 예정.

---

## 실행

```bash
cd android
./gradlew :app:assembleDebug
```

Android Studio: `android/` 폴더 Open.

---

## 현재 구현 (v0.1)

- [x] Hilt + Compose + Navigation
- [x] Room (save_slots, choice_log, foreshadow_flags)
- [x] DataStore (성향 8종)
- [x] Prologue JSON (`assets/narrative/prologue/scenes.json`)
- [x] Title → Prologue p01~p03 탭 진행
- [ ] Chapter 1 씬
- [ ] Choice 시스템 연동
- [ ] Journal / Settings

---

## 내러티브 에셋

`app/src/main/assets/narrative/` — [QuestBible.md](../docs/QuestBible.md) choice ID와 매핑.

---

## 연결

- [PrologueScript.md](../docs/PrologueScript.md)
- [Chapter2Script.md](../docs/Chapter2Script.md)
