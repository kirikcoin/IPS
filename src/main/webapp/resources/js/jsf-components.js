
/**
 Jsf components 1.101

 Copyright (c) Eyeline Communications Inc.
 **/
/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.0b2_r1012
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * Although not required, the author would appreciate an email letting him
 * know of any substantial use of jqPlot.  You can reach the author at:
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 * included jsDate library by Chris Leonello:
 *
 * Copyright (c) 2010-2011 Chris Leonello
 *
 * jsDate is currently available for use in all personal or commercial projects
 * under both the MIT and GPL version 2.0 licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * jsDate borrows many concepts and ideas from the Date Instance
 * Methods by Ken Snyder along with some parts of Ken's actual code.
 *
 * Ken's origianl Date Instance Methods and copyright notice:
 *
 * Ken Snyder (ken d snyder at gmail dot com)
 * 2008-09-10
 * version 2.0.2 (http://kendsnyder.com/sandbox/date/)
 * Creative Commons Attribution License 3.0 (http://creativecommons.org/licenses/by/3.0/)
 *
 * jqplotToImage function based on Larry Siden's export-jqplot-to-png.js.
 * Larry has generously given permission to adapt his code for inclusion
 * into jqPlot.
 *
 * Larry's original code can be found here:
 *
 * https://github.com/lsiden/export-jqplot-to-png
 *
 *
 */
(function(H){var r;H.fn.emptyForce=function(){for(var ab=0,ac;(ac=H(this)[ab])!=null;ab++){if(ac.nodeType===1){jQuery.cleanData(ac.getElementsByTagName("*"))}if(H.jqplot_use_excanvas){ac.outerHTML=""}else{while(ac.firstChild){ac.removeChild(ac.firstChild)}}ac=null}return H(this)};H.fn.removeChildForce=function(ab){while(ab.firstChild){this.removeChildForce(ab.firstChild);ab.removeChild(ab.firstChild)}};H.jqplot=function(ah,ae,ac){var ad,ab;if(ac==null){if(jQuery.isArray(ae)){ad=ae;ab=null}else{if(typeof(ae)==="object"){ad=null;ab=ae}}}else{ad=ae;ab=ac}var ag=new N();H("#"+ah).removeClass("jqplot-error");if(H.jqplot.config.catchErrors){try{ag.init(ah,ad,ab);ag.draw();ag.themeEngine.init.call(ag);return ag}catch(af){var ai=H.jqplot.config.errorMessage||af.message;H("#"+ah).append('<div class="jqplot-error-message">'+ai+"</div>");H("#"+ah).addClass("jqplot-error");document.getElementById(ah).style.background=H.jqplot.config.errorBackground;document.getElementById(ah).style.border=H.jqplot.config.errorBorder;document.getElementById(ah).style.fontFamily=H.jqplot.config.errorFontFamily;document.getElementById(ah).style.fontSize=H.jqplot.config.errorFontSize;document.getElementById(ah).style.fontStyle=H.jqplot.config.errorFontStyle;document.getElementById(ah).style.fontWeight=H.jqplot.config.errorFontWeight}}else{ag.init(ah,ad,ab);ag.draw();ag.themeEngine.init.call(ag);return ag}};H.jqplot.version="1.0.0b2_r1012";H.jqplot.CanvasManager=function(){if(typeof H.jqplot.CanvasManager.canvases=="undefined"){H.jqplot.CanvasManager.canvases=[];H.jqplot.CanvasManager.free=[]}var ab=[];this.getCanvas=function(){var ae;var ad=true;if(!H.jqplot.use_excanvas){for(var af=0,ac=H.jqplot.CanvasManager.canvases.length;af<ac;af++){if(H.jqplot.CanvasManager.free[af]===true){ad=false;ae=H.jqplot.CanvasManager.canvases[af];H.jqplot.CanvasManager.free[af]=false;ab.push(af);break}}}if(ad){ae=document.createElement("canvas");ab.push(H.jqplot.CanvasManager.canvases.length);H.jqplot.CanvasManager.canvases.push(ae);H.jqplot.CanvasManager.free.push(false)}return ae};this.initCanvas=function(ac){if(H.jqplot.use_excanvas){return window.G_vmlCanvasManager.initElement(ac)}return ac};this.freeAllCanvases=function(){for(var ad=0,ac=ab.length;ad<ac;ad++){this.freeCanvas(ab[ad])}ab=[]};this.freeCanvas=function(ac){if(H.jqplot.use_excanvas&&window.G_vmlCanvasManager.uninitElement!==r){window.G_vmlCanvasManager.uninitElement(H.jqplot.CanvasManager.canvases[ac]);H.jqplot.CanvasManager.canvases[ac]=null}else{var ad=H.jqplot.CanvasManager.canvases[ac];ad.getContext("2d").clearRect(0,0,ad.width,ad.height);H(ad).unbind().removeAttr("class").removeAttr("style");H(ad).css({left:"",top:"",position:""});ad.width=0;ad.height=0;H.jqplot.CanvasManager.free[ac]=true}}};H.jqplot.log=function(){if(window.console){window.console.log.apply(window.console,arguments)}};H.jqplot.config={addDomReference:false,enablePlugins:false,defaultHeight:300,defaultWidth:400,UTCAdjust:false,timezoneOffset:new Date(new Date().getTimezoneOffset()*60000),errorMessage:"",errorBackground:"",errorBorder:"",errorFontFamily:"",errorFontSize:"",errorFontStyle:"",errorFontWeight:"",catchErrors:false,defaultTickFormatString:"%.1f",defaultColors:["#4bb2c5","#EAA228","#c5b47f","#579575","#839557","#958c12","#953579","#4b5de4","#d8b83f","#ff5800","#0085cc","#c747a3","#cddf54","#FBD178","#26B4E3","#bd70c7"],defaultNegativeColors:["#498991","#C08840","#9F9274","#546D61","#646C4A","#6F6621","#6E3F5F","#4F64B0","#A89050","#C45923","#187399","#945381","#959E5C","#C7AF7B","#478396","#907294"],dashLength:4,gapLength:4,dotGapLength:2.5,srcLocation:"jqplot/src/",pluginLocation:"jqplot/src/plugins/"};H.jqplot.arrayMax=function(ab){return Math.max.apply(Math,ab)};H.jqplot.arrayMin=function(ab){return Math.min.apply(Math,ab)};H.jqplot.enablePlugins=H.jqplot.config.enablePlugins;H.jqplot.support_canvas=function(){if(typeof H.jqplot.support_canvas.result=="undefined"){H.jqplot.support_canvas.result=!!document.createElement("canvas").getContext}return H.jqplot.support_canvas.result};H.jqplot.support_canvas_text=function(){if(typeof H.jqplot.support_canvas_text.result=="undefined"){if(window.G_vmlCanvasManager!==r&&window.G_vmlCanvasManager._version>887){H.jqplot.support_canvas_text.result=true}else{H.jqplot.support_canvas_text.result=!!(document.createElement("canvas").getContext&&typeof document.createElement("canvas").getContext("2d").fillText=="function")}}return H.jqplot.support_canvas_text.result};H.jqplot.use_excanvas=(H.browser.msie&&!H.jqplot.support_canvas())?true:false;H.jqplot.preInitHooks=[];H.jqplot.postInitHooks=[];H.jqplot.preParseOptionsHooks=[];H.jqplot.postParseOptionsHooks=[];H.jqplot.preDrawHooks=[];H.jqplot.postDrawHooks=[];H.jqplot.preDrawSeriesHooks=[];H.jqplot.postDrawSeriesHooks=[];H.jqplot.preDrawLegendHooks=[];H.jqplot.addLegendRowHooks=[];H.jqplot.preSeriesInitHooks=[];H.jqplot.postSeriesInitHooks=[];H.jqplot.preParseSeriesOptionsHooks=[];H.jqplot.postParseSeriesOptionsHooks=[];H.jqplot.eventListenerHooks=[];H.jqplot.preDrawSeriesShadowHooks=[];H.jqplot.postDrawSeriesShadowHooks=[];H.jqplot.ElemContainer=function(){this._elem;this._plotWidth;this._plotHeight;this._plotDimensions={height:null,width:null}};H.jqplot.ElemContainer.prototype.createElement=function(ae,ag,ac,ad,ah){this._offsets=ag;var ab=ac||"jqplot";var af=document.createElement(ae);this._elem=H(af);this._elem.addClass(ab);this._elem.css(ad);this._elem.attr(ah);af=null;return this._elem};H.jqplot.ElemContainer.prototype.getWidth=function(){if(this._elem){return this._elem.outerWidth(true)}else{return null}};H.jqplot.ElemContainer.prototype.getHeight=function(){if(this._elem){return this._elem.outerHeight(true)}else{return null}};H.jqplot.ElemContainer.prototype.getPosition=function(){if(this._elem){return this._elem.position()}else{return{top:null,left:null,bottom:null,right:null}}};H.jqplot.ElemContainer.prototype.getTop=function(){return this.getPosition().top};H.jqplot.ElemContainer.prototype.getLeft=function(){return this.getPosition().left};H.jqplot.ElemContainer.prototype.getBottom=function(){return this._elem.css("bottom")};H.jqplot.ElemContainer.prototype.getRight=function(){return this._elem.css("right")};function s(ab){H.jqplot.ElemContainer.call(this);this.name=ab;this._series=[];this.show=false;this.tickRenderer=H.jqplot.AxisTickRenderer;this.tickOptions={};this.labelRenderer=H.jqplot.AxisLabelRenderer;this.labelOptions={};this.label=null;this.showLabel=true;this.min=null;this.max=null;this.autoscale=false;this.pad=1.2;this.padMax=null;this.padMin=null;this.ticks=[];this.numberTicks;this.tickInterval;this.renderer=H.jqplot.LinearAxisRenderer;this.rendererOptions={};this.showTicks=true;this.showTickMarks=true;this.showMinorTicks=true;this.drawMajorGridlines=true;this.drawMinorGridlines=false;this.drawMajorTickMarks=true;this.drawMinorTickMarks=true;this.useSeriesColor=false;this.borderWidth=null;this.borderColor=null;this._dataBounds={min:null,max:null};this._intervalStats=[];this._offsets={min:null,max:null};this._ticks=[];this._label=null;this.syncTicks=null;this.tickSpacing=75;this._min=null;this._max=null;this._tickInterval=null;this._numberTicks=null;this.__ticks=null;this._options={}}s.prototype=new H.jqplot.ElemContainer();s.prototype.constructor=s;s.prototype.init=function(){this.renderer=new this.renderer();this.tickOptions.axis=this.name;if(this.tickOptions.showMark==null){this.tickOptions.showMark=this.showTicks}if(this.tickOptions.showMark==null){this.tickOptions.showMark=this.showTickMarks}if(this.tickOptions.showLabel==null){this.tickOptions.showLabel=this.showTicks}if(this.label==null||this.label==""){this.showLabel=false}else{this.labelOptions.label=this.label}if(this.showLabel==false){this.labelOptions.show=false}if(this.pad==0){this.pad=1}if(this.padMax==0){this.padMax=1}if(this.padMin==0){this.padMin=1}if(this.padMax==null){this.padMax=(this.pad-1)/2+1}if(this.padMin==null){this.padMin=(this.pad-1)/2+1}this.pad=this.padMax+this.padMin-1;if(this.min!=null||this.max!=null){this.autoscale=false}if(this.syncTicks==null&&this.name.indexOf("y")>-1){this.syncTicks=true}else{if(this.syncTicks==null){this.syncTicks=false}}this.renderer.init.call(this,this.rendererOptions)};s.prototype.draw=function(ab,ac){if(this.__ticks){this.__ticks=null}return this.renderer.draw.call(this,ab,ac)};s.prototype.set=function(){this.renderer.set.call(this)};s.prototype.pack=function(ac,ab){if(this.show){this.renderer.pack.call(this,ac,ab)}if(this._min==null){this._min=this.min;this._max=this.max;this._tickInterval=this.tickInterval;this._numberTicks=this.numberTicks;this.__ticks=this._ticks}};s.prototype.reset=function(){this.renderer.reset.call(this)};s.prototype.resetScale=function(ab){H.extend(true,this,{min:null,max:null,numberTicks:null,tickInterval:null,_ticks:[],ticks:[]},ab);this.resetDataBounds()};s.prototype.resetDataBounds=function(){var ai=this._dataBounds;ai.min=null;ai.max=null;var ac,aj,ag;var ad=(this.show)?true:false;for(var af=0;af<this._series.length;af++){aj=this._series[af];if(aj.show){ag=aj._plotData;if(aj._type==="line"&&aj.renderer.bands.show&&this.name.charAt(0)!=="x"){ag=[[0,aj.renderer.bands._min],[1,aj.renderer.bands._max]]}var ab=1,ah=1;if(aj._type!=null&&aj._type=="ohlc"){ab=3;ah=2}for(var ae=0,ac=ag.length;ae<ac;ae++){if(this.name=="xaxis"||this.name=="x2axis"){if((ag[ae][0]!=null&&ag[ae][0]<ai.min)||ai.min==null){ai.min=ag[ae][0]}if((ag[ae][0]!=null&&ag[ae][0]>ai.max)||ai.max==null){ai.max=ag[ae][0]}}else{if((ag[ae][ab]!=null&&ag[ae][ab]<ai.min)||ai.min==null){ai.min=ag[ae][ab]}if((ag[ae][ah]!=null&&ag[ae][ah]>ai.max)||ai.max==null){ai.max=ag[ae][ah]}}}if(ad&&aj.renderer.constructor!==H.jqplot.BarRenderer){ad=false}else{if(ad&&this._options.hasOwnProperty("forceTickAt0")&&this._options.forceTickAt0==false){ad=false}else{if(ad&&aj.renderer.constructor===H.jqplot.BarRenderer){if(aj.barDirection=="vertical"&&this.name!="xaxis"&&this.name!="x2axis"){if(this._options.pad!=null||this._options.padMin!=null){ad=false}}else{if(aj.barDirection=="horizontal"&&(this.name=="xaxis"||this.name=="x2axis")){if(this._options.pad!=null||this._options.padMin!=null){ad=false}}}}}}}}if(ad&&this.renderer.constructor===H.jqplot.LinearAxisRenderer&&ai.min>=0){this.padMin=1;this.forceTickAt0=true}};function n(ab){H.jqplot.ElemContainer.call(this);this.show=false;this.location="ne";this.labels=[];this.showLabels=true;this.showSwatches=true;this.placement="insideGrid";this.xoffset=0;this.yoffset=0;this.border;this.background;this.textColor;this.fontFamily;this.fontSize;this.rowSpacing="0.5em";this.renderer=H.jqplot.TableLegendRenderer;this.rendererOptions={};this.preDraw=false;this.marginTop=null;this.marginRight=null;this.marginBottom=null;this.marginLeft=null;this.escapeHtml=false;this._series=[];H.extend(true,this,ab)}n.prototype=new H.jqplot.ElemContainer();n.prototype.constructor=n;n.prototype.setOptions=function(ab){H.extend(true,this,ab);if(this.placement=="inside"){this.placement="insideGrid"}if(this.xoffset>0){if(this.placement=="insideGrid"){switch(this.location){case"nw":case"w":case"sw":if(this.marginLeft==null){this.marginLeft=this.xoffset+"px"}this.marginRight="0px";break;case"ne":case"e":case"se":default:if(this.marginRight==null){this.marginRight=this.xoffset+"px"}this.marginLeft="0px";break}}else{if(this.placement=="outside"){switch(this.location){case"nw":case"w":case"sw":if(this.marginRight==null){this.marginRight=this.xoffset+"px"}this.marginLeft="0px";break;case"ne":case"e":case"se":default:if(this.marginLeft==null){this.marginLeft=this.xoffset+"px"}this.marginRight="0px";break}}}this.xoffset=0}if(this.yoffset>0){if(this.placement=="outside"){switch(this.location){case"sw":case"s":case"se":if(this.marginTop==null){this.marginTop=this.yoffset+"px"}this.marginBottom="0px";break;case"ne":case"n":case"nw":default:if(this.marginBottom==null){this.marginBottom=this.yoffset+"px"}this.marginTop="0px";break}}else{if(this.placement=="insideGrid"){switch(this.location){case"sw":case"s":case"se":if(this.marginBottom==null){this.marginBottom=this.yoffset+"px"}this.marginTop="0px";break;case"ne":case"n":case"nw":default:if(this.marginTop==null){this.marginTop=this.yoffset+"px"}this.marginBottom="0px";break}}}this.yoffset=0}};n.prototype.init=function(){this.renderer=new this.renderer();this.renderer.init.call(this,this.rendererOptions)};n.prototype.draw=function(ac){for(var ab=0;ab<H.jqplot.preDrawLegendHooks.length;ab++){H.jqplot.preDrawLegendHooks[ab].call(this,ac)}return this.renderer.draw.call(this,ac)};n.prototype.pack=function(ab){this.renderer.pack.call(this,ab)};function u(ab){H.jqplot.ElemContainer.call(this);this.text=ab;this.show=true;this.fontFamily;this.fontSize;this.textAlign;this.textColor;this.renderer=H.jqplot.DivTitleRenderer;this.rendererOptions={};this.escapeHtml=false}u.prototype=new H.jqplot.ElemContainer();u.prototype.constructor=u;u.prototype.init=function(){this.renderer=new this.renderer();this.renderer.init.call(this,this.rendererOptions)};u.prototype.draw=function(ab){return this.renderer.draw.call(this,ab)};u.prototype.pack=function(){this.renderer.pack.call(this)};function O(){H.jqplot.ElemContainer.call(this);this.show=true;this.xaxis="xaxis";this._xaxis;this.yaxis="yaxis";this._yaxis;this.gridBorderWidth=2;this.renderer=H.jqplot.LineRenderer;this.rendererOptions={};this.data=[];this.gridData=[];this.label="";this.showLabel=true;this.color;this.negativeColor;this.lineWidth=2.5;this.lineJoin="round";this.lineCap="round";this.linePattern="solid";this.shadow=true;this.shadowAngle=45;this.shadowOffset=1.25;this.shadowDepth=3;this.shadowAlpha="0.1";this.breakOnNull=false;this.markerRenderer=H.jqplot.MarkerRenderer;this.markerOptions={};this.showLine=true;this.showMarker=true;this.index;this.fill=false;this.fillColor;this.fillAlpha;this.fillAndStroke=false;this.disableStack=false;this._stack=false;this.neighborThreshold=4;this.fillToZero=false;this.fillToValue=0;this.fillAxis="y";this.useNegativeColors=true;this._stackData=[];this._plotData=[];this._plotValues={x:[],y:[]};this._intervals={x:{},y:{}};this._prevPlotData=[];this._prevGridData=[];this._stackAxis="y";this._primaryAxis="_xaxis";this.canvas=new H.jqplot.GenericCanvas();this.shadowCanvas=new H.jqplot.GenericCanvas();this.plugins={};this._sumy=0;this._sumx=0;this._type=""}O.prototype=new H.jqplot.ElemContainer();O.prototype.constructor=O;O.prototype.init=function(ad,ah,af){this.index=ad;this.gridBorderWidth=ah;var ag=this.data;var ac=[],ae;for(ae=0;ae<ag.length;ae++){if(!this.breakOnNull){if(ag[ae]==null||ag[ae][0]==null||ag[ae][1]==null){continue}else{ac.push(ag[ae])}}else{ac.push(ag[ae])}}this.data=ac;if(!this.color&&this.show){this.color=af.colorGenerator.get(this.index)}if(!this.negativeColor&&this.show){this.negativeColor=af.negativeColorGenerator.get(this.index)}if(!this.fillColor){this.fillColor=this.color}if(this.fillAlpha){var ab=H.jqplot.normalize2rgb(this.fillColor);var ab=H.jqplot.getColorComponents(ab);this.fillColor="rgba("+ab[0]+","+ab[1]+","+ab[2]+","+this.fillAlpha+")"}this.renderer=new this.renderer();this.renderer.init.call(this,this.rendererOptions,af);this.markerRenderer=new this.markerRenderer();if(!this.markerOptions.color){this.markerOptions.color=this.color}if(this.markerOptions.show==null){this.markerOptions.show=this.showMarker}this.showMarker=this.markerOptions.show;this.markerRenderer.init(this.markerOptions)};O.prototype.draw=function(ah,ae,ag){var ac=(ae==r)?{}:ae;ah=(ah==r)?this.canvas._ctx:ah;var ab,af,ad;for(ab=0;ab<H.jqplot.preDrawSeriesHooks.length;ab++){H.jqplot.preDrawSeriesHooks[ab].call(this,ah,ac)}if(this.show){this.renderer.setGridData.call(this,ag);if(!ac.preventJqPlotSeriesDrawTrigger){H(ah.canvas).trigger("jqplotSeriesDraw",[this.data,this.gridData])}af=[];if(ac.data){af=ac.data}else{if(!this._stack){af=this.data}else{af=this._plotData}}ad=ac.gridData||this.renderer.makeGridData.call(this,af,ag);if(this._type==="line"&&this.renderer.smooth&&this.renderer._smoothedData.length){ad=this.renderer._smoothedData}this.renderer.draw.call(this,ah,ad,ac,ag)}for(ab=0;ab<H.jqplot.postDrawSeriesHooks.length;ab++){H.jqplot.postDrawSeriesHooks[ab].call(this,ah,ac,ag)}ah=ae=ag=ab=af=ad=null};O.prototype.drawShadow=function(ah,ae,ag){var ac=(ae==r)?{}:ae;ah=(ah==r)?this.shadowCanvas._ctx:ah;var ab,af,ad;for(ab=0;ab<H.jqplot.preDrawSeriesShadowHooks.length;ab++){H.jqplot.preDrawSeriesShadowHooks[ab].call(this,ah,ac)}if(this.shadow){this.renderer.setGridData.call(this,ag);af=[];if(ac.data){af=ac.data}else{if(!this._stack){af=this.data}else{af=this._plotData}}ad=ac.gridData||this.renderer.makeGridData.call(this,af,ag);this.renderer.drawShadow.call(this,ah,ad,ac)}for(ab=0;ab<H.jqplot.postDrawSeriesShadowHooks.length;ab++){H.jqplot.postDrawSeriesShadowHooks[ab].call(this,ah,ac)}ah=ae=ag=ab=af=ad=null};O.prototype.toggleDisplay=function(ac){var ab,ad;if(ac.data.series){ab=ac.data.series}else{ab=this}if(ac.data.speed){ad=ac.data.speed}if(ad){if(ab.canvas._elem.is(":hidden")){ab.canvas._elem.removeClass("jqplot-series-hidden");if(ab.shadowCanvas._elem){ab.shadowCanvas._elem.fadeIn(ad)}ab.canvas._elem.fadeIn(ad);ab.canvas._elem.nextAll(".jqplot-point-label.jqplot-series-"+ab.index).fadeIn(ad)}else{ab.canvas._elem.addClass("jqplot-series-hidden");if(ab.shadowCanvas._elem){ab.shadowCanvas._elem.fadeOut(ad)}ab.canvas._elem.fadeOut(ad);ab.canvas._elem.nextAll(".jqplot-point-label.jqplot-series-"+ab.index).fadeOut(ad)}}else{if(ab.canvas._elem.is(":hidden")){ab.canvas._elem.removeClass("jqplot-series-hidden");if(ab.shadowCanvas._elem){ab.shadowCanvas._elem.show()}ab.canvas._elem.show();ab.canvas._elem.nextAll(".jqplot-point-label.jqplot-series-"+ab.index).show()}else{ab.canvas._elem.addClass("jqplot-series-hidden");if(ab.shadowCanvas._elem){ab.shadowCanvas._elem.hide()}ab.canvas._elem.hide();ab.canvas._elem.nextAll(".jqplot-point-label.jqplot-series-"+ab.index).hide()}}};function I(){H.jqplot.ElemContainer.call(this);this.drawGridlines=true;this.gridLineColor="#cccccc";this.gridLineWidth=1;this.background="#fffdf6";this.borderColor="#999999";this.borderWidth=2;this.drawBorder=true;this.shadow=true;this.shadowAngle=45;this.shadowOffset=1.5;this.shadowWidth=3;this.shadowDepth=3;this.shadowColor=null;this.shadowAlpha="0.07";this._left;this._top;this._right;this._bottom;this._width;this._height;this._axes=[];this.renderer=H.jqplot.CanvasGridRenderer;this.rendererOptions={};this._offsets={top:null,bottom:null,left:null,right:null}}I.prototype=new H.jqplot.ElemContainer();I.prototype.constructor=I;I.prototype.init=function(){this.renderer=new this.renderer();this.renderer.init.call(this,this.rendererOptions)};I.prototype.createElement=function(ab,ac){this._offsets=ab;return this.renderer.createElement.call(this,ac)};I.prototype.draw=function(){this.renderer.draw.call(this)};H.jqplot.GenericCanvas=function(){H.jqplot.ElemContainer.call(this);this._ctx};H.jqplot.GenericCanvas.prototype=new H.jqplot.ElemContainer();H.jqplot.GenericCanvas.prototype.constructor=H.jqplot.GenericCanvas;H.jqplot.GenericCanvas.prototype.createElement=function(af,ad,ac,ag){this._offsets=af;var ab="jqplot";if(ad!=r){ab=ad}var ae;ae=ag.canvasManager.getCanvas();if(ac!=null){this._plotDimensions=ac}ae.width=this._plotDimensions.width-this._offsets.left-this._offsets.right;ae.height=this._plotDimensions.height-this._offsets.top-this._offsets.bottom;this._elem=H(ae);this._elem.css({position:"absolute",left:this._offsets.left,top:this._offsets.top});this._elem.addClass(ab);ae=ag.canvasManager.initCanvas(ae);ae=null;return this._elem};H.jqplot.GenericCanvas.prototype.setContext=function(){this._ctx=this._elem.get(0).getContext("2d");return this._ctx};H.jqplot.GenericCanvas.prototype.resetCanvas=function(){if(this._elem){if(H.jqplot.use_excanvas&&window.G_vmlCanvasManager.uninitElement!==r){window.G_vmlCanvasManager.uninitElement(this._elem.get(0))}this._elem.emptyForce()}this._ctx=null};H.jqplot.HooksManager=function(){this.hooks=[];this.args=[]};H.jqplot.HooksManager.prototype.addOnce=function(ae,ac){ac=ac||[];var af=false;for(var ad=0,ab=this.hooks.length;ad<ab;ad++){if(this.hooks[ad][0]==ae){af=true}}if(!af){this.hooks.push(ae);this.args.push(ac)}};H.jqplot.HooksManager.prototype.add=function(ac,ab){ab=ab||[];this.hooks.push(ac);this.args.push(ab)};H.jqplot.EventListenerManager=function(){this.hooks=[]};H.jqplot.EventListenerManager.prototype.addOnce=function(af,ae){var ag=false,ad,ac;for(var ac=0,ab=this.hooks.length;ac<ab;ac++){ad=this.hooks[ac];if(ad[0]==af&&ad[1]==ae){ag=true}}if(!ag){this.hooks.push([af,ae])}};H.jqplot.EventListenerManager.prototype.add=function(ac,ab){this.hooks.push([ac,ab])};var Q=["yMidAxis","xaxis","yaxis","x2axis","y2axis","y3axis","y4axis","y5axis","y6axis","y7axis","y8axis","y9axis"];function N(){this.animate=false;this.animateReplot=false;this.axes={xaxis:new s("xaxis"),yaxis:new s("yaxis"),x2axis:new s("x2axis"),y2axis:new s("y2axis"),y3axis:new s("y3axis"),y4axis:new s("y4axis"),y5axis:new s("y5axis"),y6axis:new s("y6axis"),y7axis:new s("y7axis"),y8axis:new s("y8axis"),y9axis:new s("y9axis"),yMidAxis:new s("yMidAxis")};this.baseCanvas=new H.jqplot.GenericCanvas();this.captureRightClick=false;this.data=[];this.dataRenderer;this.dataRendererOptions;this.defaults={axesDefaults:{},axes:{xaxis:{},yaxis:{},x2axis:{},y2axis:{},y3axis:{},y4axis:{},y5axis:{},y6axis:{},y7axis:{},y8axis:{},y9axis:{},yMidAxis:{}},seriesDefaults:{},series:[]};this.defaultAxisStart=1;this.drawIfHidden=false;this.eventCanvas=new H.jqplot.GenericCanvas();this.fillBetween={series1:null,series2:null,color:null,baseSeries:0,fill:true};this.fontFamily;this.fontSize;this.grid=new I();this.legend=new n();this.negativeSeriesColors=H.jqplot.config.defaultNegativeColors;this.noDataIndicator={show:false,indicator:"Loading Data...",axes:{xaxis:{min:0,max:10,tickInterval:2,show:true},yaxis:{min:0,max:12,tickInterval:3,show:true}}};this.options={};this.previousSeriesStack=[];this.plugins={};this.series=[];this.seriesStack=[];this.seriesColors=H.jqplot.config.defaultColors;this.sortData=true;this.stackSeries=false;this.syncXTicks=true;this.syncYTicks=true;this.target=null;this.targetId=null;this.textColor;this.title=new u();this._drawCount=0;this._sumy=0;this._sumx=0;this._stackData=[];this._plotData=[];this._width=null;this._height=null;this._plotDimensions={height:null,width:null};this._gridPadding={top:null,right:null,bottom:null,left:null};this._defaultGridPadding={top:10,right:10,bottom:23,left:10};this._addDomReference=H.jqplot.config.addDomReference;this.preInitHooks=new H.jqplot.HooksManager();this.postInitHooks=new H.jqplot.HooksManager();this.preParseOptionsHooks=new H.jqplot.HooksManager();this.postParseOptionsHooks=new H.jqplot.HooksManager();this.preDrawHooks=new H.jqplot.HooksManager();this.postDrawHooks=new H.jqplot.HooksManager();this.preDrawSeriesHooks=new H.jqplot.HooksManager();this.postDrawSeriesHooks=new H.jqplot.HooksManager();this.preDrawLegendHooks=new H.jqplot.HooksManager();this.addLegendRowHooks=new H.jqplot.HooksManager();this.preSeriesInitHooks=new H.jqplot.HooksManager();this.postSeriesInitHooks=new H.jqplot.HooksManager();this.preParseSeriesOptionsHooks=new H.jqplot.HooksManager();this.postParseSeriesOptionsHooks=new H.jqplot.HooksManager();this.eventListenerHooks=new H.jqplot.EventListenerManager();this.preDrawSeriesShadowHooks=new H.jqplot.HooksManager();this.postDrawSeriesShadowHooks=new H.jqplot.HooksManager();this.colorGenerator=new H.jqplot.ColorGenerator();this.negativeColorGenerator=new H.jqplot.ColorGenerator();this.canvasManager=new H.jqplot.CanvasManager();this.themeEngine=new H.jqplot.ThemeEngine();var ad=0;this.init=function(am,aj,ao){ao=ao||{};for(var ak=0;ak<H.jqplot.preInitHooks.length;ak++){H.jqplot.preInitHooks[ak].call(this,am,aj,ao)}for(var ak=0;ak<this.preInitHooks.hooks.length;ak++){this.preInitHooks.hooks[ak].call(this,am,aj,ao)}this.targetId="#"+am;this.target=H("#"+am);if(this._addDomReference){this.target.data("jqplot_plot",this)}this.target.removeClass("jqplot-error");if(!this.target.get(0)){throw"No plot target specified"}if(this.target.css("position")=="static"){this.target.css("position","relative")}if(!this.target.hasClass("jqplot-target")){this.target.addClass("jqplot-target")}if(!this.target.height()){var al;if(ao&&ao.height){al=parseInt(ao.height,10)}else{if(this.target.attr("data-height")){al=parseInt(this.target.attr("data-height"),10)}else{al=parseInt(H.jqplot.config.defaultHeight,10)}}this._height=al;this.target.css("height",al+"px")}else{this._height=al=this.target.height()}if(!this.target.width()){var an;if(ao&&ao.width){an=parseInt(ao.width,10)}else{if(this.target.attr("data-width")){an=parseInt(this.target.attr("data-width"),10)}else{an=parseInt(H.jqplot.config.defaultWidth,10)}}this._width=an;this.target.css("width",an+"px")}else{this._width=an=this.target.width()}this._plotDimensions.height=this._height;this._plotDimensions.width=this._width;this.grid._plotDimensions=this._plotDimensions;this.title._plotDimensions=this._plotDimensions;this.baseCanvas._plotDimensions=this._plotDimensions;this.eventCanvas._plotDimensions=this._plotDimensions;this.legend._plotDimensions=this._plotDimensions;if(this._height<=0||this._width<=0||!this._height||!this._width){throw"Canvas dimension not set"}if(ao.dataRenderer&&jQuery.isFunction(ao.dataRenderer)){if(ao.dataRendererOptions){this.dataRendererOptions=ao.dataRendererOptions}this.dataRenderer=ao.dataRenderer;aj=this.dataRenderer(aj,this,this.dataRendererOptions)}if(ao.noDataIndicator&&jQuery.isPlainObject(ao.noDataIndicator)){H.extend(true,this.noDataIndicator,ao.noDataIndicator)}if(aj==null||jQuery.isArray(aj)==false||aj.length==0||jQuery.isArray(aj[0])==false||aj[0].length==0){if(this.noDataIndicator.show==false){throw {name:"DataError",message:"No data to plot."}}else{for(var af in this.noDataIndicator.axes){for(var ah in this.noDataIndicator.axes[af]){this.axes[af][ah]=this.noDataIndicator.axes[af][ah]}}this.postDrawHooks.add(function(){var av=this.eventCanvas.getHeight();var ar=this.eventCanvas.getWidth();var aq=H('<div class="jqplot-noData-container" style="position:absolute;"></div>');this.target.append(aq);aq.height(av);aq.width(ar);aq.css("top",this.eventCanvas._offsets.top);aq.css("left",this.eventCanvas._offsets.left);var au=H('<div class="jqplot-noData-contents" style="text-align:center; position:relative; margin-left:auto; margin-right:auto;"></div>');aq.append(au);au.html(this.noDataIndicator.indicator);var at=au.height();var ap=au.width();au.height(at);au.width(ap);au.css("top",(av-at)/2+"px")})}}this.data=aj;this.parseOptions(ao);if(this.textColor){this.target.css("color",this.textColor)}if(this.fontFamily){this.target.css("font-family",this.fontFamily)}if(this.fontSize){this.target.css("font-size",this.fontSize)}this.title.init();this.legend.init();this._sumy=0;this._sumx=0;for(var ak=0;ak<this.series.length;ak++){this.seriesStack.push(ak);this.previousSeriesStack.push(ak);this.series[ak].shadowCanvas._plotDimensions=this._plotDimensions;this.series[ak].canvas._plotDimensions=this._plotDimensions;for(var ai=0;ai<H.jqplot.preSeriesInitHooks.length;ai++){H.jqplot.preSeriesInitHooks[ai].call(this.series[ak],am,aj,this.options.seriesDefaults,this.options.series[ak],this)}for(var ai=0;ai<this.preSeriesInitHooks.hooks.length;ai++){this.preSeriesInitHooks.hooks[ai].call(this.series[ak],am,aj,this.options.seriesDefaults,this.options.series[ak],this)}this.populatePlotData(this.series[ak],ak);this.series[ak]._plotDimensions=this._plotDimensions;this.series[ak].init(ak,this.grid.borderWidth,this);for(var ai=0;ai<H.jqplot.postSeriesInitHooks.length;ai++){H.jqplot.postSeriesInitHooks[ai].call(this.series[ak],am,aj,this.options.seriesDefaults,this.options.series[ak],this)}for(var ai=0;ai<this.postSeriesInitHooks.hooks.length;ai++){this.postSeriesInitHooks.hooks[ai].call(this.series[ak],am,aj,this.options.seriesDefaults,this.options.series[ak],this)}this._sumy+=this.series[ak]._sumy;this._sumx+=this.series[ak]._sumx}var ag;for(var ak=0;ak<12;ak++){ag=Q[ak];this.axes[ag]._plotDimensions=this._plotDimensions;this.axes[ag].init();if(this.axes[ag].borderColor==null){if(ag.charAt(0)!=="x"&&this.axes[ag].useSeriesColor===true&&this.axes[ag].show){this.axes[ag].borderColor=this.axes[ag]._series[0].color}else{this.axes[ag].borderColor=this.grid.borderColor}}}if(this.sortData){ab(this.series)}this.grid.init();this.grid._axes=this.axes;this.legend._series=this.series;for(var ak=0;ak<H.jqplot.postInitHooks.length;ak++){H.jqplot.postInitHooks[ak].call(this,am,aj,ao)}for(var ak=0;ak<this.postInitHooks.hooks.length;ak++){this.postInitHooks.hooks[ak].call(this,am,aj,ao)}};this.resetAxesScale=function(ak,ag){var ai=ag||{};var aj=ak||this.axes;if(aj===true){aj=this.axes}if(jQuery.isArray(aj)){for(var ah=0;ah<aj.length;ah++){this.axes[aj[ah]].resetScale(ai[aj[ah]])}}else{if(typeof(aj)==="object"){for(var af in aj){this.axes[af].resetScale(ai[af])}}}};this.reInitialize=function(){this._height=this.target.height();this._width=this.target.width();if(this._height<=0||this._width<=0||!this._height||!this._width){throw"Target dimension not set"}this._plotDimensions.height=this._height;this._plotDimensions.width=this._width;this.grid._plotDimensions=this._plotDimensions;this.title._plotDimensions=this._plotDimensions;this.baseCanvas._plotDimensions=this._plotDimensions;this.eventCanvas._plotDimensions=this._plotDimensions;this.legend._plotDimensions=this._plotDimensions;for(var ak in this.axes){this.axes[ak]._plotWidth=this._width;this.axes[ak]._plotHeight=this._height}this.title._plotWidth=this._width;if(this.textColor){this.target.css("color",this.textColor)}if(this.fontFamily){this.target.css("font-family",this.fontFamily)}if(this.fontSize){this.target.css("font-size",this.fontSize)}this._sumy=0;this._sumx=0;for(var ai=0;ai<this.series.length;ai++){this.populatePlotData(this.series[ai],ai);if(this.series[ai]._type==="line"&&this.series[ai].renderer.bands.show){this.series[ai].renderer.initBands.call(this.series[ai],this.series[ai].renderer.options,this)}this.series[ai]._plotDimensions=this._plotDimensions;this.series[ai].canvas._plotDimensions=this._plotDimensions;this._sumy+=this.series[ai]._sumy;this._sumx+=this.series[ai]._sumx}var ag;for(var af=0;af<12;af++){ag=Q[af];var ah=this.axes[ag]._ticks;for(var ai=0;ai<ah.length;ai++){var aj=ah[ai]._elem;if(aj){if(H.jqplot.use_excanvas&&window.G_vmlCanvasManager.uninitElement!==r){window.G_vmlCanvasManager.uninitElement(aj.get(0))}aj.emptyForce();aj=null;ah._elem=null}}ah=null;this.axes[ag]._plotDimensions=this._plotDimensions;this.axes[ag]._ticks=[]}if(this.sortData){ab(this.series)}this.grid._axes=this.axes;this.legend._series=this.series};function ab(aj){var an,ao,ap,af,am;for(var ak=0;ak<aj.length;ak++){var ag;var al=[aj[ak].data,aj[ak]._stackData,aj[ak]._plotData,aj[ak]._prevPlotData];for(var ah=0;ah<4;ah++){ag=true;an=al[ah];if(aj[ak]._stackAxis=="x"){for(var ai=0;ai<an.length;ai++){if(typeof(an[ai][1])!="number"){ag=false;break}}if(ag){an.sort(function(ar,aq){return ar[1]-aq[1]})}}else{for(var ai=0;ai<an.length;ai++){if(typeof(an[ai][0])!="number"){ag=false;break}}if(ag){an.sort(function(ar,aq){return ar[0]-aq[0]})}}}}}this.populatePlotData=function(aj,ak){this._plotData=[];this._stackData=[];aj._stackData=[];aj._plotData=[];var an={x:[],y:[]};if(this.stackSeries&&!aj.disableStack){aj._stack=true;var al=aj._stackAxis=="x"?0:1;var am=al?0:1;var ao=H.extend(true,[],aj.data);var ap=H.extend(true,[],aj.data);for(var ah=0;ah<ak;ah++){var af=this.series[ah].data;for(var ag=0;ag<af.length;ag++){ao[ag][0]+=af[ag][0];ao[ag][1]+=af[ag][1];ap[ag][al]+=af[ag][al]}}for(var ai=0;ai<ap.length;ai++){an.x.push(ap[ai][0]);an.y.push(ap[ai][1])}this._plotData.push(ap);this._stackData.push(ao);aj._stackData=ao;aj._plotData=ap;aj._plotValues=an}else{for(var ai=0;ai<aj.data.length;ai++){an.x.push(aj.data[ai][0]);an.y.push(aj.data[ai][1])}this._stackData.push(aj.data);this.series[ak]._stackData=aj.data;this._plotData.push(aj.data);aj._plotData=aj.data;aj._plotValues=an}if(ak>0){aj._prevPlotData=this.series[ak-1]._plotData}aj._sumy=0;aj._sumx=0;for(ai=aj.data.length-1;ai>-1;ai--){aj._sumy+=aj.data[ai][1];aj._sumx+=aj.data[ai][0]}};this.getNextSeriesColor=(function(ag){var af=0;var ah=ag.seriesColors;return function(){if(af<ah.length){return ah[af++]}else{af=0;return ah[af++]}}})(this);this.parseOptions=function(aq){for(var al=0;al<this.preParseOptionsHooks.hooks.length;al++){this.preParseOptionsHooks.hooks[al].call(this,aq)}for(var al=0;al<H.jqplot.preParseOptionsHooks.length;al++){H.jqplot.preParseOptionsHooks[al].call(this,aq)}this.options=H.extend(true,{},this.defaults,aq);var af=this.options;this.animate=af.animate;this.animateReplot=af.animateReplot;this.stackSeries=af.stackSeries;if(H.isPlainObject(af.fillBetween)){var ap=["series1","series2","color","baseSeries","fill"],am;for(var al=0,aj=ap.length;al<aj;al++){am=ap[al];if(af.fillBetween[am]!=null){this.fillBetween[am]=af.fillBetween[am]}}}if(af.seriesColors){this.seriesColors=af.seriesColors}if(af.negativeSeriesColors){this.negativeSeriesColors=af.negativeSeriesColors}if(af.captureRightClick){this.captureRightClick=af.captureRightClick}this.defaultAxisStart=(aq&&aq.defaultAxisStart!=null)?aq.defaultAxisStart:this.defaultAxisStart;this.colorGenerator.setColors(this.seriesColors);this.negativeColorGenerator.setColors(this.negativeSeriesColors);H.extend(true,this._gridPadding,af.gridPadding);this.sortData=(af.sortData!=null)?af.sortData:this.sortData;for(var al=0;al<12;al++){var ag=Q[al];var ai=this.axes[ag];ai._options=H.extend(true,{},af.axesDefaults,af.axes[ag]);H.extend(true,ai,af.axesDefaults,af.axes[ag]);ai._plotWidth=this._width;ai._plotHeight=this._height}var ao=function(av,at,aw){var ar=[];var au;at=at||"vertical";if(!jQuery.isArray(av[0])){for(au=0;au<av.length;au++){if(at=="vertical"){ar.push([aw+au,av[au]])}else{ar.push([av[au],aw+au])}}}else{H.extend(true,ar,av)}return ar};var an=0;for(var al=0;al<this.data.length;al++){var ap=new O();for(var ak=0;ak<H.jqplot.preParseSeriesOptionsHooks.length;ak++){H.jqplot.preParseSeriesOptionsHooks[ak].call(ap,this.options.seriesDefaults,this.options.series[al])}for(var ak=0;ak<this.preParseSeriesOptionsHooks.hooks.length;ak++){this.preParseSeriesOptionsHooks.hooks[ak].call(ap,this.options.seriesDefaults,this.options.series[al])}H.extend(true,ap,{seriesColors:this.seriesColors,negativeSeriesColors:this.negativeSeriesColors},this.options.seriesDefaults,this.options.series[al],{rendererOptions:{animation:{show:this.animate}}});var ah="vertical";if(ap.renderer===H.jqplot.BarRenderer&&ap.rendererOptions&&ap.rendererOptions.barDirection=="horizontal"&&ap.transposeData===true){ah="horizontal"}ap.data=ao(this.data[al],ah,this.defaultAxisStart);switch(ap.xaxis){case"xaxis":ap._xaxis=this.axes.xaxis;break;case"x2axis":ap._xaxis=this.axes.x2axis;break;default:break}ap._yaxis=this.axes[ap.yaxis];ap._xaxis._series.push(ap);ap._yaxis._series.push(ap);if(ap.show){ap._xaxis.show=true;ap._yaxis.show=true}if(!ap.label){ap.label="Series "+(al+1).toString()}this.series.push(ap);for(var ak=0;ak<H.jqplot.postParseSeriesOptionsHooks.length;ak++){H.jqplot.postParseSeriesOptionsHooks[ak].call(this.series[al],this.options.seriesDefaults,this.options.series[al])}for(var ak=0;ak<this.postParseSeriesOptionsHooks.hooks.length;ak++){this.postParseSeriesOptionsHooks.hooks[ak].call(this.series[al],this.options.seriesDefaults,this.options.series[al])}}H.extend(true,this.grid,this.options.grid);for(var al=0;al<12;al++){var ag=Q[al];var ai=this.axes[ag];if(ai.borderWidth==null){ai.borderWidth=this.grid.borderWidth}}if(typeof this.options.title=="string"){this.title.text=this.options.title}else{if(typeof this.options.title=="object"){H.extend(true,this.title,this.options.title)}}this.title._plotWidth=this._width;this.legend.setOptions(this.options.legend);for(var al=0;al<H.jqplot.postParseOptionsHooks.length;al++){H.jqplot.postParseOptionsHooks[al].call(this,aq)}for(var al=0;al<this.postParseOptionsHooks.hooks.length;al++){this.postParseOptionsHooks.hooks[al].call(this,aq)}};this.destroy=function(){this.canvasManager.freeAllCanvases();if(this.eventCanvas&&this.eventCanvas._elem){this.eventCanvas._elem.unbind()}this.target.empty();this.target[0].innerHTML=""};this.replot=function(ag){var ah=ag||{};var af=(ah.clear===false)?false:true;var ai=ah.resetAxes||false;this.target.trigger("jqplotPreReplot");if(af){this.destroy()}this.reInitialize();if(ai){this.resetAxesScale(ai,ah.axes)}this.draw();this.target.trigger("jqplotPostReplot")};this.redraw=function(af){af=(af!=null)?af:true;this.target.trigger("jqplotPreRedraw");if(af){this.canvasManager.freeAllCanvases();this.eventCanvas._elem.unbind();this.target.empty()}for(var ah in this.axes){this.axes[ah]._ticks=[]}for(var ag=0;ag<this.series.length;ag++){this.populatePlotData(this.series[ag],ag)}this._sumy=0;this._sumx=0;for(ag=0;ag<this.series.length;ag++){this._sumy+=this.series[ag]._sumy;this._sumx+=this.series[ag]._sumx}this.draw();this.target.trigger("jqplotPostRedraw")};this.draw=function(){if(this.drawIfHidden||this.target.is(":visible")){this.target.trigger("jqplotPreDraw");var aB,az,ay,ai;for(aB=0,ay=H.jqplot.preDrawHooks.length;aB<ay;aB++){H.jqplot.preDrawHooks[aB].call(this)}for(aB=0,ay=this.preDrawHooks.length;aB<ay;aB++){this.preDrawHooks.hooks[aB].apply(this,this.preDrawSeriesHooks.args[aB])}this.target.append(this.baseCanvas.createElement({left:0,right:0,top:0,bottom:0},"jqplot-base-canvas",null,this));this.baseCanvas.setContext();this.target.append(this.title.draw());this.title.pack({top:0,left:0});var aF=this.legend.draw();var af={top:0,left:0,bottom:0,right:0};if(this.legend.placement=="outsideGrid"){this.target.append(aF);switch(this.legend.location){case"n":af.top+=this.legend.getHeight();break;case"s":af.bottom+=this.legend.getHeight();break;case"ne":case"e":case"se":af.right+=this.legend.getWidth();break;case"nw":case"w":case"sw":af.left+=this.legend.getWidth();break;default:af.right+=this.legend.getWidth();break}aF=aF.detach()}var al=this.axes;var aG;for(aB=0;aB<12;aB++){aG=Q[aB];this.target.append(al[aG].draw(this.baseCanvas._ctx,this));al[aG].set()}if(al.yaxis.show){af.left+=al.yaxis.getWidth()}var aA=["y2axis","y3axis","y4axis","y5axis","y6axis","y7axis","y8axis","y9axis"];var ar=[0,0,0,0,0,0,0,0];var av=0;var au;for(au=0;au<8;au++){if(al[aA[au]].show){av+=al[aA[au]].getWidth();ar[au]=av}}af.right+=av;if(al.x2axis.show){af.top+=al.x2axis.getHeight()}if(this.title.show){af.top+=this.title.getHeight()}if(al.xaxis.show){af.bottom+=al.xaxis.getHeight()}if(this.options.gridDimensions&&H.isPlainObject(this.options.gridDimensions)){var am=parseInt(this.options.gridDimensions.width,10)||0;var aC=parseInt(this.options.gridDimensions.height,10)||0;var ah=(this._width-af.left-af.right-am)/2;var aE=(this._height-af.top-af.bottom-aC)/2;if(aE>=0&&ah>=0){af.top+=aE;af.bottom+=aE;af.left+=ah;af.right+=ah}}var ag=["top","bottom","left","right"];for(var au in ag){if(this._gridPadding[ag[au]]==null&&af[ag[au]]>0){this._gridPadding[ag[au]]=af[ag[au]]}else{if(this._gridPadding[ag[au]]==null){this._gridPadding[ag[au]]=this._defaultGridPadding[ag[au]]}}}var at=(this.legend.placement=="outsideGrid")?{top:this.title.getHeight(),left:0,right:0,bottom:0}:this._gridPadding;al.xaxis.pack({position:"absolute",bottom:this._gridPadding.bottom-al.xaxis.getHeight(),left:0,width:this._width},{min:this._gridPadding.left,max:this._width-this._gridPadding.right});al.yaxis.pack({position:"absolute",top:0,left:this._gridPadding.left-al.yaxis.getWidth(),height:this._height},{min:this._height-this._gridPadding.bottom,max:this._gridPadding.top});al.x2axis.pack({position:"absolute",top:this._gridPadding.top-al.x2axis.getHeight(),left:0,width:this._width},{min:this._gridPadding.left,max:this._width-this._gridPadding.right});for(aB=8;aB>0;aB--){al[aA[aB-1]].pack({position:"absolute",top:0,right:this._gridPadding.right-ar[aB-1]},{min:this._height-this._gridPadding.bottom,max:this._gridPadding.top})}var an=(this._width-this._gridPadding.left-this._gridPadding.right)/2+this._gridPadding.left-al.yMidAxis.getWidth()/2;al.yMidAxis.pack({position:"absolute",top:0,left:an,zIndex:9,textAlign:"center"},{min:this._height-this._gridPadding.bottom,max:this._gridPadding.top});this.target.append(this.grid.createElement(this._gridPadding,this));this.grid.draw();var ak=this.series;var aD=ak.length;for(aB=0,ay=aD;aB<ay;aB++){az=this.seriesStack[aB];this.target.append(ak[az].shadowCanvas.createElement(this._gridPadding,"jqplot-series-shadowCanvas",null,this));ak[az].shadowCanvas.setContext();ak[az].shadowCanvas._elem.data("seriesIndex",az)}for(aB=0,ay=aD;aB<ay;aB++){az=this.seriesStack[aB];this.target.append(ak[az].canvas.createElement(this._gridPadding,"jqplot-series-canvas",null,this));ak[az].canvas.setContext();ak[az].canvas._elem.data("seriesIndex",az)}this.target.append(this.eventCanvas.createElement(this._gridPadding,"jqplot-event-canvas",null,this));this.eventCanvas.setContext();this.eventCanvas._ctx.fillStyle="rgba(0,0,0,0)";this.eventCanvas._ctx.fillRect(0,0,this.eventCanvas._ctx.canvas.width,this.eventCanvas._ctx.canvas.height);this.bindCustomEvents();if(this.legend.preDraw){this.eventCanvas._elem.before(aF);this.legend.pack(at);if(this.legend._elem){this.drawSeries({legendInfo:{location:this.legend.location,placement:this.legend.placement,width:this.legend.getWidth(),height:this.legend.getHeight(),xoffset:this.legend.xoffset,yoffset:this.legend.yoffset}})}else{this.drawSeries()}}else{this.drawSeries();if(aD){H(ak[aD-1].canvas._elem).after(aF)}this.legend.pack(at)}for(var aB=0,ay=H.jqplot.eventListenerHooks.length;aB<ay;aB++){this.eventCanvas._elem.bind(H.jqplot.eventListenerHooks[aB][0],{plot:this},H.jqplot.eventListenerHooks[aB][1])}for(var aB=0,ay=this.eventListenerHooks.hooks.length;aB<ay;aB++){this.eventCanvas._elem.bind(this.eventListenerHooks.hooks[aB][0],{plot:this},this.eventListenerHooks.hooks[aB][1])}var aq=this.fillBetween;if(aq.fill&&aq.series1!==aq.series2&&aq.series1<aD&&aq.series2<aD&&ak[aq.series1]._type==="line"&&ak[aq.series2]._type==="line"){this.doFillBetweenLines()}for(var aB=0,ay=H.jqplot.postDrawHooks.length;aB<ay;aB++){H.jqplot.postDrawHooks[aB].call(this)}for(var aB=0,ay=this.postDrawHooks.hooks.length;aB<ay;aB++){this.postDrawHooks.hooks[aB].apply(this,this.postDrawHooks.args[aB])}if(this.target.is(":visible")){this._drawCount+=1}var ao,ap,aw,aj;for(aB=0,ay=aD;aB<ay;aB++){ao=ak[aB];ap=ao.renderer;aw=".jqplot-point-label.jqplot-series-"+aB;if(ap.animation&&ap.animation._supported&&ap.animation.show&&(this._drawCount<2||this.animateReplot)){aj=this.target.find(aw);aj.stop(true,true).hide();ao.canvas._elem.stop(true,true).hide();ao.shadowCanvas._elem.stop(true,true).hide();ao.canvas._elem.jqplotEffect("blind",{mode:"show",direction:ap.animation.direction},ap.animation.speed);ao.shadowCanvas._elem.jqplotEffect("blind",{mode:"show",direction:ap.animation.direction},ap.animation.speed);aj.fadeIn(ap.animation.speed*0.8)}}aj=null;this.target.trigger("jqplotPostDraw",[this])}};N.prototype.doFillBetweenLines=function(){var ah=this.fillBetween;var aq=ah.series1;var ao=ah.series2;var ap=(aq<ao)?aq:ao;var an=(ao>aq)?ao:aq;var al=this.series[ap];var ak=this.series[an];if(ak.renderer.smooth){var aj=ak.renderer._smoothedData.slice(0).reverse()}else{var aj=ak.gridData.slice(0).reverse()}if(al.renderer.smooth){var am=al.renderer._smoothedData.concat(aj)}else{var am=al.gridData.concat(aj)}var ai=(ah.color!==null)?ah.color:this.series[aq].fillColor;var ar=(ah.baseSeries!==null)?ah.baseSeries:ap;var ag=this.series[ar].renderer.shapeRenderer;var af={fillStyle:ai,fill:true,closePath:true};ag.draw(al.shadowCanvas._ctx,am,af)};this.bindCustomEvents=function(){this.eventCanvas._elem.bind("click",{plot:this},this.onClick);this.eventCanvas._elem.bind("dblclick",{plot:this},this.onDblClick);this.eventCanvas._elem.bind("mousedown",{plot:this},this.onMouseDown);this.eventCanvas._elem.bind("mousemove",{plot:this},this.onMouseMove);this.eventCanvas._elem.bind("mouseenter",{plot:this},this.onMouseEnter);this.eventCanvas._elem.bind("mouseleave",{plot:this},this.onMouseLeave);if(this.captureRightClick){this.eventCanvas._elem.bind("mouseup",{plot:this},this.onRightClick);this.eventCanvas._elem.get(0).oncontextmenu=function(){return false}}else{this.eventCanvas._elem.bind("mouseup",{plot:this},this.onMouseUp)}};function ac(ao){var am=ao.data.plot;var ai=am.eventCanvas._elem.offset();var al={x:ao.pageX-ai.left,y:ao.pageY-ai.top};var aj={xaxis:null,yaxis:null,x2axis:null,y2axis:null,y3axis:null,y4axis:null,y5axis:null,y6axis:null,y7axis:null,y8axis:null,y9axis:null,yMidAxis:null};var ak=["xaxis","yaxis","x2axis","y2axis","y3axis","y4axis","y5axis","y6axis","y7axis","y8axis","y9axis","yMidAxis"];var af=am.axes;var ag,ah;for(ag=11;ag>0;ag--){ah=ak[ag-1];if(af[ah].show){aj[ah]=af[ah].series_p2u(al[ah.charAt(0)])}}return{offsets:ai,gridPos:al,dataPos:aj}}function ae(af,ag){var ak=ag.series;var aP,aO,aN,aI,aJ,aD,aC,ap,an,at,au,aE;var aM,aQ,aK,al,aB,aG;var ah,aH;for(aN=ag.seriesStack.length-1;aN>=0;aN--){aP=ag.seriesStack[aN];aI=ak[aP];switch(aI.renderer.constructor){case H.jqplot.BarRenderer:case H.jqplot.PyramidRenderer:aD=af.x;aC=af.y;for(aO=0;aO<aI._barPoints.length;aO++){aB=aI._barPoints[aO];aK=aI.gridData[aO];if(aD>aB[0][0]&&aD<aB[2][0]&&aC>aB[2][1]&&aC<aB[0][1]){return{seriesIndex:aI.index,pointIndex:aO,gridData:aK,data:aI.data[aO],points:aI._barPoints[aO]}}}break;case H.jqplot.DonutRenderer:at=aI.startAngle/180*Math.PI;aD=af.x-aI._center[0];aC=af.y-aI._center[1];aJ=Math.sqrt(Math.pow(aD,2)+Math.pow(aC,2));if(aD>0&&-aC>=0){ap=2*Math.PI-Math.atan(-aC/aD)}else{if(aD>0&&-aC<0){ap=-Math.atan(-aC/aD)}else{if(aD<0){ap=Math.PI-Math.atan(-aC/aD)}else{if(aD==0&&-aC>0){ap=3*Math.PI/2}else{if(aD==0&&-aC<0){ap=Math.PI/2}else{if(aD==0&&aC==0){ap=0}}}}}}if(at){ap-=at;if(ap<0){ap+=2*Math.PI}else{if(ap>2*Math.PI){ap-=2*Math.PI}}}an=aI.sliceMargin/180*Math.PI;if(aJ<aI._radius&&aJ>aI._innerRadius){for(aO=0;aO<aI.gridData.length;aO++){au=(aO>0)?aI.gridData[aO-1][1]+an:an;aE=aI.gridData[aO][1];if(ap>au&&ap<aE){return{seriesIndex:aI.index,pointIndex:aO,gridData:aI.gridData[aO],data:aI.data[aO]}}}}break;case H.jqplot.PieRenderer:at=aI.startAngle/180*Math.PI;aD=af.x-aI._center[0];aC=af.y-aI._center[1];aJ=Math.sqrt(Math.pow(aD,2)+Math.pow(aC,2));if(aD>0&&-aC>=0){ap=2*Math.PI-Math.atan(-aC/aD)}else{if(aD>0&&-aC<0){ap=-Math.atan(-aC/aD)}else{if(aD<0){ap=Math.PI-Math.atan(-aC/aD)}else{if(aD==0&&-aC>0){ap=3*Math.PI/2}else{if(aD==0&&-aC<0){ap=Math.PI/2}else{if(aD==0&&aC==0){ap=0}}}}}}if(at){ap-=at;if(ap<0){ap+=2*Math.PI}else{if(ap>2*Math.PI){ap-=2*Math.PI}}}an=aI.sliceMargin/180*Math.PI;if(aJ<aI._radius){for(aO=0;aO<aI.gridData.length;aO++){au=(aO>0)?aI.gridData[aO-1][1]+an:an;aE=aI.gridData[aO][1];if(ap>au&&ap<aE){return{seriesIndex:aI.index,pointIndex:aO,gridData:aI.gridData[aO],data:aI.data[aO]}}}}break;case H.jqplot.BubbleRenderer:aD=af.x;aC=af.y;var az=null;if(aI.show){for(var aO=0;aO<aI.gridData.length;aO++){aK=aI.gridData[aO];aQ=Math.sqrt((aD-aK[0])*(aD-aK[0])+(aC-aK[1])*(aC-aK[1]));if(aQ<=aK[2]&&(aQ<=aM||aM==null)){aM=aQ;az={seriesIndex:aP,pointIndex:aO,gridData:aK,data:aI.data[aO]}}}if(az!=null){return az}}break;case H.jqplot.FunnelRenderer:aD=af.x;aC=af.y;var aF=aI._vertices,aj=aF[0],ai=aF[aF.length-1],am,ay,ar;function aL(aT,aV,aU){var aS=(aV[1]-aU[1])/(aV[0]-aU[0]);var aR=aV[1]-aS*aV[0];var aW=aT+aV[1];return[(aW-aR)/aS,aW]}am=aL(aC,aj[0],ai[3]);ay=aL(aC,aj[1],ai[2]);for(aO=0;aO<aF.length;aO++){ar=aF[aO];if(aC>=ar[0][1]&&aC<=ar[3][1]&&aD>=am[0]&&aD<=ay[0]){return{seriesIndex:aI.index,pointIndex:aO,gridData:null,data:aI.data[aO]}}}break;case H.jqplot.LineRenderer:aD=af.x;aC=af.y;aJ=aI.renderer;if(aI.show){if((aI.fill||(aI.renderer.bands.show&&aI.renderer.bands.fill))&&(!ag.plugins.highlighter||!ag.plugins.highlighter.show)){var aq=false;if(aD>aI._boundingBox[0][0]&&aD<aI._boundingBox[1][0]&&aC>aI._boundingBox[1][1]&&aC<aI._boundingBox[0][1]){var ax=aI._areaPoints.length;var aA;var aO=ax-1;for(var aA=0;aA<ax;aA++){var aw=[aI._areaPoints[aA][0],aI._areaPoints[aA][1]];var av=[aI._areaPoints[aO][0],aI._areaPoints[aO][1]];if(aw[1]<aC&&av[1]>=aC||av[1]<aC&&aw[1]>=aC){if(aw[0]+(aC-aw[1])/(av[1]-aw[1])*(av[0]-aw[0])<aD){aq=!aq}}aO=aA}}if(aq){return{seriesIndex:aP,pointIndex:null,gridData:aI.gridData,data:aI.data,points:aI._areaPoints}}break}else{aH=aI.markerRenderer.size/2+aI.neighborThreshold;ah=(aH>0)?aH:0;for(var aO=0;aO<aI.gridData.length;aO++){aK=aI.gridData[aO];if(aJ.constructor==H.jqplot.OHLCRenderer){if(aJ.candleStick){var ao=aI._yaxis.series_u2p;if(aD>=aK[0]-aJ._bodyWidth/2&&aD<=aK[0]+aJ._bodyWidth/2&&aC>=ao(aI.data[aO][2])&&aC<=ao(aI.data[aO][3])){return{seriesIndex:aP,pointIndex:aO,gridData:aK,data:aI.data[aO]}}}else{if(!aJ.hlc){var ao=aI._yaxis.series_u2p;if(aD>=aK[0]-aJ._tickLength&&aD<=aK[0]+aJ._tickLength&&aC>=ao(aI.data[aO][2])&&aC<=ao(aI.data[aO][3])){return{seriesIndex:aP,pointIndex:aO,gridData:aK,data:aI.data[aO]}}}else{var ao=aI._yaxis.series_u2p;if(aD>=aK[0]-aJ._tickLength&&aD<=aK[0]+aJ._tickLength&&aC>=ao(aI.data[aO][1])&&aC<=ao(aI.data[aO][2])){return{seriesIndex:aP,pointIndex:aO,gridData:aK,data:aI.data[aO]}}}}}else{if(aK[0]!=null&&aK[1]!=null){aQ=Math.sqrt((aD-aK[0])*(aD-aK[0])+(aC-aK[1])*(aC-aK[1]));if(aQ<=ah&&(aQ<=aM||aM==null)){aM=aQ;return{seriesIndex:aP,pointIndex:aO,gridData:aK,data:aI.data[aO]}}}}}}}break;default:aD=af.x;aC=af.y;aJ=aI.renderer;if(aI.show){aH=aI.markerRenderer.size/2+aI.neighborThreshold;ah=(aH>0)?aH:0;for(var aO=0;aO<aI.gridData.length;aO++){aK=aI.gridData[aO];if(aJ.constructor==H.jqplot.OHLCRenderer){if(aJ.candleStick){var ao=aI._yaxis.series_u2p;if(aD>=aK[0]-aJ._bodyWidth/2&&aD<=aK[0]+aJ._bodyWidth/2&&aC>=ao(aI.data[aO][2])&&aC<=ao(aI.data[aO][3])){return{seriesIndex:aP,pointIndex:aO,gridData:aK,data:aI.data[aO]}}}else{if(!aJ.hlc){var ao=aI._yaxis.series_u2p;if(aD>=aK[0]-aJ._tickLength&&aD<=aK[0]+aJ._tickLength&&aC>=ao(aI.data[aO][2])&&aC<=ao(aI.data[aO][3])){return{seriesIndex:aP,pointIndex:aO,gridData:aK,data:aI.data[aO]}}}else{var ao=aI._yaxis.series_u2p;if(aD>=aK[0]-aJ._tickLength&&aD<=aK[0]+aJ._tickLength&&aC>=ao(aI.data[aO][1])&&aC<=ao(aI.data[aO][2])){return{seriesIndex:aP,pointIndex:aO,gridData:aK,data:aI.data[aO]}}}}}else{aQ=Math.sqrt((aD-aK[0])*(aD-aK[0])+(aC-aK[1])*(aC-aK[1]));if(aQ<=ah&&(aQ<=aM||aM==null)){aM=aQ;return{seriesIndex:aP,pointIndex:aO,gridData:aK,data:aI.data[aO]}}}}}break}}return null}this.onClick=function(ah){var ag=ac(ah);var aj=ah.data.plot;var ai=ae(ag.gridPos,aj);var af=jQuery.Event("jqplotClick");af.pageX=ah.pageX;af.pageY=ah.pageY;H(this).trigger(af,[ag.gridPos,ag.dataPos,ai,aj])};this.onDblClick=function(ah){var ag=ac(ah);var aj=ah.data.plot;var ai=ae(ag.gridPos,aj);var af=jQuery.Event("jqplotDblClick");af.pageX=ah.pageX;af.pageY=ah.pageY;H(this).trigger(af,[ag.gridPos,ag.dataPos,ai,aj])};this.onMouseDown=function(ah){var ag=ac(ah);var aj=ah.data.plot;var ai=ae(ag.gridPos,aj);var af=jQuery.Event("jqplotMouseDown");af.pageX=ah.pageX;af.pageY=ah.pageY;H(this).trigger(af,[ag.gridPos,ag.dataPos,ai,aj])};this.onMouseUp=function(ah){var ag=ac(ah);var af=jQuery.Event("jqplotMouseUp");af.pageX=ah.pageX;af.pageY=ah.pageY;H(this).trigger(af,[ag.gridPos,ag.dataPos,null,ah.data.plot])};this.onRightClick=function(ah){var ag=ac(ah);var aj=ah.data.plot;var ai=ae(ag.gridPos,aj);if(aj.captureRightClick){if(ah.which==3){var af=jQuery.Event("jqplotRightClick");af.pageX=ah.pageX;af.pageY=ah.pageY;H(this).trigger(af,[ag.gridPos,ag.dataPos,ai,aj])}else{var af=jQuery.Event("jqplotMouseUp");af.pageX=ah.pageX;af.pageY=ah.pageY;H(this).trigger(af,[ag.gridPos,ag.dataPos,ai,aj])}}};this.onMouseMove=function(ah){var ag=ac(ah);var aj=ah.data.plot;var ai=ae(ag.gridPos,aj);var af=jQuery.Event("jqplotMouseMove");af.pageX=ah.pageX;af.pageY=ah.pageY;H(this).trigger(af,[ag.gridPos,ag.dataPos,ai,aj])};this.onMouseEnter=function(ah){var ag=ac(ah);var ai=ah.data.plot;var af=jQuery.Event("jqplotMouseEnter");af.pageX=ah.pageX;af.pageY=ah.pageY;af.relatedTarget=ah.relatedTarget;H(this).trigger(af,[ag.gridPos,ag.dataPos,null,ai])};this.onMouseLeave=function(ah){var ag=ac(ah);var ai=ah.data.plot;var af=jQuery.Event("jqplotMouseLeave");af.pageX=ah.pageX;af.pageY=ah.pageY;af.relatedTarget=ah.relatedTarget;H(this).trigger(af,[ag.gridPos,ag.dataPos,null,ai])};this.drawSeries=function(ah,af){var aj,ai,ag;af=(typeof(ah)==="number"&&af==null)?ah:af;ah=(typeof(ah)==="object")?ah:{};if(af!=r){ai=this.series[af];ag=ai.shadowCanvas._ctx;ag.clearRect(0,0,ag.canvas.width,ag.canvas.height);ai.drawShadow(ag,ah,this);ag=ai.canvas._ctx;ag.clearRect(0,0,ag.canvas.width,ag.canvas.height);ai.draw(ag,ah,this);if(ai.renderer.constructor==H.jqplot.BezierCurveRenderer){if(af<this.series.length-1){this.drawSeries(af+1)}}}else{for(aj=0;aj<this.series.length;aj++){ai=this.series[aj];ag=ai.shadowCanvas._ctx;ag.clearRect(0,0,ag.canvas.width,ag.canvas.height);ai.drawShadow(ag,ah,this);ag=ai.canvas._ctx;ag.clearRect(0,0,ag.canvas.width,ag.canvas.height);ai.draw(ag,ah,this)}}ah=af=aj=ai=ag=null};this.moveSeriesToFront=function(ag){ag=parseInt(ag,10);var aj=H.inArray(ag,this.seriesStack);if(aj==-1){return}if(aj==this.seriesStack.length-1){this.previousSeriesStack=this.seriesStack.slice(0);return}var af=this.seriesStack[this.seriesStack.length-1];var ai=this.series[ag].canvas._elem.detach();var ah=this.series[ag].shadowCanvas._elem.detach();this.series[af].shadowCanvas._elem.after(ah);this.series[af].canvas._elem.after(ai);this.previousSeriesStack=this.seriesStack.slice(0);this.seriesStack.splice(aj,1);this.seriesStack.push(ag)};this.moveSeriesToBack=function(ag){ag=parseInt(ag,10);var aj=H.inArray(ag,this.seriesStack);if(aj==0||aj==-1){return}var af=this.seriesStack[0];var ai=this.series[ag].canvas._elem.detach();var ah=this.series[ag].shadowCanvas._elem.detach();this.series[af].shadowCanvas._elem.before(ah);this.series[af].canvas._elem.before(ai);this.previousSeriesStack=this.seriesStack.slice(0);this.seriesStack.splice(aj,1);this.seriesStack.unshift(ag)};this.restorePreviousSeriesOrder=function(){var al,ak,aj,ai,ah,af,ag;if(this.seriesStack==this.previousSeriesStack){return}for(al=1;al<this.previousSeriesStack.length;al++){af=this.previousSeriesStack[al];ag=this.previousSeriesStack[al-1];aj=this.series[af].canvas._elem.detach();ai=this.series[af].shadowCanvas._elem.detach();this.series[ag].shadowCanvas._elem.after(ai);this.series[ag].canvas._elem.after(aj)}ah=this.seriesStack.slice(0);this.seriesStack=this.previousSeriesStack.slice(0);this.previousSeriesStack=ah};this.restoreOriginalSeriesOrder=function(){var aj,ai,af=[],ah,ag;for(aj=0;aj<this.series.length;aj++){af.push(aj)}if(this.seriesStack==af){return}this.previousSeriesStack=this.seriesStack.slice(0);this.seriesStack=af;for(aj=1;aj<this.seriesStack.length;aj++){ah=this.series[aj].canvas._elem.detach();ag=this.series[aj].shadowCanvas._elem.detach();this.series[aj-1].shadowCanvas._elem.after(ag);this.series[aj-1].canvas._elem.after(ah)}};this.activateTheme=function(af){this.themeEngine.activate(this,af)}}H.jqplot.computeHighlightColors=function(ac){var ae;if(jQuery.isArray(ac)){ae=[];for(var ag=0;ag<ac.length;ag++){var af=H.jqplot.getColorComponents(ac[ag]);var ab=[af[0],af[1],af[2]];var ah=ab[0]+ab[1]+ab[2];for(var ad=0;ad<3;ad++){ab[ad]=(ah>660)?ab[ad]*0.85:0.73*ab[ad]+90;ab[ad]=parseInt(ab[ad],10);(ab[ad]>255)?255:ab[ad]}ab[3]=0.3+0.35*af[3];ae.push("rgba("+ab[0]+","+ab[1]+","+ab[2]+","+ab[3]+")")}}else{var af=H.jqplot.getColorComponents(ac);var ab=[af[0],af[1],af[2]];var ah=ab[0]+ab[1]+ab[2];for(var ad=0;ad<3;ad++){ab[ad]=(ah>660)?ab[ad]*0.85:0.73*ab[ad]+90;ab[ad]=parseInt(ab[ad],10);(ab[ad]>255)?255:ab[ad]}ab[3]=0.3+0.35*af[3];ae="rgba("+ab[0]+","+ab[1]+","+ab[2]+","+ab[3]+")"}return ae};H.jqplot.ColorGenerator=function(ac){ac=ac||H.jqplot.config.defaultColors;var ab=0;this.next=function(){if(ab<ac.length){return ac[ab++]}else{ab=0;return ac[ab++]}};this.previous=function(){if(ab>0){return ac[ab--]}else{ab=ac.length-1;return ac[ab]}};this.get=function(ae){var ad=ae-ac.length*Math.floor(ae/ac.length);return ac[ad]};this.setColors=function(ad){ac=ad};this.reset=function(){ab=0};this.getIndex=function(){return ab};this.setIndex=function(ad){ab=ad}};H.jqplot.hex2rgb=function(ad,ab){ad=ad.replace("#","");if(ad.length==3){ad=ad.charAt(0)+ad.charAt(0)+ad.charAt(1)+ad.charAt(1)+ad.charAt(2)+ad.charAt(2)}var ac;ac="rgba("+parseInt(ad.slice(0,2),16)+", "+parseInt(ad.slice(2,4),16)+", "+parseInt(ad.slice(4,6),16);if(ab){ac+=", "+ab}ac+=")";return ac};H.jqplot.rgb2hex=function(ag){var ad=/rgba?\( *([0-9]{1,3}\.?[0-9]*%?) *, *([0-9]{1,3}\.?[0-9]*%?) *, *([0-9]{1,3}\.?[0-9]*%?) *(?:, *[0-9.]*)?\)/;var ab=ag.match(ad);var af="#";for(var ae=1;ae<4;ae++){var ac;if(ab[ae].search(/%/)!=-1){ac=parseInt(255*ab[ae]/100,10).toString(16);if(ac.length==1){ac="0"+ac}}else{ac=parseInt(ab[ae],10).toString(16);if(ac.length==1){ac="0"+ac}}af+=ac}return af};H.jqplot.normalize2rgb=function(ac,ab){if(ac.search(/^ *rgba?\(/)!=-1){return ac}else{if(ac.search(/^ *#?[0-9a-fA-F]?[0-9a-fA-F]/)!=-1){return H.jqplot.hex2rgb(ac,ab)}else{throw"invalid color spec"}}};H.jqplot.getColorComponents=function(ag){ag=H.jqplot.colorKeywordMap[ag]||ag;var ae=H.jqplot.normalize2rgb(ag);var ad=/rgba?\( *([0-9]{1,3}\.?[0-9]*%?) *, *([0-9]{1,3}\.?[0-9]*%?) *, *([0-9]{1,3}\.?[0-9]*%?) *,? *([0-9.]* *)?\)/;var ab=ae.match(ad);var ac=[];for(var af=1;af<4;af++){if(ab[af].search(/%/)!=-1){ac[af-1]=parseInt(255*ab[af]/100,10)}else{ac[af-1]=parseInt(ab[af],10)}}ac[3]=parseFloat(ab[4])?parseFloat(ab[4]):1;return ac};H.jqplot.colorKeywordMap={aliceblue:"rgb(240, 248, 255)",antiquewhite:"rgb(250, 235, 215)",aqua:"rgb( 0, 255, 255)",aquamarine:"rgb(127, 255, 212)",azure:"rgb(240, 255, 255)",beige:"rgb(245, 245, 220)",bisque:"rgb(255, 228, 196)",black:"rgb( 0, 0, 0)",blanchedalmond:"rgb(255, 235, 205)",blue:"rgb( 0, 0, 255)",blueviolet:"rgb(138, 43, 226)",brown:"rgb(165, 42, 42)",burlywood:"rgb(222, 184, 135)",cadetblue:"rgb( 95, 158, 160)",chartreuse:"rgb(127, 255, 0)",chocolate:"rgb(210, 105, 30)",coral:"rgb(255, 127, 80)",cornflowerblue:"rgb(100, 149, 237)",cornsilk:"rgb(255, 248, 220)",crimson:"rgb(220, 20, 60)",cyan:"rgb( 0, 255, 255)",darkblue:"rgb( 0, 0, 139)",darkcyan:"rgb( 0, 139, 139)",darkgoldenrod:"rgb(184, 134, 11)",darkgray:"rgb(169, 169, 169)",darkgreen:"rgb( 0, 100, 0)",darkgrey:"rgb(169, 169, 169)",darkkhaki:"rgb(189, 183, 107)",darkmagenta:"rgb(139, 0, 139)",darkolivegreen:"rgb( 85, 107, 47)",darkorange:"rgb(255, 140, 0)",darkorchid:"rgb(153, 50, 204)",darkred:"rgb(139, 0, 0)",darksalmon:"rgb(233, 150, 122)",darkseagreen:"rgb(143, 188, 143)",darkslateblue:"rgb( 72, 61, 139)",darkslategray:"rgb( 47, 79, 79)",darkslategrey:"rgb( 47, 79, 79)",darkturquoise:"rgb( 0, 206, 209)",darkviolet:"rgb(148, 0, 211)",deeppink:"rgb(255, 20, 147)",deepskyblue:"rgb( 0, 191, 255)",dimgray:"rgb(105, 105, 105)",dimgrey:"rgb(105, 105, 105)",dodgerblue:"rgb( 30, 144, 255)",firebrick:"rgb(178, 34, 34)",floralwhite:"rgb(255, 250, 240)",forestgreen:"rgb( 34, 139, 34)",fuchsia:"rgb(255, 0, 255)",gainsboro:"rgb(220, 220, 220)",ghostwhite:"rgb(248, 248, 255)",gold:"rgb(255, 215, 0)",goldenrod:"rgb(218, 165, 32)",gray:"rgb(128, 128, 128)",grey:"rgb(128, 128, 128)",green:"rgb( 0, 128, 0)",greenyellow:"rgb(173, 255, 47)",honeydew:"rgb(240, 255, 240)",hotpink:"rgb(255, 105, 180)",indianred:"rgb(205, 92, 92)",indigo:"rgb( 75, 0, 130)",ivory:"rgb(255, 255, 240)",khaki:"rgb(240, 230, 140)",lavender:"rgb(230, 230, 250)",lavenderblush:"rgb(255, 240, 245)",lawngreen:"rgb(124, 252, 0)",lemonchiffon:"rgb(255, 250, 205)",lightblue:"rgb(173, 216, 230)",lightcoral:"rgb(240, 128, 128)",lightcyan:"rgb(224, 255, 255)",lightgoldenrodyellow:"rgb(250, 250, 210)",lightgray:"rgb(211, 211, 211)",lightgreen:"rgb(144, 238, 144)",lightgrey:"rgb(211, 211, 211)",lightpink:"rgb(255, 182, 193)",lightsalmon:"rgb(255, 160, 122)",lightseagreen:"rgb( 32, 178, 170)",lightskyblue:"rgb(135, 206, 250)",lightslategray:"rgb(119, 136, 153)",lightslategrey:"rgb(119, 136, 153)",lightsteelblue:"rgb(176, 196, 222)",lightyellow:"rgb(255, 255, 224)",lime:"rgb( 0, 255, 0)",limegreen:"rgb( 50, 205, 50)",linen:"rgb(250, 240, 230)",magenta:"rgb(255, 0, 255)",maroon:"rgb(128, 0, 0)",mediumaquamarine:"rgb(102, 205, 170)",mediumblue:"rgb( 0, 0, 205)",mediumorchid:"rgb(186, 85, 211)",mediumpurple:"rgb(147, 112, 219)",mediumseagreen:"rgb( 60, 179, 113)",mediumslateblue:"rgb(123, 104, 238)",mediumspringgreen:"rgb( 0, 250, 154)",mediumturquoise:"rgb( 72, 209, 204)",mediumvioletred:"rgb(199, 21, 133)",midnightblue:"rgb( 25, 25, 112)",mintcream:"rgb(245, 255, 250)",mistyrose:"rgb(255, 228, 225)",moccasin:"rgb(255, 228, 181)",navajowhite:"rgb(255, 222, 173)",navy:"rgb( 0, 0, 128)",oldlace:"rgb(253, 245, 230)",olive:"rgb(128, 128, 0)",olivedrab:"rgb(107, 142, 35)",orange:"rgb(255, 165, 0)",orangered:"rgb(255, 69, 0)",orchid:"rgb(218, 112, 214)",palegoldenrod:"rgb(238, 232, 170)",palegreen:"rgb(152, 251, 152)",paleturquoise:"rgb(175, 238, 238)",palevioletred:"rgb(219, 112, 147)",papayawhip:"rgb(255, 239, 213)",peachpuff:"rgb(255, 218, 185)",peru:"rgb(205, 133, 63)",pink:"rgb(255, 192, 203)",plum:"rgb(221, 160, 221)",powderblue:"rgb(176, 224, 230)",purple:"rgb(128, 0, 128)",red:"rgb(255, 0, 0)",rosybrown:"rgb(188, 143, 143)",royalblue:"rgb( 65, 105, 225)",saddlebrown:"rgb(139, 69, 19)",salmon:"rgb(250, 128, 114)",sandybrown:"rgb(244, 164, 96)",seagreen:"rgb( 46, 139, 87)",seashell:"rgb(255, 245, 238)",sienna:"rgb(160, 82, 45)",silver:"rgb(192, 192, 192)",skyblue:"rgb(135, 206, 235)",slateblue:"rgb(106, 90, 205)",slategray:"rgb(112, 128, 144)",slategrey:"rgb(112, 128, 144)",snow:"rgb(255, 250, 250)",springgreen:"rgb( 0, 255, 127)",steelblue:"rgb( 70, 130, 180)",tan:"rgb(210, 180, 140)",teal:"rgb( 0, 128, 128)",thistle:"rgb(216, 191, 216)",tomato:"rgb(255, 99, 71)",turquoise:"rgb( 64, 224, 208)",violet:"rgb(238, 130, 238)",wheat:"rgb(245, 222, 179)",white:"rgb(255, 255, 255)",whitesmoke:"rgb(245, 245, 245)",yellow:"rgb(255, 255, 0)",yellowgreen:"rgb(154, 205, 50)"};H.jqplot.AxisLabelRenderer=function(ab){H.jqplot.ElemContainer.call(this);this.axis;this.show=true;this.label="";this.fontFamily=null;this.fontSize=null;this.textColor=null;this._elem;this.escapeHTML=false;H.extend(true,this,ab)};H.jqplot.AxisLabelRenderer.prototype=new H.jqplot.ElemContainer();H.jqplot.AxisLabelRenderer.prototype.constructor=H.jqplot.AxisLabelRenderer;H.jqplot.AxisLabelRenderer.prototype.init=function(ab){H.extend(true,this,ab)};H.jqplot.AxisLabelRenderer.prototype.draw=function(ab,ac){if(this._elem){this._elem.emptyForce();this._elem=null}this._elem=H('<div style="position:absolute;" class="jqplot-'+this.axis+'-label"></div>');if(Number(this.label)){this._elem.css("white-space","nowrap")}if(!this.escapeHTML){this._elem.html(this.label)}else{this._elem.text(this.label)}if(this.fontFamily){this._elem.css("font-family",this.fontFamily)}if(this.fontSize){this._elem.css("font-size",this.fontSize)}if(this.textColor){this._elem.css("color",this.textColor)}return this._elem};H.jqplot.AxisLabelRenderer.prototype.pack=function(){};H.jqplot.AxisTickRenderer=function(ab){H.jqplot.ElemContainer.call(this);this.mark="outside";this.axis;this.showMark=true;this.showGridline=true;this.isMinorTick=false;this.size=4;this.markSize=6;this.show=true;this.showLabel=true;this.label=null;this.value=null;this._styles={};this.formatter=H.jqplot.DefaultTickFormatter;this.prefix="";this.formatString="";this.fontFamily;this.fontSize;this.textColor;this.escapeHTML=false;this._elem;this._breakTick=false;H.extend(true,this,ab)};H.jqplot.AxisTickRenderer.prototype.init=function(ab){H.extend(true,this,ab)};H.jqplot.AxisTickRenderer.prototype=new H.jqplot.ElemContainer();H.jqplot.AxisTickRenderer.prototype.constructor=H.jqplot.AxisTickRenderer;H.jqplot.AxisTickRenderer.prototype.setTick=function(ab,ad,ac){this.value=ab;this.axis=ad;if(ac){this.isMinorTick=true}return this};H.jqplot.AxisTickRenderer.prototype.draw=function(){if(this.label===null){this.label=this.prefix+this.formatter(this.formatString,this.value)}var ac={position:"absolute"};if(Number(this.label)){ac.whitSpace="nowrap"}if(this._elem){this._elem.emptyForce();this._elem=null}this._elem=H(document.createElement("div"));this._elem.addClass("jqplot-"+this.axis+"-tick");if(!this.escapeHTML){this._elem.html(this.label)}else{this._elem.text(this.label)}this._elem.css(ac);for(var ab in this._styles){this._elem.css(ab,this._styles[ab])}if(this.fontFamily){this._elem.css("font-family",this.fontFamily)}if(this.fontSize){this._elem.css("font-size",this.fontSize)}if(this.textColor){this._elem.css("color",this.textColor)}if(this._breakTick){this._elem.addClass("jqplot-breakTick")}return this._elem};H.jqplot.DefaultTickFormatter=function(ab,ac){if(typeof ac=="number"){if(!ab){ab=H.jqplot.config.defaultTickFormatString}return H.jqplot.sprintf(ab,ac)}else{return String(ac)}};H.jqplot.AxisTickRenderer.prototype.pack=function(){};H.jqplot.CanvasGridRenderer=function(){this.shadowRenderer=new H.jqplot.ShadowRenderer()};H.jqplot.CanvasGridRenderer.prototype.init=function(ac){this._ctx;H.extend(true,this,ac);var ab={lineJoin:"miter",lineCap:"round",fill:false,isarc:false,angle:this.shadowAngle,offset:this.shadowOffset,alpha:this.shadowAlpha,depth:this.shadowDepth,lineWidth:this.shadowWidth,closePath:false,strokeStyle:this.shadowColor};this.renderer.shadowRenderer.init(ab)};H.jqplot.CanvasGridRenderer.prototype.createElement=function(ae){var ad;if(this._elem){if(H.jqplot.use_excanvas&&window.G_vmlCanvasManager.uninitElement!==r){ad=this._elem.get(0);window.G_vmlCanvasManager.uninitElement(ad);ad=null}this._elem.emptyForce();this._elem=null}ad=ae.canvasManager.getCanvas();var ab=this._plotDimensions.width;var ac=this._plotDimensions.height;ad.width=ab;ad.height=ac;this._elem=H(ad);this._elem.addClass("jqplot-grid-canvas");this._elem.css({position:"absolute",left:0,top:0});ad=ae.canvasManager.initCanvas(ad);this._top=this._offsets.top;this._bottom=ac-this._offsets.bottom;this._left=this._offsets.left;this._right=ab-this._offsets.right;this._width=this._right-this._left;this._height=this._bottom-this._top;ad=null;return this._elem};H.jqplot.CanvasGridRenderer.prototype.draw=function(){this._ctx=this._elem.get(0).getContext("2d");var am=this._ctx;var ap=this._axes;am.save();am.clearRect(0,0,this._plotDimensions.width,this._plotDimensions.height);am.fillStyle=this.backgroundColor||this.background;am.fillRect(this._left,this._top,this._width,this._height);am.save();am.lineJoin="miter";am.lineCap="butt";am.lineWidth=this.gridLineWidth;am.strokeStyle=this.gridLineColor;var at,ar,aj,ak;var ag=["xaxis","yaxis","x2axis","y2axis"];for(var aq=4;aq>0;aq--){var aw=ag[aq-1];var ab=ap[aw];var au=ab._ticks;var al=au.length;if(ab.show){if(ab.drawBaseline){var av={};if(ab.baselineWidth!==null){av.lineWidth=ab.baselineWidth}if(ab.baselineColor!==null){av.strokeStyle=ab.baselineColor}switch(aw){case"xaxis":ai(this._left,this._bottom,this._right,this._bottom,av);break;case"yaxis":ai(this._left,this._bottom,this._left,this._top,av);break;case"x2axis":ai(this._left,this._bottom,this._right,this._bottom,av);break;case"y2axis":ai(this._right,this._bottom,this._right,this._top,av);break}}for(var an=al;an>0;an--){var ah=au[an-1];if(ah.show){var ae=Math.round(ab.u2p(ah.value))+0.5;switch(aw){case"xaxis":if(ah.showGridline&&this.drawGridlines&&((!ah.isMinorTick&&ab.drawMajorGridlines)||(ah.isMinorTick&&ab.drawMinorGridlines))){ai(ae,this._top,ae,this._bottom)}if(ah.showMark&&ah.mark&&((!ah.isMinorTick&&ab.drawMajorTickMarks)||(ah.isMinorTick&&ab.drawMinorTickMarks))){aj=ah.markSize;ak=ah.mark;var ae=Math.round(ab.u2p(ah.value))+0.5;switch(ak){case"outside":at=this._bottom;ar=this._bottom+aj;break;case"inside":at=this._bottom-aj;ar=this._bottom;break;case"cross":at=this._bottom-aj;ar=this._bottom+aj;break;default:at=this._bottom;ar=this._bottom+aj;break}if(this.shadow){this.renderer.shadowRenderer.draw(am,[[ae,at],[ae,ar]],{lineCap:"butt",lineWidth:this.gridLineWidth,offset:this.gridLineWidth*0.75,depth:2,fill:false,closePath:false})}ai(ae,at,ae,ar)}break;case"yaxis":if(ah.showGridline&&this.drawGridlines&&((!ah.isMinorTick&&ab.drawMajorGridlines)||(ah.isMinorTick&&ab.drawMinorGridlines))){ai(this._right,ae,this._left,ae)}if(ah.showMark&&ah.mark&&((!ah.isMinorTick&&ab.drawMajorTickMarks)||(ah.isMinorTick&&ab.drawMinorTickMarks))){aj=ah.markSize;ak=ah.mark;var ae=Math.round(ab.u2p(ah.value))+0.5;switch(ak){case"outside":at=this._left-aj;ar=this._left;break;case"inside":at=this._left;ar=this._left+aj;break;case"cross":at=this._left-aj;ar=this._left+aj;break;default:at=this._left-aj;ar=this._left;break}if(this.shadow){this.renderer.shadowRenderer.draw(am,[[at,ae],[ar,ae]],{lineCap:"butt",lineWidth:this.gridLineWidth*1.5,offset:this.gridLineWidth*0.75,fill:false,closePath:false})}ai(at,ae,ar,ae,{strokeStyle:ab.borderColor})}break;case"x2axis":if(ah.showGridline&&this.drawGridlines&&((!ah.isMinorTick&&ab.drawMajorGridlines)||(ah.isMinorTick&&ab.drawMinorGridlines))){ai(ae,this._bottom,ae,this._top)}if(ah.showMark&&ah.mark&&((!ah.isMinorTick&&ab.drawMajorTickMarks)||(ah.isMinorTick&&ab.drawMinorTickMarks))){aj=ah.markSize;ak=ah.mark;var ae=Math.round(ab.u2p(ah.value))+0.5;switch(ak){case"outside":at=this._top-aj;ar=this._top;break;case"inside":at=this._top;ar=this._top+aj;break;case"cross":at=this._top-aj;ar=this._top+aj;break;default:at=this._top-aj;ar=this._top;break}if(this.shadow){this.renderer.shadowRenderer.draw(am,[[ae,at],[ae,ar]],{lineCap:"butt",lineWidth:this.gridLineWidth,offset:this.gridLineWidth*0.75,depth:2,fill:false,closePath:false})}ai(ae,at,ae,ar)}break;case"y2axis":if(ah.showGridline&&this.drawGridlines&&((!ah.isMinorTick&&ab.drawMajorGridlines)||(ah.isMinorTick&&ab.drawMinorGridlines))){ai(this._left,ae,this._right,ae)}if(ah.showMark&&ah.mark&&((!ah.isMinorTick&&ab.drawMajorTickMarks)||(ah.isMinorTick&&ab.drawMinorTickMarks))){aj=ah.markSize;ak=ah.mark;var ae=Math.round(ab.u2p(ah.value))+0.5;switch(ak){case"outside":at=this._right;ar=this._right+aj;break;case"inside":at=this._right-aj;ar=this._right;break;case"cross":at=this._right-aj;ar=this._right+aj;break;default:at=this._right;ar=this._right+aj;break}if(this.shadow){this.renderer.shadowRenderer.draw(am,[[at,ae],[ar,ae]],{lineCap:"butt",lineWidth:this.gridLineWidth*1.5,offset:this.gridLineWidth*0.75,fill:false,closePath:false})}ai(at,ae,ar,ae,{strokeStyle:ab.borderColor})}break;default:break}}}ah=null}ab=null;au=null}ag=["y3axis","y4axis","y5axis","y6axis","y7axis","y8axis","y9axis","yMidAxis"];for(var aq=7;aq>0;aq--){var ab=ap[ag[aq-1]];var au=ab._ticks;if(ab.show){var ac=au[ab.numberTicks-1];var af=au[0];var ad=ab.getLeft();var ao=[[ad,ac.getTop()+ac.getHeight()/2],[ad,af.getTop()+af.getHeight()/2+1]];if(this.shadow){this.renderer.shadowRenderer.draw(am,ao,{lineCap:"butt",fill:false,closePath:false})}ai(ao[0][0],ao[0][1],ao[1][0],ao[1][1],{lineCap:"butt",strokeStyle:ab.borderColor,lineWidth:ab.borderWidth});for(var an=au.length;an>0;an--){var ah=au[an-1];aj=ah.markSize;ak=ah.mark;var ae=Math.round(ab.u2p(ah.value))+0.5;if(ah.showMark&&ah.mark){switch(ak){case"outside":at=ad;ar=ad+aj;break;case"inside":at=ad-aj;ar=ad;break;case"cross":at=ad-aj;ar=ad+aj;break;default:at=ad;ar=ad+aj;break}ao=[[at,ae],[ar,ae]];if(this.shadow){this.renderer.shadowRenderer.draw(am,ao,{lineCap:"butt",lineWidth:this.gridLineWidth*1.5,offset:this.gridLineWidth*0.75,fill:false,closePath:false})}ai(at,ae,ar,ae,{strokeStyle:ab.borderColor})}ah=null}af=null}ab=null;au=null}am.restore();function ai(aB,aA,ay,ax,az){am.save();az=az||{};if(az.lineWidth==null||az.lineWidth!=0){H.extend(true,am,az);am.beginPath();am.moveTo(aB,aA);am.lineTo(ay,ax);am.stroke();am.restore()}}if(this.shadow){var ao=[[this._left,this._bottom],[this._right,this._bottom],[this._right,this._top]];this.renderer.shadowRenderer.draw(am,ao)}if(this.borderWidth!=0&&this.drawBorder){ai(this._left,this._top,this._right,this._top,{lineCap:"round",strokeStyle:ap.x2axis.borderColor,lineWidth:ap.x2axis.borderWidth});ai(this._right,this._top,this._right,this._bottom,{lineCap:"round",strokeStyle:ap.y2axis.borderColor,lineWidth:ap.y2axis.borderWidth});ai(this._right,this._bottom,this._left,this._bottom,{lineCap:"round",strokeStyle:ap.xaxis.borderColor,lineWidth:ap.xaxis.borderWidth});ai(this._left,this._bottom,this._left,this._top,{lineCap:"round",strokeStyle:ap.yaxis.borderColor,lineWidth:ap.yaxis.borderWidth})}am.restore();am=null;ap=null};H.jqplot.DivTitleRenderer=function(){};H.jqplot.DivTitleRenderer.prototype.init=function(ab){H.extend(true,this,ab)};H.jqplot.DivTitleRenderer.prototype.draw=function(){if(this._elem){this._elem.emptyForce();this._elem=null}var ae=this.renderer;var ad=document.createElement("div");this._elem=H(ad);this._elem.addClass("jqplot-title");if(!this.text){this.show=false;this._elem.height(0);this._elem.width(0)}else{if(this.text){var ab;if(this.color){ab=this.color}else{if(this.textColor){ab=this.textColor}}var ac={position:"absolute",top:"0px",left:"0px"};if(this._plotWidth){ac.width=this._plotWidth+"px"}if(this.fontSize){ac.fontSize=this.fontSize}if(typeof this.textAlign==="string"){ac.textAlign=this.textAlign}else{ac.textAlign="center"}if(ab){ac.color=ab}if(this.paddingBottom){ac.paddingBottom=this.paddingBottom}if(this.fontFamily){ac.fontFamily=this.fontFamily}this._elem.css(ac);if(this.escapeHtml){this._elem.text(this.text)}else{this._elem.html(this.text)}}}ad=null;return this._elem};H.jqplot.DivTitleRenderer.prototype.pack=function(){};var o=0.1;H.jqplot.LinePattern=function(ap,ak){var aj={dotted:[o,H.jqplot.config.dotGapLength],dashed:[H.jqplot.config.dashLength,H.jqplot.config.gapLength],solid:null};if(typeof ak==="string"){if(ak[0]==="."||ak[0]==="-"){var aq=ak;ak=[];for(var ai=0,af=aq.length;ai<af;ai++){if(aq[ai]==="."){ak.push(o)}else{if(aq[ai]==="-"){ak.push(H.jqplot.config.dashLength)}else{continue}}ak.push(H.jqplot.config.gapLength)}}else{ak=aj[ak]}}if(!(ak&&ak.length)){return ap}var ae=0;var al=ak[0];var an=0;var am=0;var ah=0;var ab=0;var ao=function(ar,at){ap.moveTo(ar,at);an=ar;am=at;ah=ar;ab=at};var ad=function(ar,ay){var aw=ap.lineWidth;var au=ar-an;var at=ay-am;var av=Math.sqrt(au*au+at*at);if((av>0)&&(aw>0)){au/=av;at/=av;while(true){var ax=aw*al;if(ax<av){an+=ax*au;am+=ax*at;if((ae&1)==0){ap.lineTo(an,am)}else{ap.moveTo(an,am)}av-=ax;ae++;if(ae>=ak.length){ae=0}al=ak[ae]}else{an=ar;am=ay;if((ae&1)==0){ap.lineTo(an,am)}else{ap.moveTo(an,am)}al-=av/aw;break}}}};var ac=function(){ap.beginPath()};var ag=function(){ad(ah,ab)};return{moveTo:ao,lineTo:ad,beginPath:ac,closePath:ag}};H.jqplot.LineRenderer=function(){this.shapeRenderer=new H.jqplot.ShapeRenderer();this.shadowRenderer=new H.jqplot.ShadowRenderer()};H.jqplot.LineRenderer.prototype.init=function(ac,ah){ac=ac||{};this._type="line";this.renderer.animation={show:false,direction:"left",speed:2500,_supported:true};this.renderer.smooth=false;this.renderer.tension=null;this.renderer.constrainSmoothing=true;this.renderer._smoothedData=[];this.renderer._smoothedPlotData=[];this.renderer._hiBandGridData=[];this.renderer._lowBandGridData=[];this.renderer._hiBandSmoothedData=[];this.renderer._lowBandSmoothedData=[];this.renderer.bandData=[];this.renderer.bands={show:false,hiData:[],lowData:[],color:this.color,showLines:false,fill:true,fillColor:null,_min:null,_max:null,interval:"3%"};var af={highlightMouseOver:ac.highlightMouseOver,highlightMouseDown:ac.highlightMouseDown,highlightColor:ac.highlightColor};delete (ac.highlightMouseOver);delete (ac.highlightMouseDown);delete (ac.highlightColor);H.extend(true,this.renderer,ac);this.renderer.options=ac;if(this.renderer.bandData.length>1&&(!ac.bands||ac.bands.show==null)){this.renderer.bands.show=true}else{if(ac.bands&&ac.bands.show==null&&ac.bands.interval!=null){this.renderer.bands.show=true}}if(this.fill){this.renderer.bands.show=false}if(this.renderer.bands.show){this.renderer.initBands.call(this,this.renderer.options,ah)}if(this._stack){this.renderer.smooth=false}var ag={lineJoin:this.lineJoin,lineCap:this.lineCap,fill:this.fill,isarc:false,strokeStyle:this.color,fillStyle:this.fillColor,lineWidth:this.lineWidth,linePattern:this.linePattern,closePath:this.fill};this.renderer.shapeRenderer.init(ag);var ad=ac.shadowOffset;if(ad==null){if(this.lineWidth>2.5){ad=1.25*(1+(Math.atan((this.lineWidth/2.5))/0.785398163-1)*0.6)}else{ad=1.25*Math.atan((this.lineWidth/2.5))/0.785398163}}var ab={lineJoin:this.lineJoin,lineCap:this.lineCap,fill:this.fill,isarc:false,angle:this.shadowAngle,offset:ad,alpha:this.shadowAlpha,depth:this.shadowDepth,lineWidth:this.lineWidth,linePattern:this.linePattern,closePath:this.fill};this.renderer.shadowRenderer.init(ab);this._areaPoints=[];this._boundingBox=[[],[]];if(!this.isTrendline&&this.fill||this.renderer.bands.show){this.highlightMouseOver=true;this.highlightMouseDown=false;this.highlightColor=null;if(af.highlightMouseDown&&af.highlightMouseOver==null){af.highlightMouseOver=false}H.extend(true,this,{highlightMouseOver:af.highlightMouseOver,highlightMouseDown:af.highlightMouseDown,highlightColor:af.highlightColor});if(!this.highlightColor){var ae=(this.renderer.bands.show)?this.renderer.bands.fillColor:this.fillColor;this.highlightColor=H.jqplot.computeHighlightColors(ae)}if(this.highlighter){this.highlighter.show=false}}if(!this.isTrendline&&ah){ah.plugins.lineRenderer={};ah.postInitHooks.addOnce(v);ah.postDrawHooks.addOnce(Z);ah.eventListenerHooks.addOnce("jqplotMouseMove",g);ah.eventListenerHooks.addOnce("jqplotMouseDown",d);ah.eventListenerHooks.addOnce("jqplotMouseUp",Y);ah.eventListenerHooks.addOnce("jqplotClick",f);ah.eventListenerHooks.addOnce("jqplotRightClick",p)}};H.jqplot.LineRenderer.prototype.initBands=function(ae,ao){var af=ae.bandData||[];var ah=this.renderer.bands;ah.hiData=[];ah.lowData=[];var av=this.data;ah._max=null;ah._min=null;if(af.length==2){if(H.isArray(af[0][0])){var ai;var ab=0,al=0;for(var ap=0,am=af[0].length;ap<am;ap++){ai=af[0][ap];if((ai[1]!=null&&ai[1]>ah._max)||ah._max==null){ah._max=ai[1]}if((ai[1]!=null&&ai[1]<ah._min)||ah._min==null){ah._min=ai[1]}}for(var ap=0,am=af[1].length;ap<am;ap++){ai=af[1][ap];if((ai[1]!=null&&ai[1]>ah._max)||ah._max==null){ah._max=ai[1];al=1}if((ai[1]!=null&&ai[1]<ah._min)||ah._min==null){ah._min=ai[1];ab=1}}if(al===ab){ah.show=false}ah.hiData=af[al];ah.lowData=af[ab]}else{if(af[0].length===av.length&&af[1].length===av.length){var ad=(af[0][0]>af[1][0])?0:1;var aw=(ad)?0:1;for(var ap=0,am=av.length;ap<am;ap++){ah.hiData.push([av[ap][0],af[ad][ap]]);ah.lowData.push([av[ap][0],af[aw][ap]])}}else{ah.show=false}}}else{if(af.length>2&&!H.isArray(af[0][0])){var ad=(af[0][0]>af[0][1])?0:1;var aw=(ad)?0:1;for(var ap=0,am=af.length;ap<am;ap++){ah.hiData.push([av[ap][0],af[ap][ad]]);ah.lowData.push([av[ap][0],af[ap][aw]])}}else{var ak=ah.interval;var au=null;var at=null;var ac=null;var an=null;if(H.isArray(ak)){au=ak[0];at=ak[1]}else{au=ak}if(isNaN(au)){if(au.charAt(au.length-1)==="%"){ac="multiply";au=parseFloat(au)/100+1}}else{au=parseFloat(au);ac="add"}if(at!==null&&isNaN(at)){if(at.charAt(at.length-1)==="%"){an="multiply";at=parseFloat(at)/100+1}}else{if(at!==null){at=parseFloat(at);an="add"}}if(au!==null){if(at===null){at=-au;an=ac;if(an==="multiply"){at+=2}}if(au<at){var aq=au;au=at;at=aq;aq=ac;ac=an;an=aq}for(var ap=0,am=av.length;ap<am;ap++){switch(ac){case"add":ah.hiData.push([av[ap][0],av[ap][1]+au]);break;case"multiply":ah.hiData.push([av[ap][0],av[ap][1]*au]);break}switch(an){case"add":ah.lowData.push([av[ap][0],av[ap][1]+at]);break;case"multiply":ah.lowData.push([av[ap][0],av[ap][1]*at]);break}}}else{ah.show=false}}}var ag=ah.hiData;var aj=ah.lowData;for(var ap=0,am=ag.length;ap<am;ap++){if((ag[ap][1]!=null&&ag[ap][1]>ah._max)||ah._max==null){ah._max=ag[ap][1]}}for(var ap=0,am=aj.length;ap<am;ap++){if((aj[ap][1]!=null&&aj[ap][1]<ah._min)||ah._min==null){ah._min=aj[ap][1]}}if(ah.fillColor===null){var ar=H.jqplot.getColorComponents(ah.color);ar[3]=ar[3]*0.5;ah.fillColor="rgba("+ar[0]+", "+ar[1]+", "+ar[2]+", "+ar[3]+")"}};function G(ac,ab){return(3.4182054+ab)*Math.pow(ac,-0.3534992)}function k(ad,ac){var ab=Math.sqrt(Math.pow((ac[0]-ad[0]),2)+Math.pow((ac[1]-ad[1]),2));return 5.7648*Math.log(ab)+7.4456}function w(ab){var ac=(Math.exp(2*ab)-1)/(Math.exp(2*ab)+1);return ac}function F(aD){var am=this.renderer.smooth;var ax=this.canvas.getWidth();var ah=this._xaxis.series_p2u;var aA=this._yaxis.series_p2u;var az=null;var ag=null;var at=aD.length/ax;var ad=[];var ar=[];if(!isNaN(parseFloat(am))){az=parseFloat(am)}else{az=G(at,0.5)}var ap=[];var ae=[];for(var ay=0,au=aD.length;ay<au;ay++){ap.push(aD[ay][1]);ae.push(aD[ay][0])}function ao(aE,aF){if(aE-aF==0){return Math.pow(10,10)}else{return aE-aF}}var aq,al,ak,aj;var ab=aD.length-1;for(var af=1,av=aD.length;af<av;af++){var ac=[];var an=[];for(var aw=0;aw<2;aw++){var ay=af-1+aw;if(ay==0||ay==ab){ac[aw]=Math.pow(10,10)}else{if(ap[ay+1]-ap[ay]==0||ap[ay]-ap[ay-1]==0){ac[aw]=0}else{if(((ae[ay+1]-ae[ay])/(ap[ay+1]-ap[ay])+(ae[ay]-ae[ay-1])/(ap[ay]-ap[ay-1]))==0){ac[aw]=0}else{if((ap[ay+1]-ap[ay])*(ap[ay]-ap[ay-1])<0){ac[aw]=0}else{ac[aw]=2/(ao(ae[ay+1],ae[ay])/(ap[ay+1]-ap[ay])+ao(ae[ay],ae[ay-1])/(ap[ay]-ap[ay-1]))}}}}}if(af==1){ac[0]=3/2*(ap[1]-ap[0])/ao(ae[1],ae[0])-ac[1]/2}else{if(af==ab){ac[1]=3/2*(ap[ab]-ap[ab-1])/ao(ae[ab],ae[ab-1])-ac[0]/2}}an[0]=-2*(ac[1]+2*ac[0])/ao(ae[af],ae[af-1])+6*(ap[af]-ap[af-1])/Math.pow(ao(ae[af],ae[af-1]),2);an[1]=2*(2*ac[1]+ac[0])/ao(ae[af],ae[af-1])-6*(ap[af]-ap[af-1])/Math.pow(ao(ae[af],ae[af-1]),2);aj=1/6*(an[1]-an[0])/ao(ae[af],ae[af-1]);ak=1/2*(ae[af]*an[0]-ae[af-1]*an[1])/ao(ae[af],ae[af-1]);al=(ap[af]-ap[af-1]-ak*(Math.pow(ae[af],2)-Math.pow(ae[af-1],2))-aj*(Math.pow(ae[af],3)-Math.pow(ae[af-1],3)))/ao(ae[af],ae[af-1]);aq=ap[af-1]-al*ae[af-1]-ak*Math.pow(ae[af-1],2)-aj*Math.pow(ae[af-1],3);var aC=(ae[af]-ae[af-1])/az;var aB,ai;for(var aw=0,au=az;aw<au;aw++){aB=[];ai=ae[af-1]+aw*aC;aB.push(ai);aB.push(aq+al*ai+ak*Math.pow(ai,2)+aj*Math.pow(ai,3));ad.push(aB);ar.push([ah(aB[0]),aA(aB[1])])}}ad.push(aD[ay]);ar.push([ah(aD[ay][0]),aA(aD[ay][1])]);return[ad,ar]}function B(aj){var ai=this.renderer.smooth;var aO=this.renderer.tension;var ab=this.canvas.getWidth();var aB=this._xaxis.series_p2u;var ak=this._yaxis.series_p2u;var aC=null;var aD=null;var aN=null;var aI=null;var aG=null;var am=null;var aL=null;var ag=null;var aE,aF,ax,aw,au,ar;var ae,ac,ao,an;var av,at,aH;var ap=[];var ad=[];var af=aj.length/ab;var aM,aq,az,aA,ay;var al=[];var ah=[];if(!isNaN(parseFloat(ai))){aC=parseFloat(ai)}else{aC=G(af,0.5)}if(!isNaN(parseFloat(aO))){aO=parseFloat(aO)}for(var aK=0,aJ=aj.length-1;aK<aJ;aK++){if(aO===null){am=Math.abs((aj[aK+1][1]-aj[aK][1])/(aj[aK+1][0]-aj[aK][0]));aM=0.3;aq=0.6;az=(aq-aM)/2;aA=2.5;ay=-1.4;ag=am/aA+ay;aI=az*w(ag)-az*w(ay)+aM;if(aK>0){aL=Math.abs((aj[aK][1]-aj[aK-1][1])/(aj[aK][0]-aj[aK-1][0]))}ag=aL/aA+ay;aG=az*w(ag)-az*w(ay)+aM;aN=(aI+aG)/2}else{aN=aO}for(aE=0;aE<aC;aE++){aF=aE/aC;ax=(1+2*aF)*Math.pow((1-aF),2);aw=aF*Math.pow((1-aF),2);au=Math.pow(aF,2)*(3-2*aF);ar=Math.pow(aF,2)*(aF-1);if(aj[aK-1]){ae=aN*(aj[aK+1][0]-aj[aK-1][0]);ac=aN*(aj[aK+1][1]-aj[aK-1][1])}else{ae=aN*(aj[aK+1][0]-aj[aK][0]);ac=aN*(aj[aK+1][1]-aj[aK][1])}if(aj[aK+2]){ao=aN*(aj[aK+2][0]-aj[aK][0]);an=aN*(aj[aK+2][1]-aj[aK][1])}else{ao=aN*(aj[aK+1][0]-aj[aK][0]);an=aN*(aj[aK+1][1]-aj[aK][1])}av=ax*aj[aK][0]+au*aj[aK+1][0]+aw*ae+ar*ao;at=ax*aj[aK][1]+au*aj[aK+1][1]+aw*ac+ar*an;aH=[av,at];al.push(aH);ah.push([aB(av),ak(at)])}}al.push(aj[aJ]);ah.push([aB(aj[aJ][0]),ak(aj[aJ][1])]);return[al,ah]}H.jqplot.LineRenderer.prototype.setGridData=function(aj){var af=this._xaxis.series_u2p;var ab=this._yaxis.series_u2p;var ag=this._plotData;var ak=this._prevPlotData;this.gridData=[];this._prevGridData=[];this.renderer._smoothedData=[];this.renderer._smoothedPlotData=[];this.renderer._hiBandGridData=[];this.renderer._lowBandGridData=[];this.renderer._hiBandSmoothedData=[];this.renderer._lowBandSmoothedData=[];var ae=this.renderer.bands;var ac=false;for(var ah=0,ad=this.data.length;ah<ad;ah++){if(ag[ah][0]!=null&&ag[ah][1]!=null){this.gridData.push([af.call(this._xaxis,ag[ah][0]),ab.call(this._yaxis,ag[ah][1])])}else{if(ag[ah][0]==null){ac=true;this.gridData.push([null,ab.call(this._yaxis,ag[ah][1])])}else{if(ag[ah][1]==null){ac=true;this.gridData.push([af.call(this._xaxis,ag[ah][0]),null])}}}if(ak[ah]!=null&&ak[ah][0]!=null&&ak[ah][1]!=null){this._prevGridData.push([af.call(this._xaxis,ak[ah][0]),ab.call(this._yaxis,ak[ah][1])])}else{if(ak[ah]!=null&&ak[ah][0]==null){this._prevGridData.push([null,ab.call(this._yaxis,ak[ah][1])])}else{if(ak[ah]!=null&&ak[ah][0]!=null&&ak[ah][1]==null){this._prevGridData.push([af.call(this._xaxis,ak[ah][0]),null])}}}}if(ac){this.renderer.smooth=false;if(this._type==="line"){ae.show=false}}if(this._type==="line"&&ae.show){for(var ah=0,ad=ae.hiData.length;ah<ad;ah++){this.renderer._hiBandGridData.push([af.call(this._xaxis,ae.hiData[ah][0]),ab.call(this._yaxis,ae.hiData[ah][1])])}for(var ah=0,ad=ae.lowData.length;ah<ad;ah++){this.renderer._lowBandGridData.push([af.call(this._xaxis,ae.lowData[ah][0]),ab.call(this._yaxis,ae.lowData[ah][1])])}}if(this._type==="line"&&this.renderer.smooth&&this.gridData.length>2){var ai;if(this.renderer.constrainSmoothing){ai=F.call(this,this.gridData);this.renderer._smoothedData=ai[0];this.renderer._smoothedPlotData=ai[1];if(ae.show){ai=F.call(this,this.renderer._hiBandGridData);this.renderer._hiBandSmoothedData=ai[0];ai=F.call(this,this.renderer._lowBandGridData);this.renderer._lowBandSmoothedData=ai[0]}ai=null}else{ai=B.call(this,this.gridData);this.renderer._smoothedData=ai[0];this.renderer._smoothedPlotData=ai[1];if(ae.show){ai=B.call(this,this.renderer._hiBandGridData);this.renderer._hiBandSmoothedData=ai[0];ai=B.call(this,this.renderer._lowBandGridData);this.renderer._lowBandSmoothedData=ai[0]}ai=null}}};H.jqplot.LineRenderer.prototype.makeGridData=function(ai,ak){var ag=this._xaxis.series_u2p;var ab=this._yaxis.series_u2p;var al=[];var ad=[];this.renderer._smoothedData=[];this.renderer._smoothedPlotData=[];this.renderer._hiBandGridData=[];this.renderer._lowBandGridData=[];this.renderer._hiBandSmoothedData=[];this.renderer._lowBandSmoothedData=[];var af=this.renderer.bands;var ac=false;for(var ah=0;ah<ai.length;ah++){if(ai[ah][0]!=null&&ai[ah][1]!=null){al.push([ag.call(this._xaxis,ai[ah][0]),ab.call(this._yaxis,ai[ah][1])])}else{if(ai[ah][0]==null){ac=true;al.push([null,ab.call(this._yaxis,ai[ah][1])])}else{if(ai[ah][1]==null){ac=true;al.push([ag.call(this._xaxis,ai[ah][0]),null])}}}}if(ac){this.renderer.smooth=false;if(this._type==="line"){af.show=false}}if(this._type==="line"&&af.show){for(var ah=0,ae=af.hiData.length;ah<ae;ah++){this.renderer._hiBandGridData.push([ag.call(this._xaxis,af.hiData[ah][0]),ab.call(this._yaxis,af.hiData[ah][1])])}for(var ah=0,ae=af.lowData.length;ah<ae;ah++){this.renderer._lowBandGridData.push([ag.call(this._xaxis,af.lowData[ah][0]),ab.call(this._yaxis,af.lowData[ah][1])])}}if(this._type==="line"&&this.renderer.smooth&&al.length>2){var aj;if(this.renderer.constrainSmoothing){aj=F.call(this,al);this.renderer._smoothedData=aj[0];this.renderer._smoothedPlotData=aj[1];if(af.show){aj=F.call(this,this.renderer._hiBandGridData);this.renderer._hiBandSmoothedData=aj[0];aj=F.call(this,this.renderer._lowBandGridData);this.renderer._lowBandSmoothedData=aj[0]}aj=null}else{aj=B.call(this,al);this.renderer._smoothedData=aj[0];this.renderer._smoothedPlotData=aj[1];if(af.show){aj=B.call(this,this.renderer._hiBandGridData);this.renderer._hiBandSmoothedData=aj[0];aj=B.call(this,this.renderer._lowBandGridData);this.renderer._lowBandSmoothedData=aj[0]}aj=null}}return al};H.jqplot.LineRenderer.prototype.draw=function(aq,aC,ac,av){var aw;var ak=H.extend(true,{},ac);var ae=(ak.shadow!=r)?ak.shadow:this.shadow;var aD=(ak.showLine!=r)?ak.showLine:this.showLine;var au=(ak.fill!=r)?ak.fill:this.fill;var ab=(ak.fillAndStroke!=r)?ak.fillAndStroke:this.fillAndStroke;var al,ar,ao,ay;aq.save();if(aC.length){if(aD){if(au){if(this.fillToZero){var az=this.negativeColor;if(!this.useNegativeColors){az=ak.fillStyle}var ai=false;var aj=ak.fillStyle;if(ab){var aB=aC.slice(0)}if(this.index==0||!this._stack){var ap=[];var aF=(this.renderer.smooth)?this.renderer._smoothedPlotData:this._plotData;this._areaPoints=[];var aA=this._yaxis.series_u2p(this.fillToValue);var ad=this._xaxis.series_u2p(this.fillToValue);ak.closePath=true;if(this.fillAxis=="y"){ap.push([aC[0][0],aA]);this._areaPoints.push([aC[0][0],aA]);for(var aw=0;aw<aC.length-1;aw++){ap.push(aC[aw]);this._areaPoints.push(aC[aw]);if(aF[aw][1]*aF[aw+1][1]<0){if(aF[aw][1]<0){ai=true;ak.fillStyle=az}else{ai=false;ak.fillStyle=aj}var ah=aC[aw][0]+(aC[aw+1][0]-aC[aw][0])*(aA-aC[aw][1])/(aC[aw+1][1]-aC[aw][1]);ap.push([ah,aA]);this._areaPoints.push([ah,aA]);if(ae){this.renderer.shadowRenderer.draw(aq,ap,ak)}this.renderer.shapeRenderer.draw(aq,ap,ak);ap=[[ah,aA]]}}if(aF[aC.length-1][1]<0){ai=true;ak.fillStyle=az}else{ai=false;ak.fillStyle=aj}ap.push(aC[aC.length-1]);this._areaPoints.push(aC[aC.length-1]);ap.push([aC[aC.length-1][0],aA]);this._areaPoints.push([aC[aC.length-1][0],aA])}if(ae){this.renderer.shadowRenderer.draw(aq,ap,ak)}this.renderer.shapeRenderer.draw(aq,ap,ak)}else{var an=this._prevGridData;for(var aw=an.length;aw>0;aw--){aC.push(an[aw-1])}if(ae){this.renderer.shadowRenderer.draw(aq,aC,ak)}this._areaPoints=aC;this.renderer.shapeRenderer.draw(aq,aC,ak)}}else{if(ab){var aB=aC.slice(0)}if(this.index==0||!this._stack){var af=aq.canvas.height;aC.unshift([aC[0][0],af]);var ax=aC.length;aC.push([aC[ax-1][0],af])}else{var an=this._prevGridData;for(var aw=an.length;aw>0;aw--){aC.push(an[aw-1])}}this._areaPoints=aC;if(ae){this.renderer.shadowRenderer.draw(aq,aC,ak)}this.renderer.shapeRenderer.draw(aq,aC,ak)}if(ab){var at=H.extend(true,{},ak,{fill:false,closePath:false});this.renderer.shapeRenderer.draw(aq,aB,at);if(this.markerRenderer.show){if(this.renderer.smooth){aB=this.gridData}for(aw=0;aw<aB.length;aw++){this.markerRenderer.draw(aB[aw][0],aB[aw][1],aq,ak.markerOptions)}}}}else{if(this.renderer.bands.show){var ag;var aE=H.extend(true,{},ak);if(this.renderer.bands.showLines){ag=(this.renderer.smooth)?this.renderer._hiBandSmoothedData:this.renderer._hiBandGridData;this.renderer.shapeRenderer.draw(aq,ag,ak);ag=(this.renderer.smooth)?this.renderer._lowBandSmoothedData:this.renderer._lowBandGridData;this.renderer.shapeRenderer.draw(aq,ag,aE)}if(this.renderer.bands.fill){if(this.renderer.smooth){ag=this.renderer._hiBandSmoothedData.concat(this.renderer._lowBandSmoothedData.reverse())}else{ag=this.renderer._hiBandGridData.concat(this.renderer._lowBandGridData.reverse())}this._areaPoints=ag;aE.closePath=true;aE.fill=true;aE.fillStyle=this.renderer.bands.fillColor;this.renderer.shapeRenderer.draw(aq,ag,aE)}}if(ae){this.renderer.shadowRenderer.draw(aq,aC,ak)}this.renderer.shapeRenderer.draw(aq,aC,ak)}}var al=ao=ar=ay=null;for(aw=0;aw<this._areaPoints.length;aw++){var am=this._areaPoints[aw];if(al>am[0]||al==null){al=am[0]}if(ay<am[1]||ay==null){ay=am[1]}if(ao<am[0]||ao==null){ao=am[0]}if(ar>am[1]||ar==null){ar=am[1]}}if(this.type==="line"&&this.renderer.bands.show){ay=this._yaxis.series_u2p(this.renderer.bands._min);ar=this._yaxis.series_u2p(this.renderer.bands._max)}this._boundingBox=[[al,ay],[ao,ar]];if(this.markerRenderer.show&&!au){if(this.renderer.smooth){aC=this.gridData}for(aw=0;aw<aC.length;aw++){if(aC[aw][0]!=null&&aC[aw][1]!=null){this.markerRenderer.draw(aC[aw][0],aC[aw][1],aq,ak.markerOptions)}}}}aq.restore()};H.jqplot.LineRenderer.prototype.drawShadow=function(ab,ad,ac){};function v(ae,ad,ab){for(var ac=0;ac<this.series.length;ac++){if(this.series[ac].renderer.constructor==H.jqplot.LineRenderer){if(this.series[ac].highlightMouseOver){this.series[ac].highlightMouseDown=false}}}}function Z(){if(this.plugins.lineRenderer&&this.plugins.lineRenderer.highlightCanvas){this.plugins.lineRenderer.highlightCanvas.resetCanvas();this.plugins.lineRenderer.highlightCanvas=null}this.plugins.lineRenderer.highlightedSeriesIndex=null;this.plugins.lineRenderer.highlightCanvas=new H.jqplot.GenericCanvas();this.eventCanvas._elem.before(this.plugins.lineRenderer.highlightCanvas.createElement(this._gridPadding,"jqplot-lineRenderer-highlight-canvas",this._plotDimensions,this));this.plugins.lineRenderer.highlightCanvas.setContext();this.eventCanvas._elem.bind("mouseleave",{plot:this},function(ab){V(ab.data.plot)})}function X(ah,ag,ae,ad){var ac=ah.series[ag];var ab=ah.plugins.lineRenderer.highlightCanvas;ab._ctx.clearRect(0,0,ab._ctx.canvas.width,ab._ctx.canvas.height);ac._highlightedPoint=ae;ah.plugins.lineRenderer.highlightedSeriesIndex=ag;var af={fillStyle:ac.highlightColor};if(ac.type==="line"&&ac.renderer.bands.show){af.fill=true;af.closePath=true}ac.renderer.shapeRenderer.draw(ab._ctx,ad,af);ab=null}function V(ad){var ab=ad.plugins.lineRenderer.highlightCanvas;ab._ctx.clearRect(0,0,ab._ctx.canvas.width,ab._ctx.canvas.height);for(var ac=0;ac<ad.series.length;ac++){ad.series[ac]._highlightedPoint=null}ad.plugins.lineRenderer.highlightedSeriesIndex=null;ad.target.trigger("jqplotDataUnhighlight");ab=null}function g(af,ae,ai,ah,ag){if(ah){var ad=[ah.seriesIndex,ah.pointIndex,ah.data];var ac=jQuery.Event("jqplotDataMouseOver");ac.pageX=af.pageX;ac.pageY=af.pageY;ag.target.trigger(ac,ad);if(ag.series[ad[0]].highlightMouseOver&&!(ad[0]==ag.plugins.lineRenderer.highlightedSeriesIndex)){var ab=jQuery.Event("jqplotDataHighlight");ab.pageX=af.pageX;ab.pageY=af.pageY;ag.target.trigger(ab,ad);X(ag,ah.seriesIndex,ah.pointIndex,ah.points)}}else{if(ah==null){V(ag)}}}function d(ae,ad,ah,ag,af){if(ag){var ac=[ag.seriesIndex,ag.pointIndex,ag.data];if(af.series[ac[0]].highlightMouseDown&&!(ac[0]==af.plugins.lineRenderer.highlightedSeriesIndex)){var ab=jQuery.Event("jqplotDataHighlight");ab.pageX=ae.pageX;ab.pageY=ae.pageY;af.target.trigger(ab,ac);X(af,ag.seriesIndex,ag.pointIndex,ag.points)}}else{if(ag==null){V(af)}}}function Y(ad,ac,ag,af,ae){var ab=ae.plugins.lineRenderer.highlightedSeriesIndex;if(ab!=null&&ae.series[ab].highlightMouseDown){V(ae)}}function f(ae,ad,ah,ag,af){if(ag){var ac=[ag.seriesIndex,ag.pointIndex,ag.data];var ab=jQuery.Event("jqplotDataClick");ab.pageX=ae.pageX;ab.pageY=ae.pageY;af.target.trigger(ab,ac)}}function p(af,ae,ai,ah,ag){if(ah){var ad=[ah.seriesIndex,ah.pointIndex,ah.data];var ab=ag.plugins.lineRenderer.highlightedSeriesIndex;if(ab!=null&&ag.series[ab].highlightMouseDown){V(ag)}var ac=jQuery.Event("jqplotDataRightClick");ac.pageX=af.pageX;ac.pageY=af.pageY;ag.target.trigger(ac,ad)}}H.jqplot.LinearAxisRenderer=function(){};H.jqplot.LinearAxisRenderer.prototype.init=function(ab){this.breakPoints=null;this.breakTickLabel="&asymp;";this.drawBaseline=true;this.baselineWidth=null;this.baselineColor=null;this.forceTickAt0=false;this.forceTickAt100=false;this.tickInset=0;this.minorTicks=0;this.alignTicks=false;this._autoFormatString="";this._overrideFormatString=false;this._scalefact=1;H.extend(true,this,ab);if(this.breakPoints){if(!H.isArray(this.breakPoints)){this.breakPoints=null}else{if(this.breakPoints.length<2||this.breakPoints[1]<=this.breakPoints[0]){this.breakPoints=null}}}if(this.numberTicks!=null&&this.numberTicks<2){this.numberTicks=2}this.resetDataBounds()};H.jqplot.LinearAxisRenderer.prototype.draw=function(ab,ai){if(this.show){this.renderer.createTicks.call(this,ai);var ah=0;var ac;if(this._elem){this._elem.emptyForce();this._elem=null}this._elem=H(document.createElement("div"));this._elem.addClass("jqplot-axis jqplot-"+this.name);this._elem.css("position","absolute");if(this.name=="xaxis"||this.name=="x2axis"){this._elem.width(this._plotDimensions.width)}else{this._elem.height(this._plotDimensions.height)}this.labelOptions.axis=this.name;this._label=new this.labelRenderer(this.labelOptions);if(this._label.show){var ag=this._label.draw(ab,ai);ag.appendTo(this._elem);ag=null}var af=this._ticks;var ae;for(var ad=0;ad<af.length;ad++){ae=af[ad];if(ae.show&&ae.showLabel&&(!ae.isMinorTick||this.showMinorTicks)){this._elem.append(ae.draw(ab,ai))}}ae=null;af=null}return this._elem};H.jqplot.LinearAxisRenderer.prototype.reset=function(){this.min=this._options.min;this.max=this._options.max;this.tickInterval=this._options.tickInterval;this.numberTicks=this._options.numberTicks;this._autoFormatString="";if(this._overrideFormatString&&this.tickOptions&&this.tickOptions.formatString){this.tickOptions.formatString=""}};H.jqplot.LinearAxisRenderer.prototype.set=function(){var ai=0;var ad;var ac=0;var ah=0;var ab=(this._label==null)?false:this._label.show;if(this.show){var ag=this._ticks;var af;for(var ae=0;ae<ag.length;ae++){af=ag[ae];if(!af._breakTick&&af.show&&af.showLabel&&(!af.isMinorTick||this.showMinorTicks)){if(this.name=="xaxis"||this.name=="x2axis"){ad=af._elem.outerHeight(true)}else{ad=af._elem.outerWidth(true)}if(ad>ai){ai=ad}}}af=null;ag=null;if(ab){ac=this._label._elem.outerWidth(true);ah=this._label._elem.outerHeight(true)}if(this.name=="xaxis"){ai=ai+ah;this._elem.css({height:ai+"px",left:"0px",bottom:"0px"})}else{if(this.name=="x2axis"){ai=ai+ah;this._elem.css({height:ai+"px",left:"0px",top:"0px"})}else{if(this.name=="yaxis"){ai=ai+ac;this._elem.css({width:ai+"px",left:"0px",top:"0px"});if(ab&&this._label.constructor==H.jqplot.AxisLabelRenderer){this._label._elem.css("width",ac+"px")}}else{ai=ai+ac;this._elem.css({width:ai+"px",right:"0px",top:"0px"});if(ab&&this._label.constructor==H.jqplot.AxisLabelRenderer){this._label._elem.css("width",ac+"px")}}}}}};H.jqplot.LinearAxisRenderer.prototype.createTicks=function(ad){var aM=this._ticks;var aD=this.ticks;var at=this.name;var av=this._dataBounds;var ab=(this.name.charAt(0)==="x")?this._plotDimensions.width:this._plotDimensions.height;var ah;var aY,aB;var aj,ai;var aW,aT;var aA=this.min;var aX=this.max;var aP=this.numberTicks;var a2=this.tickInterval;var ag=30;this._scalefact=(Math.max(ab,ag+1)-ag)/300;if(aD.length){for(aT=0;aT<aD.length;aT++){var aH=aD[aT];var aN=new this.tickRenderer(this.tickOptions);if(H.isArray(aH)){aN.value=aH[0];if(this.breakPoints){if(aH[0]==this.breakPoints[0]){aN.label=this.breakTickLabel;aN._breakTick=true;aN.showGridline=false;aN.showMark=false}else{if(aH[0]>this.breakPoints[0]&&aH[0]<=this.breakPoints[1]){aN.show=false;aN.showGridline=false;aN.label=aH[1]}else{aN.label=aH[1]}}}else{aN.label=aH[1]}aN.setTick(aH[0],this.name);this._ticks.push(aN)}else{if(H.isPlainObject(aH)){H.extend(true,aN,aH);aN.axis=this.name;this._ticks.push(aN)}else{aN.value=aH;if(this.breakPoints){if(aH==this.breakPoints[0]){aN.label=this.breakTickLabel;aN._breakTick=true;aN.showGridline=false;aN.showMark=false}else{if(aH>this.breakPoints[0]&&aH<=this.breakPoints[1]){aN.show=false;aN.showGridline=false}}}aN.setTick(aH,this.name);this._ticks.push(aN)}}}this.numberTicks=aD.length;this.min=this._ticks[0].value;this.max=this._ticks[this.numberTicks-1].value;this.tickInterval=(this.max-this.min)/(this.numberTicks-1)}else{if(at=="xaxis"||at=="x2axis"){ab=this._plotDimensions.width}else{ab=this._plotDimensions.height}var aq=this.numberTicks;if(this.alignTicks){if(this.name==="x2axis"&&ad.axes.xaxis.show){aq=ad.axes.xaxis.numberTicks}else{if(this.name.charAt(0)==="y"&&this.name!=="yaxis"&&this.name!=="yMidAxis"&&ad.axes.yaxis.show){aq=ad.axes.yaxis.numberTicks}}}aY=((this.min!=null)?this.min:av.min);aB=((this.max!=null)?this.max:av.max);var ao=aB-aY;var aL,ar;var am;if(this.tickOptions==null||!this.tickOptions.formatString){this._overrideFormatString=true}if(this.min==null&&this.max==null&&this.tickInterval==null&&!this.autoscale){if(this.forceTickAt0){if(aY>0){aY=0}if(aB<0){aB=0}}if(this.forceTickAt100){if(aY>100){aY=100}if(aB<100){aB=100}}var aI=H.jqplot.LinearTickGenerator(aY,aB,this._scalefact,aq);var ap=aY+ao*(this.padMin-1);var aJ=aB-ao*(this.padMax-1);if(aY<ap||aB>aJ){ap=aY-ao*(this.padMin-1);aJ=aB+ao*(this.padMax-1);aI=H.jqplot.LinearTickGenerator(ap,aJ,this._scalefact,aq)}this.min=aI[0];this.max=aI[1];this.numberTicks=aI[2];this._autoFormatString=aI[3];this.tickInterval=aI[4]}else{if(aY==aB){var ac=0.05;if(aY>0){ac=Math.max(Math.log(aY)/Math.LN10,0.05)}aY-=ac;aB+=ac}if(this.autoscale&&this.min==null&&this.max==null){var ae,af,al;var aw=false;var aG=false;var au={min:null,max:null,average:null,stddev:null};for(var aT=0;aT<this._series.length;aT++){var aO=this._series[aT];var ax=(aO.fillAxis=="x")?aO._xaxis.name:aO._yaxis.name;if(this.name==ax){var aK=aO._plotValues[aO.fillAxis];var az=aK[0];var aU=aK[0];for(var aS=1;aS<aK.length;aS++){if(aK[aS]<az){az=aK[aS]}else{if(aK[aS]>aU){aU=aK[aS]}}}var an=(aU-az)/aU;if(aO.renderer.constructor==H.jqplot.BarRenderer){if(az>=0&&(aO.fillToZero||an>0.1)){aw=true}else{aw=false;if(aO.fill&&aO.fillToZero&&az<0&&aU>0){aG=true}else{aG=false}}}else{if(aO.fill){if(az>=0&&(aO.fillToZero||an>0.1)){aw=true}else{if(az<0&&aU>0&&aO.fillToZero){aw=false;aG=true}else{aw=false;aG=false}}}else{if(az<0){aw=false}}}}}if(aw){this.numberTicks=2+Math.ceil((ab-(this.tickSpacing-1))/this.tickSpacing);this.min=0;aA=0;af=aB/(this.numberTicks-1);am=Math.pow(10,Math.abs(Math.floor(Math.log(af)/Math.LN10)));if(af/am==parseInt(af/am,10)){af+=am}this.tickInterval=Math.ceil(af/am)*am;this.max=this.tickInterval*(this.numberTicks-1)}else{if(aG){this.numberTicks=2+Math.ceil((ab-(this.tickSpacing-1))/this.tickSpacing);var aC=Math.ceil(Math.abs(aY)/ao*(this.numberTicks-1));var a1=this.numberTicks-1-aC;af=Math.max(Math.abs(aY/aC),Math.abs(aB/a1));am=Math.pow(10,Math.abs(Math.floor(Math.log(af)/Math.LN10)));this.tickInterval=Math.ceil(af/am)*am;this.max=this.tickInterval*a1;this.min=-this.tickInterval*aC}else{if(this.numberTicks==null){if(this.tickInterval){this.numberTicks=3+Math.ceil(ao/this.tickInterval)}else{this.numberTicks=2+Math.ceil((ab-(this.tickSpacing-1))/this.tickSpacing)}}if(this.tickInterval==null){af=ao/(this.numberTicks-1);if(af<1){am=Math.pow(10,Math.abs(Math.floor(Math.log(af)/Math.LN10)))}else{am=1}this.tickInterval=Math.ceil(af*am*this.pad)/am}else{am=1/this.tickInterval}ae=this.tickInterval*(this.numberTicks-1);al=(ae-ao)/2;if(this.min==null){this.min=Math.floor(am*(aY-al))/am}if(this.max==null){this.max=this.min+ae}}}var ay=H.jqplot.getSignificantFigures(this.tickInterval);var aF;if(ay.digitsLeft>=ay.significantDigits){aF="%d"}else{var am=Math.max(0,5-ay.digitsLeft);am=Math.min(am,ay.digitsRight);aF="%."+am+"f"}this._autoFormatString=aF}else{aL=(this.min!=null)?this.min:aY-ao*(this.padMin-1);ar=(this.max!=null)?this.max:aB+ao*(this.padMax-1);ao=ar-aL;if(this.numberTicks==null){if(this.tickInterval!=null){this.numberTicks=Math.ceil((ar-aL)/this.tickInterval)+1}else{if(ab>100){this.numberTicks=parseInt(3+(ab-100)/75,10)}else{this.numberTicks=2}}}if(this.tickInterval==null){this.tickInterval=ao/(this.numberTicks-1)}if(this.max==null){ar=aL+this.tickInterval*(this.numberTicks-1)}if(this.min==null){aL=ar-this.tickInterval*(this.numberTicks-1)}var ay=H.jqplot.getSignificantFigures(this.tickInterval);var aF;if(ay.digitsLeft>=ay.significantDigits){aF="%d"}else{var am=Math.max(0,5-ay.digitsLeft);am=Math.min(am,ay.digitsRight);aF="%."+am+"f"}this._autoFormatString=aF;this.min=aL;this.max=ar}if(this.renderer.constructor==H.jqplot.LinearAxisRenderer&&this._autoFormatString==""){ao=this.max-this.min;var aZ=new this.tickRenderer(this.tickOptions);var aE=aZ.formatString||H.jqplot.config.defaultTickFormatString;var aE=aE.match(H.jqplot.sprintf.regex)[0];var aV=0;if(aE){if(aE.search(/[fFeEgGpP]/)>-1){var aR=aE.match(/\%\.(\d{0,})?[eEfFgGpP]/);if(aR){aV=parseInt(aR[1],10)}else{aV=6}}else{if(aE.search(/[di]/)>-1){aV=0}}var ak=Math.pow(10,-aV);if(this.tickInterval<ak){if(aP==null&&a2==null){this.tickInterval=ak;if(aX==null&&aA==null){this.min=Math.floor(this._dataBounds.min/ak)*ak;if(this.min==this._dataBounds.min){this.min=this._dataBounds.min-this.tickInterval}this.max=Math.ceil(this._dataBounds.max/ak)*ak;if(this.max==this._dataBounds.max){this.max=this._dataBounds.max+this.tickInterval}var aQ=(this.max-this.min)/this.tickInterval;aQ=aQ.toFixed(11);aQ=Math.ceil(aQ);this.numberTicks=aQ+1}else{if(aX==null){var aQ=(this._dataBounds.max-this.min)/this.tickInterval;aQ=aQ.toFixed(11);this.numberTicks=Math.ceil(aQ)+2;this.max=this.min+this.tickInterval*(this.numberTicks-1)}else{if(aA==null){var aQ=(this.max-this._dataBounds.min)/this.tickInterval;aQ=aQ.toFixed(11);this.numberTicks=Math.ceil(aQ)+2;this.min=this.max-this.tickInterval*(this.numberTicks-1)}else{this.numberTicks=Math.ceil((aX-aA)/this.tickInterval)+1;this.min=Math.floor(aA*Math.pow(10,aV))/Math.pow(10,aV);this.max=Math.ceil(aX*Math.pow(10,aV))/Math.pow(10,aV);this.numberTicks=Math.ceil((this.max-this.min)/this.tickInterval)+1}}}}}}}}if(this._overrideFormatString&&this._autoFormatString!=""){this.tickOptions=this.tickOptions||{};this.tickOptions.formatString=this._autoFormatString}var aN,a0;for(var aT=0;aT<this.numberTicks;aT++){aW=this.min+aT*this.tickInterval;aN=new this.tickRenderer(this.tickOptions);aN.setTick(aW,this.name);this._ticks.push(aN);if(aT<this.numberTicks-1){for(var aS=0;aS<this.minorTicks;aS++){aW+=this.tickInterval/(this.minorTicks+1);a0=H.extend(true,{},this.tickOptions,{name:this.name,value:aW,label:"",isMinorTick:true});aN=new this.tickRenderer(a0);this._ticks.push(aN)}}aN=null}}if(this.tickInset){this.min=this.min-this.tickInset*this.tickInterval;this.max=this.max+this.tickInset*this.tickInterval}aM=null};H.jqplot.LinearAxisRenderer.prototype.resetTickValues=function(ad){if(H.isArray(ad)&&ad.length==this._ticks.length){var ac;for(var ab=0;ab<ad.length;ab++){ac=this._ticks[ab];ac.value=ad[ab];ac.label=ac.formatter(ac.formatString,ad[ab]);ac.label=ac.prefix+ac.label;ac._elem.html(ac.label)}ac=null;this.min=H.jqplot.arrayMin(ad);this.max=H.jqplot.arrayMax(ad);this.pack()}};H.jqplot.LinearAxisRenderer.prototype.pack=function(ad,ac){ad=ad||{};ac=ac||this._offsets;var ar=this._ticks;var an=this.max;var am=this.min;var ai=ac.max;var ag=ac.min;var ak=(this._label==null)?false:this._label.show;for(var al in ad){this._elem.css(al,ad[al])}this._offsets=ac;var ae=ai-ag;var af=an-am;if(this.breakPoints){af=af-this.breakPoints[1]+this.breakPoints[0];this.p2u=function(au){return(au-ag)*af/ae+am};this.u2p=function(au){if(au>this.breakPoints[0]&&au<this.breakPoints[1]){au=this.breakPoints[0]}if(au<=this.breakPoints[0]){return(au-am)*ae/af+ag}else{return(au-this.breakPoints[1]+this.breakPoints[0]-am)*ae/af+ag}};if(this.name.charAt(0)=="x"){this.series_u2p=function(au){if(au>this.breakPoints[0]&&au<this.breakPoints[1]){au=this.breakPoints[0]}if(au<=this.breakPoints[0]){return(au-am)*ae/af}else{return(au-this.breakPoints[1]+this.breakPoints[0]-am)*ae/af}};this.series_p2u=function(au){return au*af/ae+am}}else{this.series_u2p=function(au){if(au>this.breakPoints[0]&&au<this.breakPoints[1]){au=this.breakPoints[0]}if(au>=this.breakPoints[1]){return(au-an)*ae/af}else{return(au+this.breakPoints[1]-this.breakPoints[0]-an)*ae/af}};this.series_p2u=function(au){return au*af/ae+an}}}else{this.p2u=function(au){return(au-ag)*af/ae+am};this.u2p=function(au){return(au-am)*ae/af+ag};if(this.name=="xaxis"||this.name=="x2axis"){this.series_u2p=function(au){return(au-am)*ae/af};this.series_p2u=function(au){return au*af/ae+am}}else{this.series_u2p=function(au){return(au-an)*ae/af};this.series_p2u=function(au){return au*af/ae+an}}}if(this.show){if(this.name=="xaxis"||this.name=="x2axis"){for(var ao=0;ao<ar.length;ao++){var aj=ar[ao];if(aj.show&&aj.showLabel){var ab;if(aj.constructor==H.jqplot.CanvasAxisTickRenderer&&aj.angle){var aq=(this.name=="xaxis")?1:-1;switch(aj.labelPosition){case"auto":if(aq*aj.angle<0){ab=-aj.getWidth()+aj._textRenderer.height*Math.sin(-aj._textRenderer.angle)/2}else{ab=-aj._textRenderer.height*Math.sin(aj._textRenderer.angle)/2}break;case"end":ab=-aj.getWidth()+aj._textRenderer.height*Math.sin(-aj._textRenderer.angle)/2;break;case"start":ab=-aj._textRenderer.height*Math.sin(aj._textRenderer.angle)/2;break;case"middle":ab=-aj.getWidth()/2+aj._textRenderer.height*Math.sin(-aj._textRenderer.angle)/2;break;default:ab=-aj.getWidth()/2+aj._textRenderer.height*Math.sin(-aj._textRenderer.angle)/2;break}}else{ab=-aj.getWidth()/2}var at=this.u2p(aj.value)+ab+"px";aj._elem.css("left",at);aj.pack()}}if(ak){var ah=this._label._elem.outerWidth(true);this._label._elem.css("left",ag+ae/2-ah/2+"px");if(this.name=="xaxis"){this._label._elem.css("bottom","0px")}else{this._label._elem.css("top","0px")}this._label.pack()}}else{for(var ao=0;ao<ar.length;ao++){var aj=ar[ao];if(aj.show&&aj.showLabel){var ab;if(aj.constructor==H.jqplot.CanvasAxisTickRenderer&&aj.angle){var aq=(this.name=="yaxis")?1:-1;switch(aj.labelPosition){case"auto":case"end":if(aq*aj.angle<0){ab=-aj._textRenderer.height*Math.cos(-aj._textRenderer.angle)/2}else{ab=-aj.getHeight()+aj._textRenderer.height*Math.cos(aj._textRenderer.angle)/2}break;case"start":if(aj.angle>0){ab=-aj._textRenderer.height*Math.cos(-aj._textRenderer.angle)/2}else{ab=-aj.getHeight()+aj._textRenderer.height*Math.cos(aj._textRenderer.angle)/2}break;case"middle":ab=-aj.getHeight()/2;break;default:ab=-aj.getHeight()/2;break}}else{ab=-aj.getHeight()/2}var at=this.u2p(aj.value)+ab+"px";aj._elem.css("top",at);aj.pack()}}if(ak){var ap=this._label._elem.outerHeight(true);this._label._elem.css("top",ai-ae/2-ap/2+"px");if(this.name=="yaxis"){this._label._elem.css("left","0px")}else{this._label._elem.css("right","0px")}this._label.pack()}}}ar=null};function h(ac){var ab;ac=Math.abs(ac);if(ac>=10){ab="%d"}else{if(ac>1){if(ac===parseInt(ac,10)){ab="%d"}else{ab="%.1f"}}else{var ad=-Math.floor(Math.log(ac)/Math.LN10);ab="%."+ad+"f"}}return ab}var a=[0.1,0.2,0.3,0.4,0.5,0.8,1,2,3,4,5];var b=function(ac){var ab=a.indexOf(ac);if(ab>0){return a[ab-1]}else{return a[a.length-1]/100}};var i=function(ac){var ab=a.indexOf(ac);if(ab<a.length-1){return a[ab+1]}else{return a[0]*100}};function c(af,an,am){var ak=Math.floor(am/2);var ac=Math.ceil(am*1.5);var ae=Number.MAX_VALUE;var ab=(an-af);var aq;var aj;var al;var ap;var ah;var ar=H.jqplot.getSignificantFigures;var ai;var ao;for(var ag=0,ad=ac-ak+1;ag<ad;ag++){ai=ak+ag;aq=ab/(ai-1);aj=ar(aq);aq=Math.abs(am-ai)+aj.digitsRight;if(aq<ae){ae=aq;al=ai;ao=aj.digitsRight}else{if(aq===ae){if(aj.digitsRight<ao){al=ai;ao=aj.digitsRight}}}}ap=Math.max(ao,Math.max(ar(af).digitsRight,ar(an).digitsRight));if(ap===0){ah="%d"}else{ah="%."+ap+"f"}aq=ab/(al-1);return[af,an,al,ah,aq]}function S(ac,af){af=af||7;var ae=ac/(af-1);var ad=Math.pow(10,Math.floor(Math.log(ae)/Math.LN10));var ag=ae/ad;var ab;if(ad<1){if(ag>5){ab=10*ad}else{if(ag>2){ab=5*ad}else{if(ag>1){ab=2*ad}else{ab=ad}}}}else{if(ag>5){ab=10*ad}else{if(ag>4){ab=5*ad}else{if(ag>3){ab=4*ad}else{if(ag>2){ab=3*ad}else{if(ag>1){ab=2*ad}else{ab=ad}}}}}}return ab}function M(ac,ab){ab=ab||1;var ae=Math.floor(Math.log(ac)/Math.LN10);var ag=Math.pow(10,ae);var af=ac/ag;var ad;af=af/ab;if(af<=0.38){ad=0.1}else{if(af<=1.6){ad=0.2}else{if(af<=4){ad=0.5}else{if(af<=8){ad=1}else{if(af<=16){ad=2}else{ad=5}}}}}return ad*ag}function t(ad,ac){var af=Math.floor(Math.log(ad)/Math.LN10);var ah=Math.pow(10,af);var ag=ad/ah;var ab;var ae;ag=ag/ac;if(ag<=0.38){ae=0.1}else{if(ag<=1.6){ae=0.2}else{if(ag<=4){ae=0.5}else{if(ag<=8){ae=1}else{if(ag<=16){ae=2}else{ae=5}}}}}ab=ae*ah;return[ab,ae,ah]}H.jqplot.LinearTickGenerator=function(ag,ah,ad,ae){if(ag===ah){ah=(ah)?0:1}ad=ad||1;if(ah<ag){var ai=ah;ah=ag;ag=ai}var ac=[];var aj=M(ah-ag,ad);if(ae==null){ac[0]=Math.floor(ag/aj)*aj;ac[1]=Math.ceil(ah/aj)*aj;ac[2]=Math.round((ac[1]-ac[0])/aj+1);ac[3]=h(aj);ac[4]=aj}else{var af=[];af[0]=Math.floor(ag/aj)*aj;af[1]=Math.ceil(ah/aj)*aj;af[2]=Math.round((af[1]-af[0])/aj+1);af[3]=h(aj);af[4]=aj;if(af[2]===ae){ac=af}else{var ab=S(af[1]-af[0],ae);ac[0]=af[0];ac[2]=ae;ac[4]=ab;ac[3]=h(ab);ac[1]=ac[0]+(ac[2]-1)*ac[4]}}return ac};H.jqplot.LinearTickGenerator.bestLinearInterval=M;H.jqplot.LinearTickGenerator.bestInterval=S;H.jqplot.LinearTickGenerator.bestLinearComponents=t;H.jqplot.LinearTickGenerator.bestConstrainedInterval=c;H.jqplot.MarkerRenderer=function(ab){this.show=true;this.style="filledCircle";this.lineWidth=2;this.size=9;this.color="#666666";this.shadow=true;this.shadowAngle=45;this.shadowOffset=1;this.shadowDepth=3;this.shadowAlpha="0.07";this.shadowRenderer=new H.jqplot.ShadowRenderer();this.shapeRenderer=new H.jqplot.ShapeRenderer();H.extend(true,this,ab)};H.jqplot.MarkerRenderer.prototype.init=function(ab){H.extend(true,this,ab);var ad={angle:this.shadowAngle,offset:this.shadowOffset,alpha:this.shadowAlpha,lineWidth:this.lineWidth,depth:this.shadowDepth,closePath:true};if(this.style.indexOf("filled")!=-1){ad.fill=true}if(this.style.indexOf("ircle")!=-1){ad.isarc=true;ad.closePath=false}this.shadowRenderer.init(ad);var ac={fill:false,isarc:false,strokeStyle:this.color,fillStyle:this.color,lineWidth:this.lineWidth,closePath:true};if(this.style.indexOf("filled")!=-1){ac.fill=true}if(this.style.indexOf("ircle")!=-1){ac.isarc=true;ac.closePath=false}this.shapeRenderer.init(ac)};H.jqplot.MarkerRenderer.prototype.drawDiamond=function(ad,ac,ag,af,ai){var ab=1.2;var aj=this.size/2/ab;var ah=this.size/2*ab;var ae=[[ad-aj,ac],[ad,ac+ah],[ad+aj,ac],[ad,ac-ah]];if(this.shadow){this.shadowRenderer.draw(ag,ae)}this.shapeRenderer.draw(ag,ae,ai)};H.jqplot.MarkerRenderer.prototype.drawPlus=function(ae,ad,ah,ag,ak){var ac=1;var al=this.size/2*ac;var ai=this.size/2*ac;var aj=[[ae,ad-ai],[ae,ad+ai]];var af=[[ae+al,ad],[ae-al,ad]];var ab=H.extend(true,{},this.options,{closePath:false});if(this.shadow){this.shadowRenderer.draw(ah,aj,{closePath:false});this.shadowRenderer.draw(ah,af,{closePath:false})}this.shapeRenderer.draw(ah,aj,ab);this.shapeRenderer.draw(ah,af,ab)};H.jqplot.MarkerRenderer.prototype.drawX=function(ae,ad,ah,ag,ak){var ac=1;var al=this.size/2*ac;var ai=this.size/2*ac;var ab=H.extend(true,{},this.options,{closePath:false});var aj=[[ae-al,ad-ai],[ae+al,ad+ai]];var af=[[ae-al,ad+ai],[ae+al,ad-ai]];if(this.shadow){this.shadowRenderer.draw(ah,aj,{closePath:false});this.shadowRenderer.draw(ah,af,{closePath:false})}this.shapeRenderer.draw(ah,aj,ab);this.shapeRenderer.draw(ah,af,ab)};H.jqplot.MarkerRenderer.prototype.drawDash=function(ad,ac,ag,af,ai){var ab=1;var aj=this.size/2*ab;var ah=this.size/2*ab;var ae=[[ad-aj,ac],[ad+aj,ac]];if(this.shadow){this.shadowRenderer.draw(ag,ae)}this.shapeRenderer.draw(ag,ae,ai)};H.jqplot.MarkerRenderer.prototype.drawLine=function(ag,af,ab,ae,ac){var ad=[ag,af];if(this.shadow){this.shadowRenderer.draw(ab,ad)}this.shapeRenderer.draw(ab,ad,ac)};H.jqplot.MarkerRenderer.prototype.drawSquare=function(ad,ac,ag,af,ai){var ab=1;var aj=this.size/2/ab;var ah=this.size/2*ab;var ae=[[ad-aj,ac-ah],[ad-aj,ac+ah],[ad+aj,ac+ah],[ad+aj,ac-ah]];if(this.shadow){this.shadowRenderer.draw(ag,ae)}this.shapeRenderer.draw(ag,ae,ai)};H.jqplot.MarkerRenderer.prototype.drawCircle=function(ac,ai,ae,ah,af){var ab=this.size/2;var ad=2*Math.PI;var ag=[ac,ai,ab,0,ad,true];if(this.shadow){this.shadowRenderer.draw(ae,ag)}this.shapeRenderer.draw(ae,ag,af)};H.jqplot.MarkerRenderer.prototype.draw=function(ab,ae,ac,ad){ad=ad||{};if(ad.show==null||ad.show!=false){if(ad.color&&!ad.fillStyle){ad.fillStyle=ad.color}if(ad.color&&!ad.strokeStyle){ad.strokeStyle=ad.color}switch(this.style){case"diamond":this.drawDiamond(ab,ae,ac,false,ad);break;case"filledDiamond":this.drawDiamond(ab,ae,ac,true,ad);break;case"circle":this.drawCircle(ab,ae,ac,false,ad);break;case"filledCircle":this.drawCircle(ab,ae,ac,true,ad);break;case"square":this.drawSquare(ab,ae,ac,false,ad);break;case"filledSquare":this.drawSquare(ab,ae,ac,true,ad);break;case"x":this.drawX(ab,ae,ac,true,ad);break;case"plus":this.drawPlus(ab,ae,ac,true,ad);break;case"dash":this.drawDash(ab,ae,ac,true,ad);break;case"line":this.drawLine(ab,ae,ac,false,ad);break;default:this.drawDiamond(ab,ae,ac,false,ad);break}}};H.jqplot.ShadowRenderer=function(ab){this.angle=45;this.offset=1;this.alpha=0.07;this.lineWidth=1.5;this.lineJoin="miter";this.lineCap="round";this.closePath=false;this.fill=false;this.depth=3;this.strokeStyle="rgba(0,0,0,0.1)";this.isarc=false;H.extend(true,this,ab)};H.jqplot.ShadowRenderer.prototype.init=function(ab){H.extend(true,this,ab)};H.jqplot.ShadowRenderer.prototype.draw=function(ao,am,aq){ao.save();var ab=(aq!=null)?aq:{};var an=(ab.fill!=null)?ab.fill:this.fill;var aj=(ab.fillRect!=null)?ab.fillRect:this.fillRect;var ai=(ab.closePath!=null)?ab.closePath:this.closePath;var af=(ab.offset!=null)?ab.offset:this.offset;var ad=(ab.alpha!=null)?ab.alpha:this.alpha;var ah=(ab.depth!=null)?ab.depth:this.depth;var ap=(ab.isarc!=null)?ab.isarc:this.isarc;var ak=(ab.linePattern!=null)?ab.linePattern:this.linePattern;ao.lineWidth=(ab.lineWidth!=null)?ab.lineWidth:this.lineWidth;ao.lineJoin=(ab.lineJoin!=null)?ab.lineJoin:this.lineJoin;ao.lineCap=(ab.lineCap!=null)?ab.lineCap:this.lineCap;ao.strokeStyle=ab.strokeStyle||this.strokeStyle||"rgba(0,0,0,"+ad+")";ao.fillStyle=ab.fillStyle||this.fillStyle||"rgba(0,0,0,"+ad+")";for(var ae=0;ae<ah;ae++){var al=H.jqplot.LinePattern(ao,ak);ao.translate(Math.cos(this.angle*Math.PI/180)*af,Math.sin(this.angle*Math.PI/180)*af);al.beginPath();if(ap){ao.arc(am[0],am[1],am[2],am[3],am[4],true)}else{if(aj){if(aj){ao.fillRect(am[0],am[1],am[2],am[3])}}else{if(am&&am.length){var ac=true;for(var ag=0;ag<am.length;ag++){if(am[ag][0]!=null&&am[ag][1]!=null){if(ac){al.moveTo(am[ag][0],am[ag][1]);ac=false}else{al.lineTo(am[ag][0],am[ag][1])}}else{ac=true}}}}}if(ai){al.closePath()}if(an){ao.fill()}else{ao.stroke()}}ao.restore()};H.jqplot.ShapeRenderer=function(ab){this.lineWidth=1.5;this.linePattern="solid";this.lineJoin="miter";this.lineCap="round";this.closePath=false;this.fill=false;this.isarc=false;this.fillRect=false;this.strokeRect=false;this.clearRect=false;this.strokeStyle="#999999";this.fillStyle="#999999";H.extend(true,this,ab)};H.jqplot.ShapeRenderer.prototype.init=function(ab){H.extend(true,this,ab)};H.jqplot.ShapeRenderer.prototype.draw=function(am,ak,ao){am.save();var ab=(ao!=null)?ao:{};var al=(ab.fill!=null)?ab.fill:this.fill;var ag=(ab.closePath!=null)?ab.closePath:this.closePath;var ah=(ab.fillRect!=null)?ab.fillRect:this.fillRect;var ae=(ab.strokeRect!=null)?ab.strokeRect:this.strokeRect;var ac=(ab.clearRect!=null)?ab.clearRect:this.clearRect;var an=(ab.isarc!=null)?ab.isarc:this.isarc;var ai=(ab.linePattern!=null)?ab.linePattern:this.linePattern;var aj=H.jqplot.LinePattern(am,ai);am.lineWidth=ab.lineWidth||this.lineWidth;am.lineJoin=ab.lineJoin||this.lineJoin;am.lineCap=ab.lineCap||this.lineCap;am.strokeStyle=(ab.strokeStyle||ab.color)||this.strokeStyle;am.fillStyle=ab.fillStyle||this.fillStyle;am.beginPath();if(an){am.arc(ak[0],ak[1],ak[2],ak[3],ak[4],true);if(ag){am.closePath()}if(al){am.fill()}else{am.stroke()}am.restore();return}else{if(ac){am.clearRect(ak[0],ak[1],ak[2],ak[3]);am.restore();return}else{if(ah||ae){if(ah){am.fillRect(ak[0],ak[1],ak[2],ak[3])}if(ae){am.strokeRect(ak[0],ak[1],ak[2],ak[3]);am.restore();return}}else{if(ak&&ak.length){var ad=true;for(var af=0;af<ak.length;af++){if(ak[af][0]!=null&&ak[af][1]!=null){if(ad){aj.moveTo(ak[af][0],ak[af][1]);ad=false}else{aj.lineTo(ak[af][0],ak[af][1])}}else{ad=true}}if(ag){aj.closePath()}if(al){am.fill()}else{am.stroke()}}}}}am.restore()};H.jqplot.TableLegendRenderer=function(){};H.jqplot.TableLegendRenderer.prototype.init=function(ab){H.extend(true,this,ab)};H.jqplot.TableLegendRenderer.prototype.addrow=function(ak,ae,ab,ai){var af=(ab)?this.rowSpacing+"px":"0px";var aj;var ad;var ac;var ah;var ag;ac=document.createElement("tr");aj=H(ac);aj.addClass("jqplot-table-legend");ac=null;if(ai){aj.prependTo(this._elem)}else{aj.appendTo(this._elem)}if(this.showSwatches){ad=H(document.createElement("td"));ad.addClass("jqplot-table-legend jqplot-table-legend-swatch");ad.css({textAlign:"center",paddingTop:af});ah=H(document.createElement("div"));ah.addClass("jqplot-table-legend-swatch-outline");ag=H(document.createElement("div"));ag.addClass("jqplot-table-legend-swatch");ag.css({backgroundColor:ae,borderColor:ae});aj.append(ad.append(ah.append(ag)))}if(this.showLabels){ad=H(document.createElement("td"));ad.addClass("jqplot-table-legend jqplot-table-legend-label");ad.css("paddingTop",af);aj.append(ad);if(this.escapeHtml){ad.text(ak)}else{ad.html(ak)}}ad=null;ah=null;ag=null;aj=null;ac=null};H.jqplot.TableLegendRenderer.prototype.draw=function(){if(this._elem){this._elem.emptyForce();this._elem=null}if(this.show){var ag=this._series;var ac=document.createElement("table");this._elem=H(ac);this._elem.addClass("jqplot-table-legend");var al={position:"absolute"};if(this.background){al.background=this.background}if(this.border){al.border=this.border}if(this.fontSize){al.fontSize=this.fontSize}if(this.fontFamily){al.fontFamily=this.fontFamily}if(this.textColor){al.textColor=this.textColor}if(this.marginTop!=null){al.marginTop=this.marginTop}if(this.marginBottom!=null){al.marginBottom=this.marginBottom}if(this.marginLeft!=null){al.marginLeft=this.marginLeft}if(this.marginRight!=null){al.marginRight=this.marginRight}var ab=false,ai=false,ak;for(var ah=0;ah<ag.length;ah++){ak=ag[ah];if(ak._stack||ak.renderer.constructor==H.jqplot.BezierCurveRenderer){ai=true}if(ak.show&&ak.showLabel){var af=this.labels[ah]||ak.label.toString();if(af){var ad=ak.color;if(ai&&ah<ag.length-1){ab=true}else{if(ai&&ah==ag.length-1){ab=false}}this.renderer.addrow.call(this,af,ad,ab,ai);ab=true}for(var ae=0;ae<H.jqplot.addLegendRowHooks.length;ae++){var aj=H.jqplot.addLegendRowHooks[ae].call(this,ak);if(aj){this.renderer.addrow.call(this,aj.label,aj.color,ab);ab=true}}af=null}}}return this._elem};H.jqplot.TableLegendRenderer.prototype.pack=function(ad){if(this.show){if(this.placement=="insideGrid"){switch(this.location){case"nw":var ac=ad.left;var ab=ad.top;this._elem.css("left",ac);this._elem.css("top",ab);break;case"n":var ac=(ad.left+(this._plotDimensions.width-ad.right))/2-this.getWidth()/2;var ab=ad.top;this._elem.css("left",ac);this._elem.css("top",ab);break;case"ne":var ac=ad.right;var ab=ad.top;this._elem.css({right:ac,top:ab});break;case"e":var ac=ad.right;var ab=(ad.top+(this._plotDimensions.height-ad.bottom))/2-this.getHeight()/2;this._elem.css({right:ac,top:ab});break;case"se":var ac=ad.right;var ab=ad.bottom;this._elem.css({right:ac,bottom:ab});break;case"s":var ac=(ad.left+(this._plotDimensions.width-ad.right))/2-this.getWidth()/2;var ab=ad.bottom;this._elem.css({left:ac,bottom:ab});break;case"sw":var ac=ad.left;var ab=ad.bottom;this._elem.css({left:ac,bottom:ab});break;case"w":var ac=ad.left;var ab=(ad.top+(this._plotDimensions.height-ad.bottom))/2-this.getHeight()/2;this._elem.css({left:ac,top:ab});break;default:var ac=ad.right;var ab=ad.bottom;this._elem.css({right:ac,bottom:ab});break}}else{if(this.placement=="outside"){switch(this.location){case"nw":var ac=this._plotDimensions.width-ad.left;var ab=ad.top;this._elem.css("right",ac);this._elem.css("top",ab);break;case"n":var ac=(ad.left+(this._plotDimensions.width-ad.right))/2-this.getWidth()/2;var ab=this._plotDimensions.height-ad.top;this._elem.css("left",ac);this._elem.css("bottom",ab);break;case"ne":var ac=this._plotDimensions.width-ad.right;var ab=ad.top;this._elem.css({left:ac,top:ab});break;case"e":var ac=this._plotDimensions.width-ad.right;var ab=(ad.top+(this._plotDimensions.height-ad.bottom))/2-this.getHeight()/2;this._elem.css({left:ac,top:ab});break;case"se":var ac=this._plotDimensions.width-ad.right;var ab=ad.bottom;this._elem.css({left:ac,bottom:ab});break;case"s":var ac=(ad.left+(this._plotDimensions.width-ad.right))/2-this.getWidth()/2;var ab=this._plotDimensions.height-ad.bottom;this._elem.css({left:ac,top:ab});break;case"sw":var ac=this._plotDimensions.width-ad.left;var ab=ad.bottom;this._elem.css({right:ac,bottom:ab});break;case"w":var ac=this._plotDimensions.width-ad.left;var ab=(ad.top+(this._plotDimensions.height-ad.bottom))/2-this.getHeight()/2;this._elem.css({right:ac,top:ab});break;default:var ac=ad.right;var ab=ad.bottom;this._elem.css({right:ac,bottom:ab});break}}else{switch(this.location){case"nw":this._elem.css({left:0,top:ad.top});break;case"n":var ac=(ad.left+(this._plotDimensions.width-ad.right))/2-this.getWidth()/2;this._elem.css({left:ac,top:ad.top});break;case"ne":this._elem.css({right:0,top:ad.top});break;case"e":var ab=(ad.top+(this._plotDimensions.height-ad.bottom))/2-this.getHeight()/2;this._elem.css({right:ad.right,top:ab});break;case"se":this._elem.css({right:ad.right,bottom:ad.bottom});break;case"s":var ac=(ad.left+(this._plotDimensions.width-ad.right))/2-this.getWidth()/2;this._elem.css({left:ac,bottom:ad.bottom});break;case"sw":this._elem.css({left:ad.left,bottom:ad.bottom});break;case"w":var ab=(ad.top+(this._plotDimensions.height-ad.bottom))/2-this.getHeight()/2;this._elem.css({left:ad.left,top:ab});break;default:this._elem.css({right:ad.right,bottom:ad.bottom});break}}}}};H.jqplot.ThemeEngine=function(){this.themes={};this.activeTheme=null};H.jqplot.ThemeEngine.prototype.init=function(){var ae=new H.jqplot.Theme({_name:"Default"});var ah,ac,ag;for(ah in ae.target){if(ah=="textColor"){ae.target[ah]=this.target.css("color")}else{ae.target[ah]=this.target.css(ah)}}if(this.title.show&&this.title._elem){for(ah in ae.title){if(ah=="textColor"){ae.title[ah]=this.title._elem.css("color")}else{ae.title[ah]=this.title._elem.css(ah)}}}for(ah in ae.grid){ae.grid[ah]=this.grid[ah]}if(ae.grid.backgroundColor==null&&this.grid.background!=null){ae.grid.backgroundColor=this.grid.background}if(this.legend.show&&this.legend._elem){for(ah in ae.legend){if(ah=="textColor"){ae.legend[ah]=this.legend._elem.css("color")}else{ae.legend[ah]=this.legend._elem.css(ah)}}}var ad;for(ac=0;ac<this.series.length;ac++){ad=this.series[ac];if(ad.renderer.constructor==H.jqplot.LineRenderer){ae.series.push(new m())}else{if(ad.renderer.constructor==H.jqplot.BarRenderer){ae.series.push(new P())}else{if(ad.renderer.constructor==H.jqplot.PieRenderer){ae.series.push(new e())}else{if(ad.renderer.constructor==H.jqplot.DonutRenderer){ae.series.push(new C())}else{if(ad.renderer.constructor==H.jqplot.FunnelRenderer){ae.series.push(new U())}else{if(ad.renderer.constructor==H.jqplot.MeterGaugeRenderer){ae.series.push(new z())}else{ae.series.push({})}}}}}}for(ah in ae.series[ac]){ae.series[ac][ah]=ad[ah]}}var ab,af;for(ah in this.axes){af=this.axes[ah];ab=ae.axes[ah]=new L();ab.borderColor=af.borderColor;ab.borderWidth=af.borderWidth;if(af._ticks&&af._ticks[0]){for(ag in ab.ticks){if(af._ticks[0].hasOwnProperty(ag)){ab.ticks[ag]=af._ticks[0][ag]}else{if(af._ticks[0]._elem){ab.ticks[ag]=af._ticks[0]._elem.css(ag)}}}}if(af._label&&af._label.show){for(ag in ab.label){if(af._label[ag]){ab.label[ag]=af._label[ag]}else{if(af._label._elem){if(ag=="textColor"){ab.label[ag]=af._label._elem.css("color")}else{ab.label[ag]=af._label._elem.css(ag)}}}}}}this.themeEngine._add(ae);this.themeEngine.activeTheme=this.themeEngine.themes[ae._name]};H.jqplot.ThemeEngine.prototype.get=function(ab){if(!ab){return this.activeTheme}else{return this.themes[ab]}};function K(ac,ab){return ac-ab}H.jqplot.ThemeEngine.prototype.getThemeNames=function(){var ab=[];for(var ac in this.themes){ab.push(ac)}return ab.sort(K)};H.jqplot.ThemeEngine.prototype.getThemes=function(){var ac=[];var ab=[];for(var ae in this.themes){ac.push(ae)}ac.sort(K);for(var ad=0;ad<ac.length;ad++){ab.push(this.themes[ac[ad]])}return ab};H.jqplot.ThemeEngine.prototype.activate=function(ao,au){var ab=false;if(!au&&this.activeTheme&&this.activeTheme._name){au=this.activeTheme._name}if(!this.themes.hasOwnProperty(au)){throw new Error("No theme of that name")}else{var ag=this.themes[au];this.activeTheme=ag;var at,am=false,al=false;var ac=["xaxis","x2axis","yaxis","y2axis"];for(ap=0;ap<ac.length;ap++){var ah=ac[ap];if(ag.axesStyles.borderColor!=null){ao.axes[ah].borderColor=ag.axesStyles.borderColor}if(ag.axesStyles.borderWidth!=null){ao.axes[ah].borderWidth=ag.axesStyles.borderWidth}}for(var ar in ao.axes){var ae=ao.axes[ar];if(ae.show){var ak=ag.axes[ar]||{};var ai=ag.axesStyles;var af=H.jqplot.extend(true,{},ak,ai);at=(ag.axesStyles.borderColor!=null)?ag.axesStyles.borderColor:af.borderColor;if(af.borderColor!=null){ae.borderColor=af.borderColor;ab=true}at=(ag.axesStyles.borderWidth!=null)?ag.axesStyles.borderWidth:af.borderWidth;if(af.borderWidth!=null){ae.borderWidth=af.borderWidth;ab=true}if(ae._ticks&&ae._ticks[0]){for(var ad in af.ticks){at=af.ticks[ad];if(at!=null){ae.tickOptions[ad]=at;ae._ticks=[];ab=true}}}if(ae._label&&ae._label.show){for(var ad in af.label){at=af.label[ad];if(at!=null){ae.labelOptions[ad]=at;ab=true}}}}}for(var an in ag.grid){if(ag.grid[an]!=null){ao.grid[an]=ag.grid[an]}}if(!ab){ao.grid.draw()}if(ao.legend.show){for(an in ag.legend){if(ag.legend[an]!=null){ao.legend[an]=ag.legend[an]}}}if(ao.title.show){for(an in ag.title){if(ag.title[an]!=null){ao.title[an]=ag.title[an]}}}var ap;for(ap=0;ap<ag.series.length;ap++){var aj={};var aq=false;for(an in ag.series[ap]){at=(ag.seriesStyles[an]!=null)?ag.seriesStyles[an]:ag.series[ap][an];if(at!=null){aj[an]=at;if(an=="color"){ao.series[ap].renderer.shapeRenderer.fillStyle=at;ao.series[ap].renderer.shapeRenderer.strokeStyle=at;ao.series[ap][an]=at}else{if((an=="lineWidth")||(an=="linePattern")){ao.series[ap].renderer.shapeRenderer[an]=at;ao.series[ap][an]=at}else{if(an=="markerOptions"){R(ao.series[ap].markerOptions,at);R(ao.series[ap].markerRenderer,at)}else{ao.series[ap][an]=at}}}ab=true}}}if(ab){ao.target.empty();ao.draw()}for(an in ag.target){if(ag.target[an]!=null){ao.target.css(an,ag.target[an])}}}};H.jqplot.ThemeEngine.prototype._add=function(ac,ab){if(ab){ac._name=ab}if(!ac._name){ac._name=Date.parse(new Date())}if(!this.themes.hasOwnProperty(ac._name)){this.themes[ac._name]=ac}else{throw new Error("jqplot.ThemeEngine Error: Theme already in use")}};H.jqplot.ThemeEngine.prototype.remove=function(ab){if(ab=="Default"){return false}return delete this.themes[ab]};H.jqplot.ThemeEngine.prototype.newTheme=function(ab,ad){if(typeof(ab)=="object"){ad=ad||ab;ab=null}if(ad&&ad._name){ab=ad._name}else{ab=ab||Date.parse(new Date())}var ac=this.copy(this.themes.Default._name,ab);H.jqplot.extend(ac,ad);return ac};function x(ad){if(ad==null||typeof(ad)!="object"){return ad}var ab=new ad.constructor();for(var ac in ad){ab[ac]=x(ad[ac])}return ab}H.jqplot.clone=x;function R(ad,ac){if(ac==null||typeof(ac)!="object"){return}for(var ab in ac){if(ab=="highlightColors"){ad[ab]=x(ac[ab])}if(ac[ab]!=null&&typeof(ac[ab])=="object"){if(!ad.hasOwnProperty(ab)){ad[ab]={}}R(ad[ab],ac[ab])}else{ad[ab]=ac[ab]}}}H.jqplot.merge=R;H.jqplot.extend=function(){var ag=arguments[0]||{},ae=1,af=arguments.length,ab=false,ad;if(typeof ag==="boolean"){ab=ag;ag=arguments[1]||{};ae=2}if(typeof ag!=="object"&&!toString.call(ag)==="[object Function]"){ag={}}for(;ae<af;ae++){if((ad=arguments[ae])!=null){for(var ac in ad){var ah=ag[ac],ai=ad[ac];if(ag===ai){continue}if(ab&&ai&&typeof ai==="object"&&!ai.nodeType){ag[ac]=H.jqplot.extend(ab,ah||(ai.length!=null?[]:{}),ai)}else{if(ai!==r){ag[ac]=ai}}}}}return ag};H.jqplot.ThemeEngine.prototype.rename=function(ac,ab){if(ac=="Default"||ab=="Default"){throw new Error("jqplot.ThemeEngine Error: Cannot rename from/to Default")}if(this.themes.hasOwnProperty(ab)){throw new Error("jqplot.ThemeEngine Error: New name already in use.")}else{if(this.themes.hasOwnProperty(ac)){var ad=this.copy(ac,ab);this.remove(ac);return ad}}throw new Error("jqplot.ThemeEngine Error: Old name or new name invalid")};H.jqplot.ThemeEngine.prototype.copy=function(ab,ad,af){if(ad=="Default"){throw new Error("jqplot.ThemeEngine Error: Cannot copy over Default theme")}if(!this.themes.hasOwnProperty(ab)){var ac="jqplot.ThemeEngine Error: Source name invalid";throw new Error(ac)}if(this.themes.hasOwnProperty(ad)){var ac="jqplot.ThemeEngine Error: Target name invalid";throw new Error(ac)}else{var ae=x(this.themes[ab]);ae._name=ad;H.jqplot.extend(true,ae,af);this._add(ae);return ae}};H.jqplot.Theme=function(ab,ac){if(typeof(ab)=="object"){ac=ac||ab;ab=null}ab=ab||Date.parse(new Date());this._name=ab;this.target={backgroundColor:null};this.legend={textColor:null,fontFamily:null,fontSize:null,border:null,background:null};this.title={textColor:null,fontFamily:null,fontSize:null,textAlign:null};this.seriesStyles={};this.series=[];this.grid={drawGridlines:null,gridLineColor:null,gridLineWidth:null,backgroundColor:null,borderColor:null,borderWidth:null,shadow:null};this.axesStyles={label:{},ticks:{}};this.axes={};if(typeof(ac)=="string"){this._name=ac}else{if(typeof(ac)=="object"){H.jqplot.extend(true,this,ac)}}};var L=function(){this.borderColor=null;this.borderWidth=null;this.ticks=new l();this.label=new q()};var l=function(){this.show=null;this.showGridline=null;this.showLabel=null;this.showMark=null;this.size=null;this.textColor=null;this.whiteSpace=null;this.fontSize=null;this.fontFamily=null};var q=function(){this.textColor=null;this.whiteSpace=null;this.fontSize=null;this.fontFamily=null;this.fontWeight=null};var m=function(){this.color=null;this.lineWidth=null;this.linePattern=null;this.shadow=null;this.fillColor=null;this.showMarker=null;this.markerOptions=new E()};var E=function(){this.show=null;this.style=null;this.lineWidth=null;this.size=null;this.color=null;this.shadow=null};var P=function(){this.color=null;this.seriesColors=null;this.lineWidth=null;this.shadow=null;this.barPadding=null;this.barMargin=null;this.barWidth=null;this.highlightColors=null};var e=function(){this.seriesColors=null;this.padding=null;this.sliceMargin=null;this.fill=null;this.shadow=null;this.startAngle=null;this.lineWidth=null;this.highlightColors=null};var C=function(){this.seriesColors=null;this.padding=null;this.sliceMargin=null;this.fill=null;this.shadow=null;this.startAngle=null;this.lineWidth=null;this.innerDiameter=null;this.thickness=null;this.ringMargin=null;this.highlightColors=null};var U=function(){this.color=null;this.lineWidth=null;this.shadow=null;this.padding=null;this.sectionMargin=null;this.seriesColors=null;this.highlightColors=null};var z=function(){this.padding=null;this.backgroundColor=null;this.ringColor=null;this.tickColor=null;this.ringWidth=null;this.intervalColors=null;this.intervalInnerRadius=null;this.intervalOuterRadius=null;this.hubRadius=null;this.needleThickness=null;this.needlePad=null};H.fn.jqplotChildText=function(){return H(this).contents().filter(function(){return this.nodeType==3}).text()};H.fn.jqplotGetComputedFontStyle=function(){var ae=window.getComputedStyle?window.getComputedStyle(this[0]):this[0].currentStyle;var ac=ae["font-style"]?["font-style","font-weight","font-size","font-family"]:["fontStyle","fontWeight","fontSize","fontFamily"];var af=[];for(var ad=0;ad<ac.length;++ad){var ab=String(ae[ac[ad]]);if(ab&&ab!="normal"){af.push(ab)}}return af.join(" ")};H.fn.jqplotToImageCanvas=function(ad){ad=ad||{};var ao=(ad.x_offset==null)?0:ad.x_offset;var aq=(ad.y_offset==null)?0:ad.y_offset;var af=(ad.backgroundColor==null)?"rgb(255,255,255)":ad.backgroundColor;if(H(this).width()==0||H(this).height()==0){return null}if(!H.jqplot.support_canvas){return null}var ah=document.createElement("canvas");var au=H(this).outerHeight(true);var am=H(this).outerWidth(true);var ag=H(this).offset();var ai=ag.left;var ak=ag.top;var an=0,al=0;var ar=["jqplot-table-legend","jqplot-xaxis-tick","jqplot-x2axis-tick","jqplot-yaxis-tick","jqplot-y2axis-tick","jqplot-y3axis-tick","jqplot-y4axis-tick","jqplot-y5axis-tick","jqplot-y6axis-tick","jqplot-y7axis-tick","jqplot-y8axis-tick","jqplot-y9axis-tick","jqplot-xaxis-label","jqplot-x2axis-label","jqplot-yaxis-label","jqplot-y2axis-label","jqplot-y3axis-label","jqplot-y4axis-label","jqplot-y5axis-label","jqplot-y6axis-label","jqplot-y7axis-label","jqplot-y8axis-label","jqplot-y9axis-label"];var aj,ab,ac,av;for(var at in ar){H(this).find("."+ar[at]).each(function(){aj=H(this).offset().top-ak;ab=H(this).offset().left-ai;av=ab+H(this).outerWidth(true)+an;ac=aj+H(this).outerHeight(true)+al;if(ab<-an){am=am-an-ab;an=-ab}if(aj<-al){au=au-al-aj;al=-aj}if(av>am){am=av}if(ac>au){au=ac}})}ah.width=am+Number(ao);ah.height=au+Number(aq);var ae=ah.getContext("2d");ae.save();ae.fillStyle=af;ae.fillRect(0,0,ah.width,ah.height);ae.restore();ae.translate(an,al);ae.textAlign="left";ae.textBaseline="top";function aw(ay){var az=parseInt(H(ay).css("line-height"),10);if(isNaN(az)){az=parseInt(H(ay).css("font-size"),10)*1.2}return az}function ax(az,ay,aM,aA,aI,aB){var aK=aw(az);var aE=H(az).innerWidth();var aF=H(az).innerHeight();var aH=aM.split(/\s+/);var aL=aH.length;var aJ="";var aG=[];var aO=aI;var aN=aA;for(var aD=0;aD<aL;aD++){aJ+=aH[aD];if(ay.measureText(aJ).width>aE){aG.push(aD);aJ=""}}if(aG.length===0){if(H(az).css("textAlign")==="center"){aN=aA+(aB-ay.measureText(aJ).width)/2-an}ay.fillText(aM,aN,aI)}else{aJ=aH.slice(0,aG[0]).join(" ");if(H(az).css("textAlign")==="center"){aN=aA+(aB-ay.measureText(aJ).width)/2-an}ay.fillText(aJ,aN,aO);aO+=aK;for(var aD=1,aC=aG.length;aD<aC;aD++){aJ=aH.slice(aG[aD-1],aG[aD]).join(" ");if(H(az).css("textAlign")==="center"){aN=aA+(aB-ay.measureText(aJ).width)/2-an}ay.fillText(aJ,aN,aO);aO+=aK}aJ=aH.slice(aG[aD-1],aH.length).join(" ");if(H(az).css("textAlign")==="center"){aN=aA+(aB-ay.measureText(aJ).width)/2-an}ay.fillText(aJ,aN,aO)}}function ap(aA,aD,ay){var aH=aA.tagName.toLowerCase();var az=H(aA).position();var aE=window.getComputedStyle?window.getComputedStyle(aA):aA.currentStyle;var aC=aD+az.left+parseInt(aE.marginLeft,10)+parseInt(aE.borderLeftWidth,10)+parseInt(aE.paddingLeft,10);var aF=ay+az.top+parseInt(aE.marginTop,10)+parseInt(aE.borderTopWidth,10)+parseInt(aE.paddingTop,10);var aG=ah.width;if((aH=="div"||aH=="span")&&!H(aA).hasClass("jqplot-highlighter-tooltip")){H(aA).children().each(function(){ap(this,aC,aF)});var aI=H(aA).jqplotChildText();if(aI){ae.font=H(aA).jqplotGetComputedFontStyle();ae.fillStyle=H(aA).css("color");ax(aA,ae,aI,aC,aF,aG)}}else{if(aH==="table"&&H(aA).hasClass("jqplot-table-legend")){ae.strokeStyle=H(aA).css("border-top-color");ae.fillStyle=H(aA).css("background-color");ae.fillRect(aC,aF,H(aA).innerWidth(),H(aA).innerHeight());if(parseInt(H(aA).css("border-top-width"),10)>0){ae.strokeRect(aC,aF,H(aA).innerWidth(),H(aA).innerHeight())}H(aA).find("div.jqplot-table-legend-swatch-outline").each(function(){var aO=H(this);ae.strokeStyle=aO.css("border-top-color");var aK=aC+aO.position().left;var aL=aF+aO.position().top;ae.strokeRect(aK,aL,aO.innerWidth(),aO.innerHeight());aK+=parseInt(aO.css("padding-left"),10);aL+=parseInt(aO.css("padding-top"),10);var aN=aO.innerHeight()-2*parseInt(aO.css("padding-top"),10);var aJ=aO.innerWidth()-2*parseInt(aO.css("padding-left"),10);var aM=aO.children("div.jqplot-table-legend-swatch");ae.fillStyle=aM.css("background-color");ae.fillRect(aK,aL,aJ,aN)});H(aA).find("td.jqplot-table-legend-label").each(function(){var aL=H(this);var aJ=aC+aL.position().left;var aK=aF+aL.position().top+parseInt(aL.css("padding-top"),10);ae.font=aL.jqplotGetComputedFontStyle();ae.fillStyle=aL.css("color");ae.fillText(aL.text(),aJ,aK)});var aB=null}else{if(aH=="canvas"){ae.drawImage(aA,aC,aF)}}}}H(this).children().each(function(){ap(this,ao,aq)});return ah};H.fn.jqplotToImageStr=function(ac){var ab=H(this).jqplotToImageCanvas(ac);if(ab){return ab.toDataURL("image/png")}else{return null}};H.fn.jqplotToImageElem=function(ab){var ac=document.createElement("img");var ad=H(this).jqplotToImageStr(ab);ac.src=ad;return ac};H.fn.jqplotToImageElemStr=function(ab){var ac="<img src="+H(this).jqplotToImageStr(ab)+" />";return ac};H.fn.jqplotSaveImage=function(){var ab=H(this).jqplotToImageStr({});if(ab){window.location.href=ab.replace("image/png","image/octet-stream")}};H.fn.jqplotViewImage=function(){var ac=H(this).jqplotToImageElemStr({});var ad=H(this).jqplotToImageStr({});if(ac){var ab=window.open("");ab.document.open("image/png");ab.document.write(ac);ab.document.close();ab=null}};var aa=function(){this.syntax=aa.config.syntax;this._type="jsDate";this.proxy=new Date();this.options={};this.locale=aa.regional.getLocale();this.formatString="";this.defaultCentury=aa.config.defaultCentury;switch(arguments.length){case 0:break;case 1:if(j(arguments[0])=="[object Object]"&&arguments[0]._type!="jsDate"){var ad=this.options=arguments[0];this.syntax=ad.syntax||this.syntax;this.defaultCentury=ad.defaultCentury||this.defaultCentury;this.proxy=aa.createDate(ad.date)}else{this.proxy=aa.createDate(arguments[0])}break;default:var ab=[];for(var ac=0;ac<arguments.length;ac++){ab.push(arguments[ac])}this.proxy=new Date();this.proxy.setFullYear.apply(this.proxy,ab.slice(0,3));if(ab.slice(3).length){this.proxy.setHours.apply(this.proxy,ab.slice(3))}break}};aa.config={defaultLocale:"en",syntax:"perl",defaultCentury:1900};aa.prototype.add=function(ad,ac){var ab=A[ac]||A.day;if(typeof ab=="number"){this.proxy.setTime(this.proxy.getTime()+(ab*ad))}else{ab.add(this,ad)}return this};aa.prototype.clone=function(){return new aa(this.proxy.getTime())};aa.prototype.getUtcOffset=function(){return this.proxy.getTimezoneOffset()*60000};aa.prototype.diff=function(ac,af,ab){ac=new aa(ac);if(ac===null){return null}var ad=A[af]||A.day;if(typeof ad=="number"){var ae=(this.proxy.getTime()-ac.proxy.getTime())/ad}else{var ae=ad.diff(this.proxy,ac.proxy)}return(ab?ae:Math[ae>0?"floor":"ceil"](ae))};aa.prototype.getAbbrDayName=function(){return aa.regional[this.locale]["dayNamesShort"][this.proxy.getDay()]};aa.prototype.getAbbrMonthName=function(){return aa.regional[this.locale]["monthNamesShort"][this.proxy.getMonth()]};aa.prototype.getAMPM=function(){return this.proxy.getHours()>=12?"PM":"AM"};aa.prototype.getAmPm=function(){return this.proxy.getHours()>=12?"pm":"am"};aa.prototype.getCentury=function(){return parseInt(this.proxy.getFullYear()/100,10)};aa.prototype.getDate=function(){return this.proxy.getDate()};aa.prototype.getDay=function(){return this.proxy.getDay()};aa.prototype.getDayOfWeek=function(){var ab=this.proxy.getDay();return ab===0?7:ab};aa.prototype.getDayOfYear=function(){var ac=this.proxy;var ab=ac-new Date(""+ac.getFullYear()+"/1/1 GMT");ab+=ac.getTimezoneOffset()*60000;ac=null;return parseInt(ab/60000/60/24,10)+1};aa.prototype.getDayName=function(){return aa.regional[this.locale]["dayNames"][this.proxy.getDay()]};aa.prototype.getFullWeekOfYear=function(){var ae=this.proxy;var ab=this.getDayOfYear();var ad=6-ae.getDay();var ac=parseInt((ab+ad)/7,10);return ac};aa.prototype.getFullYear=function(){return this.proxy.getFullYear()};aa.prototype.getGmtOffset=function(){var ab=this.proxy.getTimezoneOffset()/60;var ac=ab<0?"+":"-";ab=Math.abs(ab);return ac+J(Math.floor(ab),2)+":"+J((ab%1)*60,2)};aa.prototype.getHours=function(){return this.proxy.getHours()};aa.prototype.getHours12=function(){var ab=this.proxy.getHours();return ab>12?ab-12:(ab==0?12:ab)};aa.prototype.getIsoWeek=function(){var ae=this.proxy;var ad=ae.getWeekOfYear();var ab=(new Date(""+ae.getFullYear()+"/1/1")).getDay();var ac=ad+(ab>4||ab<=1?0:1);if(ac==53&&(new Date(""+ae.getFullYear()+"/12/31")).getDay()<4){ac=1}else{if(ac===0){ae=new aa(new Date(""+(ae.getFullYear()-1)+"/12/31"));ac=ae.getIsoWeek()}}ae=null;return ac};aa.prototype.getMilliseconds=function(){return this.proxy.getMilliseconds()};aa.prototype.getMinutes=function(){return this.proxy.getMinutes()};aa.prototype.getMonth=function(){return this.proxy.getMonth()};aa.prototype.getMonthName=function(){return aa.regional[this.locale]["monthNames"][this.proxy.getMonth()]};aa.prototype.getMonthNumber=function(){return this.proxy.getMonth()+1};aa.prototype.getSeconds=function(){return this.proxy.getSeconds()};aa.prototype.getShortYear=function(){return this.proxy.getYear()%100};aa.prototype.getTime=function(){return this.proxy.getTime()};aa.prototype.getTimezoneAbbr=function(){return this.proxy.toString().replace(/^.*\(([^)]+)\)$/,"$1")};aa.prototype.getTimezoneName=function(){var ab=/(?:\((.+)\)$| ([A-Z]{3}) )/.exec(this.toString());return ab[1]||ab[2]||"GMT"+this.getGmtOffset()};aa.prototype.getTimezoneOffset=function(){return this.proxy.getTimezoneOffset()};aa.prototype.getWeekOfYear=function(){var ab=this.getDayOfYear();var ad=7-this.getDayOfWeek();var ac=parseInt((ab+ad)/7,10);return ac};aa.prototype.getUnix=function(){return Math.round(this.proxy.getTime()/1000,0)};aa.prototype.getYear=function(){return this.proxy.getYear()};aa.prototype.next=function(ab){ab=ab||"day";return this.clone().add(1,ab)};aa.prototype.set=function(){switch(arguments.length){case 0:this.proxy=new Date();break;case 1:if(j(arguments[0])=="[object Object]"&&arguments[0]._type!="jsDate"){var ad=this.options=arguments[0];this.syntax=ad.syntax||this.syntax;this.defaultCentury=ad.defaultCentury||this.defaultCentury;this.proxy=aa.createDate(ad.date)}else{this.proxy=aa.createDate(arguments[0])}break;default:var ab=[];for(var ac=0;ac<arguments.length;ac++){ab.push(arguments[ac])}this.proxy=new Date();this.proxy.setFullYear.apply(this.proxy,ab.slice(0,3));if(ab.slice(3).length){this.proxy.setHours.apply(this.proxy,ab.slice(3))}break}return this};aa.prototype.setDate=function(ab){this.proxy.setDate(ab);return this};aa.prototype.setFullYear=function(){this.proxy.setFullYear.apply(this.proxy,arguments);return this};aa.prototype.setHours=function(){this.proxy.setHours.apply(this.proxy,arguments);return this};aa.prototype.setMilliseconds=function(ab){this.proxy.setMilliseconds(ab);return this};aa.prototype.setMinutes=function(){this.proxy.setMinutes.apply(this.proxy,arguments);return this};aa.prototype.setMonth=function(){this.proxy.setMonth.apply(this.proxy,arguments);return this};aa.prototype.setSeconds=function(){this.proxy.setSeconds.apply(this.proxy,arguments);return this};aa.prototype.setTime=function(ab){this.proxy.setTime(ab);return this};aa.prototype.setYear=function(){this.proxy.setYear.apply(this.proxy,arguments);return this};aa.prototype.strftime=function(ab){ab=ab||this.formatString||aa.regional[this.locale]["formatString"];return aa.strftime(this,ab,this.syntax)};aa.prototype.toString=function(){return this.proxy.toString()};aa.prototype.toYmdInt=function(){return(this.proxy.getFullYear()*10000)+(this.getMonthNumber()*100)+this.proxy.getDate()};aa.regional={en:{monthNames:["January","February","March","April","May","June","July","August","September","October","November","December"],monthNamesShort:["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],dayNames:["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"],dayNamesShort:["Sun","Mon","Tue","Wed","Thu","Fri","Sat"],formatString:"%Y-%m-%d %H:%M:%S"},fr:{monthNames:["Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Août","Septembre","Octobre","Novembre","Décembre"],monthNamesShort:["Jan","Fév","Mar","Avr","Mai","Jun","Jul","Aoû","Sep","Oct","Nov","Déc"],dayNames:["Dimanche","Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi"],dayNamesShort:["Dim","Lun","Mar","Mer","Jeu","Ven","Sam"],formatString:"%Y-%m-%d %H:%M:%S"},de:{monthNames:["Januar","Februar","März","April","Mai","Juni","Juli","August","September","Oktober","November","Dezember"],monthNamesShort:["Jan","Feb","Mär","Apr","Mai","Jun","Jul","Aug","Sep","Okt","Nov","Dez"],dayNames:["Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"],dayNamesShort:["So","Mo","Di","Mi","Do","Fr","Sa"],formatString:"%Y-%m-%d %H:%M:%S"},es:{monthNames:["Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"],monthNamesShort:["Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"],dayNames:["Domingo","Lunes","Martes","Mi&eacute;rcoles","Jueves","Viernes","S&aacute;bado"],dayNamesShort:["Dom","Lun","Mar","Mi&eacute;","Juv","Vie","S&aacute;b"],formatString:"%Y-%m-%d %H:%M:%S"},ru:{monthNames:["Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"],monthNamesShort:["Янв","Фев","Мар","Апр","Май","Июн","Июл","Авг","Сен","Окт","Ноя","Дек"],dayNames:["воскресенье","понедельник","вторник","среда","четверг","пятница","суббота"],dayNamesShort:["вск","пнд","втр","срд","чтв","птн","сбт"],formatString:"%Y-%m-%d %H:%M:%S"},ar:{monthNames:["كانون الثاني","شباط","آذار","نيسان","آذار","حزيران","تموز","آب","أيلول","تشرين الأول","تشرين الثاني","كانون الأول"],monthNamesShort:["1","2","3","4","5","6","7","8","9","10","11","12"],dayNames:["السبت","الأحد","الاثنين","الثلاثاء","الأربعاء","الخميس","الجمعة"],dayNamesShort:["سبت","أحد","اثنين","ثلاثاء","أربعاء","خميس","جمعة"],formatString:"%Y-%m-%d %H:%M:%S"},pt:{monthNames:["Janeiro","Fevereiro","Mar&ccedil;o","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"],monthNamesShort:["Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez"],dayNames:["Domingo","Segunda-feira","Ter&ccedil;a-feira","Quarta-feira","Quinta-feira","Sexta-feira","S&aacute;bado"],dayNamesShort:["Dom","Seg","Ter","Qua","Qui","Sex","S&aacute;b"],formatString:"%Y-%m-%d %H:%M:%S"},"pt-BR":{monthNames:["Janeiro","Fevereiro","Mar&ccedil;o","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"],monthNamesShort:["Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez"],dayNames:["Domingo","Segunda-feira","Ter&ccedil;a-feira","Quarta-feira","Quinta-feira","Sexta-feira","S&aacute;bado"],dayNamesShort:["Dom","Seg","Ter","Qua","Qui","Sex","S&aacute;b"],formatString:"%Y-%m-%d %H:%M:%S"}};aa.regional["en-US"]=aa.regional["en-GB"]=aa.regional.en;aa.regional.getLocale=function(){var ab=aa.config.defaultLocale;if(document&&document.getElementsByTagName("html")&&document.getElementsByTagName("html")[0].lang){ab=document.getElementsByTagName("html")[0].lang;if(!aa.regional.hasOwnProperty(ab)){ab=aa.config.defaultLocale}}return ab};var y=24*60*60*1000;var J=function(ab,ae){ab=String(ab);var ac=ae-ab.length;var ad=String(Math.pow(10,ac)).slice(1);return ad.concat(ab)};var A={millisecond:1,second:1000,minute:60*1000,hour:60*60*1000,day:y,week:7*y,month:{add:function(ad,ab){A.year.add(ad,Math[ab>0?"floor":"ceil"](ab/12));var ac=ad.getMonth()+(ab%12);if(ac==12){ac=0;ad.setYear(ad.getFullYear()+1)}else{if(ac==-1){ac=11;ad.setYear(ad.getFullYear()-1)}}ad.setMonth(ac)},diff:function(af,ad){var ab=af.getFullYear()-ad.getFullYear();var ac=af.getMonth()-ad.getMonth()+(ab*12);var ae=af.getDate()-ad.getDate();return ac+(ae/30)}},year:{add:function(ac,ab){ac.setYear(ac.getFullYear()+Math[ab>0?"floor":"ceil"](ab))},diff:function(ac,ab){return A.month.diff(ac,ab)/12}}};for(var T in A){if(T.substring(T.length-1)!="s"){A[T+"s"]=A[T]}}var D=function(af,ae,ac){if(aa.formats[ac]["shortcuts"][ae]){return aa.strftime(af,aa.formats[ac]["shortcuts"][ae],ac)}else{var ab=(aa.formats[ac]["codes"][ae]||"").split(".");var ad=af["get"+ab[0]]?af["get"+ab[0]]():"";if(ab[1]){ad=J(ad,ab[1])}return ad}};aa.strftime=function(ah,ae,ad,ai){var ac="perl";var ag=aa.regional.getLocale();if(ad&&aa.formats.hasOwnProperty(ad)){ac=ad}else{if(ad&&aa.regional.hasOwnProperty(ad)){ag=ad}}if(ai&&aa.formats.hasOwnProperty(ai)){ac=ai}else{if(ai&&aa.regional.hasOwnProperty(ai)){ag=ai}}if(j(ah)!="[object Object]"||ah._type!="jsDate"){ah=new aa(ah);ah.locale=ag}if(!ae){ae=ah.formatString||aa.regional[ag]["formatString"]}var ab=ae||"%Y-%m-%d",aj="",af;while(ab.length>0){if(af=ab.match(aa.formats[ac].codes.matcher)){aj+=ab.slice(0,af.index);aj+=(af[1]||"")+D(ah,af[2],ac);ab=ab.slice(af.index+af[0].length)}else{aj+=ab;ab=""}}return aj};aa.formats={ISO:"%Y-%m-%dT%H:%M:%S.%N%G",SQL:"%Y-%m-%d %H:%M:%S"};aa.formats.perl={codes:{matcher:/()%(#?(%|[a-z]))/i,Y:"FullYear",y:"ShortYear.2",m:"MonthNumber.2","#m":"MonthNumber",B:"MonthName",b:"AbbrMonthName",d:"Date.2","#d":"Date",e:"Date",A:"DayName",a:"AbbrDayName",w:"Day",H:"Hours.2","#H":"Hours",I:"Hours12.2","#I":"Hours12",p:"AMPM",M:"Minutes.2","#M":"Minutes",S:"Seconds.2","#S":"Seconds",s:"Unix",N:"Milliseconds.3","#N":"Milliseconds",O:"TimezoneOffset",Z:"TimezoneName",G:"GmtOffset"},shortcuts:{F:"%Y-%m-%d",T:"%H:%M:%S",X:"%H:%M:%S",x:"%m/%d/%y",D:"%m/%d/%y","#c":"%a %b %e %H:%M:%S %Y",v:"%e-%b-%Y",R:"%H:%M",r:"%I:%M:%S %p",t:"\t",n:"\n","%":"%"}};aa.formats.php={codes:{matcher:/()%((%|[a-z]))/i,a:"AbbrDayName",A:"DayName",d:"Date.2",e:"Date",j:"DayOfYear.3",u:"DayOfWeek",w:"Day",U:"FullWeekOfYear.2",V:"IsoWeek.2",W:"WeekOfYear.2",b:"AbbrMonthName",B:"MonthName",m:"MonthNumber.2",h:"AbbrMonthName",C:"Century.2",y:"ShortYear.2",Y:"FullYear",H:"Hours.2",I:"Hours12.2",l:"Hours12",p:"AMPM",P:"AmPm",M:"Minutes.2",S:"Seconds.2",s:"Unix",O:"TimezoneOffset",z:"GmtOffset",Z:"TimezoneAbbr"},shortcuts:{D:"%m/%d/%y",F:"%Y-%m-%d",T:"%H:%M:%S",X:"%H:%M:%S",x:"%m/%d/%y",R:"%H:%M",r:"%I:%M:%S %p",t:"\t",n:"\n","%":"%"}};aa.createDate=function(ad){if(ad==null){return new Date()}if(ad instanceof Date){return ad}if(typeof ad=="number"){return new Date(ad)}var ai=String(ad).replace(/^\s*(.+)\s*$/g,"$1");ai=ai.replace(/^([0-9]{1,4})-([0-9]{1,2})-([0-9]{1,4})/,"$1/$2/$3");ai=ai.replace(/^(3[01]|[0-2]?\d)[-\/]([a-z]{3,})[-\/](\d{4})/i,"$1 $2 $3");var ah=ai.match(/^(3[01]|[0-2]?\d)[-\/]([a-z]{3,})[-\/](\d{2})\D*/i);if(ah&&ah.length>3){var am=parseFloat(ah[3]);var ag=aa.config.defaultCentury+am;ag=String(ag);ai=ai.replace(/^(3[01]|[0-2]?\d)[-\/]([a-z]{3,})[-\/](\d{2})\D*/i,ah[1]+" "+ah[2]+" "+ag)}ah=ai.match(/^([0-9]{1,2})[-\/]([0-9]{1,2})[-\/]([0-9]{1,2})[^0-9]/);function al(aq,ap){var aw=parseFloat(ap[1]);var av=parseFloat(ap[2]);var au=parseFloat(ap[3]);var at=aa.config.defaultCentury;var ao,an,ax,ar;if(aw>31){an=au;ax=av;ao=at+aw}else{an=av;ax=aw;ao=at+au}ar=ax+"/"+an+"/"+ao;return aq.replace(/^([0-9]{1,2})[-\/]([0-9]{1,2})[-\/]([0-9]{1,2})/,ar)}if(ah&&ah.length>3){ai=al(ai,ah)}var ah=ai.match(/^([0-9]{1,2})[-\/]([0-9]{1,2})[-\/]([0-9]{1,2})$/);if(ah&&ah.length>3){ai=al(ai,ah)}var af=0;var ac=aa.matchers.length;var ak,ab,aj=ai,ae;while(af<ac){ab=Date.parse(aj);if(!isNaN(ab)){return new Date(ab)}ak=aa.matchers[af];if(typeof ak=="function"){ae=ak.call(aa,aj);if(ae instanceof Date){return ae}}else{aj=ai.replace(ak[0],ak[1])}af++}return NaN};aa.daysInMonth=function(ab,ac){if(ac==2){return new Date(ab,1,29).getDate()==29?29:28}return[r,31,r,31,30,31,30,31,31,30,31,30,31][ac]};aa.matchers=[[/(3[01]|[0-2]\d)\s*\.\s*(1[0-2]|0\d)\s*\.\s*([1-9]\d{3})/,"$2/$1/$3"],[/([1-9]\d{3})\s*-\s*(1[0-2]|0\d)\s*-\s*(3[01]|[0-2]\d)/,"$2/$3/$1"],function(ae){var ac=ae.match(/^(?:(.+)\s+)?([012]?\d)(?:\s*\:\s*(\d\d))?(?:\s*\:\s*(\d\d(\.\d*)?))?\s*(am|pm)?\s*$/i);if(ac){if(ac[1]){var ad=this.createDate(ac[1]);if(isNaN(ad)){return}}else{var ad=new Date();ad.setMilliseconds(0)}var ab=parseFloat(ac[2]);if(ac[6]){ab=ac[6].toLowerCase()=="am"?(ab==12?0:ab):(ab==12?12:ab+12)}ad.setHours(ab,parseInt(ac[3]||0,10),parseInt(ac[4]||0,10),((parseFloat(ac[5]||0))||0)*1000);return ad}else{return ae}},function(ae){var ac=ae.match(/^(?:(.+))[T|\s+]([012]\d)(?:\:(\d\d))(?:\:(\d\d))(?:\.\d+)([\+\-]\d\d\:\d\d)$/i);if(ac){if(ac[1]){var ad=this.createDate(ac[1]);if(isNaN(ad)){return}}else{var ad=new Date();ad.setMilliseconds(0)}var ab=parseFloat(ac[2]);ad.setHours(ab,parseInt(ac[3],10),parseInt(ac[4],10),parseFloat(ac[5])*1000);return ad}else{return ae}},function(af){var ad=af.match(/^([0-3]?\d)\s*[-\/.\s]{1}\s*([a-zA-Z]{3,9})\s*[-\/.\s]{1}\s*([0-3]?\d)$/);if(ad){var ae=new Date();var ag=aa.config.defaultCentury;var ai=parseFloat(ad[1]);var ah=parseFloat(ad[3]);var ac,ab,aj;if(ai>31){ab=ah;ac=ag+ai}else{ab=ai;ac=ag+ah}var aj=W(ad[2],aa.regional[aa.regional.getLocale()]["monthNamesShort"]);if(aj==-1){aj=W(ad[2],aa.regional[aa.regional.getLocale()]["monthNames"])}ae.setFullYear(ac,aj,ab);ae.setHours(0,0,0,0);return ae}else{return af}}];function W(ad,ae){if(ae.indexOf){return ae.indexOf(ad)}for(var ab=0,ac=ae.length;ab<ac;ab++){if(ae[ab]===ad){return ab}}return -1}function j(ab){if(ab===null){return"[object Null]"}return Object.prototype.toString.call(ab)}H.jsDate=aa;H.jqplot.sprintf=function(){function ah(an,aj,ak,am){var al=(an.length>=aj)?"":Array(1+aj-an.length>>>0).join(ak);return am?an+al:al+an}function ae(al){var ak=new String(al);for(var aj=10;aj>0;aj--){if(ak==(ak=ak.replace(/^(\d+)(\d{3})/,"$1"+H.jqplot.sprintf.thousandsSeparator+"$2"))){break}}return ak}function ad(ao,an,aq,al,am,ak){var ap=al-ao.length;if(ap>0){var aj=" ";if(ak){aj="&nbsp;"}if(aq||!am){ao=ah(ao,al,aj,aq)}else{ao=ao.slice(0,an.length)+ah("",ap,"0",true)+ao.slice(an.length)}}return ao}function ai(ar,ak,ap,al,aj,ao,aq,an){var am=ar>>>0;ap=ap&&am&&{"2":"0b","8":"0","16":"0x"}[ak]||"";ar=ap+ah(am.toString(ak),ao||0,"0",false);return ad(ar,ap,al,aj,aq,an)}function ab(an,ao,al,aj,am,ak){if(aj!=null){an=an.slice(0,aj)}return ad(an,"",ao,al,am,ak)}var ac=arguments,af=0,ag=ac[af++];return ag.replace(H.jqplot.sprintf.regex,function(aF,aq,ar,av,aH,aC,ao){if(aF=="%%"){return"%"}var aw=false,at="",au=false,aE=false,ap=false,an=false;for(var aB=0;ar&&aB<ar.length;aB++){switch(ar.charAt(aB)){case" ":at=" ";break;case"+":at="+";break;case"-":aw=true;break;case"0":au=true;break;case"#":aE=true;break;case"&":ap=true;break;case"'":an=true;break}}if(!av){av=0}else{if(av=="*"){av=+ac[af++]}else{if(av.charAt(0)=="*"){av=+ac[av.slice(1,-1)]}else{av=+av}}}if(av<0){av=-av;aw=true}if(!isFinite(av)){throw new Error("$.jqplot.sprintf: (minimum-)width must be finite")}if(!aC){aC="fFeE".indexOf(ao)>-1?6:(ao=="d")?0:void (0)}else{if(aC=="*"){aC=+ac[af++]}else{if(aC.charAt(0)=="*"){aC=+ac[aC.slice(1,-1)]}else{aC=+aC}}}var ay=aq?ac[aq.slice(0,-1)]:ac[af++];switch(ao){case"s":if(ay==null){return""}return ab(String(ay),aw,av,aC,au,ap);case"c":return ab(String.fromCharCode(+ay),aw,av,aC,au,ap);case"b":return ai(ay,2,aE,aw,av,aC,au,ap);case"o":return ai(ay,8,aE,aw,av,aC,au,ap);case"x":return ai(ay,16,aE,aw,av,aC,au,ap);case"X":return ai(ay,16,aE,aw,av,aC,au,ap).toUpperCase();case"u":return ai(ay,10,aE,aw,av,aC,au,ap);case"i":var al=parseInt(+ay,10);if(isNaN(al)){return""}var aA=al<0?"-":at;var aD=an?ae(String(Math.abs(al))):String(Math.abs(al));ay=aA+ah(aD,aC,"0",false);return ad(ay,aA,aw,av,au,ap);case"d":var al=Math.round(+ay);if(isNaN(al)){return""}var aA=al<0?"-":at;var aD=an?ae(String(Math.abs(al))):String(Math.abs(al));ay=aA+ah(aD,aC,"0",false);return ad(ay,aA,aw,av,au,ap);case"e":case"E":case"f":case"F":case"g":case"G":var al=+ay;if(isNaN(al)){return""}var aA=al<0?"-":at;var am=["toExponential","toFixed","toPrecision"]["efg".indexOf(ao.toLowerCase())];var aG=["toString","toUpperCase"]["eEfFgG".indexOf(ao)%2];var aD=Math.abs(al)[am](aC);aD=an?ae(aD):aD;ay=aA+aD;return ad(ay,aA,aw,av,au,ap)[aG]();case"p":case"P":var al=+ay;if(isNaN(al)){return""}var aA=al<0?"-":at;var ax=String(Number(Math.abs(al)).toExponential()).split(/e|E/);var ak=(ax[0].indexOf(".")!=-1)?ax[0].length-1:ax[0].length;var az=(ax[1]<0)?-ax[1]-1:0;if(Math.abs(al)<1){if(ak+az<=aC){ay=aA+Math.abs(al).toPrecision(ak)}else{if(ak<=aC-1){ay=aA+Math.abs(al).toExponential(ak-1)}else{ay=aA+Math.abs(al).toExponential(aC-1)}}}else{var aj=(ak<=aC)?ak:aC;ay=aA+Math.abs(al).toPrecision(aj)}var aG=["toString","toUpperCase"]["pP".indexOf(ao)%2];return ad(ay,aA,aw,av,au,ap)[aG]();case"n":return"";default:return aF}})};H.jqplot.sprintf.thousandsSeparator=",";H.jqplot.sprintf.regex=/%%|%(\d+\$)?([-+#0&\' ]*)(\*\d+\$|\*|\d+)?(\.(\*\d+\$|\*|\d+))?([nAscboxXuidfegpEGP])/g;H.jqplot.getSignificantFigures=function(af){var ah=String(Number(Math.abs(af)).toExponential()).split(/e|E/);var ag=(ah[0].indexOf(".")!=-1)?ah[0].length-1:ah[0].length;var ac=(ah[1]<0)?-ah[1]-1:0;var ab=parseInt(ah[1],10);var ad=(ab+1>0)?ab+1:0;var ae=(ag<=ad)?0:ag-ab-1;return{significantDigits:ag,digitsLeft:ad,digitsRight:ae,zeros:ac,exponent:ab}};H.jqplot.getPrecision=function(ab){return H.jqplot.getSignificantFigures(ab).digitsRight}})(jQuery);var backCompat=$.uiBackCompat!==false;$.jqplot.effects={effect:{}};var dataSpace="jqplot.storage.";$.extend($.jqplot.effects,{version:"1.9pre",save:function(b,c){for(var a=0;a<c.length;a++){if(c[a]!==null){b.data(dataSpace+c[a],b[0].style[c[a]])}}},restore:function(b,c){for(var a=0;a<c.length;a++){if(c[a]!==null){b.css(c[a],b.data(dataSpace+c[a]))}}},setMode:function(a,b){if(b==="toggle"){b=a.is(":hidden")?"show":"hide"}return b},createWrapper:function(b){if(b.parent().is(".ui-effects-wrapper")){return b.parent()}var c={width:b.outerWidth(true),height:b.outerHeight(true),"float":b.css("float")},e=$("<div></div>").addClass("ui-effects-wrapper").css({fontSize:"100%",background:"transparent",border:"none",margin:0,padding:0}),a={width:b.width(),height:b.height()},d=document.activeElement;b.wrap(e);if(b[0]===d||$.contains(b[0],d)){$(d).focus()}e=b.parent();if(b.css("position")==="static"){e.css({position:"relative"});b.css({position:"relative"})}else{$.extend(c,{position:b.css("position"),zIndex:b.css("z-index")});$.each(["top","left","bottom","right"],function(f,g){c[g]=b.css(g);if(isNaN(parseInt(c[g],10))){c[g]="auto"}});b.css({position:"relative",top:0,left:0,right:"auto",bottom:"auto"})}b.css(a);return e.css(c).show()},removeWrapper:function(a){var b=document.activeElement;if(a.parent().is(".ui-effects-wrapper")){a.parent().replaceWith(a);if(a[0]===b||$.contains(a[0],b)){$(b).focus()}}return a}});function _normalizeArguments(b,a,c,d){if($.isPlainObject(b)){return b}b={effect:b};if(a===undefined){a={}}if($.isFunction(a)){d=a;c=null;a={}}if($.type(a)==="number"||$.fx.speeds[a]){d=c;c=a;a={}}if($.isFunction(c)){d=c;c=null}if(a){$.extend(b,a)}c=c||a.duration;b.duration=$.fx.off?0:typeof c==="number"?c:c in $.fx.speeds?$.fx.speeds[c]:$.fx.speeds._default;b.complete=d||a.complete;return b}function standardSpeed(a){if(!a||typeof a==="number"||$.fx.speeds[a]){return true}if(typeof a==="string"&&!$.jqplot.effects.effect[a]){if(backCompat&&$.jqplot.effects[a]){return false}return true}return false}$.fn.extend({jqplotEffect:function(i,j,b,h){var g=_normalizeArguments.apply(this,arguments),d=g.mode,e=g.queue,f=$.jqplot.effects.effect[g.effect],a=!f&&backCompat&&$.jqplot.effects[g.effect];if($.fx.off||!(f||a)){if(d){return this[d](g.duration,g.complete)}else{return this.each(function(){if(g.complete){g.complete.call(this)}})}}function c(m){var n=$(this),l=g.complete,o=g.mode;function k(){if($.isFunction(l)){l.call(n[0])}if($.isFunction(m)){m()}}if(n.is(":hidden")?o==="hide":o==="show"){k()}else{f.call(n[0],g,k)}}if(f){return e===false?this.each(c):this.queue(e||"fx",c)}else{return a.call(this,{options:g,duration:g.duration,callback:g.complete,mode:g.mode})}}});var rvertical=/up|down|vertical/,rpositivemotion=/up|left|vertical|horizontal/;$.jqplot.effects.effect.blind=function(c,h){var d=$(this),k=["position","top","bottom","left","right","height","width"],i=$.jqplot.effects.setMode(d,c.mode||"hide"),m=c.direction||"up",f=rvertical.test(m),e=f?"height":"width",j=f?"top":"left",p=rpositivemotion.test(m),g={},n=i==="show",b,a,l;if(d.parent().is(".ui-effects-wrapper")){$.jqplot.effects.save(d.parent(),k)}else{$.jqplot.effects.save(d,k)}d.show();l=parseInt(d.css("top"),10);b=$.jqplot.effects.createWrapper(d).css({overflow:"hidden"});a=f?b[e]()+l:b[e]();g[e]=n?String(a):"0";if(!p){d.css(f?"bottom":"right",0).css(f?"top":"left","").css({position:"absolute"});g[j]=n?"0":String(a)}if(n){b.css(e,0);if(!p){b.css(j,a)}}b.animate(g,{duration:c.duration,easing:c.easing,queue:false,complete:function(){if(i==="hide"){d.hide()}$.jqplot.effects.restore(d,k);$.jqplot.effects.removeWrapper(d);h()}})};/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.0b2_r1012
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * Although not required, the author would appreciate an email letting him
 * know of any substantial use of jqPlot.  You can reach the author at:
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 * included jsDate library by Chris Leonello:
 *
 * Copyright (c) 2010-2011 Chris Leonello
 *
 * jsDate is currently available for use in all personal or commercial projects
 * under both the MIT and GPL version 2.0 licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * jsDate borrows many concepts and ideas from the Date Instance
 * Methods by Ken Snyder along with some parts of Ken's actual code.
 *
 * Ken's origianl Date Instance Methods and copyright notice:
 *
 * Ken Snyder (ken d snyder at gmail dot com)
 * 2008-09-10
 * version 2.0.2 (http://kendsnyder.com/sandbox/date/)
 * Creative Commons Attribution License 3.0 (http://creativecommons.org/licenses/by/3.0/)
 *
 * jqplotToImage function based on Larry Siden's export-jqplot-to-png.js.
 * Larry has generously given permission to adapt his code for inclusion
 * into jqPlot.
 *
 * Larry's original code can be found here:
 *
 * https://github.com/lsiden/export-jqplot-to-png
 *
 *
 */
(function(a){a.jqplot.CanvasAxisLabelRenderer=function(b){this.angle=0;this.axis;this.show=true;this.showLabel=true;this.label="";this.fontFamily='"Trebuchet MS", Arial, Helvetica, sans-serif';this.fontSize="11pt";this.fontWeight="normal";this.fontStretch=1;this.textColor="#666666";this.enableFontSupport=true;this.pt2px=null;this._elem;this._ctx;this._plotWidth;this._plotHeight;this._plotDimensions={height:null,width:null};a.extend(true,this,b);if(b.angle==null&&this.axis!="xaxis"&&this.axis!="x2axis"){this.angle=-90}var c={fontSize:this.fontSize,fontWeight:this.fontWeight,fontStretch:this.fontStretch,fillStyle:this.textColor,angle:this.getAngleRad(),fontFamily:this.fontFamily};if(this.pt2px){c.pt2px=this.pt2px}if(this.enableFontSupport){if(a.jqplot.support_canvas_text()){this._textRenderer=new a.jqplot.CanvasFontRenderer(c)}else{this._textRenderer=new a.jqplot.CanvasTextRenderer(c)}}else{this._textRenderer=new a.jqplot.CanvasTextRenderer(c)}};a.jqplot.CanvasAxisLabelRenderer.prototype.init=function(b){a.extend(true,this,b);this._textRenderer.init({fontSize:this.fontSize,fontWeight:this.fontWeight,fontStretch:this.fontStretch,fillStyle:this.textColor,angle:this.getAngleRad(),fontFamily:this.fontFamily})};a.jqplot.CanvasAxisLabelRenderer.prototype.getWidth=function(d){if(this._elem){return this._elem.outerWidth(true)}else{var f=this._textRenderer;var c=f.getWidth(d);var e=f.getHeight(d);var b=Math.abs(Math.sin(f.angle)*e)+Math.abs(Math.cos(f.angle)*c);return b}};a.jqplot.CanvasAxisLabelRenderer.prototype.getHeight=function(d){if(this._elem){return this._elem.outerHeight(true)}else{var f=this._textRenderer;var c=f.getWidth(d);var e=f.getHeight(d);var b=Math.abs(Math.cos(f.angle)*e)+Math.abs(Math.sin(f.angle)*c);return b}};a.jqplot.CanvasAxisLabelRenderer.prototype.getAngleRad=function(){var b=this.angle*Math.PI/180;return b};a.jqplot.CanvasAxisLabelRenderer.prototype.draw=function(c,f){if(this._elem){if(a.jqplot.use_excanvas&&window.G_vmlCanvasManager.uninitElement!==undefined){window.G_vmlCanvasManager.uninitElement(this._elem.get(0))}this._elem.emptyForce();this._elem=null}var e=f.canvasManager.getCanvas();this._textRenderer.setText(this.label,c);var b=this.getWidth(c);var d=this.getHeight(c);e.width=b;e.height=d;e.style.width=b;e.style.height=d;e=f.canvasManager.initCanvas(e);this._elem=a(e);this._elem.css({position:"absolute"});this._elem.addClass("jqplot-"+this.axis+"-label");e=null;return this._elem};a.jqplot.CanvasAxisLabelRenderer.prototype.pack=function(){this._textRenderer.draw(this._elem.get(0).getContext("2d"),this.label)}})(jQuery);/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.0b2_r1012
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * Although not required, the author would appreciate an email letting him
 * know of any substantial use of jqPlot.  You can reach the author at:
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 * included jsDate library by Chris Leonello:
 *
 * Copyright (c) 2010-2011 Chris Leonello
 *
 * jsDate is currently available for use in all personal or commercial projects
 * under both the MIT and GPL version 2.0 licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * jsDate borrows many concepts and ideas from the Date Instance
 * Methods by Ken Snyder along with some parts of Ken's actual code.
 *
 * Ken's origianl Date Instance Methods and copyright notice:
 *
 * Ken Snyder (ken d snyder at gmail dot com)
 * 2008-09-10
 * version 2.0.2 (http://kendsnyder.com/sandbox/date/)
 * Creative Commons Attribution License 3.0 (http://creativecommons.org/licenses/by/3.0/)
 *
 * jqplotToImage function based on Larry Siden's export-jqplot-to-png.js.
 * Larry has generously given permission to adapt his code for inclusion
 * into jqPlot.
 *
 * Larry's original code can be found here:
 *
 * https://github.com/lsiden/export-jqplot-to-png
 *
 *
 */
(function(a){a.jqplot.CanvasTextRenderer=function(b){this.fontStyle="normal";this.fontVariant="normal";this.fontWeight="normal";this.fontSize="10px";this.fontFamily="sans-serif";this.fontStretch=1;this.fillStyle="#666666";this.angle=0;this.textAlign="start";this.textBaseline="alphabetic";this.text;this.width;this.height;this.pt2px=1.28;a.extend(true,this,b);this.normalizedFontSize=this.normalizeFontSize(this.fontSize);this.setHeight()};a.jqplot.CanvasTextRenderer.prototype.init=function(b){a.extend(true,this,b);this.normalizedFontSize=this.normalizeFontSize(this.fontSize);this.setHeight()};a.jqplot.CanvasTextRenderer.prototype.normalizeFontSize=function(b){b=String(b);var c=parseFloat(b);if(b.indexOf("px")>-1){return c/this.pt2px}else{if(b.indexOf("pt")>-1){return c}else{if(b.indexOf("em")>-1){return c*12}else{if(b.indexOf("%")>-1){return c*12/100}else{return c/this.pt2px}}}}};a.jqplot.CanvasTextRenderer.prototype.fontWeight2Float=function(b){if(Number(b)){return b/400}else{switch(b){case"normal":return 1;break;case"bold":return 1.75;break;case"bolder":return 2.25;break;case"lighter":return 0.75;break;default:return 1;break}}};a.jqplot.CanvasTextRenderer.prototype.getText=function(){return this.text};a.jqplot.CanvasTextRenderer.prototype.setText=function(c,b){this.text=c;this.setWidth(b);return this};a.jqplot.CanvasTextRenderer.prototype.getWidth=function(b){return this.width};a.jqplot.CanvasTextRenderer.prototype.setWidth=function(c,b){if(!b){this.width=this.measure(c,this.text)}else{this.width=b}return this};a.jqplot.CanvasTextRenderer.prototype.getHeight=function(b){return this.height};a.jqplot.CanvasTextRenderer.prototype.setHeight=function(b){if(!b){this.height=this.normalizedFontSize*this.pt2px}else{this.height=b}return this};a.jqplot.CanvasTextRenderer.prototype.letter=function(b){return this.letters[b]};a.jqplot.CanvasTextRenderer.prototype.ascent=function(){return this.normalizedFontSize};a.jqplot.CanvasTextRenderer.prototype.descent=function(){return 7*this.normalizedFontSize/25};a.jqplot.CanvasTextRenderer.prototype.measure=function(d,g){var f=0;var b=g.length;for(var e=0;e<b;e++){var h=this.letter(g.charAt(e));if(h){f+=h.width*this.normalizedFontSize/25*this.fontStretch}}return f};a.jqplot.CanvasTextRenderer.prototype.draw=function(s,n){var r=0;var o=this.height*0.72;var p=0;var l=n.length;var k=this.normalizedFontSize/25;s.save();var h,f;if((-Math.PI/2<=this.angle&&this.angle<=0)||(Math.PI*3/2<=this.angle&&this.angle<=Math.PI*2)){h=0;f=-Math.sin(this.angle)*this.width}else{if((0<this.angle&&this.angle<=Math.PI/2)||(-Math.PI*2<=this.angle&&this.angle<=-Math.PI*3/2)){h=Math.sin(this.angle)*this.height;f=0}else{if((-Math.PI<this.angle&&this.angle<-Math.PI/2)||(Math.PI<=this.angle&&this.angle<=Math.PI*3/2)){h=-Math.cos(this.angle)*this.width;f=-Math.sin(this.angle)*this.width-Math.cos(this.angle)*this.height}else{if((-Math.PI*3/2<this.angle&&this.angle<Math.PI)||(Math.PI/2<this.angle&&this.angle<Math.PI)){h=Math.sin(this.angle)*this.height-Math.cos(this.angle)*this.width;f=-Math.cos(this.angle)*this.height}}}}s.strokeStyle=this.fillStyle;s.fillStyle=this.fillStyle;s.translate(h,f);s.rotate(this.angle);s.lineCap="round";var t=(this.normalizedFontSize>30)?2:2+(30-this.normalizedFontSize)/20;s.lineWidth=t*k*this.fontWeight2Float(this.fontWeight);for(var g=0;g<l;g++){var m=this.letter(n.charAt(g));if(!m){continue}s.beginPath();var e=1;var b=0;for(var d=0;d<m.points.length;d++){var q=m.points[d];if(q[0]==-1&&q[1]==-1){e=1;continue}if(e){s.moveTo(r+q[0]*k*this.fontStretch,o-q[1]*k);e=false}else{s.lineTo(r+q[0]*k*this.fontStretch,o-q[1]*k)}}s.stroke();r+=m.width*k*this.fontStretch}s.restore();return p};a.jqplot.CanvasTextRenderer.prototype.letters={" ":{width:16,points:[]},"!":{width:10,points:[[5,21],[5,7],[-1,-1],[5,2],[4,1],[5,0],[6,1],[5,2]]},'"':{width:16,points:[[4,21],[4,14],[-1,-1],[12,21],[12,14]]},"#":{width:21,points:[[11,25],[4,-7],[-1,-1],[17,25],[10,-7],[-1,-1],[4,12],[18,12],[-1,-1],[3,6],[17,6]]},"$":{width:20,points:[[8,25],[8,-4],[-1,-1],[12,25],[12,-4],[-1,-1],[17,18],[15,20],[12,21],[8,21],[5,20],[3,18],[3,16],[4,14],[5,13],[7,12],[13,10],[15,9],[16,8],[17,6],[17,3],[15,1],[12,0],[8,0],[5,1],[3,3]]},"%":{width:24,points:[[21,21],[3,0],[-1,-1],[8,21],[10,19],[10,17],[9,15],[7,14],[5,14],[3,16],[3,18],[4,20],[6,21],[8,21],[10,20],[13,19],[16,19],[19,20],[21,21],[-1,-1],[17,7],[15,6],[14,4],[14,2],[16,0],[18,0],[20,1],[21,3],[21,5],[19,7],[17,7]]},"&":{width:26,points:[[23,12],[23,13],[22,14],[21,14],[20,13],[19,11],[17,6],[15,3],[13,1],[11,0],[7,0],[5,1],[4,2],[3,4],[3,6],[4,8],[5,9],[12,13],[13,14],[14,16],[14,18],[13,20],[11,21],[9,20],[8,18],[8,16],[9,13],[11,10],[16,3],[18,1],[20,0],[22,0],[23,1],[23,2]]},"'":{width:10,points:[[5,19],[4,20],[5,21],[6,20],[6,18],[5,16],[4,15]]},"(":{width:14,points:[[11,25],[9,23],[7,20],[5,16],[4,11],[4,7],[5,2],[7,-2],[9,-5],[11,-7]]},")":{width:14,points:[[3,25],[5,23],[7,20],[9,16],[10,11],[10,7],[9,2],[7,-2],[5,-5],[3,-7]]},"*":{width:16,points:[[8,21],[8,9],[-1,-1],[3,18],[13,12],[-1,-1],[13,18],[3,12]]},"+":{width:26,points:[[13,18],[13,0],[-1,-1],[4,9],[22,9]]},",":{width:10,points:[[6,1],[5,0],[4,1],[5,2],[6,1],[6,-1],[5,-3],[4,-4]]},"-":{width:18,points:[[6,9],[12,9]]},".":{width:10,points:[[5,2],[4,1],[5,0],[6,1],[5,2]]},"/":{width:22,points:[[20,25],[2,-7]]},"0":{width:20,points:[[9,21],[6,20],[4,17],[3,12],[3,9],[4,4],[6,1],[9,0],[11,0],[14,1],[16,4],[17,9],[17,12],[16,17],[14,20],[11,21],[9,21]]},"1":{width:20,points:[[6,17],[8,18],[11,21],[11,0]]},"2":{width:20,points:[[4,16],[4,17],[5,19],[6,20],[8,21],[12,21],[14,20],[15,19],[16,17],[16,15],[15,13],[13,10],[3,0],[17,0]]},"3":{width:20,points:[[5,21],[16,21],[10,13],[13,13],[15,12],[16,11],[17,8],[17,6],[16,3],[14,1],[11,0],[8,0],[5,1],[4,2],[3,4]]},"4":{width:20,points:[[13,21],[3,7],[18,7],[-1,-1],[13,21],[13,0]]},"5":{width:20,points:[[15,21],[5,21],[4,12],[5,13],[8,14],[11,14],[14,13],[16,11],[17,8],[17,6],[16,3],[14,1],[11,0],[8,0],[5,1],[4,2],[3,4]]},"6":{width:20,points:[[16,18],[15,20],[12,21],[10,21],[7,20],[5,17],[4,12],[4,7],[5,3],[7,1],[10,0],[11,0],[14,1],[16,3],[17,6],[17,7],[16,10],[14,12],[11,13],[10,13],[7,12],[5,10],[4,7]]},"7":{width:20,points:[[17,21],[7,0],[-1,-1],[3,21],[17,21]]},"8":{width:20,points:[[8,21],[5,20],[4,18],[4,16],[5,14],[7,13],[11,12],[14,11],[16,9],[17,7],[17,4],[16,2],[15,1],[12,0],[8,0],[5,1],[4,2],[3,4],[3,7],[4,9],[6,11],[9,12],[13,13],[15,14],[16,16],[16,18],[15,20],[12,21],[8,21]]},"9":{width:20,points:[[16,14],[15,11],[13,9],[10,8],[9,8],[6,9],[4,11],[3,14],[3,15],[4,18],[6,20],[9,21],[10,21],[13,20],[15,18],[16,14],[16,9],[15,4],[13,1],[10,0],[8,0],[5,1],[4,3]]},":":{width:10,points:[[5,14],[4,13],[5,12],[6,13],[5,14],[-1,-1],[5,2],[4,1],[5,0],[6,1],[5,2]]},";":{width:10,points:[[5,14],[4,13],[5,12],[6,13],[5,14],[-1,-1],[6,1],[5,0],[4,1],[5,2],[6,1],[6,-1],[5,-3],[4,-4]]},"<":{width:24,points:[[20,18],[4,9],[20,0]]},"=":{width:26,points:[[4,12],[22,12],[-1,-1],[4,6],[22,6]]},">":{width:24,points:[[4,18],[20,9],[4,0]]},"?":{width:18,points:[[3,16],[3,17],[4,19],[5,20],[7,21],[11,21],[13,20],[14,19],[15,17],[15,15],[14,13],[13,12],[9,10],[9,7],[-1,-1],[9,2],[8,1],[9,0],[10,1],[9,2]]},"@":{width:27,points:[[18,13],[17,15],[15,16],[12,16],[10,15],[9,14],[8,11],[8,8],[9,6],[11,5],[14,5],[16,6],[17,8],[-1,-1],[12,16],[10,14],[9,11],[9,8],[10,6],[11,5],[-1,-1],[18,16],[17,8],[17,6],[19,5],[21,5],[23,7],[24,10],[24,12],[23,15],[22,17],[20,19],[18,20],[15,21],[12,21],[9,20],[7,19],[5,17],[4,15],[3,12],[3,9],[4,6],[5,4],[7,2],[9,1],[12,0],[15,0],[18,1],[20,2],[21,3],[-1,-1],[19,16],[18,8],[18,6],[19,5]]},A:{width:18,points:[[9,21],[1,0],[-1,-1],[9,21],[17,0],[-1,-1],[4,7],[14,7]]},B:{width:21,points:[[4,21],[4,0],[-1,-1],[4,21],[13,21],[16,20],[17,19],[18,17],[18,15],[17,13],[16,12],[13,11],[-1,-1],[4,11],[13,11],[16,10],[17,9],[18,7],[18,4],[17,2],[16,1],[13,0],[4,0]]},C:{width:21,points:[[18,16],[17,18],[15,20],[13,21],[9,21],[7,20],[5,18],[4,16],[3,13],[3,8],[4,5],[5,3],[7,1],[9,0],[13,0],[15,1],[17,3],[18,5]]},D:{width:21,points:[[4,21],[4,0],[-1,-1],[4,21],[11,21],[14,20],[16,18],[17,16],[18,13],[18,8],[17,5],[16,3],[14,1],[11,0],[4,0]]},E:{width:19,points:[[4,21],[4,0],[-1,-1],[4,21],[17,21],[-1,-1],[4,11],[12,11],[-1,-1],[4,0],[17,0]]},F:{width:18,points:[[4,21],[4,0],[-1,-1],[4,21],[17,21],[-1,-1],[4,11],[12,11]]},G:{width:21,points:[[18,16],[17,18],[15,20],[13,21],[9,21],[7,20],[5,18],[4,16],[3,13],[3,8],[4,5],[5,3],[7,1],[9,0],[13,0],[15,1],[17,3],[18,5],[18,8],[-1,-1],[13,8],[18,8]]},H:{width:22,points:[[4,21],[4,0],[-1,-1],[18,21],[18,0],[-1,-1],[4,11],[18,11]]},I:{width:8,points:[[4,21],[4,0]]},J:{width:16,points:[[12,21],[12,5],[11,2],[10,1],[8,0],[6,0],[4,1],[3,2],[2,5],[2,7]]},K:{width:21,points:[[4,21],[4,0],[-1,-1],[18,21],[4,7],[-1,-1],[9,12],[18,0]]},L:{width:17,points:[[4,21],[4,0],[-1,-1],[4,0],[16,0]]},M:{width:24,points:[[4,21],[4,0],[-1,-1],[4,21],[12,0],[-1,-1],[20,21],[12,0],[-1,-1],[20,21],[20,0]]},N:{width:22,points:[[4,21],[4,0],[-1,-1],[4,21],[18,0],[-1,-1],[18,21],[18,0]]},O:{width:22,points:[[9,21],[7,20],[5,18],[4,16],[3,13],[3,8],[4,5],[5,3],[7,1],[9,0],[13,0],[15,1],[17,3],[18,5],[19,8],[19,13],[18,16],[17,18],[15,20],[13,21],[9,21]]},P:{width:21,points:[[4,21],[4,0],[-1,-1],[4,21],[13,21],[16,20],[17,19],[18,17],[18,14],[17,12],[16,11],[13,10],[4,10]]},Q:{width:22,points:[[9,21],[7,20],[5,18],[4,16],[3,13],[3,8],[4,5],[5,3],[7,1],[9,0],[13,0],[15,1],[17,3],[18,5],[19,8],[19,13],[18,16],[17,18],[15,20],[13,21],[9,21],[-1,-1],[12,4],[18,-2]]},R:{width:21,points:[[4,21],[4,0],[-1,-1],[4,21],[13,21],[16,20],[17,19],[18,17],[18,15],[17,13],[16,12],[13,11],[4,11],[-1,-1],[11,11],[18,0]]},S:{width:20,points:[[17,18],[15,20],[12,21],[8,21],[5,20],[3,18],[3,16],[4,14],[5,13],[7,12],[13,10],[15,9],[16,8],[17,6],[17,3],[15,1],[12,0],[8,0],[5,1],[3,3]]},T:{width:16,points:[[8,21],[8,0],[-1,-1],[1,21],[15,21]]},U:{width:22,points:[[4,21],[4,6],[5,3],[7,1],[10,0],[12,0],[15,1],[17,3],[18,6],[18,21]]},V:{width:18,points:[[1,21],[9,0],[-1,-1],[17,21],[9,0]]},W:{width:24,points:[[2,21],[7,0],[-1,-1],[12,21],[7,0],[-1,-1],[12,21],[17,0],[-1,-1],[22,21],[17,0]]},X:{width:20,points:[[3,21],[17,0],[-1,-1],[17,21],[3,0]]},Y:{width:18,points:[[1,21],[9,11],[9,0],[-1,-1],[17,21],[9,11]]},Z:{width:20,points:[[17,21],[3,0],[-1,-1],[3,21],[17,21],[-1,-1],[3,0],[17,0]]},"[":{width:14,points:[[4,25],[4,-7],[-1,-1],[5,25],[5,-7],[-1,-1],[4,25],[11,25],[-1,-1],[4,-7],[11,-7]]},"\\":{width:14,points:[[0,21],[14,-3]]},"]":{width:14,points:[[9,25],[9,-7],[-1,-1],[10,25],[10,-7],[-1,-1],[3,25],[10,25],[-1,-1],[3,-7],[10,-7]]},"^":{width:16,points:[[6,15],[8,18],[10,15],[-1,-1],[3,12],[8,17],[13,12],[-1,-1],[8,17],[8,0]]},_:{width:16,points:[[0,-2],[16,-2]]},"`":{width:10,points:[[6,21],[5,20],[4,18],[4,16],[5,15],[6,16],[5,17]]},a:{width:19,points:[[15,14],[15,0],[-1,-1],[15,11],[13,13],[11,14],[8,14],[6,13],[4,11],[3,8],[3,6],[4,3],[6,1],[8,0],[11,0],[13,1],[15,3]]},b:{width:19,points:[[4,21],[4,0],[-1,-1],[4,11],[6,13],[8,14],[11,14],[13,13],[15,11],[16,8],[16,6],[15,3],[13,1],[11,0],[8,0],[6,1],[4,3]]},c:{width:18,points:[[15,11],[13,13],[11,14],[8,14],[6,13],[4,11],[3,8],[3,6],[4,3],[6,1],[8,0],[11,0],[13,1],[15,3]]},d:{width:19,points:[[15,21],[15,0],[-1,-1],[15,11],[13,13],[11,14],[8,14],[6,13],[4,11],[3,8],[3,6],[4,3],[6,1],[8,0],[11,0],[13,1],[15,3]]},e:{width:18,points:[[3,8],[15,8],[15,10],[14,12],[13,13],[11,14],[8,14],[6,13],[4,11],[3,8],[3,6],[4,3],[6,1],[8,0],[11,0],[13,1],[15,3]]},f:{width:12,points:[[10,21],[8,21],[6,20],[5,17],[5,0],[-1,-1],[2,14],[9,14]]},g:{width:19,points:[[15,14],[15,-2],[14,-5],[13,-6],[11,-7],[8,-7],[6,-6],[-1,-1],[15,11],[13,13],[11,14],[8,14],[6,13],[4,11],[3,8],[3,6],[4,3],[6,1],[8,0],[11,0],[13,1],[15,3]]},h:{width:19,points:[[4,21],[4,0],[-1,-1],[4,10],[7,13],[9,14],[12,14],[14,13],[15,10],[15,0]]},i:{width:8,points:[[3,21],[4,20],[5,21],[4,22],[3,21],[-1,-1],[4,14],[4,0]]},j:{width:10,points:[[5,21],[6,20],[7,21],[6,22],[5,21],[-1,-1],[6,14],[6,-3],[5,-6],[3,-7],[1,-7]]},k:{width:17,points:[[4,21],[4,0],[-1,-1],[14,14],[4,4],[-1,-1],[8,8],[15,0]]},l:{width:8,points:[[4,21],[4,0]]},m:{width:30,points:[[4,14],[4,0],[-1,-1],[4,10],[7,13],[9,14],[12,14],[14,13],[15,10],[15,0],[-1,-1],[15,10],[18,13],[20,14],[23,14],[25,13],[26,10],[26,0]]},n:{width:19,points:[[4,14],[4,0],[-1,-1],[4,10],[7,13],[9,14],[12,14],[14,13],[15,10],[15,0]]},o:{width:19,points:[[8,14],[6,13],[4,11],[3,8],[3,6],[4,3],[6,1],[8,0],[11,0],[13,1],[15,3],[16,6],[16,8],[15,11],[13,13],[11,14],[8,14]]},p:{width:19,points:[[4,14],[4,-7],[-1,-1],[4,11],[6,13],[8,14],[11,14],[13,13],[15,11],[16,8],[16,6],[15,3],[13,1],[11,0],[8,0],[6,1],[4,3]]},q:{width:19,points:[[15,14],[15,-7],[-1,-1],[15,11],[13,13],[11,14],[8,14],[6,13],[4,11],[3,8],[3,6],[4,3],[6,1],[8,0],[11,0],[13,1],[15,3]]},r:{width:13,points:[[4,14],[4,0],[-1,-1],[4,8],[5,11],[7,13],[9,14],[12,14]]},s:{width:17,points:[[14,11],[13,13],[10,14],[7,14],[4,13],[3,11],[4,9],[6,8],[11,7],[13,6],[14,4],[14,3],[13,1],[10,0],[7,0],[4,1],[3,3]]},t:{width:12,points:[[5,21],[5,4],[6,1],[8,0],[10,0],[-1,-1],[2,14],[9,14]]},u:{width:19,points:[[4,14],[4,4],[5,1],[7,0],[10,0],[12,1],[15,4],[-1,-1],[15,14],[15,0]]},v:{width:16,points:[[2,14],[8,0],[-1,-1],[14,14],[8,0]]},w:{width:22,points:[[3,14],[7,0],[-1,-1],[11,14],[7,0],[-1,-1],[11,14],[15,0],[-1,-1],[19,14],[15,0]]},x:{width:17,points:[[3,14],[14,0],[-1,-1],[14,14],[3,0]]},y:{width:16,points:[[2,14],[8,0],[-1,-1],[14,14],[8,0],[6,-4],[4,-6],[2,-7],[1,-7]]},z:{width:17,points:[[14,14],[3,0],[-1,-1],[3,14],[14,14],[-1,-1],[3,0],[14,0]]},"{":{width:14,points:[[9,25],[7,24],[6,23],[5,21],[5,19],[6,17],[7,16],[8,14],[8,12],[6,10],[-1,-1],[7,24],[6,22],[6,20],[7,18],[8,17],[9,15],[9,13],[8,11],[4,9],[8,7],[9,5],[9,3],[8,1],[7,0],[6,-2],[6,-4],[7,-6],[-1,-1],[6,8],[8,6],[8,4],[7,2],[6,1],[5,-1],[5,-3],[6,-5],[7,-6],[9,-7]]},"|":{width:8,points:[[4,25],[4,-7]]},"}":{width:14,points:[[5,25],[7,24],[8,23],[9,21],[9,19],[8,17],[7,16],[6,14],[6,12],[8,10],[-1,-1],[7,24],[8,22],[8,20],[7,18],[6,17],[5,15],[5,13],[6,11],[10,9],[6,7],[5,5],[5,3],[6,1],[7,0],[8,-2],[8,-4],[7,-6],[-1,-1],[8,8],[6,6],[6,4],[7,2],[8,1],[9,-1],[9,-3],[8,-5],[7,-6],[5,-7]]},"~":{width:24,points:[[3,6],[3,8],[4,11],[6,12],[8,12],[10,11],[14,8],[16,7],[18,7],[20,8],[21,10],[-1,-1],[3,8],[4,10],[6,11],[8,11],[10,10],[14,7],[16,6],[18,6],[20,7],[21,10],[21,12]]}};a.jqplot.CanvasFontRenderer=function(b){b=b||{};if(!b.pt2px){b.pt2px=1.5}a.jqplot.CanvasTextRenderer.call(this,b)};a.jqplot.CanvasFontRenderer.prototype=new a.jqplot.CanvasTextRenderer({});a.jqplot.CanvasFontRenderer.prototype.constructor=a.jqplot.CanvasFontRenderer;a.jqplot.CanvasFontRenderer.prototype.measure=function(c,e){var d=this.fontSize+" "+this.fontFamily;c.save();c.font=d;var b=c.measureText(e).width;c.restore();return b};a.jqplot.CanvasFontRenderer.prototype.draw=function(e,g){var c=0;var h=this.height*0.72;e.save();var d,b;if((-Math.PI/2<=this.angle&&this.angle<=0)||(Math.PI*3/2<=this.angle&&this.angle<=Math.PI*2)){d=0;b=-Math.sin(this.angle)*this.width}else{if((0<this.angle&&this.angle<=Math.PI/2)||(-Math.PI*2<=this.angle&&this.angle<=-Math.PI*3/2)){d=Math.sin(this.angle)*this.height;b=0}else{if((-Math.PI<this.angle&&this.angle<-Math.PI/2)||(Math.PI<=this.angle&&this.angle<=Math.PI*3/2)){d=-Math.cos(this.angle)*this.width;b=-Math.sin(this.angle)*this.width-Math.cos(this.angle)*this.height}else{if((-Math.PI*3/2<this.angle&&this.angle<Math.PI)||(Math.PI/2<this.angle&&this.angle<Math.PI)){d=Math.sin(this.angle)*this.height-Math.cos(this.angle)*this.width;b=-Math.cos(this.angle)*this.height}}}}e.strokeStyle=this.fillStyle;e.fillStyle=this.fillStyle;var f=this.fontSize+" "+this.fontFamily;e.font=f;e.translate(d,b);e.rotate(this.angle);e.fillText(g,c,h);e.restore()}})(jQuery);/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: @VERSION
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * Although not required, the author would appreciate an email letting him
 * know of any substantial use of jqPlot.  You can reach the author at:
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 */
(function($) {
  /**
   * Class: $.jqplot.DateAxisRenderer
   * A plugin for a jqPlot to render an axis as a series of date values.
   * This renderer has no options beyond those supplied by the <Axis> class.
   * It supplies it's own tick formatter, so the tickOptions.formatter option
   * should not be overridden.
   *
   * Thanks to Ken Synder for his enhanced Date instance methods which are
   * included with this code <http://kendsnyder.com/sandbox/date/>.
   *
   * To use this renderer, include the plugin in your source
   * > <script type="text/javascript" language="javascript" src="plugins/jqplot.dateAxisRenderer.js"></script>
   *
   * and supply the appropriate options to your plot
   *
   * > {axes:{xaxis:{renderer:$.jqplot.DateAxisRenderer}}}
   *
   * Dates can be passed into the axis in almost any recognizable value and
   * will be parsed.  They will be rendered on the axis in the format
   * specified by tickOptions.formatString.  e.g. tickOptions.formatString = '%Y-%m-%d'.
   *
   * Accecptable format codes
   * are:
   *
   * > Code    Result                  Description
   * >             == Years ==
   * > %Y      2008                Four-digit year
   * > %y      08                  Two-digit year
   * >             == Months ==
   * > %m      09                  Two-digit month
   * > %#m     9                   One or two-digit month
   * > %B      September           Full month name
   * > %b      Sep                 Abbreviated month name
   * >             == Days ==
   * > %d      05                  Two-digit day of month
   * > %#d     5                   One or two-digit day of month
   * > %e      5                   One or two-digit day of month
   * > %A      Sunday              Full name of the day of the week
   * > %a      Sun                 Abbreviated name of the day of the week
   * > %w      0                   Number of the day of the week (0 = Sunday, 6 = Saturday)
   * > %o      th                  The ordinal suffix string following the day of the month
   * >             == Hours ==
   * > %H      23                  Hours in 24-hour format (two digits)
   * > %#H     3                   Hours in 24-hour integer format (one or two digits)
   * > %I      11                  Hours in 12-hour format (two digits)
   * > %#I     3                   Hours in 12-hour integer format (one or two digits)
   * > %p      PM                  AM or PM
   * >             == Minutes ==
   * > %M      09                  Minutes (two digits)
   * > %#M     9                   Minutes (one or two digits)
   * >             == Seconds ==
   * > %S      02                  Seconds (two digits)
   * > %#S     2                   Seconds (one or two digits)
   * > %s      1206567625723       Unix timestamp (Seconds past 1970-01-01 00:00:00)
   * >             == Milliseconds ==
   * > %N      008                 Milliseconds (three digits)
   * > %#N     8                   Milliseconds (one to three digits)
   * >             == Timezone ==
   * > %O      360                 difference in minutes between local time and GMT
   * > %Z      Mountain Standard Time  Name of timezone as reported by browser
   * > %G      -06:00              Hours and minutes between GMT
   * >             == Shortcuts ==
   * > %F      2008-03-26          %Y-%m-%d
   * > %T      05:06:30            %H:%M:%S
   * > %X      05:06:30            %H:%M:%S
   * > %x      03/26/08            %m/%d/%y
   * > %D      03/26/08            %m/%d/%y
   * > %#c     Wed Mar 26 15:31:00 2008  %a %b %e %H:%M:%S %Y
   * > %v      3-Sep-2008          %e-%b-%Y
   * > %R      15:31               %H:%M
   * > %r      3:31:00 PM          %I:%M:%S %p
   * >             == Characters ==
   * > %n      \n                  Newline
   * > %t      \t                  Tab
   * > %%      %                   Percent Symbol
   */
  $.jqplot.DateAxisRenderer = function() {
    $.jqplot.LinearAxisRenderer.call(this);
    this.date = new $.jsDate();
  };

  var second = 1000;
  var minute = 60 * second;
  var hour = 60 * minute;
  var day = 24 * hour;
  var week = 7 * day;

  // these are less definitive
  var month = 30.4368499 * day;
  var year = 365.242199 * day;

  var daysInMonths = [31,28,31,30,31,30,31,30,31,30,31,30];
  // array of consistent nice intervals.  Longer intervals
  // will depend on days in month, days in year, etc.
  var niceFormatStrings = ['%M:%S.%#N', '%M:%S.%#N', '%M:%S.%#N', '%M:%S', '%M:%S', '%M:%S', '%M:%S', '%H:%M:%S', '%H:%M:%S', '%H:%M', '%H:%M', '%H:%M', '%H:%M', '%H:%M', '%H:%M', '%a %H:%M', '%a %H:%M', '%b %e %H:%M', '%b %e %H:%M', '%b %e %H:%M', '%b %e %H:%M', '%v', '%v', '%v', '%v', '%v', '%v', '%v'];
  var niceIntervals = [0.1*second, 0.2*second, 0.5*second, second, 2*second, 5*second, 10*second, 15*second, 30*second, minute, 2*minute, 5*minute, 10*minute, 15*minute, 30*minute, hour, 2*hour, 4*hour, 6*hour, 8*hour, 12*hour, day, 2*day, 3*day, 4*day, 5*day, week, 2*week];

  var niceMonthlyIntervals = [];

  function bestDateInterval(min, max, titarget) {
    // iterate through niceIntervals to find one closest to titarget
    var badness = Number.MAX_VALUE;
    var temp, bestTi, bestfmt;
    for (var i=0, l=niceIntervals.length; i < l; i++) {
      temp = Math.abs(titarget - niceIntervals[i]);
      if (temp < badness) {
        badness = temp;
        bestTi = niceIntervals[i];
        bestfmt = niceFormatStrings[i];
      }
    }

    return [bestTi, bestfmt];
  }

  $.jqplot.DateAxisRenderer.prototype = new $.jqplot.LinearAxisRenderer();
  $.jqplot.DateAxisRenderer.prototype.constructor = $.jqplot.DateAxisRenderer;

  $.jqplot.DateTickFormatter = function(format, val) {
    if (!format) {
      format = '%Y/%m/%d';
    }
    return $.jsDate.strftime(val, format);
  };

  $.jqplot.DateAxisRenderer.prototype.init = function(options){
    // prop: tickRenderer
    // A class of a rendering engine for creating the ticks labels displayed on the plot,
    // See <$.jqplot.AxisTickRenderer>.
    // this.tickRenderer = $.jqplot.AxisTickRenderer;
    // this.labelRenderer = $.jqplot.AxisLabelRenderer;
    this.tickOptions.formatter = $.jqplot.DateTickFormatter;
    // prop: tickInset
    // Controls the amount to inset the first and last ticks from
    // the edges of the grid, in multiples of the tick interval.
    // 0 is no inset, 0.5 is one half a tick interval, 1 is a full
    // tick interval, etc.
    this.tickInset = 0;
    // prop: drawBaseline
    // True to draw the axis baseline.
    this.drawBaseline = true;
    // prop: baselineWidth
    // width of the baseline in pixels.
    this.baselineWidth = null;
    // prop: baselineColor
    // CSS color spec for the baseline.
    this.baselineColor = null;
    this.daTickInterval = null;
    this._daTickInterval = null;

    $.extend(true, this, options);

    var db = this._dataBounds,
        stats,
        sum,
        s,
        d,
        pd,
        sd,
        intv;

    // Go through all the series attached to this axis and find
    // the min/max bounds for this axis.
    for (var i=0; i<this._series.length; i++) {
      stats = {intervals:[], frequencies:{}, sortedIntervals:[], min:null, max:null, mean:null};
      sum = 0;
      s = this._series[i];
      d = s.data;
      pd = s._plotData;
      sd = s._stackData;
      intv = 0;

      for (var j=0; j<d.length; j++) {
        if (this.name == 'xaxis' || this.name == 'x2axis') {
          d[j][0] = new $.jsDate(d[j][0]).getTime();
          pd[j][0] = new $.jsDate(d[j][0]).getTime();
          sd[j][0] = new $.jsDate(d[j][0]).getTime();
          if ((d[j][0] != null && d[j][0] < db.min) || db.min == null) {
            db.min = d[j][0];
          }
          if ((d[j][0] != null && d[j][0] > db.max) || db.max == null) {
            db.max = d[j][0];
          }
          if (j>0) {
            intv = Math.abs(d[j][0] - d[j-1][0]);
            stats.intervals.push(intv);
            if (stats.frequencies.hasOwnProperty(intv)) {
              stats.frequencies[intv] += 1;
            }
            else {
              stats.frequencies[intv] = 1;
            }
          }
          sum += intv;

        }
        else {
          d[j][1] = new $.jsDate(d[j][1]).getTime();
          pd[j][1] = new $.jsDate(d[j][1]).getTime();
          sd[j][1] = new $.jsDate(d[j][1]).getTime();
          if ((d[j][1] != null && d[j][1] < db.min) || db.min == null) {
            db.min = d[j][1];
          }
          if ((d[j][1] != null && d[j][1] > db.max) || db.max == null) {
            db.max = d[j][1];
          }
          if (j>0) {
            intv = Math.abs(d[j][1] - d[j-1][1]);
            stats.intervals.push(intv);
            if (stats.frequencies.hasOwnProperty(intv)) {
              stats.frequencies[intv] += 1;
            }
            else {
              stats.frequencies[intv] = 1;
            }
          }
        }
        sum += intv;
      }

      if (s.renderer.bands) {
        if (s.renderer.bands.hiData.length) {
          var bd = s.renderer.bands.hiData;
          for (var j=0, l=bd.length; j < l; j++) {
            if (this.name === 'xaxis' || this.name === 'x2axis') {
              bd[j][0] = new $.jsDate(bd[j][0]).getTime();
              if ((bd[j][0] != null && bd[j][0] > db.max) || db.max == null) {
                db.max = bd[j][0];
              }
            }
            else {
              bd[j][1] = new $.jsDate(bd[j][1]).getTime();
              if ((bd[j][1] != null && bd[j][1] > db.max) || db.max == null) {
                db.max = bd[j][1];
              }
            }
          }
        }
        if (s.renderer.bands.lowData.length) {
          var bd = s.renderer.bands.lowData;
          for (var j=0, l=bd.length; j < l; j++) {
            if (this.name === 'xaxis' || this.name === 'x2axis') {
              bd[j][0] = new $.jsDate(bd[j][0]).getTime();
              if ((bd[j][0] != null && bd[j][0] < db.min) || db.min == null) {
                db.min = bd[j][0];
              }
            }
            else {
              bd[j][1] = new $.jsDate(bd[j][1]).getTime();
              if ((bd[j][1] != null && bd[j][1] < db.min) || db.min == null) {
                db.min = bd[j][1];
              }
            }
          }
        }
      }

      var tempf = 0,
          tempn=0;
      for (var n in stats.frequencies) {
        stats.sortedIntervals.push({interval:n, frequency:stats.frequencies[n]});
      }
      stats.sortedIntervals.sort(function(a, b){
        return b.frequency - a.frequency;
      });

      stats.min = $.jqplot.arrayMin(stats.intervals);
      stats.max = $.jqplot.arrayMax(stats.intervals);
      stats.mean = sum/d.length;
      this._intervalStats.push(stats);
      stats = sum = s = d = pd = sd = null;
    }
    db = null;

  };

  // called with scope of an axis
  $.jqplot.DateAxisRenderer.prototype.reset = function() {
    this.min = this._options.min;
    this.max = this._options.max;
    this.tickInterval = this._options.tickInterval;
    this.numberTicks = this._options.numberTicks;
    this._autoFormatString = '';
    if (this._overrideFormatString && this.tickOptions && this.tickOptions.formatString) {
      this.tickOptions.formatString = '';
    }
    this.daTickInterval = this._daTickInterval;
    // this._ticks = this.__ticks;
  };

  $.jqplot.DateAxisRenderer.prototype.createTicks = function(plot) {
    // we're are operating on an axis here
    var ticks = this._ticks;
    var userTicks = this.ticks;
    var name = this.name;
    // databounds were set on axis initialization.
    var db = this._dataBounds;
    var iv = this._intervalStats;
    var dim = (this.name.charAt(0) === 'x') ? this._plotDimensions.width : this._plotDimensions.height;
    var interval;
    var min, max;
    var pos1, pos2;
    var tt, i;
    var threshold = 30;
    var insetMult = 1;

    var tickInterval = this.tickInterval;

    // if we already have ticks, use them.
    // ticks must be in order of increasing value.

    min = ((this.min != null) ? new $.jsDate(this.min).getTime() : db.min);
    max = ((this.max != null) ? new $.jsDate(this.max).getTime() : db.max);

    // see if we're zooming.  if we are, don't use the min and max we're given,
    // but compute some nice ones.  They will be reset later.

    var cursor = plot.plugins.cursor;

    if (cursor && cursor._zoom && cursor._zoom.zooming) {
      this.min = null;
      this.max = null;
    }

    var range = max - min;

    if (this.tickOptions == null || !this.tickOptions.formatString) {
      this._overrideFormatString = true;
    }

    if (userTicks.length) {
      // ticks could be 1D or 2D array of [val, val, ,,,] or [[val, label], [val, label], ...] or mixed
      for (i=0; i<userTicks.length; i++){
        var ut = userTicks[i];
        var t = new this.tickRenderer(this.tickOptions);
        if (ut.constructor == Array) {
          t.value = new $.jsDate(ut[0]).getTime();
          t.label = ut[1];
          if (!this.showTicks) {
            t.showLabel = false;
            t.showMark = false;
          }
          else if (!this.showTickMarks) {
            t.showMark = false;
          }
          t.setTick(t.value, this.name);
          this._ticks.push(t);
        }

        else {
          t.value = new $.jsDate(ut).getTime();
          if (!this.showTicks) {
            t.showLabel = false;
            t.showMark = false;
          }
          else if (!this.showTickMarks) {
            t.showMark = false;
          }
          t.setTick(t.value, this.name);
          this._ticks.push(t);
        }
      }
      this.numberTicks = userTicks.length;
      this.min = this._ticks[0].value;
      this.max = this._ticks[this.numberTicks-1].value;
      this.daTickInterval = [(this.max - this.min) / (this.numberTicks - 1)/1000, 'seconds'];
    }

    ////////
    // We don't have any ticks yet, let's make some!
    ////////

    // special case when there is only one point, make three tick marks to center the point
    else if (this.min == null && this.max == null && db.min == db.max)
    {
      var onePointOpts = $.extend(true, {}, this.tickOptions, {name: this.name, value: null});
      var delta = 300000;
      this.min = db.min - delta;
      this.max = db.max + delta;
      this.numberTicks = 3;

      for(var i=this.min;i<=this.max;i+= delta)
      {
        onePointOpts.value = i;

        var t = new this.tickRenderer(onePointOpts);

        if (this._overrideFormatString && this._autoFormatString != '') {
          t.formatString = this._autoFormatString;
        }

        t.showLabel = false;
        t.showMark = false;

        this._ticks.push(t);
      }

      if(this.showTicks) {
        this._ticks[1].showLabel = true;
      }
      if(this.showTickMarks) {
        this._ticks[1].showTickMarks = true;
      }
    }
    // if user specified min and max are null, we set those to make best ticks.
    else if (this.min == null && this.max == null) {

      var opts = $.extend(true, {}, this.tickOptions, {name: this.name, value: null});

      // want to find a nice interval
      var nttarget,
          titarget;

      // if no tickInterval or numberTicks options specified,  make a good guess.
      if (!this.tickInterval && !this.numberTicks) {
        var tdim = Math.max(dim, threshold+1);
        // how many ticks to put on the axis?
        // date labels tend to be long.  If ticks not rotated,
        // don't use too many and have a high spacing factor.
        // If we are rotating ticks, use a lower factor.
        var spacingFactor = 115;
        if (this.tickRenderer === $.jqplot.CanvasAxisTickRenderer && this.tickOptions.angle) {
          spacingFactor = 115 - 40 * Math.abs(Math.sin(this.tickOptions.angle/180*Math.PI));
        }

        nttarget =  Math.ceil((tdim-threshold)/spacingFactor + 1);
        titarget = (max - min) / (nttarget - 1);
      }

      // If tickInterval is specified, we'll try to honor it.
      // Not gauranteed to get this interval, but we'll get as close as
      // we can.
      // tickInterval will be used before numberTicks, that is if
      // both are specified, numberTicks will be ignored.
      else if (this.tickInterval) {
        titarget = this.tickInterval;
      }

      // if numberTicks specified, try to honor it.
      // Not gauranteed, but will try to get close.
      else if (this.numberTicks) {
        nttarget = this.numberTicks;
        titarget = (max - min) / (nttarget - 1);
      }

      // If we can use an interval of 2 weeks or less, pick best one
      if (titarget <= 19*day) {
        var ret = bestDateInterval(min, max, titarget);
        var tempti = ret[0];
        this._autoFormatString = ret[1];

        min = Math.floor(min/tempti) * tempti;
        min = new $.jsDate(min);
        min = min.getTime() + min.getUtcOffset();

        nttarget = Math.ceil((max - min) / tempti) + 1;
        this.min = min;
        this.max = min + (nttarget - 1) * tempti;

        // if max is less than max, add an interval
        if (this.max < max) {
          this.max += tempti;
          nttarget += 1;
        }
        this.tickInterval = tempti;
        this.numberTicks = nttarget;

        for (var i=0; i<nttarget; i++) {
          opts.value = this.min + i * tempti;
          t = new this.tickRenderer(opts);

          if (this._overrideFormatString && this._autoFormatString != '') {
            t.formatString = this._autoFormatString;
          }
          if (!this.showTicks) {
            t.showLabel = false;
            t.showMark = false;
          }
          else if (!this.showTickMarks) {
            t.showMark = false;
          }
          this._ticks.push(t);
        }

        insetMult = this.tickInterval;
      }

      // should we use a monthly interval?
      else if (titarget <= 9 * month) {

        this._autoFormatString = '%v';

        // how many months in an interval?
        var intv = Math.round(titarget/month);
        if (intv < 1) {
          intv = 1;
        }
        else if (intv > 6) {
          intv = 6;
        }

        // figure out the starting month and ending month.
        var mstart = new $.jsDate(min).setDate(1).setHours(0,0,0,0);

        // See if max ends exactly on a month
        var tempmend = new $.jsDate(max);
        var mend = new $.jsDate(max).setDate(1).setHours(0,0,0,0);

        if (tempmend.getTime() !== mend.getTime()) {
          mend = mend.add(1, 'month');
        }

        var nmonths = mend.diff(mstart, 'month');

        nttarget = Math.ceil(nmonths/intv) + 1;

        this.min = mstart.getTime();
        this.max = mstart.clone().add((nttarget - 1) * intv, 'month').getTime();
        this.numberTicks = nttarget;

        for (var i=0; i<nttarget; i++) {
          if (i === 0) {
            opts.value = mstart.getTime();
          }
          else {
            opts.value = mstart.add(intv, 'month').getTime();
          }
          t = new this.tickRenderer(opts);

          if (this._overrideFormatString && this._autoFormatString != '') {
            t.formatString = this._autoFormatString;
          }
          if (!this.showTicks) {
            t.showLabel = false;
            t.showMark = false;
          }
          else if (!this.showTickMarks) {
            t.showMark = false;
          }
          this._ticks.push(t);
        }

        insetMult = intv * month;
      }

      // use yearly intervals
      else {

        this._autoFormatString = '%v';

        // how many years in an interval?
        var intv = Math.round(titarget/year);
        if (intv < 1) {
          intv = 1;
        }

        // figure out the starting and ending years.
        var mstart = new $.jsDate(min).setMonth(0, 1).setHours(0,0,0,0);
        var mend = new $.jsDate(max).add(1, 'year').setMonth(0, 1).setHours(0,0,0,0);

        var nyears = mend.diff(mstart, 'year');

        nttarget = Math.ceil(nyears/intv) + 1;

        this.min = mstart.getTime();
        this.max = mstart.clone().add((nttarget - 1) * intv, 'year').getTime();
        this.numberTicks = nttarget;

        for (var i=0; i<nttarget; i++) {
          if (i === 0) {
            opts.value = mstart.getTime();
          }
          else {
            opts.value = mstart.add(intv, 'year').getTime();
          }
          t = new this.tickRenderer(opts);

          if (this._overrideFormatString && this._autoFormatString != '') {
            t.formatString = this._autoFormatString;
          }
          if (!this.showTicks) {
            t.showLabel = false;
            t.showMark = false;
          }
          else if (!this.showTickMarks) {
            t.showMark = false;
          }
          this._ticks.push(t);
        }

        insetMult = intv * year;
      }
    }

    ////////
    // Some option(s) specified, work around that.
    ////////

    else {
      if (name == 'xaxis' || name == 'x2axis') {
        dim = this._plotDimensions.width;
      }
      else {
        dim = this._plotDimensions.height;
      }

      // if min, max and number of ticks specified, user can't specify interval.
      if (this.min != null && this.max != null && this.numberTicks != null) {
        this.tickInterval = null;
      }

      // if user specified a tick interval, convert to usable.
      if (this.tickInterval != null)
      {
        // if interval is a number or can be converted to one, use it.
        // Assume it is in SECONDS!!!
        if (Number(this.tickInterval)) {
          this.daTickInterval = [Number(this.tickInterval), 'seconds'];
        }
        // else, parse out something we can build from.
        else if (typeof this.tickInterval == "string") {
          var parts = this.tickInterval.split(' ');
          if (parts.length == 1) {
            this.daTickInterval = [1, parts[0]];
          }
          else if (parts.length == 2) {
            this.daTickInterval = [parts[0], parts[1]];
          }
        }
      }

      // if min and max are same, space them out a bit
      if (min == max) {
        var adj = 24*60*60*500;  // 1/2 day
        min -= adj;
        max += adj;
      }

      range = max - min;

      var optNumTicks = 2 + parseInt(Math.max(0, dim-100)/100, 10);


      var rmin, rmax;

      rmin = (this.min != null) ? new $.jsDate(this.min).getTime() : min - range/2*(this.padMin - 1);
      rmax = (this.max != null) ? new $.jsDate(this.max).getTime() : max + range/2*(this.padMax - 1);
      this.min = rmin;
      this.max = rmax;
      range = this.max - this.min;

      if (this.numberTicks == null){
        // if tickInterval is specified by user, we will ignore computed maximum.
        // max will be equal or greater to fit even # of ticks.
        if (this.daTickInterval != null) {
          var nc = new $.jsDate(this.max).diff(this.min, this.daTickInterval[1], true);
          this.numberTicks = Math.ceil(nc/this.daTickInterval[0]) +1;
          // this.max = new $.jsDate(this.min).add(this.numberTicks-1, this.daTickInterval[1]).getTime();
          this.max = new $.jsDate(this.min).add((this.numberTicks-1) * this.daTickInterval[0], this.daTickInterval[1]).getTime();
        }
        else if (dim > 200) {
          this.numberTicks = parseInt(3+(dim-200)/100, 10);
        }
        else {
          this.numberTicks = 2;
        }
      }

      insetMult = range / (this.numberTicks-1)/1000;

      if (this.daTickInterval == null) {
        this.daTickInterval = [insetMult, 'seconds'];
      }


      for (var i=0; i<this.numberTicks; i++){
        var min = new $.jsDate(this.min);
        tt = min.add(i*this.daTickInterval[0], this.daTickInterval[1]).getTime();
        var t = new this.tickRenderer(this.tickOptions);
        // var t = new $.jqplot.AxisTickRenderer(this.tickOptions);
        if (!this.showTicks) {
          t.showLabel = false;
          t.showMark = false;
        }
        else if (!this.showTickMarks) {
          t.showMark = false;
        }
        t.setTick(tt, this.name);
        this._ticks.push(t);
      }
    }

    if (this.tickInset) {
      this.min = this.min - this.tickInset * insetMult;
      this.max = this.max + this.tickInset * insetMult;
    }

    if (this._daTickInterval == null) {
      this._daTickInterval = this.daTickInterval;
    }

    ticks = null;
  };

})(jQuery);/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.0b2_r1012
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * Although not required, the author would appreciate an email letting him
 * know of any substantial use of jqPlot.  You can reach the author at:
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 * included jsDate library by Chris Leonello:
 *
 * Copyright (c) 2010-2011 Chris Leonello
 *
 * jsDate is currently available for use in all personal or commercial projects
 * under both the MIT and GPL version 2.0 licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * jsDate borrows many concepts and ideas from the Date Instance
 * Methods by Ken Snyder along with some parts of Ken's actual code.
 *
 * Ken's origianl Date Instance Methods and copyright notice:
 *
 * Ken Snyder (ken d snyder at gmail dot com)
 * 2008-09-10
 * version 2.0.2 (http://kendsnyder.com/sandbox/date/)
 * Creative Commons Attribution License 3.0 (http://creativecommons.org/licenses/by/3.0/)
 *
 * jqplotToImage function based on Larry Siden's export-jqplot-to-png.js.
 * Larry has generously given permission to adapt his code for inclusion
 * into jqPlot.
 *
 * Larry's original code can be found here:
 *
 * https://github.com/lsiden/export-jqplot-to-png
 *
 *
 */
(function(j){j.jqplot.Cursor=function(q){this.style="crosshair";this.previousCursor="auto";this.show=j.jqplot.config.enablePlugins;this.showTooltip=true;this.followMouse=false;this.tooltipLocation="se";this.tooltipOffset=6;this.showTooltipGridPosition=false;this.showTooltipUnitPosition=true;this.showTooltipDataPosition=false;this.tooltipFormatString="%.4P, %.4P";this.useAxesFormatters=true;this.tooltipAxisGroups=[];this.zoom=false;this.zoomProxy=false;this.zoomTarget=false;this.looseZoom=true;this.clickReset=false;this.dblClickReset=true;this.showVerticalLine=false;this.showHorizontalLine=false;this.constrainZoomTo="none";this.shapeRenderer=new j.jqplot.ShapeRenderer();this._zoom={start:[],end:[],started:false,zooming:false,isZoomed:false,axes:{start:{},end:{}},gridpos:{},datapos:{}};this._tooltipElem;this.zoomCanvas;this.cursorCanvas;this.intersectionThreshold=2;this.showCursorLegend=false;this.cursorLegendFormatString=j.jqplot.Cursor.cursorLegendFormatString;this._oldHandlers={onselectstart:null,ondrag:null,onmousedown:null};this.constrainOutsideZoom=true;this.showTooltipOutsideZoom=false;this.onGrid=false;j.extend(true,this,q)};j.jqplot.Cursor.cursorLegendFormatString="%s x:%s, y:%s";j.jqplot.Cursor.init=function(t,s,r){var q=r||{};this.plugins.cursor=new j.jqplot.Cursor(q.cursor);var u=this.plugins.cursor;if(u.show){j.jqplot.eventListenerHooks.push(["jqplotMouseEnter",b]);j.jqplot.eventListenerHooks.push(["jqplotMouseLeave",f]);j.jqplot.eventListenerHooks.push(["jqplotMouseMove",i]);if(u.showCursorLegend){r.legend=r.legend||{};r.legend.renderer=j.jqplot.CursorLegendRenderer;r.legend.formatString=this.plugins.cursor.cursorLegendFormatString;r.legend.show=true}if(u.zoom){j.jqplot.eventListenerHooks.push(["jqplotMouseDown",a]);if(u.clickReset){j.jqplot.eventListenerHooks.push(["jqplotClick",k])}if(u.dblClickReset){j.jqplot.eventListenerHooks.push(["jqplotDblClick",c])}}this.resetZoom=function(){var x=this.axes;if(!u.zoomProxy){for(var w in x){x[w].reset();x[w]._ticks=[];if(u._zoom.axes[w]!==undefined){x[w]._autoFormatString=u._zoom.axes[w].tickFormatString}}this.redraw()}else{var v=this.plugins.cursor.zoomCanvas._ctx;v.clearRect(0,0,v.canvas.width,v.canvas.height);v=null}this.plugins.cursor._zoom.isZoomed=false;this.target.trigger("jqplotResetZoom",[this,this.plugins.cursor])};if(u.showTooltipDataPosition){u.showTooltipUnitPosition=false;u.showTooltipGridPosition=false;if(q.cursor.tooltipFormatString==undefined){u.tooltipFormatString=j.jqplot.Cursor.cursorLegendFormatString}}}};j.jqplot.Cursor.postDraw=function(){var x=this.plugins.cursor;if(x.zoomCanvas){x.zoomCanvas.resetCanvas();x.zoomCanvas=null}if(x.cursorCanvas){x.cursorCanvas.resetCanvas();x.cursorCanvas=null}if(x._tooltipElem){x._tooltipElem.emptyForce();x._tooltipElem=null}if(x.zoom){x.zoomCanvas=new j.jqplot.GenericCanvas();this.eventCanvas._elem.before(x.zoomCanvas.createElement(this._gridPadding,"jqplot-zoom-canvas",this._plotDimensions,this));x.zoomCanvas.setContext()}var v=document.createElement("div");x._tooltipElem=j(v);v=null;x._tooltipElem.addClass("jqplot-cursor-tooltip");x._tooltipElem.css({position:"absolute",display:"none"});if(x.zoomCanvas){x.zoomCanvas._elem.before(x._tooltipElem)}else{this.eventCanvas._elem.before(x._tooltipElem)}if(x.showVerticalLine||x.showHorizontalLine){x.cursorCanvas=new j.jqplot.GenericCanvas();this.eventCanvas._elem.before(x.cursorCanvas.createElement(this._gridPadding,"jqplot-cursor-canvas",this._plotDimensions,this));x.cursorCanvas.setContext()}if(x.showTooltipUnitPosition){if(x.tooltipAxisGroups.length===0){var t=this.series;var u;var q=[];for(var r=0;r<t.length;r++){u=t[r];var w=u.xaxis+","+u.yaxis;if(j.inArray(w,q)==-1){q.push(w)}}for(var r=0;r<q.length;r++){x.tooltipAxisGroups.push(q[r].split(","))}}}};j.jqplot.Cursor.zoomProxy=function(v,r){var q=v.plugins.cursor;var u=r.plugins.cursor;q.zoomTarget=true;q.zoom=true;q.style="auto";q.dblClickReset=false;u.zoom=true;u.zoomProxy=true;r.target.bind("jqplotZoom",t);r.target.bind("jqplotResetZoom",s);function t(x,w,z,y,A){q.doZoom(w,z,v,A)}function s(w,x,y){v.resetZoom()}};j.jqplot.Cursor.prototype.resetZoom=function(u,v){var t=u.axes;var s=v._zoom.axes;if(!u.plugins.cursor.zoomProxy&&v._zoom.isZoomed){for(var r in t){t[r].reset();t[r]._ticks=[];t[r]._autoFormatString=s[r].tickFormatString}u.redraw();v._zoom.isZoomed=false}else{var q=v.zoomCanvas._ctx;q.clearRect(0,0,q.canvas.width,q.canvas.height);q=null}u.target.trigger("jqplotResetZoom",[u,v])};j.jqplot.Cursor.resetZoom=function(q){q.resetZoom()};j.jqplot.Cursor.prototype.doZoom=function(G,t,C,u){var I=u;var F=C.axes;var r=I._zoom.axes;var w=r.start;var s=r.end;var B,E,z,D,v,x,q,H,J;var A=C.plugins.cursor.zoomCanvas._ctx;if((I.constrainZoomTo=="none"&&Math.abs(G.x-I._zoom.start[0])>6&&Math.abs(G.y-I._zoom.start[1])>6)||(I.constrainZoomTo=="x"&&Math.abs(G.x-I._zoom.start[0])>6)||(I.constrainZoomTo=="y"&&Math.abs(G.y-I._zoom.start[1])>6)){if(!C.plugins.cursor.zoomProxy){for(var y in t){if(I._zoom.axes[y]==undefined){I._zoom.axes[y]={};I._zoom.axes[y].numberTicks=F[y].numberTicks;I._zoom.axes[y].tickInterval=F[y].tickInterval;I._zoom.axes[y].daTickInterval=F[y].daTickInterval;I._zoom.axes[y].min=F[y].min;I._zoom.axes[y].max=F[y].max;I._zoom.axes[y].tickFormatString=(F[y].tickOptions!=null)?F[y].tickOptions.formatString:""}if((I.constrainZoomTo=="none")||(I.constrainZoomTo=="x"&&y.charAt(0)=="x")||(I.constrainZoomTo=="y"&&y.charAt(0)=="y")){z=t[y];if(z!=null){if(z>w[y]){v=w[y];x=z}else{D=w[y]-z;v=z;x=w[y]}q=F[y];H=null;if(q.alignTicks){if(q.name==="x2axis"&&C.axes.xaxis.show){H=C.axes.xaxis.numberTicks}else{if(q.name.charAt(0)==="y"&&q.name!=="yaxis"&&q.name!=="yMidAxis"&&C.axes.yaxis.show){H=C.axes.yaxis.numberTicks}}}if(this.looseZoom&&(F[y].renderer.constructor===j.jqplot.LinearAxisRenderer||F[y].renderer.constructor===j.jqplot.LogAxisRenderer)){J=j.jqplot.LinearTickGenerator(v,x,q._scalefact,H);if(F[y].tickInset&&J[0]<F[y].min+F[y].tickInset*F[y].tickInterval){J[0]+=J[4];J[2]-=1}if(F[y].tickInset&&J[1]>F[y].max-F[y].tickInset*F[y].tickInterval){J[1]-=J[4];J[2]-=1}if(F[y].renderer.constructor===j.jqplot.LogAxisRenderer&&J[0]<F[y].min){J[0]+=J[4];J[2]-=1}F[y].min=J[0];F[y].max=J[1];F[y]._autoFormatString=J[3];F[y].numberTicks=J[2];F[y].tickInterval=J[4];F[y].daTickInterval=[J[4]/1000,"seconds"]}else{F[y].min=v;F[y].max=x;F[y].tickInterval=null;F[y].numberTicks=null;F[y].daTickInterval=null}F[y]._ticks=[]}}}A.clearRect(0,0,A.canvas.width,A.canvas.height);C.redraw();I._zoom.isZoomed=true;A=null}C.target.trigger("jqplotZoom",[G,t,C,u])}};j.jqplot.preInitHooks.push(j.jqplot.Cursor.init);j.jqplot.postDrawHooks.push(j.jqplot.Cursor.postDraw);function e(E,r,B){var G=B.plugins.cursor;var w="";var K=false;if(G.showTooltipGridPosition){w=E.x+", "+E.y;K=true}if(G.showTooltipUnitPosition){var D;for(var C=0;C<G.tooltipAxisGroups.length;C++){D=G.tooltipAxisGroups[C];if(K){w+="<br />"}if(G.useAxesFormatters){var A=B.axes[D[0]]._ticks[0].formatter;var q=B.axes[D[1]]._ticks[0].formatter;var H=B.axes[D[0]]._ticks[0].formatString;var v=B.axes[D[1]]._ticks[0].formatString;w+=A(H,r[D[0]])+", "+q(v,r[D[1]])}else{w+=j.jqplot.sprintf(G.tooltipFormatString,r[D[0]],r[D[1]])}K=true}}if(G.showTooltipDataPosition){var u=B.series;var J=d(B,E.x,E.y);var K=false;for(var C=0;C<u.length;C++){if(u[C].show){var y=u[C].index;var t=u[C].label.toString();var F=j.inArray(y,J.indices);var z=undefined;var x=undefined;if(F!=-1){var I=J.data[F].data;if(G.useAxesFormatters){var A=u[C]._xaxis._ticks[0].formatter;var q=u[C]._yaxis._ticks[0].formatter;var H=u[C]._xaxis._ticks[0].formatString;var v=u[C]._yaxis._ticks[0].formatString;z=A(H,I[0]);x=q(v,I[1])}else{z=I[0];x=I[1]}if(K){w+="<br />"}w+=j.jqplot.sprintf(G.tooltipFormatString,t,z,x);K=true}}}}G._tooltipElem.html(w)}function g(C,A){var E=A.plugins.cursor;var z=E.cursorCanvas._ctx;z.clearRect(0,0,z.canvas.width,z.canvas.height);if(E.showVerticalLine){E.shapeRenderer.draw(z,[[C.x,0],[C.x,z.canvas.height]])}if(E.showHorizontalLine){E.shapeRenderer.draw(z,[[0,C.y],[z.canvas.width,C.y]])}var G=d(A,C.x,C.y);if(E.showCursorLegend){var r=j(A.targetId+" td.jqplot-cursor-legend-label");for(var B=0;B<r.length;B++){var v=j(r[B]).data("seriesIndex");var t=A.series[v];var s=t.label.toString();var D=j.inArray(v,G.indices);var x=undefined;var w=undefined;if(D!=-1){var H=G.data[D].data;if(E.useAxesFormatters){var y=t._xaxis._ticks[0].formatter;var q=t._yaxis._ticks[0].formatter;var F=t._xaxis._ticks[0].formatString;var u=t._yaxis._ticks[0].formatString;x=y(F,H[0]);w=q(u,H[1])}else{x=H[0];w=H[1]}}if(A.legend.escapeHtml){j(r[B]).text(j.jqplot.sprintf(E.cursorLegendFormatString,s,x,w))}else{j(r[B]).html(j.jqplot.sprintf(E.cursorLegendFormatString,s,x,w))}}}z=null}function d(A,F,E){var B={indices:[],data:[]};var G,w,u,C,v,q,t;var z;var D=A.plugins.cursor;for(var w=0;w<A.series.length;w++){G=A.series[w];q=G.renderer;if(G.show){z=D.intersectionThreshold;if(G.showMarker){z+=G.markerRenderer.size/2}for(var v=0;v<G.gridData.length;v++){t=G.gridData[v];if(D.showVerticalLine){if(Math.abs(F-t[0])<=z){B.indices.push(w);B.data.push({seriesIndex:w,pointIndex:v,gridData:t,data:G.data[v]})}}}}}return B}function n(r,t){var v=t.plugins.cursor;var s=v._tooltipElem;switch(v.tooltipLocation){case"nw":var q=r.x+t._gridPadding.left-s.outerWidth(true)-v.tooltipOffset;var u=r.y+t._gridPadding.top-v.tooltipOffset-s.outerHeight(true);break;case"n":var q=r.x+t._gridPadding.left-s.outerWidth(true)/2;var u=r.y+t._gridPadding.top-v.tooltipOffset-s.outerHeight(true);break;case"ne":var q=r.x+t._gridPadding.left+v.tooltipOffset;var u=r.y+t._gridPadding.top-v.tooltipOffset-s.outerHeight(true);break;case"e":var q=r.x+t._gridPadding.left+v.tooltipOffset;var u=r.y+t._gridPadding.top-s.outerHeight(true)/2;break;case"se":var q=r.x+t._gridPadding.left+v.tooltipOffset;var u=r.y+t._gridPadding.top+v.tooltipOffset;break;case"s":var q=r.x+t._gridPadding.left-s.outerWidth(true)/2;var u=r.y+t._gridPadding.top+v.tooltipOffset;break;case"sw":var q=r.x+t._gridPadding.left-s.outerWidth(true)-v.tooltipOffset;var u=r.y+t._gridPadding.top+v.tooltipOffset;break;case"w":var q=r.x+t._gridPadding.left-s.outerWidth(true)-v.tooltipOffset;var u=r.y+t._gridPadding.top-s.outerHeight(true)/2;break;default:var q=r.x+t._gridPadding.left+v.tooltipOffset;var u=r.y+t._gridPadding.top+v.tooltipOffset;break}s.css("left",q);s.css("top",u);s=null}function m(u){var s=u._gridPadding;var v=u.plugins.cursor;var t=v._tooltipElem;switch(v.tooltipLocation){case"nw":var r=s.left+v.tooltipOffset;var q=s.top+v.tooltipOffset;t.css("left",r);t.css("top",q);break;case"n":var r=(s.left+(u._plotDimensions.width-s.right))/2-t.outerWidth(true)/2;var q=s.top+v.tooltipOffset;t.css("left",r);t.css("top",q);break;case"ne":var r=s.right+v.tooltipOffset;var q=s.top+v.tooltipOffset;t.css({right:r,top:q});break;case"e":var r=s.right+v.tooltipOffset;var q=(s.top+(u._plotDimensions.height-s.bottom))/2-t.outerHeight(true)/2;t.css({right:r,top:q});break;case"se":var r=s.right+v.tooltipOffset;var q=s.bottom+v.tooltipOffset;t.css({right:r,bottom:q});break;case"s":var r=(s.left+(u._plotDimensions.width-s.right))/2-t.outerWidth(true)/2;var q=s.bottom+v.tooltipOffset;t.css({left:r,bottom:q});break;case"sw":var r=s.left+v.tooltipOffset;var q=s.bottom+v.tooltipOffset;t.css({left:r,bottom:q});break;case"w":var r=s.left+v.tooltipOffset;var q=(s.top+(u._plotDimensions.height-s.bottom))/2-t.outerHeight(true)/2;t.css({left:r,top:q});break;default:var r=s.right-v.tooltipOffset;var q=s.bottom+v.tooltipOffset;t.css({right:r,bottom:q});break}t=null}function k(r,q,v,u,t){r.preventDefault();r.stopImmediatePropagation();var w=t.plugins.cursor;if(w.clickReset){w.resetZoom(t,w)}var s=window.getSelection;if(document.selection&&document.selection.empty){document.selection.empty()}else{if(s&&!s().isCollapsed){s().collapse()}}return false}function c(r,q,v,u,t){r.preventDefault();r.stopImmediatePropagation();var w=t.plugins.cursor;if(w.dblClickReset){w.resetZoom(t,w)}var s=window.getSelection;if(document.selection&&document.selection.empty){document.selection.empty()}else{if(s&&!s().isCollapsed){s().collapse()}}return false}function f(w,t,q,z,u){var v=u.plugins.cursor;v.onGrid=false;if(v.show){j(w.target).css("cursor",v.previousCursor);if(v.showTooltip&&!(v._zoom.zooming&&v.showTooltipOutsideZoom&&!v.constrainOutsideZoom)){v._tooltipElem.hide()}if(v.zoom){v._zoom.gridpos=t;v._zoom.datapos=q}if(v.showVerticalLine||v.showHorizontalLine){var B=v.cursorCanvas._ctx;B.clearRect(0,0,B.canvas.width,B.canvas.height);B=null}if(v.showCursorLegend){var A=j(u.targetId+" td.jqplot-cursor-legend-label");for(var s=0;s<A.length;s++){var y=j(A[s]).data("seriesIndex");var r=u.series[y];var x=r.label.toString();if(u.legend.escapeHtml){j(A[s]).text(j.jqplot.sprintf(v.cursorLegendFormatString,x,undefined,undefined))}else{j(A[s]).html(j.jqplot.sprintf(v.cursorLegendFormatString,x,undefined,undefined))}}}}}function b(r,q,u,t,s){var v=s.plugins.cursor;v.onGrid=true;if(v.show){v.previousCursor=r.target.style.cursor;r.target.style.cursor=v.style;if(v.showTooltip){e(q,u,s);if(v.followMouse){n(q,s)}else{m(s)}v._tooltipElem.show()}if(v.showVerticalLine||v.showHorizontalLine){g(q,s)}}}function i(r,q,u,t,s){var v=s.plugins.cursor;if(v.show){if(v.showTooltip){e(q,u,s);if(v.followMouse){n(q,s)}}if(v.showVerticalLine||v.showHorizontalLine){g(q,s)}}}function o(y){var x=y.data.plot;var t=x.eventCanvas._elem.offset();var w={x:y.pageX-t.left,y:y.pageY-t.top};var u={xaxis:null,yaxis:null,x2axis:null,y2axis:null,y3axis:null,y4axis:null,y5axis:null,y6axis:null,y7axis:null,y8axis:null,y9axis:null,yMidAxis:null};var v=["xaxis","yaxis","x2axis","y2axis","y3axis","y4axis","y5axis","y6axis","y7axis","y8axis","y9axis","yMidAxis"];var q=x.axes;var r,s;for(r=11;r>0;r--){s=v[r-1];if(q[s].show){u[s]=q[s].series_p2u(w[s.charAt(0)])}}return{offsets:t,gridPos:w,dataPos:u}}function h(z){var x=z.data.plot;var y=x.plugins.cursor;if(y.show&&y.zoom&&y._zoom.started&&!y.zoomTarget){var B=y.zoomCanvas._ctx;var v=o(z);var w=v.gridPos;var t=v.dataPos;y._zoom.gridpos=w;y._zoom.datapos=t;y._zoom.zooming=true;var u=w.x;var s=w.y;var A=B.canvas.height;var q=B.canvas.width;if(y.showTooltip&&!y.onGrid&&y.showTooltipOutsideZoom){e(w,t,x);if(y.followMouse){n(w,x)}}if(y.constrainZoomTo=="x"){y._zoom.end=[u,A]}else{if(y.constrainZoomTo=="y"){y._zoom.end=[q,s]}else{y._zoom.end=[u,s]}}var r=window.getSelection;if(document.selection&&document.selection.empty){document.selection.empty()}else{if(r&&!r().isCollapsed){r().collapse()}}l.call(y);B=null}}function a(w,s,r,x,t){var v=t.plugins.cursor;j(document).one("mouseup.jqplot_cursor",{plot:t},p);var u=t.axes;if(document.onselectstart!=undefined){v._oldHandlers.onselectstart=document.onselectstart;document.onselectstart=function(){return false}}if(document.ondrag!=undefined){v._oldHandlers.ondrag=document.ondrag;document.ondrag=function(){return false}}if(document.onmousedown!=undefined){v._oldHandlers.onmousedown=document.onmousedown;document.onmousedown=function(){return false}}if(v.zoom){if(!v.zoomProxy){var y=v.zoomCanvas._ctx;y.clearRect(0,0,y.canvas.width,y.canvas.height);y=null}if(v.constrainZoomTo=="x"){v._zoom.start=[s.x,0]}else{if(v.constrainZoomTo=="y"){v._zoom.start=[0,s.y]}else{v._zoom.start=[s.x,s.y]}}v._zoom.started=true;for(var q in r){v._zoom.axes.start[q]=r[q]}j(document).bind("mousemove.jqplotCursor",{plot:t},h)}}function p(y){var v=y.data.plot;var x=v.plugins.cursor;if(x.zoom&&x._zoom.zooming&&!x.zoomTarget){var u=x._zoom.gridpos.x;var r=x._zoom.gridpos.y;var t=x._zoom.datapos;var z=x.zoomCanvas._ctx.canvas.height;var q=x.zoomCanvas._ctx.canvas.width;var w=v.axes;if(x.constrainOutsideZoom&&!x.onGrid){if(u<0){u=0}else{if(u>q){u=q}}if(r<0){r=0}else{if(r>z){r=z}}for(var s in t){if(t[s]){if(s.charAt(0)=="x"){t[s]=w[s].series_p2u(u)}else{t[s]=w[s].series_p2u(r)}}}}if(x.constrainZoomTo=="x"){r=z}else{if(x.constrainZoomTo=="y"){u=q}}x._zoom.end=[u,r];x._zoom.gridpos={x:u,y:r};x.doZoom(x._zoom.gridpos,t,v,x)}x._zoom.started=false;x._zoom.zooming=false;j(document).unbind("mousemove.jqplotCursor",h);if(document.onselectstart!=undefined&&x._oldHandlers.onselectstart!=null){document.onselectstart=x._oldHandlers.onselectstart;x._oldHandlers.onselectstart=null}if(document.ondrag!=undefined&&x._oldHandlers.ondrag!=null){document.ondrag=x._oldHandlers.ondrag;x._oldHandlers.ondrag=null}if(document.onmousedown!=undefined&&x._oldHandlers.onmousedown!=null){document.onmousedown=x._oldHandlers.onmousedown;x._oldHandlers.onmousedown=null}}function l(){var y=this._zoom.start;var u=this._zoom.end;var s=this.zoomCanvas._ctx;var r,v,x,q;if(u[0]>y[0]){r=y[0];q=u[0]-y[0]}else{r=u[0];q=y[0]-u[0]}if(u[1]>y[1]){v=y[1];x=u[1]-y[1]}else{v=u[1];x=y[1]-u[1]}s.fillStyle="rgba(0,0,0,0.2)";s.strokeStyle="#999999";s.lineWidth=1;s.clearRect(0,0,s.canvas.width,s.canvas.height);s.fillRect(0,0,s.canvas.width,s.canvas.height);s.clearRect(r,v,q,x);s.strokeRect(r,v,q,x);s=null}j.jqplot.CursorLegendRenderer=function(q){j.jqplot.TableLegendRenderer.call(this,q);this.formatString="%s"};j.jqplot.CursorLegendRenderer.prototype=new j.jqplot.TableLegendRenderer();j.jqplot.CursorLegendRenderer.prototype.constructor=j.jqplot.CursorLegendRenderer;j.jqplot.CursorLegendRenderer.prototype.draw=function(){if(this._elem){this._elem.emptyForce();this._elem=null}if(this.show){var w=this._series,A;var r=document.createElement("div");this._elem=j(r);r=null;this._elem.addClass("jqplot-legend jqplot-cursor-legend");this._elem.css("position","absolute");var q=false;for(var x=0;x<w.length;x++){A=w[x];if(A.show&&A.showLabel){var v=j.jqplot.sprintf(this.formatString,A.label.toString());if(v){var t=A.color;if(A._stack&&!A.fill){t=""}z.call(this,v,t,q,x);q=true}for(var u=0;u<j.jqplot.addLegendRowHooks.length;u++){var y=j.jqplot.addLegendRowHooks[u].call(this,A);if(y){z.call(this,y.label,y.color,q);q=true}}}}w=A=null;delete w;delete A}function z(D,C,F,s){var B=(F)?this.rowSpacing:"0";var E=j('<tr class="jqplot-legend jqplot-cursor-legend"></tr>').appendTo(this._elem);E.data("seriesIndex",s);j('<td class="jqplot-legend jqplot-cursor-legend-swatch" style="padding-top:'+B+';"><div style="border:1px solid #cccccc;padding:0.2em;"><div class="jqplot-cursor-legend-swatch" style="background-color:'+C+';"></div></div></td>').appendTo(E);var G=j('<td class="jqplot-legend jqplot-cursor-legend-label" style="vertical-align:middle;padding-top:'+B+';"></td>');G.appendTo(E);G.data("seriesIndex",s);if(this.escapeHtml){G.text(D)}else{G.html(D)}E=null;G=null}return this._elem}})(jQuery);/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.0b2_r1012
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * Although not required, the author would appreciate an email letting him
 * know of any substantial use of jqPlot.  You can reach the author at:
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 * included jsDate library by Chris Leonello:
 *
 * Copyright (c) 2010-2011 Chris Leonello
 *
 * jsDate is currently available for use in all personal or commercial projects
 * under both the MIT and GPL version 2.0 licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * jsDate borrows many concepts and ideas from the Date Instance
 * Methods by Ken Snyder along with some parts of Ken's actual code.
 *
 * Ken's origianl Date Instance Methods and copyright notice:
 *
 * Ken Snyder (ken d snyder at gmail dot com)
 * 2008-09-10
 * version 2.0.2 (http://kendsnyder.com/sandbox/date/)
 * Creative Commons Attribution License 3.0 (http://creativecommons.org/licenses/by/3.0/)
 *
 * jqplotToImage function based on Larry Siden's export-jqplot-to-png.js.
 * Larry has generously given permission to adapt his code for inclusion
 * into jqPlot.
 *
 * Larry's original code can be found here:
 *
 * https://github.com/lsiden/export-jqplot-to-png
 *
 *
 */
(function(d){d.jqplot.BarRenderer=function(){d.jqplot.LineRenderer.call(this)};d.jqplot.BarRenderer.prototype=new d.jqplot.LineRenderer();d.jqplot.BarRenderer.prototype.constructor=d.jqplot.BarRenderer;d.jqplot.BarRenderer.prototype.init=function(n,p){this.barPadding=8;this.barMargin=10;this.barDirection="vertical";this.barWidth=null;this.shadowOffset=2;this.shadowDepth=5;this.shadowAlpha=0.08;this.waterfall=false;this.groups=1;this.varyBarColor=false;this.highlightMouseOver=true;this.highlightMouseDown=false;this.highlightColors=[];this.transposedData=true;this.renderer.animation={show:false,direction:"down",speed:3000,_supported:true};this._type="bar";if(n.highlightMouseDown&&n.highlightMouseOver==null){n.highlightMouseOver=false}d.extend(true,this,n);d.extend(true,this.renderer,n);this.fill=true;if(this.barDirection==="horizontal"&&this.rendererOptions.animation&&this.rendererOptions.animation.direction==null){this.renderer.animation.direction="left"}if(this.waterfall){this.fillToZero=false;this.disableStack=true}if(this.barDirection=="vertical"){this._primaryAxis="_xaxis";this._stackAxis="y";this.fillAxis="y"}else{this._primaryAxis="_yaxis";this._stackAxis="x";this.fillAxis="x"}this._highlightedPoint=null;this._plotSeriesInfo=null;this._dataColors=[];this._barPoints=[];var o={lineJoin:"miter",lineCap:"round",fill:true,isarc:false,strokeStyle:this.color,fillStyle:this.color,closePath:this.fill};this.renderer.shapeRenderer.init(o);var m={lineJoin:"miter",lineCap:"round",fill:true,isarc:false,angle:this.shadowAngle,offset:this.shadowOffset,alpha:this.shadowAlpha,depth:this.shadowDepth,closePath:this.fill};this.renderer.shadowRenderer.init(m);p.postInitHooks.addOnce(h);p.postDrawHooks.addOnce(i);p.eventListenerHooks.addOnce("jqplotMouseMove",b);p.eventListenerHooks.addOnce("jqplotMouseDown",a);p.eventListenerHooks.addOnce("jqplotMouseUp",k);p.eventListenerHooks.addOnce("jqplotClick",e);p.eventListenerHooks.addOnce("jqplotRightClick",l)};function g(s,o,n,v){if(this.rendererOptions.barDirection=="horizontal"){this._stackAxis="x";this._primaryAxis="_yaxis"}if(this.rendererOptions.waterfall==true){this._data=d.extend(true,[],this.data);var r=0;var t=(!this.rendererOptions.barDirection||this.rendererOptions.barDirection==="vertical"||this.transposedData===false)?1:0;for(var p=0;p<this.data.length;p++){r+=this.data[p][t];if(p>0){this.data[p][t]+=this.data[p-1][t]}}this.data[this.data.length]=(t==1)?[this.data.length+1,r]:[r,this.data.length+1];this._data[this._data.length]=(t==1)?[this._data.length+1,r]:[r,this._data.length+1]}if(this.rendererOptions.groups>1){this.breakOnNull=true;var m=this.data.length;var u=parseInt(m/this.rendererOptions.groups,10);var q=0;for(var p=u;p<m;p+=u){this.data.splice(p+q,0,[null,null]);q++}for(p=0;p<this.data.length;p++){if(this._primaryAxis=="_xaxis"){this.data[p][0]=p+1}else{this.data[p][1]=p+1}}}}d.jqplot.preSeriesInitHooks.push(g);d.jqplot.BarRenderer.prototype.calcSeriesNumbers=function(){var q=0;var r=0;var p=this[this._primaryAxis];var o,n,t;for(var m=0;m<p._series.length;m++){n=p._series[m];if(n===this){t=m}if(n.renderer.constructor==d.jqplot.BarRenderer){q+=n.data.length;r+=1}}return[q,r,t]};d.jqplot.BarRenderer.prototype.setBarWidth=function(){var p;var m=0;var n=0;var r=this[this._primaryAxis];var w,q,u;var v=this._plotSeriesInfo=this.renderer.calcSeriesNumbers.call(this);m=v[0];n=v[1];var t=r.numberTicks;var o=(t-1)/2;if(r.name=="xaxis"||r.name=="x2axis"){if(this._stack){this.barWidth=(r._offsets.max-r._offsets.min)/m*n-this.barMargin}else{this.barWidth=((r._offsets.max-r._offsets.min)/o-this.barPadding*(n-1)-this.barMargin*2)/n}}else{if(this._stack){this.barWidth=(r._offsets.min-r._offsets.max)/m*n-this.barMargin}else{this.barWidth=((r._offsets.min-r._offsets.max)/o-this.barPadding*(n-1)-this.barMargin*2)/n}}return[m,n]};function f(n){var p=[];for(var r=0;r<n.length;r++){var q=d.jqplot.getColorComponents(n[r]);var m=[q[0],q[1],q[2]];var s=m[0]+m[1]+m[2];for(var o=0;o<3;o++){m[o]=(s>570)?m[o]*0.8:m[o]+0.3*(255-m[o]);m[o]=parseInt(m[o],10)}p.push("rgb("+m[0]+","+m[1]+","+m[2]+")")}return p}d.jqplot.BarRenderer.prototype.draw=function(D,J,p){var G;var z=d.extend({},p);var u=(z.shadow!=undefined)?z.shadow:this.shadow;var M=(z.showLine!=undefined)?z.showLine:this.showLine;var E=(z.fill!=undefined)?z.fill:this.fill;var o=this.xaxis;var H=this.yaxis;var x=this._xaxis.series_u2p;var I=this._yaxis.series_u2p;var C,B;this._dataColors=[];this._barPoints=[];if(this.barWidth==null){this.renderer.setBarWidth.call(this)}var L=this._plotSeriesInfo=this.renderer.calcSeriesNumbers.call(this);var w=L[0];var v=L[1];var r=L[2];var F=[];if(this._stack){this._barNudge=0}else{this._barNudge=(-Math.abs(v/2-0.5)+r)*(this.barWidth+this.barPadding)}if(M){var t=new d.jqplot.ColorGenerator(this.negativeSeriesColors);var A=new d.jqplot.ColorGenerator(this.seriesColors);var K=t.get(this.index);if(!this.useNegativeColors){K=z.fillStyle}var s=z.fillStyle;var q;var N;var n;if(this.barDirection=="vertical"){for(var G=0;G<J.length;G++){if(this.data[G][1]==null){continue}F=[];q=J[G][0]+this._barNudge;n;if(this._stack&&this._prevGridData.length){n=this._prevGridData[G][1]}else{if(this.fillToZero){n=this._yaxis.series_u2p(0)}else{if(this.waterfall&&G>0&&G<this.gridData.length-1){n=this.gridData[G-1][1]}else{if(this.waterfall&&G==0&&G<this.gridData.length-1){if(this._yaxis.min<=0&&this._yaxis.max>=0){n=this._yaxis.series_u2p(0)}else{if(this._yaxis.min>0){n=D.canvas.height}else{n=0}}}else{if(this.waterfall&&G==this.gridData.length-1){if(this._yaxis.min<=0&&this._yaxis.max>=0){n=this._yaxis.series_u2p(0)}else{if(this._yaxis.min>0){n=D.canvas.height}else{n=0}}}else{n=D.canvas.height}}}}}if((this.fillToZero&&this._plotData[G][1]<0)||(this.waterfall&&this._data[G][1]<0)){if(this.varyBarColor&&!this._stack){if(this.useNegativeColors){z.fillStyle=t.next()}else{z.fillStyle=A.next()}}else{z.fillStyle=K}}else{if(this.varyBarColor&&!this._stack){z.fillStyle=A.next()}else{z.fillStyle=s}}if(!this.fillToZero||this._plotData[G][1]>=0){F.push([q-this.barWidth/2,n]);F.push([q-this.barWidth/2,J[G][1]]);F.push([q+this.barWidth/2,J[G][1]]);F.push([q+this.barWidth/2,n])}else{F.push([q-this.barWidth/2,J[G][1]]);F.push([q-this.barWidth/2,n]);F.push([q+this.barWidth/2,n]);F.push([q+this.barWidth/2,J[G][1]])}this._barPoints.push(F);if(u&&!this._stack){var y=d.extend(true,{},z);delete y.fillStyle;this.renderer.shadowRenderer.draw(D,F,y)}var m=z.fillStyle||this.color;this._dataColors.push(m);this.renderer.shapeRenderer.draw(D,F,z)}}else{if(this.barDirection=="horizontal"){for(var G=0;G<J.length;G++){if(this.data[G][0]==null){continue}F=[];q=J[G][1]-this._barNudge;N;if(this._stack&&this._prevGridData.length){N=this._prevGridData[G][0]}else{if(this.fillToZero){N=this._xaxis.series_u2p(0)}else{if(this.waterfall&&G>0&&G<this.gridData.length-1){N=this.gridData[G-1][1]}else{if(this.waterfall&&G==0&&G<this.gridData.length-1){if(this._xaxis.min<=0&&this._xaxis.max>=0){N=this._xaxis.series_u2p(0)}else{if(this._xaxis.min>0){N=0}else{N=D.canvas.width}}}else{if(this.waterfall&&G==this.gridData.length-1){if(this._xaxis.min<=0&&this._xaxis.max>=0){N=this._xaxis.series_u2p(0)}else{if(this._xaxis.min>0){N=0}else{N=D.canvas.width}}}else{N=0}}}}}if((this.fillToZero&&this._plotData[G][1]<0)||(this.waterfall&&this._data[G][1]<0)){if(this.varyBarColor&&!this._stack){if(this.useNegativeColors){z.fillStyle=t.next()}else{z.fillStyle=A.next()}}}else{if(this.varyBarColor&&!this._stack){z.fillStyle=A.next()}else{z.fillStyle=s}}if(!this.fillToZero||this._plotData[G][0]>=0){F.push([N,q+this.barWidth/2]);F.push([N,q-this.barWidth/2]);F.push([J[G][0],q-this.barWidth/2]);F.push([J[G][0],q+this.barWidth/2])}else{F.push([J[G][0],q+this.barWidth/2]);F.push([J[G][0],q-this.barWidth/2]);F.push([N,q-this.barWidth/2]);F.push([N,q+this.barWidth/2])}this._barPoints.push(F);if(u&&!this._stack){var y=d.extend(true,{},z);delete y.fillStyle;this.renderer.shadowRenderer.draw(D,F,y)}var m=z.fillStyle||this.color;this._dataColors.push(m);this.renderer.shapeRenderer.draw(D,F,z)}}}}if(this.highlightColors.length==0){this.highlightColors=d.jqplot.computeHighlightColors(this._dataColors)}else{if(typeof(this.highlightColors)=="string"){var L=this.highlightColors;this.highlightColors=[];for(var G=0;G<this._dataColors.length;G++){this.highlightColors.push(L)}}}};d.jqplot.BarRenderer.prototype.drawShadow=function(y,E,o){var B;var v=(o!=undefined)?o:{};var r=(v.shadow!=undefined)?v.shadow:this.shadow;var G=(v.showLine!=undefined)?v.showLine:this.showLine;var z=(v.fill!=undefined)?v.fill:this.fill;var n=this.xaxis;var C=this.yaxis;var u=this._xaxis.series_u2p;var D=this._yaxis.series_u2p;var x,A,w,t,s,q;if(this._stack&&this.shadow){if(this.barWidth==null){this.renderer.setBarWidth.call(this)}var F=this._plotSeriesInfo=this.renderer.calcSeriesNumbers.call(this);t=F[0];s=F[1];q=F[2];if(this._stack){this._barNudge=0}else{this._barNudge=(-Math.abs(s/2-0.5)+q)*(this.barWidth+this.barPadding)}if(G){if(this.barDirection=="vertical"){for(var B=0;B<E.length;B++){if(this.data[B][1]==null){continue}A=[];var p=E[B][0]+this._barNudge;var m;if(this._stack&&this._prevGridData.length){m=this._prevGridData[B][1]}else{if(this.fillToZero){m=this._yaxis.series_u2p(0)}else{m=y.canvas.height}}A.push([p-this.barWidth/2,m]);A.push([p-this.barWidth/2,E[B][1]]);A.push([p+this.barWidth/2,E[B][1]]);A.push([p+this.barWidth/2,m]);this.renderer.shadowRenderer.draw(y,A,v)}}else{if(this.barDirection=="horizontal"){for(var B=0;B<E.length;B++){if(this.data[B][0]==null){continue}A=[];var p=E[B][1]-this._barNudge;var H;if(this._stack&&this._prevGridData.length){H=this._prevGridData[B][0]}else{H=0}A.push([H,p+this.barWidth/2]);A.push([E[B][0],p+this.barWidth/2]);A.push([E[B][0],p-this.barWidth/2]);A.push([H,p-this.barWidth/2]);this.renderer.shadowRenderer.draw(y,A,v)}}}}}};function h(p,o,m){for(var n=0;n<this.series.length;n++){if(this.series[n].renderer.constructor==d.jqplot.BarRenderer){if(this.series[n].highlightMouseOver){this.series[n].highlightMouseDown=false}}}}function i(){if(this.plugins.barRenderer&&this.plugins.barRenderer.highlightCanvas){this.plugins.barRenderer.highlightCanvas.resetCanvas();this.plugins.barRenderer.highlightCanvas=null}this.plugins.barRenderer={highlightedSeriesIndex:null};this.plugins.barRenderer.highlightCanvas=new d.jqplot.GenericCanvas();this.eventCanvas._elem.before(this.plugins.barRenderer.highlightCanvas.createElement(this._gridPadding,"jqplot-barRenderer-highlight-canvas",this._plotDimensions,this));this.plugins.barRenderer.highlightCanvas.setContext();this.eventCanvas._elem.bind("mouseleave",{plot:this},function(m){j(m.data.plot)})}function c(t,r,p,o){var n=t.series[r];var m=t.plugins.barRenderer.highlightCanvas;m._ctx.clearRect(0,0,m._ctx.canvas.width,m._ctx.canvas.height);n._highlightedPoint=p;t.plugins.barRenderer.highlightedSeriesIndex=r;var q={fillStyle:n.highlightColors[p]};n.renderer.shapeRenderer.draw(m._ctx,o,q);m=null}function j(o){var m=o.plugins.barRenderer.highlightCanvas;m._ctx.clearRect(0,0,m._ctx.canvas.width,m._ctx.canvas.height);for(var n=0;n<o.series.length;n++){o.series[n]._highlightedPoint=null}o.plugins.barRenderer.highlightedSeriesIndex=null;o.target.trigger("jqplotDataUnhighlight");m=null}function b(q,p,t,s,r){if(s){var o=[s.seriesIndex,s.pointIndex,s.data];var n=jQuery.Event("jqplotDataMouseOver");n.pageX=q.pageX;n.pageY=q.pageY;r.target.trigger(n,o);if(r.series[o[0]].highlightMouseOver&&!(o[0]==r.plugins.barRenderer.highlightedSeriesIndex&&o[1]==r.series[o[0]]._highlightedPoint)){var m=jQuery.Event("jqplotDataHighlight");m.pageX=q.pageX;m.pageY=q.pageY;r.target.trigger(m,o);c(r,s.seriesIndex,s.pointIndex,s.points)}}else{if(s==null){j(r)}}}function a(p,o,s,r,q){if(r){var n=[r.seriesIndex,r.pointIndex,r.data];if(q.series[n[0]].highlightMouseDown&&!(n[0]==q.plugins.barRenderer.highlightedSeriesIndex&&n[1]==q.series[n[0]]._highlightedPoint)){var m=jQuery.Event("jqplotDataHighlight");m.pageX=p.pageX;m.pageY=p.pageY;q.target.trigger(m,n);c(q,r.seriesIndex,r.pointIndex,r.points)}}else{if(r==null){j(q)}}}function k(o,n,r,q,p){var m=p.plugins.barRenderer.highlightedSeriesIndex;if(m!=null&&p.series[m].highlightMouseDown){j(p)}}function e(p,o,s,r,q){if(r){var n=[r.seriesIndex,r.pointIndex,r.data];var m=jQuery.Event("jqplotDataClick");m.pageX=p.pageX;m.pageY=p.pageY;q.target.trigger(m,n)}}function l(q,p,t,s,r){if(s){var o=[s.seriesIndex,s.pointIndex,s.data];var m=r.plugins.barRenderer.highlightedSeriesIndex;if(m!=null&&r.series[m].highlightMouseDown){j(r)}var n=jQuery.Event("jqplotDataRightClick");n.pageX=q.pageX;n.pageY=q.pageY;r.target.trigger(n,o)}}})(jQuery);/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.0b2_r1012
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * Although not required, the author would appreciate an email letting him
 * know of any substantial use of jqPlot.  You can reach the author at:
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 * included jsDate library by Chris Leonello:
 *
 * Copyright (c) 2010-2011 Chris Leonello
 *
 * jsDate is currently available for use in all personal or commercial projects
 * under both the MIT and GPL version 2.0 licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * jsDate borrows many concepts and ideas from the Date Instance
 * Methods by Ken Snyder along with some parts of Ken's actual code.
 *
 * Ken's origianl Date Instance Methods and copyright notice:
 *
 * Ken Snyder (ken d snyder at gmail dot com)
 * 2008-09-10
 * version 2.0.2 (http://kendsnyder.com/sandbox/date/)
 * Creative Commons Attribution License 3.0 (http://creativecommons.org/licenses/by/3.0/)
 *
 * jqplotToImage function based on Larry Siden's export-jqplot-to-png.js.
 * Larry has generously given permission to adapt his code for inclusion
 * into jqPlot.
 *
 * Larry's original code can be found here:
 *
 * https://github.com/lsiden/export-jqplot-to-png
 *
 *
 */
(function(a){a.jqplot.CategoryAxisRenderer=function(b){a.jqplot.LinearAxisRenderer.call(this);this.sortMergedLabels=false};a.jqplot.CategoryAxisRenderer.prototype=new a.jqplot.LinearAxisRenderer();a.jqplot.CategoryAxisRenderer.prototype.constructor=a.jqplot.CategoryAxisRenderer;a.jqplot.CategoryAxisRenderer.prototype.init=function(e){this.groups=1;this.groupLabels=[];this._groupLabels=[];this._grouped=false;this._barsPerGroup=null;a.extend(true,this,{tickOptions:{formatString:"%d"}},e);var b=this._dataBounds;for(var f=0;f<this._series.length;f++){var g=this._series[f];if(g.groups){this.groups=g.groups}var h=g.data;for(var c=0;c<h.length;c++){if(this.name=="xaxis"||this.name=="x2axis"){if(h[c][0]<b.min||b.min==null){b.min=h[c][0]}if(h[c][0]>b.max||b.max==null){b.max=h[c][0]}}else{if(h[c][1]<b.min||b.min==null){b.min=h[c][1]}if(h[c][1]>b.max||b.max==null){b.max=h[c][1]}}}}if(this.groupLabels.length){this.groups=this.groupLabels.length}};a.jqplot.CategoryAxisRenderer.prototype.createTicks=function(){var D=this._ticks;var z=this.ticks;var F=this.name;var C=this._dataBounds;var v,A;var q,w;var d,c;var b,x;if(z.length){if(this.groups>1&&!this._grouped){var r=z.length;var p=parseInt(r/this.groups,10);var e=0;for(var x=p;x<r;x+=p){z.splice(x+e,0," ");e++}this._grouped=true}this.min=0.5;this.max=z.length+0.5;var m=this.max-this.min;this.numberTicks=2*z.length+1;for(x=0;x<z.length;x++){b=this.min+2*x*m/(this.numberTicks-1);var h=new this.tickRenderer(this.tickOptions);h.showLabel=false;h.setTick(b,this.name);this._ticks.push(h);var h=new this.tickRenderer(this.tickOptions);h.label=z[x];h.showMark=false;h.showGridline=false;h.setTick(b+0.5,this.name);this._ticks.push(h)}var h=new this.tickRenderer(this.tickOptions);h.showLabel=false;h.setTick(b+1,this.name);this._ticks.push(h)}else{if(F=="xaxis"||F=="x2axis"){v=this._plotDimensions.width}else{v=this._plotDimensions.height}if(this.min!=null&&this.max!=null&&this.numberTicks!=null){this.tickInterval=null}if(this.min!=null&&this.max!=null&&this.tickInterval!=null){if(parseInt((this.max-this.min)/this.tickInterval,10)!=(this.max-this.min)/this.tickInterval){this.tickInterval=null}}var y=[];var B=0;var q=0.5;var w,E;var f=false;for(var x=0;x<this._series.length;x++){var k=this._series[x];for(var u=0;u<k.data.length;u++){if(this.name=="xaxis"||this.name=="x2axis"){E=k.data[u][0]}else{E=k.data[u][1]}if(a.inArray(E,y)==-1){f=true;B+=1;y.push(E)}}}if(f&&this.sortMergedLabels){y.sort(function(j,i){return j-i})}this.ticks=y;for(var x=0;x<this._series.length;x++){var k=this._series[x];for(var u=0;u<k.data.length;u++){if(this.name=="xaxis"||this.name=="x2axis"){E=k.data[u][0]}else{E=k.data[u][1]}var n=a.inArray(E,y)+1;if(this.name=="xaxis"||this.name=="x2axis"){k.data[u][0]=n}else{k.data[u][1]=n}}}if(this.groups>1&&!this._grouped){var r=y.length;var p=parseInt(r/this.groups,10);var e=0;for(var x=p;x<r;x+=p+1){y[x]=" "}this._grouped=true}w=B+0.5;if(this.numberTicks==null){this.numberTicks=2*B+1}var m=w-q;this.min=q;this.max=w;var o=0;var g=parseInt(3+v/10,10);var p=parseInt(B/g,10);if(this.tickInterval==null){this.tickInterval=m/(this.numberTicks-1)}for(var x=0;x<this.numberTicks;x++){b=this.min+x*this.tickInterval;var h=new this.tickRenderer(this.tickOptions);if(x/2==parseInt(x/2,10)){h.showLabel=false;h.showMark=true}else{if(p>0&&o<p){h.showLabel=false;o+=1}else{h.showLabel=true;o=0}h.label=h.formatter(h.formatString,y[(x-1)/2]);h.showMark=false;h.showGridline=false}h.setTick(b,this.name);this._ticks.push(h)}}};a.jqplot.CategoryAxisRenderer.prototype.draw=function(b,j){if(this.show){this.renderer.createTicks.call(this);var h=0;var c;if(this._elem){this._elem.emptyForce()}this._elem=this._elem||a('<div class="jqplot-axis jqplot-'+this.name+'" style="position:absolute;"></div>');if(this.name=="xaxis"||this.name=="x2axis"){this._elem.width(this._plotDimensions.width)}else{this._elem.height(this._plotDimensions.height)}this.labelOptions.axis=this.name;this._label=new this.labelRenderer(this.labelOptions);if(this._label.show){var g=this._label.draw(b,j);g.appendTo(this._elem)}var f=this._ticks;for(var e=0;e<f.length;e++){var d=f[e];if(d.showLabel&&(!d.isMinorTick||this.showMinorTicks)){var g=d.draw(b,j);g.appendTo(this._elem)}}this._groupLabels=[];for(var e=0;e<this.groupLabels.length;e++){var g=a('<div style="position:absolute;" class="jqplot-'+this.name+'-groupLabel"></div>');g.html(this.groupLabels[e]);this._groupLabels.push(g);g.appendTo(this._elem)}}return this._elem};a.jqplot.CategoryAxisRenderer.prototype.set=function(){var e=0;var m;var k=0;var f=0;var d=(this._label==null)?false:this._label.show;if(this.show){var n=this._ticks;for(var c=0;c<n.length;c++){var g=n[c];if(g.showLabel&&(!g.isMinorTick||this.showMinorTicks)){if(this.name=="xaxis"||this.name=="x2axis"){m=g._elem.outerHeight(true)}else{m=g._elem.outerWidth(true)}if(m>e){e=m}}}var j=0;for(var c=0;c<this._groupLabels.length;c++){var b=this._groupLabels[c];if(this.name=="xaxis"||this.name=="x2axis"){m=b.outerHeight(true)}else{m=b.outerWidth(true)}if(m>j){j=m}}if(d){k=this._label._elem.outerWidth(true);f=this._label._elem.outerHeight(true)}if(this.name=="xaxis"){e+=j+f;this._elem.css({height:e+"px",left:"0px",bottom:"0px"})}else{if(this.name=="x2axis"){e+=j+f;this._elem.css({height:e+"px",left:"0px",top:"0px"})}else{if(this.name=="yaxis"){e+=j+k;this._elem.css({width:e+"px",left:"0px",top:"0px"});if(d&&this._label.constructor==a.jqplot.AxisLabelRenderer){this._label._elem.css("width",k+"px")}}else{e+=j+k;this._elem.css({width:e+"px",right:"0px",top:"0px"});if(d&&this._label.constructor==a.jqplot.AxisLabelRenderer){this._label._elem.css("width",k+"px")}}}}}};a.jqplot.CategoryAxisRenderer.prototype.pack=function(e,c){var C=this._ticks;var v=this.max;var s=this.min;var n=c.max;var l=c.min;var q=(this._label==null)?false:this._label.show;var x;for(var r in e){this._elem.css(r,e[r])}this._offsets=c;var g=n-l;var k=v-s;this.p2u=function(h){return(h-l)*k/g+s};this.u2p=function(h){return(h-s)*g/k+l};if(this.name=="xaxis"||this.name=="x2axis"){this.series_u2p=function(h){return(h-s)*g/k};this.series_p2u=function(h){return h*k/g+s}}else{this.series_u2p=function(h){return(h-v)*g/k};this.series_p2u=function(h){return h*k/g+v}}if(this.show){if(this.name=="xaxis"||this.name=="x2axis"){for(x=0;x<C.length;x++){var o=C[x];if(o.show&&o.showLabel){var b;if(o.constructor==a.jqplot.CanvasAxisTickRenderer&&o.angle){var A=(this.name=="xaxis")?1:-1;switch(o.labelPosition){case"auto":if(A*o.angle<0){b=-o.getWidth()+o._textRenderer.height*Math.sin(-o._textRenderer.angle)/2}else{b=-o._textRenderer.height*Math.sin(o._textRenderer.angle)/2}break;case"end":b=-o.getWidth()+o._textRenderer.height*Math.sin(-o._textRenderer.angle)/2;break;case"start":b=-o._textRenderer.height*Math.sin(o._textRenderer.angle)/2;break;case"middle":b=-o.getWidth()/2+o._textRenderer.height*Math.sin(-o._textRenderer.angle)/2;break;default:b=-o.getWidth()/2+o._textRenderer.height*Math.sin(-o._textRenderer.angle)/2;break}}else{b=-o.getWidth()/2}var D=this.u2p(o.value)+b+"px";o._elem.css("left",D);o.pack()}}var z=["bottom",0];if(q){var m=this._label._elem.outerWidth(true);this._label._elem.css("left",l+g/2-m/2+"px");if(this.name=="xaxis"){this._label._elem.css("bottom","0px");z=["bottom",this._label._elem.outerHeight(true)]}else{this._label._elem.css("top","0px");z=["top",this._label._elem.outerHeight(true)]}this._label.pack()}var d=parseInt(this._ticks.length/this.groups,10);for(x=0;x<this._groupLabels.length;x++){var B=0;var f=0;for(var u=x*d;u<=(x+1)*d;u++){if(this._ticks[u]._elem&&this._ticks[u].label!=" "){var o=this._ticks[u]._elem;var r=o.position();B+=r.left+o.outerWidth(true)/2;f++}}B=B/f;this._groupLabels[x].css({left:(B-this._groupLabels[x].outerWidth(true)/2)});this._groupLabels[x].css(z[0],z[1])}}else{for(x=0;x<C.length;x++){var o=C[x];if(o.show&&o.showLabel){var b;if(o.constructor==a.jqplot.CanvasAxisTickRenderer&&o.angle){var A=(this.name=="yaxis")?1:-1;switch(o.labelPosition){case"auto":case"end":if(A*o.angle<0){b=-o._textRenderer.height*Math.cos(-o._textRenderer.angle)/2}else{b=-o.getHeight()+o._textRenderer.height*Math.cos(o._textRenderer.angle)/2}break;case"start":if(o.angle>0){b=-o._textRenderer.height*Math.cos(-o._textRenderer.angle)/2}else{b=-o.getHeight()+o._textRenderer.height*Math.cos(o._textRenderer.angle)/2}break;case"middle":b=-o.getHeight()/2;break;default:b=-o.getHeight()/2;break}}else{b=-o.getHeight()/2}var D=this.u2p(o.value)+b+"px";o._elem.css("top",D);o.pack()}}var z=["left",0];if(q){var y=this._label._elem.outerHeight(true);this._label._elem.css("top",n-g/2-y/2+"px");if(this.name=="yaxis"){this._label._elem.css("left","0px");z=["left",this._label._elem.outerWidth(true)]}else{this._label._elem.css("right","0px");z=["right",this._label._elem.outerWidth(true)]}this._label.pack()}var d=parseInt(this._ticks.length/this.groups,10);for(x=0;x<this._groupLabels.length;x++){var B=0;var f=0;for(var u=x*d;u<=(x+1)*d;u++){if(this._ticks[u]._elem&&this._ticks[u].label!=" "){var o=this._ticks[u]._elem;var r=o.position();B+=r.top+o.outerHeight()/2;f++}}B=B/f;this._groupLabels[x].css({top:B-this._groupLabels[x].outerHeight()/2});this._groupLabels[x].css(z[0],z[1])}}}}})(jQuery);/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.0b2_r1012
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * Although not required, the author would appreciate an email letting him
 * know of any substantial use of jqPlot.  You can reach the author at:
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 */
(function($) {

  /**
   * Class: $.jqplot.PointLabels
   * Plugin for putting labels at the data points.
   *
   * To use this plugin, include the js
   * file in your source:
   *
   * > <script type="text/javascript" src="plugins/jqplot.pointLabels.js"></script>
   *
   * By default, the last value in the data ponit array in the data series is used
   * for the label.  For most series renderers, extra data can be added to the
   * data point arrays and the last value will be used as the label.
   *
   * For instance,
   * this series:
   *
   * > [[1,4], [3,5], [7,2]]
   *
   * Would, by default, use the y values in the labels.
   * Extra data can be added to the series like so:
   *
   * > [[1,4,'mid'], [3 5,'hi'], [7,2,'low']]
   *
   * And now the point labels would be 'mid', 'low', and 'hi'.
   *
   * Options to the point labels and a custom labels array can be passed into the
   * "pointLabels" option on the series option like so:
   *
   * > series:[{pointLabels:{
     * >    labels:['mid', 'hi', 'low'],
     * >    location:'se',
     * >    ypadding: 12
     * >    }
     * > }]
   *
   * A custom labels array in the options takes precendence over any labels
   * in the series data.  If you have a custom labels array in the options,
   * but still want to use values from the series array as labels, set the
   * "labelsFromSeries" option to true.
   *
   * By default, html entities (<, >, etc.) are escaped in point labels.
   * If you want to include actual html markup in the labels,
   * set the "escapeHTML" option to false.
   *
   */
  $.jqplot.PointLabels = function(options) {
    // Group: Properties
    //
    // prop: show
    // show the labels or not.
    this.show = $.jqplot.config.enablePlugins;
    // prop: location
    // compass location where to position the label around the point.
    // 'n', 'ne', 'e', 'se', 's', 'sw', 'w', 'nw'
    this.location = 'n';
    // prop: labelsFromSeries
    // true to use labels within data point arrays.
    this.labelsFromSeries = false;
    // prop: seriesLabelIndex
    // array index for location of labels within data point arrays.
    // if null, will use the last element of the data point array.
    this.seriesLabelIndex = null;
    // prop: labels
    // array of arrays of labels, one array for each series.
    this.labels = [];
    // actual labels that will get displayed.
    // needed to preserve user specified labels in labels array.
    this._labels = [];
    // prop: stackedValue
    // true to display value as stacked in a stacked plot.
    // no effect if labels is specified.
    this.stackedValue = false;
    // prop: ypadding
    // vertical padding in pixels between point and label
    this.ypadding = 6;
    // prop: xpadding
    // horizontal padding in pixels between point and label
    this.xpadding = 6;
    // prop: escapeHTML
    // true to escape html entities in the labels.
    // If you want to include markup in the labels, set to false.
    this.escapeHTML = true;
    // prop: edgeTolerance
    // Number of pixels that the label must be away from an axis
    // boundary in order to be drawn.  Negative values will allow overlap
    // with the grid boundaries.
    this.edgeTolerance = -5;
    // prop: formatter
    // A class of a formatter for the tick text.  sprintf by default.
    this.formatter = $.jqplot.DefaultTickFormatter;
    // prop: formatString
    // string passed to the formatter.
    this.formatString = '';
    // prop: hideZeros
    // true to not show a label for a value which is 0.
    this.hideZeros = false;
    this._elems = [];

    $.extend(true, this, options);
  };

  var locations = ['nw', 'n', 'ne', 'e', 'se', 's', 'sw', 'w'];
  var locationIndicies = {'nw':0, 'n':1, 'ne':2, 'e':3, 'se':4, 's':5, 'sw':6, 'w':7};
  var oppositeLocations = ['se', 's', 'sw', 'w', 'nw', 'n', 'ne', 'e'];

  // called with scope of a series
  $.jqplot.PointLabels.init = function (target, data, seriesDefaults, opts, plot){
    var options = $.extend(true, {}, seriesDefaults, opts);
    options.pointLabels = options.pointLabels || {};
    if (this.renderer.constructor === $.jqplot.BarRenderer && this.barDirection === 'horizontal' && !options.pointLabels.location) {
      options.pointLabels.location = 'e';
    }
    // add a pointLabels attribute to the series plugins
    this.plugins.pointLabels = new $.jqplot.PointLabels(options.pointLabels);
    this.plugins.pointLabels.setLabels.call(this);
  };

  // called with scope of series
  $.jqplot.PointLabels.prototype.setLabels = function() {
    var p = this.plugins.pointLabels;
    var labelIdx;
    if (p.seriesLabelIndex != null) {
      labelIdx = p.seriesLabelIndex;
    }
    else if (this.renderer.constructor === $.jqplot.BarRenderer && this.barDirection === 'horizontal') {
      labelIdx = 0;
    }
    else {
      labelIdx = (this._plotData.length === 0) ? 0 : this._plotData[0].length -1;
    }
    p._labels = [];
    if (p.labels.length === 0 || p.labelsFromSeries) {
      if (p.stackedValue) {
        if (this._plotData.length && this._plotData[0].length){
          // var idx = p.seriesLabelIndex || this._plotData[0].length -1;
          for (var i=0; i<this._plotData.length; i++) {
            p._labels.push(this._plotData[i][labelIdx]);
          }
        }
      }
      else {
        var d = this._plotData;
        if (this.renderer.constructor === $.jqplot.BarRenderer && this.waterfall) {
          d = this._data;
        }
        if (d.length && d[0].length) {
          // var idx = p.seriesLabelIndex || d[0].length -1;
          for (var i=0; i<d.length; i++) {
            p._labels.push(d[i][labelIdx]);
          }
        }
        d = null;
      }
    }
    else if (p.labels.length){
      p._labels = p.labels;
    }
  };

  $.jqplot.PointLabels.prototype.xOffset = function(elem, location, padding) {
    location = location || this.location;
    padding = padding || this.xpadding;
    var offset;

    switch (location) {
      case 'nw':
        offset = -elem.outerWidth(true) - this.xpadding;
        break;
      case 'n':
        offset = -elem.outerWidth(true)/2;
        break;
      case 'ne':
        offset =  this.xpadding;
        break;
      case 'e':
        offset = this.xpadding;
        break;
      case 'se':
        offset = this.xpadding;
        break;
      case 's':
        offset = -elem.outerWidth(true)/2;
        break;
      case 'sw':
        offset = -elem.outerWidth(true) - this.xpadding;
        break;
      case 'w':
        offset = -elem.outerWidth(true) - this.xpadding;
        break;
      default: // same as 'nw'
        offset = -elem.outerWidth(true) - this.xpadding;
        break;
    }
    return offset;
  };

  $.jqplot.PointLabels.prototype.yOffset = function(elem, location, padding) {
    location = location || this.location;
    padding = padding || this.xpadding;
    var offset;

    switch (location) {
      case 'nw':
        offset = -elem.outerHeight(true) - this.ypadding;
        break;
      case 'n':
        offset = -elem.outerHeight(true) - this.ypadding;
        break;
      case 'ne':
        offset = -elem.outerHeight(true) - this.ypadding;
        break;
      case 'e':
        offset = -elem.outerHeight(true)/2;
        break;
      case 'se':
        offset = this.ypadding;
        break;
      case 's':
        offset = this.ypadding;
        break;
      case 'sw':
        offset = this.ypadding;
        break;
      case 'w':
        offset = -elem.outerHeight(true)/2;
        break;
      default: // same as 'nw'
        offset = -elem.outerHeight(true) - this.ypadding;
        break;
    }
    return offset;
  };

  // called with scope of series
  $.jqplot.PointLabels.draw = function (sctx, options, plot) {
    var p = this.plugins.pointLabels;
    // set labels again in case they have changed.
    p.setLabels.call(this);
    // remove any previous labels
    for (var i=0; i<p._elems.length; i++) {
      // Memory Leaks patch
      // p._elems[i].remove();
      p._elems[i].emptyForce();
    }
    p._elems.splice(0, p._elems.length);

    if (p.show) {
      var ax = '_'+this._stackAxis+'axis';

      if (!p.formatString) {
        p.formatString = this[ax]._ticks[0].formatString;
        p.formatter = this[ax]._ticks[0].formatter;
      }

      var pd = this._plotData;
      var xax = this._xaxis;
      var yax = this._yaxis;
      var elem, helem;

      for (var i=0, l=p._labels.length; i < l; i++) {
        var label = p._labels[i];

        if (p.hideZeros && parseInt(p._labels[i], 10) == 0) {
          label = '';
        }

        if (label != null) {
          label = p.formatter(p.formatString, label);
        }

        helem = document.createElement('div');
        p._elems[i] = $(helem);

        elem = p._elems[i];


        elem.addClass('jqplot-point-label jqplot-series-'+this.index+' jqplot-point-'+i);
        elem.css('position', 'absolute');
        elem.insertAfter(sctx.canvas);

        if (p.escapeHTML) {
          elem.text(label);
        }
        else {
          elem.html(label);
        }
        var location = p.location;
        if ((this.fillToZero && pd[i][1] < 0) || (this.fillToZero && this._type === 'bar' && this.barDirection === 'horizontal' && pd[i][0] < 0) || (this.waterfall && parseInt(label, 10)) < 0) {
          location = oppositeLocations[locationIndicies[location]];
        }
        var ell = xax.u2p(pd[i][0]) + p.xOffset(elem, location);
        var elt = yax.u2p(pd[i][1]) + p.yOffset(elem, location);
        if (this.renderer.constructor == $.jqplot.BarRenderer) {
          if (this.barDirection == "vertical") {
            ell += this._barNudge;
          }
          else {
            elt -= this._barNudge;
          }
        }
        elem.css('left', ell);
        elem.css('top', elt);
        var elr = ell + elem.width();
        var elb = elt + elem.height();
        var et = p.edgeTolerance;
        var scl = $(sctx.canvas).position().left;
        var sct = $(sctx.canvas).position().top;
        var scr = sctx.canvas.width + scl;
        var scb = sctx.canvas.height + sct;
        // if label is outside of allowed area, remove it
        if (ell - et < scl || elt - et < sct || elr + et > scr || elb + et > scb) {
          elem.remove();
        }

        elem = null;
        helem = null;
      }

      // finally, animate them if the series is animated
      // if (this.renderer.animation && this.renderer.animation._supported && this.renderer.animation.show && plot._drawCount < 2) {
      //     var sel = '.jqplot-point-label.jqplot-series-'+this.index;
      //     $(sel).hide();
      //     $(sel).fadeIn(1000);
      // }

    }
  };

  $.jqplot.postSeriesInitHooks.push($.jqplot.PointLabels.init);
  $.jqplot.postDrawSeriesHooks.push($.jqplot.PointLabels.draw);
})(jQuery);/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.0b2_r1012
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * Although not required, the author would appreciate an email letting him
 * know of any substantial use of jqPlot.  You can reach the author at:
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 * included jsDate library by Chris Leonello:
 *
 * Copyright (c) 2010-2011 Chris Leonello
 *
 * jsDate is currently available for use in all personal or commercial projects
 * under both the MIT and GPL version 2.0 licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * jsDate borrows many concepts and ideas from the Date Instance
 * Methods by Ken Snyder along with some parts of Ken's actual code.
 *
 * Ken's origianl Date Instance Methods and copyright notice:
 *
 * Ken Snyder (ken d snyder at gmail dot com)
 * 2008-09-10
 * version 2.0.2 (http://kendsnyder.com/sandbox/date/)
 * Creative Commons Attribution License 3.0 (http://creativecommons.org/licenses/by/3.0/)
 *
 * jqplotToImage function based on Larry Siden's export-jqplot-to-png.js.
 * Larry has generously given permission to adapt his code for inclusion
 * into jqPlot.
 *
 * Larry's original code can be found here:
 *
 * https://github.com/lsiden/export-jqplot-to-png
 *
 *
 */
(function(e){e.jqplot.PieRenderer=function(){e.jqplot.LineRenderer.call(this)};e.jqplot.PieRenderer.prototype=new e.jqplot.LineRenderer();e.jqplot.PieRenderer.prototype.constructor=e.jqplot.PieRenderer;e.jqplot.PieRenderer.prototype.init=function(q,u){this.diameter=null;this.padding=20;this.sliceMargin=0;this.fill=true;this.shadowOffset=2;this.shadowAlpha=0.07;this.shadowDepth=5;this.highlightMouseOver=true;this.highlightMouseDown=false;this.highlightColors=[];this.dataLabels="percent";this.showDataLabels=false;this.dataLabelFormatString=null;this.dataLabelThreshold=3;this.dataLabelPositionFactor=0.52;this.dataLabelNudge=2;this.dataLabelCenterOn=true;this.startAngle=0;this.tickRenderer=e.jqplot.PieTickRenderer;this._drawData=true;this._type="pie";if(q.highlightMouseDown&&q.highlightMouseOver==null){q.highlightMouseOver=false}e.extend(true,this,q);if(this.sliceMargin<0){this.sliceMargin=0}this._diameter=null;this._radius=null;this._sliceAngles=[];this._highlightedPoint=null;if(this.highlightColors.length==0){for(var s=0;s<this.seriesColors.length;s++){var r=e.jqplot.getColorComponents(this.seriesColors[s]);var o=[r[0],r[1],r[2]];var t=o[0]+o[1]+o[2];for(var p=0;p<3;p++){o[p]=(t>570)?o[p]*0.8:o[p]+0.3*(255-o[p]);o[p]=parseInt(o[p],10)}this.highlightColors.push("rgb("+o[0]+","+o[1]+","+o[2]+")")}}this.highlightColorGenerator=new e.jqplot.ColorGenerator(this.highlightColors);u.postParseOptionsHooks.addOnce(m);u.postInitHooks.addOnce(g);u.eventListenerHooks.addOnce("jqplotMouseMove",b);u.eventListenerHooks.addOnce("jqplotMouseDown",a);u.eventListenerHooks.addOnce("jqplotMouseUp",l);u.eventListenerHooks.addOnce("jqplotClick",f);u.eventListenerHooks.addOnce("jqplotRightClick",n);u.postDrawHooks.addOnce(i)};e.jqplot.PieRenderer.prototype.setGridData=function(t){var p=[];var u=[];var o=this.startAngle/180*Math.PI;var s=0;this._drawData=false;for(var r=0;r<this.data.length;r++){if(this.data[r][1]!=0){this._drawData=true}p.push(this.data[r][1]);u.push([this.data[r][0]]);if(r>0){p[r]+=p[r-1]}s+=this.data[r][1]}var q=Math.PI*2/p[p.length-1];for(var r=0;r<p.length;r++){u[r][1]=p[r]*q;u[r][2]=this.data[r][1]/s}this.gridData=u};e.jqplot.PieRenderer.prototype.makeGridData=function(t,u){var p=[];var v=[];var s=0;var o=this.startAngle/180*Math.PI;this._drawData=false;for(var r=0;r<t.length;r++){if(this.data[r][1]!=0){this._drawData=true}p.push(t[r][1]);v.push([t[r][0]]);if(r>0){p[r]+=p[r-1]}s+=t[r][1]}var q=Math.PI*2/p[p.length-1];for(var r=0;r<p.length;r++){v[r][1]=p[r]*q;v[r][2]=t[r][1]/s}return v};function h(o){return Math.sin((o-(o-Math.PI)/8/Math.PI)/2)}function j(u,t,o,v,r){var w=0;var q=t-u;var s=Math.abs(q);var p=o;if(v==false){p+=r}if(p>0&&s>0.01&&s<6.282){w=parseFloat(p)/2/h(q)}return w}e.jqplot.PieRenderer.prototype.drawSlice=function(B,z,y,u,w){if(this._drawData){var p=this._radius;var A=this.fill;var x=this.lineWidth;var s=this.sliceMargin;if(this.fill==false){s+=this.lineWidth}B.save();B.translate(this._center[0],this._center[1]);var D=j(z,y,this.sliceMargin,this.fill,this.lineWidth);var o=D*Math.cos((z+y)/2);var C=D*Math.sin((z+y)/2);if((y-z)<=Math.PI){p-=D}else{p+=D}B.translate(o,C);if(w){for(var v=0,t=this.shadowDepth;v<t;v++){B.save();B.translate(this.shadowOffset*Math.cos(this.shadowAngle/180*Math.PI),this.shadowOffset*Math.sin(this.shadowAngle/180*Math.PI));q(p)}for(var v=0,t=this.shadowDepth;v<t;v++){B.restore()}}else{q(p)}B.restore()}function q(r){if(y>6.282+this.startAngle){y=6.282+this.startAngle;if(z>y){z=6.281+this.startAngle}}if(z>=y){return}B.beginPath();B.fillStyle=u;B.strokeStyle=u;B.lineWidth=x;B.arc(0,0,r,z,y,false);B.lineTo(0,0);B.closePath();if(A){B.fill()}else{B.stroke()}}};e.jqplot.PieRenderer.prototype.draw=function(B,z,E,o){var W;var H=(E!=undefined)?E:{};var t=0;var s=0;var N=1;var L=new e.jqplot.ColorGenerator(this.seriesColors);if(E.legendInfo&&E.legendInfo.placement=="insideGrid"){var J=E.legendInfo;switch(J.location){case"nw":t=J.width+J.xoffset;break;case"w":t=J.width+J.xoffset;break;case"sw":t=J.width+J.xoffset;break;case"ne":t=J.width+J.xoffset;N=-1;break;case"e":t=J.width+J.xoffset;N=-1;break;case"se":t=J.width+J.xoffset;N=-1;break;case"n":s=J.height+J.yoffset;break;case"s":s=J.height+J.yoffset;N=-1;break;default:break}}var K=(H.shadow!=undefined)?H.shadow:this.shadow;var A=(H.fill!=undefined)?H.fill:this.fill;var C=B.canvas.width;var I=B.canvas.height;var Q=C-t-2*this.padding;var X=I-s-2*this.padding;var M=Math.min(Q,X);var Y=M;this._sliceAngles=[];var v=this.sliceMargin;if(this.fill==false){v+=this.lineWidth}var q;var G=0;var R,aa,Z,ab;var D=this.startAngle/180*Math.PI;for(var W=0,V=z.length;W<V;W++){aa=(W==0)?D:z[W-1][1]+D;Z=z[W][1]+D;this._sliceAngles.push([aa,Z]);q=j(aa,Z,this.sliceMargin,this.fill,this.lineWidth);if(Math.abs(Z-aa)>Math.PI){G=Math.max(q,G)}}if(this.diameter!=null&&this.diameter>0){this._diameter=this.diameter-2*G}else{this._diameter=Y-2*G}if(this._diameter<6){e.jqplot.log("Diameter of pie too small, not rendering.");return}var S=this._radius=this._diameter/2;this._center=[(C-N*t)/2+N*t+G*Math.cos(D),(I-N*s)/2+N*s+G*Math.sin(D)];if(this.shadow){for(var W=0,V=z.length;W<V;W++){ab="rgba(0,0,0,"+this.shadowAlpha+")";this.renderer.drawSlice.call(this,B,this._sliceAngles[W][0],this._sliceAngles[W][1],ab,true)}}for(var W=0;W<z.length;W++){this.renderer.drawSlice.call(this,B,this._sliceAngles[W][0],this._sliceAngles[W][1],L.next(),false);if(this.showDataLabels&&z[W][2]*100>=this.dataLabelThreshold){var F,U=(this._sliceAngles[W][0]+this._sliceAngles[W][1])/2,T;if(this.dataLabels=="label"){F=this.dataLabelFormatString||"%s";T=e.jqplot.sprintf(F,z[W][0])}else{if(this.dataLabels=="value"){F=this.dataLabelFormatString||"%d";T=e.jqplot.sprintf(F,this.data[W][1])}else{if(this.dataLabels=="percent"){F=this.dataLabelFormatString||"%d%%";T=e.jqplot.sprintf(F,z[W][2]*100)}else{if(this.dataLabels.constructor==Array){F=this.dataLabelFormatString||"%s";T=e.jqplot.sprintf(F,this.dataLabels[W])}}}}var p=(this._radius)*this.dataLabelPositionFactor+this.sliceMargin+this.dataLabelNudge;var P=this._center[0]+Math.cos(U)*p+this.canvas._offsets.left;var O=this._center[1]+Math.sin(U)*p+this.canvas._offsets.top;var u=e('<div class="jqplot-pie-series jqplot-data-label" style="position:absolute;">'+T+"</div>").insertBefore(o.eventCanvas._elem);if(this.dataLabelCenterOn){P-=u.width()/2;O-=u.height()/2}else{P-=u.width()*Math.sin(U/2);O-=u.height()/2}P=Math.round(P);O=Math.round(O);u.css({left:P,top:O})}}};e.jqplot.PieAxisRenderer=function(){e.jqplot.LinearAxisRenderer.call(this)};e.jqplot.PieAxisRenderer.prototype=new e.jqplot.LinearAxisRenderer();e.jqplot.PieAxisRenderer.prototype.constructor=e.jqplot.PieAxisRenderer;e.jqplot.PieAxisRenderer.prototype.init=function(o){this.tickRenderer=e.jqplot.PieTickRenderer;e.extend(true,this,o);this._dataBounds={min:0,max:100};this.min=0;this.max=100;this.showTicks=false;this.ticks=[];this.showMark=false;this.show=false};e.jqplot.PieLegendRenderer=function(){e.jqplot.TableLegendRenderer.call(this)};e.jqplot.PieLegendRenderer.prototype=new e.jqplot.TableLegendRenderer();e.jqplot.PieLegendRenderer.prototype.constructor=e.jqplot.PieLegendRenderer;e.jqplot.PieLegendRenderer.prototype.init=function(o){this.numberRows=null;this.numberColumns=null;e.extend(true,this,o)};e.jqplot.PieLegendRenderer.prototype.draw=function(){var r=this;if(this.show){var B=this._series;this._elem=e(document.createElement("table"));this._elem.addClass("jqplot-table-legend");var E={position:"absolute"};if(this.background){E.background=this.background}if(this.border){E.border=this.border}if(this.fontSize){E.fontSize=this.fontSize}if(this.fontFamily){E.fontFamily=this.fontFamily}if(this.textColor){E.textColor=this.textColor}if(this.marginTop!=null){E.marginTop=this.marginTop}if(this.marginBottom!=null){E.marginBottom=this.marginBottom}if(this.marginLeft!=null){E.marginLeft=this.marginLeft}if(this.marginRight!=null){E.marginRight=this.marginRight}this._elem.css(E);var I=false,A=false,o,y;var C=B[0];var p=new e.jqplot.ColorGenerator(C.seriesColors);if(C.show){var J=C.data;if(this.numberRows){o=this.numberRows;if(!this.numberColumns){y=Math.ceil(J.length/o)}else{y=this.numberColumns}}else{if(this.numberColumns){y=this.numberColumns;o=Math.ceil(J.length/this.numberColumns)}else{o=J.length;y=1}}var H,G;var q,w,v;var x,z,F;var D=0;var u,t;for(H=0;H<o;H++){q=e(document.createElement("tr"));q.addClass("jqplot-table-legend");if(A){q.prependTo(this._elem)}else{q.appendTo(this._elem)}for(G=0;G<y;G++){if(D<J.length){x=this.labels[D]||J[D][0].toString();F=p.next();if(!A){if(H>0){I=true}else{I=false}}else{if(H==o-1){I=false}else{I=true}}z=(I)?this.rowSpacing:"0";w=e(document.createElement("td"));w.addClass("jqplot-table-legend jqplot-table-legend-swatch");w.css({textAlign:"center",paddingTop:z});u=e(document.createElement("div"));u.addClass("jqplot-table-legend-swatch-outline");t=e(document.createElement("div"));t.addClass("jqplot-table-legend-swatch");t.css({backgroundColor:F,borderColor:F});w.append(u.append(t));v=e(document.createElement("td"));v.addClass("jqplot-table-legend jqplot-table-legend-label");v.css("paddingTop",z);if(this.escapeHtml){v.text(x)}else{v.html(x)}if(A){v.prependTo(q);w.prependTo(q)}else{w.appendTo(q);v.appendTo(q)}I=true}D++}}}}return this._elem};e.jqplot.PieRenderer.prototype.handleMove=function(q,p,t,s,r){if(s){var o=[s.seriesIndex,s.pointIndex,s.data];r.target.trigger("jqplotDataMouseOver",o);if(r.series[o[0]].highlightMouseOver&&!(o[0]==r.plugins.pieRenderer.highlightedSeriesIndex&&o[1]==r.series[o[0]]._highlightedPoint)){r.target.trigger("jqplotDataHighlight",o);d(r,o[0],o[1])}}else{if(s==null){k(r)}}};function c(s,r,p){p=p||{};p.axesDefaults=p.axesDefaults||{};p.legend=p.legend||{};p.seriesDefaults=p.seriesDefaults||{};var o=false;if(p.seriesDefaults.renderer==e.jqplot.PieRenderer){o=true}else{if(p.series){for(var q=0;q<p.series.length;q++){if(p.series[q].renderer==e.jqplot.PieRenderer){o=true}}}}if(o){p.axesDefaults.renderer=e.jqplot.PieAxisRenderer;p.legend.renderer=e.jqplot.PieLegendRenderer;p.legend.preDraw=true;p.seriesDefaults.pointLabels={show:false}}}function g(r,q,o){for(var p=0;p<this.series.length;p++){if(this.series[p].renderer.constructor==e.jqplot.PieRenderer){if(this.series[p].highlightMouseOver){this.series[p].highlightMouseDown=false}}}}function m(o){for(var p=0;p<this.series.length;p++){this.series[p].seriesColors=this.seriesColors;this.series[p].colorGenerator=e.jqplot.colorGenerator}}function d(t,r,q){var p=t.series[r];var o=t.plugins.pieRenderer.highlightCanvas;o._ctx.clearRect(0,0,o._ctx.canvas.width,o._ctx.canvas.height);p._highlightedPoint=q;t.plugins.pieRenderer.highlightedSeriesIndex=r;p.renderer.drawSlice.call(p,o._ctx,p._sliceAngles[q][0],p._sliceAngles[q][1],p.highlightColorGenerator.get(q),false)}function k(q){var o=q.plugins.pieRenderer.highlightCanvas;o._ctx.clearRect(0,0,o._ctx.canvas.width,o._ctx.canvas.height);for(var p=0;p<q.series.length;p++){q.series[p]._highlightedPoint=null}q.plugins.pieRenderer.highlightedSeriesIndex=null;q.target.trigger("jqplotDataUnhighlight")}function b(s,r,v,u,t){if(u){var q=[u.seriesIndex,u.pointIndex,u.data];var p=jQuery.Event("jqplotDataMouseOver");p.pageX=s.pageX;p.pageY=s.pageY;t.target.trigger(p,q);if(t.series[q[0]].highlightMouseOver&&!(q[0]==t.plugins.pieRenderer.highlightedSeriesIndex&&q[1]==t.series[q[0]]._highlightedPoint)){var o=jQuery.Event("jqplotDataHighlight");o.pageX=s.pageX;o.pageY=s.pageY;t.target.trigger(o,q);d(t,q[0],q[1])}}else{if(u==null){k(t)}}}function a(r,q,u,t,s){if(t){var p=[t.seriesIndex,t.pointIndex,t.data];if(s.series[p[0]].highlightMouseDown&&!(p[0]==s.plugins.pieRenderer.highlightedSeriesIndex&&p[1]==s.series[p[0]]._highlightedPoint)){var o=jQuery.Event("jqplotDataHighlight");o.pageX=r.pageX;o.pageY=r.pageY;s.target.trigger(o,p);d(s,p[0],p[1])}}else{if(t==null){k(s)}}}function l(q,p,t,s,r){var o=r.plugins.pieRenderer.highlightedSeriesIndex;if(o!=null&&r.series[o].highlightMouseDown){k(r)}}function f(r,q,u,t,s){if(t){var p=[t.seriesIndex,t.pointIndex,t.data];var o=jQuery.Event("jqplotDataClick");o.pageX=r.pageX;o.pageY=r.pageY;s.target.trigger(o,p)}}function n(s,r,v,u,t){if(u){var q=[u.seriesIndex,u.pointIndex,u.data];var o=t.plugins.pieRenderer.highlightedSeriesIndex;if(o!=null&&t.series[o].highlightMouseDown){k(t)}var p=jQuery.Event("jqplotDataRightClick");p.pageX=s.pageX;p.pageY=s.pageY;t.target.trigger(p,q)}}function i(){if(this.plugins.pieRenderer&&this.plugins.pieRenderer.highlightCanvas){this.plugins.pieRenderer.highlightCanvas.resetCanvas();this.plugins.pieRenderer.highlightCanvas=null}this.plugins.pieRenderer={highlightedSeriesIndex:null};this.plugins.pieRenderer.highlightCanvas=new e.jqplot.GenericCanvas();var p=e(this.targetId+" .jqplot-data-label");if(p.length){e(p[0]).before(this.plugins.pieRenderer.highlightCanvas.createElement(this._gridPadding,"jqplot-pieRenderer-highlight-canvas",this._plotDimensions,this))}else{this.eventCanvas._elem.before(this.plugins.pieRenderer.highlightCanvas.createElement(this._gridPadding,"jqplot-pieRenderer-highlight-canvas",this._plotDimensions,this))}var o=this.plugins.pieRenderer.highlightCanvas.setContext();this.eventCanvas._elem.bind("mouseleave",{plot:this},function(q){k(q.data.plot)})}e.jqplot.preInitHooks.push(c);e.jqplot.PieTickRenderer=function(){e.jqplot.AxisTickRenderer.call(this)};e.jqplot.PieTickRenderer.prototype=new e.jqplot.AxisTickRenderer();e.jqplot.PieTickRenderer.prototype.constructor=e.jqplot.PieTickRenderer})(jQuery);/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.4r1121
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * Although not required, the author would appreciate an email letting him
 * know of any substantial use of jqPlot.  You can reach the author at:
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 * included jsDate library by Chris Leonello:
 *
 * Copyright (c) 2010-2011 Chris Leonello
 *
 * jsDate is currently available for use in all personal or commercial projects
 * under both the MIT and GPL version 2.0 licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * jsDate borrows many concepts and ideas from the Date Instance
 * Methods by Ken Snyder along with some parts of Ken's actual code.
 *
 * Ken's origianl Date Instance Methods and copyright notice:
 *
 * Ken Snyder (ken d snyder at gmail dot com)
 * 2008-09-10
 * version 2.0.2 (http://kendsnyder.com/sandbox/date/)
 * Creative Commons Attribution License 3.0 (http://creativecommons.org/licenses/by/3.0/)
 *
 * jqplotToImage function based on Larry Siden's export-jqplot-to-png.js.
 * Larry has generously given permission to adapt his code for inclusion
 * into jqPlot.
 *
 * Larry's original code can be found here:
 *
 * https://github.com/lsiden/export-jqplot-to-png
 *
 *
 */
(function(d){d.jqplot.eventListenerHooks.push(["jqplotMouseMove",f]);d.jqplot.Highlighter=function(h){this.show=d.jqplot.config.enablePlugins;this.markerRenderer=new d.jqplot.MarkerRenderer({shadow:false});this.showMarker=true;this.lineWidthAdjust=2.5;this.sizeAdjust=5;this.showTooltip=true;this.tooltipLocation="nw";this.fadeTooltip=true;this.tooltipFadeSpeed="fast";this.tooltipOffset=2;this.tooltipAxes="both";this.tooltipSeparator=", ";this.tooltipContentEditor=null;this.useAxesFormatters=true;this.tooltipFormatString="%.5P";this.formatString=null;this.yvalues=1;this.bringSeriesToFront=false;this._tooltipElem;this.isHighlighting=false;this.currentNeighbor=null;d.extend(true,this,h)};var b=["nw","n","ne","e","se","s","sw","w"];var e={nw:0,n:1,ne:2,e:3,se:4,s:5,sw:6,w:7};var c=["se","s","sw","w","nw","n","ne","e"];d.jqplot.Highlighter.init=function(k,j,i){var h=i||{};this.plugins.highlighter=new d.jqplot.Highlighter(h.highlighter)};d.jqplot.Highlighter.parseOptions=function(i,h){this.showHighlight=true};d.jqplot.Highlighter.postPlotDraw=function(){if(this.plugins.highlighter&&this.plugins.highlighter.highlightCanvas){this.plugins.highlighter.highlightCanvas.resetCanvas();this.plugins.highlighter.highlightCanvas=null}if(this.plugins.highlighter&&this.plugins.highlighter._tooltipElem){this.plugins.highlighter._tooltipElem.emptyForce();this.plugins.highlighter._tooltipElem=null}this.plugins.highlighter.highlightCanvas=new d.jqplot.GenericCanvas();this.eventCanvas._elem.before(this.plugins.highlighter.highlightCanvas.createElement(this._gridPadding,"jqplot-highlight-canvas",this._plotDimensions,this));this.plugins.highlighter.highlightCanvas.setContext();var h=document.createElement("div");this.plugins.highlighter._tooltipElem=d(h);h=null;this.plugins.highlighter._tooltipElem.addClass("jqplot-highlighter-tooltip");this.plugins.highlighter._tooltipElem.css({position:"absolute",display:"none"});this.eventCanvas._elem.before(this.plugins.highlighter._tooltipElem)};d.jqplot.preInitHooks.push(d.jqplot.Highlighter.init);d.jqplot.preParseSeriesOptionsHooks.push(d.jqplot.Highlighter.parseOptions);d.jqplot.postDrawHooks.push(d.jqplot.Highlighter.postPlotDraw);function a(m,o){var j=m.plugins.highlighter;var p=m.series[o.seriesIndex];var h=p.markerRenderer;var i=j.markerRenderer;i.style=h.style;i.lineWidth=h.lineWidth+j.lineWidthAdjust;i.size=h.size+j.sizeAdjust;var l=d.jqplot.getColorComponents(h.color);var n=[l[0],l[1],l[2]];var k=(l[3]>=0.6)?l[3]*0.6:l[3]*(2-l[3]);i.color="rgba("+n[0]+","+n[1]+","+n[2]+","+k+")";i.init();i.draw(p.gridData[o.pointIndex][0],p.gridData[o.pointIndex][1],j.highlightCanvas._ctx)}function g(A,q,m){var k=A.plugins.highlighter;var D=k._tooltipElem;var r=q.highlighter||{};var t=d.extend(true,{},k,r);if(t.useAxesFormatters){var w=q._xaxis._ticks[0].formatter;var h=q._yaxis._ticks[0].formatter;var E=q._xaxis._ticks[0].formatString;var s=q._yaxis._ticks[0].formatString;var z;var u=w(E,m.data[0]);var l=[];for(var B=1;B<t.yvalues+1;B++){l.push(h(s,m.data[B]))}if(typeof t.formatString==="string"){switch(t.tooltipAxes){case"both":case"xy":l.unshift(u);l.unshift(t.formatString);z=d.jqplot.sprintf.apply(d.jqplot.sprintf,l);break;case"yx":l.push(u);l.unshift(t.formatString);z=d.jqplot.sprintf.apply(d.jqplot.sprintf,l);break;case"x":z=d.jqplot.sprintf.apply(d.jqplot.sprintf,[t.formatString,u]);break;case"y":l.unshift(t.formatString);z=d.jqplot.sprintf.apply(d.jqplot.sprintf,l);break;default:l.unshift(u);l.unshift(t.formatString);z=d.jqplot.sprintf.apply(d.jqplot.sprintf,l);break}}else{switch(t.tooltipAxes){case"both":case"xy":z=u;for(var B=0;B<l.length;B++){z+=t.tooltipSeparator+l[B]}break;case"yx":z="";for(var B=0;B<l.length;B++){z+=l[B]+t.tooltipSeparator}z+=u;break;case"x":z=u;break;case"y":z=l.join(t.tooltipSeparator);break;default:z=u;for(var B=0;B<l.length;B++){z+=t.tooltipSeparator+l[B]}break}}}else{var z;if(typeof t.formatString==="string"){z=d.jqplot.sprintf.apply(d.jqplot.sprintf,[t.formatString].concat(m.data))}else{if(t.tooltipAxes=="both"||t.tooltipAxes=="xy"){z=d.jqplot.sprintf(t.tooltipFormatString,m.data[0])+t.tooltipSeparator+d.jqplot.sprintf(t.tooltipFormatString,m.data[1])}else{if(t.tooltipAxes=="yx"){z=d.jqplot.sprintf(t.tooltipFormatString,m.data[1])+t.tooltipSeparator+d.jqplot.sprintf(t.tooltipFormatString,m.data[0])}else{if(t.tooltipAxes=="x"){z=d.jqplot.sprintf(t.tooltipFormatString,m.data[0])}else{if(t.tooltipAxes=="y"){z=d.jqplot.sprintf(t.tooltipFormatString,m.data[1])}}}}}}if(d.isFunction(t.tooltipContentEditor)){z=t.tooltipContentEditor(z,m.seriesIndex,m.pointIndex,A)}D.html(z);var C={x:m.gridData[0],y:m.gridData[1]};var v=0;var j=0.707;if(q.markerRenderer.show==true){v=(q.markerRenderer.size+t.sizeAdjust)/2}var o=b;if(q.fillToZero&&q.fill&&m.data[1]<0){o=c}switch(o[e[t.tooltipLocation]]){case"nw":var p=C.x+A._gridPadding.left-D.outerWidth(true)-t.tooltipOffset-j*v;var n=C.y+A._gridPadding.top-t.tooltipOffset-D.outerHeight(true)-j*v;break;case"n":var p=C.x+A._gridPadding.left-D.outerWidth(true)/2;var n=C.y+A._gridPadding.top-t.tooltipOffset-D.outerHeight(true)-v;break;case"ne":var p=C.x+A._gridPadding.left+t.tooltipOffset+j*v;var n=C.y+A._gridPadding.top-t.tooltipOffset-D.outerHeight(true)-j*v;break;case"e":var p=C.x+A._gridPadding.left+t.tooltipOffset+v;var n=C.y+A._gridPadding.top-D.outerHeight(true)/2;break;case"se":var p=C.x+A._gridPadding.left+t.tooltipOffset+j*v;var n=C.y+A._gridPadding.top+t.tooltipOffset+j*v;break;case"s":var p=C.x+A._gridPadding.left-D.outerWidth(true)/2;var n=C.y+A._gridPadding.top+t.tooltipOffset+v;break;case"sw":var p=C.x+A._gridPadding.left-D.outerWidth(true)-t.tooltipOffset-j*v;var n=C.y+A._gridPadding.top+t.tooltipOffset+j*v;break;case"w":var p=C.x+A._gridPadding.left-D.outerWidth(true)-t.tooltipOffset-v;var n=C.y+A._gridPadding.top-D.outerHeight(true)/2;break;default:var p=C.x+A._gridPadding.left-D.outerWidth(true)-t.tooltipOffset-j*v;var n=C.y+A._gridPadding.top-t.tooltipOffset-D.outerHeight(true)-j*v;break}D.css("left",p);D.css("top",n);if(t.fadeTooltip){D.stop(true,true).fadeIn(t.tooltipFadeSpeed)}else{D.show()}D=null}function f(n,j,i,p,l){var h=l.plugins.highlighter;var m=l.plugins.cursor;if(h.show){if(p==null&&h.isHighlighting){var o=jQuery.Event("jqplotHighlighterUnhighlight");l.target.trigger(o);var q=h.highlightCanvas._ctx;q.clearRect(0,0,q.canvas.width,q.canvas.height);if(h.fadeTooltip){h._tooltipElem.fadeOut(h.tooltipFadeSpeed)}else{h._tooltipElem.hide()}if(h.bringSeriesToFront){l.restorePreviousSeriesOrder()}h.isHighlighting=false;h.currentNeighbor=null;q=null}else{if(p!=null&&l.series[p.seriesIndex].showHighlight&&!h.isHighlighting){var o=jQuery.Event("jqplotHighlighterHighlight");o.which=n.which;o.pageX=n.pageX;o.pageY=n.pageY;var k=[p.seriesIndex,p.pointIndex,p.data,l];l.target.trigger(o,k);h.isHighlighting=true;h.currentNeighbor=p;if(h.showMarker){a(l,p)}if(h.showTooltip&&(!m||!m._zoom.started)){g(l,l.series[p.seriesIndex],p)}if(h.bringSeriesToFront){l.moveSeriesToFront(p.seriesIndex)}}else{if(p!=null&&h.isHighlighting&&h.currentNeighbor!=p){if(l.series[p.seriesIndex].showHighlight){var q=h.highlightCanvas._ctx;q.clearRect(0,0,q.canvas.width,q.canvas.height);h.isHighlighting=true;h.currentNeighbor=p;if(h.showMarker){a(l,p)}if(h.showTooltip&&(!m||!m._zoom.started)){g(l,l.series[p.seriesIndex],p)}if(h.bringSeriesToFront){l.moveSeriesToFront(p.seriesIndex)}}}}}}}})(jQuery);// Copyright 2006 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
document.createElement("canvas").getContext||(function(){var s=Math,j=s.round,F=s.sin,G=s.cos,V=s.abs,W=s.sqrt,k=10,v=k/2;function X(){return this.context_||(this.context_=new H(this))}var L=Array.prototype.slice;function Y(b,a){var c=L.call(arguments,2);return function(){return b.apply(a,c.concat(L.call(arguments)))}}var M={init:function(b){if(/MSIE/.test(navigator.userAgent)&&!window.opera){var a=b||document;a.createElement("canvas");a.attachEvent("onreadystatechange",Y(this.init_,this,a))}},init_:function(b){b.namespaces.g_vml_||
b.namespaces.add("g_vml_","urn:schemas-microsoft-com:vml","#default#VML");b.namespaces.g_o_||b.namespaces.add("g_o_","urn:schemas-microsoft-com:office:office","#default#VML");if(!b.styleSheets.ex_canvas_){var a=b.createStyleSheet();a.owningElement.id="ex_canvas_";a.cssText="canvas{display:inline-block;overflow:hidden;text-align:left;width:300px;height:150px}g_vml_\\:*{behavior:url(#default#VML)}g_o_\\:*{behavior:url(#default#VML)}"}var c=b.getElementsByTagName("canvas"),d=0;for(;d<c.length;d++)this.initElement(c[d])},
  initElement:function(b){if(!b.getContext){b.getContext=X;b.innerHTML="";b.attachEvent("onpropertychange",Z);b.attachEvent("onresize",$);var a=b.attributes;if(a.width&&a.width.specified)b.style.width=a.width.nodeValue+"px";else b.width=b.clientWidth;if(a.height&&a.height.specified)b.style.height=a.height.nodeValue+"px";else b.height=b.clientHeight}return b}};function Z(b){var a=b.srcElement;switch(b.propertyName){case "width":a.style.width=a.attributes.width.nodeValue+"px";a.getContext().clearRect();
  break;case "height":a.style.height=a.attributes.height.nodeValue+"px";a.getContext().clearRect();break}}function $(b){var a=b.srcElement;if(a.firstChild){a.firstChild.style.width=a.clientWidth+"px";a.firstChild.style.height=a.clientHeight+"px"}}M.init();var N=[],B=0;for(;B<16;B++){var C=0;for(;C<16;C++)N[B*16+C]=B.toString(16)+C.toString(16)}function I(){return[[1,0,0],[0,1,0],[0,0,1]]}function y(b,a){var c=I(),d=0;for(;d<3;d++){var f=0;for(;f<3;f++){var h=0,g=0;for(;g<3;g++)h+=b[d][g]*a[g][f];c[d][f]=
    h}}return c}function O(b,a){a.fillStyle=b.fillStyle;a.lineCap=b.lineCap;a.lineJoin=b.lineJoin;a.lineWidth=b.lineWidth;a.miterLimit=b.miterLimit;a.shadowBlur=b.shadowBlur;a.shadowColor=b.shadowColor;a.shadowOffsetX=b.shadowOffsetX;a.shadowOffsetY=b.shadowOffsetY;a.strokeStyle=b.strokeStyle;a.globalAlpha=b.globalAlpha;a.arcScaleX_=b.arcScaleX_;a.arcScaleY_=b.arcScaleY_;a.lineScale_=b.lineScale_}function P(b){var a,c=1;b=String(b);if(b.substring(0,3)=="rgb"){var d=b.indexOf("(",3),f=b.indexOf(")",d+
    1),h=b.substring(d+1,f).split(",");a="#";var g=0;for(;g<3;g++)a+=N[Number(h[g])];if(h.length==4&&b.substr(3,1)=="a")c=h[3]}else a=b;return{color:a,alpha:c}}function aa(b){switch(b){case "butt":return"flat";case "round":return"round";case "square":default:return"square"}}function H(b){this.m_=I();this.mStack_=[];this.aStack_=[];this.currentPath_=[];this.fillStyle=this.strokeStyle="#000";this.lineWidth=1;this.lineJoin="miter";this.lineCap="butt";this.miterLimit=k*1;this.globalAlpha=1;this.canvas=b;
  var a=b.ownerDocument.createElement("div");a.style.width=b.clientWidth+"px";a.style.height=b.clientHeight+"px";a.style.overflow="hidden";a.style.position="absolute";b.appendChild(a);this.element_=a;this.lineScale_=this.arcScaleY_=this.arcScaleX_=1}var i=H.prototype;i.clearRect=function(){this.element_.innerHTML=""};i.beginPath=function(){this.currentPath_=[]};i.moveTo=function(b,a){var c=this.getCoords_(b,a);this.currentPath_.push({type:"moveTo",x:c.x,y:c.y});this.currentX_=c.x;this.currentY_=c.y};
  i.lineTo=function(b,a){var c=this.getCoords_(b,a);this.currentPath_.push({type:"lineTo",x:c.x,y:c.y});this.currentX_=c.x;this.currentY_=c.y};i.bezierCurveTo=function(b,a,c,d,f,h){var g=this.getCoords_(f,h),l=this.getCoords_(b,a),e=this.getCoords_(c,d);Q(this,l,e,g)};function Q(b,a,c,d){b.currentPath_.push({type:"bezierCurveTo",cp1x:a.x,cp1y:a.y,cp2x:c.x,cp2y:c.y,x:d.x,y:d.y});b.currentX_=d.x;b.currentY_=d.y}i.quadraticCurveTo=function(b,a,c,d){var f=this.getCoords_(b,a),h=this.getCoords_(c,d),g={x:this.currentX_+
      0.6666666666666666*(f.x-this.currentX_),y:this.currentY_+0.6666666666666666*(f.y-this.currentY_)};Q(this,g,{x:g.x+(h.x-this.currentX_)/3,y:g.y+(h.y-this.currentY_)/3},h)};i.arc=function(b,a,c,d,f,h){c*=k;var g=h?"at":"wa",l=b+G(d)*c-v,e=a+F(d)*c-v,m=b+G(f)*c-v,r=a+F(f)*c-v;if(l==m&&!h)l+=0.125;var n=this.getCoords_(b,a),o=this.getCoords_(l,e),q=this.getCoords_(m,r);this.currentPath_.push({type:g,x:n.x,y:n.y,radius:c,xStart:o.x,yStart:o.y,xEnd:q.x,yEnd:q.y})};i.rect=function(b,a,c,d){this.moveTo(b,
      a);this.lineTo(b+c,a);this.lineTo(b+c,a+d);this.lineTo(b,a+d);this.closePath()};i.strokeRect=function(b,a,c,d){var f=this.currentPath_;this.beginPath();this.moveTo(b,a);this.lineTo(b+c,a);this.lineTo(b+c,a+d);this.lineTo(b,a+d);this.closePath();this.stroke();this.currentPath_=f};i.fillRect=function(b,a,c,d){var f=this.currentPath_;this.beginPath();this.moveTo(b,a);this.lineTo(b+c,a);this.lineTo(b+c,a+d);this.lineTo(b,a+d);this.closePath();this.fill();this.currentPath_=f};i.createLinearGradient=function(b,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  a,c,d){var f=new D("gradient");f.x0_=b;f.y0_=a;f.x1_=c;f.y1_=d;return f};i.createRadialGradient=function(b,a,c,d,f,h){var g=new D("gradientradial");g.x0_=b;g.y0_=a;g.r0_=c;g.x1_=d;g.y1_=f;g.r1_=h;return g};i.drawImage=function(b){var a,c,d,f,h,g,l,e,m=b.runtimeStyle.width,r=b.runtimeStyle.height;b.runtimeStyle.width="auto";b.runtimeStyle.height="auto";var n=b.width,o=b.height;b.runtimeStyle.width=m;b.runtimeStyle.height=r;if(arguments.length==3){a=arguments[1];c=arguments[2];h=g=0;l=d=n;e=f=o}else if(arguments.length==
      5){a=arguments[1];c=arguments[2];d=arguments[3];f=arguments[4];h=g=0;l=n;e=o}else if(arguments.length==9){h=arguments[1];g=arguments[2];l=arguments[3];e=arguments[4];a=arguments[5];c=arguments[6];d=arguments[7];f=arguments[8]}else throw Error("Invalid number of arguments");var q=this.getCoords_(a,c),t=[];t.push(" <g_vml_:group",' coordsize="',k*10,",",k*10,'"',' coordorigin="0,0"',' style="width:',10,"px;height:",10,"px;position:absolute;");if(this.m_[0][0]!=1||this.m_[0][1]){var E=[];E.push("M11=",
      this.m_[0][0],",","M12=",this.m_[1][0],",","M21=",this.m_[0][1],",","M22=",this.m_[1][1],",","Dx=",j(q.x/k),",","Dy=",j(q.y/k),"");var p=q,z=this.getCoords_(a+d,c),w=this.getCoords_(a,c+f),x=this.getCoords_(a+d,c+f);p.x=s.max(p.x,z.x,w.x,x.x);p.y=s.max(p.y,z.y,w.y,x.y);t.push("padding:0 ",j(p.x/k),"px ",j(p.y/k),"px 0;filter:progid:DXImageTransform.Microsoft.Matrix(",E.join(""),", sizingmethod='clip');")}else t.push("top:",j(q.y/k),"px;left:",j(q.x/k),"px;");t.push(' ">','<g_vml_:image src="',b.src,
      '"',' style="width:',k*d,"px;"," height:",k*f,'px;"',' cropleft="',h/n,'"',' croptop="',g/o,'"',' cropright="',(n-h-l)/n,'"',' cropbottom="',(o-g-e)/o,'"'," />","</g_vml_:group>");this.element_.insertAdjacentHTML("BeforeEnd",t.join(""))};i.stroke=function(b){var a=[],c=P(b?this.fillStyle:this.strokeStyle),d=c.color,f=c.alpha*this.globalAlpha;a.push("<g_vml_:shape",' filled="',!!b,'"',' style="position:absolute;width:',10,"px;height:",10,'px;"',' coordorigin="0 0" coordsize="',k*10," ",k*10,'"',' stroked="',
      !b,'"',' path="');var h={x:null,y:null},g={x:null,y:null},l=0;for(;l<this.currentPath_.length;l++){var e=this.currentPath_[l];switch(e.type){case "moveTo":a.push(" m ",j(e.x),",",j(e.y));break;case "lineTo":a.push(" l ",j(e.x),",",j(e.y));break;case "close":a.push(" x ");e=null;break;case "bezierCurveTo":a.push(" c ",j(e.cp1x),",",j(e.cp1y),",",j(e.cp2x),",",j(e.cp2y),",",j(e.x),",",j(e.y));break;case "at":case "wa":a.push(" ",e.type," ",j(e.x-this.arcScaleX_*e.radius),",",j(e.y-this.arcScaleY_*e.radius),
      " ",j(e.x+this.arcScaleX_*e.radius),",",j(e.y+this.arcScaleY_*e.radius)," ",j(e.xStart),",",j(e.yStart)," ",j(e.xEnd),",",j(e.yEnd));break}if(e){if(h.x==null||e.x<h.x)h.x=e.x;if(g.x==null||e.x>g.x)g.x=e.x;if(h.y==null||e.y<h.y)h.y=e.y;if(g.y==null||e.y>g.y)g.y=e.y}}a.push(' ">');if(b)if(typeof this.fillStyle=="object"){var m=this.fillStyle,r=0,n={x:0,y:0},o=0,q=1;if(m.type_=="gradient"){var t=m.x1_/this.arcScaleX_,E=m.y1_/this.arcScaleY_,p=this.getCoords_(m.x0_/this.arcScaleX_,m.y0_/this.arcScaleY_),
      z=this.getCoords_(t,E);r=Math.atan2(z.x-p.x,z.y-p.y)*180/Math.PI;if(r<0)r+=360;if(r<1.0E-6)r=0}else{var p=this.getCoords_(m.x0_,m.y0_),w=g.x-h.x,x=g.y-h.y;n={x:(p.x-h.x)/w,y:(p.y-h.y)/x};w/=this.arcScaleX_*k;x/=this.arcScaleY_*k;var R=s.max(w,x);o=2*m.r0_/R;q=2*m.r1_/R-o}var u=m.colors_;u.sort(function(ba,ca){return ba.offset-ca.offset});var J=u.length,da=u[0].color,ea=u[J-1].color,fa=u[0].alpha*this.globalAlpha,ga=u[J-1].alpha*this.globalAlpha,S=[],l=0;for(;l<J;l++){var T=u[l];S.push(T.offset*q+
      o+" "+T.color)}a.push('<g_vml_:fill type="',m.type_,'"',' method="none" focus="100%"',' color="',da,'"',' color2="',ea,'"',' colors="',S.join(","),'"',' opacity="',ga,'"',' g_o_:opacity2="',fa,'"',' angle="',r,'"',' focusposition="',n.x,",",n.y,'" />')}else a.push('<g_vml_:fill color="',d,'" opacity="',f,'" />');else{var K=this.lineScale_*this.lineWidth;if(K<1)f*=K;a.push("<g_vml_:stroke",' opacity="',f,'"',' joinstyle="',this.lineJoin,'"',' miterlimit="',this.miterLimit,'"',' endcap="',aa(this.lineCap),
      '"',' weight="',K,'px"',' color="',d,'" />')}a.push("</g_vml_:shape>");this.element_.insertAdjacentHTML("beforeEnd",a.join(""))};i.fill=function(){this.stroke(true)};i.closePath=function(){this.currentPath_.push({type:"close"})};i.getCoords_=function(b,a){var c=this.m_;return{x:k*(b*c[0][0]+a*c[1][0]+c[2][0])-v,y:k*(b*c[0][1]+a*c[1][1]+c[2][1])-v}};i.save=function(){var b={};O(this,b);this.aStack_.push(b);this.mStack_.push(this.m_);this.m_=y(I(),this.m_)};i.restore=function(){O(this.aStack_.pop(),
      this);this.m_=this.mStack_.pop()};function ha(b){var a=0;for(;a<3;a++){var c=0;for(;c<2;c++)if(!isFinite(b[a][c])||isNaN(b[a][c]))return false}return true}function A(b,a,c){if(!!ha(a)){b.m_=a;if(c)b.lineScale_=W(V(a[0][0]*a[1][1]-a[0][1]*a[1][0]))}}i.translate=function(b,a){A(this,y([[1,0,0],[0,1,0],[b,a,1]],this.m_),false)};i.rotate=function(b){var a=G(b),c=F(b);A(this,y([[a,c,0],[-c,a,0],[0,0,1]],this.m_),false)};i.scale=function(b,a){this.arcScaleX_*=b;this.arcScaleY_*=a;A(this,y([[b,0,0],[0,a,
    0],[0,0,1]],this.m_),true)};i.transform=function(b,a,c,d,f,h){A(this,y([[b,a,0],[c,d,0],[f,h,1]],this.m_),true)};i.setTransform=function(b,a,c,d,f,h){A(this,[[b,a,0],[c,d,0],[f,h,1]],true)};i.clip=function(){};i.arcTo=function(){};i.createPattern=function(){return new U};function D(b){this.type_=b;this.r1_=this.y1_=this.x1_=this.r0_=this.y0_=this.x0_=0;this.colors_=[]}D.prototype.addColorStop=function(b,a){a=P(a);this.colors_.push({offset:b,color:a.color,alpha:a.alpha})};function U(){}G_vmlCanvasManager=
      M;CanvasRenderingContext2D=H;CanvasGradient=D;CanvasPattern=U})();
/*!
 * jQuery Tools v1.2.7 - The missing UI library for the Web
 * 
 * tooltip/tooltip.js
 * tooltip/tooltip.dynamic.js
 * tooltip/tooltip.slide.js
 * 
 * NO COPYRIGHTS OR LICENSES. DO WHAT YOU LIKE.
 * 
 * http://flowplayer.org/tools/
 * 
 */
(function(a){a.tools=a.tools||{version:"v1.2.7"},a.tools.tooltip={conf:{effect:"toggle",fadeOutSpeed:"fast",predelay:0,delay:30,opacity:1,tip:0,fadeIE:!1,position:["top","center"],offset:[0,0],relative:!1,cancelDefault:!0,events:{def:"mouseenter,mouseleave",input:"mouseenter,mouseleave",widget:"mouseenter,mouseleave",tooltip:"mouseenter,mouseleave"},layout:"<div/>",tipClass:"tooltip"},addEffect:function(a,c,d){b[a]=[c,d]}};var b={toggle:[function(a){var b=this.getConf(),c=this.getTip(),d=b.opacity;d<1&&c.css({opacity:d}),c.show(),a.call()},function(a){this.getTip().hide(),a.call()}],fade:[function(b){var c=this.getConf();!a.browser.msie||c.fadeIE?this.getTip().fadeTo(c.fadeInSpeed,c.opacity,b):(this.getTip().show(),b())},function(b){var c=this.getConf();!a.browser.msie||c.fadeIE?this.getTip().fadeOut(c.fadeOutSpeed,b):(this.getTip().hide(),b())}]};function c(b,c,d){var e=d.relative?b.position().top:b.offset().top,f=d.relative?b.position().left:b.offset().left,g=d.position[0];e-=c.outerHeight()-d.offset[0],f+=b.outerWidth()+d.offset[1],/iPad/i.test(navigator.userAgent)&&(e-=a(window).scrollTop());var h=c.outerHeight()+b.outerHeight();g=="center"&&(e+=h/2),g=="bottom"&&(e+=h),g=d.position[1];var i=c.outerWidth()+b.outerWidth();g=="center"&&(f-=i/2),g=="left"&&(f-=i);return{top:e,left:f}}function d(d,e){var f=this,g=d.add(f),h,i=0,j=0,k=d.attr("title"),l=d.attr("data-tooltip"),m=b[e.effect],n,o=d.is(":input"),p=o&&d.is(":checkbox, :radio, select, :button, :submit"),q=d.attr("type"),r=e.events[q]||e.events[o?p?"widget":"input":"def"];if(!m)throw"Nonexistent effect \""+e.effect+"\"";r=r.split(/,\s*/);if(r.length!=2)throw"Tooltip: bad events configuration for "+q;d.on(r[0],function(a){clearTimeout(i),e.predelay?j=setTimeout(function(){f.show(a)},e.predelay):f.show(a)}).on(r[1],function(a){clearTimeout(j),e.delay?i=setTimeout(function(){f.hide(a)},e.delay):f.hide(a)}),k&&e.cancelDefault&&(d.removeAttr("title"),d.data("title",k)),a.extend(f,{show:function(b){if(!h){l?h=a(l):e.tip?h=a(e.tip).eq(0):k?h=a(e.layout).addClass(e.tipClass).appendTo(document.body).hide().append(k):(h=d.next(),h.length||(h=d.parent().next()));if(!h.length)throw"Cannot find tooltip for "+d}if(f.isShown())return f;h.stop(!0,!0);var o=c(d,h,e);e.tip&&h.html(d.data("title")),b=a.Event(),b.type="onBeforeShow",g.trigger(b,[o]);if(b.isDefaultPrevented())return f;o=c(d,h,e),h.css({position:"absolute",top:o.top,left:o.left}),n=!0,m[0].call(f,function(){b.type="onShow",n="full",g.trigger(b)});var p=e.events.tooltip.split(/,\s*/);h.data("__set")||(h.off(p[0]).on(p[0],function(){clearTimeout(i),clearTimeout(j)}),p[1]&&!d.is("input:not(:checkbox, :radio), textarea")&&h.off(p[1]).on(p[1],function(a){a.relatedTarget!=d[0]&&d.trigger(r[1].split(" ")[0])}),e.tip||h.data("__set",!0));return f},hide:function(c){if(!h||!f.isShown())return f;c=a.Event(),c.type="onBeforeHide",g.trigger(c);if(!c.isDefaultPrevented()){n=!1,b[e.effect][1].call(f,function(){c.type="onHide",g.trigger(c)});return f}},isShown:function(a){return a?n=="full":n},getConf:function(){return e},getTip:function(){return h},getTrigger:function(){return d}}),a.each("onHide,onBeforeShow,onShow,onBeforeHide".split(","),function(b,c){a.isFunction(e[c])&&a(f).on(c,e[c]),f[c]=function(b){b&&a(f).on(c,b);return f}})}a.fn.tooltip=function(b){var c=this.data("tooltip");if(c)return c;b=a.extend(!0,{},a.tools.tooltip.conf,b),typeof b.position=="string"&&(b.position=b.position.split(/,?\s/)),this.each(function(){c=new d(a(this),b),a(this).data("tooltip",c)});return b.api?c:this}})(jQuery);
(function(a){var b=a.tools.tooltip;b.dynamic={conf:{classNames:"top right bottom left"}};function c(b){var c=a(window),d=c.width()+c.scrollLeft(),e=c.height()+c.scrollTop();return[b.offset().top<=c.scrollTop(),d<=b.offset().left+b.width(),e<=b.offset().top+b.height(),c.scrollLeft()>=b.offset().left]}function d(a){var b=a.length;while(b--)if(a[b])return!1;return!0}a.fn.dynamic=function(e){typeof e=="number"&&(e={speed:e}),e=a.extend({},b.dynamic.conf,e);var f=a.extend(!0,{},e),g=e.classNames.split(/\s/),h;this.each(function(){var b=a(this).tooltip().onBeforeShow(function(b,e){var i=this.getTip(),j=this.getConf();h||(h=[j.position[0],j.position[1],j.offset[0],j.offset[1],a.extend({},j)]),a.extend(j,h[4]),j.position=[h[0],h[1]],j.offset=[h[2],h[3]],i.css({visibility:"hidden",position:"absolute",top:e.top,left:e.left}).show();var k=a.extend(!0,{},f),l=c(i);if(!d(l)){l[2]&&(a.extend(j,k.top),j.position[0]="top",i.addClass(g[0])),l[3]&&(a.extend(j,k.right),j.position[1]="right",i.addClass(g[1])),l[0]&&(a.extend(j,k.bottom),j.position[0]="bottom",i.addClass(g[2])),l[1]&&(a.extend(j,k.left),j.position[1]="left",i.addClass(g[3]));if(l[0]||l[2])j.offset[0]*=-1;if(l[1]||l[3])j.offset[1]*=-1}i.css({visibility:"visible"}).hide()});b.onBeforeShow(function(){var a=this.getConf(),b=this.getTip();setTimeout(function(){a.position=[h[0],h[1]],a.offset=[h[2],h[3]]},0)}),b.onHide(function(){var a=this.getTip();a.removeClass(e.classNames)}),ret=b});return e.api?ret:this}})(jQuery);
(function(a){var b=a.tools.tooltip;a.extend(b.conf,{direction:"up",bounce:!1,slideOffset:10,slideInSpeed:200,slideOutSpeed:200,slideFade:!a.browser.msie});var c={up:["-","top"],down:["+","top"],left:["-","left"],right:["+","left"]};b.addEffect("slide",function(a){var b=this.getConf(),d=this.getTip(),e=b.slideFade?{opacity:b.opacity}:{},f=c[b.direction]||c.up;e[f[1]]=f[0]+"="+b.slideOffset,b.slideFade&&d.css({opacity:0}),d.show().animate(e,b.slideInSpeed,a)},function(b){var d=this.getConf(),e=d.slideOffset,f=d.slideFade?{opacity:0}:{},g=c[d.direction]||c.up,h=""+g[0];d.bounce&&(h=h=="+"?"-":"+"),f[g[1]]=h+"="+e,this.getTip().animate(f,d.slideOutSpeed,function(){a(this).hide(),b.call()})})})(jQuery);
var eyelineJsfComponents = {};
var eyelineJsfGraphComponents = {};

/**
 * Функция для доступа к реестру компонентов, находящихся на странице
 *
 * @param id идентификатор компонента (указывается в аттрибуте id тега компонента)
 * @returns компонент с указанным идентификатором
 */
function jsfc(id) {
  return eyelineJsfComponents[id];
}

function jsfcGraph(id) {
  return eyelineJsfComponents[id];
}

function jsfcGraphs() {
  return eyelineJsfGraphComponents;
}

function registerJsfComponent(id, component) {
  eyelineJsfComponents[id] = component;
}

function registerJsfGraphComponent(id, component) {
  eyelineJsfComponents[id] = component;
  eyelineJsfGraphComponents[id] = component;
}

/**
 * Функция сериализует форму в объект. Отличается от jQuery.serialize() тем, что сериализуемые значения берутся
 * из аттрибута 'value', а не из параметра val(). Т.е. сериализуются те значения, которые были в полях на момент
 * загрузки страницы.
 * Пример:
 * Пусть на форме есть текстовое поле. При загрузке страницы в этом поле было значение 2. Пользователь поменял его
 * на 4. Если вызвать jQuery.serialize(), то в полученном объекте будет 4, а если вызвать serializeForm(), то 2.
 *
 * @param frm jquery объект с формой
 * @returns Объект со всепи параметрами формы
 */
function serializeForm(frm) {
  var result = {};
  $.each(frm.find(":input[name]"), function(idx, cur) {
    var jcur = $(cur);
    if (jcur.is("select")) {
      var opt = jcur.find("option[selected]");
      if (opt != null)
        result[jcur.attr("name")] = opt.val();
    } else if (jcur.is(":checkbox")) {
      if (cur.getAttribute("checked"))
        result[jcur.attr("name")] = "on";
    } else if (jcur.attr("name") != "source") {
      var value = cur.getAttribute("value");
      if (value)
        result[jcur.attr("name")] = value;
    }
  });
  return result;
}

function serializeValues(frm) {
  var result = {};
  $.each(frm.find(":input[name]"), function(idx, cur) {
    var jcur = $(cur);
    if (jcur.is(":checkbox")) {
      if (cur.checked)
        result[jcur.attr("name")] = "on";
    } else if (jcur.attr("name") != "source") {
      var value = jcur.val();
      if (value)
        result[jcur.attr("name")] = value;
    }
  });
  return result;
}

/**
 * Объект для представления f:selectItem. Может использоваться в компонентах-списках.
 * @param _id идентификатор (строка)
 * @param _value значение (строка)
 * @constructor
 */
function SelectItem(_id, _value) {
  var id = _id;
  var value = _value;

  this.id = function() {
    return id;
  }

  this.value = function() {
    return value;
  }
}
function createCollapsingGroup(options) {
  var component = new EyelineCollapsingGroupComponent(options);
  registerJsfComponent(options['id'], component);
}

function foundGraphs(body) {
  var gs = jsfcGraphs();
  var el = $(body);
  for(var index in gs){
    if(el.find("#"+index).length > 0) {
      gs[index].replot();
    }
  }
}

function EyelineCollapsingGroupComponent(options) {
  var groupId = options['id'];
  var header = $("#" + groupId + "_header");
  var headerFacet = $("#" + groupId + "_facet");
  var body = $("#" + groupId + "_body");
  var status = $("#" + groupId + "_status");
  var visibleEl = $("#" + groupId + "_visible");

  var headerOpenedClass = options['headerOpenedClass'];
  var headerClosedClass = options['headerClosedClass'];

  var facetClicked = false;
  headerFacet.click(function () {
    facetClicked = true;
  });

  header.click(function () {
    if(facetClicked) {
      facetClicked = false;
      return true;
    }
    var idx = status.val();
    if (idx == 0) {
      header.removeClass(headerOpenedClass).addClass(headerClosedClass);
      body.hide()
    } else {
      header.removeClass(headerClosedClass).addClass(headerOpenedClass);
      body.show();
      foundGraphs(body);
    }
    status.val(1 - idx);
  });

  // PRIVATE

  var _setVisible = function(visible) {
    var display = visible ? "" : "none";
    if (visible) {
      body.show(); header.show();
    } else {
      body.hide(); header.hide();
    }
    visibleEl.val(visible);
  };

  // PUBLIC

  this.setVisible = function(visible){
    _setVisible(visible);
    return this;
  };

  this.show = function() {
    _setVisible(true);
    return this;
  };

  this.hide = function() {
    _setVisible(false);
    return this;
  };
};

function createButtonsPanel(panelId) {
  var panel = new EyelineButtonsPanelComponent(panelId);
  registerJsfComponent(panelId, panel);
  panel.update();
}

/**
 * Объект - панель кнопок.
 * @param panelId идентификатор панели
 */
function EyelineButtonsPanelComponent(panelId) {

  // CONSTRUCTOR

  var buttonsTable = $("#" + panelId);
  var elements = [];

  $.each(buttonsTable.find("td"), function(idx, val){
    elements[elements.length] = $(val);
  });

  // PRIVATE

  var _update = function() {
    var prevVisible = null;
    for (var i=0; i<elements.length-1; i++) {
      var el = elements[i];
      if (el.hasClass("eyeline_button") && el.css("display") != "none") {
        if (prevVisible) {
          elements[i-1].show();
        } else if (i > 0 && elements[i-1].hasClass("eyeline_button_delimiter")) {
          elements[i-1].hide();
        }
        prevVisible = true;
      } else if (el.hasClass("eyeline_button_delimiter")) {
        el.hide();
      } else if (el.hasClass("eyeline_space_button")) {
        prevVisible = false;
      }
    }
  };

  var _setVisible = function(visible) {
    if (visible)
      buttonsTable.show();
    else
      buttonsTable.hide();
    $('#' + panelId + "_visible").val(visible);
  };

  var _setButtonVisible = function(buttonId, visible) {
    var button = buttonsTable.find('#' + buttonId);
    if (!button)
      return;
    if (visible)
      button.show();
    else
      button.hide();
    buttonsTable.find('#' + buttonId + "_visible").val(visible);
    _update();
  };

  // PUBLIC

  this.setVisible = function(visible) {
    _setVisible(visible);
    return this;
  };

  this.show = function() {
    _setVisible(true);
    return this;
  };

  this.hide = function() {
    _setVisible(false);
    return this;
  };

  this.setButtonVisible = function(buttonId, visible) {
    _setButtonVisible(buttonId, visible);
    return this;
  };

  this.showButton = function(buttonId) {
    _setButtonVisible(buttonId, true);
    return this;
  };

  this.hideButton = function(buttonId) {
    _setButtonVisible(buttonId, false);
    return this;
  };

  this.update = function() {
    _update();
    return this;
  }
};function createDataTable(tableId, tableOptions) {
  var dataTable = new EyelineDataTableComponent(tableId, tableOptions);
  registerJsfComponent(tableId, dataTable);
}

function lookupValueInArray(array, value) {
  for (var idx = 0; idx < array.length; idx++) {
    if (array[idx] == value)
      return idx;
  }
  return -1;
}

function removeValueFromArray(array, idx) {
  var result = new Array(array.length - 1);
  var k = 0;
  for (var i = 0; i < array.length; i++) {
    if (i == idx)
      continue;
    result[k++] = array[i];
  }
  return result;
}

function arrayToJson(arr) {
  var res = "[";
  var first = 1;
  for (var i = 0; i < arr.length; i++) {
    if (first == 0) {
      res += ","
    } else {
      first = 0;
    }
    res += "\"" + arr[i] + "\"";
  }
  return res + "]"
}

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
        expand.onclick = function() {_expandRow(rowId)};
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

      $.each(expanded, function(idx, expand) {
        var rowId = expand.getAttribute("id").substring((tableId + "_rowExpand_").length);
        if(!_isRowOpened(rowId)) {
          openedAll = false;
        }
      });

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


function onStop(tid) {
  jsfc(tid).stop();
}


// ::::::::::::::::::::::::::::::::::::::::: ОВЕРЛЕЙ С ПРОГРЕССОМ ::::::::::::::::::::::::::::::::::::::::::::::::::::::

var ProgressOverlay = function(parent) {

  var visible = false;
  var progressElement = $("<table>").addClass("eyeline_overlay");
  $('<tr><td>&nbsp;</td></tr><tr><td align="center" valign="bottom">' +
      '<div>' +
      '<table style="width:0">' +
      '<tr><td><div class="eyeline_loading"></div></td><td><span class="progressEl"></span><span class="stop_button" onclick="onStop(\''+parent.attr('id')+'\')"></span></td>' +
      '</tr></table>' +
      '</div></td></tr>').appendTo(progressElement);


  var progressContentElement = progressElement.find('span.progressEl');
  var progressStopElement = progressElement.find('span.stop_button');
  progressStopElement.hide();
  var progressPicElement = progressElement.find('.eyeline_loading');

  progressElement.insertAfter(parent);

  var showOverlay = function(value) {
    progressContentElement.text(value ? value : "");

    if (visible)
      return;

    var x = parent.position().left;
    var y = parent.position().top;
    var w = parent[0].offsetWidth;
    var h = parent[0].offsetHeight;
    progressElement.css("left", x + 'px').css("top", y + 'px').css("width", w + 'px').css("height", h + 'px');
    progressElement.show();
    visible = true;
  };

  this.showError = function(value) {
    progressPicElement.hide();
    if(progressStopElement != null) {
      progressStopElement.hide();
    }
    progressContentElement.removeClass("eyeline_progress_ok").addClass("eyeline_progress_error");
    showOverlay(value);
  };

  this.showProgress = function(value,stopText) {
    progressPicElement.show();
    if(stopText != null && stopText != '') {
      progressStopElement.html('(<a href="#">' + stopText + '</a>)');
      progressStopElement.show();
    }else {
      progressStopElement.hide();
    }
    progressContentElement.removeClass("eyeline_progress_error").addClass("eyeline_progress_ok");
    showOverlay(value);
  };

  this.hide = function() {
    if (visible)
      progressElement.hide();
    visible = false;
  }
};

// ::::::::::::::::::::::::::::::::::::::::::::::: ПАНЕЛЬ НАВИГАЦИИ ::::::::::::::::::::::::::::::::::::::::::::::::::::

var NavBarControl = function(id, navbarOptions) {
  // Read options
  var totalSize = navbarOptions.total;
  var pageSize = navbarOptions.pageSize;
  var allowedPageSize = navbarOptions.allowedPageSize;
  var numberOfPages = totalSize / pageSize;
  var pageNumber = Math.min(numberOfPages, navbarOptions.pageNumber);
  var onChange = navbarOptions.onChange;
  var onChangeMode = navbarOptions.onChangeMode;
  var selectedSize = navbarOptions.selected;
  var labels = navbarOptions.labels;
  if (!selectedSize)
    selectedSize = 0;
  var mode = "all";

  var navbarEl = $("#" + id);
  var navBarTable = $("<table>").addClass("eyeline_navbar_panel");
  var navBarTableBody = $("<tbody>").appendTo(navBarTable);
  var navBarTableRow = $("<tr>").appendTo(navBarTableBody);

  // Панель навигации ================================================================================================

  var firstButton = $("<td>").appendTo(navBarTableRow).addClass("eyeline_navbar_button eyeline_navbar_first");
  firstButton.click(function() {
    setPage(1, onChange);
  });

  var prevButton = $("<td>").appendTo(navBarTableRow).addClass("eyeline_navbar_button eyeline_navbar_prev");
  prevButton.click(function() {
    setPage(pageNumber - 1, onChange);
  });

  var pageButtons = new Array(9);
  for (var i = 0; i < 9; i++) {
    var pageBut = $("<td>").appendTo(navBarTableRow).addClass("eyeline_navbar_button eyeline_navbar_page");
    pageBut.click(function() {
      setPage(parseInt(this.getAttribute("curPage")), onChange);
    });
    pageButtons[i] = pageBut;
  }

  var nextButton = $("<td>").appendTo(navBarTableRow).addClass("eyeline_navbar_button eyeline_navbar_next");
  nextButton.click(function() {
    setPage(pageNumber + 1, onChange);
  });

  var lastButton = $("<td>").appendTo(navBarTableRow).addClass("eyeline_navbar_button eyeline_navbar_last");
  lastButton.click(function() {
    setPage(numberOfPages, onChange);
  });

  // Панель счетчиков ================================================================================================

  var counters = $("<td>").appendTo(navBarTableRow).addClass("eyeline_navbar_counters");
  counters.append(labels[0] + ": ");
  var total = $("<span>").appendTo(counters).text(totalSize);

  if (navbarOptions.selectionEnabled) {
    counters.append(" | ");
    var selected = $("<span>").appendTo(counters).text(labels[1] + ": " + selectedSize).addClass("eyeline_navbar_selected");
    selected.click(function() {
      if (mode == "all") {
        mode = "selected";
        selected.text(labels[3]);
      } else {
        mode = "all";
        selected.text(labels[1] + ": " + selectedSize);
      }
      if (onChangeMode)
        onChangeMode(mode);
    });
  }

  if(navbarOptions.pageSizeRendering) {
    counters.append(" | " + labels[2] + ": ");
    var onpage = $("<select>").appendTo(counters);
    for (var j = 0; j < allowedPageSize.length; j++) {
      var psize = allowedPageSize[j];
      var opt = $('<option>', { value :  psize}).text(psize);
      if (psize == pageSize)
        opt.attr("selected", "selected");
      onpage.append(opt);
    }
    onpage.change(function() {
      var selectedValue = onpage.children("option:selected")[0].getAttribute("value");
      setPageSize(parseInt(selectedValue), onChange);
    });
  }

  // Вспомогательные функции =========================================================================================

  var setPage = function(pageNumb, onChange) {
    if (pageNumb > numberOfPages)
      pageNumb = numberOfPages;
    if (pageNumb < 1)
      pageNumb = 1;

    firstButton.css("display", pageNumb <= 1 ? "none" : "");
    prevButton.css("display", pageNumb <= 1 ? "none" : "");
    nextButton.css("display", (numberOfPages == 0 || pageNumb == numberOfPages) ? "none" : "");
    lastButton.css("display", (numberOfPages == 0 || pageNumb == numberOfPages) ? "none" : "");

    if (pageNumber) {
      var prevFirstVisiblePage = Math.max(pageNumber -4, 1);
      pageButtons[pageNumber - prevFirstVisiblePage].removeClass("eyeline_navbar_current_page");
    }

    var firstVisiblePage = Math.max(pageNumb - 4, 1);
    var lastVisiblePage = Math.min(pageNumb + 4, numberOfPages);
    if (lastVisiblePage == firstVisiblePage)
      lastVisiblePage = 0;
    pageButtons[pageNumb - firstVisiblePage].addClass("eyeline_navbar_current_page");

    for (var curPage = firstVisiblePage; curPage <= lastVisiblePage; curPage++)
      pageButtons[curPage - firstVisiblePage].css("display", "").text(curPage).attr("curPage", curPage);

    for (var i = lastVisiblePage + 1; i < firstVisiblePage + 9; i++)
      pageButtons[i - firstVisiblePage].css("display", "none");

    pageNumber = pageNumb;
    if (onChange)
      onChange(pageNumb, pageSize);
  };

  var setPageSize = function(pageS, onChange) {
    var curPos = pageNumber * pageSize;
    numberOfPages = Math.round(totalSize / pageS);
    if (pageS * numberOfPages < totalSize)
      numberOfPages++;
    var pageN = Math.round(curPos / pageS);
    if (pageN != pageNumber)
      setPage(pageN, null);
    pageSize = pageS;
    if (onChange)
      onChange(pageNumber, pageSize);
  };

  this.changeWidth = function(change) {
    oldcolspan = navbarEl.attr("colspan");
    navbarEl.attr("colspan", oldcolspan + change);
  };

  this.setSelected = function(cnt) {
    selectedSize = cnt;
    selected.text(labels[1] + ": " + cnt);
  };

  this.setTotal = function(totalRows, currentPageNumber) {
    totalSize = totalRows;
    total.text(totalRows);
    setPageSize(pageSize, null);
    if (currentPageNumber) {
      setPage(pageNumber = currentPageNumber, null);
    }
  };

  this.getTotal = function() {
    return totalSize;
  };

  this.setTotal(totalSize);

  navBarTable.appendTo(navbarEl);
};

// ::::::::::::::::::::::::::::::::::: КНОПКА СОРТИРОВКИ ПО КОЛОНКЕ ТАБЛИЦЫ ::::::::::::::::::::::::::::::::::::::::::::

var SortableColumnsControl = function(columnsOptions) {

  var onchange = columnsOptions.onChange;

  var columns = {};
  for (var i = 0; i < columnsOptions.columnIds.length; i++) {
    var column = $("#" + columnsOptions.columnIds[i]);
    column.addClass("eyeline_data_table_column");
    column.click(function() {
      setSort(this.id);
    });
    columns[i] = column;
  }

  var parseSortOrder = function(sortOrderColumn) {
    var result = {};
    if (sortOrderColumn.length == 0)
      return result;

    if (sortOrderColumn.charAt(0) == '-') {
      result["column"] = sortOrderColumn.substr(1);
      result["asc"] = false;
    } else {
      result["column"] = sortOrderColumn;
      result["asc"] = true;
    }
    return result;
  };

  var setSort = function(columnId) {
    var oldSortColumn = sortOrder["column"];
    var asc = sortOrder["asc"];
    if (oldSortColumn) {
      $("#" + oldSortColumn).removeClass("eyeline_up eyeline_down");
      if (oldSortColumn == columnId)
        asc = !asc;
    }

    $("#" + columnId).addClass(asc ? "eyeline_up" : "eyeline_down");
    sortOrder["column"] = columnId;
    sortOrder["asc"] = asc;

    if (onchange)
      onchange((asc ? "" : "-") + columnId);
  };

  var sortOrder = parseSortOrder(columnsOptions.sortOrder);

  if (sortOrder["column"]) {
    var columnId = sortOrder["column"];
    var asc = sortOrder["asc"];
    $("#" + columnId).addClass(asc ? "eyeline_up" : "eyeline_down");
  }
};

// :::::::::::::::::::::::: КНОПКА УСТАНОВКИ/СНЯТИЯ ВЯДЕЛЕНИЯ ВСЕХ ЗАПИСЕЙ/ЗАПИСЕЙ НА СТРАНИЦЕ :::::::::::::::::::::::::

var SelectionControl = function(id, selectionOptions) {
  var labels = selectionOptions.labels;
  var selectAllHandler = selectionOptions.onSelectAll;
  var selectPageHandler = selectionOptions.onSelectPage;

  var selectTable = $("<table>").addClass("select_button_area");
  var selectTableBody = $("<tbody>").appendTo(selectTable);
  var selectTableRow = $("<tr>").appendTo(selectTableBody);
  var td1 = $("<td>").appendTo(selectTableRow);
  var selectPage = $("<div>&nbsp;</div>").appendTo(td1).addClass("select_page_button");
  var td2 = $("<td>").appendTo(selectTableRow);
  var showMenu = $("<div>&nbsp;</div>").appendTo(td2).addClass("select_menu_button");

  var selectMenu = $("<div>").appendTo("#" + id).addClass("select_menu_content");
  var selectAllButton = $("<span>"+labels[0]+"</span>").appendTo(selectMenu);
  selectMenu.append("<br>");
  var selectNothingButton = $("<span>"+labels[1]+"</span>").appendTo(selectMenu);


  var allowCheckAll = true;

  showMenu.click(function() {
    selectTable.toggleClass("select_button_area_clicked");
    selectMenu.toggle();
  });

  selectAllButton.click(function() {
    selectMenu.hide();
    selectTable.removeClass("select_button_area_clicked");
    selectAllHandler(true);
  });

  selectNothingButton.click(function() {
    selectMenu.hide();
    selectTable.removeClass("select_button_area_clicked");
    selectAllHandler(false);
  });

  selectPage.click(function() {
    if (allowCheckAll)
      selectPageHandler();
  });

  this.changeLock = function(flag) {
    if (flag)
      this.lock();
    else
      this.unlock();
  };

  this.lock = function() {
    showMenu.hide();
    selectMenu.hide();
    allowCheckAll = false;
  };

  this.unlock = function() {
    selectMenu.hide();
    showMenu.show();
    allowCheckAll = true;
  };

  selectTable.appendTo("#" + id);
};

// ::::::::::::::::::::::::::::::: КНОПКА ПОКАЗА/СКРЫТИЯ ВСЕХ INNER ROWS :::::::::::::::::::::::::::::::::::::::::::::::

var ToggleButtonControl = function(id, toggleOptions) {
  var toggleFunc = toggleOptions.onChange;
  var button = $("<div>").addClass("eyeline_inner_data_closed");
  button.click(function() {
    var b = $("#" + id+" div:first-child");
    var opened = b.hasClass("eyeline_inner_data_opened");
    if (opened)
      b.removeClass("eyeline_inner_data_opened").addClass("eyeline_inner_data_closed");
    else
      b.removeClass("eyeline_inner_data_closed").addClass("eyeline_inner_data_opened");
    toggleFunc(!opened);
  });
  button.appendTo("#" + id);

  this.isOpened = function() {
    var b = $("#" + id+" div:first-child");
    return b.hasClass("eyeline_inner_data_opened")
  };
  this.open = function() {
    var b = $("#" + id+" div:first-child");
    return b.removeClass("eyeline_inner_data_closed").addClass("eyeline_inner_data_opened");
  };
};


function createDataTableFilter(filterId) {
  var filter = new EyelineDataTableFilterComponent(filterId);
  registerJsfComponent(filterId, filter);
}

function EyelineDataTableFilterComponent(filterId) {
  var actionEl = $("#"+filterId + '_action');
  var visibleEl = $("#"+filterId + '_visible');

  var jform = actionEl.parents("form");

  var _click = function(action) {
    actionEl.val(action);
    $("[name='source']").val(null)
    return jform.submit();
  };

  $("#" + filterId + '_apply').click(function() {_click("apply")});
  $("#" + filterId + '_clear').click(function() {_click("clear")});

  // PRIVATE

  var _setVisible = function(visible) {
    if (visible)
      actionEl.show();
    else
      actionEl.hide();
    visibleEl.val(visible);
  };

  // PUBLIC

  this.setVisible = function(visible) {
    _setVisible(visible);
    return this;
  };

  this.show = function() {
    _setVisible(true);
    return this;
  };

  this.hide = function() {
    _setVisible(false);
    return this;
  };
}function createDynamicTableComponent(tableId, columns, dragRows) {
  var dtable = new EyelineDynamicTableComponent(tableId, columns, dragRows);
  registerJsfComponent(tableId, dtable);
  return dtable;
}

/**
 * Класс TextColumn
 * @param columnId
 */
function TextColumn(columnId, columnClass, allowEditAfterAdd, allowEmpty, maxLength) {
  this.columnId = columnId;
  this.columnClass = columnClass;

  // PRIVATE

  var createInput = function (id, value) {
    var input = document.createElement("input");
    input.name = id;
    input.id = id;
    input.setAttribute("id", id);
    input.setAttribute("value", value);
    input.value = value;
    input.type = "text";
    input.className = "txtW";
    if(maxLength != null) {
      input.maxLength = maxLength;
    }
    return input;
  };

  var getValueElementId = function (tableId, rowNum) {
    return tableId + "_" + rowNum + "_" + columnId;
  };

  // PUBLIC

  this.isAllowedToCreateColumnElement = function (tableId, value) {
    return allowEmpty || value.length != 0;
  };

  this.createColumnElement = function (tableId, newRow, newCount, value) {
    var id = getValueElementId(tableId, newCount);
    var newCell = newRow.insertCell(newRow.cells.length);
    if (columnClass != null && columnClass != '')
      newCell.className = columnClass;

    if (!allowEditAfterAdd) {
      var input = document.createElement("input");
      input.name = id;
      input.id = id;
      input.value = value;
      input.type = "hidden";
      newCell.appendChild(input);
      var span = document.createElement("span");
      span.innerHTML = value;
      newCell.appendChild(span);
    } else {
      newCell.appendChild(createInput(id, value));
    }
  };

  this.createLastColumnElement = function (tableId, newRow) {
    var id = tableId + "_newcell_" + columnId;
    var newCell = newRow.insertCell(newRow.cells.length);
    if (columnClass != null && columnClass != '')
      newCell.className = columnClass;
    newCell.appendChild(createInput(id, ''));
  };

  this.removeColumnElement = function (tableId, rowNum) {
    // do nothing
  };

  this.getLastRowValue = function (tableId) {
    var id = tableId + "_newcell_" + columnId;
    return document.getElementById(id).value;
  };

  this.clearLastRowValue = function (tableId) {
    var id = tableId + "_newcell_" + columnId;
    document.getElementById(id).value = "";
  }
}

/**
 * Класс SelectColumn
 * @param columnId
 * @param values
 */
function SelectColumn(columnId, values, columnClass, allowEditAfterAdd, uniqueValues) {
  this.columnId = columnId;
  this.columnClass = columnClass;

  // PRIVATE

  var getValueElementId = function (tableId, rowNum) {
    return tableId + "_" + rowNum + "_" + columnId;
  };

  var createSelect = function (id, value, values) {
    var select = document.createElement("select");
    select.id = id;
    select.name = id;
    select.className = "selectW";
    for (var i = 0; i < values.length; i++)
      select.options[i] = new Option(values[i].label, values[i].value, i == 0, value == values[i].value);
    return select;
  };

  // PUBLIC

  this.isAllowedToCreateColumnElement = function (tableId, value) {
    if (uniqueValues) {
      var lastColumnElement = document.getElementById(tableId + "_newcell_" + columnId);
      return lastColumnElement.options.length > 0;
    }
    return true;
  };


  function getLabelByValue(value,values) {
    for (var i = 0; i < values.length; i++) {
      var item = values[i];
      if(item.value == value) {
        return item.label;
      }
    }
    return null;
  }

  this.createColumnElement = function (tableId, newRow, newCount, value) {
    var id = getValueElementId(tableId, newCount);
    var newCell = newRow.insertCell(newRow.cells.length);
    if (columnClass != null && columnClass != '') {
      newCell.className = columnClass;
    }
    if (!allowEditAfterAdd) {
      var input = document.createElement("input");
      input.name = id;
      input.id = id;
      input.value = value;
      input.type = "hidden";
      newCell.appendChild(input);
      var span = document.createElement("span");
      span.innerHTML = getLabelByValue(value, values);
      newCell.appendChild(span);
    } else {
      newCell.appendChild(createSelect(id, value, values));
    }

    if (uniqueValues) {
      var lastColumnElement = document.getElementById(tableId + "_newcell_" + columnId);
      var options = lastColumnElement.options;
      for (var i = 0; i < options.length; i++) {
        if (options[i].value == value) {
          options[i] = null;
          break;
        }
      }
    }

  };

  this.clearLastRowValue = function (tableId) {
    var id = tableId + "_newcell_" + columnId;
    document.getElementById(id).selectedIndex = 0;
  };

  this.removeColumnElement = function (tableId, rowNum) {
    if (uniqueValues) {
      var selectId = getValueElementId(tableId, rowNum);
      var select = document.getElementById(selectId);
      var label = $('#' + selectId + ' option:selected').text();
      var lastColumnElement = document.getElementById(tableId + "_newcell_" + columnId);
      var options = lastColumnElement.options;
      options[options.length] = new Option(label, select.value, false, false);
    }
  };

  this.createLastColumnElement = function (tableId, newRow) {
    var id = tableId + "_newcell_" + columnId;
    var newCell = newRow.insertCell(newRow.cells.length);
    if (columnClass != null && columnClass != '') {
      newCell.className = columnClass;
    }
    newCell.appendChild(createSelect(id, '', values));
  };

  this.getLastRowValue = function (tableId) {
    var id = tableId + "_newcell_" + columnId;
    return document.getElementById(id).value;
  }
}


/**
 * Класс DynamicTable
 * @param tableId
 * @param columns
 */
function EyelineDynamicTableComponent(tableId, columns, dragRows) {

  var tableElem = document.getElementById(tableId);
  var tbody = tableElem.getElementsByTagName('tbody')[0];
  var tfoot = tableElem.getElementsByTagName('tfoot')[0];
  if (tfoot === undefined) {
    $('<tfoot>').appendTo(tableElem);
    tfoot = tableElem.getElementsByTagName('tfoot')[0];
  }
  var visibleEl = $(tableId + "_visible");
  var tableInstance = this;

  var listeners = [];

  // PRIVATE

  var init = function () {
    var newCount = tableElem.rows.length;
    // Create new row
    var newRow = tfoot.insertRow(0);
    newRow.className = "eyeline_row" + (newCount & 1);
    newRow.id = tableId + "_newrow";

    if(dragRows) {
      newRow.insertCell(0);
    }

    // Fill row
    for (var i = 0; i < columns.length; i++)
      columns[i].createLastColumnElement(tableId, newRow);

    var image = document.createElement("div");
    image.className = "eyeline_addbutton";
    image.innerHTML = '&nbsp;';

    image.onclick = function (e) {
      var values = new Array();
      for (var i = 0; i < columns.length; i++) {
        values[i] = columns[i].getLastRowValue(tableId);
        columns[i].clearLastRowValue(tableId)
      }

      tableInstance.addRow(values);
    };
    newRow.insertCell(newRow.cells.length).appendChild(image);
  };

  var _setVisible = function (visible) {
    if (visible)
      $(tableElem).show();
    else
      $(tableElem).hide();
    visibleEl.val(visible);
  };

  // PUBLIC

  this.delRow = function (rownum) {

    for (var j = 0; j < columns.length; j++)
      columns[j].removeColumnElement(tableId, rownum);

    var rowId = tableId + "_row_" + rownum;
    var rowElem = tableElem.rows[rowId];
    tableElem.deleteRow(rowElem.rowIndex);
    var count = tableElem.rows.length;

    for (var i = 1; i < count; i++) {
      var row = tableElem.rows[i];
      row.className = "eyeline_row" + ((i + 1) & 1);
    }

    _onEvent({
      type: 'deleted',
      rowNum: rownum
    });
  };

  this.addRow = function (values) {

    for (var j = 0; j < columns.length; j++) {
      if (!columns[j].isAllowedToCreateColumnElement(tableId, values[j]))
        return;
    }

    var newCount = tbody.rows.length;
    // Create new row
    var newRow = tbody.insertRow(newCount);
    newRow.className = "eyeline_row" + (newCount & 1);
    newRow.id = tableId + "_row_" + newCount;

    if(dragRows) {
      var newCell = newRow.insertCell(0);
      newCell.className = 'dragHandle';
    }

    // Fill row
    for (var i = 0; i < columns.length; i++) {
      var value = values[i];
      columns[i].createColumnElement(tableId, newRow, newCount, value);
    }

    var image = document.createElement("div");
    image.className = "eyeline_delbutton";
    image.innerHTML = '&nbsp;';
    image.onclick = function (e) {tableInstance.delRow(newCount);};

    newRow.insertCell(newRow.cells.length).appendChild(image);

    var lastRow = tfoot.rows[tableId + "_newrow"];
    lastRow.className = "eyeline_row" + ((newCount + 1) & 1);

    _onEvent({
      type: 'added'
    });

    if(dragRows) {
      initDragRows();
    }
  };

  this.setVisible = function(visible) {
    _setVisible(visible);
    return this;
  };

  this.show = function() {
    _setVisible(true);
    return this;
  };

  this.hide = function() {
    _setVisible(true);
    return this;
  };

  var _onEvent = function (event) {
    for (var i = 0; i < listeners.length; i++) {
      listeners[i](event);
    }
  };

  /**
   * Registers an event listener, which will be called on any supported
   * table event with a single `event' argument.
   *
   * Event object has a mandatory `type' property denoting event type and an arbitrary
   * number of event-specific fields.
   *
   * @param listener Handler to register.
   * @return {EyelineDynamicTableComponent}
   */
  this.addListener = function (listener) {
    listeners.push(listener);
    return this;
  };

  this.removeListener = function (listener) {
    for(var i = listeners.length - 1; i >= 0; i--) {
      if (listeners[i] === listener) {
        listeners.splice(i, 1);
      }
    }
    return this;
  };

  /**
   * @deprecated Use addListener/removeListener.
   */
  this.change = function (listener) {
    listeners = [listener];
    return this;
  };

  var initDragRows = function() {
    $('#' + tableId + ' tbody').tableDnD({
      dragHandle: ".dragHandle",
      onDragClass: "dragRow",

      onDrop: function(tbody, row) {
        var count = 0;
        $(tbody).find('tr').each(function() {
          this.className = 'eyeline_row' + (count & 1);
          $(this).find('td').children().each(function() {
            var i = $(this);
            var name = i.attr('name');
            if(name != undefined) {
              var _ind = name.indexOf('_', tableId.length + 1);
              var newName = tableId + '_' + count + '_' + name.substring(_ind + 1);
              i.attr('name', newName);
            }
          });
          count++;
        });
      }
    });
  };

  init();

}function createInputText(id, options) {
  var inputText = new EyelineInputTextComponent(id, options);
  registerJsfComponent(id, inputText);
}

function EyelineInputTextComponent(id, options) {

  var _delaySearch = options['delaySearch'];
  var _minSearchLength = options['minSearchLength'];
  var _readonly = options['readonly'];
  var _autocompleteModelExists = options['autocompleteModelExists'];

  $.ajaxSetup({cache: false});


  var inputTextDiv = $('#' + id + "_div");
  var input = $('#' + id);


  var searchValues = function(typed, autoCompleteResponse) {

    var onError = function() {
      autoCompleteResponse();
    };

    var onResponse = function(data, status, resp) {
      if (status != 'success')
        return;


      if (typeof(data) == "object") {
        autoCompleteResponse(data.result);
      }else {
        autoCompleteResponse();
      }
    };


    var _sendRequest = function() {
      var closestForm = input.parents("form");
      var requestUrl = closestForm.attr("action");
      var params = serializeValues(closestForm);
      params["eyelineComponentUpdate"] = id;
      params['user_typed'+id] = typed;
      $.ajax({
        type: 'POST',
        url: requestUrl,
        data: params,
        success: onResponse,
        error: onError
      });
    };

    _sendRequest();
  };


  if(_autocompleteModelExists && !_readonly) {
    input.autocomplete({
      source: function( request, response ) {
        searchValues(request.term, response)
      },
      delay: _delaySearch,
      minLength: _minSearchLength

    });
  }

  this.setVisible = function(visible) {
    inputTextDiv.css("display", visible ? "" : "none");
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

  this.val = function() {
    return input.val();
  };

  this.disable = function() {
    input.attr('disabled', 'disabled');
  };

  this.enable = function() {
    input.removeAttr('disabled');
  };

  this.showError = function(errorText) {
    input.addClass('validationError');
    var nextEl = input.next();
    if(!nextEl.is('span.error')) {
      var spanErr = $('<span class="error"> (!)</span>');
      if(errorText) {
        spanErr.attr('title', errorText);
      }
      input.after(spanErr);
    }
  };

  if(input[0].tagName.toLowerCase()=='textarea' && input.attr('maxlength')) {
    input.keyup(function() {
      var max = parseInt(input.attr('maxlength'));
      if(input.val().length >= max) {
        input.val(input.val().substr(0, max));
      }
    });
  }

  this.focus = function() {
    input.focus();
  };

  // EVENTS

  this.keydown = function(handler) {
    input.keydown(handler);
  };

  this.keyup = function(handler) {
    input.keyup(handler);
  };

  this.keypress = function(handler) {
    input.keypress(handler);
  };

  this.click = function(handler) {
    input.click(handler);
  };

  this.dblclick = function(handler) {
    input.dblclick(handler);
  };

  this.mousedown = function(handler) {
    input.mousedown(handler);
  };

  this.mouseup = function(handler) {
    input.mouseup(handler);
  };

  this.mousemove = function(handler) {
    input.mousemove(handler);
  };

  this.mouseover = function(handler) {
    input.mouseover(handler);
  };

  this.mouseout = function(handler) {
    input.mouseout(handler);
  };

  this.change = function(handler) {
    input.change(handler);
  };

}
function createInputFile(id) {
  var inputFile = new EyelineInputFileComponent(id);
  registerJsfComponent(id, inputFile);
}

function EyelineInputFileComponent(id) {
  var inputFileDiv = $('#' + id + "_div");

  this.setVisible = function(visible) {
    inputFileDiv.css("display", visible ? "" : "none");
    $('#' + id + "_visible").val(visible);
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };
}
function createMessage(elementId) {
  var message = new EyelineMessageComponent(elementId);
  registerJsfComponent(elementId, message);
}

function EyelineMessageComponent(elementId) {

  var divPanel = $("#" + elementId);

  this.setVisible = function(visible) {
    divPanel.css("display", visible ? "" : "none");
    $('#' + elementId + "_visible").val(visible);
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

}
function initLocales() {
  jQuery(function($) {
    $.datepicker.regional['ru'] = {
      closeText: 'Закрыть',
      prevText: '&#x3c;Пред',
      nextText: 'След&#x3e;',
      currentText: 'Сегодня',
      monthNames: ['Январь','Февраль','Март','Апрель','Май','Июнь',
        'Июль','Август','Сентябрь','Октябрь','Ноябрь','Декабрь'],
      monthNamesShort: ['Янв','Фев','Мар','Апр','Май','Июн',
        'Июл','Авг','Сен','Окт','Ноя','Дек'],
      dayNames: ['воскресенье','понедельник','вторник','среда','четверг','пятница','суббота'],
      dayNamesShort: ['вск','пнд','втр','срд','чтв','птн','сбт'],
      dayNamesMin: ['Вс','Пн','Вт','Ср','Чт','Пт','Сб'],
      weekHeader: 'Не',
      dateFormat: 'dd.mm.yy',
      firstDay: 1,
      isRTL: false,
      showMonthAfterYear: false,
      yearSuffix: ''};

    $.datepicker.regional['en'] = {
      closeText: 'Close',
      prevText: '&#x3c;Prev',
      nextText: 'Next&#x3e;',
      currentText: 'Today',
      monthNames: ['January','February','March','April','May','June',
        'July','August','September','October','November','December'],
      monthNamesShort: ['Jan','Feb','Mar','Apr','May','Jun',
        'Jul','Aug','Sep','Oct','Nov','Dec'],
      dayNames: ['sunday','monday','tuesday','wednesday','thursday','friday','saturday'],
      dayNamesShort: ['sun','mon','tue','wed','thu','fri','sat'],
      dayNamesMin: ['SSS','Mo','Tu','We','Th','Fr','Sa'],
      weekHeader: 'Week',
      dateFormat: 'dd.mm.yy',
      firstDay: 1,
      isRTL: false,
      showMonthAfterYear: false,
      yearSuffix: ''};
  });
}

function createInputDate(elementId, value, minDate, maxDate, inputTime, numberOfMonths, locale) {
  initLocales();
  var inputDate = new EyelineInputDateComponent(elementId, value, minDate, maxDate, inputTime, numberOfMonths, locale);
  registerJsfComponent(elementId, inputDate);
}

function EyelineInputDateComponent(elementId, value, minDate, maxDate, inputTime, numberOfMonths, locale) {

  var inputDateDiv = $('#' + elementId + "_div");

  var options = {
    showAnim:"",
    constrainInput : false,
    showOn: "button",
    dateFormat: "dd.mm.yy"
  };

  if (numberOfMonths == 1) {
    options["changeMonth"] = true;
    options["changeYear"] = true;
  } else {
    options["numberOfMonths"] = numberOfMonths;
  }

  if (inputTime) {
    options["beforeShow"] = function(input, inst) {
      this.setAttribute("oldValue", this.value)
    };
    options["onSelect"] = function(dateText, inst) {
      var oldDate = this.getAttribute("oldValue");
      var i = oldDate.indexOf(" ");
      var oldTime = i < 0 ? " 00:00:00" : oldDate.substring(i);
      this.value = dateText + oldTime;
    }
  }

  $("#" + elementId).datepicker(options);

  if (locale == "ru") {
    $("#" + elementId).datepicker("option", $.datepicker.regional['ru']);
  }

  if (minDate)
    $("#" + elementId).datepicker("option", "minDate", minDate);

  if (maxDate)
    $("#" + elementId).datepicker("option", "maxDate", maxDate);

  $("#" + elementId).val(value);


  this.setVisible = function(visible) {
    inputDateDiv.css("display", visible ? "" : "none");
    $('#' + elementId + "_visible").val(visible);
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

  this.minDate = function(d) {
    var e = $("#" + elementId);
    if (d)
      e.datepicker("option", "minDate", d);
    return e.datepicker("option", "minDate");
  };

  this.maxDate = function(d) {
    var e = $("#" + elementId);
    if (d)
      e.datepicker("option", "maxDate", d);
    return e.datepicker("option", "maxDate");
  };
}
function createInputTime(elementId) {
  var inputTime = new EyelineInputTimeComponent(elementId);
  registerJsfComponent(elementId, inputTime);
}

function EyelineInputTimeComponent(elementId) {

  var divPanel = $("#" + elementId);

  this.setVisible = function(visible) {
    divPanel.css("display", visible ? "" : "none");
    $('#' + elementId + "_visible").val(visible);
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

}(function($){
  $.fn.hoverIntent = function(cfg) {

    // cX, cY = current X and Y position of mouse, updated by mousemove event
    // pX, pY = previous X and Y position of mouse, set by mouseover and polling interval
    var cX, cY, pX, pY;

    // A private function for getting mouse position
    var track = function(ev) {
      cX = ev.pageX;
      cY = ev.pageY;
    };

    // A private function for comparing current and previous mouse position
    var compare = function(ev,ob) {
      ob.hoverIntent_t = clearTimeout(ob.hoverIntent_t);
      // compare mouse positions to see if they've crossed the threshold
      if ( ( Math.abs(pX-cX) + Math.abs(pY-cY) ) < cfg.sensitivity ) {
        $(ob).unbind("mousemove",track);
        // set hoverIntent state to true (so mouseOut can be called)
        ob.hoverIntent_s = 1;
        return cfg.over.apply(ob,[ev]);
      } else {
        // set previous coordinates for next time
        pX = cX; pY = cY;
        // use self-calling timeout, guarantees intervals are spaced out properly (avoids JavaScript timer bugs)
        ob.hoverIntent_t = setTimeout( function(){compare(ev, ob);} , cfg.interval );
      }
    };

    // A private function for delaying the mouseOut function
    var delay = function(ev,ob) {
      ob.hoverIntent_t = clearTimeout(ob.hoverIntent_t);
      ob.hoverIntent_s = 0;
      return cfg.out.apply(ob,[ev]);
    };

    var handleMouseOver = function(e) {
      var p = e.fromElement || e.relatedTarget;
      while ( p && p != this ) { try { p = p.parentNode; } catch(e) { p = this; } }
      if ( p == this ) { return false; }

      var ev = jQuery.extend({},e);
      var ob = this;
      if (ob.hoverIntent_t) { ob.hoverIntent_t = clearTimeout(ob.hoverIntent_t); }

      pX = ev.pageX; pY = ev.pageY;
      // update "current" X and Y position based on mousemove
      $(ob).bind("mousemove",track);
      // start polling interval (self-calling timeout) to compare mouse coordinates over time
      if (ob.hoverIntent_s != 1) { ob.hoverIntent_t = setTimeout( function(){compare(ev,ob);} , cfg.interval );}
    };

    var handleMouseOut = function(e) {
      var p = e.toElement || e.relatedTarget;
      while ( p && p != this ) { try { p = p.parentNode; } catch(e) { p = this; } }
      if ( p == this ) { return false; }

      var ev = jQuery.extend({},e);
      var ob = this;

      if (ob.hoverIntent_t) { ob.hoverIntent_t = clearTimeout(ob.hoverIntent_t); }

      // unbind expensive mousemove event
      $(ob).unbind("mousemove",track);
      // if hoverIntent state is true, then call the mouseOut function after the specified delay
      if (ob.hoverIntent_s == 1) { ob.hoverIntent_t = setTimeout( function(){delay(ev,ob);} , cfg.timeout );}
    };

    // bind the function to the two event listeners
    return this.mouseover(handleMouseOver).mouseout(handleMouseOut);
  };

})(jQuery);

function Menu(el) {

  var mainMenuConfig = {
    sensitivity: 7, // number = sensitivity threshold (must be 1 or higher)
    interval: 10,  // number = milliseconds for onMouseOver polling interval
    timeout: 10,   // number = milliseconds delay before onMouseOut
    over : doOpenMainMenu,
    out : doClose
  };

  function doOpenMainMenu() {
    var v = $('ul:first',this);
    v.css('position', 'absolute');
    v.css("left", $(this).position().left);
    v.css("top", $(this).position().top + $(this).height());
    v.css('visibility', 'visible');
  }

  function doClose() {
    $('ul:first',this).css('visibility', 'hidden');
  }

  el.hoverIntent(mainMenuConfig);

  function doOpenSubMenu() {
    var v = $('ul:first',this)
    v.css('position', 'absolute');
    v.css("left", $(this).position().left + $(this).width());
    v.css("top", $(this).position().top);
    v.css('visibility', 'visible');
  }

  var subMenuConfig = {
    sensitivity: 7, // number = sensitivity threshold (must be 1 or higher)
    interval: 10,  // number = milliseconds for onMouseOver polling interval
    timeout: 200,   // number = milliseconds delay before onMouseOut
    over : doOpenSubMenu,
    out : doClose
  };

  el.find("li").hoverIntent(subMenuConfig);
  el.find("ul li:has(ul)").find("a:first").append(" &raquo; ");

  var uls = el.find("ul");
  $.each(uls, function(index, value){
    var lis = $(value).children("li");
    var maxWidth = 0;
    $.each(lis, function(idx, val) {
      $(val).css("border-style", "solid");
      if (idx == 0)
        $(val).css("border-width",  "1px 1px 0px 1px");
      else if (idx == lis.length -1)
        $(val).css("border-width",  "0px 1px 1px 1px");
      else
        $(val).css("border-width",  "0px 1px 0px 1px");

      if ($(val).hasClass("menubardelimiter"))
        $(val).css("border-bottom-width", "1px");

      if ($(val).width() > maxWidth)
        maxWidth = $(val).width();
    });
    $(value).css("width", maxWidth + 10 + "px");
  });
}
function createTabs(elementId, autoSelectClass) {
  var tabs = new EyelineTabsComponent(elementId, autoSelectClass);
  registerJsfComponent(elementId, tabs);

}

function createTab(elementId) {
  var tab = new EyelineTabComponent(elementId);
  registerJsfComponent(elementId, tab);
}

function EyelineTabComponent(elementId) {

  var element = $("#" + elementId);
  var elementHeader = $("#" + elementId+"_header");

  this.setVisible = function(visible) {
    element.css("display", visible ? "" : "none");
    elementHeader.css("display", visible ? "" : "none");
    $('#' + elementId + "_visible").val(visible);
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

}


function foundGraphs(body) {
  var gs = jsfcGraphs();
  var el = $(body);
  for(var index in gs){
    if(el.find("#"+index).length > 0) {
      gs[index].replot();
    }
  }
}

function EyelineTabsComponent(elementId, autoSelectClass) {

  $("#" + elementId).tabs();

  var element = $("#" + elementId);

  var lastElement = $("#"+elementId+"_lastSelected");

  var last = lastElement.val();

  if(last != null && last != '') {
    element.tabs('select', parseInt(last));
  }

  element.bind('tabsshow', function(event, ui) {
    foundGraphs(ui.panel)
  });

  element.bind('tabsselect', function(event, ui) {
    lastElement.val(ui.index);
    return true;
  });

  if(autoSelectClass != null && autoSelectClass != '') {
    var errorIndex = -1;
    element.children("div").each(function(index, kid){
      $(kid).find("." + autoSelectClass).each(function(){
        errorIndex = index;
        var _id = kid.getAttribute('id');
        $('#'+_id+'_header').find('a').addClass('tabs_select_error_tab');
      });
    });

    if(errorIndex != -1) {
      element.tabs('select', errorIndex);
    }
  }

  this.setVisible = function(visible) {
    element.css("display", visible ? "" : "none");
    $('#' + elementId + "_visible").val(visible);
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

}

function createPageCalendar(id) {
  var calendar = new EyelinePageCalendarComponent(id);
  registerJsfComponent(id, calendar);
}


function EyelinePageCalendarComponent(contentId) {

  var bodyElement = $("#"+contentId);
  var closestForm = bodyElement.parents("form");
  var requestUrl = closestForm.attr("action");

  var dateElement = document.getElementById(contentId + '_date');
  var updateUsingSubmit = false;

  /**
   * Обновляет содержимое
   */
  this.setDate = function (date) {

    dateElement.value=date;

    if (updateUsingSubmit)
      return closestForm.submit();

    var onResponse = function(text) {
      bodyElement.html(text);
    };

    var params = serializeForm(closestForm);
    params["eyelineComponentUpdate"] = contentId;
    $.ajaxSetup({cache: false});
    $.post(requestUrl, params, onResponse);
  };

  this.setVisible = function(visible) {
    bodyElement.css("display", visible ? "" : "none");
    $('#' + contentId + "_visible").val(visible);
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };


}function createUpdatableContent(contentId, updatePeriod) {
  var content = new EyelineUpdatableContentComponent(contentId, updatePeriod);
  registerJsfComponent(contentId, content);
}

function EyelineUpdatableContentComponent(contentId, updatePeriod) {

  var c = $("#" + contentId);

  c.bind("updateContent", function() { _update() });

  if (updatePeriod > 0)
    window.setTimeout(function(){c.trigger("updateContent")}, updatePeriod);

  var progressOverlay = new ProgressUpdOverlay(c);

  var _update = function(showOverlay, hideOverlayTimeout, submitNewValues, callback) {
    var closestForm = c.parents("form");
    if (closestForm == null)
      return;

    if(showOverlay) {
      progressOverlay.show();
    }

    var formValues;
    if(submitNewValues)
      formValues = serializeValues(closestForm);
    else
      formValues = serializeForm(closestForm);

    formValues["eyelineComponentUpdate"] = contentId;

    $.ajaxSetup({cache: false});
    $.post(closestForm.attr("action"), formValues, function(data, status, resp) {
      if (status == 'success') {
        c.html(data);
        var nextUpdateTimeout = resp.getResponseHeader("nextUpdateTimeout");
        if (nextUpdateTimeout != null && nextUpdateTimeout != "0") {
          window.setTimeout(function(){c.trigger("updateContent")}, nextUpdateTimeout);
        }

        if(showOverlay) {
          if(hideOverlayTimeout)
            setTimeout(function() { progressOverlay.hide() }, hideOverlayTimeout);
          else
            progressOverlay.hide();
        }
      }
      if(callback) {
        callback();
      }
    });
  };

  this.update = function(showOverlay, hideOverlayTimeout, submitNewValues, callback) {
    _update(showOverlay, hideOverlayTimeout, submitNewValues, callback);
  };

  this.setVisible = function(visible) {
    c.css("display", visible ? "" : "none");
    $('#' + contentId + "_visible").val(visible);
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

}

// ::::::::::::::::::::::::::::::::::::::::: ОВЕРЛЕЙ С ПРОГРЕССОМ ::::::::::::::::::::::::::::::::::::::::::::::::::::::

var ProgressUpdOverlay = function(parent) {

  var progressElement = $("<table>").addClass("eyeline_upd_content_overlay");
  $('<tr><td><div class="eyeline_upd_content_loading"></div></td></tr>').appendTo(progressElement);

  progressElement.insertAfter(parent);

  var showOverlay = function() {
    var x = parent.position().left;
    var y = parent.position().top;
    var w = parent[0].offsetWidth;
    var h = parent[0].offsetHeight;
    progressElement.css("left", x + 'px').css("top", y + 'px').css("width", w + 'px').css("height", h + 'px');
    progressElement.show();
  };

  this.show = function() {
    showOverlay();
  };

  this.hide = function() {
    progressElement.hide();
  }
};

function createProgressBar(elementId) {
  var pb = new EyelineProgressBarComponent(elementId);
  registerJsfComponent(elementId, pb);
}

function EyelineProgressBarComponent(elementId) {

  var divPanel = $("#" + elementId);

  this.setVisible = function(visible) {
    divPanel.css("display", visible ? "" : "none");
    $('#' + elementId + "_visible").val(visible);
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

}/**
 * Хелпер для отрисовки графиков
 * @param contentId идентификатор блока, в котором надо рисовать
 *
 *
 */

function createLineChart(contentId, updatePeriod, options) {
  var c = new LineChart(contentId, updatePeriod, options);

  registerJsfGraphComponent(contentId, c);
}

function LineChart(contentId, updatePeriod, options) {

  var bodyElement = $("#"+contentId);
  var divElement = $("#"+contentId+"_div");
  var closestForm = bodyElement.parents("form");
  var requestUrl = closestForm.attr("action");
  var datePattern = options['x_dateStyle'];

  var immediatlyRender = options['immediatly_render'];

  var checkUpdate = function() {
    window.setTimeout(
        callUpdate, updatePeriod * 1000)
  };

  var g = new LineGraph(options);

  this.setVisible = function(visible) {
    if(visible) {
      divElement.show();
      g.replot();
    }else {
      divElement.hide();
    }
    $('#' + contentId + "_visible").val(visible);
  };

  this.update = function() {
    callUpdate();
  };


  this.replot = function() {
    g.replot();
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

  function LineGraph(options) {

    var element = $('#'+contentId);


    var plot1;

    this.replot = function() {
      if(plot1 != null) {
        plot1.replot();
      }
    };

    function get_ticks_numb(userInt, userTicks, points_number) {
      var ticks_numb = null;
      if(userInt != null && userInt) {
        ticks_numb = 10;
      }
      if(userTicks != null) {
        ticks_numb = userTicks;
      }

      if(ticks_numb != null) {
        ticks_numb = Math.min(ticks_numb, points_number);
      }
      return ticks_numb;
    }


    function parseDate(date) {
      var dateAndTime = date.split(" ");
      var yearMonthDay = dateAndTime[0].split("-");
      var year = parseInt(yearMonthDay[0],10);
      var month = parseInt(yearMonthDay[1],10);
      var day = parseInt(yearMonthDay[2],10);
      var hour,minute;

      var hourAndMin = dateAndTime[1].split(":");
      hour = parseInt(hourAndMin[0],10);
      var minStr = hourAndMin[1];
      minute = parseInt(minStr.substring(0,2),10);
      var ampm = minStr.substring(2);
      if(ampm == 'AM') {
        if(hour == 12) {
          hour = 0;
        }else {
          hour+=12;
        }
      }

      return new Date(year, month-1, day, hour,minute,0,0);
    }

    function formatDate(tmp) {
      var res = ""+tmp.getFullYear()+"-";
      if(tmp.getMonth()+1<10) {
        res+="0"
      }
      res+=(tmp.getMonth()+1)+"-";
      if(tmp.getDate()<10) {
        res+="0"
      }
      res+=tmp.getDate();
      var ampmS;
      var hourS;
      if(tmp.getHours()<10) {
        if(tmp.getHours() == 0) {
          hourS = "12";
        }else {
          hourS = "0"+tmp.getHours();
        }
        ampmS = "AM";
      }else if(tmp.getHours()<=12 && tmp.getHours()>=10) {
        hourS = "" + tmp.getHours(); ampmS = "AM";
      }else {
        hourS = "" + (tmp.getHours() - 12); ampmS = "PM";
      }
      res+=" "+hourS+":";
      if(tmp.getMinutes()<10) {
        res+="0";
      }
      res+=tmp.getMinutes()+ampmS;
      return res;
    }

    function parseInterval(interval) {
      var arr = interval.split(" ");
      return {val:parseInt(arr[0],10), type:arr[1], toString:function(){return this.val+" "+this.type}};
    }

    function count_date_ticks(minDate, maxDate, interval) {
      var tmp = new Date(minDate.getTime());
      var countTicks = 0;
      while(tmp.getTime() < maxDate.getTime()) {
        countTicks++;
        if(interval.type == "month") {
          tmp.setMonth(tmp.getMonth()+interval.val);
        }else if (interval.type == "day") {
          tmp.setDate(tmp.getDate() + interval.val);
        }else { //hour
          tmp.setHours(tmp.getHours() + interval.val);
        }
      }
      return countTicks+1;
    }


    function get_date_ticks(max_ticks, minP, maxP, _interval) {
      if(max_ticks <= 0) {
        return [];
      }
      if(minP == maxP || max_ticks == 1) {
        return minP;
      }
      var minDate = parseDate(minP);
      var maxDate = parseDate(maxP);
      var interval = parseInterval(_interval);
      var countTicks;

      while((countTicks = count_date_ticks(minDate, maxDate, interval)) > max_ticks) {
        interval.val=2*interval.val;
      }

      var ticks = [];
      var tmp = new Date(minDate.getTime());
      for(var i=0;i<countTicks-1;i++) {
        ticks[i] = formatDate(tmp);
        if(interval.type == "month") {
          tmp.setMonth(tmp.getMonth()+interval.val);
        }else if (interval.type == "day") {
          tmp.setDate(tmp.getDate() + interval.val)
        }else { //hour
          tmp.setHours(tmp.getHours() + interval.val)
        }
      }
      ticks[countTicks-1] = maxP;
      return ticks;
    }


    function get_ticks(ticks_numb, userInt, minP, maxP) {
      var ticks = [];
      if(ticks_numb != null) {
        var interval;
        if(userInt != null && userInt) {
          var tmp = parseInt(minP,10);
          if(tmp > minP) {
            tmp--;
          }
          minP = tmp;
          tmp = parseInt(maxP,10);
          if(tmp < maxP) {
            tmp++;
          }
          maxP = tmp;

          interval = Math.max(parseInt((maxP - minP)/ticks_numb, 10),1);
        }else {
          interval = (maxP - minP) / ticks_numb;
        }

        if(interval>0) {
          var t = -1;
          do {
            t++;
            ticks[t] = minP + (t*interval);
            if(maxP - ticks[t] < interval) {
              break;
            }
          }while(ticks[t]<maxP);
          ticks[t]=maxP
        }else {
          ticks[0] = minP;
        }
      }
      return ticks;
    }

    this.drawLines = function(response) {
      if(response == null) {
        return;
      }

      var responseObject = !(/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(
          response.replace(/"(\\.|[^"\\])*"/g, ''))) &&
          eval('(' + response + ')');

      element.empty();
      var lines = responseObject.lines;

      var points = [];
      var legend = [];
      var color = [];

      var userMinX = responseObject.minx;
      var userMaxX = responseObject.maxx;
      var userMinY = responseObject.miny;
      var userMaxY = responseObject.maxy;

      var userXint = options['x_int'];
      var userXticks = options['x_ticks'];

      var userYint = options['y_int'];
      var userYticks = options['y_ticks'];

      var tickInterval = options['tickInterval'];

      var minX, maxX, minY, maxY;
      var x_points_number = 0;
      var y_points_number = 0;
      var y_set = new Object();

      for(var i = 0; i < lines.length; ++i) {

        var line = [];
        var j = 0;

        var x_i_numb = 0;
        for(var k in lines[i].values) {
          var y = lines[i].values[k];
          line[j++] = [k,y];
          var xD = datePattern != null && datePattern != '' ? k : parseFloat(k);
          var yD = parseFloat(y);
          if(userMinX == null)
            if(minX == null || minX > xD)
              minX = xD;
          if(userMaxX == null)
            if(maxX == null || maxX < xD)
              maxX = xD;
          if(userMinY == null) minY = minY == null ? yD : Math.min(minY, yD);
          if(userMaxY == null) maxY = maxY == null ? yD : Math.max(maxY, yD);

          x_i_numb++;

          var y_str = y+'';
          if(!(y_str in y_set)) {
            y_set[y_str] = 1;
            y_points_number++;
          }

        }
        if(x_i_numb > x_points_number) {
          x_points_number = x_i_numb;
        }

        points[i] = line;
        legend[i] = {label:lines[i].legend, showMarker:lines[i].showMarker=='true'};
        color[i] = lines[i].color;
      }


      if(points.length == 0) {
        return;
      }

      if(minX == null) minX = 0;
      if(maxX == null) maxX = 0;
      if(minY == null) minY = 0;
      if(maxY == null) maxY = 0;

      if(datePattern == null || datePattern == '') {
        minX = userMinX == null ? minX - (minX/10) : parseFloat(userMinX, 10);
        maxX = userMaxX == null ? maxX + (maxX/10) : parseFloat(userMaxX, 10);
      }else {
        minX = userMinX == null ? minX : userMinX;
        maxX = userMaxX == null ? maxX : userMaxX;
      }
      minY = userMinY == null ? minY - (minY/10) : parseFloat(userMinY, 10);
      maxY = userMaxY == null ? maxY + (maxY/10) : parseFloat(userMaxY, 10);

      var y_ticks_numb = get_ticks_numb(userYint, userYticks, y_points_number);

      if(minX == maxX && (datePattern == null || datePattern == '')) {
        minX = Math.max(minX-1,0);
      }
      if(minY == maxY) {
        maxY=minY+1;
      }

      var x_ticks; var x_ticks_numb;
      if(datePattern == null || datePattern == '') {
        x_ticks_numb = get_ticks_numb(userXint, userXticks, x_points_number);
        x_ticks = get_ticks(x_ticks_numb, userXint, minX, maxX);
      }else{
        if(tickInterval != null) {
          var max_ticks = userXticks == null ? 20 : userXticks;
          x_ticks = get_date_ticks(max_ticks, minX, maxX, tickInterval);
        }else {
          x_ticks_numb = userXticks == null ? null : Math.min(userXticks, x_points_number);
        }
      }

      var y_ticks = get_ticks(y_ticks_numb, userYint, minY, maxY);

      var plotOptions = {
        seriesColors: color,
        series:legend,
        legend: {
          show: false,
          location: 'e'
        },
        axes:{
          xaxis:
              datePattern != null && datePattern != "" ? (
                  minX == maxX ?
                  {
                    renderer:$.jqplot.DateAxisRenderer,
                    tickOptions:{formatString: datePattern}
                  }
                      : x_ticks != null ?
                  {
                    renderer:$.jqplot.DateAxisRenderer,
                    tickOptions:{formatString: datePattern},
                    ticks : x_ticks
                  }
                      : x_ticks_numb != null ?
                  {
                    renderer:$.jqplot.DateAxisRenderer,
                    tickOptions:{formatString: datePattern},
                    min:minX,
                    max:maxX,
                    numberTicks: x_points_number
                  }
                      :
                  {
                    renderer:$.jqplot.DateAxisRenderer,
                    tickOptions:{formatString: datePattern},
                    min:minX,
                    max:maxX
                  }
                  )
                  : x_ticks_numb == null ? (
                  userXint ? {min:minX, max:maxX, tickOptions:{formatString: '%d'}}
                      : {min:minX, max:maxX}
                  )
                  : ( userXint ?  {ticks : x_ticks, tickOptions:{formatString: '%d'}}
                  : {ticks : x_ticks}
                  ),
          yaxis:
              y_ticks_numb == null ? (
                  userYint ? {min:minY, max:maxY, tickOptions:{formatString: '%d'}}
                      : {min:minY, max:maxY}
                  ) : (
                  userYint ?  {ticks : y_ticks, tickOptions:{formatString: '%d'}}
                      : {ticks : y_ticks}
                  )
        },
        highlighter: {
          show: true,
          tooltipAxes : 'y',
          tooltipFormatString: '%s',
          useAxesFormatters: false
        }
      };

      if(plot1 != null) plot1.destroy();
      plot1 = $.jqplot(contentId, points, plotOptions);
    }
  }

  var callUpdate = function () {

    var onResponse = function(text, status, resp) {
      g.drawLines(text);
      if(updatePeriod>0) {
        checkUpdate();
      }
    };


    var params = serializeValues(closestForm);
    params["eyelineComponentUpdate"] = contentId;

    $.ajaxSetup({cache: false});
    $.post(requestUrl, params, onResponse);
  };

  $(function() {
    if(immediatlyRender != null) {
      g.drawLines(immediatlyRender);
    }else {
      callUpdate();
    }
  });

}/**
 * Хелпер для отрисовки графиков
 * @param contentId идентификатор блока, в котором надо рисовать
 *
 *
 */

function createBarChart(contentId, updatePeriod, options) {
  var c = new BarChart(contentId, updatePeriod, options);

  registerJsfGraphComponent(contentId, c);
}

function BarChart(contentId, updatePeriod, options) {

  var bodyElement = $("#"+contentId);
  var divElement = $("#"+contentId+"_div");
  var closestForm = bodyElement.parents("form");
  var requestUrl = closestForm.attr("action");
  var horizontal = options['horizontal'];
  var stackMode = options['stackMode'];
  var number_ticks = options['ticks'];
  var intValues = options['intValues'];
  var immediatlyRender = options['immediatly_render'];

  var checkUpdate = function() {
    window.setTimeout(
        callUpdate, updatePeriod * 1000)
  };

  var g = new BarGraph();

  this.setVisible = function(visible) {
    if(visible) {
      divElement.show();
      g.replot();
    }else {
      divElement.hide();
    }
    $('#' + contentId + "_visible").val(visible);
  };

  this.update = function() {
    callUpdate();
  };


  this.replot = function() {
    g.replot();
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

  function BarGraph() {

    var element = $('#'+contentId);


    var plot1;

    this.replot = function() {
      if(plot1 != null) {
        plot1.replot();
      }
    };

    function get_ticks_numb(userInt, userTicks, points_number) {
      var ticks_numb = null;
      if(userInt != null && userInt) {
        ticks_numb = 10;
      }
      if(userTicks != null) {
        ticks_numb = userTicks;
      }

      if(ticks_numb != null) {
        ticks_numb = Math.min(ticks_numb, points_number);
      }
      return ticks_numb;
    }


    function get_ticks(ticks_numb, userInt, minP, maxP) {
      var ticks = [];
      if(minP>0) {
        minP = 0;
      }
      if(ticks_numb != null) {
        var interval;
        if(userInt != null && userInt) {
          var tmp = parseInt(minP);
          if(tmp > minP) {
            tmp--;
          }
          minP = tmp;
          tmp = parseInt(maxP);
          if(tmp < maxP) {
            tmp++;
          }
          maxP = tmp;

          interval = Math.max(parseInt((maxP - minP)/ticks_numb),1);
        }else {
          interval = (maxP - minP) / ticks_numb;
        }
        if(interval>0) {
          var t = -1;
          do {
            t++;
            ticks[t] = minP+(t*interval);
          }while(ticks[t]<maxP);
        }else {
          ticks[0] = minP;
        }
      }
      return ticks;
    }

    this.drawBars = function(response) {
      if(response == null) {
        return;
      }

      var responseObject = !(/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(
          response.replace(/"(\\.|[^"\\])*"/g, ''))) &&
          eval('(' + response + ')');

      element.empty();
      var bars = responseObject.bars;
      var ticks = responseObject.ticks;

      var points = [];
      var legend = [];
      var color = [];

      var minY, maxY;

      var y_points_number = 0;
      var value_set = new Object();
      for(var i=0; i<bars.length; i++) {
        var bar = [];
        var j = 0;

        for(var key in bars[i].values) {
          var value = bars[i].values[key];
          var valueD = parseFloat(value);
          bar[j++] =  horizontal ? [valueD, key] : [key, valueD];

          minY = minY == null ? valueD : Math.min(minY, valueD);
          maxY = maxY == null ? valueD : Math.max(maxY, valueD);

          var value_str = value+'';
          if(!(value_str in value_set)) {
            value_set[value_str] = 1;
            y_points_number++;
          }
        }

        points[i] = bar;
        legend[i] = {label:bars[i].legend};
        color[i] = bars[i].color;
      }

      if(points.length == 0) {
        return;
      }

      var y_ticks_numb = get_ticks_numb(intValues, number_ticks, y_points_number);

      var y_ticks = get_ticks(y_ticks_numb, intValues, minY, maxY);

      plot1 = $.jqplot(contentId, points, {
        stackSeries : stackMode,
        seriesColors: color,
        series:legend,
        legend: {
          show: false,
          location: 'e'
        },
        seriesDefaults:{
          renderer:$.jqplot.BarRenderer,
          rendererOptions: {fillToZero: true, barDirection: horizontal ? 'horizontal' : 'vertical'}
        },
        axes:{
          xaxis:
              !horizontal ? {
                renderer:$.jqplot.CategoryAxisRenderer,
                ticks: ticks
              } :
                  y_ticks_numb == null ? (
                      intValues ? {tickOptions:{formatString: '%d'}}
                          : {}
                      ) : (
                      intValues ?  {ticks : y_ticks, tickOptions:{formatString: '%d'}}
                          : {ticks : y_ticks}
                      ),
          yaxis:
              horizontal ? {
                renderer:$.jqplot.CategoryAxisRenderer,
                ticks: ticks
              } :
                  y_ticks_numb == null ? (
                      intValues ? {tickOptions:{formatString: '%d'}}
                          : {}
                      ) : (
                      intValues ?  {ticks : y_ticks, tickOptions:{formatString: '%d'}}
                          : {ticks : y_ticks}
                      )
        },
        highlighter: {
          show: true,
          tooltipAxes : horizontal ? "x" : "y",
          tooltipFormatString: '%s',
          useAxesFormatters: false
        }
      });

    }
  }

  var callUpdate = function () {

    var onResponse = function(text, status, resp) {
      g.drawBars(text);
      if(updatePeriod>0) {
        checkUpdate();
      }
    };


    var params = serializeValues(closestForm);
    params["eyelineComponentUpdate"] = contentId;

    $.ajaxSetup({cache: false});
    $.post(requestUrl, params, onResponse);
  };

  $(function() {
    if(immediatlyRender != null) {
      g.drawBars(immediatlyRender);
    }else {
      callUpdate();
    }
  });


}/**
 * Хелпер для отрисовки графиков
 * @param contentId идентификатор блока, в котором надо рисовать
 *
 *
 */

function createPieChart(contentId, updatePeriod, options) {
  var c = new PieChart(contentId, updatePeriod, options);

  registerJsfGraphComponent(contentId, c);
}

function PieChart(contentId, updatePeriod, options) {

  var bodyElement = $("#"+contentId);
  var divElement = $("#"+contentId+"_div");
  var closestForm = bodyElement.parents("form");
  var requestUrl = closestForm.attr("action");

  var immediatlyRender = options['immediatly_render'];

  var checkUpdate = function() {
    window.setTimeout(
        callUpdate, updatePeriod * 1000)
  };

  var g = new PieGraph();

  this.setVisible = function(visible) {
    if(visible) {
      divElement.show();
      g.replot();
    }else {
      divElement.hide();
    }
    $('#' + contentId + "_visible").val(visible);
  };

  this.update = function() {
    callUpdate();
  };


  this.replot = function() {
    g.replot();
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

  function PieGraph() {

    var element = $('#'+contentId);


    var plot1;

    this.replot = function() {
      if(plot1 != null) {
        plot1.replot();
      }
    };

    this.drawPie = function(response) {
      if(response == null) {
        return;
      }

      var responseObject = !(/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(
          response.replace(/"(\\.|[^"\\])*"/g, ''))) &&
          eval('(' + response + ')');

      element.empty();
      var pie = responseObject.pie;

      var points = [];
      var color = [];


      for(var i=0; i<pie.length; i++) {

        points[i] = [pie[i].legend, pie[i].value];
        color[i] = pie[i].color;

      }

      if(points.length == 0) {
        return;
      }

      plot1 = $.jqplot(contentId, [points], {
        seriesColors: color,
        seriesDefaults: {
          // Make this a pie chart.
          renderer: jQuery.jqplot.PieRenderer,
          rendererOptions: {
            // Put data labels on the pie slices.
            // By default, labels show the percentage of the slice.
            showDataLabels: true
          }
        },
        legend: { show:false, location: 'e' }
      });

    }
  }

  var callUpdate = function () {

    var onResponse = function(text, status, resp) {
      g.drawPie(text);
      if(updatePeriod>0) {
        checkUpdate();
      }
    };


    var params = serializeValues(closestForm);
    params["eyelineComponentUpdate"] = contentId;

    $.ajaxSetup({cache: false});
    $.post(requestUrl, params, onResponse);
  };

  $(function() {
    if(immediatlyRender != null) {
      g.drawPie(immediatlyRender);
    }else {
      callUpdate();
    }
  });


}
function createSelectManyShuttle(id) {
  var selectManyShuttle = new EyelineSelectManyShuttleComponent(id);
  registerJsfComponent(id, selectManyShuttle);
}

function EyelineSelectManyShuttleComponent(id) {
  var selectManyShuttleDiv = $('#' + id + "_div");

  this.setVisible = function(visible) {
    selectManyShuttleDiv.css("display", visible ? "" : "none");
    $('#' + id + "_visible").val(visible);
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

  var selectButton = $('#' + id + '_select');
  selectButton.click(function() {
    $('#' + id + '_unselectedList option:selected').remove().appendTo('#' + id + '_selectedList');
    updateSelectedValuesInput(id);
    deselectOptions(id, '_selectedList');
  });

  var selectAllButton = $('#' + id + '_selectAll');
  selectAllButton.click(function() {
    $('#' + id + '_unselectedList option').remove().appendTo('#' + id + '_selectedList');
    updateSelectedValuesInput(id);
    deselectOptions(id, '_selectedList');
  });

  var unselectButton = $('#' + id + '_unselect');
  unselectButton.click(function() {
    $('#' + id + '_selectedList option:selected').remove().appendTo('#' + id + '_unselectedList');
    updateSelectedValuesInput(id);
    deselectOptions(id, '_unselectedList');
  });

  var unselectAllButton = $('#' + id + '_unselectAll');
  unselectAllButton.click(function() {
    $('#' + id + '_selectedList option').remove().appendTo('#' + id + '_unselectedList');
    updateSelectedValuesInput(id);
    deselectOptions(id, '_unselectedList');
  });
}

function updateSelectedValuesInput(id) {
  var selectedValues = '';
  $('#' + id + '_selectedList option').each(function() {
    selectedValues += $(this).val() + ',';
  });
  var selectedValuesInput = $('#' + id + '_selectedItemsId');
  selectedValuesInput.attr("value", selectedValues);
}

function deselectOptions(id, listSuffix) {
  $('#' + id+listSuffix + ' option:selected').attr("selected", false);
}
function createSelectManyChecklist(id, options) {
  var selectManyChecklist = new EyelineSelectManyChecklistComponent(id, options);
  registerJsfComponent(id, selectManyChecklist);
}

function EyelineSelectManyChecklistComponent(_id, options) {
  var bodyId = _id + '_body';
  var bodyDiv = $('#' + bodyId);
  var selectAllBtn = $('#' + _id + '_selectAll');
  var values, selected;
  var changeHandler;

  /**
   * Переинициализирует список. Если вызвать reset() без параметров, то список будет очищен.
   * @param values массив всех возможных значений. Каждый элемент этого массива должен иметь методы bodyId() и value().
   *               Как вариант, массив может быть заполнен объектами типа SelectItem.
   * @param selectedIds массив идентификаторов выделенных значений.
   */
  this.reset = function(values, selectedIds) {
    bodyDiv.empty();
    if (!values)
      return;
    $.each(values, function() {
      var checkboxId = _id + '_' + this.id();
      var checked = selectedIds && $.inArray(this.id(), selectedIds) >= 0;
      bodyDiv.append( $(
          '<div id="'+ bodyId + '_' + $.trim(this.id())+'_div" class="ftypediv">' +
              '<input type="checkbox" name="'+checkboxId+'" id="'+checkboxId+'" value="'+this.id()+'"' + (checked ? ' checked="checked"' : '') + "/>" +
              '<label for="'+checkboxId+'">'+this.value()+'</label>' +
              '</div>'
      ));
    });
    this.values=values;
    this.selected = selectedIds;
    if (changeHandler)
      _change(changeHandler);
  };

  this.disable = function() {
    bodyDiv.find('input:checkbox').attr('disabled', 'disabled');
    selectAllBtn.attr('disabled', 'disabled');
  }

  this.enable = function() {
    bodyDiv.find('input:checkbox').removeAttr('disabled');
    selectAllBtn.removeAttr('disabled');
  }

  /**
   * Меняет видимость списка
   */
  this.setVisible = function(visible) {
    if (visible)
      bodyDiv.show();
    else
      bodyDiv.hide();
  };

  /**
   * Показывает список
   */
  this.show = function() {
    this.setVisible(true);
  };

  /**
   * Прячет список
   */
  this.hide = function() {
    this.setVisible(false);
  };

  /**
   * Фильтрует элементы списка. Пробегает по элементам списка, на каждом из них вызывает finterFunc(bodyId, value).
   * Если в результате true - элемент списка отображается, иначе прячется.
   * @param filterFunc функция фильтрации
   */
  this.filter = function(filterFunc) {
    $.each(this.values, function() {
      if (!filterFunc(this.id(), this.value()))
        $('#' + bodyId + '_' + $.trim(this.id()) + "_div").hide();
      else
        $('#' + bodyId + '_' + $.trim(this.id()) + "_div").show();
    })
  };

  /**
   * Возвращает выбранные значения списка
   * @returns {Array} массив объектов типа SelectItem, содержащий все выделенные значения.
   */
  this.selectedItems = function() {
    var result = [];
    bodyDiv.find('input:checkbox:checked').each(function(idx, _el) {
      var el = $(_el);
      var checkedId = el.val();
      var checkedValue = bodyDiv.find('label[for="' + el.attr('id') + '"]').text();
      result[result.length] = new SelectItem(checkedId, checkedValue);
    });
    return result;
  };

  /**
   * Выделить все видимые элементы
   */
  var _selectAll = function() {
    bodyDiv.find('input:checkbox:visible').each(function(idx, el) {
      $(el).attr('checked', 'checked');
    });
  }
  this.selectAll = _selectAll;

  /**
   * Снять выделение со всех видимых элементов
   */
  var _unselectAll = function() {
    bodyDiv.find('input:checkbox:visible').each(function(idx, el) {
      $(el).removeAttr('checked');
    });
  }
  this.unselectAll = _unselectAll;

  /**
   * Добавить обработчик на событие change
   * @param handler обработчик
   */
  var _change = function(handler) {
    bodyDiv.find("input").change(handler);
    changeHandler = handler;
  }
  this.change = _change;

  values = options['values'];
  selected = options['selected'];

  if (selectAllBtn) {
    selectAllBtn.click(function() {
      selectAllBtn.toggleClass("clicked");
      if (selectAllBtn.hasClass("clicked"))
        _selectAll();
      else
        _unselectAll();

      if (changeHandler)
        changeHandler();
    });
  }

  if (values)
    this.reset(values, selected);
}
function createTooltip(id, position) {
  var tooltip = new EyelineTooltipComponent(id, position);
  registerJsfComponent(id, tooltip);
}

function EyelineTooltipComponent(id, position) {
  var itemSpan = $('#' + id + '_span');

  itemSpan.tooltip({
    position: position,
    delay: 100,
    relative: true
  });
}

function createDialog(id) {
  var dialog = new EyelineDialogComponent(id);
  registerJsfComponent(id, dialog);
}

function EyelineDialogComponent(id) {
  var dialogDiv = $('#' + id + '_div');

  this.setVisible = function(visible) {
    dialogDiv.css("display", visible ? "" : "none");
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

  this.title = function(title) {
    var  t = $('#' + id + '_title');
    if(title) {
      t.text(title);
    }
    return t.text();
  };
}
