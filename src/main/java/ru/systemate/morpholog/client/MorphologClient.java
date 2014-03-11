package ru.systemate.morpholog.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Morpholog client
 */
public abstract class MorphologClient {

    private static final Logger LOG = LoggerFactory.getLogger(MorphologClient.class);

    private static MorphologClient instance;

    /**
     *
     */
    public static enum Genders{
        male,female,it
    }

    /**
     *
     */
    public static enum Cases{
        imenit,rodit,dat,vinit,tvorit,predl
    }

    public static class Numeral {
        private String numeral;
        private Cases labelCase;
        private boolean plural;

        /**
         *
         * @param numeral
         * @param labelCase
         * @param plural
         */
        public Numeral(String numeral, Cases labelCase, boolean plural) {
            this.numeral = numeral;
            this.labelCase = labelCase;
            this.plural = plural;
        }

        /**
         *
         * @return
         */
        public String getNumeral() {
            return numeral;
        }

        /**
         *
         * @return
         */
        public Cases getLabelCase() {
            return labelCase;
        }

        /**
         *
         * @return
         */
        public boolean isPlural() {
            return plural;
        }
    }

    /**
     *
     */
    public static enum FWordDictionaries{
        age_16, age_18
    }

    /**
     *
     */
    public static class FWord{
        private String word;
        private long start;
        private int length;
        private FWordDictionaries type;

        /**
         *
         * @param word
         * @param start
         * @param length
         * @param type
         */
        public FWord(String word, long start, int length, FWordDictionaries type) {
            this.word = word;
            this.start = start;
            this.length = length;
            this.type = type;
        }

        /**
         *
         * @return
         */
        public String getWord() {
            return word;
        }

        /**
         *
         * @return
         */
        public long getStart() {
            return start;
        }

        /**
         *
         * @return
         */
        public int getLength() {
            return length;
        }

        /**
         *
         * @return
         */
        public FWordDictionaries getType() {
            return type;
        }
    }

    /**
     *
     * @return
     */
    public static synchronized MorphologClient getInstance(){
        if(instance==null){
            try{
                Class.forName("ru.systemate.morpholog.phrase.PhraseDecliner");
                instance = new NativeMorphologClient();
            }catch (Exception e){
                instance = new WSMorphologClient();
            }
        }
        return instance;
    }


    public abstract String declinePhrase(String text, Cases c);

    public abstract Genders getGender(String text);

    public abstract Numeral toNumeral(Number n, Cases c, Genders g, boolean live);

    public abstract Numeral formatNumber(Number n, String format);

    public abstract String formatTimeDiff(long diff, boolean round, boolean toNumeral);

    public abstract List<FWord> detectFWords(String text);

}
