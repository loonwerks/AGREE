# Copyright (c) 2021, Collins Aerospace.
# Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this data, 
# including any software or models in source or binary form, as well as any drawings, specifications, 
# and documentation (collectively &quot;the Data&quot;), to deal in the Data without restriction, including
# without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
# and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so, 
# subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all copies or 
# substantial portions of the Data.
#
# THE DATA IS PROVIDED &quot;AS IS&quot;, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
# LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
# IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE 
# FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
# ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.

ARG BASE_IMG=maven:3.6.0-jdk-8
#ARG BASE_IMG=ubuntu:18.04
FROM $BASE_IMG

ARG UID
ARG UNAME
ARG GID
ARG GROUP

RUN groupadd -fg "${GID}" "${GROUP}" \
  && groupmod -g "${GID}" "${GROUP}" \
  && useradd -u "${UID}" -g "${GID}" "${UNAME}" \
  && passwd -d "${UNAME}" \
  && mkdir "/home/${UNAME}" \
  && chown -R "${UNAME}":"${GROUP}" "/home/${UNAME}" \
  && chmod -R ug+rw "/home/${UNAME}"

VOLUME /home/${UNAME}

RUN apt-get -q update \
  && apt-get -y -q install curl sudo texlive-xetex texlive-luatex vim wget \
  && apt-get clean autoclean \
  && apt-get autoremove --purge --yes \
  && rm -rf /var/lib/apt/lists/*

RUN wget https://github.com/jgm/pandoc/releases/download/2.12/pandoc-2.12-1-amd64.deb \
  && sudo dpkg -i pandoc-2.12-1-amd64.deb 

WORKDIR /bin
	
RUN wget -O - https://github.com/lierdakil/pandoc-crossref/releases/download/v0.3.10.0/pandoc-crossref-Linux.tar.xz | tar -xJ

WORKDIR /host
