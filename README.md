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

### AGREE Branching Strategy

Development in AGREE takes place on two main branches: 'master' and
'stable.'  The 'master' branch is the leading edge of development of
AGREE and it maintains compatibility with the OSATE master branch.
Developers are encouraged to create branches from the AGREE master
branch to implement new features, address issues, or fix bugs.  Once
these branch developments are ready, they may be merged into the 
master branch.  ATO be ready for merging onto the master branch,
work must not break build or regression testing, must be deemed
ready for use by the developmental users, and should have a suite
of test cases.

The AGREE 'stable' branch contains the releases of AGREE (since
AGREE 2.7.2).  At time for releases, the progress on the master
branch is merged onto the stable branch.  At this point progress
on the master must be ready for release to the public user community
vi a marked release in GitHub.

### Setting-Up a Development Environment

The AGREE development environement is essentially the same as that for
OSATE with the addition of the AGREE source projects.  It is
recommended to use the [setup
instructions](https://osate.org/setup-development.html) provided by
the OSATE team and then import the AGREE projects.  Note that as OSATE
does not guarantee backward compatibility for minor version updates,
it is important to match the version of OSATE to the version of AGREE.
Presently, the AGREE master branch is being developed with respect to
the OSATE master branch.

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
special points in the development process (i.e. snapshot tags).

## Continuous Integration / Continuous Deployment
The CI/CD pipeline is carried out via GitHub actions. There are three different workflows defined. They are as follows:
   1. "Build and Test Project" (defined in [.github/workflows/build_and_test.yml](https://github.com/loonwerks/AGREE/blob/main/.github/workflows/build_and_test.yml))
      - Trigger(s): 
         * push is made to master 
         * pull request is opened, reopened, or editted
      - Job(s):
         * *verify*: verifies that the project builds without errors and all tests pass by running the command `mvn clean verify` 
   2. "Push snapshot to Github Pages" (defined in [.github/workflows/snapshot.yml](https://github.com/loonwerks/AGREE/blob/main/.github/workflows/snapshot.yml))
      - Trigger(s):
         * "Build and Test Project" successfully completes on master branch
      - Job(s):
         * *verify*: verifies that the project builds without errors all tests pass, and build is a snapshot by running the command `mvn verify -Pbuild-snapshot` 
         * *publish*: publishes the p2 repo of the current build to [AGREE-Updates/snapshots/x.x.x.yyyyMMddHHmm](https://github.com/loonwerks/AGREE-Updates)
         * *find_snapshots_to_delete*: finds snapshots that should be deleted; the repo only keeps the 10 latest snapshots and those that have a tag
         * *delete_snapshots*: deletes the snapshots indicated in *find_snapshots_to_delete*
   3. "Push and publish release to GitHub" (defined in [.github/workflows/release.yml](https://github.com/loonwerks/AGREE/blob/main/.github/workflows/release.yml))
      - Trigger(s):
         * a tag was pushed
      - Job(s): 
         * *parse_tag*: parses the tag "x.x.x-RELEASE" into "x.x.x" and "RELEASE"
         * *verify*: if the suffix of tag is "-RELEASE", verifies that the project builds without errors all tests pass, and build is a release by running the command `mvn verify -Pbuild-release` 
         * *parse_version*: parses the version from the built p2 repo
         * *publish*: if the tag and version of the built p2 repo match, publishes the p2 repo of the current build to [AGREE-Updates/releases/x.x.x](https://github.com/loonwerks/AGREE-Updates)
         * *release*: if the tag and version of the built p2 repo match, generates a release on the AGREE repo and attaches the p2 repo as an artifact

**Important Details about Snapshot Tags** 
- The tag should be in the format x.x.x.yyyyMMddHHmm-SNAPSHOT where x is the major version number, y is the minor version number, and z is the patch version number.
- Make the tag by using the following git commands:
   1. `git tag x.x.x.yyyyMMddHHmm-SNAPSHOT`
   2. `git push origin x.x.x.yyyyMMddHHmm-SNAPSHOT`
- The tag number should match an already existing snapshot.

**Important Details about Release Tags** 
- The tag should be in the format x.x.x-RELEASE where x is the major version number, y is the minor version number, and z is the patch version number.
- Make the tag by using the following git commands:
   1. `git tag x.x.x-RELEASE`
   2. `git push origin x.x.x-RELEASE`
- The version number of com.rockwellcollins.atc.agree needs to match the tag number when triggering a release.
