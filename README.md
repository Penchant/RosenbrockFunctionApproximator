# RosenbrockFunctionApproximator
Using neural network, basic backprop and radial basis function.
Running without any parameters will open the GUI.  If you would rather use the command line, it is also optionally there.

For help with the command line parameters, use `java Main -h`


| Flag | Description | Default | Uses Parameter |
| ---- | ----------- | ------- | -------------- |
| `-nogui` | Runs the application without a GUI | `False` | `False` |
| `-h`      | Displays the help text | | `False` |
| `-rb`     | Sets the network to use radial basis | `False` | `False` |
| `-ds`     | The start point for the data (example) generation | `???` | `True` |
| `-de`     | The end point for the data (example) generation | `???` | `True` |
| `-di`     | The incrementation of the data point | `???` | `True` |
| `-hl`     | The amount of hidden layers | `???` | `True` |
| `-d`      | The number of dimensions the function will use | `2` | `True` |
| `-n`      | The number of nodes per hidden layer | `???` | `True` |
| `-s`      | Save the weights to a given output file | | `False` |


Example:
```
java Main -nogui -rb -ds 0 -de 20 -di 1 -hl 2 -d 3 -n 7 -s
```
