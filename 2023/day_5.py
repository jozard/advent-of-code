import itertools, sys
groups = open("data/day_5.data", "r").read().split('\n\n')

print(groups)

#seeds = [int(num) for num in groups[0].partition('seeds: ')[2].split(' ')] -- part 1
s = [int(num) for num in groups[0].partition('seeds: ')[2].split(' ')]
seed_ranges = []
x = 0
while x < len(s):
    seed_ranges.append((s[x], s[x]+s[x+1]))
    x+=2
    
print(s)    
print(seed_ranges)
print('-------------')

seed_2_soil = [[int(num) for num in line.split(' ')] for line in groups[1].partition('seed-to-soil map:\n')[2].split('\n')]
soil_2_fert = [[int(num) for num in line.split(' ')] for line in groups[2].partition('soil-to-fertilizer map:\n')[2].split('\n')]
fert_2_water = [[int(num) for num in line.split(' ')] for line in groups[3].partition('fertilizer-to-water map:\n')[2].split('\n')]
water_2_light = [[int(num) for num in line.split(' ')] for line in groups[4].partition('water-to-light map:\n')[2].split('\n')]
light_2_temp = [[int(num) for num in line.split(' ')] for line in groups[5].partition('light-to-temperature map:\n')[2].split('\n')]
temp_2_humid = [[int(num) for num in line.split(' ')] for line in groups[6].partition('temperature-to-humidity map:\n')[2].split('\n')]
humid_2_location = [[int(num) for num in line.split(' ')] for line in groups[7].partition('humidity-to-location map:\n')[2].split('\n')]

def correspond(src, maps):
    result = src
    for map in maps:
        if src >= map[1] and src< map[1] + map[2]:
            delta = src-map[1]
            result = map[0] + delta
            break
    return result
print('start')
lowest_location = sys.maxsize
sr_index = 0
for seed_range in seed_ranges:
    sr_index+=1
    print(f'processing seed range {sr_index} from {seed_range[0]} to {seed_range[1]}')
    for seed in range(seed_range[0], seed_range[1]):
        print(f'processing {seed}', end='\r')
        soil = correspond(seed, seed_2_soil) 
        fert = correspond(soil, soil_2_fert) 
        water = correspond(fert, fert_2_water) 
        light = correspond(water, water_2_light) 
        temp = correspond(light, light_2_temp) 
        humid = correspond(temp, temp_2_humid) 
        location = correspond(humid, humid_2_location) 
        lowest_location = location if lowest_location > location else lowest_location
print()   
print(lowest_location)
