from copy import deepcopy
import ast
def StringToList(str):
    try:
        list_from_string = ast.literal_eval(str)
        return list_from_string
    except (SyntaxError, ValueError):
        return []

def childs(position,fit_by):
    if(isinstance(position,str)):
        position=StringToList(position)
    childs=[]
    for index_r,row in enumerate(position):
        for index_e,cell in enumerate(row):
            if (cell==""):
                child_i=deepcopy(position)#make a deepcopy to prevent problems of copy
                child_i[index_r][index_e]=fit_by
                childs.append(child_i)
    return childs

def static_eval(position,computerIsX):
    coputerChar='x' if computerIsX else 'o'
    if(isinstance(position,str)):
        position=StringToList(position)
    for row in position:
        if all((row[0]!='' and x == row[0]) for x in row):
            return 1 if (row[0]==coputerChar) else -1
    for i in range(len(row)):
        if all((position[0][i]!='' and x[i]==position[0][i]) for x in position):
            return 1 if (position[0][i]==coputerChar) else -1
    #one diagonal
    if all((position[0][0]!='' and row[i]== position[0][0]) for i,row in enumerate(position)):#as diagonal element of row is row index
        return 1 if (position[0][0]==coputerChar) else -1
    #other diagonal
    if all((position[0][-1]!='' and row[len(row)-1-i]== position[0][-1]) for i,row in enumerate(position)):#as len(row)-1-i element of row is row index in this diagonal        return True
        return 1 if (position[0][-1]==coputerChar) else -1

    return 0
def fit_by(computer_is_x):
    return 'x' if computer_is_x else 'o'
def alphabeta_decide(position,depth,alpha,beta,maxPlayer,steps_queue,computer_is_x):#whenever used code outside alpha is -float('inf'),beta is float('inf')
    if(isinstance(position,str)):
        position=StringToList(position)
    if (depth==0) or (static_eval(position,computer_is_x)!=0):
        return static_eval(position,computer_is_x)
    if maxPlayer :
        maxEval=-float('inf')
        for child in childs(position,fit_by(computer_is_x)):
            eval_c=alphabeta_decide(child,depth-1,alpha,beta,False,steps_queue,computer_is_x)
            maxEval=max(maxEval,eval_c)
            alpha=max(alpha,eval_c)
            if beta<=alpha:
                break
        return maxEval
    else :
        minEval=float('inf')
        bstMove=None
        for child in childs(position,fit_by(not computer_is_x)):
            eval_c=alphabeta_decide(child,depth-1,alpha,beta,True,steps_queue,computer_is_x)
            minEval=min(eval_c,eval_c)
            beta=min(beta,eval_c)
            if beta<=alpha:
                break
        return minEval
def get_best_position(position,computer_is_x,depth):
    if(isinstance(position,str)):
        position=StringToList(position)
    maxEval=-float('inf')
    bstMove=None
    if(static_eval(position,computer_is_x)!=0):
        print("finished")
        print(position)
        return bstMove
    for child in childs(position,fit_by(computer_is_x)):
        eval_c=alphabeta_decide(child,depth,-float('inf'),float('inf'), True,[],computer_is_x)
        if eval_c>maxEval:
            maxEval=eval_c
            bstMove=child

    return bstMove

def getStepIndices(x1,x2):
    if(isinstance(x1,str)):
        x1=StringToList(x1)
    if(isinstance(x2,str)):
        x2=StringToList(x2)
    i=0
    j=0
    for i in range(len(x1)):
        if (x1[i]==x2[i]):
            continue
        else:
            for j in range(len(x1)) :#len(x1) is same outer as inner so no problem
                if (x1[i][j]==x2[i][j]):
                    continue
                else:
                    break #as only one change/time
            break
    return (i,j)#index of difference so new char is her
def get_best_step(position,computer_is_x,depth):
    new_position=get_best_position(position,computer_is_x,depth)
    return getStepIndices(position,new_position)