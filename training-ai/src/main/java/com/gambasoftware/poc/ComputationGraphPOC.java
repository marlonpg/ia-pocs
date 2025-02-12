package com.gambasoftware.poc;

import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;

import java.io.File;

public class ComputationGraphPOC {

    public static void main(String[] args) throws Exception {
        int numUsers = 1000;
        int numMovies = 1000;
        int embeddingSize = 64;
        int batchSize = 32;
        int numEpochs = 10; // Number of training epochs

        ComputationGraphConfiguration.GraphBuilder builder = new NeuralNetConfiguration.Builder()
                .seed(123)
                .updater(new Adam())
                .weightInit(WeightInit.XAVIER)
                .graphBuilder()
                .addInputs("user_input", "movie_input")
                .addLayer("user_embedding", new DenseLayer.Builder()
                        .nIn(numUsers)
                        .nOut(embeddingSize)
                        .activation(Activation.RELU)
                        .build(), "user_input")
                .addLayer("movie_embedding", new DenseLayer.Builder()
                        .nIn(numMovies) // Number of movies
                        .nOut(embeddingSize) // Embedding size
                        .activation(Activation.RELU)
                        .build(), "movie_input")
                .addVertex("merge", new MergeVertex(), "user_embedding", "movie_embedding") // Merge embeddings
                .addLayer("hidden_layer", new DenseLayer.Builder()
                        .nIn(embeddingSize * 2) // Combined embedding size (64 + 64)
                        .nOut(64)
                        .activation(Activation.RELU)
                        .build(), "merge")
                .addLayer("output", new OutputLayer.Builder(LossFunctions.LossFunction.MSE) // Use MSE for regression
                        .nIn(64)
                        .nOut(1) // Predict a single rating
                        .activation(Activation.IDENTITY)
                        .build(), "hidden_layer")
                .setOutputs("output");

        ComputationGraphConfiguration conf = builder.build();

        // Step 2: Initialize the model
        ComputationGraph model = new ComputationGraph(conf);
        model.init();

        // Step 3: Prepare your data (example: random data for demonstration)
        INDArray userInput = Nd4j.create(batchSize, numUsers); // One-hot encoded user IDs
        INDArray movieInput = Nd4j.create(batchSize, numMovies); // One-hot encoded movie IDs
        INDArray ratings = Nd4j.create(batchSize, 1); // Actual ratings

        // Fill with random data for demonstration
        userInput.addi(Nd4j.rand(batchSize, numUsers));
        movieInput.addi(Nd4j.rand(batchSize, numMovies));
        ratings.addi(Nd4j.rand(batchSize, 1));

        // Step 4: Train the model
        for (int i = 0; i < numEpochs; i++) {
            model.fit(new INDArray[]{userInput, movieInput}, new INDArray[]{ratings});
            System.out.println("Epoch " + (i + 1) + " complete.");
        }

        // Step 5: Evaluate the model
        INDArray predictions = model.output(new INDArray[]{userInput, movieInput})[0];
        System.out.println("Predictions: " + predictions);

        // Step 6: Save the model
        File modelFile = new File("MovieRecommendationModel.zip");
        ModelSerializer.writeModel(model, modelFile, true);
        System.out.println("Model saved to: " + modelFile.getAbsolutePath());

        // Step 7: Load the model (optional)
        ComputationGraph loadedModel = ModelSerializer.restoreComputationGraph(modelFile);
        System.out.println("Model loaded successfully!");
    }
}