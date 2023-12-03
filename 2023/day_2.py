lines = open("data/day_2.data", "r").readlines()

id_sum = 0

bag_items = {'red': 12, 'green': 13, 'blue': 14}

def verify_cube_set(cube_set):
    count = None
    s = None
    colour = None
    (count,s,colour) = cube_set.strip().partition(' ')
    return bag_items.get(colour) >= int(count)

def verify_handful(handful):
    if all(verify_cube_set(cube_set) for cube_set in handful.split(', ')):
        return True

for line in lines:
    prefix = None
    separator = None
    game_content = None
    (prefix,separator,game_content) = line.strip().partition(': ')
   
    handfuls = game_content.split('; ')
    if all(verify_handful(handful) for handful in handfuls): 
        id_sum += lines.index(line)+1
    

print(id_sum)