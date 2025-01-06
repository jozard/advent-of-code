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


def check_update(update, d) -> bool:
    for i in range(len(update) - 1):
        current = update[i]
        for j in range(i + 1, len(update) - 1):
            item = update[j]
            if item not in d[current]:
                return False
    return True


# part 1
result = 0
# weights = build_weights()
for update in updates:
    if check_update(update, rules_dict):
        result += int(get_update_mid(update))
print(result)
