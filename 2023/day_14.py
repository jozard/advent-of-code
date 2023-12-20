import re
import time

with open('data/day_14.data') as f:
    start_time = time.time()
    platform = f.read().splitlines()

    transposed = [list(a) for a in zip(*platform)]  # north to the left

    for line in transposed:
        matches = re.finditer(r'\.+O[.O]*', ''.join(line))
        for m in matches:
            start = m.start()
            end = m.end()
            l = m.group()
            stone_cnt = sum(1 if ch == 'O' else 0 for ch in l)
            for i in range(len(line)):
                if i in range(start, start + stone_cnt):
                    line[i] = 'O'
                elif i in range(start + stone_cnt, end):
                    line[i] = '.'

    total = 0
    for line in transposed:
        for i in range(len(line)):
            total += len(line) - i if line[i] == 'O' else 0

    print(f'{time.time() - start_time}s: {total}')
    f.close()
