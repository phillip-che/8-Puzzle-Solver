import java.util.*;

public class Project1 {

  private static int PUZZLE_SIZE = 3;
    
  public static void main(String[] args) {
    System.out.println("CS 4200 Project 1 - Phillip Che\n");

    Scanner input = new Scanner(System.in);
    int[][] puzzle = new int[PUZZLE_SIZE][PUZZLE_SIZE];
    System.out.println("Select Input Method: ");
    System.out.println("[1] Random");
    System.out.println("[2] Custom");
    String userInput = input.nextLine();

    boolean solveable = false;

    if (userInput.equals("1")) {
      while (!solveable) {
        puzzle = generatePuzzle();
        System.out.println("Puzzle:");
        printPuzzle(puzzle);
        solveable = isSolveable(puzzle);
        if (!solveable) {
          System.out.println("Puzzle is not solveable. Trying again...");
        }
      }
    } else if (userInput.equals("2")) {
      ArrayList<Integer> list = new ArrayList<>(9);
      puzzle = new int[PUZZLE_SIZE][PUZZLE_SIZE];

      for (int i = 0; i < 9; i++) {
        list.add(i);
      }
      while (!solveable) {
        int i = 0;
        while (!list.isEmpty()) {
          System.out.println("Enter an integer from: " + list.toString());
          userInput = input.nextLine();
          try {
            int num = Integer.parseInt(userInput);
            if (!list.contains(num)) {
              throw new Exception();
            }
            puzzle[i / PUZZLE_SIZE][i % PUZZLE_SIZE] = num;
            list.remove(Integer.valueOf(num));
            i++;
          } catch (Exception e) {
            System.out.println("Invalid input. Try again...");
          }
        }
        System.out.println("Puzzle:");
        printPuzzle(puzzle);
        solveable = isSolveable(puzzle);
        if (!solveable) {
          System.out.println("Puzzle is not solveable. Try again...");
        }
      }
    } 

    System.out.println("\nSelect H Function: ");
    System.out.println("[1] H1");
    System.out.println("[2] H2");
    System.out.println("[3] Both");
    userInput = input.nextLine();
    
    if (userInput.equals("1")) {
      final long startTime = System.currentTimeMillis();
      aStarSolve(puzzle, 1);
      final long endTime = System.currentTimeMillis();
      final long totalTime = endTime - startTime;
      System.out.println("Total Execution Time: " + totalTime + "ms");
    } else if (userInput.equals("2")) {
      aStarSolve(puzzle, 2);
    } else if (userInput.equals("3")) {
      aStarSolve(puzzle, 1);
      aStarSolve(puzzle, 2);
    } else {
      System.out.println("Invalid input. Exiting program...");
      System.exit(0);
    }

  }

  public static int aStarSolve(int[][] puzzle, int hFunction ) {
    int searchCost = 0;
    int gValue = 0;
    HashSet<String> explored = new HashSet<>();
    PriorityQueue<PuzzleState> frontier = new PriorityQueue<>((a,b) -> a.aStarValue-b.aStarValue);
    PuzzleState initial = new PuzzleState(puzzle, hFunction, gValue, null);
    frontier.add(initial);

    while (!frontier.isEmpty()) {
      PuzzleState curState = frontier.poll();


      if (curState.isGoal()) {
        printSolution(curState);
        break;
      }

      for (int i = 0; i < curState.neighbors.size(); i++) {
        int[][] neighborPuzzle = curState.neighbors.get(i);
        PuzzleState newState = new PuzzleState(neighborPuzzle, hFunction, gValue++, curState);
        if (!explored.contains(Arrays.deepToString(neighborPuzzle))) {
          frontier.add(newState);
        }
      }

      explored.add(Arrays.deepToString(curState.state));
    }
        
    return searchCost;
  }
  
  public static int[][] generatePuzzle() {
    int[][] puzzle = new int[PUZZLE_SIZE][PUZZLE_SIZE];

    ArrayList<Integer> list = new ArrayList<>(9);

    for (int i = 0; i < 9; i++){
        list.add(i);
    }

    for (int i = 0; i < PUZZLE_SIZE; i++) {
      for (int j = 0; j < PUZZLE_SIZE; j++) {
        puzzle[i][j] = list.remove((int) (Math.random() * list.size()));
      }
    }

    return puzzle;
  }

  public static void printPuzzle(int[][] puzzle) {
    for (int i = 0; i < PUZZLE_SIZE; i++) {
      System.out.println(Arrays.toString(puzzle[i]));
    }
  }

  public static void printSolution(PuzzleState goalState) {
      System.out.println("Solution Found");
      Stack<PuzzleState> solutionStack = new Stack<>();

      while (goalState != null) {
        solutionStack.push(goalState);
        goalState = goalState.parent;
      }

      solutionStack.pop();

      int steps = 1;
      while (!solutionStack.empty()) {
        goalState = solutionStack.pop();
        System.out.println("Step " + steps + ":");
        printPuzzle(goalState.state);
        steps++;
      }
  }
  
  public static int getInversions(int[][] puzzle) {
    int inversions = 0;
    ArrayList<Integer> list = new ArrayList<>(9);

    for (int i = 0; i < PUZZLE_SIZE; i++) {
      for (int j = 0; j < PUZZLE_SIZE; j++) {
        int num = puzzle[i][j];
        if (num != 0) {
          for(int k = 0; k < list.size(); k++) {
            if(num < list.get(k)) {
              inversions++;
            }
          }
          list.add(num);
        }
      }
    }

    return inversions;
  }

  public static boolean isSolveable(int[][] puzzle) {
    return getInversions(puzzle) % 2 == 0;
  }

}

