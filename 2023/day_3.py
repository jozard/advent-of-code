import math
import re
lines = open("data/day_3.data", "r").readlines()

part_number_sum = 0

previous_line = dict(matches=[],symbols=[])

def is_adjucent(symbol, match):
    rng = [*range(match.start()-1, match.end()+1, 1)]
    print(f'symbol start= {symbol.start()}, match.start()-1 = {match.start()-1}, match.end() = {match.end()}, range={rng}')
    result = symbol.start() in rng
    return result
    
    
for line in lines:
    print(line)
    matches = list(re.finditer(r'\d+', line.strip()))
    symbols = list(re.finditer(r'[^.^0-9]', line.strip()))
    
    # add parts from previous line non-adjucent current line symbols
    for match in previous_line['matches']:
        print(f'checking {match} from previous line')
        if any(is_adjucent(symbol, match) for symbol in symbols):
            arg = list(symbols)
            print(f'match group {match.group()} is adjuscent to some from {arg} add it to sum')
            part_number_sum += int(match.group())
            
    previous_line['matches'] = []
    print(f'clear previous matches')
    # move current line parts to previous matching them with previous and current line symbols
    for match in matches:
        print(f'checking {match} from current line')
        if any((symbol.start()==match.start()-1 or symbol.start()==match.end()) for symbol in symbols):
            arg = list(symbols)
            print(f'match group {match.group()} is adjuscent to some from current symbols {arg} add it to sum')
            part_number_sum += int(match.group())
        elif any(symbol.start() in [*range(match.start()-1, match.end()+1)] for symbol in previous_line['symbols']):
            arg = previous_line['symbols']
            print(f'match group {match.group()} is adjuscent to some from previous symbols {arg} add it to sum')
            part_number_sum += int(match.group()) 
        else:
            print(f'add match {match} to previous matches to check later')        
            previous_line['matches'].append(match)
            
        arg = previous_line['matches']
        print(f'previous matches updated: {arg}')        
    previous_line['symbols'] = symbols
    print(f'previous symbols updated: {symbols}')
               

print(part_number_sum)