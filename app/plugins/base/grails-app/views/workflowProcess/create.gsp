
<html>
  <head>
    <r:require modules="codemirror"/>
    <meta name="layout" content="workflow" />
  </head>
  <body>
    <h2><g:message code="views.fr.workflow.process.create.heading" /></h2>

    <g:render template="/templates/flash" />
  
    <g:if test="${process.hasErrors()}">
      <div class="error">
      <ul>
      <g:eachError bean="${process}">
          <li>${it}</li>
      </g:eachError>
      </ul>
      </div>
    </g:if>
  
    <g:form action="save">
      <g:textArea name="code" value="${(process.definition ?: '// insert workflow here')}" rows="5" cols="40"/>
      <br>
      <button type="submit" class="btn"/><g:message code="label.add" /></button>
    </g:form>

    <r:script>
      var textarea = $("#code");
      var editor = CodeMirror.fromTextArea('code',  {
        height: "600px",
        path: "",
        stylesheet: "${r.resource(dir:'/js/codemirror/css', file:'groovycolors.css', plugin:'federationregistry') }",
        basefiles: ["${r.resource(dir:'/js/codemirror/js', file:'codemirror.groovy.inframe.min.js', plugin:'federationregistry') }"],
        parserfile: [],
        content: textarea.value,
        autoMatchParens: true,
        disableSpellcheck: true,
        lineNumbers: true,
        tabMode: 'shift'
      });
    </r:script>
  </body>
</html>