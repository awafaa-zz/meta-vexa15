inherit kernel

LICENSE = "GPLv2"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/linux-vexa15_AEL3.3/${MACHINE}"

DESCRIPTION = "AEL 3.3 kernel"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/maz/arm-platforms.git;branch=ael-12.03.00;protocol=git \
	   file://defconfig \
	  "

SRCREV = "${AUTOREV}"
PV = "AEL3.3.0+git${SRCPV}"

PR = "r0"

COMPATIBLE_MACHINE = "vexa15"

S = "${WORKDIR}/git"

