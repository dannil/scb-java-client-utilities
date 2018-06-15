package com.github.dannil.scbjavaclientutil;

import java.io.File;

import com.github.dannil.scbjavaclientutil.contents.SCBTableInputs;

public class GenerateInputs {

    public static void main(String[] args) throws Exception {
        File outputDirectory = new File("inputs");
        SCBTableInputs i = new SCBTableInputs(outputDirectory);
        
        // Takes a tree JSON file as parameter
        File svTreeJson = new File("scb_2018-06-15T09-18-52.336_2018-06-15T10-12-11.865_sv.json");
        i.getInputs(svTreeJson);
    }
    
}
