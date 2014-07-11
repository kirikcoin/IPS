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
   *//*
  var hideLabelsOfLinearPath = true;*/

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

  function _addPopupsToNodes(renderer) {
    var oldDrawNodes = renderer.drawNodes();
    renderer.drawNodes(function (graph, root) {
      var svgNodes = oldDrawNodes(graph, root);
      svgNodes.append("svg:title").text(function (d) {
        return graph._nodes[d].value['detail'];
      });
      return svgNodes;
    });
  }

  function _addPopupsToLabels(renderer) {
    var oldDrawEdgeLabels = renderer.drawEdgeLabels();
    renderer.drawEdgeLabels(function (graph, root) {
      var svgEdges = oldDrawEdgeLabels(graph, root);
      svgEdges.append("svg:title").text(function (d) {
        return graph._edges[d].value['detail'];
      });
      return svgEdges;
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

  }

  function _postRender() {
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
  }

  var init = function () {
    var graph = options.graph;

    _preConvert(graph);

    var d3Graph = new dagreD3.Digraph();

    $.each(graph.nodes, function (i, node) {
      d3Graph.addNode(node.id, { label: node.label, detail: node.description });
    });

    $.each(graph.edges, function (i, edge) {
      d3Graph.addEdge(null, edge.from, edge.to, { label: edge.label, detail: edge.description });
    });

    var layout = dagreD3.layout().rankDir(options.direction);
    var renderer = new dagreD3.Renderer();

    _addPopupsToNodes(renderer);
    _addPopupsToLabels(renderer);

    renderer = renderer.layout(layout);
    renderer.run(d3Graph, d3.select("#" + contentId + " svg g"));

    _postRender();
  };

  $(function () {
    init();
  });
}
