package mobi.eyeline.ips.service;

import mobi.eyeline.ips.model.Question;
import mobi.eyeline.ips.model.QuestionOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.ceil;
import static java.lang.Math.min;

/**
 * @see com.eyelinecom.whoisd.sads2.common.USSDPageBuilder
 * @see com.eyelinecom.whoisd.sads2.adaptor.USSDAutoSegmentation
 */
@SuppressWarnings("JavadocReference")
public class SegmentationService {

    private static final Logger logger = LoggerFactory.getLogger(SegmentationService.class);

    private static final int MAX_LENGTH_7BIT    = 160;
    private static final int MAX_LENGTH_8BIT    = 140;
    private static final int MAX_LENGTH_16BIT   = 70;

    private static final String COMMAND_DELIM   = ">";
    private static final String LF              = "\n";

    public SegmentationService() {}

    public SegmentationInfo getSegmentationInfo(Question question) {
        final String text = getQuestionText(question);

        final int symbolsCount = text.length();
        final int maxSymbolsPerSegment = getSegmentLength(text);

        final int segmentsCount = (int) ceil(
                min((float) symbolsCount / maxSymbolsPerSegment, 1)
        );

        return new SegmentationInfo(segmentsCount, symbolsCount, maxSymbolsPerSegment);
    }

    private String getQuestionText(Question question) {
        final StringBuilder builder = new StringBuilder();

        builder.append(question.getTitle());
        for (QuestionOption option : question.getActiveOptions()) {
            // Increment due to 0-based ordering.
            builder.append(String.valueOf(option.getActiveIndex() + 1));
            builder.append(COMMAND_DELIM);
            builder.append(option.getAnswer());
            builder.append(LF);
        }
        return builder.toString();
    }


    private static int getSegmentLength(String text) {
        for (char c : text.toCharArray()) {
            if (c > 255) return MAX_LENGTH_16BIT;
            if (c > 127) return MAX_LENGTH_8BIT;
        }
        return MAX_LENGTH_7BIT;
    }


    //
    //
    //

    public static class SegmentationInfo {
        private final int segmentsCount;
        private final int symbolsCount;
        private final int maxSymbolsPerSegment;

        public SegmentationInfo(int segmentsCount,
                                int symbolsCount,
                                int maxSymbolsPerSegment) {
            this.segmentsCount = segmentsCount;
            this.symbolsCount = symbolsCount;
            this.maxSymbolsPerSegment = maxSymbolsPerSegment;
        }

        public int getSegmentsCount() {
            return segmentsCount;
        }

        public int getSymbolsCount() {
            return symbolsCount;
        }

        public int getMaxSymbolsPerSegment() {
            return maxSymbolsPerSegment;
        }

        @Override
        public String toString() {
            return "SegmentationInfo{" +
                    "segmentsCount=" + segmentsCount +
                    ", symbolsCount=" + symbolsCount +
                    ", maxSymbolsPerSegment=" + maxSymbolsPerSegment +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SegmentationInfo)) return false;

            SegmentationInfo that = (SegmentationInfo) o;

            return (maxSymbolsPerSegment == that.maxSymbolsPerSegment) &&
                   (segmentsCount == that.segmentsCount) &&
                   (symbolsCount == that.symbolsCount);
        }

        @Override
        public int hashCode() {
            int result = segmentsCount;
            result = 31 * result + symbolsCount;
            result = 31 * result + maxSymbolsPerSegment;
            return result;
        }
    }
}
