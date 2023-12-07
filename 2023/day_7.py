import time
import functools

start_time = time.time()
f = open("data/day_7.data", "r")
# cards = ('A','K','Q','J','T','9','8','7','6','5','4','3','2')
cards = ('A','K','Q','T','9','8','7','6','5','4','3','2','J') # part 2
items = []
for line in f.readlines():
    s = line.split()
    items.append((s[0], int(s[1])))


def compare_cards(left, right):
    if cards.index(left) < cards.index(right):
        return 1
    elif cards.index(left) > cards.index(right):
        return -1
    return 0    

def get_hand_type(hand):
    s_dict = { char: hand.count(char) for char in hand }
    if len(s_dict)==1: # 5 of a kind
        return 7
    if len(s_dict)==2: # 4 of a kind or full house
        if 4 in s_dict.values():
            return 6
        else:
            return 5
    if len(s_dict)==3: # 3 of a kind or 2 pairs
        if 3 in s_dict.values():
            return 4
        else:
            return 3
    if len(s_dict)==4:
        return 2
    return 1
    
def get_hand_type_p2(hand):
    s_dict = { char: hand.count(char) for char in hand }
    if len(s_dict)==1: # 5 of a kind
        return 7
    if len(s_dict)==2: # 4 of a kind or full house
        if 'J' in s_dict.keys(): # joker(s) in any position make it 5 of a kind
            return 7
        if 4 in s_dict.values():   
            return 6 # 4 of a kind
        return 5
            
    if len(s_dict)==3: # 3 of a kind or 2 pairs
        if 3 in s_dict.values():
            if 'J' in s_dict.keys(): # if it is in any position - 4 of a kind
                return 6        
            return 4 # 3 of a kind
        else: # 2 pairs
            if 'J' in s_dict.keys():
                if s_dict['J']==1: # single joker makes it full house
                    return 5
                else: # a pair of jokers makes it 4 of a kind
                    return 6    
    
            return 3 # 2 pairs 
            
    if len(s_dict)==4:
        if 'J' in s_dict.keys(): # joker out of or in pair makes it 3 of a kind
            return 4
        
        return 2

    if ('J' in s_dict.keys()): # joker at any position makes it a pair
        return 2
            
    return 1    
    


def compare_items(left, right):
    left_type = get_hand_type_p2(left[0])
    right_type = get_hand_type_p2(right[0])
    if left_type > right_type:
        return 1
    elif right_type > left_type:
        return -1
    else:
        for i in range(0,5):
            c = compare_cards(left[0][i], right[0][i])
            if c != 0:
                return c
    return 0

    
hands = sorted(items, key=functools.cmp_to_key(compare_items))
    
result = hands[0][1]
for i in range(1,len(hands)):
    result += hands[i][1]*(i+1)
              
print(f'{time.time      () - start_time}s: {result}')
f.close()