import re
import time

with open('data/day_10.data') as f:
    start_time = time.time()
    map = [char for char in line.rstrip().split()] for line in f]
    start = { x:0, y:0)
    for line in map:
        if 'S' in line:
            start['x'] = map.indexof(line)
            start['y'] = line.indexof('S')
            break
            
    a_x = start.get('x')-2
    a_y = start.get('y')
    b_x = start.get('x')+2
    b_y = start.get('y')
    aa = '-'
    bb = '-'
    a = map[a_x][a_y] # we know where to go from the input data  
    b = map[b_x][b_y]
    count = 1

    
    def direction(prev, curr):
        if prev == '-':
            if curr == 
        
    
    while a_x != b_x and a_y != b_y:
        match a:
                case 'J':
                    a_x-=1
                case 'b':
                    return 2
                case _:
                    return 0
        count+=1

    

    print(f'{time.time() - start_time}s: {result}, {result_2}')
    f.close()
