import re

lines = open("data/day_1.data", "r").readlines()

calibration_sum = 0
number_to_digit = {'one': 1, 'two': 2, 'three': 3, 'four': 4, 'five': 5, 'six': 6, 'seven': 7, 'eight': 8, 'nine': 9}


def mapper(s):
    result = number_to_digit.get(s)
    if result is None:
        return int(s)
    return result


pattern = '|'.join(key for key in number_to_digit.keys())
inverted_pattern = '|'.join(''.join(reversed(key)) for key in number_to_digit.keys())

for line in lines:
    first_match = re.search(r'(\d{1}|' + pattern + ')', line)
    first_digit = mapper(first_match.group(0))
    
    last_match = re.search(r'(\d{1}|' + inverted_pattern + ')', ''.join(reversed(line)))
    last_digit = mapper(''.join(reversed(last_match.group(0))))
   
    calibration_sum += first_digit * 10 + last_digit

print(calibration_sum)