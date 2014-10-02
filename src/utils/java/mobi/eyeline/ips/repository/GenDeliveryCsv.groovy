package mobi.eyeline.ips.repository

int count = 50_000_000

new File('delivery_50mil.csv').withPrintWriter { w ->
    (7_999_000_00_00..<(7_999_000_00_00 + count)).each { msisdn ->
        w.println msisdn
    }
}

