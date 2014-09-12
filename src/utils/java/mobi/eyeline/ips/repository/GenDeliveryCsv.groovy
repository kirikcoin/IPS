package mobi.eyeline.ips.repository

int count = 10_000_000

new File('delivery.csv').withPrintWriter { w ->
    (7_999_000_00_00..<(7_999_000_00_00 + count)).each { msisdn ->
        w.println msisdn
    }
}

