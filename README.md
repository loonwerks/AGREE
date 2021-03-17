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

### Setting-Up a Development Environment

The AGREE development environement is essentially the same as that for
OSATE with the addition of the AGREE source projects.  It is
recommended to use the [setup
instructions](https://osate.org/setup-development.html) provided by
the OSATE team and then import the AGREE projects.  Note that as OSATE
does not guarantee backward compatibility for minor version updates,
it is important to match the version of OSATE to the version of AGREE.
Presently, AGREE is being developed with respect to OSATE version
2.6.x on Eclipse 2019-09.

### Xtext Artifacts

AGREE uses Xtext to generate its language API and integration with
OSATE as an annex language.  While facilities to run the Xtext work
flow as a build step under the Tycho/Maven build tool, this is not
presently done.  Since AGREE uses the
[Ecore](https://wiki.eclipse.org/Ecore) meta model and classes,
successful generation of Xtext artifacts under Tycho/Maven is not
presently possible due the [Xtext Core Issue
41](https://github.com/eclipse/xtext-core/issues/41).  As a
workaround, generated Xtext artifacts are stored in configuration
management in this repository.  Further, the Xtext artifacts are
re-generated manually by application of the MWE workflow
[GenerateAgree.mwe2](https://github.com/loonwerks/AGREE/blob/master/com.rockwellcollins.atc.agree/src/com/rockwellcollins/atc/agree/GenerateAgree.mwe2)
from within an OSATE/Eclipse development environment.

### Building the Documentation

The documentation source code is maintained in
[Markdown](https://en.wikipedia.org/wiki/Markdown) from which HTML,
PDF, and DOCX output is generated.  HTML output includes indexes for
application as in-tool help files.  As there is no formal standard for
Markdown, the [Pandoc
variant](https://rmarkdown.rstudio.com/authoring_pandoc_markdown.html%23raw-tex)
is used.  Building the documentation output requires
[Pandoc](https://pandoc.org),
[pandoc-crossref](https://github.com/lierdakil/pandoc-crossref), and a
LaTeX distribution.  An Apache ANT build script provided in the AGREE
distribution can be used to automate the documentation build provided
the required build tools have been installed.  Since installation of
the LaTeX distribution and Pandoc are intensive, documentation build may
is enabled by specification of the system property maven.agree.docs.build
on the command line.  For example

`mvn -Dmaven.agree.docs.build=true clean verify`

Pre-built generated documentation artifacts (HTML, PDF, and DOCX) are
maintained in this repository such that this build step may be skipped.

### Building in Tycho-Maven

[Tycho](https://www.eclipse.org/tycho/) is a collection of
[Maven](https://maven.apache.org/) plugins that map the Eclipse builds
into Maven.  Principally, it maps the OSGi dependencies into maven
dependencies.  Accordingly, the contents of the AGREE plugin manifests
are combined with the pom.xml configurations to produce a unified
builod environment.

To build and package AGREE as an Eclipse P2 repository apply the command line

`mvn clean verify`

This command deletes generated files (except Xtext artifacts and
documenation outputs), compiles, runs tests and completes the
packaging.

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

Branch names should be chosen to describe the activity to be taken on
the branch.  For example, to develop a new feature 'A', the branch
might be named 'develop-feature-A.'  Or to fix an issue recorded in
the issue base the branch might be named 'fix-issue-x' where x is the
sequence number assigned to the issue.

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
   release. Release tags shall be of the form 'x.y.z-RELEASE' where
   `x` is the major version number, `y` is the minor version number,
   and `z` is the patch version number.  Ordinarily, AGREE would
   follow the [Semantic Versioning](https://semver.org/) method.
   However, OSATE does not guarantee that micro versions have backward
   compatible API changes and AGREE must follow this.

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

1. Ordinarily one would expect to actually perform the release using
   the customary `mvn release:perform -Dgoals="clean verify"` command.
   However, this appears to run into difficulty fetching the appropriate
   tag to build.  Instead do the following:

   `git checkout x.x.x-RELEASE`
   `mvn clean verify`

   where `x.x.x` is the version number to be released.

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
