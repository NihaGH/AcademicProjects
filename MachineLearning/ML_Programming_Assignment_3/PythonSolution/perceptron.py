import numpy as np
import pandas as pd
import csv
import argparse

class Perceptron(object):
    """Implements a perceptron network"""
    def __init__(self, input_size, lr=1, epochs=100):
        # two sets of weights -> 
        # 1. for constant learning rate
        # 2. for annealing learning rate
        self.W_constant = np.zeros(input_size+1)
        self.W_changing = np.zeros(input_size+1)
        self.epochs = epochs
        self.lr = lr
    
    def activation_fn(self, y):
        result = []
        for i in range(y.shape[0]):
            result.append(1 if y[i] > 0 else 0)
        return result
 
    def fit(self, x, target, output_file):
        # variable ending in 'constant' is used to determine the weights using constant learning rate (=1)
        # variable ending in 'changing' is used to determine the weights using annealing learning rate (=1/iteration_number)
        error_count_constant = []
        error_count_changing = []
        
        for k in range(self.epochs+1):
            result_constant = np.matmul(x,self.W_constant)
            result_changing = np.matmul(x,self.W_changing)
            
            error_constant = target - np.asarray(self.activation_fn(result_constant))
            error_changing = target - np.asarray(self.activation_fn(result_changing))
            
            self.W_constant += self.lr*np.matmul(np.transpose(x),error_constant)
            self.W_changing += (self.lr/(k+1))*np.matmul(np.transpose(x),error_changing)
            
            error_count_constant.append(np.count_nonzero(error_constant))
            error_count_changing.append(np.count_nonzero(error_changing))
        
        with open(output_file, 'w', newline='') as out_file:
            tsv_writer = csv.writer(out_file, delimiter='\t')
            tsv_writer.writerow(error_count_constant)
            tsv_writer.writerow(error_count_changing)
#         print(error_count_constant)
#         print(error_count_changing)

        
if __name__ == '__main__':
    
    parser = argparse.ArgumentParser()    
    parser.add_argument("--data", "-d", type=str, required=True)
    parser.add_argument("--output", "-o", type=str, required=True)
    args = parser.parse_args()
    
    df = pd.read_csv(args.data, sep='\t', header=None)
    
    # split dataset into classes -> y
    y = df.iloc[:,0]
    y = y.replace('A',1).replace('B',0)
    y=y.values
    
    # split dataset into inputs -> x
    x = np.zeros((df.shape[0], df.shape[1]-1))
    x[:,1:] = df[df.columns[1:3]]
    x[:, 0] =  1
    
    perceptron = Perceptron(input_size=x.shape[1]-1)
    perceptron.fit(x, y, args.output)