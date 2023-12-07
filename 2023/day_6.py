import time

start_time = time.time()
f = open("data/day_6.data", "r")
times = [int(item) for item in f.readline().partition(':')[2].strip().split()]
distances = [int(item) for item in f.readline().partition(':')[2].strip().split()]

print(times)
print(distances)

result = 1;
for i in range(0, len(times)):
    t = times[i]
    ways_count = 0
    for hold in range(1, t):
        distance = (t - hold)*hold
        if distance > distances[i]:
            ways_count+=1
    if ways_count > 0:
        result*=ways_count
        
d = time.time() - start_time        
print(f'{d}s: {result}')
f.close()
# Part II
start_time = time.time()
f = open("data/day_6.data", "r")
t = int(''.join(f.readline().partition(':')[2].strip().split()))
distance = int(''.join(f.readline().partition(':')[2].strip().split()))

print(t)
print(distance)

ways_count = 0
for hold in range(1, t):
    d = (t - hold)*hold
    if d > distance:
        ways_count+=1

delta = time.time() - start_time
print(f'{delta}s: {ways_count}')
f.close()
