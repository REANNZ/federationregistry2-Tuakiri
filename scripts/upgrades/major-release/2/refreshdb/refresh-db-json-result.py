#!/bin/env python

import sys
import json
import string
import re

fh = open(sys.argv[1])
result = json.load(fh)

# dump the json
#for k,v in result.items():
#    print '%s:%s\n' % (k,v)

print 'Parsing %s' % (sys.argv[1])

if ('result' in result.keys()):
    print 'Successful execution with result: %s' % (result['result'])

    output = result['output']

    if (output):

	output = string.replace(output,'&gt;','>')
	output = string.replace(output,'<br/>','\n')
	output = re.sub('groovy> .*\n', '', output)

	print 'Output:\n%s\n\n' % (output )

elif ('exception' in result.keys()):
    exception = result['exception']

    exception = string.replace(exception,'&gt;','>')
    exception = string.replace(exception,'&nbsp;',' ')
    exception = string.replace(exception,'&#39;',"'")
    exception = string.replace(exception,'<br/>','\n')
    print 'Execution resulted into exception:\n%s' % (exception)
    sys.exit(2)

else:
    print "The result object has neither result nor exception"
    print "Keys found: %s" % (", ".join(result.keys()) )
    sys.exit(3)
   
