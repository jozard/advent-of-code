import sys
import time

sys.setrecursionlimit(pow(2, 16))

with open('data/day_16.data') as f:
    start_time = time.time()
    grid = [list(line) for line in f.read().splitlines()]

    direction = 'east'

    energized = {}


    def process_splitter(row: int, col: int, horizontal: bool, from_direction: str):
        if horizontal:
            if from_direction in ('east', 'west'):
                return process_tile(row, col + 1 if from_direction == 'west' else col - 1, from_direction)
            else:
                process_tile(row, col + 1, 'west')
                process_tile(row, col - 1, 'east')
                return
        else:
            if from_direction in ('east', 'west'):
                process_tile(row - 1, col, 'south')
                process_tile(row + 1, col, 'north')
                return
            else:
                return process_tile(row + 1 if from_direction == 'north' else row - 1, col, from_direction)


    def process_mirror(row, col, leaning_east, from_direction):
        if from_direction == 'north' and leaning_east or from_direction == 'south' and not leaning_east:
            return process_tile(row, col - 1, 'east')
        if from_direction == 'west' and leaning_east or from_direction == 'east' and not leaning_east:
            return process_tile(row - 1, col, 'south')
        if from_direction == 'north' and not leaning_east or from_direction == 'south' and leaning_east:
            return process_tile(row, col + 1, 'west')
        if from_direction == 'west' and not leaning_east or from_direction == 'east' and leaning_east:
            return process_tile(row + 1, col, 'north')


    def process_tile(row, col, from_direction):
        if row in range(len(grid)) and col in range(len(grid[0])):
            if (row, col) not in energized:
                energized[(row, col)] = []
            if from_direction not in energized[(row, col)]:
                energized[(row, col)].append(from_direction)
                value = grid[row][col]
                if value == '.':
                    if from_direction in ('east', 'west'):
                        return process_tile(row, col - 1 if from_direction == 'east' else col + 1, from_direction)
                    else:
                        return process_tile(row - 1 if from_direction == 'south' else row + 1, col, from_direction)
                elif value == '-':
                    return process_splitter(row, col, True, from_direction)
                elif value == '|':
                    return process_splitter(row, col, False, from_direction)
                else:
                    return process_mirror(row, col, value == '/', from_direction)
        return


    process_tile(0, 0, 'west')
    total = len(energized)

    print(f'{time.time() - start_time}s: {total}')
    f.close()
