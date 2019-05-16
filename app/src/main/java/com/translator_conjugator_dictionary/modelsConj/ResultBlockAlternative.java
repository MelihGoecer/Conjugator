package com.translator_conjugator_dictionary.modelsConj;

import java.util.List;

public class ResultBlockAlternative {
        private String header;
        private List<TenseBlockAlternative> tenseBlocks;

        public ResultBlockAlternative(String header, List<TenseBlockAlternative> tenseBlocks) {
            this.header = header;
            this.tenseBlocks = tenseBlocks;
        }

        public ResultBlockAlternative() {
        }

        public String getHeader() {
            return header;
        }

        public List<TenseBlockAlternative> getTenseBlocks() {
            return tenseBlocks;
        }


}
