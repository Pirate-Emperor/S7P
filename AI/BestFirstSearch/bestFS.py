import heapq

def func(x):
    return -(x - 3) ** 2 + 5


def best_first_search(start, step_size, max_iterations):
    
    open_list = []
    heapq.heappush(open_list, (-func(start), start))  

    visited = set()
    best_solution = start
    best_value = func(start)

    while open_list and max_iterations > 0:
        
        _, current = heapq.heappop(open_list)
        current_value = func(current)

        
        if current in visited:
            continue

        
        if current_value > best_value:
            best_solution = current
            best_value = current_value

        
        visited.add(current)

        
        neighbors = [current + step_size, current - step_size]
        for neighbor in neighbors:
            if neighbor not in visited:
                heapq.heappush(open_list, (-func(neighbor), neighbor))  

        max_iterations -= 1
    
    
    return best_solution, best_value


start = 0  
step_size = 0.1  
max_iterations = 100  


solution, solution_value = best_first_search(start, step_size, max_iterations)

print(f"Solution found: x = {solution}, f(x) = {solution_value}")
