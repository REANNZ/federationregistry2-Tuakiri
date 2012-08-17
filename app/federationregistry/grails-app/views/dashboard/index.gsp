
<html>
  <head>
    <meta name="layout" content="dashboard" />
  </head>
  <body>

    <g:if test="${tasks}">
      <div class="row row-spacer">
        <div class="span8 offset2">
          <div class="alert alert-block alert-info">
            <h3 class="alert-heading">Workflows requiring your action</h3>
            <ol>
              <g:each in="${tasks}" status="i" var="instance">
                <li>
                  <strong>Submitted</strong>: ${fieldValue(bean: instance, field: "dateCreated")}<br>
                  <strong>Description</strong>: <g:link controller="workflowApproval" action="list">${fieldValue(bean: instance, field: "processInstance.description")}</g:link>
                </li>
              </g:each>
            </ol>
          </div>
        </div>
      </div>
    </g:if>

    <g:if test="${submittedTasks}">
      <div class="row row-spacer">
        <div class="span8 offset2">
          <div class="alert alert-block alert-infol">
            <h3 class="alert-heading">Workflows you have submitted</h3>
            <ol>
              <g:each in="${submittedTasks}" status="i" var="instance">
                <li>
                  <strong>Submitted</strong>: ${fieldValue(bean: instance, field: "dateCreated")}<br>
                  <strong>Waiting on</strong>: 
                  <ul class="clean">
                    <g:each in="${instance?.potentialApprovers}" var="approver">
                      <li>${fieldValue(bean: approver, field: "cn")} - <a href="mailto:${approver.email}">${fieldValue(bean: approver, field: "email")}</a></li>
                    </g:each>
                  </ul>
                </li>
              </g:each>
            </ol>
            <p>If your workflow has not been actioned by the above people <strong>please contact them directly</strong> for an update on progress.</p>
          </div>
          <span style="color:#fff">.</span>
        </div>
      </div>
    </g:if>

    <div class="row span12 row-spacer">
      <div class="span3 well">
        <h3>My Organisations</h3>
        <g:if test="${organizations}">
          <div class="dashadmincol">
            <ul class="dashlist clean">
              <g:each in="${organizations}" var="org">
                <li><a href="${createLink(controller:'organization', action:'show', id:org?.id)}">${fieldValue(bean: org, field: 'displayName')} </a></li>
              </g:each>
            </ul>
          </div>
        </g:if>
        <g:else>
          <p>You're not managing any organisations.</p>
        </g:else>
        <hr>
        <a href="${createLink(controller:'organization', action:'list')}" class="btn btn-small btn-info">View All</a>
      </div>

      <div class="span3 well">
        <h3>My Identity Providers</h3>
        <g:if test="${identityProviders}">
          <div class="dashadmincol">
            <ul class="dashlist clean">
              <g:each in="${identityProviders}" var="idp">
                <li><a href="${createLink(controller:'identityProvider', action:'show', id:idp.id)}">${fieldValue(bean: idp, field: 'displayName')}</a></li>
              </g:each>
            </ul>
          </div>
        </g:if>
        <g:else>
          <p>You're not managing any Identity Providers</p> 
        </g:else>
        <hr>
        <a href="${createLink(controller:'identityProvider', action:'list')}" class="btn btn-small btn-info">View All</a>
      </div>

      <div class="span3 well">
        <h3>My Service Providers</h3>
        <g:if test="${serviceProviders}">
          <div class="dashadmincol">
            <ul class="dashlist clean">
              <g:each in="${serviceProviders}" var="sp">
                <li><a href="${createLink(controller:'serviceProvider', action:'show', id:sp.id)}">${fieldValue(bean: sp, field: 'displayName')}</a></li>
              </g:each>
            </ul>
          </div>
        </g:if>
        <g:else>
          <p>You're not managing any Service Providers</p> 
        </g:else>
        <hr>
        <a href="${createLink(controller:'serviceProvider', action:'list')}" class="btn btn-small btn-info">View All</a>
      </div>


      <hr class="span9 offset1"/>
    

      <div class="span3 centered well">
        <strong class="dashboard-wow">${orgCount}</strong><hr><h4>Organisations</h4>
      </div>
      <div class="span3 centered well">
        <strong class="dashboard-wow">${idpCount}</strong><hr><h4>Identity Providers</h4>
      </div>
      <div class="span3 centered well">
        <strong class="dashboard-wow">${spCount}</strong><hr><h4>Service Providers</h4>
      </div>

      <div id="sessions" class="row-spacer span10 centered">
        <div class="spinner"><r:img dir="images" file="spinner.gif"/></div>
      </div> 
      <div id="registrations" class="row-spacer span10 centered">
        <div class="spinner"><r:img dir="images" file="spinner.gif"/></div>
      </div> 
    </div>

    <span style="color:#fff">.</span>

    <r:script>
      var summaryregistrationsEndpoint = "${createLink(controller:'federationReports', action:'reportsummaryregistrations')}"
      var summarysessionsEndpoint = "${createLink(controller:'federationReports', action:'reportsummarysessions')}"
      $(document).ready(function() {
        fr.summary_registrations_report('registrations');
        fr.summary_sessions_report('sessions');
      });

      <!-- Wonderful trick from http://stephenakins.blogspot.com.au/2011/01/uniform-div-heights-for-liquid-css-p.html -->
      var currentTallest = 0;
      var currentRowStart = 0;
      var rowDivs = new Array();

      function setConformingHeight(el, newHeight) {
       // set the height to something new, but remember the original height in case things change
       el.data("originalHeight", (el.data("originalHeight") == undefined) ? (el.height()) : (el.data("originalHeight")));
       el.height(newHeight);
      }

      function getOriginalHeight(el) {
       // if the height has changed, send the originalHeight
       return (el.data("originalHeight") == undefined) ? (el.height()) : (el.data("originalHeight"));
      }

      function columnConform() {

       // find the tallest DIV in the row, and set the heights of all of the DIVs to match it.
       $('div.dashadmincol').each(function(index) {

        if(currentRowStart != $(this).position().top) {

         // we just came to a new row.  Set all the heights on the completed row
         for(currentDiv = 0 ; currentDiv < rowDivs.length ; currentDiv++) setConformingHeight(rowDivs[currentDiv], currentTallest);

         // set the variables for the new row
         rowDivs.length = 0; // empty the array
         currentRowStart = $(this).position().top;
         currentTallest = getOriginalHeight($(this));
         rowDivs.push($(this));

        } else {

         // another div on the current row.  Add it to the list and check if it's taller
         rowDivs.push($(this));
         currentTallest = (currentTallest < getOriginalHeight($(this))) ? (getOriginalHeight($(this))) : (currentTallest);

        }
        // do the last row
        for(currentDiv = 0 ; currentDiv < rowDivs.length ; currentDiv++) setConformingHeight(rowDivs[currentDiv], currentTallest);

       });
      }

      $(window).resize(function() {
       columnConform();
      });

      $(document).ready(function() {
       columnConform();
      });
    </r:script>
  </body>

</html>
