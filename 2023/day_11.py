import re
import time

with open('data/day_11.data') as f:
    start_time = time.time()
    space = []
    for line in f:
        space.append(line.strip())
        
    empty_rows = []
    for i in range(len(space)):
        if not '#' in space[i]:
            empty_rows.append(i)
            
    empty_cols = []        
    for i in range(len(space[0])): # num of columns
        col = [row[i] for row in space]
        if not '#' in col:
            empty_cols.append(i)      
            
    print(f'empty columns = {empty_cols}')        
    print(f'empty rows = {empty_rows}')        
    galaxies = []
    for i in range(len(space)):
        for j in range(len(space[i])):
            if space[i][j]=='#':
                above = sum(item<i for item in empty_rows)
                before = sum(item<j for item in empty_cols)
                galaxies.append((i+999999*above,j+999999*before))                            
                            
    print(galaxies)
    s = 0
    for a in range(len(galaxies)):
        for b in range(a+1, len(galaxies)):
            m = galaxies[a]
            n = galaxies[b]           
            s += abs(m[1]-n[1]) + abs(m[0]-n[0])

    print(f'{time.time() - start_time}s: {s}')
    f.close()
