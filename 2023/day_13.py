import re
import time

with open('data/day_13.data') as f:
    start_time = time.time()
    mirrors = []
    mirror = []
    cnt = 0
    for line in f:
        cnt+=1
        if len(line.strip())==0:
            mirrors.append(mirror.copy())
            mirror = []
        else:    
            mirror.append(line.strip())
    mirrors.append(mirror)

    print(mirrors)
    
    def is_perfect(mirror, i):
        m=i
        n=i+1
        while m>=0 and n<len(mirror):                 
            if mirror[m]!=mirror[n]:
                return False
            m-=1
            n+=1      
        return True      
    
    def get_reflection_size(mirror):
        for i in range(len(mirror)-1):
            if mirror[i]==mirror[i+1]: 
                if is_perfect(mirror, i):
                    return i+1    
        return 0                             
        
    counter=0
    for mirror in mirrors:
        #find horizontal reflection 
        counter+=100*get_reflection_size(mirror)    
        
            
        row_size = len(mirror[0])
        columns=['']*row_size
        for line in mirror:
            for i in range(row_size):
                columns[i] = columns[i] + line[i]
                    
        counter+=get_reflection_size(columns)   
                    
    print(f'{time.time() - start_time}s: {counter}')
    f.close()
