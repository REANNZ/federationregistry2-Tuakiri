# Federation Registry Commit Guidline
Ver 1 - Bradley Beddoes
14/5/2012

## Overview
This set of guidelines is intended to help both the developer and reviewers of changes determine reasonable commit messages. Often when working with the code, you forget that not everyone is as familiar with the problem and/or fix as you are. Often the next person in the code doesn't understand what or why something is done so they quickly look at commit messages. Unless these messages are clear it will be difficult to understand the relevance of a given change and how future changes may impact previous decisions.

This guideline does not cover the testing of the changes, or the technical criteria for accepting a patch.

By following these guidelines we will have a better record of the problems and solutions made over the course of development. It will also help establish a clear provenance of all of the code and changes.

It is expected that this document will be updated over time where better practices are found.

Thanks to http://www.openembedded.org/wiki/Commit_Patch_Message_Guidelines from whom the original version of this was heavily adapted.

## History
As part oF FR 2 we've made a number of changes to allow others to introduce plugins and core code more readily.

One key part of making this happen smoothly is the move to the aaf.fr namespace for all core code so other federations can now create plugin code in
a <federation>.fr namespace. Code outside of aaf.fr will continue to be maintained by the respective federations. Changes within aaf.fr will
continue to be supported by the AAF (or core FR developers if that spreads outside the AAF) moving forward.

The second part of this is making heavier use of Git.

## Commit Messages
Up to and including early FR 2 beta releases development has largely been undertaken by bradleybeddoes@075cb41f855c738ed08899d5df70f63e01791d1f, as such I am terribly guilty of doing 'git commit -m <one liner>' in commit messages which is hardly best practice so I need to wrap myself on the knuckles for that one.

For all commits going forward hoever the following general rules will apply, those being that there is a single line short log or summary of the change, then a reference to related issue in tracking (if available) and then the more detailed long log.

The single short log message indicates what needed to be changed. It should begin with an indicator as to the primary item changed by this work (indicators are the major areas with FR e.g: branding, app, foundation, workflow etc)  followed by a short summary of the change. The single short log message is analogous to the git "commit summary". While no maximum line length is specified by this policy, it is suggested that it remains under 78 characters wherever possible.

Optionally, you may include pointers to defects this change corrects. As the project utilises Github issue tracking a reference such a #XX is suitable. Supported keywords: close, closes, closed, fixes, fixed and references.

You must then have a full description of the change. Specifying the intent of your change and if necessary how the change resolves the issue.
As mentioned above this is intended to answer the "what were you thinking" questions down the road and to know what other impacts are likely to result of the change needs to be reverted. It also allows for better solutions if someone comes along later and agrees with paragraph 1 (the problem statement) and either disagrees with paragraph 2 (the design) or paragraph 3 (the implementation).

FORMAT:
    <indicator>: Short log / Statement of what needed to be changed.
      
    Optional pointer to issue tracking.
      
    Long log / The intent of your change.
      
    (Optional, if it's not clear from above) how your change resolves the
    issues in the first part.

EXAMPLE:
    branding: Corrects production fault relating to image in email layout.

    Closes #31.

    After several revisions using inbuilt grails resources links the email template
    is still causing 500 errors in production. For some reason the underlying code
    appends a / to the beginning of the complete URL causing it to throw an exception
    when passed to the Java URL class. This appears to be a known fault in Grails 2.0.3

    This revision directly references the serverURL configured in fr-config.groovy to eliminate
    reliance on resources subsystem and thus eliinate the error while still make the img src URL
    dynamic to the deployed environment.

## Importing from elsewhere
If you are importing work from somewhere else, such as a cherry-pick from another repository the minimum patch header or commit message is not enough. It does not clearly establish the provenance of the code.

The following specifies the additional guidelines required for importing changes from elsewhere.

By default you should keep the original author's summary and description. If the original change that was imported does not have a summary and/or commit message from the original author, it is still your responsibility to add the summary and commit message to the patch header. Just as if you wrote the code, you should be able to clearly explain what the change does. It is also necessary to document the original author of the change. You should indicate the original author by simply stating "written by" or "posted to the ... mailing list by".

It is also required that the origin of the work be fully documented. The origin should be specified as part of the commit message in a way that an average developer would be able to track down the original code. URLs should reference original authoritative sources and not mirrors.

If changes were required to resolve conflicts, they should be documented as well. When incorporating a commit or patch from an external source, changes to the functionality not related to resolving conflicts should be made in a second commit or patch. This preserves the original external commit, and makes the modifications clearly visible, and prevents confusion over the author of the code.

EXAMPLE:
    branding: Corrects production fault relating to image in email layout.

    Closes #31.

    After several revisions using inbuilt grails resources links the email template
    is still causing 500 errors in production. For some reason the underlying code
    appends a / to the beginning of the complete URL causing it to throw an exception
    when passed to the Java URL class. This appears to be a known fault in Grails 2.0.3

    This revision directly references the serverURL configured in fr-config.groovy to eliminate
    reliance on resources subsystem and thus eliinate the error while still make the img src URL
    dynamic to the deployed environment.

    The patch was imported from Github (git://github.com/bradleybeddoes/federationregistry.git) 
    as of commit id 905a050af68c5f52773d81736a94f81ba119c54a

    A previous change in core modified the bootstrap version causing conflict. The conflict was 
    resolved by preserving the bootstrap version from core.

## Common Errors
Don't simply translate your change into English for a commit log. The log "Change compare from zero to one" is bad because it describes only the code change in the patch; we can see that from reading the patch itself. Let the code tell the story of the mechanics of the change (as much as possible), and let your comment tell the other details -- i.e. what the problem was, how it manifested itself (symptoms), and if necessary, the justification for why the fix was done in manner that it was. In other words, the long commit message must describe *why* the change was needed (instead of what has changed).

Don't have one huge patch, split your change into logical subparts. It's easier to track down problems afterward using tools such as git bisect. It also makes it easy for people to cherry-pick changes into things like stable branches.

Don't repeat your short log in the long log. If you really really don't have anything new and informational to add in as a long log, then don't put a long log at all. This should be uncommon -- i.e. the only acceptable cases for no long log would be something like "Documentation/README: Fix spelling mistakes".

Don't use links to temporary resources like pastebin and friends. The commit message may be read long after this resource timed out.

Always use the most significant ramification of the change in the words of your subject/shortlog. For example, don't say "fix compile warning in foo" when the compiler warning was really telling us that we were dereferencing complete garbage off in the weeds that could in theory cause an OOPS under some circumstances. When people are choosing commits for backports to stable or distro kernels, the shortlog will be what they use for an initial sorting selection. If they see "Fix possible OOPS in...." then these people will look closer, whereas they most likely will skip over simple warning fixes.

