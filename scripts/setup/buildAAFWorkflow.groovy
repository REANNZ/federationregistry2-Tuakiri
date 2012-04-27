
def output = new StringBuffer()

output.append("import org.apache.shiro.subject.Subject\n")
output.append("import org.apache.shiro.util.ThreadContext\n")
output.append("import org.apache.shiro.SecurityUtils\n")
output.append("import grails.plugins.federatedgrails.*\n")
output.append("import aaf.fr.app.*\n")
output.append("import aaf.fr.workflow.*\n")
output.append("import aaf.fr.foundation.*\n")
output.append("import aaf.fr.identity.*\n")

output.append("""\n/*
Bootstrap - Step 4

Having run buildAAFWorkflow to create this script the workflow in setup/src/workflow will now be imported to FR database for use.
*/\n\n""")

output.append("def subject = new aaf.fr.identity.Subject.findWhere(principal:'internaladministrator')")

output.append("// Workflow scripts\n")
def scripts = new File("./src/workflow/scripts")
def sc = 0
scripts.eachFile { script ->
    def name = script.name =~ /(.+?)(\.[^.]*$|$)/
    
    sc++
    output.append( "def script$sc = \"${script.getText().bytes.encodeBase64().toString()}\"\n")
    output.append( "def wfs$sc = new WorkflowScript(name: \"${name[0][1]}\", definition: new String(script${sc}.decodeBase64()), creator:subject)")
    output.append( """
if(!wfs${sc}.save()) {
    println \"Unable to correctly process workflow script wfs$sc during bootstrap\"
    wfs${sc}.errors.each {
        println it
    }
    throw new RuntimeException("Unable to import wfs$sc")
}
else {
    println \"Loaded valid workflow script wfs$sc\"
}
\n""")
}

output.append("// Workflow processes\n")
output.append("workflowProcessService = ctx.getBean('workflowProcessService')\n\n")

output.append("def suMetaClass = new ExpandoMetaClass(SecurityUtils)\n")
output.append("suMetaClass.'static'.getSubject = {[getPrincipal:{subject.id}] as Subject}\n")
output.append("suMetaClass.initialize()\n")
output.append("SecurityUtils.metaClass = suMetaClass\n\n")

// Create workflow processes
def processes = new File("./src/workflow/processes")
def proc = 0
processes.eachFile { process ->
    proc++
    output.append( "def process$proc = \"${process.getText().bytes.encodeBase64().toString()}\"\n")
    output.append( "workflowProcessService.create(new String(process${proc}.decodeBase64()))\n\n")
}

output.append("SecurityUtils.metaClass = null")

output.append("// Next step\n")
output.append("println 'Completed creation of AAF workflows'\n")
output.append("true")

def target = new File('target')
target.mkdir()

def out = new File('target/createAAFWorkflow.groovy')
out.write(output.toString())