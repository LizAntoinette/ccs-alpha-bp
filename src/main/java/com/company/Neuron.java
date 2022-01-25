package com.company;

public class Neuron {

    public double Value;
    public double[] Weights;
    public double Bias;
    public double Delta;

    public Neuron(int prevLayerSize){
        Weights = new double[prevLayerSize];
        Bias = Math.random()/1000000000000.0;
        Delta = Math.random()/1000000000000.0;
        Value = Math.random()/1000000000000.0;

        for(int i = 0; i < Weights.length; i++){
            Weights[i] = Math.random()/1000000000000.0;
        }
    }

    public static void main(String[] args){
        for(int i = 0; i < 10; i++){
            System.out.println( Math.random()/1000000000000.0);
        }
    }
}
