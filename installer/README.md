# FR2 Installer
This sub-project of FR2 will help deployers get up and running with both a WAR based deployment for evaluation and a Github based checkout for development.

## Overview

### What this gives you
1. A basically configured CentOS OS layer
2. A MariaDB backed MySQL FR database with some example data
3. A bootstrapped FR instance that you can jump into and start poking around
4. A ready to go FR development environment (incl Grails etc) from the FR repository on Github tracking the master branch so you can create and test your own themes (which makes use of the same database as the WAR deployment)

### What this doesn't give you
1. Anything to do with hardening your OS, database, passwords, security tokens etc. You don't want to put this on a publicly accessible box.
1. Anything to do with a Shibboleth SP. Local accounts only for this. However adding an SP to the Apache configuration and changing the FR configuration to utilise it is easy enough and left as an exercise for you :). You don't want to put this on a publicly accessible box.
2. Anything to do with SSL. HTTP only man! You don't want to put this on a publicly accessible box.
3. HTTP only man! You don't want to put this on a publicly accessible box.

## FR2 Installation

### Requirements
1. A **NEW** CentOS 6.5 based VM. This can be via Vagrant/Virtualbox or any VM provider you may have in your environment.
1. A known IP address for the machine which can accept HTTP and SSH connections (Naturally this can be local to your VirtualBox environment but it **must not** be the default NAT IP that VirtualBox provides)
1. The IP must be resolveable to some **{dns-entry}**. It is fine if this only 'resolves' on your local machine via */etc/hosts* or DNSMasq which is my prefered poison if you don't have wider DNS access. Something like *fr.dev* is suitable if you are only running locally. Where you see **{dns-entry}** below and in configuration files replace it with your actual server address.
1. The ability to SSH to the VM as the ROOT user *unimpeded*. To make this as painless as possible I recommend using your default SSH public key for this purpose on the remote VM. Naturally you can do more advanced things in your local ~/.ssh/config file. The point is running "ssh root@{dns-entry}" should log you into the VM as root without restrictions or **prompts**.
1. Ansible! via the [Ansible Install](http://docs.ansible.com/intro_installation.html) guide. We run this directly on our OSX machines but you can run it from anywhere you care to install it (and can SSH to your FR machine as described above).

   Once installed you should be able to run the following command and get a similar result:
   
        ansible {dns-entry} -m ping
		{dns-entry} | success >> {
		    "changed": false, 
		    "ping": "pong"
		}


### Don't....
1. Run this against an existing VM. **You might find it destructive**.
1. Make this VM publicly available.

### Installation process
Undertake the following steps from the installer directory:

1. `cp federationregistry.dist federationregistry`
2. edit federationregistry to suit your local environment
1. `cp group_vars/fr-servers.yml.dist group_vars/fr-servers.yml`
2. edit group_vars/fr-servers.yml to suit your local environment
1. execute `ansible-playbook -i federationregistry deploy.yml`
2. Wait for **at least 5 minutes** once the above command finishes execution to ensure Tomcat is fully spun up then....
2. execute `ansible-playbook -i federationregistry bootstrap.yml`

These should complete without error. Append -vvvv to see full debug output if you're having difficulty.

Once the above complete:

3. Access http://{dns-entry}/federationregistry in your web browser. Click Login and choose 'Fred Bloggs'
4. You should now be within the FR dashboard and you can view some basic subscriber information.
5. Get Administrative access for 'Fred Bloggs'
	1. execute `ssh root@{dns-entry}`
	2. in the ssh session execute `mysql` 
	3. run the mysql command 'use federationregistry_exampledb; insert into role_subjects (role_id, subject_base_id) values (1, 2);
	4. Refresh your browser, 'Fred Bloggs' is now a super administrator and can assign those rights to others via the web interface.
6. Edit both of the `fr-config.groovy` files and set 'aaf.fr.bootstrap=false'
6. You're done!.

## War vs Developer mode
The spun up image will support both modes of evaluation FR.

By default the WAR version is running.

Within a root ssh session:

1. To stop/start the war version: `service federationregistry stop`
1. To start/stop the development version: `cd /opt/federationregistry-repository/app/federationregistry; grails run-app`

Only one instance can be running at a time.

## FAQ

### Could I use this as a basis for a secure installation?
Yes, but review everything!. You'll want to turn some things off and turn other things on. The [Ansible Documentation](http://docs.ansible.com/) will be a great help along with your local security policies, OS/Database hardening practices and SSL certificates etc.