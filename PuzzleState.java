import java.util.*;

public class PuzzleState {
    public int[][] state;
    public int[] zeroPos = new int[2];
    public ArrayList<int[][]> neighbors;
    public PuzzleState parent;
    public int hFunction;
    public int hValue;
    public int gValue;
    public int aStarValue;

    private static int PUZZLE_SIZE = 3;
    private static int[][] goal = {
        { 0, 1, 2 },
        { 3, 4, 5 },
        { 6, 7, 8 }
    };
    
    public PuzzleState(int[][] state, int hFunction, int gValue, PuzzleState parent) {
      this.state = state;
      this.hFunction = hFunction;
      this.gValue = gValue;
      this.parent = parent;
      if (this.hFunction == 1) {
        this.hValue = getH1(state);
      } else {
        this.hValue = getH2(state);
      }
      this.aStarValue = this.hValue + this.gValue;
      this.zeroPos = findZero();
      this.neighbors = getNeighbors();
    }
    
    public boolean isGoal() {
      return Arrays.deepEquals(state, goal);
    }

    private int[] findZero() {
      int[] zeroPos = new int[2];

      for (int i = 0; i < PUZZLE_SIZE; i++) {
        for (int j = 0; j < PUZZLE_SIZE; j++) {
          if (state[i][j] == 0) {
            zeroPos[0] = i;
            zeroPos[1] = j;
            return zeroPos;
          }
        }
      }

      return zeroPos;
    }

    private int getH1(int[][] puzzle) {
      int misplacedTiles = 0;

      for (int i = 0; i < PUZZLE_SIZE; i++) {
        for (int j = 0; j < PUZZLE_SIZE; j++) {
          if (puzzle[i][j] != goal[i][j]) {
            misplacedTiles++;
          }
        }
      }

      return misplacedTiles;
    }

    private int getH2(int[][] puzzle) {
      int sum = 0;

      for (int i = 0; i < PUZZLE_SIZE; i++) {
        for (int j = 0; j < PUZZLE_SIZE; j++) {
          int num = puzzle[i][j];
          if (num != 0) {
            int goalX = num / PUZZLE_SIZE;
            int goalY = num % PUZZLE_SIZE;
            int xMoves = Math.abs(goalX - i);
            int yMoves = Math.abs(goalY - j);
            sum += xMoves + yMoves;
          }
        }
      }

      return sum;
    }
    
    private ArrayList<int[][]> getNeighbors() {
      ArrayList<int[][]> neighbors = new ArrayList<>();

      int[] pos = new int[2];

      if (zeroPos[0] + 1 < PUZZLE_SIZE) {
        pos[0] = zeroPos[0] + 1;
        pos[1] = zeroPos[1];
        int[][] rightState = swapWithZeroPos(pos);
        neighbors.add(rightState);
      }

      if (zeroPos[0] - 1 >= 0) {
        pos[0] = zeroPos[0] - 1;
        pos[1] = zeroPos[1];
        int[][] leftState = swapWithZeroPos(pos);
        neighbors.add(leftState);
      }

      if (zeroPos[1] + 1 < PUZZLE_SIZE) {
        pos[0] = zeroPos[0];
        pos[1] = zeroPos[1] + 1;
        int[][] downState = swapWithZeroPos(pos);
        neighbors.add(downState);
      }

      if (zeroPos[1] - 1 >= 0) {
        pos[0] = zeroPos[0];
        pos[1] = zeroPos[1] - 1;
        int[][] upState = swapWithZeroPos(pos);
        neighbors.add(upState);
      }

      return neighbors;
    }
    
    private int[][] swapWithZeroPos(int[] pos) {

      int[][] newState = new int[PUZZLE_SIZE][PUZZLE_SIZE];

      for (int i = 0; i < PUZZLE_SIZE; i++) {
        newState[i] = Arrays.copyOf(this.state[i], PUZZLE_SIZE);
      }

      newState[zeroPos[0]][zeroPos[1]] = state[pos[0]][pos[1]];
      newState[pos[0]][pos[1]] = 0;

      return newState;
    }

  }
