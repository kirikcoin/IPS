package mobi.eyeline.ips.model

import groovy.transform.CompileStatic

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@CompileStatic
@Embeddable
class RespondentSource {

  /**
   * Канал респондента.
   * <br/>
   * В случае звонка на C2S-номер - значение этого номера, в случае Telegram - значение ChatId;
   * в противном случае пусто.
   */
  @Column(name = 'source')
  String source

  @Column(name = 'source_type')
  @Enumerated(EnumType.STRING)
  RespondentSourceType sourceType

  boolean equals(o) {
    if (this.is(o)) return true
    if (getClass() != o.class) return false

    final that = o as RespondentSource
    return source == that.source && sourceType == that.sourceType
  }

  int hashCode() {
    int result
    result = (source != null ? source.hashCode() : 0)
    result = 31 * result + (sourceType != null ? sourceType.hashCode() : 0)
    return result
  }

  String getName() {
    sourceType == RespondentSourceType.TELEGRAM ? 'Telegram' : source
  }

  @Override String toString() { "RespondentSource{source='$source', sourceType=$sourceType}" }

  /**
   * {@code null} stands for unknown source.
   */
  static enum RespondentSourceType {
    C2S,
    TELEGRAM
  }
}
