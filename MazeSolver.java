import java.util.*;

public class MazeSolver {
    public static class Point {
        int row, col;
        Point parent;

        public Point(int row, int col, Point parent) {
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

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }

    // Build the graph (adjacency list) representation of the maze
    private static Map<Point, List<Point>> buildGraph(int[][] maze) {
        Map<Point, List<Point>> graph = new HashMap<>();
        int rows = maze.length, cols = maze[0].length;
        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (maze[row][col] == 0) {  // Valid cell
                    Point node = new Point(row, col, null);
                    List<Point> neighbors = new ArrayList<>();

                    for (int[] dir : directions) {
                        int newRow = row + dir[0];
                        int newCol = col + dir[1];
                        if (isValidMove(maze, newRow, newCol)) {
                            neighbors.add(new Point(newRow, newCol, null));  // Add valid neighbor
                        }
                    }
                    graph.put(node, neighbors);
                }
            }
        }
        return graph;
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

    private static boolean isValidMove(int[][] maze, int row, int col) {
        return row >= 0 && row < maze.length && col >= 0 && col < maze[0].length && maze[row][col] == 0;
    }

    // BFS with graph representation
    public static List<Point> bfs(int[][] maze, Point start, Point end) {
        long startTime = System.nanoTime();
        Map<Point, List<Point>> graph = buildGraph(maze);
        Queue<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        
        start.parent = null;  // Set the starting point's parent to null
        queue.offer(start);
        visited.add(start);
    
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(end)) {
                long endTime = System.nanoTime();
                double elapsedTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
                System.out.printf("BFS Maze solving time: %.6f ms%n", elapsedTime);
                return buildPath(current);
            }
    
            for (Point neighbor : graph.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    neighbor.parent = current;  // Set parent during BFS traversal
                    queue.offer(neighbor);
                }
            }
        }
        return Collections.emptyList();
    }
    
    // DFS with graph representation
    public static List<Point> dfs(int[][] maze, Point start, Point end) {
        long startTime = System.nanoTime();
        Map<Point, List<Point>> graph = buildGraph(maze);
        Stack<Point> stack = new Stack<>();
        Set<Point> visited = new HashSet<>();
        
        start.parent = null;  // Set the starting point's parent to null
        stack.push(start);
        visited.add(start);
    
        while (!stack.isEmpty()) {
            Point current = stack.pop();
            if (current.equals(end)) {
                long endTime = System.nanoTime();
                double elapsedTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
                System.out.printf("DFS Maze solving time: %.6f ms%n", elapsedTime);
                return buildPath(current);
            }
    
            for (Point neighbor : graph.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    neighbor.parent = current;  // Set parent during DFS traversal
                    stack.push(neighbor);
                }
            }
        }
        return Collections.emptyList();
    }

    // Reconstruct the path from end to start using parent pointers
    private static List<Point> buildPath(Point end) {
        List<Point> path = new ArrayList<>();
        while (end != null) {
            path.add(end);
            end = end.parent;
        }
        Collections.reverse(path);
        return path;
    }
}
