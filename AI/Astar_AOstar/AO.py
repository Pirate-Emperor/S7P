class AONode:
    def __init__(self, position, parent=None):
        self.position = position  
        self.parent = parent
        self.children = []
        self.cost = float('inf')
        self.is_goal = False
        

def get_neighbors(position, grid):
    neighbors = []
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]  
    for d in directions:
        new_pos = (position[0] + d[0], position[1] + d[1])
        if 0 <= new_pos[0] < len(grid) and 0 <= new_pos[1] < len(grid[0]) and grid[new_pos[0]][new_pos[1]] == 0:
            neighbors.append(new_pos)
    return neighbors


def ao_star(start_node, goal_position, grid):
    start_node.cost = 0
    agenda = [start_node]

    while agenda:
        current_node = agenda.pop(0)
        if current_node.position == goal_position:
            return current_node  
        
        neighbors = get_neighbors(current_node.position, grid)
        for neighbor_pos in neighbors:
            child_node = AONode(neighbor_pos)
            child_cost = current_node.cost + 1  

            if child_cost < child_node.cost:
                child_node.cost = child_cost
                child_node.parent = current_node  
                if child_node not in agenda:
                    agenda.append(child_node)

    return None

def get_path(node):
    path = []
    while node:
        path.append(node.position)
        node = node.parent
    return path[::-1]  

grid = [[0, 0, 0, 0, 0],
        [0, 1, 1, 1, 0],
        [0, 0, 0, 0, 0],
        [0, 1, 1, 0, 0],
        [0, 0, 0, 0, 0]]

start_position = (0, 0)
goal_position = (4, 4)
start_node = AONode(start_position)
goal_node = ao_star(start_node, goal_position, grid)

if goal_node:
    path = get_path(goal_node)
    print("AO* Path:", path)
else:
    print("No path found")
