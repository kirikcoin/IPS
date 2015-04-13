#!/usr/bin/env groovy

//
// Usage:
//
//  ./BuildVersionGenerator <path-to-generated-sources> <Build-ID>
//

package mobi.eyeline.ips.web

def targetDir = args[0]
def buildVersion = args[1]

new File(targetDir, 'mobi/eyeline/ips/web').with { path ->
  path.mkdirs()
  new File(path, 'BuildVersion.java').text =
      """
package mobi.eyeline.ips.web;

public class BuildVersion {
    public static final String BUILD_VERSION = "${buildVersion}";
}
"""
}

