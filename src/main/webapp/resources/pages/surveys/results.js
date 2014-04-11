var page = {

  filterKeyDown: function (event) {
    if (event.keyCode == 13) {
      jsfc('table').update(true);
      return false;
    } else {
      return true;
    }
  },

  init: function () {
    // Nothing here for now.
  }

};

function EyelineDataTableComponent(tableId, tableOptions) {

  var wasLoadImmediately = !tableOptions["immediatelyRendering"];

  var stopLoading = false;

  this.stop = function() {
    stopLoading = true;
  };

  $.ajaxSetup({cache: false});

  var currentPageNumber = tableOptions.currentPageNumber;
  var currentSortOrder = tableOptions.sortOrder;
  if (!currentSortOrder)
    currentSortOrder = "";
  var currentPageSize = tableOptions.pageSize;
  var allRowsSelected = false;
  var currentMode = "all";

  var tableElement = $("#" + tableId);
  var closestForm = tableElement.parents("form");
  var requestUrl = closestForm.attr("action");
  var bodyElement = tableElement.find("tbody");
  var select = $("#" + tableId + "_select");
  var selectAll = $("#" + tableId + "_selectAll");
  var visibleElement = $("#" + tableId + "_visible");
  var visibleColumnsElement = $("#" + tableId + "_visibleColumns");

  var params = serializeValues(closestForm);
  params["eyelineComponentUpdate"] = tableId;

  var requestRows = function(all) {
    if (all) {
      params = serializeValues(closestForm);
      params["eyelineComponentUpdate"] = tableId;
    } else {
      params[tableId + "_column"] = currentSortOrder;
      params[tableId + "_page"] = (currentPageNumber);
      params[tableId + "_pageSize"] = (currentPageSize);
      params[tableId + "_showSelected"] = (currentMode == "all" ? "false" : "true");
      params[tableId + "_select"] = select.val();
      params[tableId + "_selectAll"] = selectAll.val();
      params[tableId + "_visible"] = visibleElement.val();
      params[tableId + "_visibleColumns"] = visibleColumnsElement.val();
    }

    var progress = null;


    var onError = function() {
      progressOverlay.showError(tableOptions.cantLoadLabel);
    };

    var updateCommonElements = function (rowsCount, data, updateData) {
      progressOverlay.hide();
      if(updateData) {
        bodyElement.html(data);
      }
      navbar.setTotal(rowsCount, currentPageNumber);

      var checkboxes = $("[id*='" + tableId + "_rowCheck_']");
      $.each(checkboxes, function (idx, checkbox){
        var rowId = checkbox.getAttribute("id").substring((tableId + "_rowCheck_").length);
        checkbox.onclick = function() {_selectRow(this.checked, rowId)};
      });

      if(bodyElement.find(".eyeline_body_row").length == 0) {
        var eA = $("#"+tableId+"_expandAll");
        var sA = $("#"+tableId+"_selectAllButton");
        if(eA && eA.html()) {
          var cloneExpandAll = eA.clone(true);
          eA.remove();
          tableOptions['cloneExpandAll'] = cloneExpandAll;
        }
        if(sA && sA.html()) {
          var cloneSelectAll = sA.clone(true);
          sA.remove();
          tableOptions['cloneSelectAll'] = cloneSelectAll;
        }
      }else {
        if(tableOptions['cloneExpandAll'] && tableOptions['cloneExpandAll'].html()) {
          tableElement.find("thead").find("tr").prepend(tableOptions['cloneExpandAll']);
          tableOptions['cloneExpandAll'] = null;
        }
        if(tableOptions['cloneSelectAll'] && tableOptions['cloneSelectAll'].html()) {
          tableElement.find("thead").find("tr").prepend(tableOptions['cloneSelectAll']);
          tableOptions['cloneSelectAll'] = null;
        }
      }

      var expanded = $("[id*='" + tableId + "_rowExpand_']");
      $.each(expanded, function(idx, expand) {
        var rowId = expand.getAttribute("id").substring((tableId + "_rowExpand_").length);
        expand.parentElement.parentElement.onclick = function() {_expandRow(rowId)};
      });

      checkExpandAll();

      if(bodyElement.find(".eyeline_inner").length > 0) {
        $("#"+tableId+"_expandAll").children().css("opacity", "1").css("cursor", "pointer");
      }else{
        $("#"+tableId+"_expandAll").children().css("opacity", "0").css("cursor", "default");
      }
    };

    var onResponse = function(data, status, resp) {
      if (status != 'success')
        return;

      if (typeof(data) == "object") {
        if (data.type == "progress") {
          progress = data.data.progress;
          var stopText = data.data.stopButton;
          progressOverlay.showProgress(progress + "%", stopText);
          window.setTimeout(_sendRequest, 1000);
        } else {
          progress = 100;
          progressOverlay.showError(data.data);
        }
      } else {
        var rowsCount = resp.getResponseHeader("rowsCount");
        progress = 100;
        if(rowsCount != null) {
          updateCommonElements(rowsCount, data, true);
          if(data == null || data == '') {
            bodyElement.hide();
          }else {
            bodyElement.show();
          }
        }else {
          onError();
        }
      }
    };

    var _sendRequest = function() {
      params[tableId+"_stop_me"] = stopLoading;
      $.ajax({
        type: 'POST',
        url: requestUrl,
        data: params,
        success: onResponse,
        error: onError
      });
    };


    if(wasLoadImmediately) {
      progress = null;
      progressOverlay.showProgress(progress, null);
      _sendRequest();
    }else {
      if(tableOptions['immediatelyRenderingError']) {
        progressOverlay.showError(tableOptions['immediatelyRenderingError']);
      }else {
        updateCommonElements(tableOptions['immediatelyRenderingTotalCount'], null, false);
        if(bodyElement.html()=='') {
          bodyElement.hide();
        }
      }
      wasLoadImmediately = true;
    }
  };

  var _update = function(all) {
    $("#" + tableId + "_column").val(currentSortOrder);
    $("#" + tableId + "_page").val(currentPageNumber);
    $("#" + tableId + "_pageSize").val(currentPageSize);
    $("#" + tableId + "_showSelected").val(currentMode == "all" ? "false" : "true");
    requestRows(all);
  };

  var _setVisible = function(visible) {
    visibleElement.val(visible);
    tableElement.css("display", visible ? "" : "none");
  };

  var _setColumnVisible = function(columnName, visible) {
    var tds = $("[name*='"+ columnName + "']");
    tds.css("display", visible ? "" : "none");
    navbar.changeWidth(visible ? +1 : -1);
    var visibleColumns = eval('(' + visibleColumnsElement.val() + ')');
    var idx = lookupValueInArray(visibleColumns, columnName);
    if (visible == (idx >= 0))
      return;
    if (!visible)
      visibleColumns = removeValueFromArray(visibleColumns, idx);
    else
      visibleColumns[visibleColumns.length] = columnName
    visibleColumnsElement.val(arrayToJson(visibleColumns));
  };

  this.update = function(all) {
    _update(all);
    return this;
  };

  this.setVisible = function(visible) {
    _setVisible(visible);
    return this;
  }

  this.show = function() {
    _setVisible(true);
    return this;
  };

  this.hide = function() {
    _setVisible(false);
    return this;
  };

  this.setColumnVisible = function(columnName, visible) {
    _setColumnVisible(columnName, visible);
    return this;
  };

  this.showColumn = function(columnName) {
    _setColumnVisible(columnName, true);
    return this;
  };

  this.hideColumn = function(columnName) {
    _setColumnVisible(columnName, false);
    return this;
  };

  var progressOverlay = new ProgressOverlay(tableElement);

  var selectionControl;
  if (tableOptions.selectButton) {
    var _selectRow = this.selectRow = function(checked, rowId) {
      var selectObject = eval('(' + select.val() + ')');
      var idx = lookupValueInArray(selectObject, rowId);
      if (allRowsSelected == checked) {
        if (idx < 0)
          return;
        selectObject = removeValueFromArray(selectObject, idx);
      } else if (idx < 0) {
        selectObject[selectObject.length] = rowId;
      }
      select.val(arrayToJson(selectObject));
      if (allRowsSelected)
        navbar.setSelected(navbar.getTotal() - selectObject.length);
      else
        navbar.setSelected(selectObject.length);
    };

    var selectPage = function() {
      var checkboxes = $("[id*='" + tableId + "_rowCheck_']");
      var checkedBoxes = checkboxes.filter(":checked");

      var checked = (checkedBoxes.length == checkboxes.length);

      var prefixLen = (tableId + "_rowCheck_").length;
      $.each(checkboxes, function(idx, el) {
        el.checked = !checked;
        _selectRow(!checked, el.getAttribute("id").substr(prefixLen));
      });
    };

    selectionControl = new SelectionControl(tableOptions.selectButton, {
      onSelectPage : selectPage,
      labels : tableOptions.selectionLabels,
      onSelectAll : function(checked) {
        $("#" + tableId + "_select").val("[]");
        $("#" + tableId + "_selectAll").val(checked);
        var checkboxes = $("[id*='" + tableId + "_rowCheck_']");
        $.each(checkboxes, function(idx, el) {
          el.checked = checked;
        });
        navbar.setSelected(checked ? navbar.getTotal() : 0);
        allRowsSelected = checked;
      }
    });
  }


  var toggleButton;
  if (tableOptions.toggleButton) {
    var _expandRow = function(rowId) {
      var headerElement = $("[id='" + tableId + "_rowExpand_" + rowId + "']");
      if (headerElement == null)
        return;

      if (headerElement.hasClass('eyeline_inner_data_closed')) {
        headerElement.removeClass('eyeline_inner_data_closed').addClass('eyeline_inner_data_opened');
        $("tr[name='innerData" + tableId +rowId + "']").show();
      } else {
        headerElement.removeClass('eyeline_inner_data_opened').addClass('eyeline_inner_data_closed');
        $("tr[name='innerData" + tableId + rowId + "']").hide();
      }
    };
    var _isRowOpened = function(rowId) {
      var headerElement = $("[id='" + tableId + "_rowExpand_" + rowId + "']");
      if (headerElement == null)
        return false;

      return !headerElement.hasClass('eyeline_inner_data_closed');
    };

    toggleButton = new ToggleButtonControl(tableOptions.toggleButton, {
      onChange : function(opened) {
        if (opened) {
          $("[id*='" + tableId + "_rowExpand_']").removeClass('eyeline_inner_data_closed').addClass('eyeline_inner_data_opened');
          $("tr[name*='innerData" + tableId + "']").show();
        } else {
          $("[id*='" + tableId + "_rowExpand_']").removeClass('eyeline_inner_data_opened').addClass('eyeline_inner_data_closed');
          $("tr[name*='innerData" + tableId + "']").hide();
        }
      }});

  }

  var checkExpandAll = function() {
    if(toggleButton) {
      var openedAll = true;
      var expanded = $("[id*='" + tableId + "_rowExpand_']");

      if (expanded.length) {
        $.each(expanded, function(idx, expand) {
          var rowId = expand.getAttribute("id").substring((tableId + "_rowExpand_").length);
          if(!_isRowOpened(rowId)) {
            openedAll = false;
          }
        });
      } else {
        openedAll = false;
      }

      if(openedAll) {
        toggleButton.open();
      }

      if(toggleButton.isOpened()) {
        $.each(expanded, function(idx, expand) {
          var rowId = expand.getAttribute("id").substring((tableId + "_rowExpand_").length);
          if(!_isRowOpened(rowId)) {
            _expandRow(rowId)
          }
        });
      }
    }
  };

  var navbar = new NavBarControl(tableOptions.navbar, {
    total: tableOptions.totalRows,
    pageSize: currentPageSize,
    selectionEnabled : (selectionControl != null),
    allowedPageSize: [10,20,30,40,50],
    pageSizeRendering: tableOptions.pageSizeRendering,
    pageNumber: currentPageNumber,
    labels : tableOptions.navbarLabels,
    selected: 0,
    onChange : function(pageNumber, pageSize) {
      currentPageNumber = pageNumber;
      currentPageSize = pageSize;
      _update();
    },
    onChangeMode : function(mode) {
      currentMode = mode;
      if (selectionControl)
        selectionControl.changeLock(mode != "all");
      _update();
    }
  });

  var sortableColumns;
  if (tableOptions.columns) {
    sortableColumns = new SortableColumnsControl({
      columnIds: tableOptions.columns,
      sortOrder: currentSortOrder,
      onChange : function(newSort) {
        if (newSort.charAt(0) == '-')
          currentSortOrder = "-" + newSort.substr(tableId.length + 2);
        else
          currentSortOrder = newSort.substr(tableId.length + 1);
        _update();
      }
    });
  }

  if (visibleElement.val() == 'true')
    _update();
}
