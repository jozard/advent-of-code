import re
import time

with open('data/day_9.data') as f:
    start_time = time.time()
    lines = [[int(char) for char in line.rstrip().split()] for line in f]
    
    sublines = []
    for line in lines:
        table = [line]
        next_line = line
        while any([i!=0 for i in next_line]):
            next_line = [next_line[i]-next_line[i-1] for i in range(1, len(next_line))]
            table.append(next_line)
        sublines.append(table)
    print(sublines)  
    
    result = 0
    result_2 = 0
    for item in sublines:
        item_result = 0
        item_result_2 = 0
        prev = 0
        for i in reversed(range(0, len(item)-1)):
            print(item[i])
            last_num = item[i][len(item[i])-1]
            first_num = item[i][0]
            item_result += last_num
            
            prev = first_num-prev
            item_result_2 = prev
            i-=1
 
        result += item_result 
        result_2 += item_result_2   

    print(f'{time.time() - start_time}s: {result}, {result_2}')
    f.close()
