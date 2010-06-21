<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
   <meta http-equiv="Content-type" content="text/html; charset=utf-8">
   <title>Grails Console</title>
   <style type="text/css" media="screen"> 
      img {
         border-style: none;
      }           
      #editor {
         background: #333;
      }           
      #result {         
         padding: 3px;        
         background: #ddd;
         overflow: auto; 
         clear: both;
      }    
      #result .output {
         font-family: monospace;
         font-size: 11px;
      }
      #result .stacktrace {     
         margin: 4px;
         padding: 5px;
         -moz-border-radius: 5px;
         -webkit-border-radius: 5px;
         background: #cc2222;
         color: #fff;
         font-family: monospace;
         font-size: 11px;
      }     
      #loading-mask{
         position:absolute;
      	left:0;
      	top:0;
          width:100%;
          height:100%;
          z-index:20000;
          background-color:white;
      }
      #loading{
      	position:absolute;
      	left:45%;
      	top:40%;
      	padding:2px;
      	z-index:20001;
          height:auto;
      }
      .script-result {
         background: blue;
         margin: 4px;
         padding: 5px;
         font-size: 9px;
         color: #ddd;
         -moz-border-radius: 5px;  
         -webkit-border-radius: 5px;

      }   
      .script-result .exec-time {  
         display: block;
         color: #000;
         background: #ccc; 
         float: right;
         padding: 1px 3px;   
         -moz-border-radius: 2px;  
         -webkit-border-radius: 2px;
      }              
      .script-result .exec-res {
         font-family: monospace;
         font-size: 11px;
      }   
      #result .output {
         padding: 2px 5px;
      }          
      .CodeMirror-line-numbers {
         padding: 4px;
         font-size: 14px;
         font-family: monospace;
         color: #999;
         text-align: right;
         background: #444;
      }         
      .plugin-info {
         float: right;
         color: #fff;
         padding: 10px;
         font-size: 10px;                                 
         text-align: right;
      }                  
      .authors {
         font-size: 9px;
      }
   </style>  
   
   <script src="${request.contextPath}${pluginContextPath}/js/codemirror/js/mirrorframe.js" type="text/javascript" charset="utf-8"></script>
   <script src="${request.contextPath}${pluginContextPath}/js/codemirror/js/codemirror.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>   
   <div id="progress">
      <div id="loading-mask" style=""></div>
      <div id="loading">
        <div class="loading-indicator"><img src="${createLinkTo(dir: 'images', file: 'spinner.gif')}" width="16" height="16" style="margin-right:8px;" align="absmiddle"/>Loading...</div>
      </div>   
   </div>
   
   <div id="editor">
      <textarea id="code" style="width: 100%; height: 300px;" rows="25" cols="100">${(session['_grails_console_last_code_'] ?: "// Groovy Code here\n").encodeAsHTML()}</textarea>  
      <div class="bottom-toolbar">  
         <div class="buttons">
            <button id="submit" title="(Ctrl + Return)">Execute</button>
            <button id="clear"  title="(Esc + Esc)">Clear Results</button>           
         </div>
         <span id="progress" style="display: none;">
            <img src="${createLinkTo(dir:'images',file:'spinner.gif')}" style="border: 0;" align="absmiddle" alt="" />
            Executing Script...
         </span>  
      </div> 
      <div id="drag-handle">...</div>  
   </div>   
   <div id="result">
   </div>   
   <g:javascript library="scriptaculous" />
   <script type="text/javascript" charset="utf-8">

      Event.observe(window, Prototype.Browser.IE ? 'load':"dom:loaded", function(){

         var textarea = $('code');
         window.editor = new MirrorFrame(CodeMirror.replace(textarea), {
            height: "300px",
            content: textarea.value,
            parserfile: ["tokenizegroovy.js", "parsegroovy.js"],
            stylesheet: "${request.contextPath}${pluginContextPath}/js/codemirror/css/groovycolors.css",
            path: "${request.contextPath}${pluginContextPath}/js/codemirror/js/",
            autoMatchParens: true,
            disableSpellcheck: true,
            lineNumbers: true,
            tabMode: 'shift'
         });                
         

         var submitButton = $('submit');
         var resultContainer = $('result');
         
         var executeCode = function() {
            $('progress').show();
            new Ajax.Request("${createLink(action: 'execute')}", {
               parameters: {
                  code: editor.mirror.getCode().strip()
               },
               onSuccess: function(response, json) {
                  resultContainer.appendChild(
                     new Element('div').update(response.responseText)
                  );        
                  resultContainer.scrollTop = resultContainer.scrollHeight;
                  $('progress').hide();
               }
            });
            
         }                      
         
         submitButton.observe("click", executeCode);
         $('clear').observe("click", function() {
            resultContainer.update("");
         });   
                                         
//         var handle = $('drag-handle'), ho = Position.cumulativeOffset(handle)[1], iframe = $$('#editor iframe')[0], ih = 300;
//         new Draggable('drag-handle', {
//            snap: function(x,y) {
//               return [0, y];
//            },
//            onEnd: function() {          
//               ih -= (ho - (ho=Position.cumulativeOffset(handle)[1]));
//                iframe.setStyle({ height: (ih + 'px') });        
//                doLayout();
//            } 
//         });   

         var resultPanel = $('result');
         var splitter = $('drag-handle'), sh = splitter.getHeight();
         var doLayout = function() {
            var wh = window.innerHeight||document.documentElement.clientHeight;
            var splitOffset = Position.cumulativeOffset(splitter)[1] + sh + 6;    
            resultPanel.setStyle({ height: ((wh - splitOffset) + "px")});
         }                                                           
         try {doLayout();}catch(e){}
         
         Event.observe(window, 'resize', doLayout);
         $('loading').update("Executing...");
         $('loading-mask').setOpacity(0.3);  
         $('progress').hide(); 
                                   
         var _clearFlag = false;
                                   
         function clearResultPanel() {
            if(_clearFlag) {
               resultContainer.update("");
            } else {
               _clearFlag = true;
               setTimeout(function() { _clearFlag = false; }, 1000);
            }
         }           
                    
         setTimeout(function(){
            editor.mirror.grabKeys(function(evt){  
               var c = evt.keyCode;      
               if(c == Event.KEY_ESC) {
                  clearResultPanel();
               } else {
                  if(evt.ctrlKey || evt.metaKey) {
                     executeCode();
                  } else {
                     editor.mirror.editor.insertNewLine();
                  }  
               }
            }, function(c) { return c == Event.KEY_RETURN || c == Event.KEY_ESC; });
         },1000);

      });
   </script>
</body>
</html>
