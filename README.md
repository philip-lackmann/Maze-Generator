# Maze-Generator
An application that randomly generates a maze with a backtracking algorithm.
### Prerequisites
To run this application, you need to have the Java Runtime Environment installed on your computer.
### Built With
* [Netbeans](https://netbeans.org)
### Authors
* **Philip Lackmann**
### Additional Notes
* Due to a weird bug (of JavaFx probably?) some of the lines of the maze are partially overlapped by the generated, colored rectangles in the background, which is why it shouldn't be possible for them to overlap. This small bug doesn't influence the functionality, however it isn't very esthetically pleasing.
* Recursion could streamline the backtracking algorithm.
* The constructor of the maze is too overloaded but I kept it like that because all variables are set beforehand while they all must be set anyways and the Maze-Object is initialized once only.
