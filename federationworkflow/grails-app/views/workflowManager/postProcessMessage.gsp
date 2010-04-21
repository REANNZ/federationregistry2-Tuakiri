<script type="text/javascript">

    function postMessage() {
    
        // code for IE7+, Firefox, Chrome, Opera, Safari
        if (window.XMLHttpRequest) { 
            xmlhttp=new XMLHttpRequest();
            
        // code for IE6, IE5
        } else { 
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
               
        var params = "id=${params.id}&"+document.getElementById("message").name+"="+document.getElementById("message").value
        xmlhttp.open("POST","${createLink(action: 'postProcessMessage')}",true);        
        
        //Send the proper header information along with the request
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xmlhttp.setRequestHeader("Content-length", params.length);
        xmlhttp.setRequestHeader("Connection", "close");
        
        xmlhttp.onreadystatechange=function() {
            if(xmlhttp.readyState==4) {
                document.getElementById("messages").innerHTML=xmlhttp.responseText
            }
        }        

        xmlhttp.send(params);
                
    }
    
</script>

<div id="messages">
    <h3>Messages:</h3><br>
    <p>&nbsp;
    <g:each var="message" in="${process.messages}">
    <workflow:userfullname username="${message.postedBy}"/> at <g:formatDate format="h:mm:ssa, EEE dd/MMM/yyyy" date="${message.date}"/>
    <table>
        <tr class="odd">
            <td>${message.message}</td>
        </tr>            
    </table>
    <p>&nbsp;</p>
    </g:each>

    <b>Post Message:</b><br>
    <g:textArea name="message" /><br>
    <g:submitButton name="Comment" value="Comment" onclick="postMessage();"/>
</div>
