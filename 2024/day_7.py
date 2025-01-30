# row, column
import time

data_map: dict[int, list[int]] = dict()


def parse_equation(l):
    parts = l.split(": ")
    return int(parts[0]), [int(item) for item in parts[1].split(" ")]


with open("data/day_7.data", "r") as f:
    for line in f:
        test_value, data = parse_equation(line.strip())
        data_map[test_value] = data


def apply(operator, acc, item):
    if '*' == operator:
        return acc * item
    elif '+' == operator:
        return acc + item
    elif '||' == operator:
        return acc * pow(10, len(str(item))) + item
    else:
        raise RuntimeError


def f(acc, data) -> tuple:
    if acc is None:
        return f(data[0], data[1:])
    acc_1 = apply('*', acc, data[0])
    acc_2 = apply('+', acc, data[0])
    return (acc_1, acc_2) if len(data) == 1 else f(acc_1, data[1:]) + f(acc_2, data[1:])


def f2(acc, data, target) -> tuple:
    if acc is None:
        return f2(data[0], data[1:], target)
    elif acc > target:
        return ()
    acc_1 = apply('*', acc, data[0])
    acc_2 = apply('+', acc, data[0])
    acc_3 = apply('||', acc, data[0])
    return (acc_1, acc_2, acc_3) if len(data) == 1 else f2(acc_1, data[1:], target) + f2(acc_2, data[1:], target) + f2(acc_3,
                                                                                                                       data[1:],
                                                                                                                       target)


# part 1
t = time.time()
result = 0
for test_value, data in data_map.items():
    result = result + (test_value if test_value in f(acc=None, data=data) else 0)

dt: time = time.time() - t
print('{} {}ms'.format(result, dt))

# part 2
t = time.time()
result_2 = 0
for test_value, data in data_map.items():
    result_2 = result_2 + (test_value if test_value in f2(acc=None, data=data, target=test_value) else 0)

dt: time = time.time() - t
print('{} {}'.format(result_2, dt))
