import java.util.*;


public class MazeSolverFinal {


   static class Point {
       int row, col;
       Point parent;


       Point(int row, int col, Point parent) {
           this.row = row;
           this.col = col;
           this.parent = parent;
       }


       @Override
       public boolean equals(Object obj) {
           if (!(obj instanceof Point)) return false;
           Point other = (Point) obj;
           return this.row == other.row && this.col == other.col;
       }


       @Override
       public int hashCode() {
           return Objects.hash(row, col);
       }
   }


   public static int[][] generateMaze(int rows, int cols) {
       int[][] maze = new int[rows][cols];
       for (int[] row : maze) Arrays.fill(row, 1); // fill with walls


       Random rand = new Random();
       Stack<Point> stack = new Stack<>();
       Point start = new Point(0, 0, null);
       maze[start.row][start.col] = 0;
       stack.push(start);


       int[][] directions = { { -2, 0 }, { 2, 0 }, { 0, -2 }, { 0, 2 } };


       while (!stack.isEmpty()) {
           Point current = stack.peek();
           List<int[]> neighbors = new ArrayList<>();


           for (int[] dir : directions) {
               int newRow = current.row + dir[0];
               int newCol = current.col + dir[1];


               if (isInBounds(newRow, newCol, rows, cols) && maze[newRow][newCol] == 1) {
                   neighbors.add(new int[] { newRow, newCol });
               }
           }


           if (!neighbors.isEmpty()) {
               int[] chosen = neighbors.get(rand.nextInt(neighbors.size()));
               int midRow = (current.row + chosen[0]) / 2;
               int midCol = (current.col + chosen[1]) / 2;
               maze[midRow][midCol] = 0;
               maze[chosen[0]][chosen[1]] = 0;
               stack.push(new Point(chosen[0], chosen[1], null));
           } else {
               stack.pop();
           }
       }


       maze[rows - 1][cols - 1] = 0; // end is open
       return maze;
   }


   private static boolean isInBounds(int row, int col, int rows, int cols) {
       return row >= 0 && row < rows && col >= 0 && col < cols;
   }


   public static List<Point> bfs(int[][] maze, Point start, Point end) {
       int rows = maze.length;
       int cols = maze[0].length;
       boolean[][] visited = new boolean[rows][cols];


       Queue<Point> queue = new LinkedList<>();
       queue.offer(start);
       visited[start.row][start.col] = true;


       List<int[]> directions = Arrays.asList(
               new int[]{ -1, 0 }, new int[]{ 1, 0 },
               new int[]{ 0, -1 }, new int[]{ 0, 1 }
       );


       while (!queue.isEmpty()) {
           Point current = queue.poll();


           if (current.row == end.row && current.col == end.col) {
               return buildPath(current);
           }


           Collections.shuffle(directions); // randomizes direction


           for (int[] dir : directions) {
               int newRow = current.row + dir[0];
               int newCol = current.col + dir[1];


               if (isValidMove(maze, visited, newRow, newCol)) {
                   visited[newRow][newCol] = true;
                   queue.offer(new Point(newRow, newCol, current));
               }
           }
       }


       return Collections.emptyList(); // displays no path found
   }


   private static boolean isValidMove(int[][] maze, boolean[][] visited, int row, int col) {
       return row >= 0 && row < maze.length &&
               col >= 0 && col < maze[0].length &&
               maze[row][col] == 0 && !visited[row][col];
   }


   private static List<Point> buildPath(Point end) {
       List<Point> path = new ArrayList<>();
       while (end != null) {
           path.add(end);
           end = end.parent;
       }
       Collections.reverse(path);
       return path;
   }


   public static void printMazeWithPath(int[][] maze, List<Point> path) {
       char[][] display = new char[maze.length][maze[0].length];


       for (int i = 0; i < maze.length; i++) {
           for (int j = 0; j < maze[0].length; j++) {
               display[i][j] = (maze[i][j] == 1) ? '|' : ' ';
           }
       }


       for (Point p : path) {
           if (maze[p.row][p.col] == 0) {
               display[p.row][p.col] = '*';
           }
       }


       for (char[] row : display) {
           for (char cell : row) {
               System.out.print(cell + " ");
           }
           System.out.println();
       }
   }


   public static void main(String[] args) {
       int rows = 11;
       int cols = 11;


       // Measures maze generation time
       long genStart = System.nanoTime();
       int[][] maze = generateMaze(rows, cols);
       long genEnd = System.nanoTime();
       double genTimeMs = (genEnd - genStart) / 1_000_000.0;


       Point start = new Point(0, 0, null);
       Point end = new Point(rows - 1, cols - 1, null);


       // Measures solving time
       long solveStart = System.nanoTime();
       List<Point> path = bfs(maze, start, end);
       long solveEnd = System.nanoTime();
       double solveTimeMs = (solveEnd - solveStart) / 1_000_000.0;


       // Output
       System.out.printf("Maze Generation Time: %.3f ms\n", genTimeMs);
       System.out.printf("Maze Solving Time: %.3f ms\n", solveTimeMs);


       if (!path.isEmpty()) {
           System.out.println("Shortest path length: " + path.size());
           System.out.println("\nMaze with path:");
           printMazeWithPath(maze, path);
       } else {
           System.out.println("No path found.");
       }
   }
}



