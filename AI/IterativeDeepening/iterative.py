def iterative_deepening_search(start, goal, neighbors_fn, max_depth):
    def depth_limited_search(node, depth):
        if depth == 0 and node == goal:
            return [node]
        if depth > 0:
            for neighbor in neighbors_fn(node):
                path = depth_limited_search(neighbor, depth - 1)
                if path:
                    return [node] + path
        return None

    for depth in range(max_depth + 1):
        result = depth_limited_search(start, depth)
        if result:
            return result
    return None

# Example: 8-puzzle
start_state = (1, 2, 3, 4, 0, 5, 6, 7, 8)
goal_state = (1, 2, 3, 4, 5, 6, 7, 8, 0)

def get_neighbors(state):
    i = state.index(0)
    moves = []
    if i % 3 > 0: moves.append(i - 1)
    if i % 3 < 2: moves.append(i + 1)
    if i // 3 > 0: moves.append(i - 3)
    if i // 3 < 2: moves.append(i + 3)
    neighbors = []
    for move in moves:
        new_state = list(state)
        new_state[i], new_state[move] = new_state[move], new_state[i]
        neighbors.append(tuple(new_state))
    return neighbors

path = iterative_deepening_search(start_state, goal_state, get_neighbors, max_depth=30)

print("\nPath to goal:")
if path:
    for step in path:
        print(step)
else:
    print("No solution found.")
