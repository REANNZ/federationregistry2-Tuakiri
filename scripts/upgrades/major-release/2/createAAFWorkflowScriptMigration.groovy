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
Having run createFRBaseEnvironment this process will now remove all existing workflow scripts and replace them with FR 2 compatible versions.
*/\n\n""")

output.append("""def scripts = WorkflowScript.list()

scripts.each {
   it.delete(flush:true)
}

println 'Removed all exisiting scripts'
""")

output.append("def subject = aaf.fr.identity.Subject.findWhere(principal:'internaladministrator')\n\n")

output.append("// Workflow scripts\n")
def scripts = new File("../../../setup/src/workflow/scripts")
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
    return false
}
else {
    println \"Loaded valid workflow script wfs$sc\"
}
\n""")
}

output.append("\n\nprintln 'replaced exisiting workflow scripts'")

def target = new File('target')
target.mkdir()

def out = new File('target/migrateAAFWorkflowScripts.groovy')
out.write(output.toString())