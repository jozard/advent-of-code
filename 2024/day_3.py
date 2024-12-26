import re
from functools import reduce


def get_result(arg: str):
    mul_sum = 0
    re_result = re.findall(r'mul\((\d{1,3}),(\d{1,3})\)', arg)
    for item in re_result:
        mul_sum += int(item[0]) * int(item[1])
    return mul_sum


lines = [l.strip() for l in open("data/day_3.data", "r").readlines()]

# part 1
result = reduce(lambda x, y: x + get_result(y), lines, 0)
print(result)
