package com.translator_conjugator_dictionary.modelsConj;

public class Data {

    private Details[][] detections;

    public Data(Details[][] detections) {
        this.detections = detections;
    }

    public Details[][] getDetections() {
        return detections;
    }
}
