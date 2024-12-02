lines = [l.strip().split(' ') for l in open("data/day_2.data", "r").readlines()]

# part 1
safe_counter = 0
for line in lines:
    left = int(line[0])
    right = int(line[1])
    sign = 1 # asc
    if left > right:
        sign = -1 # desc
    elif left == right:
        continue
    else:
        pass

    res = True
    right = int(line[0])
    for i in range(0, len(line)-1):
        left = right
        right = int(line[i+1])
        acc = (right - left) * sign
        if acc <= 0 or acc > 3:
            res = False
            break
    if res:
        safe_counter += 1

print(safe_counter)

# part 2
