import numpy as np

class SimpleNeuralNetwork:
    def __init__(self, input_size, hidden_size, output_size, learning_rate=0.1):
        self.learning_rate = learning_rate
        self.weights_input_hidden = np.random.rand(input_size, hidden_size)
        self.weights_hidden_output = np.random.rand(hidden_size, output_size)
        self.bias_hidden = np.random.rand(hidden_size)
        self.bias_output = np.random.rand(output_size)

    def sigmoid(self, x):
        return 1 / (1 + np.exp(-x))

    def sigmoid_derivative(self, x):
        return x * (1 - x)

    def forward(self, inputs):
        self.hidden_layer_input = np.dot(inputs, self.weights_input_hidden) + self.bias_hidden
        self.hidden_layer_output = self.sigmoid(self.hidden_layer_input)
        self.output_layer_input = np.dot(self.hidden_layer_output, self.weights_hidden_output) + self.bias_output
        self.output_layer_output = self.sigmoid(self.output_layer_input)
        return self.output_layer_output

    def backward(self, inputs, expected_output, actual_output):
        output_error = expected_output - actual_output
        output_delta = output_error * self.sigmoid_derivative(actual_output)

        hidden_error = np.dot(output_delta, self.weights_hidden_output.T)
        hidden_delta = hidden_error * self.sigmoid_derivative(self.hidden_layer_output)

        self.weights_hidden_output += np.dot(self.hidden_layer_output.T[:, None], output_delta[None, :]) * self.learning_rate
        self.bias_output += output_delta * self.learning_rate
        self.weights_input_hidden += np.dot(inputs.T[:, None], hidden_delta[None, :]) * self.learning_rate
        self.bias_hidden += hidden_delta * self.learning_rate

        print(f"Error: {output_error}")
        print(f"Updated weights (input-hidden): \n{self.weights_input_hidden}")
        print(f"Updated weights (hidden-output): \n{self.weights_hidden_output}")

    def train(self, inputs, expected_output, epochs):
        for epoch in range(epochs):
            print(f"Epoch {epoch + 1}")
            actual_output = self.forward(inputs)
            self.backward(inputs, expected_output, actual_output)
            print(f"Output after epoch {epoch + 1}: {actual_output}\n")


inputs = np.array([0.5, 0.9])  
expected_output = np.array([1.0])  


nn = SimpleNeuralNetwork(input_size=2, hidden_size=2, output_size=1, learning_rate=0.1)

nn.train(inputs, expected_output, epochs=10)
