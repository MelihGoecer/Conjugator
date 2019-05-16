package com.translator_conjugator_dictionary.modelsConj;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ResultBlock {
    @Expose
    private String header;
    @Expose
    private List<TenseBlock> tenseBlocks;

    public ResultBlock(String header, List<TenseBlock> tenseBlocks) {
        this.header = header;
        this.tenseBlocks = tenseBlocks;
    }

    public ResultBlock() {
    }

    public String getHeader() {
        return header;
    }

    public List<TenseBlock> getTenseBlocks() {
        return tenseBlocks;
    }

}
