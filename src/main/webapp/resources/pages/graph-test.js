var page = {

  getObj: function () {

    var page4 = {
      name: 'page-4'
    };

    return {
      name: 'start page',
      children: [
        {
          name: 'page-1',
          children: [
//            page4
            {
              name: 'page-4'
            }
          ]
        },
        {
          name: 'page-2',
          children: [
            page4
          ]
        },
        {
          name: 'page-3',
          children: [
            {
              name: 'page-5'
            }
          ]
        }
      ]
    };

  },

  /*init: function () {
    //*var vis = d3.select("#graph")
        .append("svg");
    var w = 900,
        h = 400;

    var circleWidth = 5;

    var palette = {
      "lightgray": "#819090",
      "gray": "#708284",
      "mediumgray": "#536870",
      "darkgray": "#475B62",

      "darkblue": "#0A2933",
      "darkerblue": "#042029",

      "paleryellow": "#FCF4DC",
      "paleyellow": "#EAE3CB",
      "yellow": "#A57706",
      "orange": "#BD3613",
      "red": "#D11C24",
      "pink": "#C61C6F",
      "purple": "#595AB7",
      "blue": "#2176C7",
      "green": "#259286",
      "yellowgreen": "#738A05"
    }

    vis.attr("width", w)
        .attr("height", h);

    var nodes = [
      {"name": "Matteo" },
      {"name": "Daniele"},
      {"name": "Marco"},
      {"name": "Lucio"},
      {"name": "Davide" }
    ];

    var links = [
      {source: nodes[0], target: nodes[1]},
      {source: nodes[1], target: nodes[2]},
      {source: nodes[0], target: nodes[3]},
      {source: nodes[4], target: nodes[2]},
      {source: nodes[2], target: nodes[3]}
    ];

    var force = d3.layout.cluster()
        .nodes(nodes)
        .links([])
        .gravity(0.1)
        .charge(-1000)
        .size([w, h]);

    var link = vis.selectAll(".link")
        .data(links)
        .enter().append("line")
        .attr("class", "link")
        .attr("stroke", "#CCC")
        .attr("fill", "none");

    var node = vis.selectAll("circle.node")
        .data(nodes)
        .enter().append("g")
        .attr("class", "node")

      //MOUSEOVER
        .on("mouseover", function(d,i) {
          if (i>0) {
            //CIRCLE
            d3.select(this).selectAll("circle")
                .transition()
                .duration(250)
                .style("cursor", "none")
                .attr("r", circleWidth+3)
                .attr("fill",palette.orange);

            //TEXT
            d3.select(this).select("text")
                .transition()
                .style("cursor", "none")
                .duration(250)
                .style("cursor", "none")
                .attr("font-size","1.5em")
                .attr("x", 15 )
                .attr("y", 5 )
          } else {
            //CIRCLE
            d3.select(this).selectAll("circle")
                .style("cursor", "none")

            //TEXT
            d3.select(this).select("text")
                .style("cursor", "none")
          }
        })

      //MOUSEOUT
        .on("mouseout", function(d,i) {
          if (i>0) {
            //CIRCLE
            d3.select(this).selectAll("circle")
                .transition()
                .duration(250)
                .attr("r", circleWidth)
                .attr("fill",palette.pink);

            //TEXT
            d3.select(this).select("text")
                .transition()
                .duration(250)
                .attr("font-size","1em")
                .attr("x", 8 )
                .attr("y", 4 )
          }
        })

        .call(force.drag);


    //CIRCLE
    node.append("svg:circle")
        .attr("cx", function(d) { return d.x; })
        .attr("cy", function(d) { return d.y; })
        .attr("r", circleWidth)
        .attr("fill", function(d, i) { if (i>0) { return  palette.pink; } else { return palette.paleryellow } } )

    //TEXT
    node.append("text")
        .text(function(d, i) { return d.name; })
        .attr("x",    function(d, i) { return circleWidth + 5; })
        .attr("y",            function(d, i) { if (i>0) { return circleWidth + 0 }    else { return 8 } })
        .attr("font-family",  "Bree Serif")
        .attr("fill",         function(d, i) {  return  palette.paleryellow;  })
        .attr("font-size",    function(d, i) {  return  "1em"; })
        .attr("text-anchor",  function(d, i) { if (i>0) { return  "beginning"; }      else { return "end" } })



    force.on("tick", function(e) {
      node.attr("transform", function(d, i) {
        return "translate(" + d.x + "," + d.y + ")";
      });

      link.attr("x1", function(d)   { return d.source.x; })
          .attr("y1", function(d)   { return d.source.y; })
          .attr("x2", function(d)   { return d.target.x; })
          .attr("y2", function(d)   { return d.target.y; })
    });

    force.start();*//*

    var width = 600,
        height = 800;

    var cluster = d3.layout.tree()
        .size([height, width]);

    var diagonal = d3.svg.diagonal()
        .projection(function(d) { return [d.y, d.x]; });

    var svg = d3.select("#graph").append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        *//*.attr("transform", "translate(40,0)")*//*;

      var nodes = cluster.nodes(page.getObj()),
          links = cluster.links(nodes);

      var link = svg.selectAll(".link")
          .data(links)
          .enter().append("path")
          .attr("class", "link")
          .attr("d", diagonal);

      var node = svg.selectAll(".node")
          .data(nodes)
          .enter().append("g")
          .attr("class", "node")
          .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; })

      node.append("circle")
          .attr("r", 4.5);

      node.append("text")
          .attr("dx", function(d) { return d.children ? -8 : 8; })
          .attr("dy", 3)
          .style("text-anchor", function(d) { return d.children ? "end" : "start"; })
          .text(function(d) { return d.name; });

//    d3.select(self.frameElement).style("height", height + "px");


    function zoomed() {
      svg.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
      svg.select(".state-border").style("stroke-width", 1.5 / d3.event.scale + "px");
      svg.select(".county-border").style("stroke-width", .5 / d3.event.scale + "px");
    }

    var zoom = d3.behavior.zoom()
        .translate([0, 0])
        .scale(1)
        .scaleExtent([1, 8])
        .on("zoom", zoomed);

    svg.append("rect")
        .attr("class", "overlay")
        .attr("width", width)
        .attr("height", height)
        .call(zoom);

  }
*/

  init: function () {
    // Create a new directed graph
    var g = new dagreD3.Digraph();

// Add nodes to the graph. The first argument is the node id. The second is
// metadata about the node. In this case we're going to add labels to each of
// our nodes.
    g.addNode("1",    { label: "Вопрос 1" });
    g.addNode("2",  { label: "Вопрос 2" });
    g.addNode("3",      { label: "Вопрос 3" });
    g.addNode("4",      { label: "Вопрос 4" });
    g.addNode("5",    { label: "Вопрос 5" });
    g.addNode("end",    { label: "Конец опроса" });

    g.addEdge(null, "1",   "2", { label: "a" });
    g.addEdge(null, "1", "3",    { label: "b" });
    g.addEdge(null, "1",     "end",    { label: "c" });

    g.addEdge(null, "2", "4",    { label: "a" });
    g.addEdge(null, "2", "4",    { label: "b" });
    g.addEdge(null, "2", "4",    { label: "c" });

    g.addEdge(null, "3", "5",    { label: "a" });
    g.addEdge(null, "3", "5",    { label: "b" });
    g.addEdge(null, "3", "5",    { label: "c" });

    g.addEdge(null, "4", "end",    { label: "a" });
    g.addEdge(null, "4", "end",    { label: "b" });
    g.addEdge(null, "4", "end",    { label: "c" });

    g.addEdge(null, "5", "end",    { label: "a" });
    g.addEdge(null, "5", "end",    { label: "b" });
    g.addEdge(null, "5", "end",    { label: "c" });

    var layout = dagreD3.layout().rankDir('LR');

    var renderer = new dagreD3.Renderer().layout(layout);
//    renderer.edgeInterpolate('cardinal');
    renderer.run(g, d3.select("svg g"));
  }
};