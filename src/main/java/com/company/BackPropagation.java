package com.company;

public class BackPropagation {
    public double l_rate = 0.1;
    protected Layer[] fLayers;

    public BackPropagation(int[] layers ){
        fLayers = new Layer[layers.length];
        for(int i = 0; i < layers.length; i++){
            fLayers[i] = (i!= 0)? (new Layer(layers[i], layers[i-1]) ): (new Layer(layers[i],0));
        }
    }

    public double activation(double[] weights, double[] inputs){
        double activation = 0.0 ;
        for(int i=0; i < weights.length-1; i++){
            activation += weights[i] * inputs[i];
        }
        return activation;
    }


    public double transfer(double activation){
        return 1.0/ (1.0 + Math.pow(Math.E, -activation));
    }

    public double[] forward_propagate(double[] input){
        int i, j, k;
        double activation;

        double output[] = new double[fLayers[fLayers.length-1].Length];
        //System.out.println("output lenght"+output)
        for(i = 0; i < fLayers[0].Length; i++)
        {
            fLayers[0].Neurons[i].Value = input[i];
        }

        for (k = 1; k < fLayers.length; k++){
            for (i =0; i < fLayers[k].Length; i++){
                activation = 0.0;
                for(j = 0; j < fLayers[k-1].Length; j++)
                    activation += fLayers[k].Neurons[i].Weights[j] * fLayers[k-1].Neurons[j].Value;
                activation += fLayers[k].Neurons[i].Bias;
                fLayers[k].Neurons[i].Value = transfer(activation);
            }
        }
        for(i = 0; i < fLayers[fLayers.length-1].Length; i++){
            output[i] = fLayers[fLayers.length - 1].Neurons[i].Value;
        }

        return output;

    }

    public double transfer_derivative(double output) {
        return output - Math.pow(output,2);
    }

    public double backPropagate(double[] input, double[] output){
        int i, j, k;

        double new_output[] = forward_propagate(input);
//        System.out.println(new_output.length);
        double error =0.0;


        for(i = 0; i < fLayers[fLayers.length - 1].Length; i++){
            error = output[i] - new_output[i];
            fLayers[fLayers.length - 1].Neurons[i].Delta = error * transfer_derivative(new_output[i]);
        }

        for(k = fLayers.length - 2; k>=0; k--){
            for( i = 0; i < fLayers[k].Length; i++){
                error = 0.0;
                for(j = 0; j < fLayers[k+1].Length; j++)
                    error += fLayers[k+1].Neurons[j].Delta *  fLayers[k + 1].Neurons[j].Weights[i];
                fLayers[k].Neurons[i].Delta =  error * transfer_derivative(fLayers[k].Neurons[i].Value);
            }

            for( i = 0; i < fLayers[k + 1].Length; i++){
                for(j = 0; j < fLayers[k].Length; j++)
                    fLayers[k+1].Neurons[i].Weights[j] += l_rate * fLayers[k+1].Neurons[i].Delta * fLayers[k].Neurons[j].Value;

                fLayers[k + 1].Neurons[i].Bias += l_rate * fLayers[k+1].Neurons[i].Delta;
            }
        }


        //  System.out.println("This is working ");
        error =0.0;
        for(i = 0; i < new_output.length; i++){
            //System.out.print(new_output[i]);
            error += Math.abs(new_output[i] - output[i]);

        }
        // System.out.println("just the new output");


        return Math.abs(error/output.length);

    }

    public void train_network(double[] inputs, double[] outputs, int epochs){
        double sum_error;
        for(int i = 0; i < epochs; i++){
            sum_error = backPropagate(inputs, outputs);
        }
    }

//    public double predict(double[] data_row){
//        int maxAt = 0;
//        double[] outputs = forward_propagate(data_row);
//        for(int i = 0; i < outputs.length; i++){
//            System.out.println(outputs[i]);
//            maxAt = outputs[i] > outputs[maxAt]?i: maxAt;
//        }
//        return maxAt;
//    }

//    public double predict(int[] dataRow){
//        double activation = weight[0];
//        for (int i = 0; i < 35; i++ ){
//            activation += weight[i+1] * dataRow[i];
//        }
//        return (activation > 0.0)? 1.0: 0.0;
//    }

    public void trainNNForAlpha( double[][] dataset, double[][] output){
        int i, j;
        double error = 0.0;
        double prop = 0;
        for( i = 0; i < 100000; i++){
            error = 0.0;
            for(j = 0; j < 26; j++){
                prop = backPropagate(dataset[j], output[j]);
                error += prop;
            }

            System.out.format("epoch = %s, l_rate = %.3f, error = %.3f %n", i++, l_rate, error);
        }
    }

    public static void main(String[] args) {
        // write your code here


        int i, j;
        int[] layers = new int[]{35, 12, 5};
        BackPropagation net = new BackPropagation(layers);
        double error = 0.0;

        double[][] dataset = {
                { 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1}, //A
                { 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0}, //B
                { 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0}, //C
                { 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0}, //D
                { 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1}, //E
                { 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0}, //F
                { 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0}, //G
                { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1}, //H
                { 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1}, //i
                { 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0}, //J
                { 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1}, //K
                { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1}, //L
                { 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1}, //M
                { 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1}, //N
                { 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0}, //O
                { 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0}, //P
                { 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1}, //Q
                { 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1}, //R
                { 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0}, //S
                { 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0}, //T
                { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0}, //U
                { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0}, //V
                { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1}, //W
                { 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1}, //x
                { 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0}, //Y
                { 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1}, //Z
        };

//        double[] outputs = { 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0};
        double [][] outputs = {
                {0, 0, 0, 0, 1}, //A
                {0, 0, 0, 1, 0}, //B
                {0, 0, 0, 1, 1}, //C
                {0, 0, 1, 0, 0}, //D
                {0, 0, 1, 0, 1}, //E
                {0, 0, 1, 1, 0}, //F
                {0, 0, 1, 1, 1}, //G
                {0, 1, 0, 0, 0}, //H
                {0, 1, 0, 0, 1}, //I
                {0, 1, 0, 1, 0}, //J
                {0, 1, 0, 1, 1}, //K
                {0, 1, 1, 0, 0}, //L
                {0, 1, 1, 0, 1}, //M
                {0, 1, 1, 1, 0}, //N
                {0, 1, 1, 1, 1}, //O
                {1, 0, 0, 0, 1}, //P
                {1, 0, 0, 1, 0}, //Q
                {1, 0, 0, 1, 1}, //R
                {1, 0, 1, 0, 0}, //S
                {1, 0, 1, 0, 1}, //T
                {1, 0, 1, 1, 0}, //U
                {1, 0, 1, 1, 1}, //V
                {1, 1, 0, 0, 0}, //W
                {1, 1, 0, 0, 1}, //X
                {1, 1, 0, 1, 0}, //Y
                {1, 1, 0, 1, 1}, //Z
        };
        double prop = 0;
        i = 0;
        for( i = 0; i < 100000; i++){
            error = 0.0;
            for(j = 0; j < 26; j++){
                prop = net.backPropagate(dataset[j], outputs[j]);
                error += prop;
            }

            System.out.format("epoch = %s, l_rate = %.3f, error = %.3f %n", i++, net.l_rate, error);
        }


        double [] letter_A = { 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1};
        double [] letter_E = { 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1};
        double [] letter_I = { 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1};
        double [] letter_O = { 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0};
        double [] letter_U = { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0};



        double [] letter_V = { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0}; //V
        double [] letter_W = { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1}; //W
        double [] letter_X = { 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1}; //x
        double [] letter_Y = { 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0}; //Y
        double [] letter_Z = { 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1}; //Z
        System.out.println("The vowels");
        double[] predictA = net.forward_propagate(letter_A);
        System.out.println("Expected= "+outputs[0][0]+" "+outputs[0][1]+" "+outputs[0][2]+" "+outputs[0][3]+" "+outputs[0][4]+", Predicted="+ predictA[0]+" "+predictA[1]+" "+predictA[2]+" "+predictA[3]+" "+predictA[4]);
        double[] predictE = net.forward_propagate(letter_E);
        System.out.println("Expected={0, 0, 1, 0, 1}"+", Predicted="+ predictE[0]+" "+predictE[1]+" "+predictE[2]+" "+predictE[3]+" "+predictE[4]);
//        double[] predictI = net.forward_propagate(letter_I);
//        System.out.println("Expected="+ 1.0 +", Predicted="+ predictI[0]);
//        double[] predictO = net.forward_propagate(letter_O);
//        System.out.println("Expected="+ 1.0 +", Predicted="+ predictO[0]);
//        double[] predictU = net.forward_propagate(letter_U);
//        System.out.println("Expected="+ 1.0 +", Predicted="+ predictU[0]);
//
//
//        System.out.println("Not so vowels");
//        double[] predictV = net.forward_propagate(letter_V);
//        System.out.println("Expected="+ 0.0 +", Predicted="+ predictV[0]);
//        double[] predictW = net.forward_propagate(letter_W);
//        System.out.println("Expected="+ 0.0 +", Predicted="+ predictW[0]);
//        double[] predictX = net.forward_propagate(letter_X);
//        System.out.println("Expected="+ 0.0 +", Predicted="+ predictX[0]);
//        double[] predictY = net.forward_propagate(letter_Y);
//        System.out.println("Expected="+ 0.0 +", Predicted="+ predictY[0]);
//        double[] predictZ = net.forward_propagate(letter_Z);
//        System.out.println("Expected="+ 0.0 +", Predicted="+ predictZ[0]);










    }
}

