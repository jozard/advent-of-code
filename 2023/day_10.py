import re
import time

with open('data/day_10.data') as f:
    start_time = time.time()
    map = [list(line.rstrip()) for line in f]
    print(map)
    row = None
    col = None
    for line in map:
        if 'S' in line:
            row = map.index(line)
            col = line.index('S')
            break

    print(row)
    print(col)
    
    class Node:
        
        def __init__(self, x, y, input, output, char):
            self.x = x
            self.y = y
            self.input = input
            self.output = output
            self.char = char
            
        def __eq__(self, other):
            return self.x==other.x and self.y==other.y
        
        def __str__(self):
             return f'x: {self.x}, y: {self.y}, output: {self.output}, input: {self.input}, char: {self.char}'
                
    mapper = {'W': {'L': 'N', 'F': 'S', '-': 'W'},
              'N': {'7': 'W', 'F': 'E', '|': 'N'},
              'E': {'7': 'S', 'J': 'N', '-': 'E'},   
              'S': {'L': 'E', 'J': 'W', '|': 'S'}}
    
    def get_node(ex:Node, x, y, char):
        input = ex.output
        output = mapper[ex.output][char]                    
        return Node(x, y, input, output, char)
        
    
    def get_position(direction, x, y):
        if direction == 'N':
            return (x, y-1)
        elif direction == 'E':
            return (x+1, y)
        elif direction == 'S':
            return (x, y+1)
        else:
            return (x-1, y) # 'W'    
                    

    a = Node(col-1, row, 'E', 'W', map[col-1][row])
    b = Node(col+1, row, 'W', 'E', map[col+1][row])
    count = 1     
    
    while a != b:
        (x,y) = get_position(a.output, a.x, a.y)
        a = get_node(a, x, y, map[y][x])
        print(f'next a = {a}')
        (x,y) = get_position(b.output, b.x, b.y)
        b = get_node(b, x, y, map[y][x])
        print(f'next b = {b}')
        count+=1



    print(f'{time.time() - start_time}s: {count}, {a} {b}')
    f.close()
