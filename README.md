# RosenbrockFunctionApproximator
Using neural network, basic backprop and radial basis function.
Running without any parameters will open the GUI.  If you would rather use the command line, it is also optionally there.

For help with the command line parameters, use `java Main -h`

```
-nogui  Runs the application without a GUI - default [False]     Takes no parameter
-h      Displays the help text   Takes no parameter
-rb     Sets the network to use radial basis - default [False]   Takes no parameter
-ds     The start point for the data (example) generation - default [???]
-de     The end point for the data (example) generation - default [???]
-di     The incrementation of the data point - default [???]
-hl     The amount of hidden layers - default [???]
-d      The number of dimensions the function will use - default [2]
-n      The number of nodes per hidden layer - default [???]
```

Example:
```
java Main -nogui -rb -ds 0 -de 20 -di 1 -hl 2 -d 3 -n 7
```