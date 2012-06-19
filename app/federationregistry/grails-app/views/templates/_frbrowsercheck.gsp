<r:script type="text/javascript">
  window.onload=function(){$.reject({
      reject: {
        all: false, 
        msie5: true, msie6: true, msie7: true, msie8:true,
        firefox1: true, firefox2:true, firefox3:true, firefox4:true, firefox5:true, firefox6:true,
        safari1:true, safari2:true, safari3:true,
        opera7:true, opera8:true, opera9:true,
        konqueror: true,
        chrome1: true, chrome2:true, chrome3:true, chrome4:true, chrome5:true, chrome6:true,
        chrome7: true, chrome8:true, chrome9:true, chrome10:true, chrome11:true, chrome12:true, 
        chrome13:true, chrome14:true, chrome15:true,
        unknown: true
      },
      display: ['chrome','firefox','safari', 'opera','msie'],
      imagePath: '${g.resource(dir:"images")}/',
      browserShow: false,
      browserInfo: { // Settings for which browsers to display  
          firefox: {  
              text: 'Firefox',
              url: 'http://www.mozilla.com/firefox/'
          },  
          safari: {  
              text: 'Safari',  
              url: 'http://www.apple.com/safari/download/'  
          },  
          opera: {  
              text: 'Opera',  
              url: 'http://www.opera.com/download/'  
          },  
          chrome: {  
              text: 'Chrome',  
              url: 'http://www.google.com/chrome/'  
          },  
          msie: {  
              text: 'Internet Explorer 9+',  
              url: 'http://www.microsoft.com/windows/Internet-explorer/'  
          },  
          gcf: {  
              text: 'Google Chrome Frame',  
              url: 'http://code.google.com/chrome/chromeframe/',   
              allow: { all: false, msie: true }  
          }  
      },

      header: '${g.message(code:"templates.fr.browsercheck.header")}',
      paragraph1: '${g.message(code:"templates.fr.browsercheck.paragraph1")}',
      paragraph2: '${g.message(code:"templates.fr.browsercheck.paragraph2")}',
                    
      close: true,
      closeMessage: '${g.message(code:"templates.fr.browsercheck.closemessage")}',
      closeLink: '${g.message(code:"templates.fr.browsercheck.closelink")}', 
      closeURL: '#', // Close URL  
      closeESC: true, // Allow closing of window with esc key  
    
      closeCookie: true,  
      // Cookie settings are only used if closeCookie is true  
      cookieSettings: {    
          path: '/',   
          expires: 0  
      }
    });
  };
</r:script>