<head>
  <meta name="layout" content="${grailsApplication.config.nimble.layout.application}"/>

  <n:jquery/>

  <link rel="stylesheet" href="${createLinkTo(dir: pluginContextPath, file: '/css/login.css')}"/>
  <link rel="stylesheet" href="${createLinkTo(dir: pluginContextPath, file: '/css/icons.css')}"/>

  <title>Login</title>

  <script type="text/javascript">

    $(function() {
      $(".loginmethod").hide();
      $("#loginlocal").show();

      $("#loginfacebookcontinue").hide();

      var active = jQuery.url.param("active")
      if (active)
        changeLogin(active);
      else
        changeLogin('local');

      $(".flash").show();
    });

    function changeLogin(ident) {
      $(".flash").hide();
      $(".loginselector").removeClass("current");
      $(".loginmethod").hide();
      $("#" + ident).show("highlight");
    }

    function enableFacebookContinue() {
      $("#loginfacebookcontinue").show();
      $("#loginfacebookenable").hide();
    }

    function disableFacebookContinue() {
      $("#loginfacebookcontinue").hide();
    }
  </script>
</head>

<body>

<g:if test="${facebook || openid}">
  <div class="container">

    <div class="login">

      <div class="flash">
        <n:flashembed/>
      </div>

      <div class="logininteraction">
        <g:if test="${local}">
          <div id="local" class="loginmethod">
            <h2>Login with a local account</h2>

            <g:form action="signin" name="signin">
              <fieldset>
                <label for="username" class="append-1">Username</label>
                <input id="username" type="text" name="username" class="title"/>

                <label for="password" class="append-1">Password</label>
                <input id="password" type="password" name="password" class="title"/>
              </fieldset>

              <fieldset>
                <g:checkBox id="rememberme" name="rememberme"/>
                <label>Remember me on this computer</label>
              </fieldset>

              <fieldset class="loginbuttons">
                <button type="submit" class="button darkbutton icon icon_user_green">Login</button>
              </fieldset>

            </g:form>

            <div class="accountoptions">
              <g:link controller="account" action="forgottenpassword" class="textlink icon icon_flag_purple">I've forgotten my password</g:link>
              <g:if test="${registration}">
                <a href="#" id="accountcreationpolicybtn" rel="accountcreationpolicy" class="textlink icon icon_user_go">New user</a>
              </g:if>
            </div>
          </div>
        </g:if>

        <g:if test="${facebook}">
          <div id="facebook" class="loginmethod externalloginmethod">
            <h2>Login using your Facebook account</h2>
            <p>
              <n:socialimg name="facebook" size="64" alt="Login using Facebook"/>
              Simply login using your existing  Facebook account. You'll then be able to access this service.
            </p>
            <div id="loginfacebookcontinue">
              <a href="${createLink(controller: "auth", action: "facebook")}" class="button icon icon_user_green">Login</a>
            </div>

            <div id="loginfacebookenable">
              <fb:login-button autologoutlink="true" size="large" background="white" length="long" onlogin="enableFacebookContinue();" onlogout="disableFacebookContinue();"></fb:login-button>
            </div>
          </div>
        </g:if>

        <g:if test="${openid}">
          <div id="google" class="loginmethod externalloginmethod">
            <h2>Login with a Google account</h2>
            <p>
              <n:socialimg name="google" size="64" alt="Login using Google"/>
              Simply login using your existing Google account. You'll then be able to access this service.
            </p>
            <p>
              <a href="${createLink(controller: "auth", action: "googlereq")}" class="button icon icon_user_green">Login</a>
            </p>
          </div>

          <div id="yahoo" class="loginmethod externalloginmethod">
            <h2>Login using your Yahoo! account</h2>
            <p>
              <n:socialimg name="yahoo" size="64" alt="Login using Yahoo"/>
              Simply login using your existing Yahoo! account. You'll then be able to access this service.
            </p>
            <p>
              <a href="${createLink(controller: "auth", action: "yahooreq")}" class="button icon icon_user_green">Login</a>
            </p>
          </div>

          <div id="openid" class="loginmethod externalloginmethod">
            <h2>Login using your OpenID account</h2>
            <p>
              <n:socialimg name="openid" size="64" alt="Login using OpenID"/>
              Simply login using your existing OpenID account. You'll then be able to access this service.
            </p>
            <g:form controller="auth" action="openidreq">
              <strong>My OpenID is:</strong>
              <input id="openiduri" type="text" name="openiduri" class="easyinput" value="http://"/>
              <button type="submit" class="button icon icon_user_green">Login</button>
            </g:form>
          </div>

          <div id="blogger" class="loginmethod externalloginmethod">
            <h2>Login with a Blogger account</h2>
            <p>
              <n:socialimg name="blogger" size="64" alt="Login using Blogger"/>
              Simply login using your existing Blogger account. You'll then be able to access this service.
            </p>
            <g:form controller="auth" action="bloggerreq">
              <label for="openiduri" class="append-1">Blog URL</label>
              <input id="openiduri" type="text" name="openiduri" class="easyinput"/>
              <fieldset>
                <button type="submit" class="button darkbutton icon icon_user_green">Login</button>
              </fieldset>
            </g:form>
          </div>

          <div id="wordpress" class="loginmethod externalloginmethod">
            <h2>Login with a Wordpress account</h2>
            <p>
              <n:socialimg name="wordpress" size="64" alt="Login using Wordpress"/>
              Simply login using your existing Wordpress account. You'll then be able to access this service.
            </p>
            <g:form controller="auth" action="wordpressreq">
              <label for="openiduri" class="append-1">Blog URL</label>
              <input id="openiduri" type="text" name="openiduri" class="easyinput"/>
              <fieldset>
                <button type="submit" class="button darkbutton icon icon_user_green">Login</button>
              </fieldset>
            </g:form>
          </div>

          <div id="technorati" class="loginmethod externalloginmethod">
            <h2>Login with a Technorati account</h2>
            <p>
              <n:socialimg name="technorati" size="64" alt="Login using Technorati"/>
              Simply login using your existing Technorati account. You'll then be able to access this service.
            </p>
            <g:form controller="auth" action="technoratireq">
              <label for="openiduri" class="append-1">Technorati Username</label>
              <input id="openiduri" type="text" name="technoratiusername" class="easyinput"/>
              <fieldset>
                <button type="submit" class="button darkbutton icon icon_user_green">Login</button>
              </fieldset>
            </g:form>
          </div>

          <div id="flickr" class="loginmethod externalloginmethod">
            <h2>Login with a Flickr account</h2>
            <p>
              <n:socialimg name="flickr" size="64" alt="Login using Flickr"/>
              Simply login using your existing Flickr account. You'll then be able to access this service.
            </p>
            <p>
              <a href="${createLink(controller: "auth", action: "flickreq")}" class="button icon icon_user_green">Login</a>
            </p>
          </div>
        </g:if>
      </div>


      <div class="loginchoices">
        <g:if test="${local}">
          <h2><a href="#" onClick="changeLogin('local');" class="icon icon_user_go">Login with a local account</a></h2>
        </g:if>
        <h2>Login with an external account</h2>
        <table>
          <tbody>
          <tr>
            <td>
              <g:if test="${openid}">
                <a href="#" class="" onClick="changeLogin('openid');"><n:socialimg name="openid" size="64" alt="Login using OpenID"/>OpenID</a>
              </g:if>
            </td>
            <td>
              <g:if test="${facebook}">
                <a href="#" class="" onClick="changeLogin('facebook');"><n:socialimg name="facebook" size="64" alt="Login using Facebook"/>Facebook</a>
              </g:if>
            </td>
          </tr>
          <g:if test="${openid}">
            <tr>
              <td>
                <a class="" onClick="changeLogin('google');"><n:socialimg name="google" size="64" alt="Login using Google"/>Google</a>
              </td>
              <td>
                <a class="" onClick="changeLogin('yahoo');"><n:socialimg name="yahoo" size="64" alt="Login using Yahoo!"/>Yahoo!</a>
              </td>
            </tr>
            <tr>
              <td>
                <a href="#" class="" onClick="changeLogin('blogger');"><n:socialimg name="blogger" size="64" alt="Login using Blogger"/>Blogger</a>
              </td>
              <td>
                <a href="#" class="" onClick="changeLogin('wordpress');"><n:socialimg name="wordpress" size="64" alt="Login using Wordpress"/>Wordpress</a>
              </td>
            </tr>
            <tr>
              <td>
                <a href="#" class="" onClick="changeLogin('technorati');"><n:socialimg name="technorati" size="64" alt="Login using Technorati"/>Technorati</a>
              </td>
              <td>
                <a href="#" class="" onClick="changeLogin('flickr');"><n:socialimg name="flickr" size="64" alt="Login using Flickr"/>Flickr</a>
              </td>
            </tr>
          </g:if>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</g:if>
<g:else>
  <div class="container">
    <div class="login">

      <div class="flash">
        <n:flashembed/>
      </div>

      <div id="local" class="localonlymethod">
        <h2>Login</h2>

        <g:form action="signin" name="signin">
          <input type="hidden" name="targetUri" value="${targetUri}"/>

          <fieldset>
            <label for="username" class="append-1">Username</label>
            <input id="username" type="text" name="username" class="title"/>

            <label for="password" class="append-1">Password</label>
            <input id="password" type="password" name="password" class="title"/>
          </fieldset>

          <fieldset>
            <g:checkBox id="rememberme" name="rememberme"/>
            <label>Remember me on this computer</label>
          </fieldset>

          <fieldset class="loginbuttons">
            <button type="submit" class="button darkbutton icon icon_user_green">Login</button>
          </fieldset>

        </g:form>

        <div class="accountoptions">
          <g:link controller="account" action="forgottenpassword" class="textlink icon icon_flag_purple">I've forgotten my password</g:link>
          <g:if test="${registration}">
            <g:link controller=" account" action="createuser" class="textlink icon icon_user_go">New User</g:link>
          </g:if>
        </div>
      </div>

    </div>
  </div>

</g:else>

<n:accountcreationpolicy/>
<n:facebookConnect/>

</body>
