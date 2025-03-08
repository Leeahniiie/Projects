from asyncio.unix_events import BaseChildWatcher
from torch import no_grad, stack
from torch.utils.data import DataLoader
from torch.nn import Module
import torch.nn as nn
import torch.nn.functional as F



"""
Functions you should use.
Please avoid importing any other torch functions or modules.
Your code will not pass if the gradescope autograder detects any changed imports
"""
from torch.nn import Parameter, Linear
from torch import optim, tensor, tensordot, empty, ones
from torch.nn.functional import cross_entropy, relu, mse_loss
from torch import movedim
import torch


class PerceptronModel(Module):
    def __init__(self, dimensions):
        """
        Initialize a new Perceptron instance.

        A perceptron classifies data points as either belonging to a particular
        class (+1) or not (-1). `dimensions` is the dimensionality of the data.
        For example, dimensions=2 would mean that the perceptron must classify
        2D points.

        In order for our autograder to detect your weight, initialize it as a 
        pytorch Parameter object as follows:

        Parameter(weight_vector)

        where weight_vector is a pytorch Tensor of dimension 'dimensions'

        
        Hint: You can use ones(dim) to create a tensor of dimension dim.
        """
        super(PerceptronModel, self).__init__()
        
        "*** YOUR CODE HERE ***"
        self.w = Parameter(ones(1,dimensions)) #Initialize your weights here

    def get_weights(self):
        """
        Return a Parameter instance with the current weights of the perceptron.
        """
        return self.w

    def run(self, x):
        """
        Calculates the score assigned by the perceptron to a data point x.

        Inputs:
            x: a node with shape (1 x dimensions)
        Returns: a node containing a single number (the score)

        The pytorch function `tensordot` may be helpful here.
        """
        "*** YOUR CODE HERE ***"
        return tensordot(self.w, x)



    def get_prediction(self, x):
        """
        Calculates the predicted class for a single data point `x`.

        Returns: 1 or -1
        """
        "*** YOUR CODE HERE ***"
        if self.run(x) >= 0:
            return 1
        return -1



    def train(self, dataset):
        """
        Train the perceptron until convergence.
        You can iterate through DataLoader in order to 
        retrieve all the batches you need to train on.

        Each sample in the dataloader is in the form {'x': features, 'label': label} where label
        is the item we need to predict based off of its features.
        """        
        with no_grad():
            dataloader = DataLoader(dataset, batch_size=1, shuffle=True)
            "*** YOUR CODE HERE ***"
            weights_updated = True
            while weights_updated: 
                weights_updated = False
                for element in dataloader:
                    x, y= element['x'],element['label']
                    yhat = self.get_prediction(x)
                    if yhat != y:
                        weights_updated = True
                        self.w += y*x
                        
class RegressionModel(Module):
    """
    A neural network model for approximating a function that maps from real
    numbers to real numbers. The network should be sufficiently large to be able
    to approximate sin(x) on the interval [-2pi, 2pi] to reasonable precision.
    """
    def __init__(self):
        # Initialize your model parameters here
        "*** YOUR CODE HERE ***"
        super().__init__()
        self.layer= Linear(1, 20) 
        self.layer2 = Linear(20, 40)
        self.layer3 = Linear(40,1)
        self.bias = Parameter(tensor(0.), requires_grad=True)


    def forward(self, x):
        """ 
        Runs the model for a batch of examples.

        Inputs:
            x: a node with shape (batch_size x 1)
        Returns:
            A node with shape (batch_size x 1) containing predicted y-values
        """
        "*** YOUR CODE HERE ***"

        h1 = relu(self.layer(x))
        h2 = relu(self.layer2(h1))
        h3 = (self.layer3(h2))
        return h3

    
    def get_loss(self, x, y):
        """
        Computes the loss for a batch of examples.

        Inputs:
            x: a node with shape (batch_size x 1)
            y: a node with shape (batch_size x 1), containing the true y-values
                to be used for training
        Returns: a tensor of size 1 containing the loss
        """
        "*** YOUR CODE HERE ***"

        return mse_loss(self.forward(x),y)
        # N = y.dim()
        # sum = tensor([0])
        # f_x = self.forward(x)
        # #print(y.shape, f_x.shape, x.shape)
        # #print(y.dim(), f_x.dim(), x.dim())
        # for fx, r_y in zip(f_x,y):
        #     #print(fx,r_y)
        #     sum += (r_y-fx)**2
        #     print(sum)
        # 1/(2*N)*(sum)
        
 
  

    def train(self, dataset):
        """
        Trains the model.

        In order to create batches, create a DataLoader object and pass in `dataset` as well as your required 
        batch size. You can look at PerceptronModel as a guideline for how you should implement the DataLoader

        Each sample in the dataloader object will be in the form {'x': features, 'label': label} where label
        is the item we need to predict based off of its features.

        Inputs:
            dataset: a PyTorch dataset object containing data to be trained on
            
        """
        "*** YOUR CODE HERE ***"
        optimizer = optim.Adam(self.parameters(), lr=0.0001)
        dataloader = DataLoader(dataset, batch_size=6, shuffle=True)
        average_loss = 1000
        while average_loss > 0.0200:
            batch_loss = 0
            for element in dataloader:
                x, y= element['x'],element['label']
                optimizer.zero_grad()
                loss = self.get_loss(x,y)
                loss.backward()
                optimizer.step()
                batch_loss += loss

            average_loss = batch_loss / len(dataloader)
            if average_loss < 0.0200:
                break
        print("LOSS",batch_loss)




class DigitClassificationModel(Module):
    """2
    A model for handwritten digit classification using the MNIST dataset.

    Each handwritten digit is a 28x28 pixel grayscale image, which is flattened
    into a 784-dimensional vector for the purposes of this model. Each entry in
    the vector is a floating point number between 0 and 1.

    The goal is to sort each digit into one of 10 classes (number 0 through 9).

    (See RegressionModel for more information about the APIs of different
    methods here. We recommend that you implement the RegressionModel before
    working on this part of the project.)
    """
    def __init__(self):
        # Initialize your model parameters here
        super().__init__()
        input_size = 28 * 28
        output_size = 10
        self.layer= Linear(input_size, 200)
        self.layer2 = Linear(200, output_size)
        "*** YOUR CODE HERE ***"



    def run(self, x):
        """
        Runs the model for a batch of examples.

        Your model should predict a node with shape (batch_size x 10),
        containing scores. Higher scores correspond to greater probability of
        the image belonging to a particular class.

        Inputs:
            x: a tensor with shape (batch_size x 784)
        Output:
            A node with shape (batch_size x 10) containing predicted scores
                (also called logits)
        """
        """ YOUR CODE HERE """
        x1 = relu(self.layer(x))
        x2 = self.layer2(x1)
        return x2

        


    def get_loss(self, x, y):
        """
        Computes the loss for a batch of examples.

        The correct labels `y` are represented as a tensor with shape
        (batch_size x 10). Each row is a one-hot vector encoding the correct
        digit class (0-9).

        Inputs:
            x: a node with shape (batch_size x 784)
            y: a node with shape (batch_size x 10)
        Returns: a loss tensor
        """
        """ YOUR CODE HERE """
        return cross_entropy(self.run(x), y)

        

    def train(self, dataset):
        """
        Trains the model.
        """
        """ YOUR CODE HERE """
        optimizer = optim.Adam(self.parameters(), lr=0.01)
        dataloader = DataLoader(dataset, batch_size=60, shuffle=True)
        for epoch in range(100):
            total_loss = 0
            for element in dataloader:
                x, y= element['x'],element['label']
                loss = self.get_loss(x,y)
                loss.backward()
                total_loss += loss
            if dataset.get_validation_accuracy() >= 0.98:
                break
            if total_loss < 0.0200:
                break
            optimizer.step()
            optimizer.zero_grad()



class LanguageIDModel(Module):
    """
    A model for language identification at a single-word granularity.

    (See RegressionModel for more information about the APIs of different
    methods here. We recommend that you implement the RegressionModel before
    working on this part of the project.)
    """
    def __init__(self):
        # Our dataset contains words from five different languages, and the
        # combined alphabets of the five languages contain a total of 47 unique
        # characters.
        # You can refer to self.num_chars or len(self.languages) in your code
        self.num_chars = 47
        self.languages = ["English", "Spanish", "Finnish", "Dutch", "Polish"]
        super(LanguageIDModel, self).__init__()
        "*** YOUR CODE HERE ***"
        self.layer = Linear(47, 350)
        self.layer2 = Linear(350, 350)
        self.layer3 = Linear(350, 5)


    def run(self, xs):
        """
        Runs the model for a batch of examples.

        Although words have different lengths, our data processing guarantees
        that within a single batch, all words will be of the same length (L).

        Here `xs` will be a list of length L. Each element of `xs` will be a
        tensor with shape (batch_size x self.num_chars), where every row in the
        array is a one-hot vector encoding of a character. For example, if we
        have a batch of 8 three-letter words where the last word is "cat", then
        xs[1] will be a tensor that contains a 1 at position (7, 0). Here the
        index 7 reflects the fact that "cat" is the last word in the batch, and
        the index 0 reflects the fact that the letter "a" is the inital (0th)
        letter of our combined alphabet for this task.

        Your model should use a Recurrent Neural Network to summarize the list
        `xs` into a single tensor of shape (batch_size x hidden_size), for your
        choice of hidden_size. It should then calculate a tensor of shape
        (batch_size x 5) containing scores, where higher scores correspond to
        greater probability of the word originating from a particular language.

        Inputs:
            xs: a list with L elements (one per character), where each element
                is a node with shape (batch_size x self.num_chars)
        Returns:
            A node with shape (batch_size x 5) containing predicted scores
                (also called logits)
        """
        "*** YOUR CODE HERE ***"
        # initial = self.layer(xs[0])
        # for y in xs[1:]:
        #     layer = relu(self.layer(y))
        #     h = relu(self.layer2(initial))
        #     initial = h + layer
        # return self.layer3(initial)
            
        # xs = torch.stack(tuple(xs), dim=1)
        # output, hn = self.rnn(xs)
        # output = relu(output)
        # last_hidden = hn[-1]
        # last_hidden = relu(last_hidden)
        # rv = self.fc(last_hidden)
        batch_size = xs[0].shape[0] 
        h = empty(batch_size, 350)
        val = False
        for x in xs:
            if val == False:
                h = relu(self.layer(x))
                val = True
            else:
                current_input = relu(self.layer(x))
                h = relu(self.layer2(h) + current_input)
        rv = self.layer3(h)
        return rv

    
    def get_loss(self, xs, y):
        """
        Computes the loss for a batch of examples.

        The correct labels `y` are represented as a node with shape
        (batch_size x 5). Each row is a one-hot vector encoding the correct
        language.

        Inputs:
            xs: a list with L elements (one per character), where each element
                is a node with shape (batch_size x self.num_chars)
            y: a node with shape (batch_size x 5)
        Returns: a loss node
        """
        "*** YOUR CODE HERE ***"
        return cross_entropy(self.run(xs), y)


    def train(self, dataset):
        """
        Trains the model.

        Note that when you iterate through dataloader, each batch will returned as its own vector in the form
        (batch_size x length of word x self.num_chars). However, in order to run multiple samples at the same time,
        get_loss() and run() expect each batch to be in the form (length of word x batch_size x self.num_chars), meaning
        that you need to switch the first two dimensions of every batch. This can be done with the movedim() function 
        as follows:

        movedim(input_vector, initial_dimension_position, final_dimension_position)

        For more information, look at the pytorch documentation of torch.movedim()
        """
        "*** YOUR CODE HERE ***"
        optimizer = optim.Adam(self.parameters(), lr=0.001)
        dataloader = DataLoader(dataset, batch_size=100, shuffle=True)
        for epoch in range(25):
            total_loss = 0
            for element in dataloader:
                x, y= element['x'],element['label']
                x = movedim(x, (0, 1), (1, 0))
                optimizer.zero_grad()
                loss = self.get_loss(x,y)
                #print("Loss", type(loss))
                loss.backward()
                total_loss += loss
                optimizer.step()
            if dataset.get_validation_accuracy() >= 0.81:
                break
            if total_loss < 0.0200:
                #print(total_loss)
                break



        

def Convolve(input: tensor, weight: tensor):
    """
    Acts as a convolution layer by applying a 2d convolution with the given inputs and weights.
    DO NOT import any pytorch methods to directly do this, the convolution must be done with only the functions
    already imported.

    There are multiple ways to complete this function. One possible solution would be to use 'tensordot'.
    If you would like to index a tensor, you can do it as such:

    tensor[y:y+height, x:x+width]

    This returns a subtensor who's first element is tensor[y,x] and has height 'height, and width 'width'
    """
    input_tensor_dimensions = input.shape
    weight_dimensions = weight.shape
    Output_Tensor = tensor(())
    "*** YOUR CODE HERE ***"

    input_height, input_width = input.shape
    kernel_height, kernel_width = weight.shape

    output_height = input_height - kernel_height + 1
    output_width = input_width - kernel_width + 1

    output = torch.empty((output_height, output_width))

 
    for i in range(output_height):
        for j in range(output_width):
            sub_tensor = input[i:i+kernel_height, j:j+kernel_width]

            output[i, j] = torch.sum(sub_tensor * weight)

    return output



class DigitConvolutionalModel(Module):
    """
    A model for handwritten digit classification using the MNIST dataset.

    This class is a convolutational model which has already been trained on MNIST.
    if Convolve() has been correctly implemented, this model should be able to achieve a high accuracy
    on the mnist dataset given the pretrained weights.


    """
    

    def __init__(self):
        # Initialize your model parameters here
        super().__init__()
        output_size = 10

        self.convolution_weights = Parameter(ones((3, 3)))
        """ YOUR CODE HERE """
        self.layer= Linear(676, output_size)


    def run(self, x):
        """
        The convolutional layer is already applied, and the output is flattened for you. You should treat x as
        a regular 1-dimentional datapoint now, similar to the previous questions.
        """
        x = x.reshape(len(x), 28, 28)
        x = stack(list(map(lambda sample: Convolve(sample, self.convolution_weights), x)))
        x = x.flatten(start_dim=1)
        """ YOUR CODE HERE """
        x1 = relu(x)
        return self.layer(x1)

 

    def get_loss(self, x, y):
        """
        Computes the loss for a batch of examples.

        The correct labels `y` are represented as a tensor with shape
        (batch_size x 10). Each row is a one-hot vector encoding the correct
        digit class (0-9).

        Inputs:
            x: a node with shape (batch_size x 784)
            y: a node with shape (batch_size x 10)
        Returns: a loss tensor
        """
        """ YOUR CODE HERE """
        return cross_entropy(self.run(x), y)

        

    def train(self, dataset):
        """
        Trains the model.
        """
        """ YOUR CODE HERE """
        optimizer = optim.Adam(self.parameters(), lr=0.01)
        dataloader = DataLoader(dataset, batch_size=60, shuffle=True)
        for epoch in range(100):
            total_loss = 0
            for element in dataloader:
                x, y= element['x'],element['label']
                optimizer.zero_grad()
                loss = self.get_loss(x, y)
                loss.backward()
                optimizer.step()
                total_loss += loss
            if dataset.get_validation_accuracy() >= 0.8:
                break
            # optimizer.step()
            # optimizer.zero_grad()


 