package mobi.eyeline.ips.util

import groovy.util.logging.Slf4j
import mobi.eyeline.ips.web.validators.PhoneValidator

@Slf4j('logger')
class CsvParseService {

    private final PhoneValidator phoneValidator = new PhoneValidator()

    CsvParseService() {
    }

    @SuppressWarnings(["GrMethodMayBeStatic", "GroovyMissingReturnStatement"])
    List<String> parseFile(InputStream inputStream) throws CsvLineException {
        def lines = new LinkedHashSet<String>()

        inputStream.eachLine('UTF-8') { String original, int lineNumber ->
            original = original.trim()
            if (!original.startsWith('#')) {
                String line = original.replaceFirst('^\\+', '')

                if (!phoneValidator.validate(line)) {
                    logger.debug "CSV parse error: $lineNumber: $original"
                    throw new InvalidMsisdnFormatException(lineNumber, original)
                }

                if (lines.contains(line)) {
                    logger.debug "CSV parse error: $lineNumber: $original"
                    throw new DuplicateMsisdnException(lineNumber, original)
                }
                lines << line
            }
        }

        return lines.asList()
    }

    static abstract class CsvLineException extends Exception {
        final int lineNumber
        final String lineContent

        CsvLineException(int lineNumber, String lineContent) {
            super("CSV format error: $lineNumber: $lineContent")

            this.lineNumber = lineNumber
            this.lineContent = lineContent
        }
    }

    static class InvalidMsisdnFormatException extends CsvLineException {
        InvalidMsisdnFormatException(int lineNumber, String lineContent) {
            super(lineNumber, lineContent)
        }
    }

    static class DuplicateMsisdnException extends CsvLineException {
        DuplicateMsisdnException(int lineNumber, String lineContent) {
            super(lineNumber, lineContent)
        }
    }

}
