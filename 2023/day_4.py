import math
import re
lines = open("data/day_4.data", "r").readlines()
points = 0
scratch_cards = [1] * len(lines)
line_index = 1
for line in lines:
    print(line)
    item = line.partition(':')[2].strip().partition(' | ')
    card = {}
    print(item)
    card['wins'] = [int(num) for num in item[0].strip().split(' ') if len(num)>0]
    card['numbers'] = [int(num) for num in item[2].strip().split(' ') if len(num)>0]
    print(card)
    acc = 0
    matches = 0
    for win in card['wins']:
       if (win in card['numbers']):
           acc = 1 if acc == 0 else acc*2
           # part 2 only counts matches
           matches+=1
           
    points += acc
    if matches>0:
        for x in range(1, matches+1):
            scratch_cards[line_index-1+x] = scratch_cards[line_index-1+x] + scratch_cards[line_index-1]
    
    line_index+=1
    
    
print(points)     
print(sum(scratch_cards))   