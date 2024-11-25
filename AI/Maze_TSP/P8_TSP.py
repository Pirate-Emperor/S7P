import numpy as np

class TSP:
    def __init__(self, distance_matrix):
        self.distance_matrix = distance_matrix
        self.num_cities = len(distance_matrix)

    def nearest_neighbor(self, start_city):
        visited = [False] * self.num_cities
        visited[start_city] = True
        tour = [start_city]
        total_distance = 0

        current_city = start_city
        
        for _ in range(self.num_cities - 1):
            nearest_city = None
            nearest_distance = float('inf')

            for city in range(self.num_cities):
                if not visited[city] and self.distance_matrix[current_city][city] < nearest_distance:
                    nearest_city = city
                    nearest_distance = self.distance_matrix[current_city][city]

            tour.append(nearest_city)
            total_distance += nearest_distance
            visited[nearest_city] = True
            current_city = nearest_city
        
        
        total_distance += self.distance_matrix[current_city][start_city]
        tour.append(start_city)
        
        return tour, total_distance

def main():
    n = int(input("Enter the count of nodes: "))
    distance_matrix = (np.random.rand(n,n)*100).astype(int)
    print("Distance Matrix", distance_matrix)
    tsp = TSP(distance_matrix)
    start_city = 0  
    tour, total_distance = tsp.nearest_neighbor(start_city)

    print("Tour:", tour)
    print("Total Distance:", total_distance)

if __name__ == "__main__":
    main()
