<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <meta http-equiv="Content-type" content="text/html; charset=utf-8">
  <title>Grails Console</title>
  <script src="http://yui.yahooapis.com/3.0.0pr2/build/yui/yui-min.js" type="text/javascript"></script>
  <script>
    var cfg = {
      modules:{
        'yui2-layout-css':{type:'css',fullpath:"http://yui.yahooapis.com/combo?2.7.0/build/resize/assets/skins/sam/resize.css&2.7.0/build/layout/assets/skins/sam/layout.css"},
        'yui2-layout':{requires:['yui2-layout-css'],type:'js',fullpath:"http://yui.yahooapis.com/combo?2.7.0/build/yahoo-dom-event/yahoo-dom-event.js&2.7.0/build/animation/animation-min.js&2.7.0/build/dragdrop/dragdrop-min.js&2.7.0/build/element/element-min.js&2.7.0/build/resize/resize-min.js&2.7.0/build/layout/layout-min.js"},
        'consolelayout':{fullpath:'${createLinkTo(dir: 'js/grails-console', file: 'grails-console-layout.js')}',type:'js',requires:['yui2-layout','widget','json']}
      }}

    YUI(cfg).use('consolelayout', 'node', 'event', function(Y) {
      Y.on('domready', function() {
        Y.log('domready', 'debug', 'head')
        new Y.ConsoleLayout({units:[
          {position:'top',body:'header',height:'20px'},
          {position:'bottom',body:'result', collapse:true,resize:true,height:'300px'},
          {position:'right',body:'help', collapse:true,resize:true,width:'300px'},
          {position:'center', body:'main',height:'100%', width:'100%'}
        ]}).render()


        /* 
         var textarea = $('code');
         var editor = new MirrorFrame(CodeMirror.replace(textarea), {
         //height: "300px",
         content: textarea.value,
         parserfile: ["tokenizegroovy.js", "parsegroovy.js"],
         stylesheet: "${createLinkTo(dir:'js/codemirror', file: 'css/jscolors.css')}",
         path: "${createLinkTo(dir:'js/codemirror/js')}/",
         autoMatchParens: true,
         dumbTabs: true
         });

         var submitButton = $('submit');
         var resultContainer = $('result');

         submitButton.observe("click", function() {
         $('progress').show();
         new Ajax.Request("${createLink(action: 'execute')}", {
         parameters: {
         code: editor.mirror.getCode().strip()
         },
         onSuccess: function(response, json) {
         resultContainer.appendChild(
         new Element('pre').update(response.responseText)
         );
         $('progress').hide();
         }
         });

         });
         $('clear').observe("click", function() {
         resultContainer.update("");
         });*/

      })
    })
  </script>
  %{--<link rel="stylesheet" media="screen" href="${createLinkTo(dir: 'css', file: 'grails-console.css')}"/>--}%
  <script src="${createLinkTo(dir: 'js/codemirror/js', file: 'codemirror.js')}" type="text/javascript" charset="utf-8"></script>
  <script src="${createLinkTo(dir: 'js/codemirror/js', file: 'mirrorframe.js')}" type="text/javascript" charset="utf-8"></script>

</head>
<body class="yui-skin-sam">
<style>
#header {
  background-color: lightpink
}

#main {
  background-color: lightyellow
}

#result {
  background-color: lightgreen
}

#help {
  background-color: lightblue
}

</style>
<div id="header">header</div>
<div id="main"><textarea style="width: 100%; height: 100%" class="codepress java" rows="25" cols="100">// Groovy Code here</textarea></div>
<div id="help">help</div>
<div id="result">result</div>
<!--
<h1 id="header">Grails Console</h1>
<div id="console" class="console-editor">
  <textarea style="width: 100%; height: 100%" class="codepress java" rows="25" cols="100">// Groovy Code here</textarea>

  <div id="control">
    <button id="submit">Execute</button>
    <button id="clear">Clear Results</button>
    <span id="progress" style="display: none;">
      <img src="${createLinkTo(dir: 'images', file: 'spinner.gif')}" style="border: 0;" align="absmiddle" alt=""/>
      Executing Script...
    </span>
  </div>
</div>
<div id="result">
  <div style="color:#ddd">Script Output...</div>
</div>
-->
</body>
</html>