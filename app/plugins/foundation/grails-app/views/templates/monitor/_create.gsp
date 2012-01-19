<fr:hasPermission target="descriptor:${descriptor.id}:monitor:create">

  <%@page import="aaf.fr.foundation.MonitorType" %>

  <div id="addmonitor">
    <a class="show-add-monitor btn info"><g:message code="label.addmonitor"/></a>
  </div>
  
  <div id="newmonitor" class="hidden">
    <h4><g:message code="fedreg.templates.monitor.add.heading"/></h4>
    <form id="newmonitordata" class="span8">
      <fieldset>
       <g:hiddenField name="interval" value="0" />

        <div class="clearfix">
          <label for="type"><g:message code="label.monitortype"/></label>
          <div class="input">
            <g:select name="type" from="${MonitorType.list()}" optionKey="id" optionValue="name" class="span2"/>
            <fr:tooltip code='fedreg.help.monitor.type' />
          </div>
        </div>

        <div class="clearfix">
          <label for="url"><g:message code="label.location"/></label>
          <div class="input">
            <input name="url" type="text" class="required span5" />
            <fr:tooltip code='fedreg.help.monitor.location' />
          </div>
        </div>

        <div class="clearfix">
          <label for="node"><g:message code="label.node"/></label>
          <div class="input">
            <input name="node" type="text" class="span3" />
            <fr:tooltip code="fedreg.help.monitor.node" />
          </div>
        </div>

        <div class="input">
          <a class="add-monitor btn success"><g:message code="label.add"/></a>
          <a class="cancel-add-monitor btn"><g:message code="label.cancel"/></a>
        </div>

      </fieldset>
    </form>
  </div>
  
</fr:hasPermission>

<div id="delete-monitor-modal" class="modal hide fade">
  <div class="modal-header">
    <a href="#" class="close">×</a>
    <h3><g:message code="fedreg.templates.monitor.delete.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="fedreg.templates.monitor.delete.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message code="label.cancel" /></a>
    <a class="btn danger delete-monitor"><g:message code="label.delete" /></a>
  </div>
</div>
