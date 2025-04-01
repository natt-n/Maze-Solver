import java.util.*;

public class MazeTester {
    public static void main(String[] args) {
        int[] sizes = {5, 25, 75, 365, 1025};

        for (int size : sizes) {
            System.out.println("\nGenerating " + size + "x" + size + " maze...");
            int[][] maze = MazeSolver.generateMaze(size, size);
            
            MazeSolver.Point start = new MazeSolver.Point(0, 0, null);
            MazeSolver.Point end = new MazeSolver.Point(size - 1, size - 1, null);
            
            List<MazeSolver.Point> dfsPath = MazeSolver.dfs(maze, start, end);
            System.out.println("DFS Path Length: " + dfsPath.size());
            List<MazeSolver.Point> bfsPath = MazeSolver.bfs(maze, start, end);
            System.out.println("BFS Path Length: " + bfsPath.size());

        }
    }
}