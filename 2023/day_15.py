import collections
import re
import time

with open('data/day_15.data') as f:
    start_time = time.time()
    steps = [x for item in [line.split(',') for line in f.read().splitlines()] for x in item]


    def do_hash(str):
        result = 0
        for ch in str:
            result = ((ord(ch) + result) * 17) % 256
        return result


    total = sum([do_hash(step) for step in steps])

    boxes = {}

    for step in steps:
        label = re.search(r'[a-z]+', step).group()
        length = re.search(r'[0-9]+', step)
        length = None if length is None else int(length.group())
        box = do_hash(label)
        if length is None:
            if box in boxes and label in boxes[box]:
                boxes[box].pop(label)
        else:
            if box not in boxes:
                boxes[box] = collections.OrderedDict()
            boxes[box][label] = length

    power = sum(
        [sum([(box + 1) * (list(boxes[box].items()).index(item) + 1) * item[1] for item in boxes[box].items()]) for box
         in boxes.keys()])
    print(f'{time.time() - start_time}s: {total}, power = {power}')
    f.close()
