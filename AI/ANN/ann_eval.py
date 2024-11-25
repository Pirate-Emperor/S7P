import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error

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

    def train(self, inputs, expected_output, epochs):
        for epoch in range(epochs):
            for x, y in zip(inputs, expected_output):
                actual_output = self.forward(x)
                self.backward(x, y, actual_output)

    def predict(self, inputs):
        outputs = [self.forward(x) for x in inputs]
        return np.array(outputs)


np.random.seed(42)
X = np.random.rand(1000, 2)  
y = np.array([[0.8 * x[0] + 0.2 * x[1]] for x in X])  


X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)


nn = SimpleNeuralNetwork(input_size=2, hidden_size=5, output_size=1, learning_rate=0.1)


nn.train(X_train, y_train, epochs=100)


y_pred = nn.predict(X_test)


mse = mean_squared_error(y_test, y_pred)
print(f"Mean Squared Error on Test Set: {mse}")


for i in range(5):
    print(f"Input: {X_test[i]}, Predicted: {y_pred[i]}, Actual: {y_test[i]}")
