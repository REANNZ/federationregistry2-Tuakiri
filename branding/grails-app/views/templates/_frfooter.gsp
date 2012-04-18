<fr:isLoggedIn>
  Logged in as <strong><fr:principalName /></strong> ( TargetedID: <fr:principal /> )<br><br>
</fr:isLoggedIn>
Federation Registry <strong>version <g:meta name="app.version"/></strong>
<br>
Developed for the <a href="http://www.aaf.edu.au">Australian Access Federation</a> by <a href="http://bradleybeddoes.com">Bradley Beddoes</a>
<br>
Powered by Grails <g:meta name="app.grails.version"/>

<r:script>
  if (typeof(Zenbox) !== "undefined") {
    Zenbox.init({
      dropboxID:   "6875",
      url:         "australianaccessfederation.zendesk.com",
      tabID:       "support",
      hide_tab:	   true
    });
  }

  <g:if test="${grailsApplication.config.aaf.fr.gapps.enabled}">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', "${grailsApplication.config.aaf.fr.gapps.id}"]);
    _gaq.push(['_trackPageview']);

    (function() {
      var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
      ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
      var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    })();
  </g:if>
</r:script>
