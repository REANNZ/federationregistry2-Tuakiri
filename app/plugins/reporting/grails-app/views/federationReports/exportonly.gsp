<html>
  <head>
    <meta name="layout" content="reporting" />
  </head>
  <body>

    <div class="centered">
        <p class="alert alert-info">The reports presented on this page are only provided in Excel(cvs) format for further statistical manipulation and do not have an associated graphical representation.</p>
    </div>

    <div class="row">
      <div class="span8">
        <h4>SP utilisation showing source IdP breakdown (box reporting)</h4>
        <g:form type="post" action="reportserviceutilizationbreakdown" class="form-horizontal report-parameters validating">
          <div class="control-group">
            <label for="startDate"><g:message encodeAs="HTML" code="label.startdate"/> </label>
            <div class="controls">
              <input name="startDate" placeholder="Start Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>
            </div>
          </div>

          <div class="control-group">
            <label for="startDate"><g:message encodeAs="HTML" code="label.enddate"/> </label>
            <div class="controls">
              <input name="endDate" placeholder="End Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>
            </div>
          </div>

          <div class="control-group">
            <label for="idpcount"><g:message encodeAs="HTML" code="label.idpcount"/> </label>
            <div class="controls">
              <input name="idpcount" value="5" class="required span1 number" type="text"/>
            </div>
          </div>

          <div class="control-group">
            <label for="idpcount"><g:message encodeAs="HTML" code="label.excludespfromorg"/> </label>
            <div class="controls">
              <g:select name="excludeorg" from="${organizations.sort{it.displayName}}" optionKey="id" optionValue="${{ it.displayName?.encodeAsHTML() }}"/>
            </div>
          </div>

          <div class="control-group">
            <label for="excludetestuat"><g:message encodeAs="HTML" code="label.excludetestuat"/> </label>
            <div class="controls">
              <input type="checkbox" name="excludetestuat" value="true" checked>
            </div>
          </div>

          <input type="submit" class="btn" label="${g.message(encodeAs:"HTML", code:'label.generate')}"></input>

        </g:form>
      </div>
    </div>
    <r:script>
      $(document).ready(function() {
        fr.setup_date_range();
      });
    </r:script>
  </body>
</html>
