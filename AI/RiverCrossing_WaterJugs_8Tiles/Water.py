from collections import deque

def get_neighbors(state):
    x, y = state
    neighbors = []
    
    neighbors.append((3, y)) 
    neighbors.append((x, 5))
    neighbors.append((0, y))
    neighbors.append((x, 0))
    
    pour_to_2 = min(x, 5 - y)
    neighbors.append((x - pour_to_2, y + pour_to_2))
    pour_to_1 = min(y, 3 - x)
    neighbors.append((x + pour_to_1, y - pour_to_1))
    
    return neighbors

def format_state(state):
    return f"Jug 1: {state[0]} gal | Jug 2: {state[1]} gal"

def solve_water_jugs_problem():
    initial_state = (0, 0)
    goal_state = (0, 4)
    queue = deque([(initial_state, [])])
    visited = set()
    
    while queue:
        (state, path) = queue.popleft()
        if state == goal_state:
            return path + [state]
        
        if state in visited:
            continue
        visited.add(state)
        
        for neighbor in get_neighbors(state):
            queue.append((neighbor, path + [state]))

    return None

solution = solve_water_jugs_problem()
if solution:
    print("Solution Path:")
    for step in solution:
        print(format_state(step))
else:
    print("No solution found.")
