# row, column
from argparse import ArgumentError

visited_counted: dict[tuple, int] = dict()

lab_map: list[str] = list()
with open("data/day_6.data", "r") as f:
    for line in f:
        l = line.strip()
        lab_map.append(l)

arrow = (0, 0)
for j, line in enumerate(lab_map):
    i = line.find('^')
    if i >= 0:
        arrow = (j, i)
        break
print(arrow)


def move(s, d):
    if "nsew".find(d) == -1:
        raise ArgumentError
    return (s[0] - 1, s[1]) if d == 'n' else ((s[0] + 1, s[1]) if d == 's' else (
        (s[0], s[1] + 1) if d == 'e' else (s[0], s[1] - 1)))


def change_direction(d):
    if "nsew".find(d) == -1:
        raise ArgumentError
    return 'e' if d == 'n' else ('w' if d == 's' else ('s' if d == 'e' else 'n'))


def find_next(s, d, m):
    n = move(s, d)
    while m[n[0]][n[1]] == '#' or m[n[0]][n[1]] == '0':
        d = change_direction(d)
        n = move(s, d)
    return n, d


def is_last(current, d) -> bool:
    nxt = move(current, d)
    return 0 > nxt[0] or nxt[0] >= len(lab_map) or 0 > nxt[1] or nxt[1] >= len(lab_map[0])


# part 1
result = 0
start = tuple(arrow)

direction = 'n'  # e,s,w
visited_counted[start] = 1
while True:
    if is_last(start, direction):
        break
    start, direction = find_next(start, direction, lab_map)
    visited_counted[start] = 1 if not start in visited_counted else visited_counted[start] + 1

result = len(visited_counted.values())
print(result)

# part 2
test_positions = list(visited_counted.keys())
test_positions.remove(arrow)

result = 0
visited_directions: dict[tuple, set[str]]
for pos in test_positions:
    visited_directions = dict()  # this dictionary will contain set of directions
    m = list(lab_map)  # copy the original map to m
    s = list(m[pos[0]])  # get a row in m matching pos row
    s[pos[1]] = '0'  # put an obstacle to the pos position in the m
    m[pos[0]] = "".join(s)  # replace row in m with a row with obstacle
    start = tuple(arrow)

    direction = 'n'  # e,s,w
    start_set = set(direction)
    visited_directions[start] = start_set

    while True:
        if is_last(start, direction):  # if the next move is out of map, current obstacle will not result into a loop
            break
        start, direction = find_next(start, direction, m)
        if not start in visited_directions:
            visited_directions[start] = set(direction)
        else:
            vd = visited_directions[start]  # get visited directions at start position
            if direction in vd:  # this position has already been visited from the same direction -> looped
                result = result + 1
                break
            else:
                visited_directions[start].add(direction)

print(result)
