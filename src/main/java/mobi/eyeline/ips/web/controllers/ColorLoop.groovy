package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic

@CompileStatic
class ColorLoop {

  private static final List<String> COLORS = [
      'green',
      'yellow',
      'magenta',
      'blue',
      '#ADFF2F',
      '#9ACD32',
      '#00CED1',
      '#7CFC00',
      '#FFE4B5',
      '#ADD8E6',
      '#3CB371',
      '#483D8B',
      '#00FA9A',
      '#6495ED',
      '#FFA500',
      '#00FFFF',
      'black',
      '#66CDAA',
      '#FFD700',
      '#663399',
      '#F0F8FF'
  ]

  static String colorLoop(int idx) {
    COLORS[idx % COLORS.size()]
  }
}
