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
    
    def cnt(mirror, coef):
        cnt = 0
        for i in range(len(mirror)-1):
            if mirror[i]==mirror[i+1]:
                m=i
                n=i+1
                perfect = True
                while m>=0 and n<len(mirror):                 
                    if mirror[m]!=mirror[n]:
                        perfect = False
                        break
                    m-=1
                    n+=1
                if perfect:
                    cnt+=coef*(i+1)    
        return cnt                    
        
    counter=0
    for mirror in mirrors:
        #find horizontal reflection 
        counter+=cnt(mirror, 100)    
        
            
        row_size = len(mirror[0])
        columns=['']*row_size

        for line in mirror:
            for i in range(row_size):
                columns[i] = columns[i] + line[i]
                    
        counter+=cnt(columns, 1)   
                    
    print(f'{time.time() - start_time}s: {counter}')
    f.close()
