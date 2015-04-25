package mobi.eyeline.ips.tzdata

import groovy.transform.Memoized

/**
 * Generates l10n bundles for timezone IDs. Uses .po-files from tzdata package as source.
 *
 * <p>Use the following to get the source files:
 * <code>bzr branch lp:ubuntu/tzdata</code>
 *
 * @see <a href="http://www.iana.org/time-zones">Time Zone Database aka zoneinfo</a>
 * @see <a href="https://www.gnu.org/software/gettext/manual/html_node/PO-Files.html">PO file format</a>
 */
class GenTzData {

  static void main(String... args) {
    def getOpts = {
      def cli = new CliBuilder(
          usage: 'GenTzData --source=<path> --target=<path> [locales]',
          header: 'Usage:').with {
        h longOpt: 'help', 'Usage information'
        _ longOpt: 'source', args: 1, argName: 'source', 'Path to .PO-files'
        _ longOpt: 'target', args: 1, argName: 'target', 'Target resource path'
        it
      }

      def opts = cli.parse args
      if (opts && (opts.help || opts.arguments().empty)) {
        cli.usage()
        return null
      }

      opts
    }

    def opts = getOpts()
    if (opts) new GenTzData().process(opts)
  }

  private void process(opts) {
    def maps = opts.arguments().collect { locale -> translate(opts.source, locale) }
    def keys = maps.first().keySet().collectMany { key -> maps.any { map -> !map[key] } ? [] : [key] }

    opts.arguments().each { locale ->
      new File("$opts.target/tznames_${locale}.properties").withWriter { out ->
        translate(opts.source, locale).subMap(keys).each { k, v -> out.writeLine "$k = $v" }
      }
    }
  }

  private Map<String, String> translate(String poPath, String localeId) {
    def tr = { what -> parsePo("$poPath/${localeId}.po")[what] }
    def split = { _ -> _.split '/' }

    TimeZone.getAvailableIDs().collectEntries(new LinkedHashMap<String, String>()) { tzId ->
      def ret = { _ -> [(tzId): _] }

      // Lookup as a whole.
      if (tr(tzId)) ret tr(tzId)
      else if (split(tzId).length > 1) {
        // Assume it's a two-part string, e.g. 'Asia/Novosibirsk' and translate the parts.
        def p0 = split(tzId)[0], p1 = split(tzId)[1]
        ret((tr(p0) && tr(p1)) ? "${tr(p0)}/${tr(p1)}" : null)
      }
      // Fail.
      else ret null
    }
  }

  /**
   * @return Key-value pairs of translated messages.
   */
  @SuppressWarnings("GrMethodMayBeStatic")
  @Memoized
  private Map parsePo(String poPath) {
    new File(poPath)
        .readLines('UTF-8')
        .findAll { line -> !(line.startsWith('#') || line.trim().empty) }
        .with { where ->

      def trim = { _, prefix -> _.replace("$prefix ", '').replace('"', '') }

      def map = [:]
      for (int i = 0; i < where.size() - 1; i++) {
        if (where[i].startsWith('msgid'))
          map << [(trim(where[i], 'msgid')): trim(where[i + 1], 'msgstr')]
      }
      map
    }
  }
}
