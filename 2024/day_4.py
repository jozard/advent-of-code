from file_utils import to_string_list

chars: [str] = to_string_list("data/day_4.data")


def get_target_coordinates(target, chars):
    coordinates = []
    for i in range(0, len(chars)):
        for j in range(0, len(chars[i])):
            if chars[i][j] == target:
                coordinates.append((i, j))
    return coordinates


def check_mas_coordinates(coordinates: list[tuple], chars):
    return 1 if chars[coordinates[0][0]][coordinates[0][1]] == "M" and chars[coordinates[1][0]][coordinates[1][1]] == "A" and \
                chars[coordinates[2][0]][coordinates[2][1]] == "S" else 0


def check_ms_coordinates(lt: tuple, rt: tuple, lb: tuple, rb: tuple, chars):
    if chars[lt[0]][lt[1]] == "M":
        return 1 if (chars[rt[0]][rt[1]] == "M" and chars[lb[0]][lb[1]] == "S" and chars[rb[0]][rb[1]] == "S") or (
                chars[rt[0]][rt[1]] == "S" and chars[lb[0]][lb[1]] == "M" and chars[rb[0]][rb[1]] == "S") else 0
    elif chars[lt[0]][lt[1]] == "S":
        return 1 if (chars[rt[0]][rt[1]] == "S" and chars[lb[0]][lb[1]] == "M" and chars[rb[0]][rb[1]] == "M") or (
                chars[rt[0]][rt[1]] == "M" and chars[lb[0]][lb[1]] == "S" and chars[rb[0]][rb[1]] == "M") else 0
    return 0


def count_xmases(x_coordinate, chars):
    row = x_coordinate[0]
    col = x_coordinate[1]
    cnt = 0
    if col < len(chars[row]) - 3:
        mas_coordinates_right = [(row, col + 1), (row, col + 2), (row, col + 3)]
        cnt += check_mas_coordinates(mas_coordinates_right, chars)
    if col >= 3:
        mas_coordinates_left = [(row, col - 1), (row, col - 2), (row, col - 3)]
        cnt += check_mas_coordinates(mas_coordinates_left, chars)
    if row >= 3:
        mas_coordinates_up = [(row - 1, col), (row - 2, col), (row - 3, col)]
        cnt += check_mas_coordinates(mas_coordinates_up, chars)
    if row < len(chars) - 3:
        mas_coordinates_down = [(row + 1, col), (row + 2, col), (row + 3, col)]
        cnt += check_mas_coordinates(mas_coordinates_down, chars)
    # check diagonals
    if col < len(chars[row]) - 3 and row >= 3:
        mas_coordinates_top_right = [(row - 1, col + 1), (row - 2, col + 2), (row - 3, col + 3)]
        cnt += check_mas_coordinates(mas_coordinates_top_right, chars)
    if col >= 3 and row >= 3:
        mas_coordinates_top_left = [(row - 1, col - 1), (row - 2, col - 2), (row - 3, col - 3)]
        cnt += check_mas_coordinates(mas_coordinates_top_left, chars)
    if row < len(chars) - 3 and col >= 3:
        mas_coordinates_bottom_left = [(row + 1, col - 1), (row + 2, col - 2), (row + 3, col - 3)]
        cnt += check_mas_coordinates(mas_coordinates_bottom_left, chars)
    if row < len(chars) - 3 and col < len(chars[row]) - 3:
        mas_coordinates_bottom_right = [(row + 1, col + 1), (row + 2, col + 2), (row + 3, col + 3)]
        cnt += check_mas_coordinates(mas_coordinates_bottom_right, chars)
    return cnt


def count_mases(a_coordinate, chars):
    row = a_coordinate[0]
    col = a_coordinate[1]
    cnt = 0
    if 0 < row < len(chars[row]) - 1 and 0 < col < len(chars) - 1:
        # check MAS diagonals
        cnt += check_ms_coordinates((row - 1, col - 1), (row - 1, col + 1), (row + 1, col - 1), (row + 1, col + 1), chars)
    return cnt


# part 1
xes = get_target_coordinates('X', chars)
result = 0
for x_coord in xes:
    result += count_xmases(x_coord, chars)

print(result)

# part 2
ases = get_target_coordinates('A', chars)
result = 0
for a_coord in ases:
    result += count_mases(a_coord, chars)

print(result)
