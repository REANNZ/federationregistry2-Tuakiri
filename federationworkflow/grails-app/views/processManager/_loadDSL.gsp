<g:form name="load" action="${params.action}" id="${params.id}">
<ul>
    <g:if test="${flash.message || newProcessDefinition?.hasErrors()}">    
    <div class="errors">
    <ul>
        <g:if test="${flash.message}">
            <li>${flash.message}</li>
        </g:if>

        <g:hasErrors bean="${newProcessDefinition}">
        <g:eachError var="err">
            <g:if test="${!err.code.equals('validator.invalid')}">
            <li><g:message error="${err}" /></li>
            </g:if>
        </g:eachError>
        </g:hasErrors>
    </ul>    
    </div>
    <p>&nbsp;</p>
    </g:if>
    
    <script src="${resource(dir: 'js/codemirror', file: 'codemirror.js')}" type="text/javascript"></script>
    <style type="text/css">
      .CodeMirror-line-numbers {
        width: 1.0em;
        color: #999;
        background-color: #333;
        text-align: right;
        padding: .4em;
        margin: 0;
        font-size: 10pt;
        line-height: 1.1em;
        font-family: consolas, monospace;
      }
    </style>
    
 
    <textarea id="code" name="code" cols="100" rows="40">${code}</textarea>
    <script type="text/javascript">
        var editor = CodeMirror.fromTextArea('code', {
            parserfile: ["tokenizejavascript.js", "parsejavascript.js"],
            stylesheet: "${resource(dir: 'css/codemirror', file: 'jscolors.css')}",
            path: "${resource(dir: 'js/codemirror/')}",
            lineNumbers: true,
            textWrapping: false,
            indentUnit: 4,
            parserConfig: {},
            height: '500px'
        });
    </script>
    
    <p>&nbsp;</p>
    <g:submitButton name="save" value="Save"/>
</ul>

</g:form>

