import matplotlib.pyplot as plt
import numpy as np

# Maze sizes
maze_sizes = ['5x5', '25x25', '75x75', '365x365', '1025x1025']

# Average DFS path lengths
dfs_lengths = [
    np.mean([9, 9, 9]),
    np.mean([51, 49, 97]),
    np.mean([339, 333, 319]),
    np.mean([995, 729, 729]),
    np.mean([2049, 4711, 2053])
]

# Average BFS path lengths
bfs_lengths = [
    np.mean([9, 9, 9]),
    np.mean([49, 49, 49]),
    np.mean([149, 149, 149]),
    np.mean([729, 729, 729]),
    np.mean([2049, 2049, 2049])
]

plt.figure(figsize=(10, 6))
plt.plot(maze_sizes, dfs_lengths, 'o-', label='DFS')
plt.plot(maze_sizes, bfs_lengths, 's-', label='BFS')
plt.title('Path Length Found by Algorithm')
plt.xlabel('Maze Size')
plt.ylabel('Average Path Length')
plt.legend()
plt.grid(True)
plt.show()