lines = [l.strip().split(' ') for l in open("data/day_2.data", "r").readlines()]

# part 1
safe_counter = 0
for line in lines:
    left = int(line[0])
    right = int(line[1])
    sign = 1  # asc
    if left > right:
        sign = -1  # desc
    elif left == right:
        continue
    else:
        pass

    res = True
    right = int(line[0])
    for i in range(0, len(line) - 1):
        left = right
        right = int(line[i + 1])
        acc = (right - left) * sign
        if acc <= 0 or acc > 3:
            res = False
            break
    if res:
        safe_counter += 1

print(safe_counter)

# part 1 improved
lines = [[int(i) for i in line] for line in lines]
safe_counter = 0


def get_sign(left, right):
    if left > right:
        return -1  # desc
    elif left == right:
        return 0
    else:
        return 1  # asc


def is_level_diff_valid(left, right, sign):
    acc = (right - left) * sign
    if acc <= 0 or acc > 3:
        return False
    return True


def reports_array_valid(reports: list[int]) -> bool:
    right = reports[0]
    sign = get_sign(reports[0], reports[1])
    for i in range(0, len(reports) - 1):
        left = right
        right = reports[i + 1]
        s = get_sign(left, right)
        if not s == sign or not is_level_diff_valid(left, right, s):
            return False
    return True


for line in lines:
    if reports_array_valid(line):
        safe_counter += 1

print(safe_counter)

# part 2
lines = [[int(i) for i in line] for line in lines]
safe_counter = 0

for line in lines:
    if not reports_array_valid(line):
        tolerated = False
        try:
            for i in range(0, len(line)):
                candidate = line[:i] + line[i + 1:]
                if reports_array_valid(candidate):
                    tolerated = True
                    break
            if tolerated:
                safe_counter += 1
            else:
                pass
        except Exception as e:
            pass

    else:
        safe_counter += 1

print(safe_counter)
