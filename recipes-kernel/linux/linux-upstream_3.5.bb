inherit kernel

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/linux-upstream-3.5/${MACHINE}"

DESCRIPTION = "Upstream 3.5 kernel"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git;protocol=git \
           file://defconfig \
	   "

SRCREV = "${AUTOREV}"
#SRCREV = "2150f72fe35397cc6d6ce39866bd0462cfbcc916"

PV = "3.5+git${SRCPV}"

PR = "r0"

COMPATIBLE_MACHINE = "qemuarmv7a"

S = "${WORKDIR}/git"

