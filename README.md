# AI-Logic
The Obscured Sweeper game is inspired by the well-known Mineswepeer computer game. Similar to the classic Minesweeper, an Obscured Sweeper world is a grid of N × N cells, and M mines are scattered among the cells. However, some of the cells are “blocked”. A blocked cell is a cell not containing mines which does not give any clue to the number of neighbouring mines, and therefore cannot be probed.

This project is the creation of an autonomous logical agent that that is able to perceive facts about the world it occupies, and act accordingly using logic. Its goal is to uncover all cells but the mines - and therefore win the game.


**Running Instructions**
1. Navigate to the src directory included in the AI-Logic folder:
      cd AI-Search/src/
2. From within src, compile and run the program:
./playSweeper.sh <P1|P2|P3|P4|P5> <ID> [verbose] OR [inferences]
  
The first argument specified the logical agent to be used. The ID argument is used to specify which test to run. If the verbose flag is passed, then the output is printed whenever the agent makes an inference. Alternatively, if the inferences is used the number of inferences required is being recorded.

 Example:
Run the program using IntermediateAgent (P3), on the TEST4 map. Print the output after each inference (verbose).
  
./playSweeper.sh P3 TEST4 verbose
