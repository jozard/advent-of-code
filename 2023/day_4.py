import math
import re
lines = open("data/day_4.data", "r").readlines()
points = 0
for line in lines:
    print(line)
    item = line.partition(':')[2].strip().partition(' | ')
    card = {}
    print(item)
    card['wins'] = [int(num) for num in item[0].strip().split(' ') if len(num)>0]
    card['numbers'] = [int(num) for num in item[2].strip().split(' ') if len(num)>0]
    print(card)
    acc = 0
    for win in card['wins']:
       if (win in card['numbers']):
           acc = 1 if acc == 0 else acc*2
    points += acc
    
print(points)        
    
    
    