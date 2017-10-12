# RosenbrockFunctionApproximator
Using neural network, basic backprop and radial basis function.
Running without any parameters will open the GUI.  If you would rather use the command line, it is also optionally there.

For help with the command line parameters, use `java -jar Rosenbrock.jar -h`


| Flag   | Description                                       | Default | Parameter |
|--------|---------------------------------------------------|:-------:|:---------:|
| -nogui | Runs the application without a GUI                | true    | Void      |
| -h     | Displays the help text                            |         | Void      |
| -rb    | Sets the network to use radial basis              | false   | Void      |
| -ds    | The start point for the data (example) generation | 0.000   | Double    |
| -de    | The end point for the data (example) generation   | 20.000  | Double    |
| -di    | The incrementation of the data point              | 0.100   | Double    |
| -hl    | The amount of hidden layers                       | 1       | Integer   |
| -d     | The number of dimensions the function will use    | 2       | Integer   |
| -n     | The number of nodes per hidden layer              | 3       | Integer   |
| -s     | Save the weights to a given output file           |         | String    |


Example:
```
java -jar Rosenbrock.jar -nogui -rb -ds 0 -de 20 -di 1 -hl 2 -d 3 -n 7 -s
```
