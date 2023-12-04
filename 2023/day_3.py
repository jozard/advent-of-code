import math
import re
lines = open("data/day_3.data", "r").readlines()


previous_line = dict(matches=[], symbols=[])
gears = {} # a gear is a dict: gear index -> list of numbers
gear_symbols = []
    
def process_above(symbol, match):
    rng = [*range(match.start()-1, match.end()+1, 1)]
    result = symbol.start() in rng
    if result==True:
        update_gear(symbol, int(match.group()))

def process(symbol, match):
    result = symbol.start()==match.start()-1 or symbol.start()==match.end()
    if result==True:
        update_gear(symbol, int(match.group())) 
    
        
def update_gear(symbol, value):
    if symbol in gear_symbols:
        gears[gear_symbols.index(symbol)].append(value)    
    else:
        gear_symbols.append(symbol)     
        gears[gear_symbols.index(symbol)] = [value]
                  
    
for line in lines:
    print(line)
    matches = list(re.finditer(r'\d+', line.strip()))
    symbols = list(re.finditer(r'\*', line.strip()))
    
    # add parts from previous line non-adjucent current line symbols
    for match in previous_line['matches']:
        for symbol in symbols:
            process_above(symbol, match)   
            
            
    previous_line['matches'] = []
    # move current line parts to previous matching them with previous and current line symbols
    for match in matches:
        for symbol in symbols:
            process(symbol, match)
        for symbol in previous_line['symbols']:
            process_above(symbol, match)    
        
    previous_line['symbols'] = symbols
    previous_line['matches'] = matches
    
gear_ratios = 0

for gear in gears.values():
    if len(gear) == 2:
        gear_ratios += gear[0]*gear[1] 
print(gear_ratios)
               

