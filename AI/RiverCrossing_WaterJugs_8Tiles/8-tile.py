import heapq

def heuristic(state):
    goal_state = [1, 2, 3, 8, 0, 4, 7, 6, 5]
    distance = 0
    for i, value in enumerate(state):
        goal_index = goal_state.index(value)
        if value != 0:
            current_row, current_col = divmod(i, 3)
            goal_row, goal_col = divmod(goal_index, 3)
            distance += abs(current_row - goal_row) + abs(current_col - goal_col)
    return distance

def get_neighbors(state):
    neighbors = []
    blank_index = state.index(0)
    row, col = divmod(blank_index, 3)
    possible_moves = []
    
    if row > 0:
        possible_moves.append(blank_index - 3)
    if row < 2:
        possible_moves.append(blank_index + 3)
    if col > 0:
        possible_moves.append(blank_index - 1)
    if col < 2:
        possible_moves.append(blank_index + 1)
    
    for new_index in possible_moves:
        new_state = list(state)
        new_state[blank_index], new_state[new_index] = new_state[new_index], new_state[blank_index]
        neighbors.append(tuple(new_state))
    
    return neighbors

def format_state(state):
    return "\n".join(" ".join(str(x) if x != 0 else "_" for x in state[i:i+3]) for i in range(0, 9, 3))

def solve_8_tiles_problem():
    initial_state = (2, 8, 3, 1, 6, 4, 7, 0, 5)
    goal_state = (1, 2, 3, 8, 0, 4, 7, 6, 5)
    
    open_set = []
    heapq.heappush(open_set, (heuristic(initial_state), 0, initial_state, []))
    visited = set()
    
    while open_set:
        _, cost, state, path = heapq.heappop(open_set)
        
        if state == goal_state:
            return path + [state]
        
        if state in visited:
            continue
        visited.add(state)
        
        for neighbor in get_neighbors(state):
            heapq.heappush(open_set, (cost + 1 + heuristic(neighbor), cost + 1, neighbor, path + [state]))

    return None

solution = solve_8_tiles_problem()
if solution:
    print("Solution Path:")
    for step in solution:
        print(format_state(step))
        print()
else:
    print("No solution found.")
