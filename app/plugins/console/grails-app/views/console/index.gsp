<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
   <meta http-equiv="Content-type" content="text/html; charset=utf-8">
   <title>Grails Console</title>
   <style type="text/css" media="screen"> 
	  body {
		width: 99%;
	}
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
		text-align: left;
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
         padding: 3px;
         color: #999;
      }     
      .bottom-toolbar {
	      padding-top: 6px;
	  }
   </style>  
   
   	<r:use modules="jquery-ui, codemirror"/>
	<r:layoutResources/>
</head>
<body>   
   <div id="progress">
      <div id="loading-mask" style=""></div>
      <div id="loading">
        <div class="loading-indicator"><img src="${createLinkTo(dir: 'images', file: 'spinner.gif')}" width="16" height="16" style="margin-right:8px;" align="absmiddle"/>Loading...</div>
      </div>   
   </div>
   
   <div id="editor">
      <textarea id="code" style="width: 100%; height: 400px;" rows="40" cols="100">${(session['_grails_console_last_code_'] ?: "").encodeAsHTML()}</textarea>  
      <div class="bottom-toolbar">  
         <div>
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
   <r:script>

		// TODO: (BB) I have started to sanitize this plugin from what we got from the Grails repo but have run out of time, at least it now uses optimized versions of codemirror
		// We need to re-write this to just be JQuery when there is time and get rid of calls to scriptaculous. For now minor used feature for admins so not overly concerning.

		var textarea = $('code');
		window.editor = CodeMirror.fromTextArea('code',  {
			width: "98%",
			height: "400px",
			path: "",
			stylesheet: "${r.resource(dir:'/js/codemirror/css', file:'groovycolors.css', plugin:'federationregistry') }",
			basefiles: ["${r.resource(dir:'/js/codemirror/js', file:'codemirror.groovy.inframe.min.js', plugin:'federationregistry') }"],
			parserfile: [],
			content: textarea.value,
			autoMatchParens: true,
			disableSpellcheck: true,
			lineNumbers: true,
			tabMode: 'shift',
		});
			
      Event.observe(window, Prototype.Browser.IE ? 'load':"dom:loaded", function(){

             
         

         var submitButton = $('submit');
         var resultContainer = $('result');
         
         var executeCode = function() {
            $('progress').show();
            new Ajax.Request("${createLink(action: 'execute')}", {
               parameters: {
                  code: editor.getCode().strip()
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
            editor.grabKeys(function(evt){  
               var c = evt.keyCode;      
               if(c == Event.KEY_ESC) {
                  clearResultPanel();
               }
            }, function(c) { return c == Event.KEY_ESC; });
         },1000);

      });
   </r:script>

	<r:layoutResources/>
</body>
</html>
