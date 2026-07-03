# Schwimmen 🏊🃏

[![Scala CI](https://github.com/maxdoebele/Schwimmen/actions/workflows/scala.yml/badge.svg)](https://github.com/maxdoebele/Schwimmen/actions/workflows/scala.yml)
[![Coverage Status](https://coveralls.io/repos/github/maxdoebele/Schwimmen/badge.svg?branch=main)](https://coveralls.io/github/maxdoebele/Schwimmen?branch=main)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A digital version of the classic German card game **Schwimmen** (also known as *31*, *Knack* or *Schnauz*), written in **Scala 3** with both a **text-based interface (TUI)** and a **graphical interface (GUI)** built with ScalaFX.

Developed as a Software Engineering project at HTWG Konstanz.

## Game Rules

Schwimmen is played with a 32-card deck (7 through Ace). Each player holds exactly **three cards** and tries to collect the highest score possible in one suit.

**Card values**

| Card | Points |
|------|--------|
| 7 – 10 | Face value |
| Jack, Queen, King | 10 |
| Ace | 11 |

**Scoring**

- Only cards of the **same suit** are added together — the best possible hand is **31** (Ace + two ten-point cards)
- Three cards of the **same rank** count as **30.5**
- Three **Aces** are *Feuer* (33) and immediately end the round

**On your turn you can**

1. **Knock** (*Klopfen*) — signal the final round; every other player gets one last turn (not allowed in the very first round)
2. **Skip** (*Schieben*) — pass without trading; if all players skip, the table cards are replaced
3. **Trade** (*Tauschen*) — swap **one** card or **all three** cards with the table

**Lives**

- Each player starts with 3 lives; the player(s) with the lowest score at the end of a round lose one life
- A player who loses their last life starts *swimming* (**Schwimmer**) — one extra life
- Losing while swimming means you're out; the last player standing wins

## Features

- 🖥️ **TUI and GUI running in parallel** — both views observe the same game state and stay in sync
- ↩️ **Undo / Redo** for every move via the Command pattern
- 💾 **Save & load games** in three interchangeable formats: **XML**, **JSON** and **YAML**
- 👥 **2–9 players** per game
- ✅ Extensive **unit tests** (ScalaTest + Mockito) with coverage tracking via scoverage/Coveralls
- 🐳 **Docker support**

## Architecture

The project follows the **Model-View-Controller** pattern and makes use of several classic design patterns:

| Pattern | Where |
|---------|-------|
| Observer | `util/Observable` — TUI & GUI subscribe to controller updates |
| Command (+ Undo/Redo) | `Controller/Command` — Knock, Skip, TradeOne, TradeAll |
| Chain of Responsibility | `Controller/COR` — life points, swimmer and potential-swimmer handling at round end |
| Builder | `Controller/GameBuilder` — constructing new games and rounds |
| Dependency Injection | Google Guice (`Controller/DependencyInjection/GameModule`) |
| Strategy / Interface-based FileIO | `FileIO` — XML, JSON and YAML implementations behind one trait |

```
src/main/scala
├── Main.scala                  # Entry point: wires everything via Guice, starts TUI + GUI
├── Model/                      # Immutable game state: GameState, User, Card, CardDeck
├── Controller/
│   ├── Controller.scala        # Central game logic, command execution, undo manager
│   ├── Command/                # Player actions as undoable commands
│   ├── COR/                    # Round-end evaluation chain
│   ├── GameBuilder/            # New game / new round construction
│   └── DependencyInjection/    # Guice module
├── View/
│   ├── tui/                    # Text interface (async input handling)
│   └── gui/                    # ScalaFX interface with card graphics
├── FileIO/                     # Persistence (XML / JSON / YAML)
└── util/                       # Observable, Observer, UndoManager
```

## Getting Started

### Prerequisites

- **JDK 17**
- **sbt** (Scala 3.5.0 is pulled automatically)

### Run

```bash
git clone https://github.com/maxdoebele/Schwimmen.git
cd Schwimmen
sbt run
```

The game starts both the TUI (in your terminal) and the GUI window. Enter the player names comma-separated (e.g. `Anna, Max`) and play in whichever view you prefer.

### Run with Docker

```bash
docker build -t schwimmen .
docker run -it -e DISPLAY=host.docker.internal:0.0 schwimmen
```

> The GUI requires an X server on the host (e.g. XQuartz on macOS, VcXsrv on Windows).

### Tests & Coverage

```bash
sbt test                          # run the test suite
sbt clean coverage test           # run tests with coverage instrumentation
sbt coverageReport                # generate the coverage report
```

## Tech Stack

- [Scala 3.5](https://www.scala-lang.org/) & [sbt](https://www.scala-sbt.org/)
- [ScalaFX / JavaFX](https://www.scalafx.org/) — GUI
- [Google Guice](https://github.com/google/guice) — dependency injection
- [Play JSON](https://github.com/playframework/play-json), [scala-xml](https://github.com/scala/scala-xml), [Jackson YAML](https://github.com/FasterXML/jackson-dataformats-text) — persistence
- [ScalaTest](https://www.scalatest.org/) & [Mockito](https://site.mockito.org/) — testing
- GitHub Actions CI + [Coveralls](https://coveralls.io/github/maxdoebele/Schwimmen)

## License

This project is licensed under the [MIT License](LICENSE).
