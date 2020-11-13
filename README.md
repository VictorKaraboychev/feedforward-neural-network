# feedforward-neural-network

Simple [feedforward neural network](https://en.wikipedia.org/wiki/Feedforward_neural_network) in Java for generation and classification of images. There is some implimentation for convolutional and pooling layers, however they are still very **WIP** and should not be used. The provided training data set is small (200 digits) which often results in [overfiting](https://en.wikipedia.org/wiki/Overfitting) of the network and can lead to poor results on foreign data. For better results a much larger data set should be used such as the [MNIST database of handwritten digits](http://yann.lecun.com/exdb/mnist/), which contains 60,000 digits. Inspiration drawn from [Neural networks tutorial: Fully Connected 1 [Java] - Finn Eggers](https://www.youtube.com/playlist?list=PLgomWLYGNl1dL1Qsmgumhcg4HOcWZMd3k).

### Usage Information
1. Edit the network config, this is found in `NeuralNetwork.java` in **`main()`**. Only use `FullyConnected` layers for now, `Convolutional` and `Pooling` layers are **WIP**. Each layer requires a neuron config, 3 dimensional vector (Vector3) and an activation function (**NONE** for the first layer), and `SIGMOID`, `RELU` or `LeakyRELU` on the other layers, **SIGMOID** generally works best for most situations.

2. If you have changed the **`file path`** for training or testing data you must modify the `path` variable found in `NeuralNetwork.java` to change the global path. To edit the training and testing path change the `training` and `testing` variables in **`populateTrainingData()`**. The `output` path variable is located in **`outputData()`** and is where the images will be placed during training and testing. Training, testing and output are all relative paths and are based on the global path.

3. If you are using **`color`** images for training then the `input` or `output` layer, *depending on whether you are inputting or outputting images* should have a `depth of 3` for the 3 RGB color channels, eg(`new Vector3(32, 32, 3)`). Change the boolean in `NeuralNetwork.java` in **`populateTrainingData()`**, within `ImageUtil.getImageData(training, true);` and `ImageUtil.getImageData(testing, true);` to **`true`**, indicating that you want color data in the training and testing sets.

4. If you want to **`switch the input and output`**, eg([1, image] to [image, 1]), to do this switch `input` and `output` variables in `new TrainingData(dataInput, dataOutput);` found in `ImageUtil.java`. You may want to do this if you are changing the network over from `classification` to `generation`.

5. To modify the `training iterations`, `learning rate` or `dropout rate` you can change the constants under `NeuralNetwork.java` in **`main()`**. Learning rate should be kept near **0.05f** and dropout rate around **0.01f - 0.1f**, for most use cases greater than 1000 training iterations won't affect the results much. 

6. Running the network requires running the **`main()`** in `NeuralNetwork.java`, during training it will print the current `iteration` and `average error` to the terminal as well as saving files in the output directory, by default its **`src/network/data/output/`**, when it's complete with training it will run the `testing dataset` and save the results in the output directory.


### Example Outputs

![Zero](https://github.com/InternetAlien/feedforward-neural-network/blob/main/NeuralNetwork/src/network/data/output/output0.png)
![One](https://github.com/InternetAlien/feedforward-neural-network/blob/main/NeuralNetwork/src/network/data/output/output1.png)
![Two](https://github.com/InternetAlien/feedforward-neural-network/blob/main/NeuralNetwork/src/network/data/output/output2.png)
![Three](https://github.com/InternetAlien/feedforward-neural-network/blob/main/NeuralNetwork/src/network/data/output/output3.png)
![Four](https://github.com/InternetAlien/feedforward-neural-network/blob/main/NeuralNetwork/src/network/data/output/output4.png)
![Five](https://github.com/InternetAlien/feedforward-neural-network/blob/main/NeuralNetwork/src/network/data/output/output5.png)
![Six](https://github.com/InternetAlien/feedforward-neural-network/blob/main/NeuralNetwork/src/network/data/output/output6.png)
![Seven](https://github.com/InternetAlien/feedforward-neural-network/blob/main/NeuralNetwork/src/network/data/output/output7.png)
![Eight](https://github.com/InternetAlien/feedforward-neural-network/blob/main/NeuralNetwork/src/network/data/output/output8.png)
![Nine](https://github.com/InternetAlien/feedforward-neural-network/blob/main/NeuralNetwork/src/network/data/output/output9.png)

**Generation Settings**
- Network configuration `(10x1x1), (16,1,1), (32,1,1), (32,32,1)`
- **500** `training iterations`, **0.05f** `learning rate` and **0.01f** `dropout`
