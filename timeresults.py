import matplotlib.pyplot as plt
import numpy as np

# Maze sizes
maze_sizes = ['5x5', '25x25', '75x75', '365x365', '1025x1025']

# Average DFS times (ms)
dfs_times = [
    np.mean([1.040422, 0.890949, 1.070832]),
    np.mean([1.670544, 1.231872, 1.051845]),
    np.mean([2.357935, 3.335583, 2.390045]),
    np.mean([8.281993, 10.507811, 9.082009]),
    np.mean([16.216374, 17.417828, 17.497572])
]

# Average BFS times (ms)
bfs_times = [
    np.mean([1.609193, 0.841849, 0.853581]),
    np.mean([3.491798, 1.551450, 1.531369]),
    np.mean([2.232198, 2.031944, 2.459487]),
    np.mean([7.761050, 9.415860, 10.927133]),
    np.mean([27.641073, 24.111462, 23.003408])
]

plt.figure(figsize=(10, 6))
plt.plot(maze_sizes, dfs_times, 'o-', label='DFS')
plt.plot(maze_sizes, bfs_times, 's-', label='BFS')
plt.title('Maze Solving Algorithm Performance')
plt.xlabel('Maze Size')
plt.ylabel('Average Time (ms)')
plt.legend()
plt.grid(True)
plt.show()