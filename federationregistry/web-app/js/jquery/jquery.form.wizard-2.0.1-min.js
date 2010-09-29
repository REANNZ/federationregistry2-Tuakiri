(function(a){a.fn.formwizard=function(q,f,e){var c=a(this);var k=c.find(".step");switch(arguments[0]){case"state":return o();case"reset":p();return a(this);case"show":(c[0].settings.historyEnabled)?a.historyLoad("_"+arguments[1].substr(arguments[1].indexOf("#")+1)):m("_"+arguments[1].substr(arguments[1].indexOf("#")+1));return a(this);case"destroy":l();return a(this);default:if(c[0].settings===undefined){g()}}function g(){var s=(e)?e.success:undefined;c[0].formSettings=a.extend(e,{success:function(t){if(e&&e.resetForm||!e){p()}if(s){s(t)}else{alert("success")}}});c[0].settings=a.extend({historyEnabled:false,validationEnabled:false,formPluginEnabled:false,linkClass:".link",submitStepClass:"submit_step",back:":reset",next:":submit",textSubmit:"Submit",textNext:"Next",textBack:"Back",afterNext:undefined,afterBack:undefined,serverSideValidationUrls:undefined,inAnimation:"fadeIn",outAnimation:"fadeOut",focusFirstInput:false,disableInputFields:true,showBackOnFirstStep:false},q);c[0].activatedSteps=new Array();c[0].isLastStep=false;c[0].previousStep=undefined;c[0].currentStep=k.eq(0).attr("id");c[0].backButton=c.find(c[0].settings.back);c[0].nextButton=c.find(c[0].settings.next);c[0].originalResetValue=c[0].backButton.val();c[0].originalSubmitValue=c[0].nextButton.val();if(c[0].settings.validationEnabled&&jQuery().validate==undefined){c[0].settings.validationEnabled=false;alert("the validation plugin needs to be included")}else{if(c[0].settings.validationEnabled){c.validate(f)}}if(c[0].settings.formPluginEnabled&&jQuery().ajaxSubmit==undefined){c[0].settings.formPluginEnabled=false;alert("the form plugin needs to be included")}k.hide();if(c[0].settings.disableInputFields==true){a(k).find(":input").attr("disabled","disabled")}if(c[0].settings.historyEnabled&&a.historyInit==undefined){c[0].settings.historyEnabled=false;alert("the history plugin needs to be included")}else{if(c[0].settings.historyEnabled){a.historyInit(m);a.historyLoad("_"+c[0].currentStep)}else{m(undefined)}}c[0].initialized=true;c[0].backButton.val(c[0].settings.textBack);return a(this)}c[0].nextButton.click(function(){if(c[0].settings.validationEnabled){if(!c.valid()){c.validate().focusInvalid();return false}}if(c[0].isLastStep){for(var t=0;t<c[0].activatedSteps.length;t++){k.filter("#"+c[0].activatedSteps[t]).find(":input").removeAttr("disabled")}if(c[0].settings.formPluginEnabled){c[0].initialized=false;c.ajaxSubmit(c[0].formSettings);return false}c.submit();return false}if(c[0].settings.serverSideValidationUrls){var s=c[0].settings.serverSideValidationUrls[c[0].currentStep];if(s!=undefined){var u=s.success;a.extend(s,{success:function(v,w){if((u!=undefined&&u(v,w))||(u==undefined)){h()}}});c.ajaxSubmit(s);return false}}h();return false});c[0].backButton.click(function(){if(c[0].settings.historyEnabled&&c[0].activatedSteps.length>0){history.back()}else{if(c[0].activatedSteps.length>0){m("_"+c[0].activatedSteps[c[0].activatedSteps.length-2])}}return false});function h(){c[0].initialized=true;var s=d(c[0].currentStep);if(s==c[0].currentStep){b(s,s);return}if(c[0].settings.historyEnabled){a.historyLoad("_"+s)}else{m("_"+s)}}function r(){c[0].nextButton.attr("disabled","disabled");c[0].backButton.attr("disabled","disabled")}function j(){if(c[0].isLastStep){c[0].nextButton.val(c[0].settings.textSubmit)}else{c[0].nextButton.val(c[0].settings.textNext)}if(c[0].currentStep!==k.eq(0).attr("id")){c[0].backButton.removeAttr("disabled").show()}else{if(!c[0].settings.showBackOnFirstStep){c[0].backButton.hide()}}c[0].nextButton.removeAttr("disabled")}function b(v,t){r();var s=k.filter("#"+v);var u=k.filter("#"+t);s.find(":input").attr("disabled","disabled");u.find(":input").removeAttr("disabled");if(c[0].settings.historyEnabled){s[c[0].settings.outAnimation](0);u[c[0].settings.inAnimation](400,function(){if(c[0].settings.focusFirstInput){u.find(":input:first").focus()}j()});return}s[c[0].settings.outAnimation](400,function(){u[c[0].settings.inAnimation](400,function(){if(c[0].settings.focusFirstInput){u.find(":input:first").focus()}j()})})}function n(s){c[0].isLastStep=false;if(a("#"+s).hasClass(c[0].settings.submitStepClass)||k.filter(":last").attr("id")==s){c[0].isLastStep=true}}function i(u){var t=undefined;var s=k.filter("#"+u).find(c[0].settings.linkClass);if(s!=undefined&&s.length==1){t=a(s).val()}else{if(s!=undefined&&s.length>1){t=s.filter(c[0].settings.linkClass+":checked").val()}else{t=undefined}}return t}function d(t){var s=i(t);if(s!=undefined){if((s!=""&&s!=null&&s!=undefined)&&k.filter("#"+s).attr("id")!=undefined){return s}return c[0].currentStep}else{if(s==undefined&&!c[0].isLastStep){var u=k.index(a("#"+c[0].currentStep));return k.filter(":eq("+(1*u+1)+")").attr("id")}}}function m(t){var s=false;if(t==undefined||t==""){c[0].activatedSteps.pop();t=a(k).eq(0).attr("id");c[0].activatedSteps.push(t)}else{t=t.substr(1);if(c[0].activatedSteps[c[0].activatedSteps.length-2]==t){s=true;c[0].activatedSteps.pop()}else{c[0].activatedSteps.push(t)}}var u=c[0].currentStep;n(t);c[0].previousStep=u;c[0].currentStep=t;b(u,t);if(s){if(c[0].settings.afterBack){c[0].settings.afterBack({currentStep:c[0].currentStep,previousStep:c[0].previousStep,isLastStep:c[0].isLastStep,activatedSteps:c[0].activatedSteps})}}else{if(c[0].initialized){if(c[0].settings.afterNext){c[0].settings.afterNext({currentStep:c[0].currentStep,previousStep:c[0].previousStep,isLastStep:c[0].isLastStep,activatedSteps:c[0].activatedSteps})}}}}function p(){c[0].reset();a("label,:input,textarea",this).removeClass("error");for(var s=0;s<c[0].activatedSteps.length;s++){k.filter("#"+c[0].activatedSteps[s]).hide().find(":input").attr("disabled","disabled")}c[0].activatedSteps=new Array();c[0].previousStep=undefined;c[0].isLastStep=false;var t=k.eq(0).attr("id");if(c[0].settings.historyEnabled){a.historyLoad("_"+t)}else{m("_"+t)}}function o(){return{settings:c[0].settings,activatedSteps:c[0].activatedSteps,isLastStep:c[0].isLastStep,previousStep:c[0].previousStep,currentStep:c[0].currentStep,backButton:c[0].backButton,nextButton:c[0].nextButton}}function l(){c[0].reset();a(k).show();if(c[0].settings.disableInputFields==true){a(k).find(":input").removeAttr("disabled")}c[0].backButton.removeAttr("disabled").val(c[0].originalResetValue).show();c[0].nextButton.val(c[0].originalSubmitValue);c[0].nextButton.unbind("click");c[0].backButton.unbind("click");c[0].activatedSteps=undefined;c[0].previousStep=undefined;c[0].currentStep=undefined;c[0].isLastStep=undefined;c[0].settings=undefined;c[0].nextButton=undefined;c[0].backButton=undefined;c[0].formwizard=undefined;c[0]=undefined;k=undefined;c=undefined}}})(jQuery);