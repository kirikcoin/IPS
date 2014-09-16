"use strict";

//noinspection JSUnusedGlobalSymbols
function createTree(clientId, parentId, options) {
  var c = new Tree(clientId, options);

  jsfcomponents.addPageComponent(c, parentId);
}

/**
 * Renders a directed graph.
 *
 * @param clientId   Containing element identifier.
 * @param options
 * @constructor
 */
function Tree(clientId, options) {

  this.id = jsfcomponents.clientId2Id(clientId);

  var $divElement = $("#" + this.id);

  var $events = $({});

  /**
   * If true, edges originating from and targeted to the same nodes
   * are collapsed into a single one.
   * The resulting edge inherits all the labels.
   * @type {boolean}
   */
  var collapseEdges = true;

  /**
   * Hide labels for all the edges which:
   * <ol>
   *   <li>Have the same origin O,
   *   <li>Have the same target T, and
   *   <li>There are no edges from O to other nodes.
   * </ol>
   *
   * @type {boolean}
   */
  var hideLabelsOfLinearPath = true;

  /**
   * If set, zoom is only available using toolbar buttons.
   * @type {boolean}
   */
  var zoomButtonsOnly = true;

  var initOnLoad = false;

  this.setVisible = function (visible) {
    $divElement.show(visible);
  };

  var _setVisible = this.setVisible;

  this.show = function () {
    _setVisible(true);
  };

  this.hide = function () {
    _setVisible(false);
  };

  /**
   * Binds an event listener.
   *
   * @param event     Event type.
   * @param listener  Listener instance.
   */
  this.bind = function (event, listener) {
    $events.on(event, listener);
  };

  /**
   * Removes event listener.
   *
   * @param event     Event type.
   * @param listener  Event listener instance.
   *                  If null, all the listeners for this event type are removed.
   */
  this.unbind = function (event, listener) {
    if (listener)   $events.off(event, listener);
    else            $events.off(event);
  };

  this.zoomReset = function () {
    _zoomReset();
  };

  this.init = function (force) {
    init(force);
  };

  function _drawNodes(renderer) {
    var oldDrawNodes = renderer.drawNodes();
    renderer.drawNodes(function (graph, root) {
      $.each(graph._nodes, function (i, e) {
        var node = e.value;
        node.label = node.label.replace(/\\r\\n/g, '\n');
      });

      var svgNodes = oldDrawNodes(graph, root);
      svgNodes.attr('data-id', function (d) { return d; });
      svgNodes.append("svg:title").text(function (d) {
        return graph.node(d).detail
            .replace(/\\r\\n/g, ' ')
            .replace(/\\r/g, ' ')
            .replace(/\\n/g, ' ');
      });
      svgNodes.attr('class', function (d) {
        var styleClass = graph.node(d).styleClass ? graph.node(d).styleClass : '';
        return 'node enter ' + styleClass;
      });
      return svgNodes;
    });
  }

  function _drawEdgeLabels(renderer) {
    var oldDrawEdgeLabels = renderer.drawEdgeLabels();
    renderer.drawEdgeLabels(function (graph, root) {
      var svgEdges = oldDrawEdgeLabels(graph, root);
      svgEdges.append("svg:title").text(function (d) {
        return graph.edge(d).detail;
      });
      return svgEdges;
    });
  }

  function _drawEdgePaths(renderer) {
    var oldDrawEdgePaths = renderer.drawEdgePaths();
    renderer.drawEdgePaths(function (graph, root) {
      var svgPaths = oldDrawEdgePaths(graph, root);
      svgPaths.attr('data-id', function (d) { return d; });
      svgPaths.append("svg:title").text(function (d) {
        var edge = graph.edge(d);
        return edge.collapsedLinearPathIds ? edge.detail : undefined;
      });
      svgPaths.attr('class', function (d) {
        var styleClass = graph.edge(d).styleClass ? graph.edge(d).styleClass : '';
        return 'edgePath enter ' + styleClass;
      });
      return svgPaths;
    });
  }

  function _resizeText($wrapper) {
    var marginX = 10; // px
    $wrapper.find('.nodes rect').each(function (i, rect) {
      var $rect = $(rect),
          $text = $rect.siblings('g');
      $rect.attr('width', $text[0].getBBox().width + 2 * marginX);
    });
  }

  function _zoom(renderer) {
    renderer.zoom(function (graph, root) {
      if (zoomButtonsOnly) {
        return d3.behavior.drag().on('drag', function() {
          _updateTransform(function (transform) {
            transform.translate[0] += d3.event.dx;
            transform.translate[1] += d3.event.dy;
          });
        });

      } else {
        return d3.behavior.zoom().on('zoom', function() {
          root.attr('transform', 'translate(' + d3.event.translate + ')scale(' + d3.event.scale + ')');
          _resizeText($divElement);
        });
      }
    });
  }

  function _diff(from, what) {
    return $.grep(from, function (e) {
      return $.inArray(e, what) == -1;
    });
  }

  function _preConvert(graph) {

    while (collapseEdges && (function (g) {
      for (var i1 = 0; i1 < g.nodes.length; i1++) {
        var from = g.nodes[i1];

        for (var i2 = 0; i2 < g.nodes.length; i2++) {
          var to = g.nodes[i2];

          var collapsible = $.grep(g.edges, function (e) {
            //noinspection JSReferencingMutableVariableFromClosure
            return e.from == from.id && e.to == to.id;
          });

          if (collapsible.length > 1) {
            var labels = $.map(collapsible, function (e) { return e.label; }),
                descs = $.map(collapsible, function (e) { return e.description; }),
                styles = $.map(collapsible, function (e) { return e.styleClass ? e.styleClass : ''; });

            // Replace collapsed edges with a new one.
            g.edges = _diff(g.edges, collapsible);
            g.edges.push({
              from: from.id,
              to: to.id,
              label: labels.join(', '),
              styleClass: styles.join(' '),
              description: descs.join('\n'),
              collapsedIds: $.map(collapsible, function (e) { return e.id; })
            });

            return true;
          }
        } // for i2
      } // for i1
      return false;
    })(graph)) {}

    while (collapseEdges && hideLabelsOfLinearPath && (function (g) {
      for (var i1 = 0; i1 < g.nodes.length; i1++) {
        var from = g.nodes[i1];

        for (var i2 = 0; i2 < g.nodes.length; i2++) {
          var to = g.nodes[i2];

          var nonConnecting = $.grep(g.edges, function (e) {
            //noinspection JSReferencingMutableVariableFromClosure
            return e.from == from.id && e.to != to.id;
          });

          var connecting = $.grep(g.edges, function (e) {
            //noinspection JSReferencingMutableVariableFromClosure
            return e.from == from.id && e.to == to.id;
          });

          if (!nonConnecting.length && ((connecting.length > 1) ||
              ((connecting.length == 1) && !connecting[0].hiddenLabels))) {

            var descs = $.map(connecting, function (e) { return e.description; }),
                styles = $.map(connecting, function (e) { return e.styleClass ? e.styleClass : ''; });

            // Replace collapsed edges with a new one.
            g.edges = _diff(g.edges, connecting);
            g.edges.push({
              from: from.id,
              to: to.id,
              label: '',
              description: descs.join('\n'),
              styleClass: styles.join(' '),
              collapsedLinearPathIds: $.map(connecting, function (e) { return e.id; }),
              hiddenLabels: true
            });

            return true;
          }
        } // for i2
      } // for i1
      return false;
    })(graph)) {}
  }

  function _postRender(d3Graph) {

    // Center depending on orientation.
    _zoomReset();

    // Bind node click events.
    (function () {
      $divElement.find('g.node.enter')
          .on('click',  function () {
            $events.trigger('node-click', { id: $(this).attr('data-id') });
          });
    })();

    _setupHighlight(d3Graph);
  }

  function _setupHighlight(graph) {
    function path(id) {
      return $divElement.find('g.edgePath.enter[data-id="' + id + '"]');
    }

    function node(id) {
      return $divElement.find('g.node.enter[data-id="' + id + '"]');
    }

    function target(id) { return graph._edges[id].v; }
    function source(id) { return graph._edges[id].u; }

    function getEdgeClass(e, highlight, nodeId) {
      var dstId = target(e);
      var srcId = source(e);
      if (highlight) {
        if      (nodeId == dstId && nodeId == srcId)  return 'hlEdgeFrom hlEdgeTo';
        else if (nodeId == dstId)                     return 'hlEdgeTo';
        else                                          return 'hlEdgeFrom';

      } else {
        return '';
      }
    }

    function onHighlight($nodeElem, highlight) {
      var nodeId = $nodeElem.attr('data-id');

      if (highlight)  node(nodeId).attr('class', 'node enter hlSource');
      else            node(nodeId).attr('class', 'node enter');

      $.each(graph.incidentEdges(nodeId), function (i, e) {
        // Note: those are SVG elements, so `addClass'/`removeClass' are of no help here.
        var edgeHlClass = getEdgeClass(e, highlight, nodeId);
        var edgeStyleClass = graph.edge(e).styleClass ? graph.edge(e).styleClass : '';
        path(e).attr('class', 'edgePath enter ' + edgeStyleClass + ' ' + edgeHlClass);

        var dstId = target(e);
        var nodeStyleClass = graph.node(dstId).styleClass ? graph.node(dstId).styleClass : '';
        if (highlight) node(dstId).attr('class', 'node enter ' + nodeStyleClass + ' hlTarget');
        else           node(dstId).attr('class', 'node enter ' + nodeStyleClass);
      });
    }

    $divElement.find('g.node.enter')
        .on('mouseover',  function () { onHighlight($(this), true); })
        .on('mouseout',   function () { onHighlight($(this), false); });
  }

  function _updateTransform(callback) {
    var zoomCanvas = d3.select($divElement.find('.zoom')[0]);
    var transform = d3.transform(zoomCanvas.attr('transform'));
    callback(transform);
    zoomCanvas.attr('transform', transform);

    _resizeText($divElement);
  }

  function _zoomIn() {
    _updateTransform(function (transform) {
      transform.scale[0] *= 1.1;
      transform.scale[1] *= 1.1;
    });
  }

  function _zoomOut() {
    _updateTransform(function (transform) {
      transform.scale[0] *= 0.9;
      transform.scale[1] *= 0.9;
    });
  }

  function _zoomReset() {
    function centerIfFits() {
      var canvasWidth = $divElement.width(),
          canvasHeight = $divElement.height(),
          $translateInner = $divElement.find('.zoom');

      var bbox = $translateInner[0].getBoundingClientRect();

      var translateY = (canvasHeight - bbox.height) / 2;
      var translateX = (canvasWidth - bbox.width) / 2;

      if (translateX > 0 && translateY > 0) {
        _updateTransform(function (transform) { transform.translate = [translateX, translateY]; });
        return true;

      } else {
        return false;
      }
    }

    _updateTransform(function (transform) {
      transform.scale = [1, 1];
    });

    while (!centerIfFits()) {
      _zoomOut();
    }
  }

  function _bindToolbar() {
    var $toolbar = $divElement.find('.eyeline_tree_toolbar');

    $toolbar.find('.zoom_in').on('click', function () { _zoomIn(); });
    $toolbar.find('.zoom_out').on('click', function () { _zoomOut(); });
    $toolbar.find('.zoom_reset').on('click', function () { _zoomReset(); });
  }

  var init = function (force) {
    if (!initOnLoad && !force) {
      // Called by JSFc library
      return;
    }

    /**
     * Copy nodes and edges from graph to d3Graph.
     * @param d3Graph
     * @param graph
     */
    function initD3Graph(d3Graph, graph) {
      $.each(graph.nodes, function (i, node) {
        var attrs = { label: node.label, detail: node.description };
        if (node.styleClass)  attrs['styleClass'] = node.styleClass;
        d3Graph.addNode(node.id, attrs);
      });
      $.each(graph.edges, function (i, edge) {
        var attrs = { collapsedLinearPathIds: edge.collapsedLinearPathIds };
        if (edge.label)       attrs['label'] = edge.label;
        if (edge.description) attrs['detail'] = edge.description;
        if (edge.styleClass)  attrs['styleClass'] = edge.styleClass;
        d3Graph.addEdge(null, edge.from, edge.to, attrs);
      });
    }

    var graph = options.graph;
    _preConvert(graph);

    var d3Graph = new dagreD3.Digraph();
    initD3Graph(d3Graph, graph);

    var layout = dagreD3.layout().rankDir(options.direction);
    var renderer = new dagreD3.Renderer();

    // Dagre-d3 behavior overrides.
    _drawNodes(renderer);
    _drawEdgeLabels(renderer);
    _drawEdgePaths(renderer);
    _zoom(renderer);

    _bindToolbar();

    renderer = renderer.layout(layout);
    renderer.run(d3Graph, d3.select("#" + jsfcomponents.clientId2Id(clientId) + " svg g"));

    _postRender(d3Graph);
  };
}
