# row, column
from argparse import ArgumentError

visited_counted: dict[tuple, int] = dict()

lab_map: list[str] = list()
with open("data/day_6.data", "r") as f:
    for line in f:
        l = line.strip()
        lab_map.append(l)

start = (0, 0)
for j, line in enumerate(lab_map):
    i = line.find('^')
    if i >= 0:
        start = (j, i)
        break
print(start)


def move(s, d):
    if "nsew".find(d) == -1:
        raise ArgumentError
    return (s[0] - 1, s[1]) if d == 'n' else ((s[0] + 1, s[1]) if d == 's' else (
        (s[0], s[1] + 1) if d == 'e' else (s[0], s[1] - 1)))


def change_direction(d):
    if "nsew".find(d) == -1:
        raise ArgumentError
    return 'e' if d == 'n' else ('w' if d == 's' else ('s' if d == 'e' else 'n'))


# part 1
result = 0
# print(lab_map)
direction = 'n'  # e,s,w
visited_counted[start] = 1


def find_next(s, d):
    n = move(s, d)
    while lab_map[n[0]][n[1]] == '#':
        d = change_direction(d)
        n = move(s, d)
    return n, d


def is_last(current, d) -> bool:
    nxt = move(start, direction)
    return 0 > nxt[0] or nxt[0] >= len(lab_map) or 0 > nxt[1] or nxt[1] >= len(lab_map[0])


while True:
    if is_last(start, direction):
        break
    start, direction = find_next(start, direction)
    visited_counted[start] = 1 if not start in visited_counted else visited_counted[start] + 1

result = len(visited_counted.values())
print(result)

# part 2
