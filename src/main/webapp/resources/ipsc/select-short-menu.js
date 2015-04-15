"use strict";

ips.SelectShortMenu = function ($select) {

  /**
   * @param {string} val
   * @return {string}
   */
  var getShortValue = function (val) { return val.split('.')[0]; };

  // Save long labels into `data'-attributes.
  function saveLabels() {
    $select.find('option').each(function () {
      var $opt = $(this);
      if (!$opt.hasClass('placeholder')) {
        var longValue = $opt.html();
        var shortValue = getShortValue(longValue);
        $opt.data('long-label', longValue).data('short-label', shortValue).html(shortValue);
      }
    });
  }

  function expandLabels() {
    $select.find('option').each(function () {
      var $opt = $(this);
      $opt.html($opt.data('long-label'));
    });
  }

  function shortenLabels() {
    $select.find('option').each(function () {
      var $opt = $(this);
      $opt.html($opt.data('short-label'));
    });
  }

  //
  //  Mark the element as already processed to avoid repeated processing.
  //

  if ($select.hasClass('select-short-menu')) {
    return;
  }
  $select.addClass('select-short-menu');

  saveLabels();


  //
  //  Bind event listeners.
  //

  $select.off();

  $select.on('focus', function () {
    expandLabels();
  });

  $select.on('blur', function () { shortenLabels(); });
  $select.on('change', function () {
    shortenLabels();
    $select.trigger('blur');
  });

  $select.keydown(function (e) {
    e.cancel = true;
    return false;
  });
};