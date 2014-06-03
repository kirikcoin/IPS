package mobi.eyeline.ips.util

import mobi.eyeline.ips.web.validators.PhoneValidator

class DeliveryUtils {

    public List<String> parseFile(InputStream inputStream){
        PhoneValidator phoneValidator = new PhoneValidator()
        def msisdns = []
            inputStream.eachLine('UTF-8') { String line, int lineNumber ->

                if (!line.startsWith('#')) {
                    line = line.replace('+', '')
                    if (!phoneValidator.validate(line)) {
                        throw new InvalidMsisdnFormatException(lineNumber,line)
                    }
                    if (msisdns.contains(line)) {
                        throw new DuplicateMsisdnException(lineNumber,line)
                    }
                    msisdns << line
                }
            }
        return msisdns
    }

    static class InvalidMsisdnFormatException extends Exception {
        int lineNumber
        String invalidString

        InvalidMsisdnFormatException(int lineNumber, String invalidString) {
            this.lineNumber = lineNumber
            this.invalidString = invalidString
        }
    }

    static class DuplicateMsisdnException extends Exception{
        int lineNumber
        String invalidString

        DuplicateMsisdnException(int lineNumbber, String invalidString) {
            this.lineNumber = lineNumbber
            this.invalidString = invalidString
        }
    }

}
