# GeneticRobot

example Genetic Algorithm robot controller.

this project was created to help be learn and better understand Genetic Algorithms. the problem comes from [Melanie
Mitchell's](http://www.amazon.com/Melanie-Mitchell/e/B001H6OO62/ref=dp_byline_cont_book_1) excellent book [Complexity a Guided Tour](http://www.amazon.com/Complexity-Guided-Tour-Melanie-Mitchell/dp/0199798109/ref=sr_1_sc_1?ie=UTF8&qid=1438457875&sr=8-1-spell&keywords=complexiy+a+guided+tour) which i learned about taking her [Introduction to Complexity](http://www.complexityexplorer.org/online-courses/27-introduction-to-complexity-summer-2015)
course.

you want to create a robot control algorithm that will get the best score. the field is represented as a grid where there
are cans in some of the grid spaces and blank spots in the others. the robot is simply supposed to pick up as many cans
as it can. it's score is determined by the following scoring system:

* if the robot runs into a wall, you subtract 5 points (-5)
* if the robot tries to pick up a can, but there is no can to pick up, you subtract 1 point (-1)
* if the robot tries to pick up a can and there *is* a can to pick up, you add 10 points (10)

it's important to note that the robot only has visibility to the squares north, south, east, west, and it's current
location. the robot also has 1 of seven different actions it can perform:

* try to pick up a can
* move north one square
* move south one square
* move west one square
* move east one square
* move in a random horizontal or vertical direction one square
* do nothing

a naive control algorithm for the robot could be defined as:

* if there is a can in my current location, pick it up
* if there is a can west,north,east,or south of my location, move to the can location
* if the robot cannot see a can in it's visible range, then move in a random direction that does not include a wall

