/**
 * Created with IntelliJ IDEA.
 * User: MBP13
 * Date: 23.01.14
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */

var ips = new (function() {

  var VALIDATION_ERROR_CLASS = "validationError";

  this.$byId = function(id) {
    var $res = $(document.getElementById('content:' + id));
    if ($res.length == 0)
      $res = $(document.getElementById(id));
    return $res;
  };

  /**
   * API для работы с сообщением
   */
  this.message = new (function() {

    var JSF_MESSAGES_ELEMENT_ID = "#jsf_messages";
    var PAGE_MESSAGES_ELEMENT_ID = "#page_messages";

    /**
     * Отображает на экране сообщение об ошибке
     * @param {String} errorText текст сообщения об ошибке
     * @param {String} [elementId] опциональный идентификатор DOM-элемента, из-за которого произошла ошибка. К данному элементу будет применен стиль validationError
     */
    this.error = function (errorText, elementId, skipMarker) {
      $(JSF_MESSAGES_ELEMENT_ID).hide();

      var $ips_page_errors = $(PAGE_MESSAGES_ELEMENT_ID);
      $ips_page_errors.empty();
      $ips_page_errors.append('<li class=\'error-message\'><span>' + errorText + '</span></li>');
      $ips_page_errors.show();

      if(elementId) {
        var el = ips.$byId(elementId);
        el.addClass(VALIDATION_ERROR_CLASS);

        if (!skipMarker) {
          var nextEl = el.next();
          if(!nextEl.is('span.error'))
            el.after('<span class="error" title="'+errorText+'"> (!)</span>');
        }
      }
    };

    /**
     * Отображает на экране информационное сообщение
     * @param {String} infoText текст сообщения
     */
    this.info = function(infoText) {
      $(JSF_MESSAGES_ELEMENT_ID).hide();

      var $ips_page_errors = $(PAGE_MESSAGES_ELEMENT_ID);
      $ips_page_errors.empty();
      $ips_page_errors.append('<li class=\'info-message\'><span>' + infoText + '</span></li>');
      $ips_page_errors.show();
    };

    /**
     * Прячет сообщение об ошибке, если оно отображается на экране
     * @param {String} [elementId] опциональный идентификатор элемента, из-за которого произошла ошибки или внутри которого есть элемент, явившийся причиной ошибки.
     */
    this.hide = function(elementId) {
      $(JSF_MESSAGES_ELEMENT_ID).hide();
      $(PAGE_MESSAGES_ELEMENT_ID).hide();

      if(elementId) {
        var $element = ips.$byId(elementId);
        $element.removeClass(VALIDATION_ERROR_CLASS);
        if($element.next().is('span.error'))
          $element.next().remove();

        $element.find('.' + VALIDATION_ERROR_CLASS).each(function() {
          var el = $(this);
          el.removeClass(VALIDATION_ERROR_CLASS);
          var $nextEl = el.next();
          if($nextEl.is('span.error')) {
            $nextEl.remove();
          }
        });
      }
    };

    /**
     * Hides on-screen messages along with removing error markup on input elements (if any).
     */
    this.hideAll = function() {
      this.hide();
      $('.' + VALIDATION_ERROR_CLASS).each(function (i, e) { ips.message.hide(e.id); });
    };

    $(function() {
      $(JSF_MESSAGES_ELEMENT_ID).click(function(){$(this).hide()});
      $(PAGE_MESSAGES_ELEMENT_ID).click(function(){$(this).hide()});
    });

  })();

  /**
   * Страницы интерфейса
   * @type {{}}
   */
  this.pages = {};

})();


// Prohibits multiple form submits by removing `onclick' actions
// from all the links marked with the specified class once they're clicked.
(function (submitButtonClass) {
  $(function () {
    $('a.' + submitButtonClass).each(function (i, elem) {
      var $elem = $(elem);

      var oldClick = $elem.attr('onclick');
      $elem.attr('onclick',
          '$(this).attr("onclick", "return false;").unbind("click"); ' + oldClick);
    });
  });
})('btnSubmit');