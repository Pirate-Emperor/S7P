import pygame
import random
import sys


WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
RED = (255, 0, 0)
GREEN = (0, 255, 0)
GRAY = (200, 200, 200)


def generate_maze(width, height):
    maze = [['#'] * width for _ in range(height)]
    stack = []
    
    start_x = random.randint(1, width - 2)
    start_y = random.randint(1, height - 2)
    
    maze[start_y][start_x] = ' '
    stack.append((start_x, start_y))

    while stack:
        x, y = stack[-1]
        neighbors = []

        for dx, dy in [(2, 0), (-2, 0), (0, 2), (0, -2)]:
            nx, ny = x + dx, y + dy
            if 0 < nx < width - 1 and 0 < ny < height - 1 and maze[ny][nx] == '#':
                neighbors.append((nx, ny))

        if neighbors:
            nx, ny = random.choice(neighbors)
            maze[ny][nx] = ' '
            maze[y + (ny - y) // 2][x + (nx - x) // 2] = ' '
            stack.append((nx, ny))
        else:
            stack.pop()

    
    start_pos = (start_x, start_y)
    end_pos = (random.randint(1, width - 2), random.randint(1, height - 2))

    while maze[end_pos[1]][end_pos[0]] != ' ':
        end_pos = (random.randint(1, width - 2), random.randint(1, height - 2))

    maze[start_pos[1]][start_pos[0]] = 'S'  
    maze[end_pos[1]][end_pos[0]] = 'E'      

    return maze, start_pos, end_pos


def draw_maze(screen, maze, player_pos, end_pos):
    screen.fill(GRAY)
    for y, row in enumerate(maze):
        for x, cell in enumerate(row):
            if cell == '#':
                pygame.draw.rect(screen, BLACK, (x * 20, y * 20, 20, 20))
            elif cell == 'S':
                pygame.draw.rect(screen, GREEN, (x * 20, y * 20, 20, 20))  
            elif cell == 'E':
                pygame.draw.rect(screen, (0, 0, 255), (x * 20, y * 20, 20, 20))  
            else:
                pygame.draw.rect(screen, WHITE, (x * 20, y * 20, 20, 20))

    player_x, player_y = player_pos
    pygame.draw.rect(screen, RED, (player_x * 20, player_y * 20, 20, 20))  


def main_menu(screen):
    font = pygame.font.Font(None, 48)
    title_text = font.render("Maze Game", True, BLACK)
    play_text = font.render("Play", True, BLACK)
    quit_text = font.render("Quit", True, BLACK)

    while True:
        screen.fill(WHITE)
        screen.blit(title_text, (100, 50))
        screen.blit(play_text, (150, 150))
        screen.blit(quit_text, (150, 250))
        pygame.display.flip()

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.MOUSEBUTTONDOWN:
                mouse_pos = pygame.mouse.get_pos()
                if 150 <= mouse_pos[0] <= 250:
                    if 150 <= mouse_pos[1] <= 200:  
                        return "play"
                    elif 250 <= mouse_pos[1] <= 300:  
                        pygame.quit()
                        sys.exit()


def size_query(screen):
    font = pygame.font.Font(None, 36)
    input_box = pygame.Rect(100, 150, 140, 32)
    color_inactive = pygame.Color('lightskyblue3')
    color_active = pygame.Color('dodgerblue2')
    color = color_inactive
    active = False
    size_text = ''
    prompt_text = font.render("Enter Maze Size (width height):", True, BLACK)

    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.MOUSEBUTTONDOWN:
                if input_box.collidepoint(event.pos):
                    active = not active
                else:
                    active = False
                color = color_active if active else color_inactive
            if event.type == pygame.KEYDOWN:
                if active:
                    if event.key == pygame.K_RETURN:
                        try:
                            width, height = map(int, size_text.split())
                            width+=(width+1)%2
                            height+=(height+1)%2
                            if width % 2 == 0 or height % 2 == 0:
                                raise ValueError
                            return width, height
                        except ValueError:
                            size_text = ''
                    elif event.key == pygame.K_BACKSPACE:
                        size_text = size_text[:-1]
                    else:
                        size_text += event.unicode

        screen.fill(WHITE)
        screen.blit(prompt_text, (50, 50))
        txt_surface = font.render(size_text, True, color)
        width_input = max(200, txt_surface.get_width()+10)
        input_box.w = width_input
        screen.blit(txt_surface, (input_box.x+5, input_box.y+5))
        pygame.draw.rect(screen, color, input_box, 2)
        pygame.display.flip()


def game_loop(screen, width, height):
    maze, start_pos, end_pos = generate_maze(width, height)
    player_pos = list(start_pos)

    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()

        keys = pygame.key.get_pressed()
        if keys[pygame.K_w] and maze[player_pos[1] - 1][player_pos[0]] in (' ', 'E'):
            player_pos[1] -= 1
        if keys[pygame.K_s] and maze[player_pos[1] + 1][player_pos[0]] in (' ', 'E'):
            player_pos[1] += 1
        if keys[pygame.K_a] and maze[player_pos[1]][player_pos[0] - 1] in (' ', 'E'):
            player_pos[0] -= 1
        if keys[pygame.K_d] and maze[player_pos[1]][player_pos[0] + 1] in (' ', 'E'):
            player_pos[0] += 1
        pygame.time.delay(100)
        if tuple(player_pos) == end_pos:
            return  

        draw_maze(screen, maze, player_pos, end_pos)
        pygame.display.flip()

def main():
    pygame.init()
    screen = pygame.display.set_mode((1800, 1000))
    pygame.display.set_caption("Maze Game")

    while True:
        choice = main_menu(screen)
        if choice == "play":
            width, height = size_query(screen)
            game_loop(screen, width, height)

if __name__ == "__main__":
    main()
