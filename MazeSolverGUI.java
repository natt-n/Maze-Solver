import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MazeSolverGUI extends JPanel {
    private static final int CELL_SIZE = 30;
    private int[][] maze;
    private List<MazeSolver.Point> path;
    private int step = 0;

    public MazeSolverGUI(int[][] maze, List<MazeSolver.Point> path) {
        this.maze = maze;
        this.path = path;
        Timer timer = new Timer(100, e -> {
            if (step < path.size()) {
                step++;
                repaint();
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == 1) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.GRAY);
                }
                g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        for (int i = 0; i < step; i++) {
            MazeSolver.Point p = path.get(i);
            g.setColor(Color.PINK);
            g.fillRect(p.col * CELL_SIZE, p.row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    public static void main(String[] args) {
        int rows = 11, cols = 11;
        int[][] maze = MazeSolver.generateMaze(rows, cols);
        List<MazeSolver.Point> DFSpath = MazeSolver.dfs(maze, new MazeSolver.Point(0, 0, null), new MazeSolver.Point(rows - 1, cols - 1, null));
        List<MazeSolver.Point> BFSpath= MazeSolver.bfs(maze, new MazeSolver.Point(0, 0, null), new MazeSolver.Point(rows - 1, cols - 1, null));
        
        JFrame frame = new JFrame("Maze Solver");
        MazeSolverGUI panel = new MazeSolverGUI(maze, DFSpath);
        frame.add(panel);
        frame.setSize(cols * CELL_SIZE + 20, rows * CELL_SIZE + 40);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        /*
        javax.swing.Timer timer = new javax.swing.Timer(2000, e -> {
            JFrame frame2 = new JFrame("Maze Solver");
            MazeSolverGUI panel2 = new MazeSolverGUI(maze, BFSpath);
            frame2.add(panel2);
            frame2.setSize(cols * CELL_SIZE + 20, rows * CELL_SIZE + 40);
            frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame2.setVisible(true);
        });
        
        timer.setRepeats(false);
        timer.start();
        */
        // Uncomment the above code to show BFS path after a delay
    }
}
