<%@page import="aaf.fr.foundation.MonitorType" %>

<fr:hasPermission target="federation:management:descriptor:${descriptor.id}:monitor:create">
  <hr>

  <div id="addmonitor">
    <a class="show-add-monitor btn"><g:message encodeAs="HTML" code="label.addmonitor"/></a>
  </div>
  
  <div id="newmonitor" class="revealable">
    <h4><g:message encodeAs="HTML" code="templates.fr.monitor.add.heading"/></h4>
    <form id="newmonitordata" class="form-horizontal" onsubmit="return false;">
      <fieldset>
       <g:hiddenField name="interval" value="0" />

        <div class="control-group">
          <label class="control-label" for="type"><g:message encodeAs="HTML" code="label.monitortype"/></label>
          <div class="controls">
            <g:select name="type" from="${MonitorType.list()}" optionKey="id" optionValue="${{ it.name?.encodeAsHTML() }}" class="span2"/>
            <fr:tooltip code='help.fr.monitor.type' />
          </div>
        </div>

        <div class="control-group">
          <label class="control-label" for="url"><g:message encodeAs="HTML" code="label.location"/></label>
          <div class="controls">
            <input name="url" type="text" class="required span4" />
            <fr:tooltip code='help.fr.monitor.location' />
          </div>
        </div>

        <div class="control-group">
          <label class="control-label" for="node"><g:message encodeAs="HTML" code="label.node"/></label>
          <div class="controls">
            <input name="node" type="text" class="span2" />
            <fr:tooltip code="help.fr.monitor.node" />
          </div>
        </div>

        <div class="form-actions">
          <a class="add-monitor btn btn-success"><g:message encodeAs="HTML" code="label.add"/></a>
          <a class="cancel-add-monitor btn"><g:message encodeAs="HTML" code="label.cancel"/></a>
        </div>

      </fieldset>
    </form>
  </div>
  
</fr:hasPermission>

<div id="delete-monitor-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message encodeAs="HTML" code="templates.fr.monitor.delete.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message encodeAs="HTML" code="templates.fr.monitor.delete.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message encodeAs="HTML" code="label.cancel" /></a>
    <a class="btn btn-danger delete-monitor"><g:message encodeAs="HTML" code="label.delete" /></a>
  </div>
</div>
