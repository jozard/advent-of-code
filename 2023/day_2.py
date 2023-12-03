import math
lines = open("data/day_2.data", "r").readlines()

id_sum = 0

# part one cube count limits
bag_items = {'red': 12, 'green': 13, 'blue': 14}

def verify_cube_set(cube_set):
    count = None
    s = None
    colour = None
    (count,s,colour) = cube_set.strip().partition(' ')
    return bag_items.get(colour) >= int(count)
    
def get_cube_set_colour_count(cube_set):
    count = None
    s = None
    colour = None
    (count,s,colour) = cube_set.strip().partition(' ')
    return (colour,count)    

def verify_handful(handful):
    if all(verify_cube_set(cube_set) for cube_set in handful.split(', ')):
        return True

def get_handful_dict(handful):
    return {item[0]: int(item[1]) for item in list(map(get_cube_set_colour_count, handful.split(', ')))}

for line in lines:
    prefix = None
    separator = None
    game_content = None
    (prefix,separator,game_content) = line.strip().partition(': ')
   
    handfuls = game_content.split('; ')
    
    #part one
    # if all(verify_handful(handful) for handful in handfuls):
        # id_sum += lines.index(line)+1
        
    game_dict = {'red':1,'blue':1,'green':1}    
    for handful in handfuls:
        handful_dict = get_handful_dict(handful)
        for handful_item in handful_dict.items():
            if handful_item[1] > game_dict[handful_item[0]]:
                game_dict[handful_item[0]]=handful_item[1]
    id_sum+= math.prod(game_dict.values())        
    

print(id_sum)