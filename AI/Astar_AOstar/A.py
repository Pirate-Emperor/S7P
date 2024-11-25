import heapq

class ANode:
    def __init__(self, position, parent=None):
        self.position = position
        self.parent = parent
        self.g = 0  
        self.h = 0  
        self.f = 0  

    def __eq__(self, other):
        return self.position == other.position

    def __lt__(self, other):
        return self.f < other.f

def heuristic(a, b):
    return abs(a[0] - b[0]) + abs(a[1] - b[1])

def get_neighbors(position, grid):
    neighbors = []
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]  
    for d in directions:
        new_pos = (position[0] + d[0], position[1] + d[1])
        if 0 <= new_pos[0] < len(grid) and 0 <= new_pos[1] < len(grid[0]) and grid[new_pos[0]][new_pos[1]] == 0:
            neighbors.append(new_pos)
    return neighbors

def astar(grid, start, goal):
    open_list = []
    closed_list = []
    
    start_node = ANode(start)
    goal_node = ANode(goal)

    heapq.heappush(open_list, (start_node.f, start_node))

    while open_list:
        current_node = heapq.heappop(open_list)[1]
        closed_list.append(current_node)

        if current_node == goal_node:
            path = []
            while current_node:
                path.append(current_node.position)
                current_node = current_node.parent
            return path[::-1]


        neighbors = get_neighbors(current_node.position, grid)
        for neighbor_pos in neighbors:
            node_position = neighbor_pos
            
            neighbor_node = ANode(node_position, current_node)
            if neighbor_node in closed_list:
                continue

            neighbor_node.g = current_node.g + 1
            neighbor_node.h = heuristic(neighbor_node.position, goal_node.position)
            neighbor_node.f = neighbor_node.g + neighbor_node.h

            if add_to_open(open_list, neighbor_node):
                heapq.heappush(open_list, (neighbor_node.f, neighbor_node))

    return None

def add_to_open(open_list, neighbor):
    for ANode in open_list:
        if neighbor == ANode[1] and neighbor.g > ANode[1].g:
            return False
    return True


grid = [[0, 0, 0, 0, 0],
        [0, 1, 1, 1, 0],
        [0, 0, 0, 0, 0],
        [0, 1, 1, 0, 0],
        [0, 0, 0, 0, 0]]

start = (0, 0)
goal = (4, 4)
path = astar(grid, start, goal)
print("A* Path:", path)
