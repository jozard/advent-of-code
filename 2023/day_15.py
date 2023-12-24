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

    print(f'{time.time() - start_time}s: {total}')
    f.close()
