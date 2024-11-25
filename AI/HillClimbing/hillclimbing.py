import random
def func(x):
    return -(x - 3) ** 2 + 5

def hill_climbing(start, step_size, max_iterations):
    current = start
    current_value = func(current)
    
    for _ in range(max_iterations):
        
        neighbors = [current + step_size, current - step_size]
        best_neighbor = neighbors[0]
        best_value = func(best_neighbor)
        
        
        for neighbor in neighbors:
            value = func(neighbor)
            if value > best_value:
                best_value = value
                best_neighbor = neighbor
        
        
        if best_value <= current_value:
            break
        
        
        current = best_neighbor
        current_value = best_value
        
    return current, current_value


start = random.uniform(-10, 10)  
step_size = 0.1  
max_iterations = 100  


solution, solution_value = hill_climbing(start, step_size, max_iterations)

print(f"Solution found: x = {solution}, f(x) = {solution_value}")
