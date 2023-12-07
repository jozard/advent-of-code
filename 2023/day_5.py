import itertools, sys
print(groups)

# seeds = [int(num) for num in groups[0].partition('seeds: ')[2].split(' ')] -- part 1
s = [int(num) for num in groups[0].partition('seeds: ')[2].split(' ')]
seed_ranges = []
x = 0
while x < len(s):
    seed_ranges.append(range(s[x], s[x] + s[x + 1]))
    x += 2

print(s)
print(seed_ranges)
print('-------------')


def map_to_rng(map):
    # source range, target_range, shift source to target:  5, 23, 11 -> (23, 34), (5, 16), 18. Converting src to dst shift should be added
    return [range(map[1], map[1] + map[2]), range(map[0], map[0] + map[2]), map[0] - map[1]]


seed_2_soil = [map_to_rng([int(num) for num in line.split(' ')]) for line in
               groups[1].partition('seed-to-soil map:\n')[2].split('\n')]
soil_2_fert = [map_to_rng([int(num) for num in line.split(' ')]) for line in
               groups[2].partition('soil-to-fertilizer map:\n')[2].split('\n')]
fert_2_water = [map_to_rng([int(num) for num in line.split(' ')]) for line in
                groups[3].partition('fertilizer-to-water map:\n')[2].split('\n')]
water_2_light = [map_to_rng([int(num) for num in line.split(' ')]) for line in
                 groups[4].partition('water-to-light map:\n')[2].split('\n')]
light_2_temp = [map_to_rng([int(num) for num in line.split(' ')]) for line in
                groups[5].partition('light-to-temperature map:\n')[2].split('\n')]
temp_2_humid = [map_to_rng([int(num) for num in line.split(' ')]) for line in
                groups[6].partition('temperature-to-humidity map:\n')[2].split('\n')]
humid_2_location = [map_to_rng([int(num) for num in line.split(' ')]) for line in
                    groups[7].partition('humidity-to-location map:\n')[2].strip().split('\n')]


def correspond_desc(src, rng):
    if src in rng[0]:
        return src + rng[2]
    return None


def correspond_asc(src, rng):
    if src in rng[1]:
        return src - rng[2]
    return None


def descend(src, ranges):
    for rng in ranges:
        dst = correspond_desc(src, rng)
        if dst is not None:
            return dst
    return src


def ascend(src, ranges):
    for rng in ranges:
        dst = correspond_asc(src, rng)
        if dst is not None:
            return dst
    return src


print('start')
# lowest_location = sys.maxsize
# for seed_range in seed_ranges:
#     sr_index+=1
#     print(f'processing seed range {sr_index} from {seed_range[0]} to {seed_range[1]}')
#     for seed in range(seed_range[0], seed_range[1]):
#         print(f'processing {seed}', end='\r')
#         soil = descend(seed, seed_2_soil)
#         fert = descend(soil, soil_2_fert)
#         water = descend(fert, fert_2_water)
#         light = descend(water, water_2_light)
#         temp = descend(light, light_2_temp)
#         humid = descend(temp, temp_2_humid)
#         location = descend(humid, humid_2_location)
#         lowest_location = location if lowest_location > location else lowest_location

lowest_location = None
for location in range(46, 389056265):
    print(f'processing location {location}', end='\r')
    humid = ascend(location, humid_2_location)
    temp = ascend(humid, temp_2_humid)
    light = ascend(temp, light_2_temp)
    water = ascend(light, water_2_light)
    fert = ascend(water, fert_2_water)
    soil = ascend(fert, soil_2_fert)
    seed = ascend(soil, seed_2_soil)
    for seed_range in seed_ranges:
        if seed in seed_range:
            lowest_location = location
            break
    if lowest_location is not None:
        break

print()
print(lowest_location)
