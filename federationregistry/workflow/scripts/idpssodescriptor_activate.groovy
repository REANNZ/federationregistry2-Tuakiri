import fedreg.core.*

/*
mailService = ctx.getBean("mailService")
messageSource = ctx.getBean("messageSource")

Object[] args = ["hi there"]

​mailService.sendMail {            to 'donkey@kong.com'  
   from ctx.grailsApplication.config.nimble.messaging.mail.from
            subject messageSource.getMessage('fedreg.templates.mail.workflow.requestapproval.subject', args, 'fedreg.templates.mail.workflow.requestapproval.subject', new Locale("EN"))
            body(view: '/templates/mail/_workflow_requestapproval', plugin: "federationworkflow")
        }
*/

workflowTaskService = ctx.getBean("workflowTaskService")

def idp = IDPSSODescriptor.get(env.identityProvider.toLong())
idp.approved = true
idp.active = true
idp.save()

workflowTaskService.complete(env.taskInstanceID.toLong(), 'idpssodescriptoractivated')