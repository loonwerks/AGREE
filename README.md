# Assume-Guarantee REasoning Environment (AGREE)

The Assume Guarantee REasoning Environment (AGREE) is a compositional,
assume-guarantee-style model checker for [AADL](https://aadl.info)
models. It is compositional in that it attempts to prove properties
about one layer of the architecture using properties allocated to
subcomponents. The composition is performed in terms of assumptions
and guarantees that are provided for each component. Assumptions
describe the expectations the component has on the environment, while
guarantees describe bounds on the behavior of the component. AGREE is
integrated into the [OSATE](https://osate.org) AADL model development
environment and uses k-induction as the underlying algorithm for the
model checking.

## User Guide

The user's guide for AGREE, including description of the plug-in for
OSATE, assume-guarantee concepts, specification language reference
manual, and examples is contained in the in-tool help.  The source
code for the help documentation is contained in the
com.rockwellcollins.atc.agree.doc project.

## Development Guide

TBD

### Branches and Tags

The AGREE tool development occurs on mainly on the **master** branch
of this repository.  The 'master' branch contains the releases and the
HEAD of this branch should contain the latest release.  Individual
development efforts should be completed on branches from the master
branch and then merged back into the master via a **pull request**
including informal peer review.

It is the general policy to consider releases as final; no updates to
releases are made.  Instead all updates are made to the master branch
and incorporated into the next release.

Occasionally it is desirable to create additional special-purpose
branches for responding to issue/bug reports.  These should be made
from a branch from the release point and, if possible, merged onto the
master branch prior to completion of the next release cycle.  If such
a branch spans a release, the developer should be merge the updated
master branch onto the special-purpose branch to capture the updated
versioning information and to avoid conflicts merging back into the
develop branch.

Tags are typically reserved for releases, but may be used to mark
special points in the development process.

### Continuous Integration / Continuous Deployment

AGREE continuous integration is carried out at
[Travis-CI](https://www.travis.org).  Daily builds of developmental
snapshots are uploaded to the GitHub
[AGREE releases](https://github.com/loonwerks/AGREE/releases) page.
Additionally, Travis-CI conducts builds and tests in response to pull
requests against the AGREE repository.

### Releases

The process for a release updates master branch with the release
version number, commits the master branch version number updates,
builds the release version, updates the version numbering to the new
development snapshot numbers, and finally commits the master branch
with the new development version numbering.  To accomplish the release
preparation, the AGREE release process applies the [Tycho Release
Workflow](https://wiki.eclipse.org/Tycho/Release_Workflow) to help
automate the process.  The steps in the release workflow are as
follows:

1. Prepare for the release by running the release workflow plugin:

   `mvn release:prepare -DreleaseVersion=x.x.x
   -DdevelopmentVersion=y.y.y-SNAPSHOT`

   where 'x.x.x' is replaced withe the desired version number for the
   release and 'y.y.y' is replaced with the desired version number for
   the next development cycle.  The SNAPSHOT qualifier must be
   appended to the development version as shown to facilitate the
   CI/CD automation of interim development builds.

   During this process maven will ask for the tag to applied to the
   release. Following convention, the form of the tag should be
   'x.y.z-release'. (For example 1.1.0-release.)

   Also during the process an apparent bug in the Tycho release plugin
   will likely be encountered due to the apparent failure to be able
   to resolve the new version of the target. DON'T PANIC! If this
   occurs, edit the parent-level pom.xml and update the version of the
   target at approximately line 155, updating it to the new release
   version. Then resume the release process with the same command line
   as before.

   And, the same thing will happen again as it updates the manifests
   for the new development version.  Again edit the parent-level
   pom.xml and update the version of the target at approximately line
   155, updating to the new development version.  Then, again, resume
   the release process with the same command line as before.

1. Perform the release:

   `mvn release:perform -Dgoals="clean verify"`

1. Commit the release binaries into the companion releases repository at
   git@github.com:loonwerks/AGREE-releases.git:

   `git add ...`

   `git commit -m "Commit release binaries into repository"`

1. Finally, push the update develop branch to the origin repository:

   `git push`

Note that the previous instructions assume that the developer has set
up appropriate ssh keys such that interactive query for authentication
is necessary.  If this is not done, the `release:prepare` and
`release:perform` steps will apparently just hang when such
interaction is required as they are in fact headless operations.
Alternatively to setting up ssh keys, the developer may add necessary
authenication information to the maven settings as described in [Tycho
Release Workflow -- Configure the
SCM](https://wiki.eclipse.org/Tycho/Release_Workflow#Configure_the_SCM).
