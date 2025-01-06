from functools import cmp_to_key

rules = []
updates = []
with open("data/day_5.data", "r") as f:
    read_updates = False
    for line in f:
        l = line.strip()
        if l == "":
            read_updates = True
            continue
        if read_updates:
            updates.append(l.split(','))
        else:
            rules.append(l)


def process_rules(rules: [str]) -> dict[str, set[str]]:
    result: dict[str, set[str]] = dict()
    for rule in rules:
        [left, right] = rule.split('|')
        result.setdefault(left, set()).add(right)
    return result


rules_dict: dict[str, set[str]] = process_rules(rules)


def get_update_mid(update):
    return update[len(update) // 2]


def compare(x, y):
    if x in rules_dict.keys():
        if y in rules_dict[x]:
            return 1  # x definitely > y
        else:
            if y in rules_dict.keys():
                if x in rules_dict[y]:
                    return -1  # x definitely < y
                else:
                    return 0  # impossible branch
            else:
                return 0  # impossible branch
    else:
        if y in rules_dict.keys():
            if x in rules_dict[y]:
                return -1  # x definitely < y
            else:
                return 0  # impossible branch
        else:
            return 0  # impossible branch


def check_update(update, d) -> bool:
    for i in range(len(update) - 1):
        current = update[i]
        if current not in d.keys():
            return False
        for j in range(i + 1, len(update) - 1):
            item = update[j]
            if item not in d[current]:
                return False
    return True


# part 1
result = 0
for update in updates:
    if check_update(update, rules_dict):
        result += int(get_update_mid(update))
print(result)

# part 2
result = 0


def fix_update(update):
    return sorted(update, key=cmp_to_key(compare), reverse=True)


for update in updates:
    if not check_update(update, rules_dict):
        new_update = fix_update(update)
        result += int(get_update_mid(new_update))
print(result)
