# 🧩 Advanced Sudoku Solver & GUI in Java

A complete 9x9 Sudoku game and algorithmic solver developed from scratch in **Java (Swing)**. This project combines a modern desktop interface with rigorous computer science algorithms for maze/matrix generation, backtracking, and uniqueness validation.

## 🚀 Key Technical Features
- **Backtracking Algorithm Solver (`BackTrackingSolverSudoku`):** Implements recursive depth-first search combined with randomized number ordering (`Collections.shuffle`) to generate unique, freshly randomized valid Sudoku solutions on the fly.
- **Uniqueness Validation (`ContarSolucion`):** Ensures that generated puzzles have strictly **one unique solution** by counting valid configuration branches before removing cells based on difficulty level.
- **Rule Engine Validation (`VerificarCasilla`):** Built-in matrix rule-checking verifying rows, columns, and sub-grids ($3 \times 3$ blocks) simultaneously.
- **Dynamic View Management:** Uses `CardLayout` to seamlessly switch between the main menu and the active game panel within a single window frame.
- **Interactive UI & Custom Borders:** Built using `JButton` matrices with custom matte borders (`BorderFactory.createMatteBorder`) to cleanly delineate $3 \times 3$ sub-grids, coupled with contextual pop-up menus (`JPopupMenu`) for rapid digit selection.
- **Progressive Hint System:** Integrated adaptive hint allocation based on chosen difficulty tiers (Easy, Medium, Hard).

## 🛠️ Tech Stack
- **Language:** Java
- **UI & Layouts:** Java Swing (`JFrame`, `JPanel`, `JButton`, `JComboBox`, `JPopupMenu`, `CardLayout`)
- **Data Structures:** 2D Arrays (`int[][]`, `boolean[][]`), `ArrayList`, Random Collection Shuffling.
- **Core Concepts:** Object-Oriented Programming (OOP), Recursion, Backtracking, Event-Driven Architecture.

## 🎮 How to Run
1. Clone the repository:
   ```bash
   git clone [https://github.com/RediSlayer/sudoku-java.git](https://github.com/RediSlayer/sudoku-java.git)
2. Compile and run the main class
   javac Sudoku.java
   java Sudoku
