# feedforward-neural-network

Simple neural network in Java for generation and classification of images.

**To Use**
1. Edit the network config, this is found in `NeualNetwork.java` in **`main()`**. Only use `FullyConnected` layers for now, `Convolutional` and `Pooling` layers are **WIP**. Each layer requires a neuron config, 3 dimensional vector (Vector3) and an activation function (NONE for the first layer), and `SIGMOID`, `RELU` or `LeakyRELU` on the other layers, currently **SIGMOID** generally works best.

2. If you have changed the **`file path`** for training or testing data you must modify the `path` variable found in `NeualNetwork.java` to change the global path. To edit the training and testing path change the `training` and `testing` variables in **`populateTrainingData()`**. The `output` path variable is located in **`outputData()`** and is where the images will be placed during training and testing. Training, testing and output are all relative paths and are based on the global path.

3. If you are using **`color`** images for training then the `input` or `output` layer, *depending on whether you are inputting or outputting images* should have a `depth of 3` for the 3 RGB color channels, eg(`new Vector3(32, 32, 3)`). Change the boolean in **`populateTrainingData()`**, within `ImageUtil.getImageData(training, true);` and `ImageUtil.getImageData(testing, true);` to **`true`**, indicating that you want color data in the training and testing sets.

4. If you want to **`switch the input and output`**, eg([1, image] to [image, 1]), to do this switch `input` and `output` variables in `new TrainingData(dataInput, dataOutput);` found in `ImageUtil.java`. You may want to do this if you are changing the network over from `classification` to `generation`.

5. To modify the `training iterations`, `learning rate` or `dropout rate` you can change the constants under `NeualNetwork.java` in **`main()`**. Learning rate should be kept near **0.05f** and dropout rate around **0.01f - 0.1f**, for most use cases greater than 1000 training iterations won't affect the results much. 

6. Running the network requires running the **`main()`**, during training it will print the current `iteration` and `average error` to the terminal as well as saving files in the output directory, by default its **`src/network/data/output/`**, when it's complete with training it will run the `testing dataset` and save the results in the output directory.

![Zero](src/network/data/output/output0.png)
