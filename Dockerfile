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


