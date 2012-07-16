
<html>
  <head>
    <r:require modules="codemirror"/>
    <meta name="layout" content="workflow" />
  </head>
  <body>
      <h2><g:message code="views.fr.workflow.script.create.heading" /></h2>
      
      <g:render template="/templates/flash" />

      <g:if test="${script.hasErrors()}">
        <div class="error">
        <ul>
        <g:eachError bean="${script}">
            <li>${it}</li>
        </g:eachError>
        </ul>
        </div>
      </g:if>
    
      <g:form action="save">
        <table class="table borderless">
          <tbody>
            <tr>
              <td><label for="name"><g:message code="label.name" /></label></td>
              <td><g:textField name="name" value="${script.name ?: ''}" /></td>
            </tr>
            <tr>
              <td><label for="description"><g:message code="label.description" /></label></td>
              <td><g:textField name="description" value="${script.description ?: ''}" /></td>
            </tr>
          </tbody>
        </table>
        <g:textArea name="definition" value="${(script.definition ?: '// Script definition').encodeAsHTML()}" rows="5" cols="40"/>
        <br>
        <button type="submit" class="btn"/><g:message code="label.create" /></button>
      </g:form>
    
      <r:script>
        var textarea = $("#definition");
        var editor = CodeMirror.fromTextArea('definition',  {
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