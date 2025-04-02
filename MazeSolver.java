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

    public static int[][] generateMaze(int rows, int cols) {
        int[][] maze = new int[rows][cols];
        for (int[] row : maze) Arrays.fill(row, 1); // fill with walls
        
        // Create the direct path (right then down)
        createDirectPath(maze);
        
        // Create two additional winding paths
        createWindingPath(maze, 0.7); // First winding path
        createWindingPath(maze, 0.5); // Second winding path
        
        // Ensure start and end are open
        maze[0][0] = 0;
        maze[rows-1][cols-1] = 0;
        
        return maze;
    }
    
    private static void createDirectPath(int[][] maze) {
        int rows = maze.length;
        int cols = maze[0].length;
        
        // First go right about 1/3 of the way
        int rightSteps = cols / 3;
        for (int x = 0; x < rightSteps; x++) {
            maze[0][x] = 0;
        }
        
        // Then go down to bottom
        for (int y = 0; y < rows; y++) {
            maze[y][rightSteps] = 0;
        }
        
        // Then go right to end
        for (int x = rightSteps; x < cols; x++) {
            maze[rows-1][x] = 0;
        }
    }
    
    private static void createWindingPath(int[][] maze, double turnProbability) {
        int rows = maze.length;
        int cols = maze[0].length;
        Random rand = new Random();
        
        int x = 0, y = 0;
        maze[y][x] = 0;
        
        while (x < cols-1 || y < rows-1) {
            // Randomly choose direction (but prefer progressing toward end)
            boolean canRight = x < cols-1;
            boolean canDown = y < rows-1;
            
            if (canRight && canDown) {
                if (rand.nextDouble() < turnProbability) {
                    x++; // go right
                } else {
                    y++; // go down
                }
            } else if (canRight) {
                x++;
            } else if (canDown) {
                y++;
            }
            
            // Mark path
            maze[y][x] = 0;
            
            // Occasionally branch sideways
            if (rand.nextDouble() < 0.3) {
                if (x > 0 && rand.nextBoolean()) {
                    maze[y][x-1] = 0;
                }
                if (y > 0 && rand.nextBoolean()) {
                    maze[y-1][x] = 0;
                }
            }
        }
    }

    private static boolean isValidMove(int[][] maze, int row, int col) {
        return row >= 0 && row < maze.length && col >= 0 && col < maze[0].length && maze[row][col] == 0;
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
