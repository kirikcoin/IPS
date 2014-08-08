"use strict";

//noinspection JSUnusedGlobalSymbols
function createTree(contentId, options) {
  var c = new Tree(contentId, options);

  registerJsfComponent(contentId, c);
}

/**
 * Renders a directed graph.
 *
 * @param contentId   Containing element identifier.
 * @param options
 * @constructor
 */
function Tree(contentId, options) {
  var $divElement = $("#" + contentId);

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

  function _drawNodes(renderer) {
    var oldDrawNodes = renderer.drawNodes();
    renderer.drawNodes(function (graph, root) {
      var svgNodes = oldDrawNodes(graph, root);
      svgNodes.attr('data-id', function (d) { return d; });
      svgNodes.append("svg:title").text(function (d) {
        return graph.node(d).detail.replace('\\n', ' ');
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
      return svgPaths;
    });
  }

  function _zoom(renderer) {
    var marginX = 10; // px
    renderer.zoom(function (graph, root) {
      return d3.behavior.zoom().on('zoom', function() {
        root.attr('transform', 'translate(' + d3.event.translate + ')scale(' + d3.event.scale + ')');
        $(root[0]).find('.nodes rect').each(function (i, rect) {
          var $rect = $(rect),
              $text = $rect.siblings('g');
          $rect.attr('width', $text[0].getBBox().width + 2 * marginX);
        });
      });
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
                descs = $.map(collapsible, function (e) { return e.description; });

            // Replace collapsed edges with a new one.
            g.edges = _diff(g.edges, collapsible);
            g.edges.push({
              from: from.id,
              to: to.id,
              label: labels.join(', '),
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

            var descs = $.map(connecting, function (e) { return e.description; });

            // Replace collapsed edges with a new one.
            g.edges = _diff(g.edges, connecting);
            g.edges.push({
              from: from.id,
              to: to.id,
              label: '',
              description: descs.join('\n'),
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
    (function () {
      var canvasWidth = $divElement.width(),
          canvasHeight = $divElement.height(),
          $translateInner = $divElement.find('svg > g');

      var bbox = $translateInner[0].getBBox();

      var translateY = 20;
      if (canvasHeight > bbox.height) {
        translateY = (canvasHeight - bbox.height) / 2;
      }

      var translateX = 20;
      if (canvasWidth > bbox.width) {
        translateX = (canvasWidth - bbox.width) / 2;
      }

      $translateInner.attr('transform', 'translate(' + translateX + ', ' + translateY + ')');
    })();

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

    function target(id) {
      return graph._edges[id].v;
    }

    function onHighlight($nodeElem, highlight) {
      var nodeId = $nodeElem.attr('data-id');

      if (highlight)  node(nodeId).attr('class', 'node enter hlSource');
      else            node(nodeId).attr('class', 'node enter');

      $.each(graph.incidentEdges(nodeId), function (i, e) {
        var targetNodeId = target(e);
        if (highlight) {
          // Note: those are SVG elements, so `addClass'/`removeClass' are of no help here.
          if (targetNodeId == nodeId)   path(e).attr('class', 'edgePath enter hlEdgeTo');
          else                          path(e).attr('class', 'edgePath enter hlEdgeFrom');
        } else {
          path(e).attr('class', 'edgePath enter');
        }

        if (highlight) node(targetNodeId).attr('class', 'node enter hlTarget');
        else           node(targetNodeId).attr('class', 'node enter');
      });
    }

    $divElement.find('g.node.enter')
        .on('mouseover',  function () { onHighlight($(this), true); })
        .on('mouseout',   function () { onHighlight($(this), false); });
  }

  var init = function () {
    var graph = options.graph;

    _preConvert(graph);

    var d3Graph = new dagreD3.Digraph();

    $.each(graph.nodes, function (i, node) {
      d3Graph.addNode(node.id, { label: node.label, detail: node.description });
    });

    $.each(graph.edges, function (i, edge) {
      d3Graph.addEdge(null, edge.from, edge.to, {
        label: edge.label,
        detail: edge.description,
        collapsedLinearPathIds: edge.collapsedLinearPathIds
      });
    });

    var layout = dagreD3.layout().rankDir(options.direction);
    var renderer = new dagreD3.Renderer();

    _drawNodes(renderer);
    _drawEdgeLabels(renderer);
    _drawEdgePaths(renderer);
    _zoom(renderer);

    renderer = renderer.layout(layout);
    renderer.run(d3Graph, d3.select("#" + contentId + " svg g"));

    _postRender(d3Graph);
  };

  $(function () {
    init();
  });
}
