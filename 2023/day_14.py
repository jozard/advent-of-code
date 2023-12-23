import re
import time

with open('data/day_14.data') as f:
    start_time = time.time()
    platform = [list(line) for line in f.read().splitlines()]
    print('original platform')
    print(platform)

    width = len(platform[0])
    height = len(platform)

    hor_spaces = []
    for row in platform:
        ranges = re.finditer(r'[.O]+', ''.join(row))
        hor_spaces.append([])
        for r in ranges:
            hor_spaces[len(hor_spaces) - 1].append(range(r.start(), r.end()))

    vert_spaces = []
    for col in [list(a) for a in zip(*platform)]:
        ranges = re.finditer(r'[.O]+', ''.join(col))
        vert_spaces.append([])
        for r in ranges:
            vert_spaces[len(vert_spaces) - 1].append(range(r.start(), r.end()))

    stones = []
    for row in range(height):
        for col in range(width):
            if platform[row][col] == 'O':
                stones.append([row, col])


    def show():
        for i in range(height):
            for j in range(width):
                if any([stone[0] == i and stone[1] == j for stone in stones]):
                    print('O', end='')
                elif any([j in space for space in hor_spaces[i]]):
                    print('.', end='')
                else:
                    print('#', end='')
            print()
        print()


    def get_total():
        # calculate load to north
        total = 0
        for stone in stones:
            total += (height - stone[0])
        return total


    def tilt(stones, spaces, size, tilt_vertical, roll_to_start):
        for i in range(size):
            filtered_stones = [stone for stone in stones if stone[1 if tilt_vertical else 0] == i]
            filtered_spaces = spaces[i]
            for space in filtered_spaces:
                space_stones = [stone for stone in filtered_stones if
                                stone[0 if tilt_vertical else 1] in space]
                for j in range(len(space_stones)):
                    space_stones[j][0 if tilt_vertical else 1] = j + space[0] if roll_to_start else space[-1] - j


    def get_period(totals):
        min_count = 10  # we consider period found if sequence repeated 10 times
        p = 1  # set initial sequence length to 1
        while len(totals) / min_count >= p:
            for shift in range(len(totals)):
                splits = totals[shift:]
                splits = [splits[i:i + p] for i in range(0, len(splits), p)]
                if min_count <= len(splits):
                    if len(splits) >= min_count:
                        first_match = splits[0]
                        if all(split == first_match for split in splits):
                            return shift, p
            p += 1
        return None


    totals = []
    period = None
    while period is None:
        # for cycle in range(3):
        tilt(stones, vert_spaces, width, True, True)
        tilt(stones, hor_spaces, height, False, True)
        tilt(stones, vert_spaces, width, True, False)
        tilt(stones, hor_spaces, height, False, False)

        # show()
        total = get_total()
        totals.append(total)

        period = get_period(totals)

    total_index = (999999999 - period[0]) % period[1] + period[0]
    print(f'{time.time() - start_time}s: {totals[total_index]}')
    f.close()
