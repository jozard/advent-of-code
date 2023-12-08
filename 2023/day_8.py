import re

with open('day_8.data') as f:
    lines = [line.rstrip() for line in f]
    instructions = [char for char in lines[0]]
    print(instructions)

    nodes = {}
    for i in range(2, len(lines)):
        node = lines[i].partition(' = ')
        nodes[node[0]] = re.findall(r'[A-Z]{3}', node[2])
    print(nodes)
    key = 'AAA'
    node = nodes[key]
    step = 0
    while key != 'ZZZ':
        print(f'current node {node}')

        instruction = instructions[step % len(instructions)]
        print(f'go {instruction}')
        key = node[0] if instruction == 'L' else node[1]
        print(f'key = {key}')
        node = nodes[key]
        step += 1
    print(f'result {step}')
