lines = open("data/day_1.data", "r").readlines()

# part 1
left, right = zip(*[item.strip().split('   ') for item in lines])
left = list(left)
left.sort()
right = list(right)
right.sort()
result = 0
for i in range(len(left)):
    result += abs(int(left[i]) - int(right[i]))
print(result)

# part 2

similarities = {}
result = 0
for item in right:
    similarities[item] = similarities.get(item, 0) + 1
for item in left:
    result += int(item) * similarities.get(item, 0)
print(result)
