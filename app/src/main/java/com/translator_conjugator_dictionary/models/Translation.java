package com.translator_conjugator_dictionary.models;

public class Translation {

    private String header;
    private String result;
    private String thirdResult;
    private String source;
    private String target;
    private String thirdLanguage;
    private String timestamp;


    public Translation(String header, String result, String thirdResult, String source, String target, String thirdLanguage, String timestamp) {
        this.header = header;
        this.result = result;
        this.thirdResult = thirdResult;
        this.source = source;
        this.target = target;
        this.thirdLanguage = thirdLanguage;
        this.timestamp = timestamp;
    }

    public String getHeader() {
        return header;
    }

    public String getResult() {
        return result;
    }

    public String getThirdResult() {
        return thirdResult;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getThirdLanguage() {
        return thirdLanguage;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
