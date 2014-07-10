function createTree(contentId, options) {
  var c = new Tree(contentId, options);

  registerJsfComponent(contentId, c);
}

function Tree(contentId, options) {
  var $divElement = $("#" + contentId);

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

  var init = function () {
    var g = new dagreD3.Digraph();

    $.each(options.graph.nodes, function (i, node) {
      g.addNode(node.id, { label: node.label, detail: node.description });
    });

    $.each(options.graph.edges, function (i, edge) {
      g.addEdge(null, edge.from, edge.to, { label: edge.label, detail: edge.description });
    });

    var layout = dagreD3.layout().rankDir('LR');
    var renderer = new dagreD3.Renderer();

    _addPopupsToNodes(renderer);
    _addPopupsToLabels(renderer);

    renderer = renderer.layout(layout);
    renderer.run(g, d3.select("#" + contentId + " svg g"));
  };

  $(function () {
    init();
  });
}

