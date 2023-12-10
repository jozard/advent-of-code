import re
import time
from functools import reduce

with open('data/day_8.data') as f:
    start_time = time.time()
    lines = [line.rstrip() for line in f]
    instructions = [char for char in lines[0]]
    print(instructions)

    nodes = {}
    for i in range(2, len(lines)):
        node = lines[i].partition(' = ')
        nodes[node[0]] = re.findall(r'[A-Z0-9]{3}', node[2])
    print(nodes)
    
    # key = 'AAA'
#     node = nodes[key]
#     step = 0
#     while key != 'ZZZ':
#         # print(f'current node {node}')
#
#         instruction = instructions[step % len(instructions)]
#         # print(f'go {instruction}')
#         key = node[0] if instruction == 'L' else node[1]
#         # print(f'key = {key}')
#         node = nodes[key]
#         step += 1
#
#     print(f'{time.time      () - start_time}s: {step}')

    print('----------')
    start_time = time.time()
    start_keys =[key for key in nodes.keys() if key.endswith('A')] # find keys ending with A
    print(start_keys)

    def factorize(num):
        result={}
        i = 2
        while True:
            (k,l) = divmod(num, i)
            if l==0:
                result[i] = result[i]+1 if i in result else 1
                if k == 1:
                    return result
                num=k    
            else:
                i+=1
                
    factors = []
    for start_key in start_keys:
        exit_keys={} # here we store found exit keys for this key key->{}
        node = nodes[start_key]
        step = 0
        print(f'testing start_key {start_key} flow')
        while True:
            instruction = instructions[step % len(instructions)]
            step+=1
            # print(f'step {step} go {instruction}')
            key = node[0] if instruction == 'L' else node[1]
            # print(f'reached key = {key}')
            if key.endswith('Z'): # we found an exit key
                if len(exit_keys) == 0:
                    # add the first exit key to exit_keys. We will consider it to be the loop start
                    exit_keys[key] = {'from_start': step, 'loop_len': 0} # how many steps it takes to reach the exit key from the start
                elif key in exit_keys:
                    print(f'step is {step}')
                    print(f'loop start item = {exit_keys.get(key)}')
                    exit_keys.get(key)['loop_len'] = (step - exit_keys.get(key).get('from_start'))
                    print(exit_keys)
                    factors.append(factorize(exit_keys.get(key).get('from_start')))
                    print()
                    break
                
            node=nodes[key]       
            print(f'step {step} exit_keys {exit_keys}', end='\r')

    print(factors)
    result_factors = {}
    for item in factors:
        for key in item.keys():
            if key in result_factors:
                if result_factors[key]<item[key]:
                    result_factors[key] = item[key]
            else:
                result_factors[key] = item[key]
    
    print(result_factors)     
    print(reduce((lambda x, y: x * y), result_factors.keys()))                
    
    f.close()
