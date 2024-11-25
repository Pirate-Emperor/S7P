from collections import deque

def is_valid_state(state):
    farmer, wolf, goat, cabbage = state
    if wolf == goat and wolf != farmer:
        return False
    if goat == cabbage and goat != farmer:
        return False
    return True

def get_neighbors(state):
    farmer, wolf, goat, cabbage = state
    neighbors = []
    moves = [('wolf', wolf), ('goat', goat), ('cabbage', cabbage), ('none', None)]
    
    for item, current in moves:
        if item == 'none':
            new_state = (1 - farmer, wolf, goat, cabbage)
        else:
            new_farm = 1 - farmer
            if item == 'wolf':
                new_state = (new_farm, 1 - wolf, goat, cabbage)
            elif item == 'goat':
                new_state = (new_farm, wolf, 1 - goat, cabbage)
            elif item == 'cabbage':
                new_state = (new_farm, wolf, goat, 1 - cabbage)
        
        if is_valid_state(new_state):
            neighbors.append(new_state)
    
    return neighbors

def format_state(state):
    bank = ["Left", "Right"]
    f, w, g, c = state
    f, w, g, c = bank[f], bank[w], bank[g], bank[c]
    return f"{f:<6} {w:<6} {g:<6} {c:<6}"

def solve_farm_problem():
    initial_state = (0, 0, 0, 0)
    goal_state = (1, 1, 1, 1)
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

solution = solve_farm_problem()
if solution:
    print("Solution Path:")
    print(f"{'Farmer':<6} {'Wolf':<6} {'Goat':<6} {'Cabbage':<6}")
    for step in solution:
        print(format_state(step))
else:
    print("No solution found.")
