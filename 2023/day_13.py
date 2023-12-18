import copy
import time

with open('data/day_13.data') as f:
    start_time = time.time()
    mirrors = [[list(map(lambda ch: 0 if ch == '.' else 1, ll)) for ll in line.splitlines()] for line in
               f.read().split('\n\n')]


    def is_perfect(mirror, i):
        m = i
        n = i + 1
        while m >= 0 and n < len(mirror):
            if mirror[m] != mirror[n]:
                return False
            m -= 1
            n += 1
        return True


    def print_mirror(mirror):
        for row in mirror:
            print(row)
        print()


    def get_reflection_index_2(m):
        for i in range(1, len(m)):
            above = m[:i][::-1]
            below = m[i:]
            above = above[:len(below)]
            below = below[:len(above)]
            if above == below:
                return i
        return 0


    def get_reflection_index(m) -> (int, bool):
        for i in range(len(m) - 1):
            if m[i] == m[i + 1]:
                if is_perfect(m, i):
                    return i + 1, False
        m = rotate(m)
        for i in range(len(m) - 1):
            if m[i] == m[i + 1]:
                if is_perfect(m, i):
                    return i + 1, True
        return 0, False

    def get_reflection_index_part2(m, prev) -> (int, bool):
        for i in range(len(m) - 1):
            if m[i] == m[i + 1]:
                if is_perfect(m, i) and (prev[0] != i+1 or prev[1]):
                    return i + 1, False
        m = rotate(m)
        for i in range(len(m) - 1):
            if m[i] == m[i + 1]:
                if is_perfect(m, i) and (prev[0] != i+1 or not prev[1]):
                    return i + 1, True
        return 0, False


    def get_fixed_index(m):
        for i in range(1, len(m)):
            above = m[:i][::-1]
            below = m[i:]
            above = above[:len(below)]
            below = below[:len(above)]

            diff_cnt = sum(sum((0 if ch_a == ch_b else 1 for ch_a, ch_b in zip(line_a, line_b))) for line_a, line_b in
                           zip(above, below))
            if diff_cnt == 1:
                return i

        return 0


    def get_fixed_size(mirror, prev):
        print('original mirror')
        print_mirror(mirror)
        fixed_size = get_fixed_index(mirror)
        c = list(zip(*mirror))
        fixed_size_cols = get_fixed_index(c)
        for i in range(len(mirror)):
            for j in range(len(mirror[i])):
                mirror_copy = copy.deepcopy(mirror)
                mirror_copy[i][j] = mirror_copy[i][j] ^ 1
                print(f'mirror copy with fixed  smudge  {i}:{j}')
                print_mirror(mirror_copy)
                s1 = get_reflection_index_part2(mirror_copy, prev)
                if s1[0] > 0 and s1 != prev:
                    print(f'smudge fixed with new hor. size {s1[0]} and is_vertical {s1[1]}')
                    # if (fixed_size != s1[0] and not s1[1]) or (fixed_size_cols != s1[0] and s1[1]):
                    #     print(f'properly fixed size = {fixed_size}')
                    #     print(f'properly fixed size cols = {fixed_size_cols}')
                    #     print(f'original mirror = {mirror}')
                    #     raise Exception("!!!!")
                    return s1[0] if s1[1] else(s1[0] * 100)
        return 0


    def rotate(mirror):
        row_size = len(mirror[0])
        rotated = []
        for i in range(row_size):
            rotated.append([])
        for row in mirror:
            for i in range(row_size):
                rotated[i].append(row[i])
        return rotated


    total = 0
    total_alt = 0
    counter_2 = 0
    new_counter_2 = 0
    for mirror in mirrors:
        # find horizontal reflection
        index1 = get_reflection_index(mirror)
        row = get_reflection_index_2(mirror)

        cols = list(zip(*mirror))
        col = get_reflection_index_2(cols)

        print(f'size1={index1[0]} vertical={index1[1]}')
        print(f'size alter ={row}')
        print(f'size col alter ={col}')

        total += index1[0] if index1[1] else index1[0] * 100
        total_alt += row * 100
        total_alt += col

        print('try fix mirror')
        size2 = get_fixed_size(mirror, index1)
        counter_2 += size2

        r = get_fixed_index(mirror)
        c = get_fixed_index(list(zip(*mirror)))
        new_counter_2 += r*100
        new_counter_2 += c

    print(f'{time.time() - start_time}s: {total}')
    print(f'{time.time() - start_time}s: {total_alt}')
    print(f'{time.time() - start_time}s: {counter_2}')
    print(f'{time.time() - start_time}s:!!! {new_counter_2}')
    f.close()
