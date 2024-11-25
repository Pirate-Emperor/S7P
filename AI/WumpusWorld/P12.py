import random

class WumpusWorld:
    def __init__(self):
        self.grid_size = 4
        self.world = [["" for _ in range(self.grid_size)] for _ in range(self.grid_size)]
        self.agent_position = (0, 0)
        self.gold_position = self._place_item("G")
        self.wumpus_position = self._place_item("W")
        self.pit_positions = [self._place_item("P") for _ in range(3)]
        self.visited = set()
        self.safe_moves = set()
        self.sensors_log = []

    def _place_item(self, item):
        while True:
            x, y = random.randint(0, 3), random.randint(0, 3)
            if (x, y) != (0, 0) and not self.world[x][y]:
                self.world[x][y] = item
                return (x, y)

    def sensors(self, x, y):
        sensors = []
        for dx, dy in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            nx, ny = x + dx, y + dy
            if 0 <= nx < self.grid_size and 0 <= ny < self.grid_size:
                if "W" in self.world[nx][ny]:
                    sensors.append("Stench")
                if "P" in self.world[nx][ny]:
                    sensors.append("Breeze")
        if "G" in self.world[x][y]:
            sensors.append("Glitter")
        return sensors

    def move_agent(self, move):
        x, y = self.agent_position
        if move == "up" and x > 0:
            x -= 1
        elif move == "down" and x < self.grid_size - 1:
            x += 1
        elif move == "left" and y > 0:
            y -= 1
        elif move == "right" and y < self.grid_size - 1:
            y += 1
        self.agent_position = (x, y)
        self.visited.add(self.agent_position)
        return self.check_status()

    def check_status(self):
        x, y = self.agent_position
        if self.agent_position == self.gold_position:
            return "Gold found! You win!"
        elif self.agent_position == self.wumpus_position:
            return "Eaten by Wumpus! Game over!"
        elif self.agent_position in self.pit_positions:
            return "Fell into a pit! Game over!"
        else:
            return "Safe, but proceed carefully."

    def get_possible_moves(self, x, y):
        moves = []
        if x > 0: moves.append("up")
        if x < self.grid_size - 1: moves.append("down")
        if y > 0: moves.append("left")
        if y < self.grid_size - 1: moves.append("right")
        return moves

    def ai_decision(self):
        x, y = self.agent_position
        sensors = self.sensors(x, y)
        self.sensors_log.append((self.agent_position, sensors))

        if "Glitter" in sensors:
            return "Gold found! You win!"

        possible_moves = self.get_possible_moves(x, y)
        safe_moves = [move for move in possible_moves if self.simulate_move(move) not in self.visited]
        random.shuffle(safe_moves)

        if safe_moves:
            chosen_move = safe_moves[0]
        else:
            chosen_move = random.choice(possible_moves)

        return self.move_agent(chosen_move)

    def simulate_move(self, move):
        x, y = self.agent_position
        if move == "up" and x > 0:
            return (x - 1, y)
        elif move == "down" and x < self.grid_size - 1:
            return (x + 1, y)
        elif move == "left" and y > 0:
            return (x, y - 1)
        elif move == "right" and y < self.grid_size - 1:
            return (x, y + 1)
        return (x, y)

    def play(self):
        print("Starting AI Agent Simulation in the Wumpus World...")
        while True:
            status = self.ai_decision()
            print(f"\nAgent is at {self.agent_position}.")
            print("Sensors:", self.sensors(*self.agent_position))
            print(status)
            if "Game over" in status or "win" in status:
                break

if __name__ == "__main__":
    game = WumpusWorld()
    game.play()
