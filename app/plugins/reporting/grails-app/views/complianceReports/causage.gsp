
<html>
    <head>
        <meta name="layout" content="reporting" />
    </head>
    <body>

      <table class="table borderless">
        <tbody>
          <g:each in="${causage.keySet()}" status="i" var="k">
          <tr>
            <td>
              <table>
                <tbody>
                  <tr>
                    <th><g:message code="label.authority"/></th>
                    <td>${k.encodeAsHTML()}</td>
                  </tr>
                  <tr>
                    <th><g:message code="label.usagecount"/></th>
                    <td>${causage.get(k).size()}</td>
                  </tr>
                  <tr>
                    <th><g:message code="label.usedby"/></th>
                    <td>
                      <g:each in="${causage.get(k)}" status="j" var="rd">
                        ${rd.entityID.encodeAsHTML()}<br/>
                      </g:each>
                    </td>
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
          </g:each>
        </tbody>
      </table>

    </body>
</html>
