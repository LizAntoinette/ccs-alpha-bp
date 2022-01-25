package com.company;

public class Layer {
    public Neuron Neurons[];
    public int Length;

    public Layer(int l, int prev){
        Length = l;
        Neurons = new Neuron[l];

        for(int j=0; j < Length; j++)
            Neurons[j] = new Neuron(prev);
    }

}
