# Happy XO V1.0.0


## Contents of tis readme:
- summary description about the project
- how to use app
- explain of how application work 
- Languages ,API ,resources used 
- limitation
- contact info


## summary description about the project:
### this ***android app*** is XO game (tic-tac-toe) that offer

    - play with AI (using minimax, alpha purning algorithm )
    - play with another user (this version restricted on one mobile phone ,see limitation)
    - game configuration (change play level)

## how to use app:
easy interface with animations : after installition you can:
- choose to play with AI (robot logo)
- choose to play with another person (user with laptop logo)
- choose game configuration (playstation logo)

## overview of how application work in outside:
### Python code:
- static_eval : given XO matrix ***should be n*n*** return evelution of this matrix such that :
- 0: no one won,game haven't finished 
- 1:computer won or opponent won (in user game)
- -1:user won
- alphabeta_decide : main method , alpha beta with min-max return static_eval
- get_best_position : from alphabeta_decide get the new matrix with new step
- :compare 2 matrix (before step,after step) (logically one step done) and get certain step (step is indices in matrix to use it in java easily)
- get_best_step :from get_best_position get  get step not matrix 
- StringToList convert list (got from java using chaquo as string) to list 
### java code
- Utils is mostly used methods in app  dialog,go to activity ,startPython ,format data sent to python 
- one method is ArrayToBracket that given java 2d array (represent matrix) convert it to string of python 2d list (to send to python then recognized in StringToList)
- in (splach screen) it load SharedPreferences 
- loaded SharedPreferences data are loaded in memory with static attributes in Settings
- SharedPreferences are created /changed in configuration Activity ,and put default on Activity when loaded
- XO_Ai ,XO_user are nearly same but difference in main code ,i put many shared methods in XO_user as static methods but some methods ,copied it as it some difficult to make static
- i tried to make code orgainzed as possible

## Languages ,API ,resources used 
-      ***Language:*** java,python3 ,gradle with groovy
-      ***API:*** chaquo version 14 ----to connect python to java
-      ***Resources:*** algorithm of alpha-beta is in format from internet
    
## limitation:
    -the most important limit is that when play with another user ,both on same mobile phone 
    (user not connected)
    -same times may got suddenly not-work


## contact info
    -tahaelshrif1@gmail.com