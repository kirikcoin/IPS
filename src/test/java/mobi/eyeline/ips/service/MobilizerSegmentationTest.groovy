package mobi.eyeline.ips.service

import static mobi.eyeline.ips.service.MobilizerSegmentation.checkOptionLength
import static mobi.eyeline.ips.utils.SurveyBuilder.survey

/**
 * These tests check validity of all the surveys found in the production DB at the moment
 * an additional length check got implemented.
 *
 * <p>Auto-generated.
 *
 * @see mobi.eyeline.ips.repository.TestGenerator
 */
@SuppressWarnings("GroovyDocCheck")
class MobilizerSegmentationTest extends GroovyTestCase {

  void test1() {
    def survey = survey(id: 1) {
      questions {
        question(title: 'Ako ste boli celkovo spokojný pri návšteve Obchodného centra?') {
          option(answer: 'Mimoriadne spokojný')
          option(answer: 'Veľmi spokojný')
          option(answer: 'Spokojný')
          option(answer: 'Skôr nespokojný')
          option(answer: 'Nespokojný')
        }

        question(title: 'Uveďte dôvody Vášho hodnotenia') {
          option(answer: 'Nebola mi jasná fakturovaná čiastka')
          option(answer: 'Uvažoval som o zmene operátora ale presvedčili ste ma zostať u Vás')
        }
      }
    }

    assertTrue checkOptionLength(survey.questions[0]) == null
    assertTrue checkOptionLength(survey.questions[1]) != null
  }

  void test31503() {
    def survey = survey(id: 31503) {
      questions {
        question(title: 'Chcete sa zucastnit ankety? Ak stlacite "ano" mozno nieco vyhrate') {
          option(answer: 'Ano')
          option(answer: 'Nie')
        }
      }
    }

    assertNull checkOptionLength(survey.questions[0])
  }

  void test31507() {
    def survey = survey(id: 31507) {
      questions {
        question(title: 'OBCHOD.SK doručili sme Vám zásielku ID 31507. Ohodnoťte kvalitu') {
          option(answer: 'Pokračovať v hodnotení')
          option(answer: 'Koniec')
        }

        question(title: 'Boli ste celkovo spokojný s kvalitou zásielky') {
          option(answer: 'Áno')
          option(answer: 'Čiastočne')
          option(answer: 'Nie')
        }

        question(title: 'Čo by ste zlepšili?') {
          option(answer: 'Rýchlosť doručenia')
          option(answer: 'Komunikáciu o doručovaní')
          option(answer: 'Kvalita obalu')
          option(answer: 'Cena poštovného')
        }
      }
    }

    assertNull checkOptionLength(survey.questions[0])
    assertNull checkOptionLength(survey.questions[1])
    assertNull checkOptionLength(survey.questions[2])
  }

  void test31508() {
    def survey = survey(id: 31508) {
      questions {
        question(title: 'Чтобы получить купон, пройдите опрос') {
          option(answer: 'Дальше')
        }
      }
    }

    assertNull checkOptionLength(survey.questions[0])
  }

  void test31547() {
    def survey = survey(id: 31547) {
      questions {
        question(title: 'Dobry den! Prieskum o kvalite zivota.') {
          option(answer: 'Ano, chcem sa zucastnit')
          option(answer: 'Ne, nechcem sa zucastnit')
        }
        question(title: 'Je dnesni den pro Vas, uspesnej?') {
          option(answer: 'Ano je ')
          option(answer: 'Ne neni')
          option(answer: 'nechci odpovedet')
        }
      }
    }

    assertNull checkOptionLength(survey.questions[0])
    assertNull checkOptionLength(survey.questions[1])
  }

  void test31559() {
    def survey = survey(id: 31559) {
      questions {
        question(title: 'š,č,ť,ž ä, ...., €, $') {
          option(answer: 'š,č,ť,ž')
          option(answer: 'ä, ...., €, $')
        }

        question(title: 'Next\nquestion') {
          option(answer: 'Option 1')
          option(answer: 'Option 2')
        }

      }
    }

    assertNull checkOptionLength(survey.questions[0])
    assertNull checkOptionLength(survey.questions[1])
  }

  void test31560() {
    def survey = survey(id: 31560) {
      questions {
        question(title: 'Robime bezplatny prieskum spolocnosti VAS OPERATOR. Zucastnite sa?') {
          option(answer: 'Ano')
          option(answer: 'Nie')
          option(answer: 'Neskor')
        }
        question(title: 'Ktory z nasich produktov najcastejcie pouzivate?') {
          option(answer: 'SMS')
          option(answer: 'Internet')
          option(answer: 'Aplikacie')
          option(answer: 'Vsetky')
        }
        question(title: 'Ake su hlavne motivy pre zmenu?') {
          option(answer: 'Cena')
          option(answer: 'Kvalita')
          option(answer: 'Dostupnost sluzieb')
          option(answer: 'Komunikacia')
        }
      }
    }

    assertNull checkOptionLength(survey.questions[0])
    assertNull checkOptionLength(survey.questions[1])
    assertNull checkOptionLength(survey.questions[2])
  }

  void test31575() {
    def survey = survey(id: 31575) {
      questions {
        question(title: 'Zúčastníš sa prieskumu?') {
          option(answer: 'Áno')
          option(answer: 'Nie')
        }

        question(title: 'Ako sa dnes cítiš?') {
          option(answer: 'Som zaneprázdnený')
          option(answer: 'Oddychujem')
          option(answer: 'Som úplne mimo')
        }

        question(title: 'Plánuješ cestovať?') {
          option(answer: 'Áno')
          option(answer: 'Nie')
        }

        question(title: 'Kde plánuješ ísť?') {
          option(answer: 'po Slovensku')
          option(answer: 'Európa')
          option(answer: 'Rusko')
        }

        question(title: 'Máš vízum?') {
          option(answer: 'Áno')
          option(answer: 'Nie')
        }

        question(title: 'Ako dlho platí vízum?') {
          option(answer: '1 mesiac')
          option(answer: 'pol roka')
          option(answer: '1 rok')
          option(answer: 'viac ako 1 rok')
        }

        question(title: 'Očakávaš problémy s vydaním víza?') {
          option(answer: 'Áno')
          option(answer: 'Nie')
        }

        question(title: 'Charakter cesty.') {
          option(answer: 'Biznis')
          option(answer: 'Oddych')
          option(answer: 'Návšteva priateľov')
        }

      }
    }

    assertNull checkOptionLength(survey.questions[0])
    assertNull checkOptionLength(survey.questions[1])
    assertNull checkOptionLength(survey.questions[2])
    assertNull checkOptionLength(survey.questions[3])
    assertNull checkOptionLength(survey.questions[4])
    assertNull checkOptionLength(survey.questions[5])
    assertNull checkOptionLength(survey.questions[6])
    assertNull checkOptionLength(survey.questions[7])
  }

  void test31588() {
    def survey = survey(id: 31588) {
      questions {
        question(title: 'Ako ste boli celkovo spokojný pri návšteve Obchodného centra?') {
          option(answer: 'Mimoriadne spokojný')
          option(answer: 'Veľmi spokojný')
          option(answer: 'Spokojný')
          option(answer: 'Skôr nespokojný')
          option(answer: 'Nespokojný')
        }

        question(title: 'Ako ste boli spokojný s prístupom predajcu?') {
          option(answer: 'Mimoriadne spokojný')
          option(answer: 'Veľmi spokojný')
          option(answer: 'Spokojný')
          option(answer: 'Skôr nespokojný')
          option(answer: 'Nespokojný')
        }

        question(title: 'Ponúkol Vám predajca aj príslušenstvo k Vášmu zariadeniu?') {
          option(answer: 'Nie')
          option(answer: 'Áno, kúpil som si')
          option(answer: 'Áno ale nekúpil som si ho')
        }

        question(title: 'Aký bol dôvod návštevy Obchodného centra?') {
          option(answer: 'Služby (faktúra, osobné údaje, program, ...)')
          option(answer: 'Nákup (nové zariadenie)')
          option(answer: 'Informácie')
        }

        question(title: 'Uveďte dôvody Vášho hodnotenia') {
          option(answer: 'Som spokojný s Vašimi službami a odporučím ich ďalším')
          option(answer: 'Vaše služby sú inovatívne a rád si ich osobne overím')
          option(answer: 'Chcel som zmeniť programy za efektívnejšie')
          option(answer: 'Nebola mi jasná fakturovaná čiastka')
          option(answer: 'Som nespokojný s ponúkanými programami a odchádzam od Vás')
          option(answer: 'Ste príliš drahí pre moje potreby')
          option(answer: 'Uvažoval som o zmene operátora ale presvedčili ste ma zostať u Vás')
        }
      }
    }

    assertNull checkOptionLength(survey.questions[0])
    assertNull checkOptionLength(survey.questions[1])
    assertNull checkOptionLength(survey.questions[2])
    assertNull checkOptionLength(survey.questions[3])

    assertEquals 0, checkOptionLength(survey.questions[4])
  }

}
