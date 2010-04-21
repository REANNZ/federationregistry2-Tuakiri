package fedreg.workflow

class CalendarTagLib {

    static namespace = "calendar"

    def datePicker = { attrs ->

        if (!attrs.name)
            throwTagError("Tag [datePicker] is missing required attribute [name]")

        String name = attrs.remove('name')

        String dateFormat = attrs.dateFormat ? attrs.remove('dateFormat') : "%m/%d/%Y"
        
        String showTime = attrs.showTime ? attrs.remove('showTime') : "false"

        String timeFormat = attrs.timeFormat ? attrs.remove('timeFormat') : "24"

        String years = attrs.years ? "[${attrs.remove('years')}]" : "[1999,2999]"

        String singleClick = attrs.singleClick ? attrs.remove('singleClick') : "true"
        
        def value = attrs.value  ? attrs.remove('value') : attrs.defaultValue;

        def calendar = null

        def day = ''
		def month = ''
		def year = ''
		def hour = ''
        def minute = ''

        def date = ''
        def dateParam = ''

        if(value) {

            if(value instanceof Calendar) {
			    calendar = value
		    } else {
			    calendar = new GregorianCalendar();
			    calendar.setTime(value)
		    }

		    day = calendar.get(GregorianCalendar.DAY_OF_MONTH)
			month = calendar.get(GregorianCalendar.MONTH)+1
			year = calendar.get(GregorianCalendar.YEAR)
			hour = calendar.get(GregorianCalendar.HOUR_OF_DAY)
			minute = calendar.get(GregorianCalendar.MINUTE)

		}


        out << """
                    <input type="hidden" name="${name}_year" id="${name}_year" value="$year"/>
                    <input type="hidden" name="${name}_month" id="${name}_month" value="$month"/>
                    <input type="hidden" name="${name}_day" id="${name}_day" value="$day"/>
                    <input type="hidden" name="${name}_hour" id="${name}_hour" value="$hour"/>
                    <input type="hidden" name="${name}_minute" id="${name}_minute" value="$minute"/>
                    <input type="hidden" name="${name}" id="${name}" value=""/>

                    <input type="text" id="${name}_value" name="${name}_value" readonly="true"/>

                    <img src="${resource(dir:pluginContextPath,file:"images/skin/calendar.png")}" id="$name-trigger" alt="Date"/>
                    <script type="text/javascript">

                        Calendar.setup({
                            name:"$name",
                            inputField:"${name}_value",
                            ifFormat:"$dateFormat",
                            button:"$name-trigger",                            
                            showsTime:$showTime,
                            timeFormat:"$timeFormat",
                            onUpdate:${name}_updated,
                            singleClick:$singleClick,
                            range:${value ? years+',' : years}
                            ${value ? "date:new Date($year,${month -1},$day,$hour,$minute)" : '' }
                        });

                        function ${name}_updated(calendar) {

                            document.getElementById('${name}').value='struct'
                            document.getElementById('${name}_year').value= calendar.date.getFullYear();
				            document.getElementById('${name}_month').value= calendar.date.getMonth()+1;
				            document.getElementById('${name}_day').value= calendar.date.getDate();

                            if(calendar.showsTime) {
                                document.getElementById('${name}_hour').value= calendar.date.getHours();
				                document.getElementById('${name}_minute').value= calendar.date.getMinutes();
                            }else {
                                document.getElementById('${name}_hour').value = 0;
				                document.getElementById('${name}_minute').value = 0;
                            }

                        }

                        function ${name}_clean() {
                                document.getElementById('${name}').value='';
                                document.getElementById('${name}_value').value=''
                                document.getElementById('${name}_year').value= '';
				                document.getElementById('${name}_month').value= '';
				                document.getElementById('${name}_day').value= '';
                                document.getElementById('${name}_hour').value = '';
				                document.getElementById('${name}_minute').value = '';                            
                        }

                    </script>
               """

    }

    def resources = { attrs ->

        String style = attrs.style ? "${attrs.remove('style')}" : null;
        String theme = attrs.theme ? "css/${attrs.remove('theme')}.css" : "css/tiger.css";
        String lang = attrs.lang ? "calendar-${attrs.remove('lang')}.js" : "calendar-en.js";

        if(style) {
            out << "<style type='text/css'>@import url(${style});</style>"
        } else {
            out << "<style type='text/css'>@import url(${resource(dir:pluginContextPath,file:theme)});</style>"
        }

        out << """
	            <script type="text/javascript" src="${resource(dir:pluginContextPath,file:"js/calendar.js")}"></script>\n
	            <script type="text/javascript" src="${resource(dir:pluginContextPath,file:"js/lang/$lang")}"></script>\n
	            <script type="text/javascript" src="${resource(dir:pluginContextPath,file:"js/calendar-setup.js")}"></script>\n

               """
    }

}




